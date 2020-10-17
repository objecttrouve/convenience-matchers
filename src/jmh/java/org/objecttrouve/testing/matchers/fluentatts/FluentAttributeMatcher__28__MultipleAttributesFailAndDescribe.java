/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.objecttrouve.testing.boilerplate.Flatts;
import org.openjdk.jmh.annotations.*;

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
public class FluentAttributeMatcher__28__MultipleAttributesFailAndDescribe {

    private static final Attribute<ThingWithAttributes, String> strVal = attribute("string", ThingWithAttributes::getStr);
    private static final Attribute<ThingWithAttributes, Integer> intVal = attribute("int", ThingWithAttributes::getInteger);
    private static final Attribute<ThingWithAttributes, OneTwoThree> o23Val = attribute("int", ThingWithAttributes::getO23);
    private static final Attribute<ThingWithAttributes, Boolean> boolVal = attribute("int", ThingWithAttributes::isBool);

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
    public void checkFails() {
        assertThat(matcher(), not(is("")));
        assertThat(control(), not(is("")));
    }

    @Benchmark
    public String matcher() {
        final FluentAttributeMatcher<ThingWithAttributes> matcher = Flatts.aNonTracking(ThingWithAttributes.class)//
                .with(strVal, "putt")//
                .with(intVal, 2)//
                .with(o23Val, OneTwoThree.two)//
                .with(boolVal, false);
        return matchAndDescribe(matcher, input);
    }

    @Benchmark
    public String control() {
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
        return d.toString();
    }
}
