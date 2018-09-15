/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentcollections;

import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparingInt;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.aCollectionOf;

public class FluentCollectionMatcherTest {

    @Test
    public void test__matchesSafely__match__empty_expectation__no_requirements__empty_actual(){

        final List<String> strings = emptyList();

        assertThat(strings, is(aCollectionOf(String.class)));
    }

    @Test
    public void test__matchesSafely__mismatch__empty_expectation__no_requirements__null_actual(){

        final List<String> strings = null;

        assertThat(strings, not(is(aCollectionOf(String.class))));
    }

    @Test
    public void test__matchesSafely__match__empty_expectation__no_requirements__non_empty_actual(){

        final List<String> strings = singletonList("item");

        assertThat(strings, is(aCollectionOf(String.class)));
    }

    @Test
    public void test__matchesSafely__mismatch__empty_expectation__exactly__non_empty_actual(){

        final List<String> strings = singletonList("item");

        assertThat(strings, not(is(
            aCollectionOf(String.class)
                .exactly()
        )));
    }

    @Test
    public void test__matchesSafely__match__matcher_expectation__exactly__non_empty_actual(){

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(containsString("it"))
                .exactly()
        ));
    }

    @Test
    public void test__matchesSafely__match__matcher_expectation__no_requirements__non_empty_actual(){

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(containsString("it"))
        ));
    }

    @Test
    public void test__matchesSafely__match__1_matcher_expectation__ofSize_1__1_actual(){

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(containsString("it"))
                .ofSize(1)
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__1_matcher_expectation__ofSize_2__1_actual(){

        final List<String> strings = singletonList("item");

        assertThat(strings, not(is(
            aCollectionOf(String.class)
                .withItemsMatching(containsString("it"))
                .ofSize(2)
        )));
    }

    @Test
    public void test__matchesSafely__match__1_matcher_expectation__ofSize_1__1_actual__consistent_with__exactly(){

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(containsString("it"))
                .ofSize(1)
                .exactly()
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__matchesSafely__error__1_matcher_expectation__ofSize_2__1_actual__inconsistent_with__exactly(){

        final List<String> strings = singletonList("item");
        final FluentCollectionMatcher<String, Collection<String>> matcher = aCollectionOf(String.class)
            .withItemsMatching(containsString("it"))
            .ofSize(2)
            .exactly();

        matcher.matchesSafely(strings);

    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__ofSize_2__1_actual__consistent_with__exactly(){

        final List<String> strings = singletonList("item");

        assertThat(strings, not(is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    containsString("tem")
                )
                .ofSize(2)
                .exactly()
        )));
    }

    @Test(expected=IllegalArgumentException.class)
    public void test__matchesSafely__error__3_matcher_expectations__ofSize_2__1_actual__inconsistent_with__exactly(){

        final List<String> strings = singletonList("item");
        final FluentCollectionMatcher<String, Collection<String>> matcher = aCollectionOf(String.class)
            .withItemsMatching(
                containsString("it"),
                containsString("tem")
            )
            .ofSize(3)
            .exactly();

        matcher.matchesSafely(strings);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test__matchesSafely__error__2_matcher_expectations__ofSize_1__1_actual__inconsistent_with__exactly(){

        final List<String> strings = singletonList("item");
        final FluentCollectionMatcher<String, Collection<String>> matcher = aCollectionOf(String.class)
            .withItemsMatching(
                containsString("it"),
                containsString("tem")
            )
            .ofSize(1)
            .exactly();

        matcher.matchesSafely(strings);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test__matchesSafely__error__2_matcher_expectations__ofSize_1__1_actual__inconsistent(){

        final List<String> strings = singletonList("item");
        final FluentCollectionMatcher<String, Collection<String>> matcher = aCollectionOf(String.class)
            .withItemsMatching(
                containsString("it"),
                containsString("tem")
            )
            .ofSize(1);

        matcher.matchesSafely(strings);
    }

    @Test
    public void test__matchesSafely__match__ofSize_1__1_actual(){

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            aCollectionOf(String.class)
                .ofSize(1)
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__ofSize_1__2_actual(){

        final List<String> strings = asList("item", "element");

        assertThat(strings, not(is(
            aCollectionOf(String.class)
                .ofSize(1)
        )));
    }

    @Test
    public void test__matchesSafely__match__1_matcher_expectation__1_actual(){

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(containsString("it"))
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__1_actual_matching(){

        final List<String> strings = singletonList("item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                ))
        ));
    }

    @Test
    public void test__matchesSafely__match__2_matcher_expectations__2_actual_matching(){

        final List<String> strings = asList("item", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        ));
    }

    @Test
    public void test__matchesSafely__match__2_matcher_expectations__3_actual_matching(){

        final List<String> strings = asList("item", "item", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__3_actual_matching__for_exactly(){

        final List<String> strings = asList("item", "item", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
            .exactly()
        )));
    }


    @Test
    public void test__matchesSafely__match__2_matcher_expectations__2_actual_matching__1_actual_not_matching(){

        final List<String> strings = asList("item", "element", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__1_actual_matching__2_actual_not_matching(){

        final List<String> strings = asList("element", "element", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        )));
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__3_actual_none_matching(){

        final List<String> strings = asList("element", "element", "element");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        )));
    }

    @Test
    public void test__matchesSafely__match__3_matcher_expectations__3_actual_all_matching(){

        final List<String> strings = asList("item", "element", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    containsString("l"),
                    is("item")
                )
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__ofSize__error__negative_arg(){

        aCollectionOf(String.class).ofSize(-1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void test__sorted__error__requested_sorting_for_non_Comparable(){

        aCollectionOf(Object.class).sorted();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__withItemsMatching__error__null_arg(){

        //noinspection ConfusingArgumentToVarargsMethod
        aCollectionOf(Object.class).withItemsMatching(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__withItems__error__null_arg(){

        //noinspection ConfusingArgumentToVarargsMethod
        aCollectionOf(Object.class).withItems(null);
    }

    @Test
    public void test__matchesSafely__match__3_item_expectations__3_actual_all_matching(){

        final List<String> strings = asList("item", "element", "object");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItems(
                    "item",
                    "element",
                    "object"
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__3_item_expectations__2_actual_matching__1_actual_non_matching(){

        final List<String> strings = asList("item", "element", "object");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        )));
    }

    @Test
    public void test__matchesSafely__mismatch__3_item_expectations__3_actual_none_matching(){

        final List<String> strings = asList("it", "element", "objection");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        )));
    }

    @Test
    public void test__matchesSafely__match__mixed_item_and_matcher_expectations__all_matching__1(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__match__mixed_item_and_matcher_expectations__all_matching__2(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__1(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__2(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__3(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__all_matching__but_not_exactly(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump", "alternative", "facts");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__match__mixed_item_and_matcher_expectations__ordered(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__match__mixed_item_and_matcher_expectations__ordered__and_matchers_match_multiple_items__1(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)

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
    public void test__matchesSafely__match__mixed_item_and_matcher_expectations__ordered__and_matchers_match_multiple_items__2(){

        final List<String> strings = asList("faake", "neeeews", "impeaaaachment", "Doneeld", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)

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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__1(){

        final List<String> strings = asList("fake", "news", "Donald", "Trump", "impeachment");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__2(){

        final List<String> strings = asList("Donald", "Trump", "fake", "news", "impeachment");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__3(){

        final List<String> strings = asList("impeachment", "Donald", "Trump", "fake", "news");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__4(){

        final List<String> strings = asList("impeachment", "Donald", "fake", "news", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__match__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__1(){

        final List<String> strings = asList("fake", "news",  "alternative", "facts", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__match__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__2(){

        final List<String> strings = asList("alternative", "fake", "news",  "facts", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__exactly(){

        final List<String> strings = asList("alternative", "fake", "news",  "facts", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__match__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__3(){

        final List<String> strings = asList("alternative", "fake", "news", "impeachment", "Donald", "Trump",  "facts");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__with_more_actuals_than_expected__1(){

        final List<String> strings = asList("news", "fake", "alternative", "facts", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__items_expected_missing(){

        final List<String> strings = asList("alternative", "facts", "impeachment", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__mismatch__all_items_expected_missing(){

        final List<String> strings = emptyList();

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, not(is(
            aCollectionOf(String.class)
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
    public void test__matchesSafely__match__sorted__empty_Collection(){

        final List<String> strings = emptyList();

        assertThat(strings, is(
            aCollectionOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__match__sorted__Collection_with_1_element(){

        final List<String> strings = singletonList("B");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__match__sorted__sorted_Collection__1(){

        final List<String> strings = asList("A", "B", "C");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__match__sorted__sorted_Collection__2(){

        final List<String> strings = asList("A", "A", "B", "C","C", "D", "E", "F", "F");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__1(){

        final List<String> strings = asList("D", "B", "C");

        assertThat(strings, not(is(
            aCollectionOf(String.class).sorted()
        )));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__2(){

        final List<String> strings = asList("B", "A", "A",  "C","C", "D", "E", "F", "F");

        assertThat(strings, not(is(
            aCollectionOf(String.class).sorted()
        )));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__3(){

        final List<String> strings = asList( "A", "B", "A", "C","C", "D", "E", "F", "F");

        assertThat(strings, not(is(
            aCollectionOf(String.class).sorted()
        )));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__4(){

        final List<String> strings = asList( "A",  "A", "C","C", "D", "E", "F", "F", "B");

        assertThat(strings, not(is(
            aCollectionOf(String.class).sorted()
        )));
    }

    @Test
    public void test__matchesSafely__match__sorted__empty_Collection__using_Comparator(){

        final List<String> strings = emptyList();

        assertThat(strings, is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__match__sorted__Collection_with_1_element__using_Comparator(){

        final List<String> strings = singletonList("B");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__match__sorted__sorted_Collection__using_Comparator_1(){

        final List<String> strings = asList("A", "B", "C");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__match__sorted__sorted_Collection__using_Comparator_2(){

        final List<String> strings = asList("A", "BB", "CCC");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__match__sorted__sorted_Collection__using_Comparator_3(){

        final List<String> strings = asList("AA", "BB", "CCC", "DDD", "EEEE");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_1(){

        final List<String> strings = asList("AAA", "BB", "CCC", "DDD", "EEEE");

        assertThat(strings, not(is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        )));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_2(){

        final List<String> strings = asList("AA", "BB", "CCC", "DDD", "EE");

        assertThat(strings, not(is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        )));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_3(){

        final List<String> strings = asList("A", "BB", "CCCC", "DDD", "EEEE");

        assertThat(strings, not(is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        )));
    }

    @Test
    public void test__matchesSafely__match__unique__empty_Collection(){

        final List<String> strings = emptyList();

        assertThat(strings, is(
            aCollectionOf(String.class).unique()
        ));
    }

    @Test
    public void test__matchesSafely__match__unique__Collection_with_1_item(){

        final List<String> strings = singletonList("singleton");

        assertThat(strings, is(
            aCollectionOf(String.class).unique()
        ));
    }


    @Test
    public void test__matchesSafely__match__unique__Collection_with_3_unique_items(){

        final List<String> strings = asList("singleton", "doubleton", "tripleton");

        assertThat(strings, is(
            aCollectionOf(String.class).unique()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__1(){

        final List<String> strings = asList("doubleton", "doubleton");

        assertThat(strings, not(is(
            aCollectionOf(String.class).unique()
        )));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__2(){

        final List<String> strings = asList("singleton", "doubleton", "doubleton", "tripleton");

        assertThat(strings, not(is(
            aCollectionOf(String.class).unique()
        )));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__3(){

        final List<String> strings = asList("singleton", "singleton", "doubleton", "tripleton");

        assertThat(strings, not(is(
            aCollectionOf(String.class).unique()
        )));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__4(){

        final List<String> strings = asList("singleton", "singleton", "doubleton", "tripleton", "tripleton");

        assertThat(strings, not(is(
            aCollectionOf(String.class).unique()
        )));
    }


    @Test
    public void test__matchesSafely__match__unique__empty_Collection__with_equator_function(){

        final List<String> strings = emptyList();

        assertThat(strings, is(
            aCollectionOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        ));
    }

    @Test
    public void test__matchesSafely__match__unique__Collection_with_1_item__with_equator_function(){

        final List<String> strings = singletonList("x");

        assertThat(strings, is(
            aCollectionOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        ));
    }

    @Test
    public void test__matchesSafely__match__unique__Collection_without_duplicates__with_equator_function(){

        final List<String> strings = asList("x", "yy", "zzz");

        assertThat(strings, is(
            aCollectionOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__with_equator_function(){

        final List<String> strings = asList("x", "yy", "zzz", "åå");

        assertThat(strings, not(is(
            aCollectionOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        )));
    }


    @Test
    public void test__matchesSafely__resets(){

        final List<String> strings = asList("x", "zzz", "yy", "yy", "åå");
        final List<String> strings2 = asList("x", "yy", "zzz");
        final FluentCollectionMatcher<String, Collection<String>> matching = aCollectionOf(String.class)
            .exactly()
            .sorted()
            .ordered()
            .unique()
            .withItems("x", "yy", "zzz");

        assertThat(strings, not(is(matching)));
        assertThat(strings2, is(matching));
    }
}