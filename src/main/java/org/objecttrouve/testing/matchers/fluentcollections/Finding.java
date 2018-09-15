/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentcollections;

class Finding {

    private final String description;

    Finding(final String description) {
        this.description = description;
    }

    String getDescription(){
        return description;
    }
}
