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

class EqTo<T> extends IsEqual<T> {
    private final T equalArg;
    private final Stringifiers stringifiers;

    EqTo(final T equalArg, final Stringifiers stringifiers) {
        super(equalArg);
        this.equalArg = equalArg;
        this.stringifiers = stringifiers;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("<"+stringifiers.getShortStringifier(equalArg).orElse(Objects::toString).apply(equalArg)+">");
    }
}
