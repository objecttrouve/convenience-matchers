/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

class Actual<O> {
    private final O actual;

    Actual(final O actual) {
        this.actual = actual;
    }

    O getActual() {
        return actual;
    }
}
