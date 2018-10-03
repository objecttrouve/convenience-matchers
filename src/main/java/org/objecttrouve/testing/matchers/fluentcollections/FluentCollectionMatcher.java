/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentcollections;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.TypeSafeMatcher;
import org.objecttrouve.testing.matchers.api.ScorableMatcher;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import static java.lang.System.arraycopy;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;

public class FluentCollectionMatcher<X, C extends Collection<X>> extends TypeSafeMatcher<C> {

    // Config.
    private final Settings<X> settings = new Settings<>();

    @SuppressWarnings("unchecked")

    // Matching
    private X[] actual = (X[]) new Object[0];
    private double[][] matchMatrix = new double[0][0];
    private final Set<Integer> matchedExpected = new HashSet<>();
    private final Set<Integer> matchedActual = new HashSet<>();
    private final Set<Integer> unsorted = new HashSet<>();
    private final Set<Integer> unordered = new HashSet<>();
    private final Set<Integer> duplicates =new HashSet<>();
    private final Set<Integer> unwanted = new HashSet<>();
    private final List<Finding> findings = new LinkedList<>();


    public FluentCollectionMatcher(final Class<X> klass) {
        if (klass == null) {
            throw new IllegalArgumentException("Argument 'klass' must not be null.");
        }
        settings.klass = klass;
    }

    @Override
    protected boolean matchesSafely(final C collection) {

        reset();
        validateSetup();
        if (collection == null) {
            findings.add(new Finding("Actual collection was null."));
            return false;
        }

        //noinspection unchecked,ConstantConditions
        actual = (X[]) collection.toArray();
        matchMatrix = new double[settings.expectations.length][actual.length];

        loop(this::match);
        loop(this::aggregate);
        assess();


        return findings.isEmpty();

    }

    private void validateSetup() {
        if (settings.expectedSize >= 0 && settings.expectedSize < settings.expectations.length) {
            throw new IllegalArgumentException(
                "Invalid setup. " +
                    "Argument passed to ofSize() " +
                    "is less than expected items specified."
            );
        }
        if (settings.mustNotHaveUnexpectedItems && settings.expectedSize >= 0 && settings.expectations.length != settings.expectedSize) {
            throw new IllegalArgumentException(
                "Invalid setup. " +
                    "Argument passed to ofSize() " +
                    "must match number of expected items " +
                    "when exactly() is set."
            );
        }
    }

    private void assess() {

        if (settings.expectedSize >= 0 && settings.expectedSize != actual.length) {
            findings.add(new Finding("Size mismatch. Expected: " + settings.expectedSize + ". Actual was: " + actual.length + "."));
        }
        if (matchedExpected.size() < settings.expectations.length) {
            findings.add(new Finding("Not all expectations were fulfilled."));
        }
        if (settings.mustNotHaveUnexpectedItems && actual.length > settings.expectations.length) {
            findings.add(new Finding("Unexpected actual items."));
        }
        if (matchedExpected.size() > matchedActual.size()) {
            findings.add(new Finding("Could not find matches for all expectations."));
        }
        if (settings.ordered) {
            int matchedInOrder = 0;
            for (int i = 0, j = 0; i < settings.expectations.length && j < actual.length; i++, j++) {
                if (matchMatrix[i][j] == 1) {
                    matchedInOrder++;
                } else if (!settings.mustNotHaveUnexpectedItems) {
                    unordered.add(j);
                    i--;
                } else {
                    unordered.add(j);
                    //break;
                }
            }
            if (matchedInOrder < settings.expectations.length) {
                findings.add(new Finding("Items did not appear in the expected order."));
            }
        }
        if (settings.sorted && actual.length > 1) {
            for (int k = 0, l = 1; l < actual.length; k++, l++) {
                final String description = "Collection is not sorted.";
                if (settings.comparator == null) {
                    final Comparable x1 = (Comparable) actual[k];
                    final Comparable x2 = (Comparable) actual[l];
                    //noinspection unchecked
                    if (x1.compareTo(x2) > 0) {
                        unsorted.add(l);
                        findings.add(new Finding(description));
                    }
                } else {
                    if (settings.comparator.compare(actual[k], actual[l]) > 0) {
                        unsorted.add(l);
                        findings.add(new Finding(description));
                    }
                }
            }
        }
        if (settings.unique && actual.length > 1) {
            for (int k = 0; k < actual.length; k++) {
                for (int l = k + 1; l < actual.length; l++) {
                    final X x1 = actual[k];
                    final X x2 = actual[l];
                    if (settings.equator.test(x1, x2)) {
                        this.duplicates.add(k);
                        this.duplicates.add(l);
                    }
                }
            }
            if (!duplicates.isEmpty()) {
                findings.add(new Finding("Detected duplicates."));
            }
        }
    }

    private void reset() {
        this.unwanted.clear();
        this.unordered.clear();
        this.unsorted.clear();
        this.duplicates.clear();
        this.findings.clear();
        this.matchedActual.clear();
        this.matchedExpected.clear();
        this.matchMatrix = new double[0][0];
    }


    private void loop(final BiConsumer<Integer, Integer> action) {

        for (int i = 0; i < settings.expectations.length; i++) {
            for (int j = 0; j < actual.length; j++) {
                action.accept(i, j);
            }
        }
    }

    private void match(final int i, final int j) {
        final Matcher<X> expectation = settings.expectations[i];
        if (expectation.matches(actual[j])) {
            matchMatrix[i][j] = 1;
        } else {
            if (expectation instanceof ScorableMatcher) {
                matchMatrix[i][j] = ((ScorableMatcher) expectation).getScore();
            } else {
                matchMatrix[i][j] = 0;
            }
        }
    }

    private void aggregate(final int i, final int j) {
        if (matchMatrix[i][j] == 1) {
            matchedExpected.add(i);
            matchedActual.add(j);
        }
    }


    @Override
    public void describeTo(final Description description) {

        final Prose<X> prose = new Prose<>();
        prose.describeExpectations(settings, description::appendText);

    }

    private static class ScoredMismatch implements Comparable<ScoredMismatch> {
        private final int actual;
        private final int matcher;
        private final double score;

        private ScoredMismatch(final int actual, final int matcher, final double score) {
            this.actual = actual;
            this.matcher = matcher;
            this.score = score;
        }

        double getScore() {
            return score;
        }

        public int getActual() {
            return actual;
        }

        public int getMatcher() {
            return matcher;
        }

        @Override
        public int compareTo(final ScoredMismatch o) {
            return Comparator
                .comparingDouble(ScoredMismatch::getScore).reversed()
                .thenComparing(ScoredMismatch::getMatcher)
                .thenComparing(ScoredMismatch::getActual)
                .compare(this, o);
        }
    }

    @Override
    protected void describeMismatchSafely(final C item, final Description mismatchDescription) {

        final List<SelfDescribing> fs = findings.stream()
            .map(Finding::getDescription)
            .map(s -> (SelfDescribing) description1 -> description1.appendValue(s))
            .collect(toList());
        mismatchDescription.appendList("Findings:\n", "\n", "\n", fs);

        final int longestActual = Arrays.stream(actual).map(Objects::toString).mapToInt(String::length).max().orElse(1);
        final List<ItemResult> itemResults = getItemResults();

        final Prose prose = new Prose();
        mismatchDescription.appendText("\n");
        //noinspection unchecked
        mismatchDescription.appendText(itemResults.stream()
            .map(result -> prose.line(result, actual.length, longestActual))
            .collect(joining("\n")
            ));
        mismatchDescription.appendText("\n");


        super.describeMismatchSafely(item, mismatchDescription);
    }

    List<ItemResult> getItemResults() {
        final List<ItemResult> itemResults = new LinkedList<>();
        for (int j = 0; j < actual.length; j++) {
            if (matchedActual.contains(j)) {
                itemResults.add(ItemResult.builder(actual[j])
                    .matched(true)
                    .withIndex(j)
                    .breakingItemOrder(this.unordered.contains(j))
                    .duplicate(this.duplicates.contains(j))
                    .breakingSortOrder(this.unsorted.contains(j))
                    .build());
            } else if (settings.mustNotHaveUnexpectedItems && settings.ordered) {
                this.unwanted.add(j);
                final ItemResult.Builder<X> builder = ItemResult.builder(actual[j]);
                if (j < settings.expectations.length) {
                    builder
                        .withMatchers(singletonList(settings.expectations[j]));
                }
                itemResults.add(builder
                    .matched(false)
                    .withIndex(j)
                    .breakingItemOrder(true)
                    .duplicate(this.duplicates.contains(j))
                    .unwanted(true)
                    .breakingSortOrder(this.unsorted.contains(j))
                    .build());
            } else {
                if (settings.mustNotHaveUnexpectedItems){
                    this.unwanted.add(j);
                }
                final Set<ScoredMismatch> unmatched = new TreeSet<>();
                for (int i = 0; i < settings.expectations.length; i++) {
                    final double score = matchMatrix[i][j];
                    if (score != 1.0) {
                        unmatched.add(new ScoredMismatch(j, i, score));
                    }
                }
                itemResults.add(ItemResult.builder(actual[j])
                    .matched(false)
                    .withIndex(j)
                    .withMatchers(unmatched.stream().map(sm -> settings.expectations[sm.matcher]).collect(toList()))
                    .breakingItemOrder(this.unordered.contains(j))
                    .duplicate(this.duplicates.contains(j))
                    .breakingSortOrder(this.unsorted.contains(j))
                    .unwanted(this.unwanted.contains(j))
                    .build());
            }
        }
        return itemResults;
    }

    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> ofSize(final int expectedSize) {
        if (expectedSize < 0) {
            throw new IllegalArgumentException("Size must not be negative.");
        }
        this.settings.expectedSize = expectedSize;
        return this;
    }


    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> sorted() {
        if (!Comparable.class.isAssignableFrom(settings.klass)) {
            final String msg = "" +
                "Class " + settings.klass.getSimpleName() + " does not implement " + Comparable.class.getSimpleName() + ". " +
                "Either implement that interface or " +
                "use " + FluentCollectionMatcher.class.getSimpleName() + " .sorted(java.util.Comparator<X>). ";
            throw new IllegalArgumentException(msg);
        }
        this.settings.sorted = true;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> sorted(final Comparator<X> comparator) {
        this.settings.comparator = comparator;
        this.settings.sorted = true;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> ordered() {
        settings.ordered = true;
        return this;
    }


    @SuppressWarnings("WeakerAccess")
    public final FluentCollectionMatcher<X, C> withItemsMatching(final Matcher... expectedItemMatchers) {
        if (expectedItemMatchers == null) {
            throw new IllegalArgumentException("Item expectations must not be null.");
        }
        addToExpectations(expectedItemMatchers);
        return this;
    }

    @SafeVarargs
    @SuppressWarnings("WeakerAccess")
    public final FluentCollectionMatcher<X, C> withItems(final X... expectedItems) {
        if (expectedItems == null) {
            throw new IllegalArgumentException("Item expectations must not be null.");
        }
        final int nrOfExistingExpectations = this.settings.expectations.length;
        expandExpectationsArray(expectedItems.length);
        for (int i = nrOfExistingExpectations, j = 0; i < settings.expectations.length && j < expectedItems.length; i++, j++) {
            settings.expectations[i] = equalTo(expectedItems[j]);
        }
        return this;
    }


    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> exactly() {
        this.settings.mustNotHaveUnexpectedItems = true;
        return this;
    }


    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> unique() {
        this.settings.unique = true;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> unique(final BiPredicate<X, X> equator) {
        this.settings.unique = true;
        this.settings.equator = equator;
        return this;
    }

    private void addToExpectations(final Matcher[] matchers) {
        final int nrOfExistingExpectations = this.settings.expectations.length;
        expandExpectationsArray(matchers.length);
        arraycopy(matchers, 0, this.settings.expectations, nrOfExistingExpectations, matchers.length);
    }

    private void expandExpectationsArray(final int nrOfdditionalItems) {
        final Matcher[] array = new Matcher[this.settings.expectations.length + nrOfdditionalItems];
        arraycopy(this.settings.expectations, 0, array, 0, this.settings.expectations.length);
        //noinspection unchecked
        this.settings.expectations = array;
    }

}
