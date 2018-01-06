/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.CoreMatchers;
import org.objecttrouve.testing.boilerplate.Flatts;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentAttributeMatcher__07__Tracking__MultipleAttributesMatch {

    private enum OneTwoThree {
        one, two, three
    }
    private static class ThingWithAttributes {
        private final String str;
        private final int integer;
        private final boolean bool;
        private final OneTwoThree o23;


        ThingWithAttributes(final String str, final int integer, final boolean bool, final OneTwoThree o23) {
            this.str = str;
            this.integer = integer;
            this.bool = bool;
            this.o23 = o23;
        }

        String getStr() {
            return str;
        }

        int getInteger() {
            return integer;
        }

        OneTwoThree getO23() {
            return o23;
        }

        boolean isBool() {
            return bool;
        }
    }

    @SuppressWarnings("FieldMayBeFinal")
    private ThingWithAttributes input = new ThingWithAttributes("input", 3, true, OneTwoThree.three);

    @Setup(Level.Trial)
    public void checkMatches() {
        assertThat(matcher(), is(true));
        assertThat(control(), is(true));
    }


    @Benchmark
    public boolean matcher() {
        return Flatts.aTracking(ThingWithAttributes.class)//
                .with(ThingWithAttributes::getStr, "input")//
                .with(ThingWithAttributes::getInteger, 3)//
                .with(ThingWithAttributes::getO23, OneTwoThree.three)//
                .with(ThingWithAttributes::isBool, true)//
                .matches(input);
    }

    @Benchmark
    public boolean control() {
        return CoreMatchers.is("input").matches(input.getStr()) //
                && CoreMatchers.is(3).matches(input.getInteger()) //
                && CoreMatchers.is(OneTwoThree.three).matches(input.getO23()) //
                && CoreMatchers.is(true).matches(input.isBool()) //
                ;
    }
}
