/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import org.hamcrest.Description;
import org.hamcrest.core.IsEqual;
import org.objecttrouve.testing.matchers.api.Stringifiers;

import java.util.Objects;
import java.util.function.Function;

class EqTo<T> extends IsEqual<T> {
    private final T equalArg;
    private final Stringifiers stringifiers;
    private final boolean debugging;

    EqTo(final T equalArg, final Stringifiers stringifiers, final boolean debugging) {
        super(equalArg);
        this.equalArg = equalArg;
        this.stringifiers = stringifiers;
        this.debugging = debugging;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("<" + getStringifier(equalArg).apply(equalArg) + ">");
    }

    @Override
    public void describeMismatch(final Object item, final Description description) {
        final String expDescription = getStringifier(equalArg).apply(equalArg);
        final String actualDescription = getStringifier(item).apply(item);
        description.appendText("<" + expDescription + "> was <" + actualDescription + ">");
    }

    private Function<Object, String> getStringifier(final Object o) {
        return debugging ? getDebugStringifier(o) : getShortStringifier(o);
    }

    private Function<Object, String> getDebugStringifier(final Object item) {
        return stringifiers.getDebugStringifier(item)
            .orElseGet(() -> getShortStringifier(item));
    }

    private Function<Object, String> getShortStringifier(final Object item) {
        return stringifiers.getShortStringifier(item)
            .orElse(Objects::toString);
    }
}
