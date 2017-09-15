/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.Matcher;

import java.util.function.Function;
import java.util.function.Predicate;

class Expectation<T, O> {
    private final Function<T, O> getter;
    private final Predicate<O> expectation;
    private final O expectedValue;
    private final Matcher<O> matcher;

    Expectation(final Function<T, O> getter, final Predicate<O> expectation, final O expectedValue, final Matcher<O> matcher) {
        this.getter = getter;
        this.expectation = expectation;
        this.expectedValue = expectedValue;
        this.matcher = matcher;
    }

    Function<T, O> getGetter() {
        return getter;
    }

    Predicate<O> getExpectation() {
        return expectation;
    }

    O getExpectedValue() {
        return expectedValue;
    }

    Matcher getMatcher(){
        return matcher;
    }

    boolean isAboutMatcher(){
        return matcher != null;
    }
}
