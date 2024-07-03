package org.objecttrouve.testing.matchers.fluentits;

class MatchMatrix {

    private final double[][] matchMatrix;
    private final int nrOfExpectations;
    private final int nrOfActualItems;

    MatchMatrix(final int nrOfExpectations, final int nrOfActualItems) {
        this.nrOfExpectations = nrOfExpectations;
        this.nrOfActualItems = nrOfActualItems;
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

    boolean isOneToOne() {
        final double[][] oneToOneMatrix = new double[nrOfExpectations][nrOfActualItems];
        // Calculate an "exclusiveness" score for each match...
        for (int i = 0; i < nrOfExpectations; i++) {
            double exclusiveness = getExclusiveness(i);
            for (int j = 0; j < nrOfActualItems; j++) {
                if (matched(i, j)) {
                    oneToOneMatrix[i][j] = exclusiveness;
                }
            }
        }
        // Check if all actual items have enough matchers that match (non-exclusively...)
        for (int j = 0; j < nrOfActualItems; j++) {
            double matcherCoverage = 0.0;
            for (int i = 0; i < nrOfExpectations; i++) {
                matcherCoverage += oneToOneMatrix[i][j];
            }
            // TODO: Double comparison
            if (matcherCoverage < 1.0){
                return false;
            }
        }
        return true;


    }

    private double getExclusiveness(int expectationIndex) {
        int itemsMatchedByCurrentMatcher = 0;
        for (int actualItemIndex = 0; actualItemIndex < nrOfActualItems; actualItemIndex++) {
            if (matched(expectationIndex, actualItemIndex)) {
                itemsMatchedByCurrentMatcher ++;
            }
        }
        return 1.0d/itemsMatchedByCurrentMatcher;
    }

}
