/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;


class Scorer {

    static double score(final int matchedProps, final int checkedProps) {

        if (checkedProps == 0){
            return 1;
        }
        return (double)matchedProps/(double)checkedProps;
    }


}
