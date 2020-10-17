/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.api;

/**
 * <p>
 *    Configuration settings for convenient matcher instances.
 * </p>
 * <p>
 *    Convenient matchers have configurable behavior:
 * </p>
 * <ul>
 *    <li>unicode characters used to express particular mismatch characteristics</li>
 *    <li>printing of objects involved</li>
 *    <li>debug mode / additonal verbose output</li>
 * </ul>
 * <p>
 *    The <code>Config</code> is the single source of truth for all such settings.
 * </p>
 */
public interface Config {

    /**
     * Gets the <code>Set</code> of <code>Symbols</code> to be used in error descriptions.
     *
     * @return <code>Set</code> of <code>Symbols</code> for nice error descriptions
     */
    Symbols getSymbols();

    /**
     * Gets the <code>Stringifiers</code> to pretty-print objects.
     *
     * @return <code>Stringifiers</code> to pretty-print objects
     */
    Stringifiers getStringifiers();

    /**
     * Gets the debug mode flag.
     *
     * @return <code>true</code> when debug output enabled
     */
    boolean isInDebugMode();
}
