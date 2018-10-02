/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentcollections;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ProseTest {

    
    private static final Prose<String> prose = new Prose<>();
    
    @Test
    public void test__describeExpectations__no_expectations(){

        final StringBuilder description = new StringBuilder();

        prose.describeExpectations(new Settings(), description::append);

        assertThat(description.toString(), is("" +
            "a collection with the following properties:\n" +
            "- collection of Object\n\n" +
            ""));
    }


    @Test
    public void test__describeExpectations__class(){

        final Settings settings = new Settings();
        settings.klass = Integer.class;
        final StringBuilder description = new StringBuilder();

        prose.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("" +
            "a collection with the following properties:\n" +
            "- collection of Integer\n\n" +
            ""));
    }

    @Test
    public void test__describeExpectations__explicit_size(){

        final Settings settings = new Settings();
        settings.expectedSize = 0;
        final StringBuilder description = new StringBuilder();

        prose.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("" +
            "a collection with the following properties:\n" +
            "- collection of Object\n" +
            "- exactly 0 item(s)\n\n"));
    }

    @Test
    public void test__describeExpectations__nr_of_expected_items(){

        final Settings settings = new Settings();
        settings.expectations = new Matcher[]{nullValue(), nullValue(), nullValue()};
        final StringBuilder description = new StringBuilder();

        prose.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("" +
            "a collection with the following properties:\n" +
            "- collection of Object\n" +
            "- at least 3 matching item(s)\n\n"));
    }


    @Test
    public void test__describeExpectations__without_any_unexpected_items(){

        final Settings settings = new Settings();
        settings.mustNotHaveUnexpectedItems = true;
        final StringBuilder description = new StringBuilder();

        prose.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("a collection with the following properties:\n" +
            "- collection of Object\n" +
            "- no unexpected items\n" +
            "\n"));
    }


    @Test
    public void test__describeExpectations__ordered(){

        final Settings settings = new Settings();
        settings.ordered = true;
        final StringBuilder description = new StringBuilder();

        prose.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("a collection with the following properties:\n" +
            "- collection of Object\n" +
            "- ordered\n" +
            "\n"));
    }

    @Test
    public void test__describeExpectations__sorted(){

        final Settings settings = new Settings();
        settings.sorted = true;
        final StringBuilder description = new StringBuilder();

        prose.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("a collection with the following properties:\n" +
            "- collection of Object\n" +
            "- sorted\n" +
            "\n"));
    }

    @Test
    public void test__describeExpectations__unique(){

        final Settings settings = new Settings();
        settings.unique = true;
        final StringBuilder description = new StringBuilder();

        prose.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("a collection with the following properties:\n" +
            "- collection of Object\n" +
            "- no duplicates\n" +
            "\n"));
    }


    @Test
    public void test__describeExpectations__all_expectations(){

        final Settings settings = new Settings();
        settings.klass = String.class;
        settings.expectations = new Matcher[]{nullValue(), nullValue(), nullValue()};
        settings.expectedSize = 3;
        settings.mustNotHaveUnexpectedItems = true;
        settings.sorted = true;
        settings.ordered = true;
        settings.unique = true;
        final StringBuilder description = new StringBuilder();

        prose.describeExpectations(settings, description::append);

        assertThat(description.toString(), is("" +
            "a collection with the following properties:\n" +
            "- collection of String\n" +
            "- exactly 3 item(s)\n" +
            "- at least 3 matching item(s)\n" +
            "- no unexpected items\n" +
            "- sorted\n" +
            "- ordered\n" +
            "- no duplicates\n" +
            "\n"));
    }

    @Test
    public void test__actualItemString__null_arg(){
        final String s = prose.actualItemString(null);

        assertThat(s, is("null           "));
    }

    @Test
    public void test__actualItemString__empty_arg(){
        final String s = prose.actualItemString("");

        assertThat(s, is("               "));
    }

    @Test
    public void test__actualItemString__happy_arg(){
        final String s = prose.actualItemString("xxxxxxxxxxxxxxx");

        assertThat(s, is("xxxxxxxxxxxxxxx"));
    }

    @Test
    public void test__actualItemString__newln_arg(){
        final String s = prose.actualItemString("xxxxxxx\nxxxxxxx");

        assertThat(s, is("xxxxxxx xxxxxxx"));
    }

    @Test
    public void test__actualItemString__trunc_arg(){
        final String s = prose.actualItemString("xxxxxxxxxxxxxxxXXXXXXXX");

        assertThat(s, is("xxxxxxxxxxxxxxx"));
    }

    @Test
    public void test__actualItemString__pad_arg(){
        final String s = prose.actualItemString("xxxxx");

        assertThat(s, is("xxxxx          "));
    }

    @Test
    public void test__actualItemString__custom_stringifier(){
        final String s = new Prose<String>(str -> "str").actualItemString("xxxxxxxxxxxxxxxXXXXXXXX");

        assertThat(s, is("str"));
    }

    @Test
    public void test__matcherSaying__equals_matcher(){
        final Matcher<String> matcher = CoreMatchers.equalTo("Y");
        matcher.matches("X");
        final StringDescription self = new StringDescription();
        final StringDescription mismatch = new StringDescription();
        matcher.describeTo(self) ;
        matcher.describeMismatch("X", mismatch);

        final String matcherSaying = prose.matcherSaying(self.toString(), mismatch.toString());

        assertThat(matcherSaying, is("\"Y\" was \"X\""));
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
    public void test__matcherSaying__matcher_with_newlines(){
        final Matcher<Object> matcher = new MatcherWithNewLines();
        final StringDescription self = new StringDescription();
        final StringDescription mismatch = new StringDescription();
        matcher.describeTo(self) ;
        matcher.describeMismatch("X", mismatch);

        final String matcherSaying = prose.matcherSaying(self.toString(), mismatch.toString());

        assertThat(matcherSaying, is("y y x x"));
    }

    @SuppressWarnings("unchecked")
    @Ignore("Only for illustrative purposes.")
    @Test
    public void test__itemString(){

        final ItemResult r1 = ItemResult.<String>builder(null)
            .withIndex(0)
            .matched(true)
            .withMatchers(singletonList(equalTo("y")))
            .build();

        final ItemResult r2 = ItemResult.builder("pooooh")
            .withIndex(1)
            .matched(false)
            .withMatchers(singletonList(equalTo("y")))
            .build();

        final ItemResult r3 = ItemResult.builder("fooooo")
            .withIndex(2)
            .matched(false)
            .breakingSortOrder(true)
            .withMatchers(singletonList(equalTo("y")))
            .build();

        final ItemResult r4 = ItemResult.builder(Arrays.asList("XXXXX", "YYYYY").toString())
            .withIndex(3)
            .matched(false)
            .breakingSortOrder(true)
            .breakingItemOrder(true)
            .withMatchers(singletonList(equalTo("y")))
            .build();


        final ItemResult r5 = ItemResult.builder(Arrays.asList("XXXXX", "YYYYY", "ZZZZZZZZZZZZZZ").toString())
            .withIndex(4)
            .matched(false)
            .breakingSortOrder(true)
            .breakingItemOrder(true)
            .duplicate(true)
            .withMatchers(asList(equalTo("y"), equalTo("z")))
            .build();

        final ItemResult r6 = ItemResult.builder(Arrays.asList("XXXXX", "YYYYY", "ZZZZZZZZZZZZZZ").toString())
            .withIndex(5)
            .matched(false)
            .breakingSortOrder(true)
            .breakingItemOrder(false)
            .duplicate(false)
            .obsolete(true)
            .withMatchers(asList(equalTo("y"), equalTo("z")))
            .build();

        final ItemResult r7 = ItemResult.builder(Arrays.asList("XXXXX", "YYYYY", "ZZZZZZZZZZZZZZ").toString())
            .withIndex(6)
            .matched(true)
            .breakingSortOrder(true)
            .breakingItemOrder(true)
            .duplicate(true)
            .obsolete(true)
            .withMatchers(asList(equalTo("y"), equalTo("z")))
            .build();


        System.out.println(prose.line(r1, 100));
        System.out.println(prose.line(r2, 100));
        System.out.println(prose.line(r3, 100));
        System.out.println(prose.line(r4, 100));
        System.out.println(prose.line(r5, 100));
        System.out.println(prose.line(r6, 100));
        System.out.println(prose.line(r7, 100));
    }
}
