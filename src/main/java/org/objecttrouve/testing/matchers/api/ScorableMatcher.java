/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.api;

/**
 * <p>
 *     Provides a measure for the extent to which an implementing matcher matched.
 * </p>
 * <p>
 *     Matchers may evaluate more than one facet of an object.
 *     (The <i>actual</i> <code>Object</code>.)
 *     <br>
 *     If one of these aspects is not as expected, there's a mismatch.
 *     <br>
 *     If multiple such matchers are applied to the same actual object,
 *      some may fit better than others.
 * </p>
 * <p>
 *     If a matcher implements the <code>ScorableMatcher</code> interface,
 *     it can be ranked among other <code>ScorableMatcher</code>s.
 *     <br>
 *     (And other mismatching matchers assuming a hypothetical lowest score for these.)
 * </p>
 * <p>
 *     The matcher with the highest score fits the actual object best.
 * </p>
 */
public interface ScorableMatcher {

    /**
     * <p>
     *     Gets a score indicating the matching fraction of a (partial) mismatch.
     * </p>
     * <p>
     *     The score is only meaningful for matchers that <b>didn't</b> match.
     *     <br>
     *     So, always check the return value of {@code Matcher#matches()}
     *     for being {@code false} before interpreting the score.
     * </p>
     * <p>A score of 0 means the matcher didn't match anything.</p>
     *
     * @return value between 0 and 1
     */
    double getScore();


}
