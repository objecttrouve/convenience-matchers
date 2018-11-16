/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.api;

import org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher;
import org.objecttrouve.testing.matchers.fluentits.FluentIterableMatcher;

/**
 * Configuration settings for  {@link FluentAttributeMatcher} and {@link FluentIterableMatcher} instances. */
public interface Config {

    /**
     * @return Set of {@code Symbols} to be used in error descriptions.
     */
    Symbols getSymbols();

    /**
     * @return {@code Stringifiers} to pretty-print actual instances.
     */
    Stringifiers getStringifiers();

    /**
     * @return {@code true} if matchers should run in debug mode.
     */
    boolean isInDebugMode();
}
