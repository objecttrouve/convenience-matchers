/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.api;

import java.util.Optional;
import java.util.function.Function;

/**
 * <p>
 *     Mappings of classes to stringifying <code>Function</code>s.
 * </p>
 * <p>
 *     Not all classes have a useful <code>toString()</code> implementation.
 *     Some have none at all.
 *     <br>
 *     <code>Stringifiers</code> compensate for such poor or missing <code>toString</code>-methods.
 * </p>
 * <p>
 *    Stringifier functions are applied to objects of a particular <code>Class</code>.
 *    They create a suitable <code>String</code> representation of every instance of that class.
 *    <br>
 *    They come in two flavours:
 * </p>
 * <ul>
 *     <li>"short" for a brief object description</li>
 *     <li>"debug" for a detailed object description in debug mode</li>
 * </ul>
 *<p>
 *     There's usually (but not necessarily) a fallback cascade:
 *</p>
 * <ul>
 *     <li>debug (with debug mode)</li>
 *     <li>short</li>
 *     <li><code>Objects#toString()</code></li>
 * </ul>
 * <p>
 *     The fallback strategy is based on the assumption that dedicated stringifiers are <i>always</i>
 *     more useful than <code>toString</code>-methods.
 * </p>
 */
public interface Stringifiers {

    /**
     * <p>
     *     Gets a condensing stringifier <Code>Function</Code> for the input <code>object</code>,
     *     wrapped in an <code>Optional</code> for being nullable.
     * </p>
     * <p>
     *     The returned function provides a <b>short</b> description of the input object.
     * </p>
     * @param <X> object type
     * @param object the instance to stringify
     * @return <code>Optional</code> stringifier function for input <code>object</code>
     */
    <X> Optional<Function<X, String>> getShortStringifier(final X object);

    /**
     * <p>
     *     Gets a verbose stringifier <Code>Function</Code> for the input <code>object</code>,
     *     wrapped in an <code>Optional</code> for being nullable.
     * </p>
     * <p>
     *     The returned function provides a <b>detailed</b> description of the input object.
     * </p>
     * @param <X> object type
     * @param object the instance to stringify
     * @return <code>Optional</code> stringifier function for input <code>object</code>
     */
    <X> Optional<Function<X, String>> getDebugStringifier(final X object);
}
