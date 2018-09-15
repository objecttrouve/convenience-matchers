/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.api;

import org.hamcrest.Matcher;

public interface ScorableMatcher {


    /**
     * A score for (partial) mismatches to find the best mismatch.
     * The score is only meaningful for matchers that didn't match.
     * Always check the return value of {@link Matcher#matches(java.lang.Object)}
     * for being false before interpreting the score.
     * A score of 0 means the matcher didn't match anything.
     *
     * @return Value between 0 and 1.
     */
    double getScore();


}
