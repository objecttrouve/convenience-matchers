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
import org.hamcrest.StringDescription;
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
public class FluentAttributeMatcher__08__Tracking__MultipleAttributesFailAndDescribe {

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

        public int getInteger() {
            return integer;
        }

        public OneTwoThree getO23() {
            return o23;
        }

        public boolean isBool() {
            return bool;
        }
    }

    @SuppressWarnings("FieldMayBeFinal")
    private ThingWithAttributes input = new ThingWithAttributes("input", 3, true, OneTwoThree.three);

    @Setup(Level.Trial)
    public void checkFails() {
        assertThat(matcher(), not(is("")));
        /* Just believe the control logic is correct.*/
    }

    @Benchmark
    public Description matcher() {
        final FluentAttributeMatcher<ThingWithAttributes> matcher = Flatts.aTracking(ThingWithAttributes.class)//
                .with(ThingWithAttributes::getStr, "putt")//
                .with(ThingWithAttributes::getInteger, 2)//
                .with(ThingWithAttributes::getO23, OneTwoThree.two)//
                .with(ThingWithAttributes::isBool, false);
        return matchAndDescribe(matcher, input);
    }

    @Benchmark
    public StringDescription control() {
        final Matcher<String> m1 = CoreMatchers.is("putt");
        final Matcher<Integer> m2 = CoreMatchers.is(2);
        final Matcher<OneTwoThree> m3 = CoreMatchers.is(OneTwoThree.two);
        final Matcher<Boolean> m4 = CoreMatchers.is(false);
        m1.matches(this.input.getStr());
        m2.matches(this.input.getInteger());
        m3.matches(this.input.getO23());
        m4.matches(this.input.isBool());
        final StringDescription d = new StringDescription();
        m1.describeTo(d);
        m2.describeTo(d);
        m3.describeTo(d);
        m4.describeTo(d);
        return d;
    }
}
