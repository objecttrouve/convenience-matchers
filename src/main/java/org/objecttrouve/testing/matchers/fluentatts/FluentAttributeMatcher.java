/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * {@link org.hamcrest.TypeSafeMatcher} implementation
 * offering a fluent-style lambda-accepting API
 * to match multiple properties of the <i>actual</i> object
 * simultaneously.
 * </p>
 * <p>
 * Example:
 * </p>
 *
 * <pre>
 * <code>
 *
 * package org.objecttrouve.testing.matchers.fluentatts;
 *
 * import org.junit.Ignore;
 * import org.junit.Test;
 *
 * import static org.hamcrest.CoreMatchers.is;
 * import static org.hamcrest.MatcherAssert.assertThat;
 * // Use the factory method for syntactic sugaring and configurability!
 * import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
 * import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;
 *
 * public class Example {
 *
 * static class Result {
 * private final String stringValue;
 * private final int intValue;
 * private final boolean boolValue;
 *
 * Result(final String stringValue, final int intValue, final boolean boolValue) {
 * this.stringValue = stringValue;
 * this.intValue = intValue;
 * this.boolValue = boolValue;
 * }
 *
 *
 * public String getStringValue() {
 * return stringValue;
 * }
 *
 * public int getIntValue() {
 * return intValue;
 * }
 *
 * public boolean isBoolValue() {
 * return boolValue;
 * }
 * }
 *
 * static Result methodWithResult(final String s, final int i, final boolean b){
 * return new Result(s, i, b);
 * }
 *
 * private static final Attribute&lt;Result, String&gt; stringValue = attribute("stringValue", Result::getStringValue);
 * private static final Attribute&lt;Result, Integer&gt; intValue = attribute("intValue", Result::getIntValue);
 * private static final Attribute&lt;Result, Boolean&gt; boolValue = attribute("booleanValue", Result::isBoolValue);
 *
 * {@literal @}Test
 * public void testSomething(){
 *
 * final Result result = methodWithResult("2=", 1, false);
 *
 * assertThat(result, is(//
 * a(Result.class)//
 * .with(stringValue, "1=") //
 * .with(intValue, is(2)) //
 * .with(boolValue, true)
 * ));
 * }
 *
 * }
 *
 *
 * </code>
 * </pre>
 *
 */
public class FluentAttributeMatcher<T> extends TypeSafeMatcher<T> {


    private final List<Expectation<T, ?>> expectations = new LinkedList<>();
    private final List<Result> results = new LinkedList<>();
    private final boolean tracking;

    /**
     * New instance.
     *
     * @param tracking attempt (expensively!) to create human-readable descriptions of attribute lambdas
     */
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
     * Builder method to set up checking {@code Objects.equals(attribute.getGetter().apply(), expectedValue)}.
     * The name of the attribute is used in the description in case there is a mismatch.
     * If the @param expectedValue is a {@link org.hamcrest.Matcher}, delegates to {@link FluentAttributeMatcher#withMatching(org.objecttrouve.testing.matchers.fluentatts.Attribute, org.hamcrest.Matcher)}.
     *
     * @param expectedValue the expected value
     * @param attribute     {@link org.objecttrouve.testing.matchers.fluentatts.Attribute} describing the property to be checked.
     * @param <O>           type of the value returned by the getter
     * @return FluentAttributeMatcher
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
     * Builder method to set up checking {@code Objects.equals(attribute.getGetter().apply(), expectedValue)}.
     * The name of the attribute is used in the description in case there is a mismatch.
     *
     * @param expectedValue the expected value
     * @param attribute     {@link org.objecttrouve.testing.matchers.fluentatts.Attribute} describing the property to be checked.
     * @param <O>           type of the value returned by the getter
     * @return FluentAttributeMatcher
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
     * @param matcher {@link org.hamcrest.Matcher} to be applied
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
     * Builder method to set up checking {@code matcher.matches(attribute.getGetter().apply())}.
     * The name of the attribute is used in the description in case there is a mismatch.
     *
     * @param matcher   {@link org.hamcrest.Matcher} to be applied
     * @param attribute {@link org.objecttrouve.testing.matchers.fluentatts.Attribute} describing the property to be checked.
     * @param <O>       type of the value returned by the getter
     * @return FluentAttributeMatcher
     */
    @SuppressWarnings("WeakerAccess")
    public <O> FluentAttributeMatcher<T> withMatching(final Attribute<T, O> attribute, final Matcher<O> matcher) {
        check(attribute);
        check(matcher);
        expectations.add(new Expectation<>(attribute.getName(), attribute.getGetter(), matcher::matches, null, matcher));
        return this;
    }

    @Override
    protected boolean matchesSafely(final T item) {
        results.clear();
        for (final Expectation<T, ?> exp : expectations) {
            final Result<?> result = matching(item, exp);
            if (result.isNotMatched()) {
                results.add(result);
            }
        }
        return results.isEmpty();
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
        if (!results.isEmpty()) {
            description.appendText("\n\t");
            for (final Result r : results) {
                Prose.wording(description, r);
                description.appendText("\n\t");
            }
            Prose.appendEnableTrackingHint(description, tracking);
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
