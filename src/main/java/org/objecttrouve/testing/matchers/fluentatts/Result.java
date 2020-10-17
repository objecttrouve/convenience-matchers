/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

@SuppressWarnings("rawtypes")
class Result<O> {

    private final boolean matched;
    private final Expectation expectation;
    private final O actual;

    Result(final boolean matched, final Expectation expectation, final O actual) {
        this.matched = matched;
        this.expectation = expectation;
        this.actual = actual;
    }


    private boolean isMatched() {
        return matched;
    }

    boolean isNotMatched(){
        return !isMatched();
    }

    Expectation getExpectation() {
        return expectation;
    }

    public O getActual() {
        return actual;
    }
}
