/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ProseTest {


    private static final Prose<String> stringProse = new Prose<>();
    private static final Prose<Boolean> boolProse = new Prose<>();

    @Test
    public void test__describeExpectations__no_expectations() {

        final StringBuilder description = new StringBuilder();

        stringProse.describeExpectations(new Settings(), description::append);

        assertThat(description.toString(), is("" +
            "an Iterable with the following properties:\n" +
            "- Iterable of Object\n\n" +
            ""));
    }


    @Test
    public void test__describeExpectations__class() {

        final Settings settings = new Settings();
        settings.klass = Integer.class;
        final StringBuilder description = new StringBuilder();

        stringProse.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("" +
            "an Iterable with the following properties:\n" +
            "- Iterable of Integer\n\n" +
            ""));
    }

    @Test
    public void test__describeExpectations__explicit_size() {

        final Settings settings = new Settings();
        settings.expectedSize = 0;
        final StringBuilder description = new StringBuilder();

        stringProse.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("" +
            "an Iterable with the following properties:\n" +
            "- Iterable of Object\n" +
            "- exactly 0 item(s)\n\n"));
    }

    @Test
    public void test__describeExpectations__nr_of_expected_items() {

        final Settings settings = new Settings();
        settings.expectations = new Matcher[]{nullValue(), nullValue(), nullValue()};
        final StringBuilder description = new StringBuilder();

        stringProse.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("" +
            "an Iterable with the following properties:\n" +
            "- Iterable of Object\n" +
            "- at least 3 matching item(s)\n\n"));
    }


    @Test
    public void test__describeExpectations__without_any_unexpected_items() {

        final Settings settings = new Settings();
        settings.mustNotHaveUnexpectedItems = true;
        final StringBuilder description = new StringBuilder();

        stringProse.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("an Iterable with the following properties:\n" +
            "- Iterable of Object\n" +
            "- no unexpected items\n" +
            "\n"));
    }


    @Test
    public void test__describeExpectations__ordered() {

        final Settings settings = new Settings();
        settings.ordered = true;
        final StringBuilder description = new StringBuilder();

        stringProse.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("an Iterable with the following properties:\n" +
            "- Iterable of Object\n" +
            "- ordered\n" +
            "\n"));
    }

    @Test
    public void test__describeExpectations__sorted() {

        final Settings settings = new Settings();
        settings.sorted = true;
        final StringBuilder description = new StringBuilder();

        stringProse.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("an Iterable with the following properties:\n" +
            "- Iterable of Object\n" +
            "- sorted\n" +
            "\n"));
    }

    @Test
    public void test__describeExpectations__unique() {

        final Settings settings = new Settings();
        settings.unique = true;
        final StringBuilder description = new StringBuilder();

        stringProse.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("an Iterable with the following properties:\n" +
            "- Iterable of Object\n" +
            "- no duplicates\n" +
            "\n"));
    }


    @Test
    public void test__describeExpectations__all_expectations() {

        final Settings settings = new Settings();
        settings.klass = String.class;
        settings.expectations = new Matcher[]{nullValue(), nullValue(), nullValue()};
        settings.expectedSize = 3;
        settings.mustNotHaveUnexpectedItems = true;
        settings.sorted = true;
        settings.ordered = true;
        settings.unique = true;
        final StringBuilder description = new StringBuilder();

        stringProse.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("" +
            "an Iterable with the following properties:\n" +
            "- Iterable of String\n" +
            "- exactly 3 item(s)\n" +
            "- at least 3 matching item(s)\n" +
            "- no unexpected items\n" +
            "- sorted\n" +
            "- ordered\n" +
            "- no duplicates\n" +
            "\n"));
    }

    @Test
    public void test__actualItemString__null_arg() {
        final String s = stringProse.actualItemString(null, 15);

        assertThat(s, is("null           "));
    }

    @Test
    public void test__actualItemString__empty_arg() {
        final String s = stringProse.actualItemString("", 15);

        assertThat(s, is("               "));
    }

    @Test
    public void test__actualItemString__happy_arg() {
        final String s = stringProse.actualItemString("xxxxxxxxxxxxxxx", 15);

        assertThat(s, is("xxxxxxxxxxxxxxx"));
    }

    @Test
    public void test__actualItemString__newln_arg() {
        final String s = stringProse.actualItemString("xxxxxxx\nxxxxxxx", 15);

        assertThat(s, is("xxxxxxx xxxxxxx"));
    }

    @Test
    public void test__actualItemString__trunc_arg() {
        final String s = stringProse.actualItemString("xxxxxxxxxxxxxxxXXXXXXXX", 15);

        assertThat(s, is("xxxxxxxxxxxxxxx"));
    }

    @Test
    public void test__actualItemString__pad_arg() {
        final String s = stringProse.actualItemString("xxxxx", 15);

        assertThat(s, is("xxxxx          "));
    }

    @Test
    public void test__actualItemString__pad_arg__0() {
        final String s = stringProse.actualItemString("xxxxx", 1);

        assertThat(s, is("x"));
    }

    @Test
    public void test__matcherSaying__equals_matcher() {
        final Matcher<String> matcher = CoreMatchers.equalTo("Y");
        matcher.matches("X");
        final StringDescription self = new StringDescription();
        final StringDescription mismatch = new StringDescription();
        matcher.describeTo(self);
        matcher.describeMismatch("X", mismatch);

        final String matcherSaying = stringProse.matcherSaying(self.toString());

        assertThat(matcherSaying, is("\"Y\""));
    }

    private static class MatcherWithNewLines implements Matcher<Object> {
        @Override
        public boolean matches(final Object item) {
            return false;
        }

        @Override
        public void describeMismatch(final Object item, final Description mismatchDescription) {
            mismatchDescription.appendText("x\nx");
        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("y\ny");
        }
    }

    @Test
    public void test__matcherSaying__matcher_with_newlines() {
        final Matcher<Object> matcher = new MatcherWithNewLines();
        final StringDescription self = new StringDescription();
        final StringDescription mismatch = new StringDescription();
        matcher.describeTo(self);
        matcher.describeMismatch("X", mismatch);

        final String matcherSaying = stringProse.matcherSaying(self.toString());

        assertThat(matcherSaying, is("y y"));
    }

    @Test
    public void line__nothing_to_report() {
        final ItemResult r1 = ItemResult.builder(null)
            .withIndex(0)
            .matched(false)
            .build();

        //noinspection unchecked
        final String line = stringProse.line(r1, 1, 1);

        assertThat(line, is("[0][n]          "));
    }

    @Test
    public void line__nothing_to_report__item_length_4() {
        final ItemResult r1 = ItemResult.builder(null)
            .withIndex(0)
            .matched(false)
            .build();

        //noinspection unchecked
        final String line = stringProse.line(r1, 1, 4);

        assertThat(line, is("[0][null]          "));
    }

    @Test
    public void line__matched_item() {
        final ItemResult<Boolean> r1 = ItemResult.builder(true)
            .withIndex(0)
            .matched(true)
            .build();

        //noinspection unchecked
        final String line = boolProse.line(r1, 1, 4);

        assertThat(line, is("[0][true]💕        "));
    }

    @Test
    public void line__index_0__item_breaking_item_order() {
        final ItemResult<Boolean> r1 = ItemResult.builder(true)
            .withIndex(0)
            .breakingItemOrder(true)
            .build();

        //noinspection unchecked
        final String line = boolProse.line(r1, 1, 4);

        assertThat(line, is("[0][true]    ↔     "));
    }

    @Test
    public void line__index_1__item_breaking_sort_order() {
        final ItemResult<Boolean> r1 = ItemResult.builder(true)
            .withIndex(1)
            .breakingSortOrder(true)
            .build();

        //noinspection unchecked
        final String line = boolProse.line(r1, 1, 4);

        assertThat(line, is("[1][true]  ↕       "));
    }

    @Test
    public void line__index_22__duplicate_item__of__100() {
        final ItemResult<Boolean> r = ItemResult.builder(true)
            .withIndex(22)
            .duplicate(true)
            .build();

        //noinspection unchecked
        final String line = boolProse.line(r, 100, 4);

        assertThat(line, is("[ 22][true]      👯  "));
    }

    @Test
    public void line__index_22__unwanted_item__of__1000__among_items_of_10() {
        final ItemResult<Boolean> r = ItemResult.builder(true)
            .withIndex(22)
            .unwanted(true)
            .build();

        //noinspection unchecked
        final String line = boolProse.line(r, 1000, 10);

        assertThat(line, is("[  22][true      ]        🚯"));
    }


    @Test
    public void line__index_22__mismatched_item__of__1000__imaginary_truncated() {

        final ItemResult<Boolean> r = ItemResult.builder(true)
            .withIndex(22)
            .withMatchers(singletonList(mwi(nullValue(), 3)))
            .build();

        //noinspection unchecked
        final String line = boolProse.line(r, 1000, 3);

        assertThat(line, is("[  22][tru]           💔[3][null]"));
    }

    private ItemResult.MatcherWithIndex mwi(final Matcher matcher, final int index) {
        return new ItemResult.MatcherWithIndex(matcher, index);
    }

    @Test
    public void line__s__with_all_of_it() {
        final ItemResult<String> r1 = ItemResult.builder("scene de menage")
            .withIndex(0)
            .matched(false)
            .withMatchers(singletonList(mwi(equalTo("scène de ménage"), 0)))
            .build();
        final ItemResult<String> r2 = ItemResult.builder("scene de manège")
            .withIndex(1)
            .matched(false)
            .breakingItemOrder(true)
            .breakingSortOrder(true)
            .withMatchers(asList(mwi(equalTo("scène de ménage"),2), mwi(endsWith("age"),3)))
            .build();
        final ItemResult<String> r3 = ItemResult.builder("le mariage")
            .withIndex(99)
            .matched(false)
            .breakingItemOrder(true)
            .breakingSortOrder(true)
            .duplicate(true)
            .unwanted(true)
            .withMatchers(singletonList(mwi(equalTo("scène de ménage"),0)))
            .build();
        final ItemResult<String> r4 = ItemResult.builder("scène de ménage")
            .withIndex(9999)
            .matched(true)
            .breakingItemOrder(true)
            .breakingSortOrder(true)
            .duplicate(true)
            .unwanted(true)
            .build();

        //noinspection unchecked
        final String line1 = stringProse.line(r1, 100, 15);
        final String line2 = stringProse.line(r2, 100, 15);
        final String line3 = stringProse.line(r3, 100, 15);
        final String line4 = stringProse.line(r4, 100, 15);

        assertThat(line1, is("[  0][scene de menage]           💔[0][\"scène de ménage\"]"));
        assertThat(line2, is("[  1][scene de manège]  ↕ ↔      💔[2][\"scène de ménage\"] 💔[3][a string ending with \"age\"]"));
        assertThat(line3, is("[ 99][le mariage     ]  ↕ ↔ 👯🚯 💔[0][\"scène de ménage\"]"));
        assertThat(line4, is("[999][scène de ménage]💕↕ ↔ 👯🚯"));
    }

}
