/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import org.junit.Ignore;
import org.junit.Test;
import org.objecttrouve.testing.matchers.fluentatts.Attribute;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparingInt;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.anIterableOf;


@Ignore("Failing intentionally.")
public class Examples {

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__ofSize_2__1_actual__consistent_with__exactly() {

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    containsString("tem")
                )
                .ofSize(2)
                .exactly()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__ofSize_1__2_actual() {

        final List<String> strings = asList("item", "element");

        assertThat(strings, is(
            anIterableOf(String.class)
                .ofSize(1)
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__1_actual_matching() {

        final List<String> strings = singletonList("item");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                ))
        );
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__3_actual_matching__for_exactly() {

        final List<String> strings = asList("item", "item", "item");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
                .exactly()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__1_actual_matching__2_actual_not_matching() {

        final List<String> strings = asList("element", "element", "item");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__3_actual_none_matching() {

        final List<String> strings = asList("element", "element", "element");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__3_item_expectations__2_actual_matching__1_actual_non_matching() {

        final List<String> strings = asList("item", "element", "object");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__3_item_expectations__3_actual_none_matching() {

        final List<String> strings = asList("it", "element", "objection");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__1() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");


        assertThat(strings, is(
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
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__2() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");


        assertThat(strings, is(
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
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__3() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");


        assertThat(strings, is(
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

        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__all_matching__but_not_exactly() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump", "alternative", "facts");


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
                .exactly()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__1() {

        final List<String> strings = asList("fake", "news", "Donald", "Trump", "impeachment");


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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__2() {

        final List<String> strings = asList("Donald", "Trump", "fake", "news", "impeachment");


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
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__3() {

        final List<String> strings = asList("impeachment", "Donald", "Trump", "fake", "news");

        assertThat(strings, is(
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
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__4() {

        final List<String> strings = asList("impeachment", "Donald", "fake", "news", "Trump");


        assertThat(strings, is(
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
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__exactly() {

        final List<String> strings = asList("alternative", "fake", "news", "facts", "impeachment", "Donald", "Trump");


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
                .exactly()

        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__with_more_actuals_than_expected__1() {

        final List<String> strings = asList("news", "fake", "alternative", "facts", "impeachment", "Donald", "Trump");


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
    public void test__matchesSafely__mismatch__items_expected_missing() {

        final List<String> strings = asList("alternative", "facts", "impeachment", "Trump");

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
    public void test__matchesSafely__mismatch__all_items_expected_missing() {

        final List<String> strings = emptyList();

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
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__1() {

        final List<String> strings = asList("D", "B", "C");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__2() {

        final List<String> strings = asList("B", "A", "A", "C", "C", "D", "E", "F", "F");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__3() {

        final List<String> strings = asList("A", "B", "A", "C", "C", "D", "E", "F", "F");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__4() {

        final List<String> strings = asList("A", "A", "C", "C", "D", "E", "F", "F", "B");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_1() {

        final List<String> strings = asList("AAA", "BB", "CCC", "DDD", "EEEE");

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_2() {

        final List<String> strings = asList("AA", "BB", "CCC", "DDD", "EE");

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_3() {

        final List<String> strings = asList("A", "BB", "CCCC", "DDD", "EEEE");

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__1() {

        final List<String> strings = asList("doubleton", "doubleton");

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__2() {

        final List<String> strings = asList("singleton", "doubleton", "doubleton", "tripleton");

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__3() {

        final List<String> strings = asList("singleton", "singleton", "doubleton", "tripleton");

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__4() {

        final List<String> strings = asList("singleton", "singleton", "doubleton", "tripleton", "tripleton");

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__with_equator_function() {

        final List<String> strings = asList("x", "yy", "zzz", "åå");

        assertThat(strings, is(
            anIterableOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        ));
    }

    private static final Attribute<String, String> prefix = Attribute.attribute("prefix", s -> s.substring(0,2));
    private static final Attribute<String, String> suffix = Attribute.attribute("suffix", s -> s.substring(2,s.length()));


    @Test
    public void heavyMismatch() {

        final List<String> strings = asList(
            "fake",
            "impeachment",
            "Donald",
            "Trump",
            "fake",
            "news"
        );

        assertThat(strings, is(
            anIterableOf(String.class)
                .exactly()
                .sorted()
                .ordered()
                .unique()
                .withItemsMatching(
                    startsWith("Ron"),
                    endsWith("ment")
                )
                .withItems(
                    "true",
                    "news",
                    "impeachment"
                )
        ));
    }

    @Test
    public void full_force_mismatch() {

        final List<String> strings = asList("fake", "impeachment", "Donald", "Trump", "fake", "news");

        assertThat(strings, is(
            anIterableOf(String.class)
                .ofSize(9)
                .sorted()
                .ordered()
                .unique()
                .withItemsMatching(
                    a(String.class).with(prefix, "Ron").with(suffix, "bard"),
                    a(String.class).with(prefix, "Ron").with(suffix, "nald")
                )
                .withItems(
                    "true",
                    "news",
                    "impeachment"
                )
        ));
    }

}