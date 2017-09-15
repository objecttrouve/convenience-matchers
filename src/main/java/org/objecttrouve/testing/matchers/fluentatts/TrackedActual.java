/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

class TrackedActual<O> {
    private final O actual;
    private final TrackingTree trackedCalls;

    TrackedActual(final O actual, final TrackingTree trackedCalls) {
        this.actual = actual;
        this.trackedCalls = trackedCalls;
    }

    TrackingTree getTrackedCalls() {
        return trackedCalls;
    }

    O getActual() {
        return actual;
    }
}
