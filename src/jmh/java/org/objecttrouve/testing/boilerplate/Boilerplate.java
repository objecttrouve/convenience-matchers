/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.boilerplate;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class Boilerplate {
    public static Description matchAndDescribe(final Matcher<?> matcher, final Object input) {
        matcher.matches(input);
        final StringDescription description = new StringDescription();
        matcher.describeTo(description);
        return description;
    }
}
