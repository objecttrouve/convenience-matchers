package org.objecttrouve.testing.matchers.fluentits;

class MatchMatrix {

    private final double[][] matchMatrix;

    MatchMatrix(final int nrOfExpectations, final int nrOfActualItems) {
        matchMatrix = new double[nrOfExpectations][nrOfActualItems];
    }

    void match(final int expectationIndex, final int actualItemIndex) {
        matchMatrix[expectationIndex][actualItemIndex] = 1.0;
    }

    void scoredMismatch(final int expectationIndex, final int actualItemIndex, double score) {
        matchMatrix[expectationIndex][actualItemIndex] = score;
    }

    boolean matched(final int expectationIndex, final int actualItemIndex) {
        return matchMatrix[expectationIndex][actualItemIndex] == 1.0;
    }

    double getScore(final int expectationIndex, final int actualItemIndex) {
        return matchMatrix[expectationIndex][actualItemIndex];
    }

}
