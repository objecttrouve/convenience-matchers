/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers;

import org.objecttrouve.testing.matchers.customization.MatcherFactory;
import org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher;
import org.objecttrouve.testing.matchers.fluentits.FluentIterableMatcher;

/**
 * <p>
 * Factory for {@link FluentAttributeMatcher} and {@link FluentIterableMatcher}.
 * </p>
 */
public class ConvenientMatchers {

    private static final boolean tracking = tracking();

    private static final String sysPropTracking = "org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher.tracking";
    private static final MatcherFactory defaultFactory = MatcherFactory.factory().build();

    private ConvenientMatchers(){
        /* Not there. */
    }

    private static boolean tracking() {
        return Boolean.parseBoolean(System.getProperty(sysPropTracking, "false"));
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
     * @deprecated The associated feature is deprecated. Don't use it at all.
     * @return {@code true} if lambda tracking is enabled, {@code false} otherwise.
     */
    @Deprecated
    public static boolean isTracking() {
        return tracking;
    }


    /**
     * <p>Factory method for a {@link FluentIterableMatcher}
     * to match an <i>actual</i> {@code Iterable}'s properties.</p>
     *
     * @param klass The expected class of the actual {@code Iterable}.
     * @param <X> Expected type of the actual {@code Iterable}'s items.
     * @param <C> Expected type of the actual {@code Iterable}.
     * @return FluentAttributeMatcher.
     */
    public static <X, C extends Iterable<X>> FluentIterableMatcher<X, C> anIterableOf(final Class<X> klass){
        return defaultFactory.iterableOf(klass);
    }

    /**
     * <p>Retrieve a {@link MatcherFactory} on which symbols, stringifiers and other settings can be configured.</p>
     * @return {@link MatcherFactory}*/
    public static MatcherFactory.Builder customized(){
        return MatcherFactory.factory();
    }

}
