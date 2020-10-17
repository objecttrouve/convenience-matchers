/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.boilerplate;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class Boilerplate {
    public static String matchAndDescribe(final Matcher<?> matcher, final Object input) {
        final boolean matches = matcher.matches(input);
        if (matches){
            throw new IllegalArgumentException("Matcher matches but is expected not to!");
        }
        final StringDescription description = new StringDescription();
        matcher.describeTo(description);
        final StringDescription mmDescription = new StringDescription();
        matcher.describeMismatch(input, mmDescription);
        return description.toString() + mmDescription.toString() ;
    }
}
