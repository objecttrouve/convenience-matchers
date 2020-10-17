/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.*;
import org.junit.Test;
import org.objecttrouve.testing.matchers.ConvenientMatchers;
import org.objecttrouve.testing.matchers.customization.MatcherFactory;
import org.objecttrouve.testing.matchers.customization.SymbolsConfig;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.an;
import static org.objecttrouve.testing.matchers.customization.StringifiersConfig.stringifiers;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;


@SuppressWarnings({"deprecation", "rawtypes", "ConstantConditions"})
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
    public void test__withValue__happy_path() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = a(Thing.class)//
            .withValue(value, "the value"); //

        assertThat(new Thing("the value"), is(matching));
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
            "\tanswer ▶ embedded answer ▶ byteval ⩳ 'null' ≠ 'was <42b>'\n" +
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
            "\tanswer ▶ embedded answer ▶ byteval ⩳ 'null' ≠ 'was <42b>'\n" +
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
            "\tanswer ▶ embedded answer ▶ byteval ⩳ 'null' ≠ 'was <42b>'\n" +
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
            "\tanswer >> embedded answer >> byteval =~ 'null' != 'was <42b>'\n" +
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
            "\tanswer⤼embedded answer⤼byteval♒'null'╪'was <42b>'\n" +
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
        a(String.class)//
            .withValue(a, null); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void withValue__Attribute_having_null_getter() {
        final Attribute<String, Integer> a = attribute("length", null);
        a(String.class)//
            .withValue(a, null); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void withMatching__Attribute_having_null_name() {
        final Attribute<String, Integer> a = attribute(null, String::length);

        a(String.class)//
            .withMatching(a, CoreMatchers.equalTo(0)); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void withMatching__Attribute_having_null_getter() {
        final Attribute<String, Integer> a = attribute("length", null);

        a(String.class)//
            .withMatching(a, CoreMatchers.equalTo(0)); //
    }


    @Test
    public void test__FluentAttributeMatcher__should_not_be_moved_or_renamed() {
        // Rename with caution!
        // Because it's used in a sysprop that's part of the API.
        // Rename anyway -> keep sysprop backwards compatible.
        assertThat(FluentAttributeMatcher.class.getCanonicalName(),
            is("org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher"));
    }
}

