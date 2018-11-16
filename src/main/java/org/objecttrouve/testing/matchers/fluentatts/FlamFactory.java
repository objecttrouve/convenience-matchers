/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.objecttrouve.testing.matchers.api.Config;

/**
 * Factory to build a {@link FluentAttributeMatcher} with package-private collaborateurs.
 */
public class FlamFactory {

    /**
     * <p>
     * Factory method for a {@link FluentAttributeMatcher}
     * to match an <i>actual</i> object's properties.</p>
     *
     * @param <T> Expected type of the actual object.
     * @param klass The expected class of the actual object.
     * @param config The {@link Config} to to apply to the matcher.
     * @return FluentAttributeMatcher.
     */
    public static <T> FluentAttributeMatcher<T> fluentAttributeMatcher(final Class<T> klass, final Config config) {
        if (klass == null) {
            throw new IllegalArgumentException("Class arg must not be null.");
        }
        final Prose prose = new Prose(config.getSymbols(), config.getStringifiers());
        //noinspection deprecation
        return new FluentAttributeMatcher<T>(prose).debugging(config.isInDebugMode());
    }
}
