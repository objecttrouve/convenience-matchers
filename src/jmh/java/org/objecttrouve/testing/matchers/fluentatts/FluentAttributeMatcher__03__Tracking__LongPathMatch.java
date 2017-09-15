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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentAttributeMatcher__03__Tracking__LongPathMatch {

    private static class ThingWithString {
        private final String str;

        ThingWithString(final String str) {
            this.str = str;
        }

        String getStr() {
            return str;
        }
    }

    private static class ThingWithThingsWithString {
        private final List<ThingWithString> thingWithStringList;

        ThingWithThingsWithString(final ThingWithString thingWithString) {
            thingWithStringList = new LinkedList<>();
            thingWithStringList.add(thingWithString);
        }

        public List<ThingWithString> getThingWithStringList() {
            return thingWithStringList;
        }
    }

    public static class YetAnotherThingWithOtherThings {

        private final ThingWithThingsWithString thingWithThingsWithString;

        public YetAnotherThingWithOtherThings(final ThingWithThingsWithString thingWithThingsWithString) {
            this.thingWithThingsWithString = thingWithThingsWithString;
        }

        public ThingWithThingsWithString getThingWithThingsWithString() {
            return thingWithThingsWithString;
        }
    }

    public static class RootThing {
        private final YetAnotherThingWithOtherThings yat;

        public RootThing(final YetAnotherThingWithOtherThings yat) {
            this.yat = yat;
        }

        public YetAnotherThingWithOtherThings getYat() {
            return yat;
        }
    }

    @SuppressWarnings("FieldMayBeFinal")
    private RootThing input = new RootThing(//
            new YetAnotherThingWithOtherThings(//
                    new ThingWithThingsWithString(//
                            new ThingWithString("input"))));

    @Setup(Level.Trial)
    public void checkMatches() {
        assertThat(matcher(), is(true));
        assertThat(control(), is(true));
    }


    @Benchmark
    public boolean matcher() {
        return Flatts.aTracking(RootThing.class)//
                .with(r -> r//
                                .getYat()//
                                .getThingWithThingsWithString()//
                                .getThingWithStringList()//
                                .iterator()//
                                .next()//
                                .getStr(), //
                        "input"//
                )//
                .matches(input);
    }

    @Benchmark
    public boolean control() {
        return CoreMatchers.is("input").matches(//
                input //
                        .getYat()//
                        .getThingWithThingsWithString()//
                        .getThingWithStringList()//
                        .iterator()//
                        .next()//
                        .getStr() //
        );
    }
}
