package org.objecttrouve.testing.matchers.fluentits;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MatchMatrixTest {

    @Test
    public void test__MatchMatrix__match__matched(){

        final MatchMatrix matchMatrix = new MatchMatrix(2, 2);

        matchMatrix.match(0, 1);

        assertTrue(matchMatrix.matched(0,1));
        assertFalse(matchMatrix.matched(0,0));
        assertFalse(matchMatrix.matched(1,0));
        assertFalse(matchMatrix.matched(1,1));
    }

    @Test
    public void test__MatchMatrix__match__getScore(){

        final MatchMatrix matchMatrix = new MatchMatrix(2, 2);

        matchMatrix.match(0, 1);

        assertThat(matchMatrix.getScore(0,1), is(1.0));
        assertThat(matchMatrix.getScore(0,0), is(0.0));
        assertThat(matchMatrix.getScore(1,0), is(0.0));
        assertThat(matchMatrix.getScore(1,1), is(0.0));
    }

    @Test
    public void test__MatchMatrix__scoredMismatch__getScore(){

        final MatchMatrix matchMatrix = new MatchMatrix(2, 2);

        matchMatrix.scoredMismatch(0, 1, 0.5);

        assertThat(matchMatrix.getScore(0,1), is(0.5));
        assertThat(matchMatrix.getScore(0,0), is(0.0));
        assertThat(matchMatrix.getScore(1,0), is(0.0));
        assertThat(matchMatrix.getScore(1,1), is(0.0));
    }

    @Test
    public void test__MatchMatrix__scoredMismatch_match_getScore(){

        final MatchMatrix matchMatrix = new MatchMatrix(2, 2);

        matchMatrix.scoredMismatch(0, 1, 0.5);
        matchMatrix.match(0, 0);

        assertThat(matchMatrix.getScore(0,1), is(0.5));
        assertThat(matchMatrix.getScore(0,0), is(1.0));
        assertThat(matchMatrix.getScore(1,0), is(0.0));
        assertThat(matchMatrix.getScore(1,1), is(0.0));
    }


}