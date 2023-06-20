/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import java.util.Map;
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
     * @return FluentIterableMatcher.
     */
    public static <X, C extends Iterable<X>> FluentIterableMatcher<X, C> fluentIterableMatcher(final Class<X> klass, final Config config){
        final Prose<X> prose = new Prose<>(config.getSymbols(), config.getStringifiers());
        return new FluentIterableMatcher<X, C>(klass, prose, config).debugging(config.isInDebugMode());
    }

    /**
     * <p>Factory method for a {@link FluentIterableMatcher}
     * to match an <i>actual</i> {@code Iterable}'s properties.</p>
     *
     * @param iterable to steal type from
     * @param <X> Expected type of the actual {@code Iterable}'s items.
     * @param <C> Expected type of the actual {@code Iterable}.
     * @param config The {@link Config} to apply to the matcher.
     * @return FluentIterableMatcher.
     */
    public static <X, C extends Iterable<X>> FluentIterableMatcher<X, C> fluentIterableMatcherLike(@SuppressWarnings("unused") final C iterable, final Config config){
        final Prose<X> prose = new Prose<>(config.getSymbols(), config.getStringifiers());
        return new FluentIterableMatcher<X, C>(null, prose, config).debugging(config.isInDebugMode());
    }

    /**
     * <p>Factory method for a {@link FluentMapMatcher}
     * to match an <i>actual</i> {@code Map}'s properties.</p>
     *
     * @param map for type inference
     * @param <K> Expected type of the actual {@code Map}'s keys.
     * @param <V> Expected type of the actual {@code Map}s values.
     * @return FluentMapMatcher.
     */
    public static <K, V> FluentMapMatcher<K, V> aMapLike(final Map<K, V> map){
        return new FluentMapMatcher<>(map);
    }

    /**
     * <p>Factory method for a {@link FluentMapMatcher}
     * to match an <i>actual</i> {@code Map}'s properties.</p>
     *
     * @param map for type inference
     * @param <K> Expected type of the actual {@code Map}'s keys.
     * @param <V> Expected type of the actual {@code Map}s values.
     * @param config The {@link Config} to apply to the matcher.
     * @return FluentMapMatcher.
     */
    public static <K, V> FluentMapMatcher<K, V> aMapLike(final Map<K, V> map, final Config config){
        final Prose<Map.Entry<K, V>> prose = new Prose<>(config.getSymbols(), config.getStringifiers());
        return new FluentMapMatcher<>(map, prose, config);
    }

}
