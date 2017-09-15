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
public class FluentAttributeMatcher__20__NonTracking__CrossAnArrayFailAndDescribe {

    private static class ThingWithString {
        private final String str;

        ThingWithString(final String str) {
            this.str = str;
        }

        String getStr() {
            return str;
        }
    }

    private static class ThingWithThingWithStringArray {
        private final ThingWithString[] array;

        private ThingWithThingWithStringArray(final ThingWithString... array) {
            this.array = array;
        }

        public ThingWithString[] getArray() {
            return array;
        }
    }


    @SuppressWarnings("FieldMayBeFinal")
    private ThingWithThingWithStringArray input = new ThingWithThingWithStringArray(//
            new ThingWithString("1"),//
            new ThingWithString("2"),//
            new ThingWithString("3")//
    );

    @Setup(Level.Trial)
    public void checkFails() {
        assertThat(matcher(), not(is("")));
        assertThat(control(), not(is(matchAndDescribe(is("3"), "3"))));
    }

    @Benchmark
    public Description matcher() {
        final FluentAttributeMatcher<ThingWithThingWithStringArray> matcher = Flatts.aNonTracking(ThingWithThingWithStringArray.class)//
                .with(twa -> twa.getArray()[1].getStr(), "3");
        return matchAndDescribe(matcher, input);
    }

    @Benchmark
    public Description control() {
        final Matcher<String> matcher = CoreMatchers.is("3");
        return matchAndDescribe(matcher, input.getArray()[1].getStr());
    }
}
