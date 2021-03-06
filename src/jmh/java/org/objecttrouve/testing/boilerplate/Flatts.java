/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.boilerplate;

import org.objecttrouve.testing.matchers.ConvenientMatchers;
import org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher;

public class Flatts {

    public static <T> FluentAttributeMatcher<T> aNonTracking(@SuppressWarnings("UnusedParameters") final Class<T> clazz) {
        return ConvenientMatchers.a(clazz);
    }

}
