/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.*;
import org.junit.Test;
import org.objecttrouve.testing.matchers.ConvenientMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.*;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;


@SuppressWarnings({"ResultOfMethodCallIgnored", "deprecation"})
public class FluentAttributeMatcherMatcherTest {


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

        final FluentAttributeMatcher matcher = ConvenientMatchers.a(Thing.class);

        assertThat(matcher, notNullValue());
    }

    @Test
    public void testFactoryAnNotNull() {

        final FluentAttributeMatcher matcher = ConvenientMatchers.an(Thing.class);

        assertThat(matcher, notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryANullInput() {
        ConvenientMatchers.a(null);
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

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
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
        matching.matchesSafely(new Thing("not the value"));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetValue = \"the value\" <> \"not the value\"\n\t"));
    }

    @Test
    public void test__with_describes__mismatch() {


        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(value, "the value"); //
        matching.matchesSafely(new Thing("not the value"));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tvalue = \"the value\" <> \"not the value\"\n\t"));
    }

    @Test
    public void test__withValue_describes__mismatch() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .withValue(value, "the value"); //
        matching.matchesSafely(new Thing("not the value"));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tvalue = \"the value\" <> \"not the value\"\n\t"));
    }


    @Test
    public void test__having__happy_path() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .having(Thing::getValue, equalTo("the value")); //

        assertThat(new Thing("the value"), is(matching));
    }


    @Test
    public void test__withMatching__happy_path() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .withMatching(value, equalTo("the value")); //

        assertThat(new Thing("the value"), is(matching));
    }


    @Test
    public void test__with__happy_path__expecting_Matcher() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
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
    public void test__withMatching__mismatch() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .withMatching(value, equalTo("the value")); //

        assertThat(new Thing("the other value"), not(is(matching)));
    }


    @Test
    public void test__with__mismatch__expecting_Matcher() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(value, equalTo("the value")); //

        assertThat(new Thing("the other value"), not(is(matching)));
    }


    @Test
    public void test__with__mismatch__expecting_Matcher__with_inappropriate_type() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = aTracking(Thing.class)//
            .with(value, everyItem(notNullValue())); //

        assertFalse(matcher.matches("It's a String!"));

    }


    private static class InappropriateExpectation {
    }

    @Test
    public void test__with__mismatch__expecting_value__with_inappropriate_type() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = aTracking(Thing.class)//
            .with(value, new InappropriateExpectation()); //

        assertFalse(matcher.matches(new Thing("This is not what we expected.")));

    }

    private static class HeirThing extends Thing {

        HeirThing(final String value) {
            super(value);
        }
    }

    @Test
    public void test__with__mismatch__expecting_Matcher__with_super_type() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = aTracking(Thing.class)//
            .with(value, aTracking(HeirThing.class)); //

        assertTrue(matcher.matches(new Thing("It's a String!")));
    }

    @Test
    public void test__with__mismatch__expecting_Matcher__with_sub_type() {

        final Attribute<HeirThing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<HeirThing> matcher = aTracking(HeirThing.class)//
            .with(value, aTracking(HeirThing.class)); //

        assertFalse(matcher.matches(new Thing("It's a String!")));
    }

    @Test
    public void test__with__mismatch__expecting_null() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = aTracking(Thing.class)//
            .with(value, null); //

        assertFalse(matcher.matches(new Thing("This is not what we expected.")));

    }

    @Test
    public void test__with__mismatch__expecting_String_getting_null_target() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = aTracking(Thing.class)//
            .with(value, "We expect a String"); //

        assertFalse(matcher.matches(null));
    }

    @Test
    public void test__with__mismatch__expecting_matching_String_getting_null_target() {

        final Attribute<Thing, String> value = attribute("value", Thing::getValue);

        final FluentAttributeMatcher<Thing> matcher = aTracking(Thing.class)//
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
        matching.matchesSafely(new Question(new Answer(24)));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetAnswer =~ answer 42\n\t"));
    }

    @Test
    public void test__withMatching__mismatch__describes_nicely() {

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
        final FluentAttributeMatcher<Question> matching = aTracking(Question.class)//
            .withMatching(answer, answerMatcher); //
        matching.matchesSafely(new Question(new Answer(24)));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tanswer =~ answer 42\n\t"));
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
        matching.matchesSafely(new HumanThinking(new Philosophy(new Question(new Answer(24)), new Question(new Answer(31)))));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetPhilosophy/getQuestions/get/getAnswer =~ answer 42\n\t"));
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
        matching.matchesSafely(new Untrackable("untrackable"));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetUntrackable/??? = \"trackable\" <> \"untrackable\"\n\t"));
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
        matching.matchesSafely(new NestedUntrackable(new Untrackable("untrackable")));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetNested/getUntrackable/??? = \"trackable\" <> \"untrackable\"\n\t"));
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
        matching.matchesSafely(new SubThing("value of superclass"));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetValue = \"value of subclass\" <> \"value of superclass\"\n\t"));
    }

    @Test
    public void testDescribesSuperclassCallsWithLambda() {

        //noinspection Convert2MethodRef
        final FluentAttributeMatcher<SubThing> matching = aTracking(SubThing.class)//
            .with(t -> t.getValue(), "value of subclass"); //
        matching.matchesSafely(new SubThing("value of superclass"));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetValue = \"value of subclass\" <> \"value of superclass\"\n\t"));
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
        matching.matchesSafely(new Thing("not null"));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetValue = null <> \"not null\"\n\t"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__with__null_arg__self_describing() {
        final Function f = null;
        //noinspection unchecked
        aTracking(Thing.class)//
            .with(f, null); //
    }


    @Test(expected = IllegalArgumentException.class)
    public void test__with__null_arg() {
        final Attribute a = null;
        //noinspection unchecked
        aTracking(Thing.class)//
            .with(a, null); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__with__Attribute_having_null_name() {
        final Attribute<String, Integer> a = attribute(null, String::length);
        //noinspection unchecked
        aTracking(String.class)//
            .with(a, null); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__with__Attribute_having_null_getter() {
        final Attribute<String, Integer> a = attribute("length", null);
        //noinspection unchecked
        aTracking(String.class)//
            .with(a, null); //
    }


    @Test(expected = IllegalArgumentException.class)
    public void test__withValue__null_arg() {
        final Attribute a = null;
        //noinspection unchecked
        aTracking(Thing.class)//
            .withValue(a, null); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__withValue__Attribute_having_null_name() {
        final Attribute<String, Integer> a = attribute(null, String::length);
        //noinspection unchecked
        aTracking(String.class)//
            .withValue(a, null); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__withValue__Attribute_having_null_getter() {
        final Attribute<String, Integer> a = attribute("length", null);
        //noinspection unchecked
        aTracking(String.class)//
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
    public void test__withMatching__Attribute_having_null_name() {
        final Attribute<String, Integer> a = attribute(null, String::length);

        //noinspection unchecked
        aTracking(String.class)//
            .withMatching(a, CoreMatchers.equalTo(0)); //
    }

    @Test(expected = IllegalArgumentException.class)
    public void test__withMatching__Attribute_having_null_getter() {
        final Attribute<String, Integer> a = attribute("length", null);

        //noinspection unchecked
        aTracking(String.class)//
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
        matching.matchesSafely(new ThingWithNumbers(1, 2, 3));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        /*
        * Somewhat awkward but not really the most prominent use case.
        */
        assertThat(description.toString(), is("\n\tgetNumbers/get&sum&count = <7> <> <10L>\n\t"));
    }


    @Test
    public void test__multiple_expectations__all_matching() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .withValue(attribute("count", ThingWithNumbers::count), 3L)//
            .withValue(attribute("sum",ThingWithNumbers::sum), 6)//
            .withMatching(attribute("length", twn -> twn.getNumbers().size()), is(3))//
            ;//

        assertThat(new ThingWithNumbers(1, 2, 3), is(matching));
    }




    @Test
    public void test__multiple_expectations__some_matching__getScore() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .withValue(attribute("count", ThingWithNumbers::count), 3L)//
            .withValue(attribute("sum",ThingWithNumbers::sum), 6)//
            .withMatching(attribute("length", twn -> twn.getNumbers().size()), is(3))//
            .withMatching(attribute("length2", twn -> twn.getNumbers().size()*2), is(6))//
            ;//
        assertThat(new ThingWithNumbers(1, 2, 3), is(matching));

        final double score = matching.getScore();

        assertThat(score,  is(1.0));
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

        assertThat(score,  is(0.0));
    }


    @Test
    public void test__multiple_expectations__none_matching__describes_nicely() {
        final FluentAttributeMatcher<ThingWithNumbers> matching = aTracking(ThingWithNumbers.class)//
            .with(ThingWithNumbers::count, 6)//
            .with(ThingWithNumbers::sum, 3)//
            .having(twn -> twn.getNumbers().size(), is(2))//
            ;//

        matching.matchesSafely(new ThingWithNumbers(1, 2, 3));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        /*
        * Somewhat awkward but not really the most prominent use case.
        */
        assertThat(description.toString(), is("\n" //
            + "\tcount = <6> <> <3L>\n" //
            + "\tsum = <3> <> <6>\n" //
            + "\tgetNumbers/size =~ is <2>\n" //
            + "\t"));
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

        matching.matchesSafely(new ThingWithNumbers(1, 2, 3));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        /*
        * Somewhat awkward but not really the most prominent use case.
        */
        assertThat(description.toString(), is("\n" //
            + "\tsum = <3> <> <6>\n" //
            + "\tgetNumbers/size =~ is <2>\n" //
            + "\t"));
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

        matching.matchesSafely(new ThingWithNumbers(1, 2, 3));
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n" + //
            "\tsum = <3> <> <6>\n" +//
            "\tgetNumbers =~ items 5,6,7\n" +//
            "\t"));
    }

    @Test
    public void testMatcherIsReset() {

        final FluentAttributeMatcher<Thing> matching = aTracking(Thing.class)//
            .with(Thing::getValue, "the value"); //

        assertThat(new Thing("not the value"), not(is(matching)));
        assertThat(new Thing("the value"), is(matching));
        final boolean matches = matching.matchesSafely(new Thing("not the value"));
        assertFalse(matches);
        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetValue = \"the value\" <> \"not the value\"\n\t"));
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

        matching.matchesSafely(new ThingArray(//
            new Thing("the first value"),//
            new Thing("the second value"),//
            new Thing("the third value")//
        ));

        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetThings[...]/getValue = \"the third value\" <> \"the second value\"\n\t"));

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

        matching.matchesSafely(new ThingArray(//
            new Thing("the first value"),//
            new Thing("the second value"),//
            new Thing("the third value")//
        ));

        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetThings&getThings[...]/getValue&getThings[...]/getValue = \"the third value\" <> \"the second value\"\n\t"));

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

        matching.matchesSafely(new ThingArray(//
            new Thing("the first value"),//
            new Thing("the second value"),//
            new Thing("the third value")//
        ));

        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetThings&getThings[...]/getValue&getThings[...]/getValue&getThingsAsList&getThingsAsList = \"the third value\" <> \"the second value\"\n\t"));

    }


    @Test
    public void testSthInAnArrayNotMatchingButDescribedLovely4() {

        final FluentAttributeMatcher<ThingArray> matching = aTracking(ThingArray.class)//
            .with(ThingArray::getThings, "the third value"); //

        matching.matchesSafely(new ThingArray());

        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetThings = \"the third value\" <> []\n\t"));

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

        matching.matchesSafely(new Outer(new Outer.Inner("X")));

        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetInner/getS = \"S\" <> \"X\"\n\t"));

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

        matching.matchesSafely(new AnonymousAttributes());

        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tgetAttr/attr = \"a property\" <> \"an attribute\"\n\t"));

    }

    @Test
    public void testFunctionCallNotMatchingButDescribedLovely() {

        final FluentAttributeMatcher<AnonymousAttributes> matching = aTracking(AnonymousAttributes.class)//
            .with(a -> a.func().apply("an attribute"), "a property"); //

        matching.matchesSafely(new AnonymousAttributes());

        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tfunc/??? = \"a property\" <> \"an attribute\"\n\t"));

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

        matching.matchesSafely(new Proxying());

        final StringDescription description = new StringDescription();

        matching.describeTo(description);

        assertThat(description.toString(), is("\n\tproxied/??? = \"a property\" <> \"a real attribute\"\n\t"));

    }

    @Test
    public void testWhatsTheDescriptionIfThingsMatch() {

        /* To clarify conditions relied upon in the benchmark tests. */

        final Thing theThing = new Thing("the value");

        final FluentAttributeMatcher<Thing> matcher = aTracking(Thing.class)//
            .with(Thing::getValue, "the value"); //
        matcher.matches(theThing);
        final StringDescription d = new StringDescription();
        matcher.describeTo(d);

        assertThat(d.toString(), is(""));

        final Matcher<String> hamcrestMatcher = is("the value");
        hamcrestMatcher.matches(theThing.getValue());
        hamcrestMatcher.describeTo(d);

        final Matcher<String> hamcrestMatcher2 = is("not the value");
        hamcrestMatcher2.matches(theThing);
        final StringDescription d2 = new StringDescription();
        hamcrestMatcher2.describeTo(d2);

        assertThat(d.toString(), not(is(d2.toString())));
    }

    @Test
    public void test__FluentAttributeMatcher__should_not_be_moved_or_renamed() {
        // Rename with caution!
        // Because it's used in a sysprop that's part of the API now.
        // Rename anyway -> keep sysprop backwards compatible.
        assertThat(FluentAttributeMatcher.class.getCanonicalName(),
            is("org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher"));
    }

    private static <T> FluentAttributeMatcher<T> aTracking(@SuppressWarnings("UnusedParameters") final Class<T> clazz) {
        return new FluentAttributeMatcher<>(true);
    }

}