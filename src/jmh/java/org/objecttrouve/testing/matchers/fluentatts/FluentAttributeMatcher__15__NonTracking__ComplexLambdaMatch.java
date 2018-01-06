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
public class FluentAttributeMatcher__15__NonTracking__ComplexLambdaMatch {

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
        private final ThingWithThingsWithString twtwt;
        private final ThingWithString twt;

        public RootThing(final YetAnotherThingWithOtherThings yat, final ThingWithThingsWithString twtwt, final ThingWithString twt) {
            this.yat = yat;
            this.twtwt = twtwt;
            this.twt = twt;
        }

        public YetAnotherThingWithOtherThings getYat() {
            return yat;
        }

        ThingWithString getTwt() {
            return twt;
        }

        ThingWithThingsWithString getTwtwt() {
            return twtwt;
        }
    }

    @SuppressWarnings("FieldMayBeFinal")
    private RootThing input = getInput();

    private RootThing getInput() {
        final ThingWithString twt = new ThingWithString("input");
        final ThingWithThingsWithString twtwt = new ThingWithThingsWithString(//
                twt);
        return new RootThing(
                new YetAnotherThingWithOtherThings(
                        twtwt), twtwt, twt);
    }

    @Setup(Level.Trial)
    public void checkMatches() {
        assertThat(matcher(), is(true));
        assertThat(control(), is(true));
    }


    @Benchmark
    public boolean matcher() {
        return Flatts.aNonTracking(RootThing.class)//
                .with(r -> {
                            String str1 = r.getTwt().getStr();
                            int size = r.getTwtwt().getThingWithStringList().size();
                            boolean empty = r.getYat().getThingWithThingsWithString().getThingWithStringList().isEmpty();
                            return str1 + size + empty;
                        }, //
                        "input1false"//
                )//
                .matches(input);
    }

    @Benchmark
    public boolean control() {
        final String str1 = input.getTwt().getStr();
        final int size = input.getTwtwt().getThingWithStringList().size();
        final boolean empty = input.getYat().getThingWithThingsWithString().getThingWithStringList().isEmpty();
        final String actual = str1 + size + empty;
        return CoreMatchers.is("input1false").matches(actual);
    }
}
