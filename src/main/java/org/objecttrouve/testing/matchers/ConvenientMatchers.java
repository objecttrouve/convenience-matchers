/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers;

import org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher;
import org.objecttrouve.testing.matchers.fluentits.FluentIterableMatcher;

/**
 * <p>
 * {@link FluentAttributeMatcher} factory.
 * </p>
 * <p>
 * Performs minimal configuration such that lambda tracking
 * is en- or disabled via a system property.
 * </p>
 * <p>
 * "Lambda tracking" refers to the attempt to track what's happening in the getter functions.
 * The goal is to deduce meaningful String descriptions of non-matching attributes.
 * Tracking comes with a significant performance penalty, so it should only be enabled during development.
 * </p>
 * <p>
 * Set
 * <br>
 * {@code org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher.tracking=true}
 * <br>
 * for human-readable descriptions of failing tests.
 * <br>
 * Keep the default
 * <br>
 * {@code org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher.tracking=false}
 * <br>
 * for production.
 * </p>
 */
public class ConvenientMatchers {

    private static final boolean tracking = tracking();
    /**
     * System property to enable lambda tracking:
     * <br>
     * {@code org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher.tracking=<true|false>}
     * */
    private static final String sysPropTracking = "org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher.tracking";

    private ConvenientMatchers(){
        /* Not there. */
    }
    private static boolean tracking() {
        return Boolean.valueOf(System.getProperty(sysPropTracking, "false"));
    }

    /**
     * Factory method for a {@link FluentAttributeMatcher}
     * matching the <i>actual</i> object's properties.
     *
     * @param klass <i>actual</i> object class
     * @param <T> type of <i>actual</i> object
     * @return FluentAttributeMatcher
     */
    public static <T> FluentAttributeMatcher<T> a(final Class<T> klass) {
        if (klass == null) {
            throw new IllegalArgumentException("Class arg must not be null.");
        }
        return new FluentAttributeMatcher<>(tracking);
    }


    /**
     * Factory method for a {@link FluentAttributeMatcher}
     * matching the <i>actual</i> object's properties.
     *
     * @param klass <i>actual</i> object class
     * @param <T> type of <i>actual</i> object
     * @return FluentAttributeMatcher
     */
    public static <T> FluentAttributeMatcher<T> an(final Class<T> klass) {
        return a(klass);
    }

    /**
     * @return {@code true} if lambda tracking is enabled, {@code false} otherwise.
     */
    @SuppressWarnings("unused")
    public static boolean isTracking() {
        return tracking;
    }



    public static <X, C extends Iterable<X>> FluentIterableMatcher<X, C> anIterableOf(final Class<X> klass){
        return new FluentIterableMatcher<>(klass);
    }

}
