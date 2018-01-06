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
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentAttributeMatcher__29__CrossAnArrayMatch {


    private static final Attribute<ThingWithThingWithStringArray, String> str = attribute("string", twa -> twa.getArray()[1].getStr());

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

        ThingWithString[] getArray() {
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
        return Flatts.aNonTracking(ThingWithThingWithStringArray.class)//
                .with(str, "2")//
                .matches(input);
    }

    @Benchmark
    public boolean control() {
        return CoreMatchers.is("2").matches(input.getArray()[1].getStr());
    }
}
