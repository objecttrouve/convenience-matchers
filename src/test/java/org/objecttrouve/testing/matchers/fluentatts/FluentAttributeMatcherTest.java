/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.*;
import org.junit.Test;
import org.objecttrouve.testing.matchers.ConvenientMatchers;
import org.objecttrouve.testing.matchers.customization.MatcherFactory;
import org.objecttrouve.testing.matchers.customization.SymbolsConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.*;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.an;
import static org.objecttrouve.testing.matchers.customization.StringifiersConfig.stringifiers;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;


@SuppressWarnings({"ResultOfMethodCallIgnored", "deprecation"})
public class FluentAttributeMatcherTest {


    private static class Thing {

        private final String value;

        Thing(final String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "A thing with value '" + value + "'";
        }
    }

    @Test
    public void testFactoryANotNull() {

        final FluentAttributeMatcher matcher = a(Thing.class);

        assertThat(matcher, notNullValue());
    }

    @Test
    public void testFactoryAnNotNull() {

        final FluentAttributeMatcher matcher = an(Thing.class);

        assertThat(matcher, notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryANullInput() {
        a(null);
    }

    @Test
    public void test__with__happy_path__self_describing() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(Thing::getValue, "the value"); //

        assertThat(new Thing("the value"), is(matching));
    }

    @Test
    public void test__withValue__happy_path() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = a(Thing.class)//
            .withValue(value, "the value"); //

        assertThat(new Thing("the value"), is(matching));
    }


    @Test
    public void test__with__mismatch__self_describing() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(Thing::getValue, "the value"); //

        assertThat(new Thing("not the value"), not(is(matching)));
    }


    @Test
    public void test__with_describes__mismatch__self_describing() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(Thing::getValue, "the value"); //
        final Thing actual = new Thing("not the value");
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tgetValue = 'the value' ≠ 'not the value'\n"));
    }

    @Test
    public void describeTo__with() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);
        final FluentAttributeMatcher<Thing> matching = a(Thing.class)//
            .with(value, "the value"); //
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tvalue = 'the value'\n"));
    }

    @Test
    public void describeMismatchSafely__with__mismatch() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);
        final FluentAttributeMatcher<Thing> matching = a(Thing.class)//
            .with(value, "the value"); //
        final Thing item = new Thing("not the value");
        matching.matchesSafely(item);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(item, description);

        assertThat(description.toString(), is("\n\tvalue = 'the value' ≠ 'not the value'\n"));
    }

    @Test
    public void describeTo__withValue() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);
        final FluentAttributeMatcher<Thing> matching = a(Thing.class)//
            .withValue(value, "the value"); //
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tvalue = 'the value'\n"));
    }

    @Test
    public void describeMismatchSafely__withValue__mismatch() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);
        final FluentAttributeMatcher<Thing> matching = a(Thing.class)//
            .withValue(value, "the value"); //
        final Thing item = new Thing("not the value");
        matching.matchesSafely(item);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(item, description);

        assertThat(description.toString(), is("\n\tvalue = 'the value' ≠ 'not the value'\n"));
    }


    @Test
    public void test__having__happy_path() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .having(Thing::getValue, equalTo("the value")); //

        assertThat(new Thing("the value"), is(matching));
    }


    @Test
    public void withMatching__happy_path() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = a(Thing.class)//
            .withMatching(value, equalTo("the value")); //

        assertThat(new Thing("the value"), is(matching));
    }


    @Test
    public void with__happy_path__expecting_Matcher() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = a(Thing.class)//
            .with(value, equalTo("the value")); //

        assertThat(new Thing("the value"), is(matching));
    }

    @Test
    public void test__having__mismatch() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .having(Thing::getValue, equalTo("the value")); //

        assertThat(new Thing("the other value"), not(is(matching)));
    }

    @Test
    public void testSthNotMatching2() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .having(Thing::getValue, endsWith("the value")); //

        assertThat(new Thing("the other value"), not(is(matching)));
    }

    @Test
    public void withMatching__mismatch() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = a(Thing.class)//
            .withMatching(value, equalTo("the value")); //

        assertThat(new Thing("the other value"), not(is(matching)));
    }


    @Test
    public void with__mismatch__expecting_Matcher() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = a(Thing.class)//
            .with(value, equalTo("the value")); //

        assertThat(new Thing("the other value"), not(is(matching)));
    }


    @Test
    public void with__mismatch__expecting_Matcher__with_inappropriate_type() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = a(Thing.class)//
            .with(value, everyItem(notNullValue())); //

        assertFalse(matcher.matches("It's a String!"));

    }


    private static class InappropriateExpectation {
    }

    @Test
    public void with__mismatch__expecting_value__with_inappropriate_type() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = a(Thing.class)//
            .with(value, new InappropriateExpectation()); //

        assertFalse(matcher.matches(new Thing("This is not what we expected.")));

    }

    private static class HeirThing extends Thing {

        HeirThing(final String value) {
            super(value);
        }
    }

    @Test
    public void with__mismatch__expecting_Matcher__with_super_type() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = a(Thing.class)//
            .with(value, aTracking(HeirThing.class)); //

        assertTrue(matcher.matches(new Thing("It's a String!")));
    }

    @Test
    public void with__mismatch__expecting_Matcher__with_sub_type() {

        final Attribute<HeirThing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<HeirThing> matcher = a(HeirThing.class)//
            .with(value, aTracking(HeirThing.class)); //

        assertFalse(matcher.matches(new Thing("It's a String!")));
    }

    @Test
    public void with__mismatch__expecting_null() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = a(Thing.class)//
            .with(value, null); //

        assertFalse(matcher.matches(new Thing("This is not what we expected.")));

    }

    @Test
    public void with__mismatch__expecting_String_getting_null_target() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = a(Thing.class)//
            .with(value, "We expect a String"); //

        assertFalse(matcher.matches(null));
    }

    @Test
    public void with__mismatch__expecting_matching_String_getting_null_target() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = a(Thing.class)//
            .with(value, containsString("We expect a String")); //

        assertFalse(matcher.matches(null));
    }

    private static class Answer {

        private final int theAnswer;

        private Answer(final int theAnswer) {
            this.theAnswer = theAnswer;
        }

        int toEverything() {
            return getTheAnswer();
        }

        int getTheAnswer() {
            return theAnswer;
        }

        int getHalfTheAnswer() {
            return theAnswer / 2;
        }

        @Override
        public String toString() {
            return "toString->" + theAnswer;
        }
    }

    private static class Question {
        private final Answer answer;

        private Question(final Answer answer) {
            this.answer = answer;
        }

        Answer getAnswer() {
            return answer;
        }
    }

    @Test
    public void test__having__mismatch__describes_nicely() {

        final Matcher<Answer> answerMatcher = new TypeSafeMatcher<Answer>() {
            @Override
            protected boolean matchesSafely(final Answer item) {
                return item.toEverything() == 42;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("answer 42");
            }
        };
        final FluentAttributeMatcher<Question> matching = aTracking(Question.class)//
            .having(Question::getAnswer, answerMatcher); //
        final Question actual = new Question(new Answer(24));
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tgetAnswer ⩳ 'answer 42' ≠ 'was <toString->24>'\n"));
    }

    @Test
    public void describeTo__withMatching__describes_nicely() {

        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Matcher<Answer> answerMatcher = new TypeSafeMatcher<Answer>() {
            @Override
            protected boolean matchesSafely(final Answer item) {
                return item.toEverything() == 42;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("answer 42");
            }
        };
        final FluentAttributeMatcher<Question> matching = a(Question.class)//
            .withMatching(answer, answerMatcher);
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tanswer ⩳ 'answer 42'\n"));
    }


    @Test
    public void describeTo__withMatching__describes_nicely__nested_FluentAttributeMatcher() {
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> embeddedAnswer = attribute("embedded answer", Answer::getTheAnswer);
        final Attribute<Answer, Integer> halfAnswer = attribute("half answer", Answer::getHalfTheAnswer);
        final Attribute<Integer, Object> byt = attribute("byteval", Integer::byteValue);
        final Attribute<Integer, Object> doub = attribute("doubleval", Integer::doubleValue);
        final FluentAttributeMatcher<Question> matching = a(Question.class)//
            .withMatching(answer, an(Answer.class)
                .withMatching(embeddedAnswer, an(Integer.class)
                    .withMatching(byt, nullValue())
                    .withMatching(doub, nullValue())
                ).withMatching(halfAnswer, lessThan(1))
            );
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tanswer ▶ embedded answer ▶ byteval ⩳ 'null'\n" +
            "\tanswer ▶ embedded answer ▶ doubleval ⩳ 'null'\n" +
            "\tanswer ▶ half answer ⩳ 'a value less than <1>'\n"
        ));
    }

    @Test
    public void describeTo__withMatching__withValue__describes_nicely__nested_FluentAttributeMatcher() {
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> embeddedAnswer = attribute("embedded answer", Answer::getTheAnswer);
        final Attribute<Answer, Integer> halfAnswer = attribute("half answer", Answer::getHalfTheAnswer);
        final Attribute<Integer, Object> byt = attribute("byteval", Integer::byteValue);
        final Attribute<Integer, Object> doub = attribute("doubleval", Integer::doubleValue);
        final FluentAttributeMatcher<Question> matching = a(Question.class)//
            .withMatching(answer, an(Answer.class)
                .withMatching(embeddedAnswer, an(Integer.class)
                    .withMatching(byt, nullValue())
                    .withValue(doub, 1.0)
                ).withValue(halfAnswer, 1)
            );
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tanswer ▶ embedded answer ▶ byteval ⩳ 'null'\n" +
            "\tanswer ▶ embedded answer ▶ doubleval = '1.0'\n" +
            "\tanswer ▶ half answer = '1'\n"
        ));
    }


    @Test
    public void customized__withDefaultSymbols__describeTo__withMatching__withValue() {
        final MatcherFactory an = ConvenientMatchers.customized().withDefaultSymbols().build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> embeddedAnswer = attribute("embedded answer", Answer::getTheAnswer);
        final Attribute<Answer, Integer> halfAnswer = attribute("half answer", Answer::getHalfTheAnswer);
        final Attribute<Integer, Object> byt = attribute("byteval", Integer::byteValue);
        final Attribute<Integer, Object> doub = attribute("doubleval", Integer::doubleValue);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)//
            .withMatching(answer, an.instanceOf(Answer.class)
                .withMatching(embeddedAnswer, an.instanceOf(Integer.class)
                    .withMatching(byt, nullValue())
                    .withValue(doub, 1.0)
                ).withValue(halfAnswer, 1)
            );
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tanswer ▶ embedded answer ▶ byteval ⩳ 'null'\n" +
            "\tanswer ▶ embedded answer ▶ doubleval = '1.0'\n" +
            "\tanswer ▶ half answer = '1'\n"
        ));
    }

    @Test
    public void customized__withAsciiSymbols__describeTo__withMatching__withValue() {
        final MatcherFactory an = ConvenientMatchers.customized().withAsciiSymbols().build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> embeddedAnswer = attribute("embedded answer", Answer::getTheAnswer);
        final Attribute<Answer, Integer> halfAnswer = attribute("half answer", Answer::getHalfTheAnswer);
        final Attribute<Integer, Object> byt = attribute("byteval", Integer::byteValue);
        final Attribute<Integer, Object> doub = attribute("doubleval", Integer::doubleValue);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)//
            .withMatching(answer, an.instanceOf(Answer.class)
                .withMatching(embeddedAnswer, an.instanceOf(Integer.class)
                    .withMatching(byt, nullValue())
                    .withValue(doub, 1.0)
                ).withValue(halfAnswer, 1)
            );
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tanswer >> embedded answer >> byteval =~ 'null'\n" +
            "\tanswer >> embedded answer >> doubleval = '1.0'\n" +
            "\tanswer >> half answer = '1'\n"
        ));
    }

    @Test
    public void customized__with_custom_Symbols__describeTo__withMatching__withValue() {
        final SymbolsConfig.Builder symbols = SymbolsConfig.symbols()
            .withExpectedEquals("⩶")
            .withActualNotEquals("╪")
            .withExpectedMatches("♒")
            .withPointingNested("⤼");
        final MatcherFactory an = ConvenientMatchers.customized().withSymbols(symbols).build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> embeddedAnswer = attribute("embedded answer", Answer::getTheAnswer);
        final Attribute<Answer, Integer> halfAnswer = attribute("half answer", Answer::getHalfTheAnswer);
        final Attribute<Integer, Object> byt = attribute("byteval", Integer::byteValue);
        final Attribute<Integer, Object> doub = attribute("doubleval", Integer::doubleValue);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)//
            .withMatching(answer, an.instanceOf(Answer.class)
                .withMatching(embeddedAnswer, an.instanceOf(Integer.class)
                    .withMatching(byt, nullValue())
                    .withValue(doub, 1.0)
                ).withValue(halfAnswer, 1)
            );
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tanswer⤼embedded answer⤼byteval♒'null'\n" +
            "\tanswer⤼embedded answer⤼doubleval⩶'1.0'\n" +
            "\tanswer⤼half answer⩶'1'\n"
        ));
    }

    @Test
    public void customized__withShortStringifiers__describeTo__withValue() {
        final MatcherFactory an = ConvenientMatchers.customized()
            .withStringifiers(stringifiers()
                .withShortStringifier(Answer.class, a -> "A-" + a.theAnswer)
            ).build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)//
            .withValue(answer, new Answer(42));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("" +
            "\n\tanswer = 'A-42'\n"
        ));
    }


    @Test
    public void describeMismatchSafely__withMatching__mismatch__describes_nicely__nested_FluentAttributeMatcher() {
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> embeddedAnswer = attribute("embedded answer", Answer::getTheAnswer);
        final Attribute<Answer, Integer> halfAnswer = attribute("half answer", Answer::getHalfTheAnswer);
        final Attribute<Integer, Object> byt = attribute("byteval", Integer::byteValue);
        final Attribute<Integer, Object> doub = attribute("doubleval", Integer::doubleValue);
        final FluentAttributeMatcher<Question> matching = a(Question.class)//
            .withMatching(answer, an(Answer.class)
                .withMatching(embeddedAnswer, an(Integer.class)
                    .withMatching(byt, nullValue())
                    .withMatching(doub, nullValue())
                ).withMatching(halfAnswer, lessThan(1))
            );
        final StringDescription description = new StringDescription();
        final Question item = new Question(new Answer(42));

        matching.describeMismatchSafely(item, description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tanswer ▶ embedded answer ▶ byteval ⩳ 'null' ≠ 'was <42>'\n" +
            "\tanswer ▶ embedded answer ▶ doubleval ⩳ 'null' ≠ 'was <42.0>'\n" +
            "\tanswer ▶ half answer ⩳ 'a value less than <1>' ≠ '<21> was greater than <1>'\n"
        ));
    }

    @Test
    public void describeMismatchSafely__withMatching__withValue__mismatch__describes_nicely__nested_FluentAttributeMatcher() {
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> embeddedAnswer = attribute("embedded answer", Answer::getTheAnswer);
        final Attribute<Answer, Integer> halfAnswer = attribute("half answer", Answer::getHalfTheAnswer);
        final Attribute<Integer, Object> byt = attribute("byteval", Integer::byteValue);
        final Attribute<Integer, Object> doub = attribute("doubleval", Integer::doubleValue);
        final FluentAttributeMatcher<Question> matching = a(Question.class)//
            .withMatching(answer, an(Answer.class)
                .withMatching(embeddedAnswer, an(Integer.class)
                    .withMatching(byt, nullValue())
                    .withValue(doub, 1.0)
                ).withValue(halfAnswer, 1)
            );
        final StringDescription description = new StringDescription();
        final Question item = new Question(new Answer(42));

        matching.describeMismatchSafely(item, description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tanswer ▶ embedded answer ▶ byteval ⩳ 'null' ≠ 'was <42>'\n" +
            "\tanswer ▶ embedded answer ▶ doubleval = '1.0' ≠ '42.0'\n" +
            "\tanswer ▶ half answer = '1' ≠ '21'\n"
        ));
    }

    @Test
    public void customized__withDefaultSymbols__describeMismatchSafely__withMatching__withValue() {
        final MatcherFactory an = ConvenientMatchers.customized().withDefaultSymbols().build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> embeddedAnswer = attribute("embedded answer", Answer::getTheAnswer);
        final Attribute<Answer, Integer> halfAnswer = attribute("half answer", Answer::getHalfTheAnswer);
        final Attribute<Integer, Object> byt = attribute("byteval", Integer::byteValue);
        final Attribute<Integer, Object> doub = attribute("doubleval", Integer::doubleValue);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)//
            .withMatching(answer, an.instanceOf(Answer.class)
                .withMatching(embeddedAnswer, an.instanceOf(Integer.class)
                    .withMatching(byt, nullValue())
                    .withValue(doub, 1.0)
                ).withValue(halfAnswer, 1)
            );
        final StringDescription description = new StringDescription();
        final Question item = new Question(new Answer(42));

        matching.describeMismatchSafely(item, description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tanswer ▶ embedded answer ▶ byteval ⩳ 'null' ≠ 'was <42>'\n" +
            "\tanswer ▶ embedded answer ▶ doubleval = '1.0' ≠ '42.0'\n" +
            "\tanswer ▶ half answer = '1' ≠ '21'\n"
        ));
    }

    @Test
    public void customized__withAsciiSymbols__describeMismatchSafely__withMatching__withValue() {
        final MatcherFactory an = ConvenientMatchers.customized().withAsciiSymbols().build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> embeddedAnswer = attribute("embedded answer", Answer::getTheAnswer);
        final Attribute<Answer, Integer> halfAnswer = attribute("half answer", Answer::getHalfTheAnswer);
        final Attribute<Integer, Object> byt = attribute("byteval", Integer::byteValue);
        final Attribute<Integer, Object> doub = attribute("doubleval", Integer::doubleValue);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)//
            .withMatching(answer, an.instanceOf(Answer.class)
                .withMatching(embeddedAnswer, an.instanceOf(Integer.class)
                    .withMatching(byt, nullValue())
                    .withValue(doub, 1.0)
                ).withValue(halfAnswer, 1)
            );
        final StringDescription description = new StringDescription();
        final Question item = new Question(new Answer(42));

        matching.describeMismatchSafely(item, description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tanswer >> embedded answer >> byteval =~ 'null' != 'was <42>'\n" +
            "\tanswer >> embedded answer >> doubleval = '1.0' != '42.0'\n" +
            "\tanswer >> half answer = '1' != '21'\n"
        ));
    }

    @Test
    public void customized__withShortStringifiers__describeMismatchSafely__withMatching__withValue() {
        final MatcherFactory an = ConvenientMatchers.customized()
            .withStringifiers(
                stringifiers().withShortStringifier(Answer.class, a -> "A-" + a.theAnswer)
            ).build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)//
            .withValue(answer, new Answer(24));
        final StringDescription description = new StringDescription();
        final Question item = new Question(new Answer(42));

        matching.describeMismatchSafely(item, description);

        assertThat(description.toString(), is("" +
            "\n\tanswer = 'A-24' ≠ 'A-42'\n"
        ));
    }


    @Test
    public void customized__withShortStringifiers__debugging__globally__describeMismatchSafely__withMatching__withValue() {
        final MatcherFactory an = ConvenientMatchers.customized()
            .debugging()
            .withStringifiers(
                stringifiers().withShortStringifier(Answer.class, a -> "A-" + a.theAnswer)
            ).build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)//
            .withValue(answer, new Answer(24));
        final StringDescription description = new StringDescription();
        final Question item = new Question(new Answer(42));

        matching.describeMismatchSafely(item, description);

        final String result = description.toString();
        assertThat(result, startsWith("" +
            "\n" +
            "\tanswer = 'A-24' ≠ 'A-42'\n\n\n" +
            "DEBUG:\n\n\n" +
            "\tanswer = 'A-24' ≠ 'A-42'\n\n\n" +
            "Actual object:\n\n ▶ toString():\n" +
            ""));

    }

    @Test
    public void customized__withDebugStringifiers__debugging__globally__describeMismatchSafely__withMatching__withValue() {
        final MatcherFactory an = ConvenientMatchers.customized()
            .debugging()
            .withStringifiers(
                stringifiers()
                    .withShortStringifier(Answer.class, a -> "A-" + a.theAnswer)
                    .withDebugStringifier(Answer.class, a -> "The answer to everything is " + a.theAnswer + ".")
                    .withDebugStringifier(Question.class, q -> "The question was... Err...")
                    .withDebugStringifier(Integer.class, n -> "INT-" + n)
            ).build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> answerVal = attribute("THE answer", Answer::getTheAnswer);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)//
            .withMatching(answer, an.instanceOf(Answer.class)
                .with(answerVal, 42));
        final StringDescription description = new StringDescription();
        final Question question = new Question(new Answer(24));

        matching.describeMismatchSafely(question, description);

        final int length = description.toString().length();
        final int toggling = question.toString().length();
        if (length != 174 + toggling) {
            fail("Stable length was " + (length - toggling) + ". Debug output changed in an unexpected way:\n\n" + description);
        }
    }

    @Test
    public void customized__withDebugStringifiers__debugging__top_actual__describeMismatchSafely__withMatching__withValue() {
        final MatcherFactory an = ConvenientMatchers.customized()
            .withStringifiers(
                stringifiers()
                    .withShortStringifier(Answer.class, a -> "A-" + a.theAnswer)
                    .withDebugStringifier(Answer.class, a -> "The answer to everything is " + a.theAnswer + ".")
                    .withDebugStringifier(Question.class, q -> "The question was... Err...")
            ).build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)
            .debugging()//
            .withValue(answer, new Answer(24));
        final StringDescription description = new StringDescription();
        final Question question = new Question(new Answer(42));

        matching.describeMismatchSafely(question, description);

        final int length = description.toString().length();
        final int toggling = question.toString().length();
        if (length != 194 + toggling) {
            fail("Stable length was " + (length - toggling) + ". Debug output changed in an unexpected way:\n\n" + description);
        }
    }

    @Test
    public void customized__withDebugStringifiers__debugging__embedded_matcher__describeMismatchSafely__withMatching__withValue() {
        final MatcherFactory an = ConvenientMatchers.customized()
            .withStringifiers(
                stringifiers()
                    .withShortStringifier(Answer.class, a -> "A-" + a.theAnswer)
                    .withDebugStringifier(Answer.class, a -> "The answer to everything is " + a.theAnswer + ".")
                    .withDebugStringifier(Question.class, q -> "The question was... Err...")
                    .withDebugStringifier(Integer.class, n -> "INT-" + n)
            ).build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> answerVal = attribute("THE answer", Answer::getTheAnswer);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)
            .withMatching(answer, an.instanceOf(Answer.class)
                .debugging()
                .with(answerVal, 42));
        final StringDescription description = new StringDescription();
        final Question question = new Question(new Answer(24));

        matching.describeMismatchSafely(question, description);

        final int length = description.toString().length();
        if (length != 44) {
            fail("Length was " + length + ". Debug output changed in an unexpected way:\n\n" + description);
        }
    }

    @Test
    public void customized__with_custom_Symbols__describeMismatchSafely__withMatching__withValue() {
        final SymbolsConfig.Builder symbols = SymbolsConfig.symbols()
            .withExpectedEquals("⩶")
            .withActualNotEquals("╪")
            .withExpectedMatches("♒")
            .withPointingNested("⤼");
        final MatcherFactory an = ConvenientMatchers.customized().withSymbols(symbols).build();
        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Attribute<Answer, Integer> embeddedAnswer = attribute("embedded answer", Answer::getTheAnswer);
        final Attribute<Answer, Integer> halfAnswer = attribute("half answer", Answer::getHalfTheAnswer);
        final Attribute<Integer, Object> byt = attribute("byteval", Integer::byteValue);
        final Attribute<Integer, Object> doub = attribute("doubleval", Integer::doubleValue);
        final FluentAttributeMatcher<Question> matching = an.instanceOf(Question.class)//
            .withMatching(answer, an.instanceOf(Answer.class)
                .withMatching(embeddedAnswer, an.instanceOf(Integer.class)
                    .withMatching(byt, nullValue())
                    .withValue(doub, 1.0)
                ).withValue(halfAnswer, 1)
            );
        final StringDescription description = new StringDescription();
        final Question item = new Question(new Answer(42));

        matching.describeMismatchSafely(item, description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tanswer⤼embedded answer⤼byteval♒'null'╪'was <42>'\n" +
            "\tanswer⤼embedded answer⤼doubleval⩶'1.0'╪'42.0'\n" +
            "\tanswer⤼half answer⩶'1'╪'21'\n"
        ));
    }

    @Test
    public void describeMismatchSafely__withMatching__mismatch__describes_nicely() {

        final Attribute<Question, Answer> answer = attribute("answer", Question::getAnswer);
        final Matcher<Answer> answerMatcher = new TypeSafeMatcher<Answer>() {
            @Override
            protected boolean matchesSafely(final Answer item) {
                return item.toEverything() == 42;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("answer 42");
            }
        };
        final FluentAttributeMatcher<Question> matching = a(Question.class)//
            .withMatching(answer, answerMatcher); //
        final Question actual = new Question(new Answer(24));
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tanswer ⩳ 'answer 42' ≠ 'was <toString->24>'\n"));
    }


    private static class Philosophy {
        private final List<Question> questions;

        private Philosophy(final Question... questions) {
            this.questions = new ArrayList<>(Arrays.asList(questions));
        }

        List<Question> getQuestions() {
            return questions;
        }
    }

    private static class HumanThinking {
        private final Philosophy philosophy;

        private HumanThinking(final Philosophy philosophy) {
            this.philosophy = philosophy;
        }

        Philosophy getPhilosophy() {
            return philosophy;
        }
    }

    @Test
    public void testSthNotMatchingOnALongPathDescribesNicelyOutOfTheBox() {

        final Matcher<Answer> answerMatcher = new TypeSafeMatcher<Answer>() {
            @Override
            protected boolean matchesSafely(final Answer item) {
                return item.toEverything() == 42;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("answer 42");
            }
        };
        final FluentAttributeMatcher<HumanThinking> matching = aTracking(HumanThinking.class)//
            .having(ht -> ht.getPhilosophy().getQuestions().get(1).getAnswer(), answerMatcher); //
        final HumanThinking actual = new HumanThinking(new Philosophy(new Question(new Answer(24)), new Question(new Answer(31))));
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("" +
            "\n\tgetPhilosophy/getQuestions/get/getAnswer ⩳ 'answer 42' ≠ 'was <toString->31>'\n"
        ));
    }

    private static class Untrackable {
        private final List<String> untrackable;

        private Untrackable(final String... untrackable) {
            /*
            * It's an implementation detail of that list to cause IllegalAccessErrors on proxy creation.
            * At least until either their or our implementation is fixed.
            * Until that happens it servs to reproduce the issue.
            */
            this.untrackable = Arrays.asList(untrackable);
        }

        List<String> getUntrackable() {
            return untrackable;
        }
    }

    @Test
    public void testSthUntrackableStillDescribesHalfwayHelpful() {

        final FluentAttributeMatcher<Untrackable> matching = aTracking(Untrackable.class)//
            .with(un -> un.getUntrackable().get(0), "trackable"); //
        final Untrackable actual = new Untrackable("untrackable");
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("" +
            "\n\tgetUntrackable/??? = 'trackable' ≠ 'untrackable'\n" +
            ""));
    }

    private static class NestedUntrackable {
        private final Untrackable untrackable;

        private NestedUntrackable(final Untrackable untrackable) {
            this.untrackable = untrackable;
        }

        Untrackable getNested() {
            return untrackable;
        }
    }

    @Test
    public void testNestedUntrackableStillDescribesHalfwayHelpful() {

        final FluentAttributeMatcher<NestedUntrackable> matching = aTracking(NestedUntrackable.class)//
            .with(un -> un.getNested().getUntrackable().get(0), "trackable"); //
        final NestedUntrackable actual = new NestedUntrackable(new Untrackable("untrackable"));
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tgetNested/getUntrackable/??? = 'trackable' ≠ 'untrackable'\n"));
    }

    private static class SubThing extends Thing {

        SubThing(final String value) {
            super(value);
        }
    }

    @Test
    public void testDescribesSuperclassCallsWithMethodReference() {

        final FluentAttributeMatcher<SubThing> matching = aTracking(SubThing.class)//
            .with(Thing::getValue, "value of subclass"); //
        final SubThing actual = new SubThing("value of superclass");
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tgetValue = 'value of subclass' ≠ 'value of superclass'\n"));
    }

    @Test
    public void testDescribesSuperclassCallsWithLambda() {

        //noinspection Convert2MethodRef
        final FluentAttributeMatcher<SubThing> matching = aTracking(SubThing.class)//
            .with(t -> t.getValue(), "value of subclass"); //
        final SubThing actual = new SubThing("value of superclass");
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tgetValue = 'value of subclass' ≠ 'value of superclass'\n"));
    }

    @Test
    public void testSthWithNullExpectationNotMatching() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(Thing::getValue, null); //

        assertThat(new Thing("not null"), not(is(matching)));
    }

    @Test
    public void testSthWithNullExpectationMatching() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(Thing::getValue, null); //

        assertThat(new Thing(null), is(matching));
    }

    @Test
    public void testSthWithNullNotMatching() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(Thing::getValue, "not null"); //

        assertThat(new Thing(null), not(is(matching)));
    }

    @Test
    public void testSthWithNullExpectationNotMatchingDescribesNicely() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(Thing::getValue, null); //
        final Thing actual = new Thing("not null");
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tgetValue = 'null' ≠ 'not null'\n"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__with__null_arg__self_describing() {
        final Function f = null;
        //noinspection unchecked
        aTracking(Thing.class)//
            .with(f, null); //
    }


    @Test(expected = IllegalArgumentException.class)
    public void with__null_arg() {
        final Attribute a = null;
        //noinspection unchecked
        aTracking(Thing.class)//
            .with(a, null); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void with__Attribute_having_null_name() {
        final Attribute<String, Integer> a = attribute(null, String::length);
        //noinspection unchecked
        aTracking(String.class)//
            .with(a, null); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void with__Attribute_having_null_getter() {
        final Attribute<String, Integer> a = attribute("length", null);
        //noinspection unchecked
        aTracking(String.class)//
            .with(a, null); //
    }


    @Test(expected = IllegalArgumentException.class)
    public void withValue__null_arg() {
        final Attribute a = null;
        //noinspection unchecked
        a(Thing.class)//
            .withValue(a, null); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void withValue__Attribute_having_null_name() {
        final Attribute<String, Integer> a = attribute(null, String::length);
        //noinspection unchecked
        a(String.class)//
            .withValue(a, null); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void withValue__Attribute_having_null_getter() {
        final Attribute<String, Integer> a = attribute("length", null);
        //noinspection unchecked
        a(String.class)//
            .withValue(a, null); //
    }


    @Test(expected = IllegalArgumentException.class)
    public void test__having__null_lambda_arg() {
        aTracking(Thing.class)//
            .having(null, notNullValue()); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__having__null_matcher_arg() {
        aTracking(Thing.class)//
            .having(Object::toString, null); //
    }


    @Test(expected = IllegalArgumentException.class)
    public void withMatching__Attribute_having_null_name() {
        final Attribute<String, Integer> a = attribute(null, String::length);

        //noinspection unchecked
        a(String.class)//
            .withMatching(a, CoreMatchers.equalTo(0)); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void withMatching__Attribute_having_null_getter() {
        final Attribute<String, Integer> a = attribute("length", null);

        //noinspection unchecked
        a(String.class)//
            .withMatching(a, CoreMatchers.equalTo(0)); //
    }


    @SuppressWarnings({"ReplaceInefficientStreamCount", "UseBulkOperation"})
    private static class ThingWithNumbers {

        private final List<Integer> numbers = new LinkedList<>();

        ThingWithNumbers(final Integer... theNumbers) {
            Arrays.stream(theNumbers).forEach(numbers::add);
        }

        List<Integer> getNumbers() {
            return numbers;
        }

        int sum() {
            return numbers.stream().mapToInt(n -> n).sum();
        }

        long count() {
            return numbers.stream().count();
        }
    }

    @Test
    public void testLambdaWithMultipleCallsMatches() {

        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(twn -> twn.getNumbers().get(0) + twn.sum() + twn.count(), 10L); //

        assertThat(new ThingWithNumbers(1, 2, 3), is(matching));
    }

    @Test
    public void testLambdaWithMultipleCallsMatchesNot() {

        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(twn -> twn.getNumbers().get(0) + twn.sum() + twn.count(), 7); //

        assertThat(new ThingWithNumbers(1, 2, 3), not(is(matching)));
    }

    @Test
    public void testLambdaWithMultipleCallsMatchesNotButDescriptionDoesItsBest() {

        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(twn -> twn.getNumbers().get(0) + twn.sum() + twn.count(), 7); //
        final ThingWithNumbers actual = new ThingWithNumbers(1, 2, 3);
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        /*
        * Somewhat awkward but not really the most prominent use case.
        */
        assertThat(description.toString(), is("\n\tgetNumbers/get&sum&count = '7' ≠ '10'\n"));
    }


    @Test
    public void test__multiple_expectations__all_matching() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .withValue(attribute("count", ThingWithNumbers::count), 3L)//
            .withValue(attribute("sum", ThingWithNumbers::sum), 6)//
            .withMatching(attribute("length", twn -> twn.getNumbers().size()), is(3))//
            ;//

        assertThat(new ThingWithNumbers(1, 2, 3), is(matching));
    }


    @Test
    public void test__multiple_expectations__some_matching__getScore() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .withValue(attribute("count", ThingWithNumbers::count), 3L)//
            .withValue(attribute("sum", ThingWithNumbers::sum), 6)//
            .withMatching(attribute("length", twn -> twn.getNumbers().size()), is(3))//
            .withMatching(attribute("length2", twn -> twn.getNumbers().size() * 2), is(6))//
            ;//
        assertThat(new ThingWithNumbers(1, 2, 3), is(matching));

        final double score = matching.getScore();

        assertThat(score, is(1.0));
    }

    @Test
    public void test__multiple_expectations__none_matching() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(ThingWithNumbers::count, 6)//
            .with(ThingWithNumbers::sum, 3)//
            .having(twn -> twn.getNumbers().size(), is(2))//
            ;//

        assertThat(new ThingWithNumbers(1, 2, 3, 4), not(is(matching)));
    }

    @Test
    public void test__multiple_expectations__none_matching__getScore() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(ThingWithNumbers::count, 6)//
            .with(ThingWithNumbers::sum, 3)//
            .having(twn -> twn.getNumbers().size(), is(2))//
            ;//
        assertThat(new ThingWithNumbers(1, 2, 3, 4), not(is(matching)));

        final double score = matching.getScore();

        assertThat(score, is(0.0));
    }


    @Test
    public void test__describeMismatchSafely__with__having__multiple_expectations__none_matching__describes_nicely() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(ThingWithNumbers::count, 6)//
            .with(ThingWithNumbers::sum, 3)//
            .having(twn -> twn.getNumbers().size(), is(2))//
            ;//

        final ThingWithNumbers item = new ThingWithNumbers(1, 2, 3);
        matching.matchesSafely(item);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(item, description);

        /*
        * Somewhat awkward but not really the most prominent use case.
        */
        assertThat(description.toString(), is("\n" +
            "\tcount = '6' ≠ '3'\n" +
            "\tsum = '3' ≠ '6'\n" +
            "\tgetNumbers/size ⩳ 'is <2>' ≠ 'was <3>'" +
            "\n"));
    }


    @Test
    public void test__multiple_expectations__some_not_matching() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(ThingWithNumbers::count, 3L)//
            .with(ThingWithNumbers::sum, 3)//
            .having(twn -> twn.getNumbers().size(), is(2))//
            ;//

        assertThat(new ThingWithNumbers(1, 2, 3), not(is(matching)));
    }


    @Test
    public void test__multiple_expectations__some_not_matching__getScore() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(ThingWithNumbers::count, 3L)//
            .with(ThingWithNumbers::sum, 3)//
            .having(twn -> twn.getNumbers().size(), is(2))//
            ;//
        assertThat(new ThingWithNumbers(1, 2, 3), not(is(matching)));

        final double score = matching.getScore();

        assertThat(score, closeTo(0.33, 0.01));
    }

    @Test
    public void test__multiple_expectations__some_not_matching__describes_nicely() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(ThingWithNumbers::count, 3L)//
            .with(ThingWithNumbers::sum, 3)//
            .having(twn -> twn.getNumbers().size(), is(2))//
            ;//

        final ThingWithNumbers item = new ThingWithNumbers(1, 2, 3);
        matching.matchesSafely(item);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(item, description);

        /*
        * Somewhat awkward but not really the most prominent use case.
        */
        assertThat(description.toString(), is("\n" +
            "\tsum = '3' ≠ '6'\n" +
            "\tgetNumbers/size ⩳ 'is <2>' ≠ 'was <3>'" +
            "\n"));
    }

    @Test
    public void testOnlyDescribesMethodsCalledOnGetting() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(ThingWithNumbers::count, 3L)//
            .with(ThingWithNumbers::sum, 3)//
            .having(ThingWithNumbers::getNumbers, new TypeSafeMatcher<List<Integer>>() {
                @Override
                protected boolean matchesSafely(final List<Integer> item) {
                    return item.get(0) == 5 && item.get(1) == 6 && item.get(2) == 7;
                }

                @Override
                public void describeTo(final Description description) {
                    description.appendText("items 5,6,7");
                }
            });//

        final ThingWithNumbers actual = new ThingWithNumbers(1, 2, 3);
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n" +
            "\tsum = '3' ≠ '6'\n" +
            "\tgetNumbers ⩳ 'items 5,6,7' ≠ 'was <[1, 2, 3]>'" +
            "\n"));
    }

    @Test
    public void testMatcherIsReset() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(Thing::getValue, "the value"); //

        final Thing eventualActual = new Thing("not the value");
        assertThat(eventualActual, not(is(matching)));
        assertThat(new Thing("the value"), is(matching));
        final boolean matches = matching.matchesSafely(eventualActual);
        assertFalse(matches);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(eventualActual, description);

        assertThat(description.toString(), is("\n\tgetValue = 'the value' ≠ 'not the value'\n"));
    }


    @SuppressWarnings("UseBulkOperation")
    public static class ThingArray {
        private final Thing[] things;

        ThingArray(final Thing... things) {
            this.things = things;
        }

        Thing[] getThings() {
            return things;
        }

        @SuppressWarnings("UnusedReturnValue")
        List<Thing> getThingsAsList() {
            final ArrayList<Thing> thingList = new ArrayList<>();
            Arrays.stream(things).forEach(thingList::add);
            return thingList;
        }

        @Override
        public String toString() {
            return "toString->" + Arrays.stream(things).map(Thing::toString).collect(Collectors.joining());
        }
    }


    @Test
    public void testSthInAnArray() {

        final FluentAttributeMatcher<ThingArray> matching = aTracking(ThingArray.class)//
            .with(a -> a.getThings()[1].getValue(), "the second value"); //

        assertThat(new ThingArray(//
            new Thing("the first value"),//
            new Thing("the second value"),//
            new Thing("the third value")//
        ), is(matching));
    }

    @Test
    public void testSthInAnArrayNotMatching() {

        final FluentAttributeMatcher<ThingArray> matching = aTracking(ThingArray.class)//
            .with(a -> a.getThings()[1].getValue(), "the third value"); //

        assertThat(new ThingArray(//
            new Thing("the first value"),//
            new Thing("the second value"),//
            new Thing("the third value")//
        ), not(is(matching)));
    }

    @Test
    public void testSthInAnArrayNotMatchingButDescribedLovely1() {

        final FluentAttributeMatcher<ThingArray> matching = aTracking(ThingArray.class)//
            .with(a -> a.getThings()[1].getValue(), "the third value"); //
        final ThingArray actual = new ThingArray(//
            new Thing("the first value"),//
            new Thing("the second value"),//
            new Thing("the third value")//
        );
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tgetThings[...]/getValue = 'the third value' ≠ 'the second value'\n"));

    }

    @Test
    public void testSthInAnArrayNotMatchingButDescribedLovely2() {

        final FluentAttributeMatcher<ThingArray> matching = aTracking(ThingArray.class)//
            .with(a -> {
                //noinspection unused
                final Thing thing = a.getThings()[2];
                //noinspection ResultOfMethodCallIgnored
                a.getThings()[0].getValue();
                return a.getThings()[1].getValue();
            }, "the third value"); //
        final ThingArray actual = new ThingArray(//
            new Thing("the first value"),//
            new Thing("the second value"),//
            new Thing("the third value")//
        );
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n" +
            "\tgetThings&getThings[...]/getValue&getThings[...]/getValue = 'the third value' ≠ 'the second value'" +
            "\n"));

    }

    @Test
    public void testSthInAnArrayNotMatchingButDescribedLovely3() {

        final FluentAttributeMatcher<ThingArray> matching = aTracking(ThingArray.class)//
            .with(a -> {
                //noinspection unused
                a.getThingsAsList();
                //noinspection unused
                final Thing thing = a.getThings()[2];
                a.getThings()[0].getValue();
                a.getThingsAsList();
                return a.getThings()[1].getValue();
            }, "the third value"); //
        final ThingArray actual = new ThingArray(//
            new Thing("the first value"),//
            new Thing("the second value"),//
            new Thing("the third value")//
        );
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("" +
            "\n" +
            "\tgetThings&getThings[...]/getValue&getThings[...]/getValue&getThingsAsList&getThingsAsList = 'the third value' ≠ 'the second value'" +
            "\n"

        ));

    }

    private static class Outer {
        private Outer(final Inner inner) {
            this.inner = inner;
        }

        private static class Inner {
            private final String s;

            Inner(final String s) {
                this.s = s;
            }

            String getS() {
                return s;
            }
        }

        private final Inner inner;

        Inner getInner() {
            return inner;
        }
    }

    @Test
    public void testSthInInnerClassNotMatchingButDescribedLovely() {

        final FluentAttributeMatcher<Outer> matching = aTracking(Outer.class)//
            .with(outer -> outer.getInner().getS(), "S"); //
        final Outer actual = new Outer(new Outer.Inner("X"));
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tgetInner/getS = 'S' ≠ 'X'\n"));

    }

    interface WithAttribute {
        String attr();
    }

    private static class AnonymousAttributes {
        WithAttribute getAttr() {
            //noinspection Convert2Lambda
            return new WithAttribute() {
                @Override
                public String attr() {
                    return "an attribute";
                }
            };
        }

        Function<String, String> func() {
            return s -> s;
        }
    }

    @Test
    public void testSthInAnonymousClassNotMatchingButDescribedLovely() {

        final FluentAttributeMatcher<AnonymousAttributes> matching = aTracking(AnonymousAttributes.class)//
            .with(a -> a.getAttr().attr(), "a property"); //
        final AnonymousAttributes actual = new AnonymousAttributes();
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tgetAttr/attr = 'a property' ≠ 'an attribute'\n"));

    }

    @Test
    public void testFunctionCallNotMatchingButDescribedLovely() {

        final FluentAttributeMatcher<AnonymousAttributes> matching = aTracking(AnonymousAttributes.class)//
            .with(a -> a.func().apply("an attribute"), "a property"); //

        final AnonymousAttributes actual = new AnonymousAttributes();
        matching.matchesSafely(actual);

        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("\n\tfunc/??? = 'a property' ≠ 'an attribute'\n"));

    }

    private static class ConcreteAttributes implements WithAttribute {

        @Override
        public String attr() {
            return "a real attribute";
        }
    }

    private static class Proxying {

        private final ConcreteAttributes delegate = new ConcreteAttributes();

        WithAttribute proxied() {
            final InvocationHandler invc = (proxy, method, args) -> method.invoke(delegate, args);
            return (WithAttribute) Proxy.newProxyInstance(this.getClass().getClassLoader(), ConcreteAttributes.class.getInterfaces(), invc);
        }

    }

    @Test
    public void testSthWithProxyNotMatchingButDescribedLovely() {

        final FluentAttributeMatcher<Proxying> matching = aTracking(Proxying.class)//
            .with(p -> p.proxied().attr(), "a property"); //
        final Proxying actual = new Proxying();
        matching.matchesSafely(actual);
        final StringDescription description = new StringDescription();

        matching.describeMismatchSafely(actual, description);

        assertThat(description.toString(), is("" +
            "\n\tproxied/??? = 'a property' ≠ 'a real attribute'\n" +
            ""));

    }

    @Test
    public void test__FluentAttributeMatcher__should_not_be_moved_or_renamed() {
        // Rename with caution!
        // Because it's used in a sysprop that's part of the API.
        // Rename anyway -> keep sysprop backwards compatible.
        assertThat(FluentAttributeMatcher.class.getCanonicalName(),
            is("org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher"));
    }

    private static <T> FluentAttributeMatcher<T> aTracking(@SuppressWarnings("UnusedParameters") final Class<T> clazz) {
        return new FluentAttributeMatcher<>(true);
    }

}