/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

class Result<O> {

    private final boolean matched;
    private final TrackingTree called;
    private final Expectation expectation;
    private final O actual;

    Result(final boolean matched, final TrackingTree called, final Expectation expectation, final O actual) {
        this.matched = matched;
        this.called = called;
        this.expectation = expectation;
        this.actual = actual;
    }


    boolean isMatched() {
        return matched;
    }

    boolean isNotMatched(){
        return !isMatched();
    }

    public TrackingTree getCalled() {
        return called;
    }

    public Expectation getExpectation() {
        return expectation;
    }

    public O getActual() {
        return actual;
    }
}
