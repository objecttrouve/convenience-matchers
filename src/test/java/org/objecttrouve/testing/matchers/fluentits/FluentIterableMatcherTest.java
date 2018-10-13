/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.objecttrouve.testing.matchers.fluentatts.Attribute;
import org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparingInt;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.anIterableOf;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

public class FluentIterableMatcherTest {

    @Test
    public void matchesSafely__match__empty_expectation__no_requirements__empty_actual() {

        final List<String> strings = emptyList();

        assertThat(strings, is(anIterableOf(String.class)));
    }

    @Test
    public void matchesSafely__mismatch__empty_expectation__no_requirements__null_actual() {

        final List<String> strings = null;

        assertThat(strings, not(is(anIterableOf(String.class))));
    }


    @Test
    public void matchesSafely__mismatch__empty_expectation__no_requirements__null_actual__plain_matchesSafely_call() {

        final List<String> strings = null;

        final boolean matches = anIterableOf(String.class).matches(strings);

        //noinspection ConstantConditions
        assertThat(matches, is(false));
    }


    @Test
    public void matchesSafely__mismatch__empty_expectation__no_requirements__null_actual__empty_IssueResult_list() {

        final List<String> strings = null;

        final List<ItemResult> itemResults = matchResults(strings, anIterableOf(String.class));

        assertThat(itemResults, hasSize(0));
    }

    private static <X> List<ItemResult> matchResults(final List<X> xs, final FluentIterableMatcher<X, Iterable<X>> matcher) {
        matcher.matchesSafely(xs);
        return matcher.getItemResults();
    }

    private static <X> double matchScore(final List<X> xs, final FluentIterableMatcher<X, Iterable<X>> matcher) {
        matcher.matchesSafely(xs);
        return matcher.getScore();
    }


    @Test
    public void matchesSafely__match__empty_expectation__no_requirements__non_empty_actual() {

        final List<String> strings = singletonList("item");

        assertThat(strings, is(anIterableOf(String.class)));
    }

    @Test
    public void matchesSafely__match__null_item() {

        final List<String> strings = singletonList(null);

        assertThat(strings, is(anIterableOf(String.class).withItemsMatching(nullValue())));
    }

    @Test
    public void matchesSafely__mismatch__empty_expectation__exactly__non_empty_actual() {

        final List<String> strings = singletonList("item");

        assertThat(strings, not(is(anIterableOf(String.class).exactly())));
    }

    @Test
    public void matchesSafely__mismatch__empty_expectation__exactly__non_empty_actual__has_ItemResule() {

        final List<String> strings = singletonList("item");

        final List<ItemResult> itemResults = matchResults(strings, anIterableOf(String.class).exactly());

        assertThat(itemResults, hasSize(1));
        final ItemResult itemResult = itemResults.get(0);
        assertThat(itemResult.getActual(), is("item"));
        assertThat(itemResult.getIndex(), is(0));
        assertThat(itemResult.isMatched(), is(false));
        assertThat(itemResult.isBreakingItemOrder(), is(false));
        assertThat(itemResult.isBreakingSortOrder(), is(false));
        assertThat(itemResult.isDuplicate(), is(false));
        assertThat(itemResult.isUnwanted(), is(true));
        assertThat(itemResult.getMismatchedItemMatchers().size(), is(0));

    }


    @Test
    public void matchesSafely__match__matcher_expectation__exactly__non_empty_actual() {

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(containsString("it"))
                .exactly()
        ));
    }

    @Test
    public void matchesSafely__match__matcher_expectation__no_requirements__non_empty_actual() {

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(containsString("it"))
        ));
    }

    @Test
    public void matchesSafely__match__1_matcher_expectation__ofSize_1__1_actual() {

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(containsString("it"))
                .ofSize(1)
        ));
    }

    @Test
    public void matchesSafely__mismatch__1_matcher_expectation__ofSize_2__1_actual() {

        final List<String> strings = singletonList("item");

        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItemsMatching(containsString("it"))
                .ofSize(2)
        )));
    }

    @Test
    public void matchesSafely__mismatch__1_matcher_expectation__ofSize_2__1_actual__has_ItemResult() {

        final List<String> strings = singletonList("item");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(containsString("it"))
                .ofSize(2)
        );

        assertThat(itemResults, hasSize(1));
        final ItemResult itemResult = itemResults.get(0);
        assertThat(itemResult.getActual(), is("item"));
        assertThat(itemResult.getIndex(), is(0));
        assertThat(itemResult.isMatched(), is(true));
        assertThat(itemResult.isBreakingItemOrder(), is(false));
        assertThat(itemResult.isBreakingSortOrder(), is(false));
        assertThat(itemResult.isDuplicate(), is(false));
        assertThat(itemResult.isUnwanted(), is(false));
        assertThat(itemResult.getMismatchedItemMatchers().size(), is(0));
    }

    @Test
    public void matchesSafely__match__1_matcher_expectation__ofSize_1__1_actual__consistent_with__exactly() {

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(containsString("it"))
                .ofSize(1)
                .exactly()
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void matchesSafely__error__1_matcher_expectation__ofSize_2__1_actual__inconsistent_with__exactly() {

        final List<String> strings = singletonList("item");
        final FluentIterableMatcher<String, Iterable<String>> matcher = anIterableOf(String.class)
            .withItemsMatching(containsString("it"))
            .ofSize(2)
            .exactly();

        matcher.matchesSafely(strings);

    }

    @Test
    public void matchesSafely__mismatch__2_matcher_expectations__ofSize_2__1_actual__consistent_with__exactly() {

        final List<String> strings = singletonList("item");

        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    containsString("tem")
                )
                .ofSize(2)
                .exactly()
        )));
    }

    @Test
    public void matchesSafely__mismatch__2_matcher_expectations__ofSize_2__1_actual__consistent_with__exactly__has_ItemResult() {

        final List<String> strings = singletonList("item");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    containsString("tem")
                )
                .ofSize(2)
                .exactly()
        );

        assertThat(itemResults, hasSize(1));
        final ItemResult itemResult = itemResults.get(0);
        assertThat(itemResult.getActual(), is("item"));
        assertThat(itemResult.getIndex(), is(0));
        assertThat(itemResult.isMatched(), is(true));
        assertThat(itemResult.isBreakingItemOrder(), is(false));
        assertThat(itemResult.isBreakingSortOrder(), is(false));
        assertThat(itemResult.isDuplicate(), is(false));
        assertThat(itemResult.isUnwanted(), is(false));
        assertThat(itemResult.getMismatchedItemMatchers().size(), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void matchesSafely__error__3_matcher_expectations__ofSize_2__1_actual__inconsistent_with__exactly() {

        final List<String> strings = singletonList("item");
        final FluentIterableMatcher<String, Iterable<String>> matcher = anIterableOf(String.class)
            .withItemsMatching(
                containsString("it"),
                containsString("tem")
            )
            .ofSize(3)
            .exactly();

        matcher.matchesSafely(strings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void matchesSafely__error__2_matcher_expectations__ofSize_1__1_actual__inconsistent_with__exactly() {

        final List<String> strings = singletonList("item");
        final FluentIterableMatcher<String, Iterable<String>> matcher = anIterableOf(String.class)
            .withItemsMatching(
                containsString("it"),
                containsString("tem")
            )
            .ofSize(1)
            .exactly();

        matcher.matchesSafely(strings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void matchesSafely__error__2_matcher_expectations__ofSize_1__1_actual__inconsistent() {

        final List<String> strings = singletonList("item");
        final FluentIterableMatcher<String, Iterable<String>> matcher = anIterableOf(String.class)
            .withItemsMatching(
                containsString("it"),
                containsString("tem")
            )
            .ofSize(1);

        matcher.matchesSafely(strings);
    }

    @Test
    public void matchesSafely__match__ofSize_1__1_actual() {

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            anIterableOf(String.class)
                .ofSize(1)
        ));
    }

    @Test
    public void matchesSafely__mismatch__ofSize_1__2_actual() {

        final List<String> strings = asList("item", "element");

        assertThat(strings, not(is(
            anIterableOf(String.class)
                .ofSize(1)
        )));
    }

    @Test
    public void matchesSafely__mismatch__ofSize_1__2_actual__has_ItemResult() {

        final List<String> strings = asList("item", "element");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .ofSize(1)
        );

        assertThat(itemResults, hasSize(2));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("item"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(false));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("element"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(false));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));

    }

    @Test
    public void matchesSafely__match__1_matcher_expectation__1_actual() {

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(containsString("it"))
        ));
    }

    @Test
    public void matchesSafely__mismatch__2_matcher_expectations__1_actual_matching() {

        final List<String> strings = singletonList("item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                ))
        ));

    }

    @Test
    public void matchesSafely__mismatch__2_matcher_expectations__1_actual_matching__has_ItemResult_list() {

        final List<String> strings = singletonList("item");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        );

        assertThat(itemResults, hasSize(1));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("item"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));

    }

    @Test
    public void matchesSafely__match__2_matcher_expectations__2_actual_matching() {

        final List<String> strings = asList("item", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        ));
    }

    @Test
    public void matchesSafely__match__2_matcher_expectations__3_actual_matching() {

        final List<String> strings = asList("item", "item", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        ));
    }

    @Test
    public void matchesSafely__mismatch__2_matcher_expectations__3_actual_matching__for_exactly() {

        final List<String> strings = asList("item", "item", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
                .exactly()
        )));
    }

    @Test
    public void matchesSafely__mismatch__2_matcher_expectations__3_actual_matching__for_exactly__has_IssueResults() {

        final List<String> strings = asList("item", "item", "item");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
                .exactly()
        );

        assertThat(itemResults, hasSize(3));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("item"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("item"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(true));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("item"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(false));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));

    }


    @Test
    public void matchesSafely__match__2_matcher_expectations__2_actual_matching__1_actual_not_matching() {

        final List<String> strings = asList("item", "element", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        ));
    }

    @Test
    public void matchesSafely__mismatch__2_matcher_expectations__1_actual_matching__2_actual_not_matching() {

        final List<String> strings = asList("element", "element", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        )));
    }

    @Test
    public void matchesSafely__mismatch__2_matcher_expectations__1_actual_matching__2_actual_not_matching__has_ItemResults() {

        final List<String> strings = asList("element", "element", "item");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        );

        assertThat(itemResults, hasSize(3));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("element"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(false));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(2));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("element"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(false));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(2));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("item"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(false));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
    }

    @Test
    public void matchesSafely__mismatch__2_matcher_expectations__3_actual_none_matching() {

        final List<String> strings = asList("element", "element", "element");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        )));
    }

    @Test
    public void matchesSafely__mismatch__2_matcher_expectations__3_actual_none_matching__has_ItemResults() {

        final List<String> strings = asList("element", "element", "element");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        );

        assertThat(itemResults, hasSize(3));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("element"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(false));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(2));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("element"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(false));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(2));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("element"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(false));
        assertThat(itemResult3.isBreakingItemOrder(), is(false));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(2));
    }

    @Test
    public void matchesSafely__match__3_matcher_expectations__3_actual_all_matching() {

        final List<String> strings = asList("item", "element", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    containsString("l"),
                    is("item")
                )
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ofSize__error__negative_arg() {

        anIterableOf(String.class).ofSize(-1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void sorted__error__requested_sorting_for_non_Comparable() {

        anIterableOf(Object.class).sorted();
    }

    @Test(expected = IllegalArgumentException.class)
    public void withItemsMatching__error__null_arg() {

        //noinspection ConfusingArgumentToVarargsMethod
        anIterableOf(Object.class).withItemsMatching(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withItems__error__null_arg() {

        //noinspection ConfusingArgumentToVarargsMethod
        anIterableOf(Object.class).withItems(null);
    }

    @Test
    public void matchesSafely__match__3_item_expectations__3_actual_all_matching() {

        final List<String> strings = asList("item", "element", "object");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "item",
                    "element",
                    "object"
                )
        ));
    }

    @Test
    public void matchesSafely__mismatch__3_item_expectations__2_actual_matching__1_actual_non_matching() {

        final List<String> strings = asList("item", "element", "object");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        )));
    }

    @Test
    public void matchesSafely__mismatch__3_item_expectations__2_actual_matching__1_actual_non_matching__has_ItemResults() {

        final List<String> strings = asList("item", "element", "object");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        );

        assertThat(itemResults, hasSize(3));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("item"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("element"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(false));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(3));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("object"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(false));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
    }

    @Test
    public void matchesSafely__mismatch__3_item_expectations__3_actual_none_matching() {

        final List<String> strings = asList("it", "element", "objection");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        )));
    }

    @Test
    public void matchesSafely__mismatch__3_item_expectations__3_actual_none_matching__has_ItemResults() {

        final List<String> strings = asList("it", "element", "objection");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        );

        assertThat(itemResults, hasSize(3));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("it"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(false));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(3));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("element"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(false));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(3));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("objection"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(false));
        assertThat(itemResult3.isBreakingItemOrder(), is(false));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(3));
    }

    @Test
    public void matchesSafely__match__mixed_item_and_matcher_expectations__all_matching__1() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
        ));
    }

    @Test
    public void matchesSafely__match__mixed_item_and_matcher_expectations__all_matching__2() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    endsWith("ump"),
                    startsWith("Don")
                )
                .withItems(
                    "news",
                    "fake",
                    "impeachment"
                )

        ));
    }


    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__1() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    equalTo("not dump")
                )
        )));

    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__1__has_ItemResults() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    equalTo("not dump")
                )
        );

        assertThat(itemResults, hasSize(5));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("fake"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("news"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(true));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("impeachment"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(false));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("Donald"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isMatched(), is(true));
        assertThat(itemResult4.isBreakingItemOrder(), is(false));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        assertThat(itemResult4.isDuplicate(), is(false));
        assertThat(itemResult4.isUnwanted(), is(false));
        assertThat(itemResult4.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("Trump"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isMatched(), is(false));
        assertThat(itemResult5.isBreakingItemOrder(), is(false));
        assertThat(itemResult5.isBreakingSortOrder(), is(false));
        assertThat(itemResult5.isDuplicate(), is(false));
        assertThat(itemResult5.isUnwanted(), is(false));
        assertThat(itemResult5.getMismatchedItemMatchers().size(), is(5));

    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__2() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "peaches"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
        )));
    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__3() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .withItems(
                    "fake",
                    "news",
                    "peaches"
                )

        )));
    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__all_matching__but_not_exactly() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump", "alternative", "facts");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .exactly()
        )));
    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__all_matching__but_not_exactly__has_ItemResults() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump", "alternative", "facts");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .exactly()
        );

        assertThat(itemResults, hasSize(7));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("fake"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("news"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(true));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("impeachment"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(false));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("Donald"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isMatched(), is(true));
        assertThat(itemResult4.isBreakingItemOrder(), is(false));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        assertThat(itemResult4.isDuplicate(), is(false));
        assertThat(itemResult4.isUnwanted(), is(false));
        assertThat(itemResult4.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("Trump"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isMatched(), is(true));
        assertThat(itemResult5.isBreakingItemOrder(), is(false));
        assertThat(itemResult5.isBreakingSortOrder(), is(false));
        assertThat(itemResult5.isDuplicate(), is(false));
        assertThat(itemResult5.isUnwanted(), is(false));
        assertThat(itemResult5.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult6 = itemResults.get(5);
        assertThat(itemResult6.getActual(), is("alternative"));
        assertThat(itemResult6.getIndex(), is(5));
        assertThat(itemResult6.isMatched(), is(false));
        assertThat(itemResult6.isBreakingItemOrder(), is(false));
        assertThat(itemResult6.isBreakingSortOrder(), is(false));
        assertThat(itemResult6.isDuplicate(), is(false));
        assertThat(itemResult6.isUnwanted(), is(true));
        assertThat(itemResult6.getMismatchedItemMatchers().size(), is(5));
        final ItemResult itemResult7 = itemResults.get(6);
        assertThat(itemResult7.getActual(), is("facts"));
        assertThat(itemResult7.getIndex(), is(6));
        assertThat(itemResult7.isMatched(), is(false));
        assertThat(itemResult7.isBreakingItemOrder(), is(false));
        assertThat(itemResult7.isBreakingSortOrder(), is(false));
        assertThat(itemResult7.isDuplicate(), is(false));
        assertThat(itemResult7.isUnwanted(), is(true));
        assertThat(itemResult7.getMismatchedItemMatchers().size(), is(5));
    }

    @Test
    public void matchesSafely__match__mixed_item_and_matcher_expectations__ordered() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        ));
    }

    @Test
    public void matchesSafely__match__mixed_item_and_matcher_expectations__ordered__and_matchers_match_multiple_items__1() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)

                .withItemsMatching(
                    containsString("a"),
                    containsString("e"),
                    containsString("m"),
                    containsString("a"),
                    containsString("m")
                )
                .ordered()
        ));
    }

    @Test
    public void matchesSafely__match__mixed_item_and_matcher_expectations__ordered__and_matchers_match_multiple_items__2() {

        final List<String> strings = asList("faake", "neeeews", "impeaaaachment", "Doneeld", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)

                .withItemsMatching(
                    containsString("aa"),
                    containsString("eee"),
                    containsString("aaa"),
                    containsString("ee"),
                    containsString("m")
                )
                .ordered()
        ));
    }


    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__1() {

        final List<String> strings = asList("fake", "news", "Donald", "Trump", "impeachment");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        )));
    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__1__has_ItemResults() {

        final List<String> strings = asList("fake", "news", "Donald", "Trump", "impeachment");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        );

        assertThat(itemResults, hasSize(5));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("fake"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("news"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(true));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("Donald"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(true));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("Trump"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isMatched(), is(true));
        assertThat(itemResult4.isBreakingItemOrder(), is(true));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        assertThat(itemResult4.isDuplicate(), is(false));
        assertThat(itemResult4.isUnwanted(), is(false));
        assertThat(itemResult4.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("impeachment"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isMatched(), is(true));
        assertThat(itemResult5.isBreakingItemOrder(), is(false));
        assertThat(itemResult5.isBreakingSortOrder(), is(false));
        assertThat(itemResult5.isDuplicate(), is(false));
        assertThat(itemResult5.isUnwanted(), is(false));
        assertThat(itemResult5.getMismatchedItemMatchers().size(), is(0));

    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__2() {

        final List<String> strings = asList("Donald", "Trump", "fake", "news", "impeachment");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        )));
    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__2__has_ItemResults() {

        final List<String> strings = asList("Donald", "Trump", "fake", "news", "impeachment");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        );

        assertThat(itemResults, hasSize(5));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("Donald"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(true));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("Trump"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(true));
        assertThat(itemResult2.isBreakingItemOrder(), is(true));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("fake"));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(false));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("news"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isMatched(), is(true));
        assertThat(itemResult4.isBreakingItemOrder(), is(false));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        assertThat(itemResult4.isDuplicate(), is(false));
        assertThat(itemResult4.isUnwanted(), is(false));
        assertThat(itemResult4.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("impeachment"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isMatched(), is(true));
        assertThat(itemResult5.isBreakingItemOrder(), is(false));
        assertThat(itemResult5.isBreakingSortOrder(), is(false));
        assertThat(itemResult5.isDuplicate(), is(false));
        assertThat(itemResult5.isUnwanted(), is(false));
        assertThat(itemResult5.getMismatchedItemMatchers().size(), is(0));
    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__3() {

        final List<String> strings = asList("impeachment", "Donald", "Trump", "fake", "news");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .ordered()
        )));
    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__3__has_ItemResults() {

        final List<String> strings = asList("impeachment", "Donald", "Trump", "fake", "news");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .ordered()
        );

        assertThat(itemResults, hasSize(5));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("impeachment"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(true));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("Donald"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(true));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("Trump"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(false));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("fake"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isMatched(), is(true));
        assertThat(itemResult4.isBreakingItemOrder(), is(false));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        assertThat(itemResult4.isDuplicate(), is(false));
        assertThat(itemResult4.isUnwanted(), is(false));
        assertThat(itemResult4.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("news"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isMatched(), is(true));
        assertThat(itemResult5.isBreakingItemOrder(), is(false));
        assertThat(itemResult5.isBreakingSortOrder(), is(false));
        assertThat(itemResult5.isDuplicate(), is(false));
        assertThat(itemResult5.isUnwanted(), is(false));
        assertThat(itemResult5.getMismatchedItemMatchers().size(), is(0));
    }


    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__4() {

        final List<String> strings = asList("impeachment", "Donald", "fake", "news", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .ordered()
        )));

    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__4__has_ItemResults() {

        final List<String> strings = asList("impeachment", "Donald", "fake", "news", "Trump");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .ordered()
        );

        assertThat(itemResults, hasSize(5));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("impeachment"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(true));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("Donald"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(true));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("fake"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(true));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("news"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isMatched(), is(true));
        assertThat(itemResult4.isBreakingItemOrder(), is(true));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        assertThat(itemResult4.isDuplicate(), is(false));
        assertThat(itemResult4.isUnwanted(), is(false));
        assertThat(itemResult4.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("Trump"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isMatched(), is(true));
        assertThat(itemResult5.isBreakingItemOrder(), is(false));
        assertThat(itemResult5.isBreakingSortOrder(), is(false));
        assertThat(itemResult5.isDuplicate(), is(false));
        assertThat(itemResult5.isUnwanted(), is(false));
        assertThat(itemResult5.getMismatchedItemMatchers().size(), is(0));

    }


    @Test
    public void matchesSafely__match__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__1() {

        final List<String> strings = asList("fake", "news", "alternative", "facts", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        ));
    }

    @Test
    public void matchesSafely__match__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__2() {

        final List<String> strings = asList("alternative", "fake", "news", "facts", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        ));
    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__exactly() {

        final List<String> strings = asList("alternative", "fake", "news", "facts", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
                .exactly()
        )));
    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__exactly__has_ItemResults() {

        final List<String> strings = asList("alternative", "fake", "news", "facts", "impeachment", "Donald", "Trump");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
                .exactly()
        );

        assertThat(itemResults, hasSize(7));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("alternative"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(false));
        assertThat(itemResult1.isBreakingItemOrder(), is(true));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(true));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(1));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("fake"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(true));
        assertThat(itemResult2.isBreakingItemOrder(), is(true));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("news"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(true));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("facts"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isMatched(), is(false));
        assertThat(itemResult4.isBreakingItemOrder(), is(true));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        assertThat(itemResult4.isDuplicate(), is(false));
        assertThat(itemResult4.isUnwanted(), is(true));
        assertThat(itemResult4.getMismatchedItemMatchers().size(), is(1));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("impeachment"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isMatched(), is(true));
        assertThat(itemResult5.isBreakingItemOrder(), is(true));
        assertThat(itemResult5.isBreakingSortOrder(), is(false));
        assertThat(itemResult5.isDuplicate(), is(false));
        assertThat(itemResult5.isUnwanted(), is(false));
        assertThat(itemResult5.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult6 = itemResults.get(5);
        assertThat(itemResult6.getActual(), is("Donald"));
        assertThat(itemResult6.getIndex(), is(5));
        assertThat(itemResult6.isMatched(), is(true));
        assertThat(itemResult6.isBreakingItemOrder(), is(false));
        assertThat(itemResult6.isBreakingSortOrder(), is(false));
        assertThat(itemResult6.isDuplicate(), is(false));
        assertThat(itemResult6.isUnwanted(), is(false));
        assertThat(itemResult6.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult7 = itemResults.get(6);
        assertThat(itemResult7.getActual(), is("Trump"));
        assertThat(itemResult7.getIndex(), is(6));
        assertThat(itemResult7.isMatched(), is(true));
        assertThat(itemResult7.isBreakingItemOrder(), is(false));
        assertThat(itemResult7.isBreakingSortOrder(), is(false));
        assertThat(itemResult7.isDuplicate(), is(false));
        assertThat(itemResult7.isUnwanted(), is(false));
        assertThat(itemResult7.getMismatchedItemMatchers().size(), is(0));

    }

    @Test
    public void matchesSafely__match__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__3() {

        final List<String> strings = asList("alternative", "fake", "news", "impeachment", "Donald", "Trump", "facts");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        ));
    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__with_more_actuals_than_expected__1() {

        final List<String> strings = asList("news", "fake", "alternative", "facts", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        )));

    }

    @Test
    public void matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__with_more_actuals_than_expected__1__has_ItemResults() {

        final List<String> strings = asList("news", "fake", "alternative", "facts", "impeachment", "Donald", "Trump");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        );

        assertThat(itemResults, hasSize(7));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("news"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(true));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("fake"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(true));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(false));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("alternative"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(false));
        assertThat(itemResult3.isBreakingItemOrder(), is(true));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(5));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("facts"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isMatched(), is(false));
        assertThat(itemResult4.isBreakingItemOrder(), is(true));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        assertThat(itemResult4.isDuplicate(), is(false));
        assertThat(itemResult4.isUnwanted(), is(false));
        assertThat(itemResult4.getMismatchedItemMatchers().size(), is(5));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("impeachment"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isMatched(), is(true));
        assertThat(itemResult5.isBreakingItemOrder(), is(true));
        assertThat(itemResult5.isBreakingSortOrder(), is(false));
        assertThat(itemResult5.isDuplicate(), is(false));
        assertThat(itemResult5.isUnwanted(), is(false));
        assertThat(itemResult5.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult6 = itemResults.get(5);
        assertThat(itemResult6.getActual(), is("Donald"));
        assertThat(itemResult6.getIndex(), is(5));
        assertThat(itemResult6.isMatched(), is(true));
        assertThat(itemResult6.isBreakingItemOrder(), is(true));
        assertThat(itemResult6.isBreakingSortOrder(), is(false));
        assertThat(itemResult6.isDuplicate(), is(false));
        assertThat(itemResult6.isUnwanted(), is(false));
        assertThat(itemResult6.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult7 = itemResults.get(6);
        assertThat(itemResult7.getActual(), is("Trump"));
        assertThat(itemResult7.getIndex(), is(6));
        assertThat(itemResult7.isMatched(), is(true));
        assertThat(itemResult7.isBreakingItemOrder(), is(true));
        assertThat(itemResult7.isBreakingSortOrder(), is(false));
        assertThat(itemResult7.isDuplicate(), is(false));
        assertThat(itemResult7.isUnwanted(), is(false));
        assertThat(itemResult7.getMismatchedItemMatchers().size(), is(0));
    }


    @Test
    public void matchesSafely__mismatch__items_expected_missing() {

        final List<String> strings = asList("alternative", "facts", "impeachment", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
        )));
    }

    @Test
    public void matchesSafely__mismatch__all_items_expected_missing() {

        final List<String> strings = emptyList();

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
        )));
    }

    @Test
    public void matchesSafely__match__sorted__empty_Collection() {

        final List<String> strings = emptyList();

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }

    @Test
    public void matchesSafely__match__sorted__Collection_with_1_element() {

        final List<String> strings = singletonList("B");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }

    @Test
    public void matchesSafely__match__sorted__sorted_Collection__1() {

        final List<String> strings = asList("A", "B", "C");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }

    @Test
    public void matchesSafely__match__sorted__sorted_Collection__2() {

        final List<String> strings = asList("A", "A", "B", "C", "C", "D", "E", "F", "F");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }

    @Test
    public void matchesSafely__mismatch__sorted__unsorted_Collection__1() {

        final List<String> strings = asList("D", "B", "C");

        assertThat(strings, not(is(
            anIterableOf(String.class).sorted()
        )));
    }

    @Test
    public void matchesSafely__mismatch__sorted__unsorted_Collection__1__has_ItemResults() {

        final List<String> strings = asList("D", "B", "C");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class).sorted()
        );

        assertThat(itemResults, hasSize(3));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("D"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(false));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("B"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(false));
        assertThat(itemResult2.isBreakingItemOrder(), is(false));
        assertThat(itemResult2.isBreakingSortOrder(), is(true));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(false));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("C"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(false));
        assertThat(itemResult3.isBreakingItemOrder(), is(false));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(false));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
    }

    @Test
    public void matchesSafely__mismatch__sorted__unsorted_Collection__2() {

        final List<String> strings = asList("B", "A", "A", "C", "C", "D", "E", "F", "F");

        assertThat(strings, not(is(
            anIterableOf(String.class).sorted()
        )));
    }

    @Test
    public void matchesSafely__mismatch__sorted__unsorted_Collection__3() {

        final List<String> strings = asList("A", "B", "A", "C", "C", "D", "E", "F", "F");

        assertThat(strings, not(is(
            anIterableOf(String.class).sorted()
        )));
    }

    @Test
    public void matchesSafely__mismatch__sorted__unsorted_Collection__4() {

        final List<String> strings = asList("A", "A", "C", "C", "D", "E", "F", "F", "B");

        assertThat(strings, not(is(
            anIterableOf(String.class).sorted()
        )));
    }

    @Test
    public void matchesSafely__mismatch__sorted__unsorted_Collection__5__has_ItemResults() {

        final List<String> strings = asList("B", "A", "A", "C", "B");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class).sorted()
        );

        assertThat(itemResults, hasSize(5));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("B"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("A"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isBreakingSortOrder(), is(true));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("A"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("C"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("B"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isBreakingSortOrder(), is(true));
    }


    @Test
    public void matchesSafely__match__sorted__empty_Collection__using_Comparator() {

        final List<String> strings = emptyList();

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void matchesSafely__match__sorted__Collection_with_1_element__using_Comparator() {

        final List<String> strings = singletonList("B");

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void matchesSafely__match__sorted__sorted_Collection__using_Comparator_1() {

        final List<String> strings = asList("A", "B", "C");

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void matchesSafely__match__sorted__sorted_Collection__using_Comparator_2() {

        final List<String> strings = asList("A", "BB", "CCC");

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void matchesSafely__match__sorted__sorted_Collection__using_Comparator_3() {

        final List<String> strings = asList("AA", "BB", "CCC", "DDD", "EEEE");

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_1() {

        final List<String> strings = asList("AAA", "BB", "CCC", "DDD", "EEEE");

        assertThat(strings, not(is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        )));
    }

    @Test
    public void matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_2() {

        final List<String> strings = asList("AA", "BB", "CCC", "DDD", "EE");

        assertThat(strings, not(is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        )));
    }

    @Test
    public void matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_3() {

        final List<String> strings = asList("A", "BB", "CCCC", "DDD", "EEEE");

        assertThat(strings, not(is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        )));
    }

    @Test
    public void matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_4__hasItemResults() {

        final List<String> strings = asList("BBB", "AA", "AA", "CCC", "D");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class).sorted(comparingInt(String::length))
        );

        assertThat(itemResults, hasSize(5));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("BBB"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("AA"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isBreakingSortOrder(), is(true));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("AA"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("CCC"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("D"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isBreakingSortOrder(), is(true));
    }

    @Test
    public void matchesSafely__match__unique__empty_Collection() {

        final List<String> strings = emptyList();

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }

    @Test
    public void matchesSafely__match__unique__Collection_with_1_item() {

        final List<String> strings = singletonList("singleton");

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }


    @Test
    public void matchesSafely__match__unique__Collection_with_3_unique_items() {

        final List<String> strings = asList("singleton", "doubleton", "tripleton");

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }


    @Test
    public void matchesSafely__mismatch__unique__Collection_with_duplicates__1() {

        final List<String> strings = asList("doubleton", "doubleton");

        assertThat(strings, not(is(
            anIterableOf(String.class).unique()
        )));
    }

    @Test
    public void matchesSafely__mismatch__unique__Collection_with_duplicates__2() {

        final List<String> strings = asList("singleton", "doubleton", "doubleton", "tripleton");

        assertThat(strings, not(is(
            anIterableOf(String.class).unique()
        )));
    }

    @Test
    public void matchesSafely__mismatch__unique__Collection_with_duplicates__3() {

        final List<String> strings = asList("singleton", "singleton", "doubleton", "tripleton");

        assertThat(strings, not(is(
            anIterableOf(String.class).unique()
        )));
    }

    @Test
    public void matchesSafely__mismatch__unique__Collection_with_duplicates__4() {

        final List<String> strings = asList("singleton", "singleton", "doubleton", "tripleton", "tripleton");

        assertThat(strings, not(is(
            anIterableOf(String.class).unique()
        )));
    }

    @Test
    public void matchesSafely__mismatch__unique__Collection_with_duplicates__5__has_ItemResults() {

        final List<String> strings = asList("B", "A", "D", "A", "B");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class).unique()
        );

        assertThat(itemResults, hasSize(5));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("B"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isDuplicate(), is(true));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("A"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isDuplicate(), is(true));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("D"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isDuplicate(), is(false));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("A"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isDuplicate(), is(true));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("B"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isDuplicate(), is(true));
    }


    @Test
    public void matchesSafely__match__unique__empty_Collection__with_equator_function() {

        final List<String> strings = emptyList();

        assertThat(strings, is(
            anIterableOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        ));
    }

    @Test
    public void matchesSafely__match__unique__Collection_with_1_item__with_equator_function() {

        final List<String> strings = singletonList("x");

        assertThat(strings, is(
            anIterableOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        ));
    }

    @Test
    public void matchesSafely__match__unique__Collection_without_duplicates__with_equator_function() {

        final List<String> strings = asList("x", "yy", "zzz");

        assertThat(strings, is(
            anIterableOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        ));
    }

    @Test
    public void matchesSafely__mismatch__unique__Collection_with_duplicates__with_equator_function() {

        final List<String> strings = asList("x", "yy", "zzz", "åå");

        assertThat(strings, not(is(
            anIterableOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        )));
    }

    @Test
    public void matchesSafely__mismatch__unique__Collection_with_duplicates__with_equator_function__2__has_ItemResults() {

        final List<String> strings = asList("B", "a", "D", "A", "b");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class).unique(String::equalsIgnoreCase)
        );

        assertThat(itemResults, hasSize(5));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("B"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isDuplicate(), is(true));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("a"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isDuplicate(), is(true));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("D"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isDuplicate(), is(false));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("A"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isDuplicate(), is(true));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("b"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isDuplicate(), is(true));
    }


    @Test
    public void matchesSafely__resets() {

        final List<String> strings = asList("x", "zzz", "yy", "yy", "åå");
        final List<String> strings2 = asList("x", "yy", "zzz");
        final FluentIterableMatcher<String, Iterable<String>> matching = anIterableOf(String.class)
            .exactly()
            .sorted()
            .ordered()
            .unique()
            .withItems("x", "yy", "zzz");

        assertThat(strings, not(is(matching)));
        assertThat(strings2, is(matching));
    }

    @Test
    public void matchesSafely__resets__also_IssueResults() {

        final List<String> strings = asList("x", "wzx", "yy", "yy", "åå");
        final List<String> strings2 = asList("x", "yy", "zzz");
        final FluentIterableMatcher<String, Iterable<String>> matching = anIterableOf(String.class)
            .exactly()
            .sorted()
            .ordered()
            .unique()
            .withItems("x", "yy", "zzz");

        final List<ItemResult> itemResults = matchResults(strings,
            matching
        );

        assertThat(itemResults, hasSize(5));
        final ItemResult itemResult1 = itemResults.get(0);
        assertThat(itemResult1.getActual(), is("x"));
        assertThat(itemResult1.getIndex(), is(0));
        assertThat(itemResult1.isMatched(), is(true));
        assertThat(itemResult1.isBreakingItemOrder(), is(false));
        assertThat(itemResult1.isBreakingSortOrder(), is(false));
        assertThat(itemResult1.isDuplicate(), is(false));
        assertThat(itemResult1.isUnwanted(), is(false));
        assertThat(itemResult1.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult2 = itemResults.get(1);
        assertThat(itemResult2.getActual(), is("wzx"));
        assertThat(itemResult2.getIndex(), is(1));
        assertThat(itemResult2.isMatched(), is(false));
        assertThat(itemResult2.isBreakingItemOrder(), is(true));
        assertThat(itemResult2.isBreakingSortOrder(), is(true));
        assertThat(itemResult2.isDuplicate(), is(false));
        assertThat(itemResult2.isUnwanted(), is(true));
        assertThat(itemResult2.getMismatchedItemMatchers().size(), is(1));
        final ItemResult itemResult3 = itemResults.get(2);
        assertThat(itemResult3.getActual(), is("yy"));
        assertThat(itemResult3.getIndex(), is(2));
        assertThat(itemResult3.isMatched(), is(true));
        assertThat(itemResult3.isBreakingItemOrder(), is(true));
        assertThat(itemResult3.isBreakingSortOrder(), is(false));
        assertThat(itemResult3.isDuplicate(), is(true));
        assertThat(itemResult3.isUnwanted(), is(false));
        assertThat(itemResult3.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult4 = itemResults.get(3);
        assertThat(itemResult4.getActual(), is("yy"));
        assertThat(itemResult4.getIndex(), is(3));
        assertThat(itemResult4.isMatched(), is(true));
        assertThat(itemResult4.isBreakingItemOrder(), is(false));
        assertThat(itemResult4.isBreakingSortOrder(), is(false));
        assertThat(itemResult4.isDuplicate(), is(true));
        assertThat(itemResult4.isUnwanted(), is(false));
        assertThat(itemResult4.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult5 = itemResults.get(4);
        assertThat(itemResult5.getActual(), is("åå"));
        assertThat(itemResult5.getIndex(), is(4));
        assertThat(itemResult5.isMatched(), is(false));
        assertThat(itemResult5.isBreakingItemOrder(), is(true));
        assertThat(itemResult5.isBreakingSortOrder(), is(false));
        assertThat(itemResult5.isDuplicate(), is(false));
        assertThat(itemResult5.isUnwanted(), is(true));
        assertThat(itemResult5.getMismatchedItemMatchers().size(), is(0));


        final List<ItemResult> itemResults2 = matchResults(strings2,
            matching
        );

        assertThat(itemResults2, hasSize(3));
        final ItemResult itemResult21 = itemResults2.get(0);
        assertThat(itemResult21.getActual(), is("x"));
        assertThat(itemResult21.getIndex(), is(0));
        assertThat(itemResult21.isMatched(), is(true));
        assertThat(itemResult21.isBreakingItemOrder(), is(false));
        assertThat(itemResult21.isBreakingSortOrder(), is(false));
        assertThat(itemResult21.isDuplicate(), is(false));
        assertThat(itemResult21.isUnwanted(), is(false));
        assertThat(itemResult21.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult22 = itemResults2.get(1);
        assertThat(itemResult22.getActual(), is("yy"));
        assertThat(itemResult22.getIndex(), is(1));
        assertThat(itemResult22.isMatched(), is(true));
        assertThat(itemResult22.isBreakingItemOrder(), is(false));
        assertThat(itemResult22.isBreakingSortOrder(), is(false));
        assertThat(itemResult22.isDuplicate(), is(false));
        assertThat(itemResult22.isUnwanted(), is(false));
        assertThat(itemResult22.getMismatchedItemMatchers().size(), is(0));
        final ItemResult itemResult23 = itemResults2.get(2);
        assertThat(itemResult23.getActual(), is("zzz"));
        assertThat(itemResult23.getIndex(), is(2));
        assertThat(itemResult23.isMatched(), is(true));
        assertThat(itemResult23.isBreakingItemOrder(), is(false));
        assertThat(itemResult23.isBreakingSortOrder(), is(false));
        assertThat(itemResult23.isDuplicate(), is(false));
        assertThat(itemResult23.isUnwanted(), is(false));
        assertThat(itemResult23.getMismatchedItemMatchers().size(), is(0));
    }

    @Test
    public void no_ArrayIndexOutOfBoundsException__on_getIssueResults() {

        final List<String> strings = asList("x", "wzx", "yy", "yy", "åå");

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .exactly()
                .sorted()
                .ordered()
                .unique()
                .withItems("x", "yy", "zzz")
        );

        assertThat(itemResults, hasSize(5));
    }

    @Test
    public void matchesSafely__mismatch__sorts_mismatching_matchers() {

        final List<String> strings = singletonList("AAA");
        final Attribute<String, Integer> length = attribute("length", String::length);
        final Attribute<String, Boolean> firstCharUpper = attribute("fcu", s -> s.substring(0, 1).toUpperCase().equals(s.substring(0, 1)));
        final Attribute<String, Boolean> secondCharUpper = attribute("fcu", s -> s.substring(1, 2).toUpperCase().equals(s.substring(1, 2)));
        final FluentAttributeMatcher<String> m1 = a(String.class)
            .with(length, 4)
            .with(firstCharUpper, false)
            .with(secondCharUpper, false);
        final FluentAttributeMatcher<String> m2 = a(String.class)
            .with(length, 3)
            .with(firstCharUpper, false)
            .with(secondCharUpper, true);
        final FluentAttributeMatcher<String> m3 = a(String.class)
            .with(length, 5)
            .with(firstCharUpper, true)
            .with(secondCharUpper, false);

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(m1, m2, m3)
        );

        assertTrue(itemResults.get(0).getMismatchedItemMatchers().get(0) == m2);
        assertTrue(itemResults.get(0).getMismatchedItemMatchers().get(1) == m3);
        assertTrue(itemResults.get(0).getMismatchedItemMatchers().get(2) == m1);
    }

    @Test
    public void matchesSafely__mismatch__sorts_mismatching_matchers__2() {

        final List<String> strings = singletonList("aAAA");
        final Attribute<String, Integer> length = attribute("length", String::length);
        final Attribute<String, Boolean> firstCharUpper = attribute("fcu", s -> s.substring(0, 1).toUpperCase().equals(s.substring(0, 1)));
        final Attribute<String, Boolean> secondCharUpper = attribute("fcu", s -> s.substring(1, 2).toUpperCase().equals(s.substring(1, 2)));
        final FluentAttributeMatcher<String> m1 = a(String.class)
            .with(length, 4)
            .with(firstCharUpper, false)
            .with(secondCharUpper, false);
        final FluentAttributeMatcher<String> m2 = a(String.class)
            .with(length, 3)
            .with(firstCharUpper, false)
            .with(secondCharUpper, true);
        final FluentAttributeMatcher<String> m3 = a(String.class)
            .with(length, 5)
            .with(firstCharUpper, true)
            .with(secondCharUpper, false);

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(m1, m2, m3)
        );

        assertTrue(itemResults.get(0).getMismatchedItemMatchers().get(0) == m1);
        assertTrue(itemResults.get(0).getMismatchedItemMatchers().get(1) == m2);
        assertTrue(itemResults.get(0).getMismatchedItemMatchers().get(2) == m3);
    }

    @Test
    public void matchesSafely__mismatch__sorts_mismatching_matchers__3() {

        final List<String> strings = singletonList("AAAAa");
        final Attribute<String, Integer> length = attribute("length", String::length);
        final Attribute<String, Boolean> firstCharUpper = attribute("fcu", s -> s.substring(0, 1).toUpperCase().equals(s.substring(0, 1)));
        final Attribute<String, Boolean> secondCharUpper = attribute("fcu", s -> s.substring(1, 2).toUpperCase().equals(s.substring(1, 2)));
        final FluentAttributeMatcher<String> m1 = a(String.class)
            .with(length, 4)
            .with(firstCharUpper, false)
            .with(secondCharUpper, false);
        final FluentAttributeMatcher<String> m2 = a(String.class)
            .with(length, 3)
            .with(firstCharUpper, false)
            .with(secondCharUpper, true);
        final FluentAttributeMatcher<String> m3 = a(String.class)
            .with(length, 5)
            .with(firstCharUpper, true)
            .with(secondCharUpper, false);

        final List<ItemResult> itemResults = matchResults(strings,
            anIterableOf(String.class)
                .withItemsMatching(m1, m2, m3)
        );

        assertTrue(itemResults.get(0).getMismatchedItemMatchers().get(0) == m3);
        assertTrue(itemResults.get(0).getMismatchedItemMatchers().get(1) == m2);
        assertTrue(itemResults.get(0).getMismatchedItemMatchers().get(2) == m1);
    }

    @Test
    public void matchesSafely__match__getScore__max___1() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class));

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max___2__sorted() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class).sorted());

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max___2__ordered() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .ordered()
        );

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max___3__ordered__withItems() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .ordered()
            .withItems(1)
        );

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max___4__ordered__withItems() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .ordered()
            .withItems(2, 3)
        );

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max___5__unique() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class).unique());

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max___6__ofSize() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class).ofSize(3));

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max___7__exactly__withItems() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .exactly()
            .withItems(3, 2, 1)
        );

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max___8__exactly__withItems__ordered() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .exactly()
            .withItems(1, 2, 3)
            .ordered()
        );

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max___9__exactly__withItems__ordered__ofSize() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .ofSize(3)
            .exactly()
            .withItems(1, 2, 3)
            .ordered()
        );

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max__10__exactly__withItems__ordered__ofSize__sorted() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .ofSize(3)
            .exactly()
            .withItems(1, 2, 3)
            .ordered()
            .sorted()
        );

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__match__getScore__max__11__exactly__withItems__ordered__ofSize__sorted__unique() {

        final List<Integer> numbers = asList(1, 2, 3);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .ofSize(3)
            .exactly()
            .withItems(1, 2, 3)
            .ordered()
            .sorted()
            .unique()
        );

        assertThat(score, closeTo(1.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__min__null_Collection_arg() {

        final List<Integer> numbers = null;

        final double score = matchScore(numbers, anIterableOf(Integer.class));

        assertThat(score, closeTo(0.0, 0.00001));
    }


    @Test
    public void matchesSafely__mismatch__getScore__min__ofSize_doesnt_match__null() {

        final List<Integer> numbers = null;

        final double score = matchScore(numbers, anIterableOf(Integer.class).ofSize(1));

        assertThat(score, closeTo(0.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__min__ofSize_doesnt_match() {

        final List<Integer> numbers = emptyList();

        final double score = matchScore(numbers, anIterableOf(Integer.class).ofSize(1));

        assertThat(score, closeTo(1.0/2.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__partial__exactly_isnt_the_case() {

        final List<Integer> numbers = singletonList(9);

        final double score = matchScore(numbers, anIterableOf(Integer.class).exactly());

        assertThat(score, closeTo(1.0/2.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__partial__sorted_isnt_the_case() {

        final List<Integer> numbers = asList(9, 8);

        final double score = matchScore(numbers, anIterableOf(Integer.class).sorted());

        assertThat(score, closeTo(1.0/2.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__partial__unique_isnt_the_case() {

        final List<Integer> numbers = asList(9, 9);

        final double score = matchScore(numbers, anIterableOf(Integer.class).unique());

        assertThat(score, closeTo(1.0/2.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__partial__ordered_isnt_the_case__and_no_item_matches() {

        final List<Integer> numbers = asList(9, 8);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .ordered()
            .withItems(6, 7)
        );

        assertThat(score, closeTo(1.0/5.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__partial__ordered_isnt_the_case__but_all_items_match() {

        final List<Integer> numbers = asList(9, 8);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .ordered()
            .withItems(8, 9)
        );

        assertThat(score, closeTo(4.0/5.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__partial__ordered_isnt_the_case__but_some_items_match() {

        final List<Integer> numbers = asList(9, 8);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .ordered()
            .withItems(7, 9)
        );

        assertThat(score, closeTo(2.0/5.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__partial__ordered_isnt_really_the_case__but_some_items_match__but_not_exactly() {

        final List<Integer> numbers = asList(9, 8);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .ordered()
            .withItems(7, 9, 10)
            .exactly()
        );

        assertThat(score, closeTo(3.0/7.0, 0.00001));
    }


    @Test
    public void matchesSafely__mismatch__getScore__partial__sorted_isnt_the_case__but_some_items_match__but_not_exactly() {

        final List<Integer> numbers = asList(9, 8);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .sorted()
            .withItems(10, 9, 7)
            .exactly()
        );

        assertThat(score, closeTo(3.0/7.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__partial__sorted_isnt_the_case__but_some_items_match__but_not_exactly__and_with_wrong_size() {

        final List<Integer> numbers = asList(9, 8, 11, 13);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .sorted()
            .withItems(10, 9, 7)
            .exactly()
            .ofSize(3)
        );

        assertThat(score, closeTo(2.0/8.0, 0.00001));
    }

    @Test
    public void matchesSafely__mismatch__getScore__partial__sorted_isnt_the_case__but_some_items_match__but_not_exactly__and_with_wrong_size__unique() {

        final List<Integer> numbers = asList(9, 8, 11, 13);

        final double score = matchScore(numbers, anIterableOf(Integer.class)
            .sorted()
            .withItems(10, 9, 7)
            .exactly()
            .ofSize(3)
            .unique()
        );

        assertThat(score, closeTo(3.0/9.0, 0.00001));
    }


    private static class Paper{
        private final String text;
        private final int pages;

        private Paper(final String text, final int pages) {
            this.text = text;
            this.pages = pages;
        }
        int getPages() {
            return pages;
        }

        String getText() {
            return text;
        }

        @Override
        public String toString() {
            return "Paper{" +
                "text='" + text + '\'' +
                ", pages=" + pages +
                '}';
        }
    }

    private static Paper pap(final String text){return new Paper(text, text.length()*10);}
    private static final Attribute<Paper, String> txt = Attribute.attribute("text", Paper::getText);
    private static final Attribute<Paper, Integer> pages = Attribute.attribute("pages", Paper::getPages);

    @Test
    public void matchesSafely__mismatch__describeTo__describeMismatchSafely__with_full_force(){
        final StringDescription self = new StringDescription();
        final StringDescription issues = new StringDescription();
        final Iterable<Paper> input = asList(pap("PAP!"), pap("The Law Of Gravity"), pap("Booh!"), pap("PAP!"));
        final FluentIterableMatcher<Paper, Iterable<Paper>> matcher = anIterableOf(Paper.class)
            .exactly()
            .ordered()
            .sorted(comparingInt(Paper::getPages))
            .unique((p1, p2) -> p1.text.equals(p2.text))
            .withItemsMatching(
                a(Paper.class).with(txt, "Booh!").with(pages, 3),
                a(Paper.class).with(txt, "PAP!").with(pages, 40),
                a(Paper.class).with(txt, "Grave").with(pages, 0)
            );

        final boolean matches = matcher.matchesSafely(input);

        assertThat(matches, is(false));

        matcher.describeTo(self);

        assertThat(self.toString(), is( "" +
            "an Iterable with the following properties:\n" +
            "- Iterable of Paper\n" +
            "- at least 3 matching item(s)\n" +
            "- no unexpected items\n" +
            "- sorted\n" +
            "- ordered\n" +
            "- no duplicates\n" +
            "\n"));

        matcher.describeMismatchSafely(input, issues);

        assertThat(issues.toString(), is("" +
            "Findings:\n" +
            "\"Not all expectations were fulfilled.\"\n" +
            "\"Unexpected actual items.\"\n" +
            "\"Items did not appear in the expected order.\"\n" +
            "\"Collection is not sorted.\"\n" +
            "\"Detected duplicates.\"\n" +
            "\n" +
            "[0][Paper{text='PAP!', pages=40}  ]💕  ↔ 👯  \n" +
            "[1][Paper{text='The Law Of Gravity]    ↔   🚯 💔[ \ttext = \"PAP!\" <> \"The Law Of Gravity\" \tpages = <40> <> <180> \t]\n" +
            "[2][Paper{text='Booh!', pages=50} ]  ↕ ↔   🚯 💔[ \ttext = \"Grave\" <> \"Booh!\" \tpages = <0> <> <50> \t]\n" +
            "[3][Paper{text='PAP!', pages=40}  ]💕↕   👯  \n" +
            "was <[Paper{text='PAP!', pages=40}, Paper{text='The Law Of Gravity', pages=180}, Paper{text='Booh!', pages=50}, Paper{text='PAP!', pages=40}]>"
        ));
    }


    @Test
    public void matchesSafely__mismatch__describeTo__describeMismatchSafely__with_full_force_2(){
        final StringDescription self = new StringDescription();
        final StringDescription issues = new StringDescription();
        final Iterable<Paper> input = asList(pap("PAP!"), pap("The Law Of Gravity"), pap("Booh!"), pap("PAP!"));
        final FluentIterableMatcher<Paper, Iterable<Paper>> matcher = anIterableOf(Paper.class)
            .ofSize(9)
            .ordered()
            .sorted(comparingInt(Paper::getPages))
            .unique((p1, p2) -> p1.text.equals(p2.text))
            .withItemsMatching(
                a(Paper.class).with(txt, "Booh!").with(pages, 3),
                a(Paper.class).with(txt, "PAP!").with(pages, 40),
                a(Paper.class).with(txt, "Grave").with(pages, 0)
            );

        final boolean matches = matcher.matchesSafely(input);

        assertThat(matches, is(false));

        matcher.describeTo(self);

        assertThat(self.toString(), is( "" +
            "an Iterable with the following properties:\n" +
            "- Iterable of Paper\n" +
            "- exactly 9 item(s)\n" +
            "- at least 3 matching item(s)\n" +
            "- sorted\n" +
            "- ordered\n" +
            "- no duplicates\n\n"
        ));

        matcher.describeMismatchSafely(input, issues);

        assertThat(issues.toString(), is("" +
            "Findings:\n" +
            "\"Size mismatch. Expected: 9. Actual was: 4.\"\n" +
            "\"Not all expectations were fulfilled.\"\n" +
            "\"Items did not appear in the expected order.\"\n" +
            "\"Collection is not sorted.\"\n" +
            "\"Detected duplicates.\"\n" +
            "\n" +
            "[0][Paper{text='PAP!', pages=40}  ]💕  ↔ 👯  \n" +
            "[1][Paper{text='The Law Of Gravity]    ↔      💔[ \ttext = \"Booh!\" <> \"The Law Of Gravity\" \tpages = <3> <> <180> \t] 💔[ \ttext = \"PAP!\" <> \"The Law Of Gravity\" \tpages = <40> <> <180> \t] 💔[ \ttext = \"Grave\" <> \"The Law Of Gravity\" \tpages = <0> <> <180> \t]\n" +
            "[2][Paper{text='Booh!', pages=50} ]  ↕ ↔      💔[ \tpages = <3> <> <50> \t] 💔[ \ttext = \"PAP!\" <> \"Booh!\" \tpages = <40> <> <50> \t] 💔[ \ttext = \"Grave\" <> \"Booh!\" \tpages = <0> <> <50> \t]\n" +
            "[3][Paper{text='PAP!', pages=40}  ]💕↕ ↔ 👯  \n" +
            "was <[Paper{text='PAP!', pages=40}, Paper{text='The Law Of Gravity', pages=180}, Paper{text='Booh!', pages=50}, Paper{text='PAP!', pages=40}]>"
        ));
    }
}