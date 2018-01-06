Convenience Matchers
====================

Convenience library with custom [Hamcrest](http://hamcrest.org/JavaHamcrest/) Matcher derivates for comfortable unit testing.

`FluentAttributeMatcher`
------------------------
A [`TypeSafeMatcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/TypeSafeMatcher.html) derivate to check multiple target object attributes at once. 
Offers a fluent builder API in which the attributes of the target object can be referred to via lambda expressions. 

### Basic Concept 

Best illustrated with an example:

```java
package org.objecttrouve.testing.matchers.fluentatts;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
// Use the factory method for syntactic sugaring and configurability!
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

@SuppressWarnings("ALL")
@Ignore("Failing intentionally.")
public class Example {

    // The class we want to match.
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

    // Name the functions that provide the output you're interested in. 
    // The names are used for descriptions in case there's a mismatch.
    private static final Attribute<Result, String> stringValue = attribute("stringValue", Result::getStringValue);
    private static final Attribute<Result, Integer> intValue = attribute("intValue", Result::getIntValue);
    private static final Attribute<Result, Boolean> boolValue = attribute("booleanValue", Result::isBoolValue);

    @Test
    public void testSomething(){

        final Result result = methodWithResult("2=", 1, false);

        assertThat(result, is(//
                a(Result.class)                 // One matcher to rule them ALL!
                .with(stringValue, "1=")        // Add an expectation about a particular property value.
                .with(intValue, is(2))          // You can also pass another matcher.
                .with(boolValue, true)          // ...And so on...
        ));
    }

}
```

The idea is to pass a lambda expression or method reference that is getting an attribute value from the target object along with the expected value. 
Alternatively, it is possible to pass a `Matcher` instead of an expected object. 
The `FluentAttributeMatcher` implements the builder pattern so that it can be set up in a fluent style.  


### Interpreting The Output

If you provide nice names you get nice output in case of a mismatch.

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


License
-------
[MIT License](https://opensource.org/licenses/MIT)


L'Objet Trouvé
==============

It's Art!
---------

This piece of code can just be grabbed and turned into artwork.
(So much for the theory.)

Of course, people will become aware of the ground-breaking cultural impact only after the author's passing.  

And yes, you write the real French [objet trouvé](https://en.wikipedia.org/wiki/Found_object) without a _c_. But who knows French, anyway? So just consider the "object" in `objecttrouve` an anglicism. 