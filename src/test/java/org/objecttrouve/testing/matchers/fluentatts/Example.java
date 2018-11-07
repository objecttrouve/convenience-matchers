/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

// Use the customization method for syntactic sugaring and configurability!


@SuppressWarnings("ALL")
@Ignore("Failing intentionally.")
public class Example {

    static class Result {
        private final String stringValue;
        private final int intValue;
        private final boolean boolValue;

        Result(final String stringValue, final int intValue, final boolean boolValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
            this.boolValue = boolValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public boolean isBoolValue() {
            return boolValue;
        }
    }

    static Result methodWithResult(final String s, final int i, final boolean b){
        return new Result(s, i, b);
    }


    private static final Attribute<Result, Integer> intValue = attribute("intValue", Result::getIntValue);
    private static final Attribute<Result, Boolean> boolValue = attribute("booleanValue", Result::isBoolValue);
    private static final Attribute<String, String> substring = attribute("substring", s -> s.substring(0, 1));
    private static final Attribute<String, Integer> length = attribute("length", String::length);
    private static final Attribute<Result, String> stringValue = attribute("stringValue", Result::getStringValue);

    @Test
    public void test(){

        final Result result = methodWithResult("result", 1, false);

        assertThat(result, is(//
            a(Result.class)//
                .with(stringValue, a(String.class)
                    .with(length, 3)
                    .with(substring, a(String.class)
                        .with(length, 10))) //
                .with(intValue, lessThan(0)) //
                .with(boolValue, true)
        ));
    }

    @Test
    public void testSomething(){

        final Result result = methodWithResult("2=", 1, false);

        assertThat(result, is(//
                a(Result.class)//
                .with(stringValue, "1=") //
                .with(intValue, is(2)) //
                .with(boolValue, true)
        ));
    }




}
