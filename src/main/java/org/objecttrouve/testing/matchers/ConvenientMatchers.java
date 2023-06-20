/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers;

import java.util.Map;
import org.objecttrouve.testing.matchers.customization.MatcherFactory;
import org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher;
import org.objecttrouve.testing.matchers.fluentits.FluentIterableMatcher;
import org.objecttrouve.testing.matchers.fluentits.FluentMapMatcher;

/**
 * <p>
 * Factory for {@link FluentAttributeMatcher} and {@link FluentIterableMatcher}.
 * </p>
 */
public class ConvenientMatchers {

    private static final MatcherFactory defaultFactory = MatcherFactory.factory().build();

    private ConvenientMatchers(){
        /* Not there. */
    }

    /**
     * <p>
     * Factory method for a {@link FluentAttributeMatcher}
     * to match an <i>actual</i> object's properties.</p>
     *
     * <p>Same as {@link ConvenientMatchers#an(java.lang.Class)},
     * just for class names that start with a consonant.</p>
     *
     * @param klass The expected class of the actual object.
     * @param <T> Expected type of the actual object.
     * @return FluentAttributeMatcher.
     */
    public static <T> FluentAttributeMatcher<T> a(final Class<T> klass) {
        return defaultFactory.instanceOf(klass);
    }


    /**
     * <p>Factory method for a {@link FluentAttributeMatcher}
     * to match an <i>actual</i> object's properties.</p>
     *
     * <p>Same as {@link ConvenientMatchers#a(java.lang.Class)},
     * just for class names that start with a vowel.</p>
     *
     * @param klass The expected class of the actual object.
     * @param <T> Expected type of the actual object.
     * @return FluentAttributeMatcher.
     */
    public static <T> FluentAttributeMatcher<T> an(final Class<T> klass) {
        return a(klass);
    }

    /**
     * <p>Factory method for a {@link FluentIterableMatcher}
     * to match an <i>actual</i> {@code Iterable}'s properties.</p>
     *
     * @param klass The expected class of the actual {@code Iterable}.
     * @param <X> Expected type of the actual {@code Iterable}'s items.
     * @param <C> Expected type of the actual {@code Iterable}.
     * @return FluentIterableMatcher.
     */
    public static <X, C extends Iterable<X>> FluentIterableMatcher<X, C> anIterableOf(final Class<X> klass){
        return defaultFactory.iterableOf(klass);
    }

    /**
     * <p>Factory method for a {@link FluentIterableMatcher}
     * to match an <i>actual</i> {@code Iterable}'s properties.</p>
     *
     * @param iterable iterable to steal type from.
     * @param <X> Expected type of the actual {@code Iterable}'s items.
     * @param <C> Expected type of the actual {@code Iterable}.
     * @return FluentIterableMatcher.
     */
    public static <X, C extends Iterable<X>> FluentIterableMatcher<X, C> anIterableLike(final C iterable){
        return defaultFactory.iterableLike(iterable);
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
        return defaultFactory.aMapLike(map);
    }

    /**
     * <p>Retrieve a {@link MatcherFactory} on which symbols, stringifiers and other settings can be configured.</p>
     * @return {@link MatcherFactory}*/
    public static MatcherFactory.Builder customized(){
        return MatcherFactory.factory();
    }

}
