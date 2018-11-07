/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import org.objecttrouve.testing.matchers.api.Config;

/**
 * Factory to build a {@link FluentIterableMatcher} with package-private collaborateurs.
 */
public class FlimFactory {

    /**
     * <p>Factory method for a {@link FluentIterableMatcher}
     * to match an <i>actual</i> {@code Iterable}'s properties.</p>
     *
     * @param <X> Expected type of the actual {@code Iterable}'s items.
     * @param <C> Expected type of the actual {@code Iterable}.
     * @param klass The expected class of the actual {@code Iterable}.
     * @param config The {@link Config} to apply to the matcher.
     * @return FluentAttributeMatcher.
     */
    public static <X, C extends Iterable<X>> FluentIterableMatcher<X, C> fluentIterableMatcher(final Class<X> klass, final Config config){
        final Prose<X> prose = new Prose<>(config.getSymbols(), config.getStringifiers());
        return new FluentIterableMatcher<>(klass, prose, config);
    }

}
