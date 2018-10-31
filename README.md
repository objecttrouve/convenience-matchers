Convenience Matchers
====================

Convenience library with custom [Hamcrest](http://hamcrest.org/JavaHamcrest/) Matcher derivates for more comfortable unit testing.

`FluentAttributeMatcher`
------------------------
A [`TypeSafeMatcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/TypeSafeMatcher.html) extension to check multiple target object attributes at once. 
Offers a fluent builder API in which the attributes of the target object can be referred to via lambda expressions. 

### Basic Concept 

Best illustrated with an example:

```java

// [...]
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

@SuppressWarnings("ALL")
@Ignore("Failing intentionally.")
public class Example {

    // [...]

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

(See also the [full example](https://github.com/objecttrouve/convenience-matchers/blob/master/src/test/java/org/objecttrouve/testing/matchers/fluentatts/Example.java).)

Object properties are described with named lambdas or method references (`Attribute`s).
The `Attribute`'s expression returns the actual value form the object.
That value is then compared to the expected value or matched against a provided `Matcher`.
The `FluentAttributeMatcher` implements the builder pattern so that it can be set up in a fluent style.  


### Interpreting The Output

If you provide nice names you get nice output in case of a mismatch.

![Error description by FluentAttributeMatcher](https://github.com/objecttrouve/convenience-matchers/blob/master/doc/img/FluentAttributeMatcher-test-output.png)




### Why (not)?

#### Benefits 
* Check all relevant properties of an object on one go.
* Ignore irrelevant properties at the same time. 
* Match nested structures in a uniform way.
* Human friendly DSL.
* Human friendly output in case of a mismatch.

#### Drawbacks
* Performance is traded for convenience.
    * (When compared to the minimal logic you could alternatively use to test the same features. Less convenient, of course.)
    * (Check [benchmarks](https://github.com/objecttrouve/convenience-matchers/tree/master/benchmarks/) or run [JMH](https://github.com/objecttrouve/convenience-matchers/tree/master/src/jmh/java/org/objecttrouve/testing) if it's crucial.)
* Requires a minimum of syntactic sugaring.
* Heavy dependencies (but deprecated, to be removed with v1.0). 

`FluentIterableMatcher`
-----------------------

A [`TypeSafeMatcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/TypeSafeMatcher.html) with fluently formulatable expectations about an `Iterable`.

### Concept & Goals

Have you ever spent time wondering *which* item caused a mismatch when a collection `Matcher` failed? 
Have you ever asked yourself whether that matcher was sensitive to the size of the `Collection`? 
Have you ever wondered if the item order was playing a role?

The `FluentIterableMatcher` addresses such sources of confusion. 
It produces an error description that tells you exactly which items in the `Iterable` need more attention. 
It has a fluent API to specify requirements beyond items, such as size or order. 

Here's a typical example:

```
// [...]
import static org.objecttrouve.testing.matchers.ConvenientMatchers.anIterableOf;

@Ignore("Failing intentionally.")
public class Examples {

    // [...]
    
      @Test
        public void heavyMismatch() {
    
            final List<String> strings = asList(
                "fake",
                "impeachment",
                "Donald",
                "Trump",
                "fake",
                "news"
            );
    
            assertThat(strings, is(
                anIterableOf(String.class)
                    .ofSize(9)
                    .sorted()
                    .ordered()
                    .unique()
                    .withItemsMatching(
                        startsWith("Ron"),
                        endsWith("ment")
                    )
                    .withItems(
                        "true",
                        "news",
                        "impeachment"
                    )
            ));
        }
}
```

(See also the [full example](https://github.com/objecttrouve/convenience-matchers/blob/master/src/test/java/org/objecttrouve/testing/matchers/fluentits/Examples.java).)

You can fluently express the essential requirements you might have about an `Iterable`: 

* The size.
* The items.
    * By expected item.
    * By matcher.
* Is the iterable sorted? 
* Are items in the same order as specified? 
* No duplicates. 
* Are there any unexpected items?

The main goal, however, is to have an error message that immediately tells *which* items were not matched and *which* expectation was unmet, if any.

### Reading The Output

#### Mismatch Description

The above example produces the following error message: 

![Error description by FluentIterableMatcher](https://github.com/objecttrouve/convenience-matchers/blob/master/doc/img/FluentIterableMatcher-output.png)

The mismatch description starts with a summary of expectations followed by a summary of findings. 
Afterwards it's getting interesting. 
The actual items are presented in the order in which they were iterated. 
After each actual items there is a sequence of symbols that indicate if the actual item was matched or (in what way) not matched.
At the end of each line there's a sequence of the expectations for the item at the given position that were not met.

### Pro/Con

#### Benefits 
* Instantaneous overview of which items matched and which didn't.
* Clear indication of which actual item breaks sort order or expected order of items.
* Easy identification of unwanted duplicates. 
* Easy identification of unwanted items.
* Symbols are so lovely that they comfort you in case of a mismatch.
* Fluent DSL that allows for focusing on the relevant aspects. 

#### Drawbacks
* Performance is traded for convenience.
    * (When compared to the minimal logic you could use otherwise.)
    * (Check [benchmarks](https://github.com/objecttrouve/convenience-matchers/tree/master/benchmarks/) or run [JMH](https://github.com/objecttrouve/convenience-matchers/tree/master/src/jmh/java/org/objecttrouve/testing) if it's crucial.)
* Heavy dependencies (but deprecated, to be removed with v1.0).
* Lovely symbols aren't always displayed nicely. (And therefore there's a plan for an optional ASCII-only flavor by v1.0.)


License
-------
[MIT License](https://opensource.org/licenses/MIT)

