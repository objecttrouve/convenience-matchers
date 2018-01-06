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
public class FluentAttributeMatcher__26__ComplexLambdaFailAndDescribe {

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
    public void checkFails() {
        assertThat(matcher(), not(is("")));
        assertThat(control(), not(is(matchAndDescribe(is("input1false"), "input1false"))));
    }

    @Benchmark
    public Description matcher() {
        final FluentAttributeMatcher<RootThing> matcher = Flatts.aNonTracking(RootThing.class)//
                .with(str,"output2true"//
                );
        return matchAndDescribe(matcher, input);
    }

    @Benchmark
    public Description control() {
        final String str1 = input.getTwt().getStr();
        final int size = input.getTwtwt().getThingWithStringList().size();
        final boolean empty = input.getYat().getThingWithThingsWithString().getThingWithStringList().isEmpty();
        final String actual = str1 + size + empty;
        final Matcher<String> matcher = CoreMatchers.is("output2true");
        return matchAndDescribe(matcher, actual);
    }
}
