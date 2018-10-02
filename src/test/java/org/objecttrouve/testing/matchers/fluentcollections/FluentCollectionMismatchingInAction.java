/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentcollections;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparingInt;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.aCollectionOf;


@Ignore("Only for illustration.")
public class FluentCollectionMismatchingInAction {


    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__ofSize_2__1_actual__consistent_with__exactly(){

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    containsString("tem")
                )
                .ofSize(2)
                .exactly()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__ofSize_1__2_actual(){

        final List<String> strings = asList("item", "element");

        assertThat(strings, is(
            aCollectionOf(String.class)
                .ofSize(1)
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__1_actual_matching(){

        final List<String> strings = singletonList("item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                ))
        );
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__3_actual_matching__for_exactly(){

        final List<String> strings = asList("item", "item", "item");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
            .exactly()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__1_actual_matching__2_actual_not_matching(){

        final List<String> strings = asList("element", "element", "item");

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
    public void test__matchesSafely__mismatch__2_matcher_expectations__3_actual_none_matching(){

        final List<String> strings = asList("element", "element", "element");

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
    public void test__matchesSafely__mismatch__3_item_expectations__2_actual_matching__1_actual_non_matching(){

        final List<String> strings = asList("item", "element", "object");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__3_item_expectations__3_actual_none_matching(){

        final List<String> strings = asList("it", "element", "objection");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
            aCollectionOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__1(){

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
                    equalTo("not dump")
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__2(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
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
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__3(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
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

        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__all_matching__but_not_exactly(){

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump", "alternative", "facts");

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
            .exactly()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__1(){

        final List<String> strings = asList("fake", "news", "Donald", "Trump", "impeachment");

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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__2(){

        final List<String> strings = asList("Donald", "Trump", "fake", "news", "impeachment");

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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__3(){

        final List<String> strings = asList("impeachment", "Donald", "Trump", "fake", "news");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
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
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__4(){

        final List<String> strings = asList("impeachment", "Donald", "fake", "news", "Trump");

        /* There must be at least one matching item for every matcher. */
        assertThat(strings, is(
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
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__exactly(){

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
                .exactly()

        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__with_more_actuals_than_expected__1(){

        final List<String> strings = asList("news", "fake", "alternative", "facts", "impeachment", "Donald", "Trump");

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
    public void test__matchesSafely__mismatch__items_expected_missing(){

        final List<String> strings = asList("alternative", "facts", "impeachment", "Trump");

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
    public void test__matchesSafely__mismatch__all_items_expected_missing(){

        final List<String> strings = emptyList();

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
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__1(){

        final List<String> strings = asList("D", "B", "C");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__2(){

        final List<String> strings = asList("B", "A", "A",  "C","C", "D", "E", "F", "F");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__3(){

        final List<String> strings = asList( "A", "B", "A", "C","C", "D", "E", "F", "F");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__4(){

        final List<String> strings = asList( "A",  "A", "C","C", "D", "E", "F", "F", "B");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_1(){

        final List<String> strings = asList("AAA", "BB", "CCC", "DDD", "EEEE");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_2(){

        final List<String> strings = asList("AA", "BB", "CCC", "DDD", "EE");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_3(){

        final List<String> strings = asList("A", "BB", "CCCC", "DDD", "EEEE");

        assertThat(strings, is(
            aCollectionOf(String.class).sorted(comparingInt(String::length))
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__1(){

        final List<String> strings = asList("doubleton", "doubleton");

        assertThat(strings, is(
            aCollectionOf(String.class).unique()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__2(){

        final List<String> strings = asList("singleton", "doubleton", "doubleton", "tripleton");

        assertThat(strings, is(
            aCollectionOf(String.class).unique()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__3(){

        final List<String> strings = asList("singleton", "singleton", "doubleton", "tripleton");

        assertThat(strings, is(
            aCollectionOf(String.class).unique()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__4(){

        final List<String> strings = asList("singleton", "singleton", "doubleton", "tripleton", "tripleton");

        assertThat(strings, is(
            aCollectionOf(String.class).unique()
        ));
    }



    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__with_equator_function(){

        final List<String> strings = asList("x", "yy", "zzz", "åå");

        assertThat(strings, is(
            aCollectionOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        ));
    }

}