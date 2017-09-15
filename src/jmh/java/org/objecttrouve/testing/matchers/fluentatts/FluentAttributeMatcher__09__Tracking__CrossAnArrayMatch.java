/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.CoreMatchers;
import org.objecttrouve.testing.boilerplate.Flatts;
import org.objecttrouve.testing.matchers.ConvenientMatchers;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentAttributeMatcher__09__Tracking__CrossAnArrayMatch {

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
    public void checkMatches() {
        assertThat(matcher(), is(true));
        assertThat(control(), is(true));
    }


    @Benchmark
    public boolean matcher() {
        return Flatts.aTracking(ThingWithThingWithStringArray.class)//
                .with(twa -> twa.getArray()[1].getStr(), "2")//
                .matches(input);
    }

    @Benchmark
    public boolean control() {
        return CoreMatchers.is("2").matches(input.getArray()[1].getStr());
    }
}
