/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentcollections;

import org.hamcrest.Matcher;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiPredicate;

@SuppressWarnings("PackageVisibleField")
class Settings<X> {
    Class<X> klass;
    boolean ordered;
    boolean sorted;
    boolean unique;
    boolean mustNotHaveUnexpectedItems;
    int expectedSize = -1;
    @SuppressWarnings("unchecked")
    Matcher<X>[] expectations = new Matcher[0];
    Comparator<X> comparator;
    BiPredicate<X, X> equator = Objects::equals;

}
