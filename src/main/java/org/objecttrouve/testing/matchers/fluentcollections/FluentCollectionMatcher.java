/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
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
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;

public class FluentCollectionMatcher<X, C extends Collection<X>> extends TypeSafeMatcher<C> {

    // Config.
    private final Class<X> klass;
    private boolean ordered;
    private boolean sorted;
    private Comparator<X> comparator;
    private BiPredicate<X, X> equator = Objects::equals;
    private boolean mustNotHaveUnexpectedItems;
    @SuppressWarnings("unchecked")
    private Matcher<X>[] expectations = new Matcher[0];
    private int expectedSize = -1;
    private boolean unique;
    @SuppressWarnings("unchecked")

    // Matching
    private X[] actual = (X[]) new Object[0];
    private double[][] matchMatrix = new double[0][0];
    private final Set<Integer> matchedExpected = new HashSet<>();
    private final Set<Integer> matchedActual = new HashSet<>();
    private final List<Integer> duplicates = new LinkedList<>();
    private final List<Finding> findings = new LinkedList<>();


    public FluentCollectionMatcher(final Class<X> klass) {
        if (klass == null) {
            throw new IllegalArgumentException("Argument 'klass' must not be null.");
        }
        this.klass = klass;
    }

    @Override
    protected boolean matchesSafely(final C collection) {

        reset();
        validateSetup();
        if (collection == null) {
            findings.add(new Finding("Actual collection was null."));
        }

        //noinspection unchecked,ConstantConditions
        actual = (X[]) collection.toArray();
        matchMatrix = new double[expectations.length][actual.length];

        loop(this::match);
        loop(this::aggregate);
        assess();


        return findings.isEmpty();

    }

    private void validateSetup() {
        if (expectedSize >= 0 && expectedSize < expectations.length) {
            throw new IllegalArgumentException(
                "Invalid setup. " +
                    "Argument passed to ofSize() " +
                    "is less than expected items specified."
            );
        }
        if (mustNotHaveUnexpectedItems && expectedSize >= 0 && expectations.length != expectedSize) {
            throw new IllegalArgumentException(
                "Invalid setup. " +
                    "Argument passed to ofSize() " +
                    "must match number of expected items " +
                    "when exactly() is set."
            );
        }
    }

    private void assess() {

        if (expectedSize >= 0 && expectedSize != actual.length) {
            findings.add(new Finding("Size mismatch. Expected: " + expectedSize + ". Actual was: " + actual.length + "."));
        }
        if (matchedExpected.size() < expectations.length) {
            findings.add(new Finding("Not all expectations were fulfilled."));
        }
        if (mustNotHaveUnexpectedItems && actual.length > expectations.length) {
            findings.add(new Finding("Unexpected actual items."));
        }
        if (matchedExpected.size() > matchedActual.size()) {
            findings.add(new Finding("Could not find matches for all expectations."));
        }
        if (ordered) {
            int matchedInOrder = 0;
            for (int i = 0, j = 0; i < expectations.length && j < actual.length; i++, j++) {
                if (matchMatrix[i][j] == 1) {
                    matchedInOrder++;
                } else if (!mustNotHaveUnexpectedItems) {
                    i--;
                } else {
                    break;
                }
            }
            if (matchedInOrder < expectations.length) {
                findings.add(new Finding("Items did not appear in the expected order."));
            }
        }
        if (sorted && actual.length > 1) {
            for (int k = 0, l = 1; l < actual.length; k++, l++) {
                final String description = "Collection is not sorted.";
                if (comparator == null) {
                    final Comparable x1 = (Comparable) actual[k];
                    final Comparable x2 = (Comparable) actual[l];
                    //noinspection unchecked
                    if (x1.compareTo(x2) > 0) {
                        findings.add(new Finding(description));
                    }
                } else {
                    if (comparator.compare(actual[k], actual[l]) > 0) {
                        findings.add(new Finding(description));
                    }
                }
            }
        }
        if (unique && actual.length > 1){
            for (int k = 0; k <actual.length ; k++) {
                for (int l = k+1; l < actual.length; l++) {
                    final X x1 = actual[k];
                    final X x2 = actual[l];
                    if(equator.test(x1, x2)){
                        this.duplicates.add(k);
                        this.duplicates.add(l);
                    }
                }
            }
            if(!duplicates.isEmpty()){
                findings.add(new Finding("Detected duplicates."));
            }
        }
    }

    private void reset() {
       this.duplicates.clear();
       this.findings.clear();
       this.matchedActual.clear();
       this.matchedExpected.clear();
       this.matchMatrix = new double[0][0];
    }


    private void loop(final BiConsumer<Integer, Integer> action) {

        for (int i = 0; i < expectations.length; i++) {
            for (int j = 0; j < actual.length; j++) {
                action.accept(i, j);
            }
        }
    }

    private void match(final int i, final int j) {
        final Matcher<X> expectation = expectations[i];
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

        description.appendText("a collection with the following properties:\n");
        if(this.expectedSize >= 0){
            description.appendText("- exactly " + expectedSize + " item(s)\n");
        } else {
            description.appendText("- at least " + expectations.length + " matching item(s)\n");
        }
        if(this.sorted){
            description.appendText("- sorted\n");
        }
        if (this.ordered){
            description.appendText("- ordered\n");
        }
        if(this.unique){
            description.appendText("- no duplicates\n");
        }
        description.appendText("\n");

        final List<SelfDescribing> fs = findings.stream()
            .map(Finding::getDescription)
            .map(s -> (SelfDescribing) description1 -> description1.appendValue(s))
            .collect(toList());
        description.appendList("Findings:\n", "\n", "\n", fs);
    }

    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> ofSize(final int expectedSize) {
        if (expectedSize < 0) {
            throw new IllegalArgumentException("Size must not be negative.");
        }
        this.expectedSize = expectedSize;
        return this;
    }


    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> sorted() {
        if (!Comparable.class.isAssignableFrom(klass)) {
            final String msg = "" +
                "Class " + klass.getSimpleName() + " does not implement " + Comparable.class.getSimpleName() + ". " +
                "Either implement that interface or " +
                "use " + FluentCollectionMatcher.class.getSimpleName() + " .sorted(java.util.Comparator<X>). ";
            throw new IllegalArgumentException(msg);
        }
        this.sorted = true;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> sorted(final Comparator<X> comparator) {
        this.comparator = comparator;
        this.sorted = true;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> ordered() {
        ordered = true;
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
        final int nrOfExistingExpectations = this.expectations.length;
        expandExpectationsArray(expectedItems.length);
        for (int i = nrOfExistingExpectations, j = 0; i < expectations.length && j < expectedItems.length; i++, j++) {
            expectations[i] = equalTo(expectedItems[j]);
        }
        return this;
    }


    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> exactly() {
        this.mustNotHaveUnexpectedItems = true;
        return this;
    }


    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> unique() {
        this.unique = true;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public FluentCollectionMatcher<X, C> unique(final BiPredicate<X, X> equator) {
        this.unique = true;
        this.equator = equator;
        return this;
    }

    private void addToExpectations(final Matcher[] matchers) {
        final int nrOfExistingExpectations = this.expectations.length;
        expandExpectationsArray(matchers.length);
        arraycopy(matchers, 0, this.expectations, nrOfExistingExpectations, matchers.length);
    }

    private void expandExpectationsArray(final int nrOfdditionalItems) {
        final Matcher[] array = new Matcher[this.expectations.length + nrOfdditionalItems];
        arraycopy(this.expectations, 0, array, 0, this.expectations.length);
        //noinspection unchecked
        this.expectations = array;
    }

}
