/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
// Use the factory method for syntactic sugaring and configurability!
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;

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

    @Test
    public void testSomething(){

        final Result result = methodWithResult("2=", 1, false);

        // Failing intentionally.
        //noinspection Convert2MethodRef
        assertThat(result, is(//
                a(Result.class)//
                .with(Result::getStringValue, "1=") //
                .having(Result::getIntValue, is(2)) //
                .with(r -> r.isBoolValue(), true)
        ));
    }


}
