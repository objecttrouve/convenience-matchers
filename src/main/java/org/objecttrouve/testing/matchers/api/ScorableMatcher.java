/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.api;

/**
 * <p>Provides a measure for the extent to which a {@code Matcher} matched.</p>
 * <p>Some {@code Matcher}s check more than one facet of an <i>actual</i> {@code Object}.
 * If one of these aspects is not as expected, there's a mismatch.
 * If multiple such {@code Matcher}s are applied to the same {@code Object},
 * some may fit better than others.</p>
 * <p>Thus, if a {@code Matcher} implements the {@code ScorableMatcher} interface,
 * it can be ranked among other {@code Matcher}s.</p>
 * <p>This way, you can identify those {@code Matcher}s that come closest to the actual {@code Object}. </p>
 */
public interface ScorableMatcher {

    /**
     * <p>A score for (partial) mismatches to measure the matching part.</p>
     * <p>The score is only meaningful for matchers that didn't match.
     * So, always check the return value of {@code Matcher#matches(java.lang.Object)}
     * for being {@code false} before interpreting the score.</p>
     * <p>A score of 0 means the matcher didn't match anything.</p>
     *
     * @return Value between 0 and 1.
     */
    double getScore();


}
