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

import java.util.LinkedList;
import java.util.List;
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
public class FluentAttributeMatcher__24__LongPathFailAndDescribe {

    private static final Attribute<RootThing, String> str = attribute("string", r -> r//
        .getYat()//
        .getThingWithThingsWithString()//
        .getThingWithStringList()//
        .iterator()//
        .next()//
        .getStr());

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
    public void checkFails() {
        assertThat(matcher(), not(is("")));
        assertThat(control(), not(is(matchAndDescribe(is("input"), "input"))));
    }

    @Benchmark
    public Description matcher() {
        final FluentAttributeMatcher<RootThing> matcher = Flatts.aNonTracking(RootThing.class)//
                .with(str, "putt"//
                );
        return matchAndDescribe(matcher, input);
    }

    @Benchmark
    public Description control() {
        final Matcher<String> matcher = CoreMatchers.is("putt");
        return matchAndDescribe(matcher,//
                this.input //
                        .getYat()//
                        .getThingWithThingsWithString()//
                        .getThingWithStringList()//
                        .iterator()//
                        .next()//
                        .getStr() //
        );
    }
}
