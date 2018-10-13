/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.boilerplate;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class Boilerplate {
    public static Description matchAndDescribe(final Matcher<?> matcher, final Object input) {
        final boolean matches = matcher.matches(input);
        if (matches){
            throw new IllegalArgumentException("Matcher matches but is expected not to!");
        }
        final StringDescription description = new StringDescription();
        matcher.describeTo(description);
        return description;
    }
}
