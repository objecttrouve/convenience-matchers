Convenience Matchers
====================

Convenience library with [Hamcrest](http://hamcrest.org/JavaHamcrest/) Matchers for comfortable unit testing.

`FluentAttributeMatcher`
------------------------
A [`TypeSafeMatcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/TypeSafeMatcher.html) implementation to check multiple target object attributes at once. 
Offers a fluent builder API in which the attributes of the target object can be referred to via lambda expressions. 

### Basic Concept 

Best illustrated with an example. 

Example:

```java
package org.objecttrouve.testing.matchers.fluentatts;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
// Use the factory method for syntactic sugaring and configurability!
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;

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
```

The idea is to pass a lambda expression or method reference that is getting an attribute value from the target object along with the expected value. 
Alternatively, it is possible to pass a `Matcher` instead of an expected object. 
Moreover, the `FluentAttributeMatcher` implements the builder pattern so that it can be set up in a fluent style.  

Use 
* `FluentAttributeMatcher.with` to pass an expected `Object` and
* `FluentAttributeMatcher.having` to pass a `Matcher` for the expected object. 

### Run It

Running the `Example` yields the following output: 

```
> ./gradlew -Dorg.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher.tracking=true test --tests org.objecttrouve.testing.matchers.fluentatts.Example --info

org.objecttrouve.testing.matchers.fluentatts.Example > testSomething FAILED
    java.lang.AssertionError: 
    Expected: is 
        org.objecttrouve.testing.matchers.fluentatts.Example$$Lambda$1/872982730@23d168a2 = "1=" <> "2="
        org.objecttrouve.testing.matchers.fluentatts.Example$$Lambda$3/891373301@649869c7 =~ is <2>
        org.objecttrouve.testing.matchers.fluentatts.Example$$Lambda$5/322067951@371320e6 = <true> <> <false>
        

    If tracking is disabled, test output is not human-friendly.
    Accept a performance penalty and set system property
    org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher.tracking=true
    to obtain human readable output.

         but: was <org.objecttrouve.testing.matchers.fluentatts.Example$Result@d6d3c30>
        at org.hamcrest.MatcherAssert.assertThat(MatcherAssert.java:20)
        at org.hamcrest.MatcherAssert.assertThat(MatcherAssert.java:8)
        at org.objecttrouve.testing.matchers.fluentatts.Example.testSomething(Example.java:48)

1 test completed, 1 failed
```

Not very human-friendly but assuming optimistically that tests seldomly fail when running as a safety net on Jenkins. 

In the hot phase, during development, however, you might want to have something more user friendly. 

Just run the test with the sytem property `org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher.tracking=true`. 

Like so:

```
./gradlew -Dorg.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher.tracking=true test --tests org.objecttrouve.testing.matchers.fluentatts.Example --info 

org.objecttrouve.testing.matchers.fluentatts.Example > testSomething FAILED
    java.lang.AssertionError: 
    Expected: is 
        getStringValue = "1=" <> "2="
        getIntValue =~ is <2>
        isBoolValue = <true> <> <false>
        
         but: was <org.objecttrouve.testing.matchers.fluentatts.Example$Result@2f3e8ae3>
        at org.hamcrest.MatcherAssert.assertThat(MatcherAssert.java:20)
        at org.hamcrest.MatcherAssert.assertThat(MatcherAssert.java:8)
        at org.objecttrouve.testing.matchers.fluentatts.Example.testSomething(Example.java:52)

1 test completed, 1 failed
```

All of a sudden, the output becomes much more user friendly.

### Limitations

The `FluentAttributeMatcher` attempts to track what's happening inside the getter lambda. This involves quite a bit of expensive magic with a significant performance penalty. Which is why the feature is disabled by default. 

And it does come with quite a few limitations at the moment. There's no support for 
* tracking across final classes or methods,
* tracking anything but method calls,
* tracking implementations with package structures causing `IllegalAccessError`,
* probably more....


### Interpreting The Output

The output always follows the form 
```
<getter description> = <expected value> <> <actual value>
```
reading as "property equal to expected value and not actual value"
or 
```
<getter description> =~ <matcher provided description>
```
reading as "property matching whatever the matchter formulates".

If the getter function involves further calls on intermediate objects they are separated by a `/` e.g.
```
getValue/getSubValues/first/length
```
sometimes terminating with a `/???` indicating that the call could not be tracked any further. 

In some cases, e.g. when the thing tracked is not a method, the getter description may end up empty.


License
-------
[MIT License](https://opensource.org/licenses/MIT)