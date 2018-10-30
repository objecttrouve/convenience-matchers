/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.CoreMatchers;
import org.objecttrouve.testing.boilerplate.Flatts;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentAttributeMatcher__27__MultipleAttributesMatch {

    private static final Random random = new Random();
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

    private String randomString;
    private int randomInt;
    private boolean randomBool;
    private OneTwoThree randomOTT;
    private ThingWithAttributes input;

    @Setup(Level.Trial)
    public synchronized void setupInput() {
        randomString = UUID.randomUUID().toString();
        randomInt = random.nextInt();
        randomBool = randomInt%2>0;
        final int ord = random.nextInt(3);
        randomOTT = Arrays.stream(OneTwoThree.values()).filter(ott -> ott.ordinal() == ord).findAny().orElse(OneTwoThree.one);
        input = new ThingWithAttributes(
            randomString,
            randomInt,
            randomBool,
            randomOTT);
        checkMatches();
    }

    @Benchmark
    public boolean matcher() {
        return Flatts.aNonTracking(ThingWithAttributes.class)//
                .with(strVal, randomString)//
                .with(intVal, randomInt)//
                .with(o23Val, randomOTT)//
                .with(boolVal, randomBool)//
                .matches(input);
    }

    @Benchmark
    public boolean control() {
        return CoreMatchers.is(randomString).matches(input.getStr()) //
                && CoreMatchers.is(randomInt).matches(input.getInteger()) //
                && CoreMatchers.is(randomOTT).matches(input.getO23()) //
                && CoreMatchers.is(randomBool).matches(input.isBool()) //
                ;
    }

    private void checkMatches() {
        assertThat(matcher(), is(true));
        assertThat(control(), is(true));
    }
}
