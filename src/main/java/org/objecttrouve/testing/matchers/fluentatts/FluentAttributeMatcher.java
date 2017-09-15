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
 <p>
 {@link org.hamcrest.TypeSafeMatcher} implementation
 offering a fluent-style lambda-accepting API
 to match multiple properties of the <i>actual</i> object
 simultaneously.
 </p>
 <p>
 Example:
 </p>

  <pre>
 <code>

    package org.objecttrouve.testing.matchers.examples;
    import org.junit.Test;
    import java.util.Properties;
    import java.util.Set;
    import static org.hamcrest.CoreMatchers.hasItems;
    import static org.hamcrest.CoreMatchers.is;
    import static org.junit.Assert.assertThat;
    import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;

    public class FluentAttributeMatcherExample {

        {@literal @}Test
        public void example() {

            final Properties props = new Properties();
            props.put("attribute1", "value1");
            props.put("attribute2", "value2");
            props.put("attribute3", "value3");

            assertThat(props, is(//
                a(Properties.class)//
                    .with(Properties::size, 3)//
                    .having(Properties::stringPropertyNames, //
                        hasItems(//
                            "attribute1", //
                            "attribute2", //
                            "attribute3" //
                        )) //
                    ));
            }
        }

 </code>
 </pre>
 */
public class FluentAttributeMatcher<T> extends TypeSafeMatcher<T> {


    private final List<Expectation<T, ?>> expectations = new LinkedList<>();
    private final List<Result> results = new LinkedList<>();
    private final boolean tracking;

    /**
     * New instance.
     * @param tracking attempt (expensively!) to create human-readable descriptions of attribute lambdas
     */
    public FluentAttributeMatcher(final boolean tracking) {
        this.tracking = tracking;
    }

    /**
     * Builder method to set up checking {@code Objects.equals(getter.apply(), expectedValue)}.
     * @param expectedValue the expected value
     * @param getter Function returning the value to be checked
     * @param <O> type of the value returned by the getter
     * @return FluentAttributeMatcher
     */
    public <O> FluentAttributeMatcher<T> with(final Function<T, O> getter, final O expectedValue) {
        check(getter);
        expectations.add(new Expectation<>(getter, (actual) -> Objects.equals(actual, expectedValue), expectedValue, null));
        return this;
    }

    /**
     * Builder method to set up checking {@code matcher.matches(getter.apply())}.
     * @param matcher {@link org.hamcrest.Matcher} to be applied
     * @param getter Function returning the value to be checked
     * @param <O> type of the value returned by the getter
     * @return FluentAttributeMatcher
     */
    public <O> FluentAttributeMatcher<T> having(final Function<T, O> getter, final Matcher<O> matcher) {
        check(getter);
        check(matcher);
        expectations.add(new Expectation<>(getter, matcher::matches, null, matcher));
        return this;
    }

    @Override
    protected boolean matchesSafely(final T item) {
        results.clear();
        for (final Expectation<T, ?> exp : expectations) {
            final Result<?> result = matching(item, exp);
            if (result.isNotMatched()){
                results.add(result);
            }
        }
        return results.isEmpty();
    }


    private <O> Result<O> matching(final T item, final Expectation<T, O> exp) {

        final TrackedActual<O> tract = getActual(item, exp.getGetter());
        final boolean matched = exp.getExpectation().test(tract.getActual());
        //noinspection unchecked
        return new Result(matched, tract.getTrackedCalls(), exp, tract.getActual());
    }

    private <O>TrackedActual<O> getActual(final T item, final Function<T, O> getter){
        if(tracking){
            final TrackingTree calls = TrackingTree.trackingCalls();
            final O actual = getter.apply(Tracker.track(item, calls));
            calls.stopTracking();
            return new TrackedActual<>(actual, calls);
        } else {
            return new TrackedActual<>(getter.apply(item), null);
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
