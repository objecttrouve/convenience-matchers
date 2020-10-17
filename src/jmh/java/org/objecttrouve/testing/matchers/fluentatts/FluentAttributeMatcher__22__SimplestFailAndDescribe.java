/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.objecttrouve.testing.boilerplate.Flatts;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.objecttrouve.testing.boilerplate.Boilerplate.matchAndDescribe;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentAttributeMatcher__22__SimplestFailAndDescribe {

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
    public void checkFails() {
        assertThat(matcher(), not(is("")));
        assertThat(control(), not(is("")));
    }


    @Benchmark
    public String matcher() {

        final FluentAttributeMatcher<ThingWithString> matcher = Flatts.aNonTracking(ThingWithString.class)//
                .with(str, "putt");

        return matchAndDescribe(matcher, this.input);
    }

    @Benchmark
    public String control() {
        final Matcher<String> matcher = CoreMatchers.is("putt");
        return matchAndDescribe(matcher, this.input.getStr());
    }

}
