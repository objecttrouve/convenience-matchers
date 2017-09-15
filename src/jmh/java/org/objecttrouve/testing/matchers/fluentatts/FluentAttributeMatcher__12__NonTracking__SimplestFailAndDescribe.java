/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.objecttrouve.testing.boilerplate.Flatts;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.objecttrouve.testing.boilerplate.Boilerplate.matchAndDescribe;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentAttributeMatcher__12__NonTracking__SimplestFailAndDescribe {

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
        assertThat(control(), not(is(matchAndDescribe(is("input"), "input"))));
    }


    @Benchmark
    public Description matcher() {

        final FluentAttributeMatcher<ThingWithString> matcher = Flatts.aNonTracking(ThingWithString.class)//
                .with(ThingWithString::getStr, "putt");

        return matchAndDescribe(matcher, this.input);
    }

    @Benchmark
    public Description control() {
        final Matcher<String> matcher = CoreMatchers.is("putt");

        return matchAndDescribe(matcher, this.input.getStr());
    }

}
