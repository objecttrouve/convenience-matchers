/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

class TrackingTree {


    private final Method m;
    private final List<TrackingTree> called = new LinkedList<>();
    private Throwable e;
    private boolean stopped = false;
    private final List<TrackingTree> array = new LinkedList<>();

    static TrackingTree trackingCalls() {
        return new TrackingTree();
    }

    TrackingTree() {
        this(null);
    }

    private TrackingTree(final Method m) {
        this.m = m;
    }

    TrackingTree track(final Method m) {
        if (!stopped) {
            final TrackingTree trackingTree = new TrackingTree(m);
            called.add(trackingTree);
            return trackingTree;
        }
        return new TrackingTree();
    }

    List<TrackingTree> tracked() {
        return new ArrayList<>(called);
    }

    Optional<Method> method() {
        return ofNullable(m);
    }

    void err(final Throwable e) {
        if (!stopped) {
            this.e = e;
        }
    }

    Optional<Throwable> error() {
        return ofNullable(e);
    }

    void stopTracking() {
        called.forEach(TrackingTree::stopTracking);
        stopped = true;
    }

    TrackingTree array(final Method m) {
        final TrackingTree arrayTree = new TrackingTree(m);
         this.array.add(arrayTree);
        return arrayTree;
    }

    List<TrackingTree> getArray() {
        return array;
    }
}
