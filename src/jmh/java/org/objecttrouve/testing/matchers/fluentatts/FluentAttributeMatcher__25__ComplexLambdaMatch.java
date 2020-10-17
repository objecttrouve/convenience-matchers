/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.CoreMatchers;
import org.objecttrouve.testing.matchers.ConvenientMatchers;
import org.openjdk.jmh.annotations.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentAttributeMatcher__25__ComplexLambdaMatch {


    private static final Attribute<RootThing, String> str = attribute("string", r -> {
        String str1 = r.getTwt().getStr();
        int size = r.getTwtwt().getThingWithStringList().size();
        boolean empty = r.getYat().getThingWithThingsWithString().getThingWithStringList().isEmpty();
        return str1 + size + empty;
    });

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

        List<ThingWithString> getThingWithStringList() {
            return thingWithStringList;
        }
    }

    static class YetAnotherThingWithOtherThings {

        private final ThingWithThingsWithString thingWithThingsWithString;

        YetAnotherThingWithOtherThings(final ThingWithThingsWithString thingWithThingsWithString) {
            this.thingWithThingsWithString = thingWithThingsWithString;
        }

        ThingWithThingsWithString getThingWithThingsWithString() {
            return thingWithThingsWithString;
        }
    }

    public static class RootThing {
        private final YetAnotherThingWithOtherThings yat;
        private final ThingWithThingsWithString twtwt;
        private final ThingWithString twt;

        RootThing(final YetAnotherThingWithOtherThings yat, final ThingWithThingsWithString twtwt, final ThingWithString twt) {
            this.yat = yat;
            this.twtwt = twtwt;
            this.twt = twt;
        }

        YetAnotherThingWithOtherThings getYat() {
            return yat;
        }

        ThingWithString getTwt() {
            return twt;
        }

        ThingWithThingsWithString getTwtwt() {
            return twtwt;
        }
    }

    private String randomString;
    private String expected;
    private RootThing input;

    private RootThing getInput() {
        final ThingWithString twt = new ThingWithString(randomString);
        final ThingWithThingsWithString twtwt = new ThingWithThingsWithString(//
                twt);
        return new RootThing(
                new YetAnotherThingWithOtherThings(
                        twtwt), twtwt, twt);
    }

    @Setup(Level.Trial)
    public synchronized  void setupInput() {
        randomString = UUID.randomUUID().toString();
        input = getInput();
        expected = randomString + "1false";
        checkMatches();
    }


    @Benchmark
    public boolean matcher() {
        return ConvenientMatchers.a(RootThing.class)//
                .with(str,expected)//
                .matches(input);
    }

    @Benchmark
    public boolean control() {
        final String str1 = input.getTwt().getStr();
        final int size = input.getTwtwt().getThingWithStringList().size();
        final boolean empty = input.getYat().getThingWithThingsWithString().getThingWithStringList().isEmpty();
        final String actual = str1 + size + empty;
        return CoreMatchers.is(expected).matches(actual);
    }


    private void checkMatches() {
        assertThat(matcher(), is(true));
        assertThat(control(), is(true));
    }
}
