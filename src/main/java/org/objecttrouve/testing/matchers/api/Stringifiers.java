/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.api;

import java.util.Optional;
import java.util.function.Function;

/**
 * Mappings of classes to stringifying {@code java.util.function.Function}s.
 * Intended to provide useful string descriptions of an instance when the respective class doesn't have a suitable {@code Object#toString()} implementation.
 */
public interface Stringifiers {

    /**
     * Short description {@code java.util.function.Function} for a class.
     * @param <X> Type of obj.
     * @param obj the instance to stringify.
     * @return {@code Optional} stringifier function foe {@code obj}.
     */
    <X> Optional<Function<X, String>> getShortStringifier(final X obj);

    /**
     * Exhaustive description {@code java.util.function.Function} for a class.
     * @param <X> Type of obj.
     * @param obj the instance to stringify.
     * @return {@code Optional} stringifier function foe {@code obj}.
     */
    <X> Optional<Function<X, String>> getDebugStringifier(final X obj);
}
