/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.objecttrouve.testing.matchers.api.ScorableMatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.objecttrouve.testing.matchers.fluentatts.Scorer.score;

/**
 * <p>
 * A {@code org.hamcrest.TypeSafeMatcher} implementation
 * to match multiple properties of an <i>actual</i> {@code Object}
 * at the same time.
 * </p>
 * <p>Offers a fluent API
 * in which the properties of the actual {@code Object} are represented
 * as method references or lambda expressions.</p>
 * <p>
 * Example:
 * </p>
 *
 * <pre>
 * <code>
 *
 * package org.objecttrouve.testing.matchers.fluentatts;
 *
 * import org.junit.Test;
 *
 * import static org.hamcrest.CoreMatchers.is;
 * import static org.hamcrest.MatcherAssert.assertThat;
 * import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
 * import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;
 *
 * public class Example {
 *
 *  private static final Attribute&lt;Result, String&gt; stringValue = attribute("stringValue", Result::getStringValue);
 *  private static final Attribute&lt;Result, Integer&gt; intValue = attribute("intValue", Result::getIntValue);
 *  private static final Attribute&lt;Result, Boolean&gt; boolValue = attribute("booleanValue", Result::isBoolValue);
 *
 *  {@literal @}Test
 *  public void testSomething(){
 *
 *      final Result result = methodWithResult("2=", 1, false);
 *
 *      assertThat(result, is(//
 *          a(Result.class)//
 *              .with(stringValue, "1=") //
 *              .with(intValue, is(2)) //
 *              .with(boolValue, true)
 *      ));
 * }

 *
 *  static class Result {
 *
 *      private final String stringValue;
 *      private final int intValue;
 *      private final boolean boolValue;
 *
 *      Result(final String stringValue, final int intValue, final boolean boolValue) {
 *          this.stringValue = stringValue;
 *          this.intValue = intValue;
 *          this.boolValue = boolValue;
 *      }
 *  }
 *
 *  public String getStringValue() {
 *      return stringValue;
 *  }
 *
 *  public int getIntValue() {
 *      return intValue;
 *  }
 *
 *  public boolean isBoolValue() {
 *      return boolValue;
 *  }
 *
 *
 *  static Result methodWithResult(final String s, final int i, final boolean b){
 *      return new Result(s, i, b);
 *  }
 *
 *
 * }
 *
 *
 * </code>
 * </pre>
 *
 */
public class FluentAttributeMatcher<T> extends TypeSafeMatcher<T> implements ScorableMatcher {


    private final List<Expectation<T, ?>> expectations = new LinkedList<>();
    private final List<Result> results = new LinkedList<>();
    private final boolean tracking;

    /**
     * New instance.
     *
     * @deprecated C'tor will be removed by v1.0. Use factory methods in {@code ConvenientMatchers}.
     * @param tracking attempt (expensively!) to create human-readable descriptions of attribute lambdas
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public FluentAttributeMatcher(final boolean tracking) {
        this.tracking = tracking;
    }

    /**
     * Builder method to set up checking {@code Objects.equals(getter.apply(), expectedValue)}.
     *
     * @param expectedValue the expected value
     * @param getter        Function returning the value to be checked
     * @param <O>           type of the value returned by the getter
     * @return FluentAttributeMatcher
     * @deprecated Method will be removed with version 1.0.0. Use {@link FluentAttributeMatcher#with(org.objecttrouve.testing.matchers.fluentatts.Attribute, java.lang.Object)}.
     */
    @Deprecated
    public <O> FluentAttributeMatcher<T> with(final Function<T, O> getter, final O expectedValue) {
        check(getter);
        expectations.add(new Expectation<>(null, getter, (actual) -> Objects.equals(actual, expectedValue), expectedValue, null));
        return this;
    }

    /**
     * <p>Builder method to formulate an expectation about a particular property of an <i>actual</i> {@code Object}.</p>
     * <p>If the  {@code expectedValue} is a {@code org.hamcrest.Matcher},
     * the call is delegated to {@link FluentAttributeMatcher#withMatching(org.objecttrouve.testing.matchers.fluentatts.Attribute, org.hamcrest.Matcher)},
     * otherwise to {@link FluentAttributeMatcher#withValue(org.objecttrouve.testing.matchers.fluentatts.Attribute, java.lang.Object)}.</p>
     *
     * @param expectedValue The expected value.
     * @param attribute     An {@link org.objecttrouve.testing.matchers.fluentatts.Attribute} describing the property to be checked.
     * @param <O>           The type of the value returned by the {@code Attribute}'s getter function.
     * @return FluentAttributeMatcher.
     */
    public <O> FluentAttributeMatcher<T> with(final Attribute<T, O> attribute, final Object expectedValue) {
        check(attribute);
        if (expectedValue instanceof Matcher) {
            //noinspection unchecked
            return withMatching(attribute, (Matcher<O>) expectedValue);
        } else {
            //noinspection unchecked
            return withValue(attribute, (O) expectedValue);
        }
    }

    /**
     * <p>Builder method to state an expected value for a particular property of an <i>actual</i> {@code Object}.</p>
     * @param expectedValue The expected value.
     * @param attribute     An {@link org.objecttrouve.testing.matchers.fluentatts.Attribute} describing the property to be checked.
     * @param <O>           The type of the value returned by the {@code Attribute}'s getter function.
     * @return FluentAttributeMatcher.
     */
    @SuppressWarnings("WeakerAccess")
    public <O> FluentAttributeMatcher<T> withValue(final Attribute<T, O> attribute, final O expectedValue) {
        check(attribute);
        expectations.add(new Expectation<>(attribute.getName(), attribute.getGetter(), (actual) -> Objects.equals(actual, expectedValue), expectedValue, null));
        return this;
    }

    /**
     * Builder method to set up checking {@code matcher.matches(getter.apply())}.
     *
     * @param matcher {@code org.hamcrest.Matcher} to be applied
     * @param getter  Function returning the value to be checked
     * @param <O>     type of the value returned by the getter
     * @return FluentAttributeMatcher
     * @deprecated Method will be removed with version 1.0.0. Use {@link FluentAttributeMatcher#with(org.objecttrouve.testing.matchers.fluentatts.Attribute, java.lang.Object)}.
     */
    @SuppressWarnings("WeakerAccess")
    @Deprecated
    public <O> FluentAttributeMatcher<T> having(final Function<T, O> getter, final Matcher<O> matcher) {
        check(getter);
        check(matcher);
        expectations.add(new Expectation<>(null, getter, matcher::matches, null, matcher));
        return this;
    }

    /**
     * <p>Builder method to set a {@code org.hamcrest.Matcher} for a particular property of an <i>actual</i> {@code Object}.</p>
     * @param matcher       A {@code org.hamcrest.Matcher} for the property value.
     * @param attribute     An {@link org.objecttrouve.testing.matchers.fluentatts.Attribute} describing the property to be checked.
     * @param <O>           The type of the value returned by the {@code Attribute}'s getter function.
     * @return FluentAttributeMatcher.
     */
    @SuppressWarnings("WeakerAccess")
    public <O> FluentAttributeMatcher<T> withMatching(final Attribute<T, O> attribute, final Matcher<O> matcher) {
        check(attribute);
        check(matcher);
        expectations.add(new Expectation<>(attribute.getName(), attribute.getGetter(), matcher::matches, null, matcher));
        return this;
    }

    /**
     * The ratio of matched expectations and all expectations.
     * @return Value between 0 and 1.
     */
    @Override
    public double getScore() {
        return score((expectations.size()-results.size()), expectations.size());
    }

    @Override
    protected boolean matchesSafely(final T item) {
        reset();
        for (final Expectation<T, ?> exp : expectations) {
            final Result<?> result = matching(item, exp);
            if (result.isNotMatched()) {
                results.add(result);
            }
        }
        return results.isEmpty();
    }

    private void reset() {
        results.clear();
    }


    private <O> Result<O> matching(final T item, final Expectation<T, O> exp) {

        final TrackedActual<O> tract = getActual(item, exp.getGetter(), shouldTrack(exp));
        final boolean matched = exp.getExpectation().test(tract.getActual());
        //noinspection unchecked
        return new Result(matched, tract.getTrackedCalls(), exp, tract.getActual());
    }

    private <O> boolean shouldTrack(final Expectation<T, O> exp) {
        return this.tracking && exp.getDescription() == null;
    }

    private <O> TrackedActual<O> getActual(final T item, final Function<T, O> getter, final boolean trackCall) {
        if (trackCall) {
            final TrackingTree calls = TrackingTree.trackingCalls();
            final T track = Tracker.track(item, calls);
            final O actual = apply(track, getter);
            calls.stopTracking();
            return new TrackedActual<>(actual, calls);
        } else {
            return new TrackedActual<>(apply(item, getter), null);
        }
    }

    private <O> O apply(final T target, final Function<T, O> getter) {
        try {
            return getter.apply(target);
        } catch (final ClassCastException e) {
            //noinspection unchecked
            results.add(new Result(false, null, new Expectation(Prose.typeMatchExpectationDescription, null, null, Prose.typeMismatchMsg(target), null), target.getClass().getName()));
            return null;
        }
    }

    @Override
    public void describeTo(final Description description) {
        expectations.forEach(e -> {
            description.appendText("\n\t");
            Prose.expectation(description, e);
            description.appendText("\n\t");
        });
    }

    @Override
    protected void describeMismatchSafely(final T item, final Description mismatchDescription) {
        matchesSafely(item);
        describeTheMismatch(mismatchDescription);
    }

    private void describeTheMismatch(final Description description) {
        if (!results.isEmpty()) {
            description.appendText("\n\t");
            for (final Result r : results) {
                Prose.wording(description, r);
                description.appendText("\n\t");
            }
        }
    }

    private <O> void check(final Attribute<T, O> attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("Please provide a non-null " + Attribute.class.getSimpleName() + ".");
        }
        if (attribute.getName() == null) {
            throw new IllegalArgumentException("Please provide a non-null name for the " + Attribute.class.getSimpleName() + ".");
        }
        check(attribute.getGetter());
    }

    private <O> void check(final Function<T, O> getter) {
        if (getter == null) {
            throw new IllegalArgumentException("Please provide a getter function.");
        }
    }

    private <O> void check(final Matcher<O> matcher) {
        if (matcher == null) {
            throw new IllegalArgumentException("Please provide a matcher.");
        }
    }


}
