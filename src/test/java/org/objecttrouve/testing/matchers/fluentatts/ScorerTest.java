/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.junit.Test;

import java.util.List;

import static java.lang.Double.compare;
import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.objecttrouve.testing.matchers.fluentatts.Scorer.score;

public class ScorerTest {

    @Test
    public void score__0__0() {

        final double score = score(0, 0);

        assertThat(score, is(1.0));
    }

    @Test
    public void score__0__1() {

        final double score = score(0, 1);

        assertThat(score, is(0.0));
    }

    @Test
    public void score__1__1() {

        final double score = score(1, 1);

        assertThat(score, is(1.0));
    }


    @Test
    public void score__3__3() {

        final double score = score(3, 3);

        assertThat(score, is(1.0));
    }


    @Test
    public void score__1__3() {

        final double score = score(1, 3);

        assertThat(score, closeTo(0.333, 0.01));
    }

    @Test
    public void score__73__100() {

        final double score = score(73, 100);

        assertThat(score, closeTo(0.73, 0.001));
    }

    @Test
    public void score__100__100() {

        final double score = score(100, 100);

        assertThat(score, closeTo(1.0, 0.001));
    }

    private Parameters p(final int matched, final int checked) {
        return new Parameters(matched, checked);
    }

    private static class Parameters {
        private final int matched;
        private final int checked;

        private Parameters(final int matched, final int checked) {
            this.matched = matched;
            this.checked = checked;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Parameters that = (Parameters) o;

            return matched == that.matched && checked == that.checked;
        }

        @Override
        public int hashCode() {
            int result = matched;
            result = 31 * result + checked;
            return result;
        }

        @Override
        public String toString() {
            return "\np{" +
                "matched=" + matched +
                ", checked=" + checked +
                '}';
        }

        private double score() {
            return Scorer.score(matched, checked);
        }
    }

    @Test
    public void score__sort_by_score(){

        final List<Parameters> input = asList(
            p(1,1),
            p(5,6),
            p(1,9),
            p(13,14),
            p(2,3),
            p(56,100),
            p(5,5),
            p(0,7),
            p(6,6),
            p(0,8)
        );

        //noinspection Java8ListSort
        sort(input, (p1, p2) -> compare(p2.score(), p1.score()));

        final List<Parameters> expected = asList(
            p(1,1),
            p(5,5),
            p(6,6),
            p(13,14),
            p(5,6),
            p(2,3),
            p(56,100),
            p(1,9),
            p(0,7),
            p(0,8)
        );
        assertThat(input, is(expected));
    }

}
