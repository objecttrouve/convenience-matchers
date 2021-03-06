/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.objecttrouve.testing.matchers.customization.StringifiersConfig;

import java.util.function.Function;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.objecttrouve.testing.matchers.customization.StringifiersConfig.stringifiers;

public class EqToTest {


    private static class It {
        private final String it;

        private It(final String it) {
            this.it = it;
        }
    }

    @Test
    public void describeTo__withShortStringifier() {
        final Function<It, String> strg = it -> it.it;
        final StringifiersConfig.Builder stringifiers = stringifiers()
            .withShortStringifier(It.class, strg);
        final EqTo<It> eqTo = new EqTo<>(new It("girl"), stringifiers.build(), false);
        final StringDescription description = new StringDescription();

        eqTo.describeTo(description);

        assertThat(description.toString(), is("<girl>"));
    }

    @Test
    public void describeTo__without_stringifier() {
        final StringifiersConfig.Builder stringifiers = stringifiers();
        final EqTo<It> eqTo = new EqTo<>(new It("girl"), stringifiers.build(), false);
        final StringDescription description = new StringDescription();

        eqTo.describeTo(description);

        assertThat(description.toString(), startsWith("<org.objecttrouve.testing.matchers.fluentits.EqToTest$It@"));
        assertThat(description.toString(), endsWith(">"));
    }

    @Test
    public void describeTo__withDebugStringifier__debugging() {
        final Function<It, String> strg = it -> it.it;
        final StringifiersConfig.Builder stringifiers = stringifiers()
            .withDebugStringifier(It.class, strg);
        final EqTo<It> eqTo = new EqTo<>(new It("girl"), stringifiers.build(), true);
        final StringDescription description = new StringDescription();

        eqTo.describeTo(description);

        assertThat(description.toString(), is("<girl>"));
    }
}