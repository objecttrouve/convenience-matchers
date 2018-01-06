/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentatts;

import org.objecttrouve.testing.boilerplate.Flatts;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentAttributeMatcher__21__SimplestMatch {

    private static final Attribute<ThingWithString, String> str = attribute("string", ThingWithString::getStr);

    private static class ThingWithString {
        private final String str;

        ThingWithString(final String str) {
            this.str = str;
        }

        String getStr() {
            return str;
        }
    }

    @SuppressWarnings("FieldMayBeFinal")
    private ThingWithString input = new ThingWithString("input");

    @Setup(Level.Trial)
    public void checkMatches() {
        assertThat(matcher(), is(true));
        assertThat(control(), is(true));
    }

    @Benchmark
    public boolean matcher() {
        return Flatts.aNonTracking(ThingWithString.class)//
                .with(str, "input")//
                .matches(input);
    }

    @Benchmark
    public boolean control() {
        return is("input").matches(input.getStr());
    }
}
