# Convenience Matchers: Fluent Matchers For Hamcrest Assertions

Convenience library with custom [Hamcrest](http://hamcrest.org/JavaHamcrest/) matcher derivates.<br>
Fluent API to formulate multiple coherent expectations in a single line assertion.<br>
Customizable object printing for highly revealing test output.<br>

[<img src="https://maven-badges.herokuapp.com/maven-central/org.objecttrouve/convenience-matchers/badge.svg" alt="Latest release in Maven Repository"/>](https://mvnrepository.com/artifact/org.objecttrouve/convenience-matchers)

## API

### Matching Objects: `FluentAttributeMatcher`

Yet another [approach for verifying multiple properties in a single line assertion](https://www.baeldung.com/java-testing-single-assert-multiple-properties). 
The `FluentAttributeMatcher` is a [`TypeSafeMatcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/TypeSafeMatcher.html) with a fluent builder API. 
The API represents target object attributes as named lambda expressions. 
Naming the object properties is particularly helpful for comprehensible output.
The matcher allows to jointly verify properties that belong conceptually together and, at the same time, to ignore irrelevant properties. 

Here's an example:

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


Since you provided revealing names for the object properties, you get helpful output in case a test fails. 

![Error description by FluentAttributeMatcher](https://github.com/objecttrouve/convenience-matchers/blob/master/doc/img/FluentAttributeMatcher-test-output.png)


### Matching Iterables: `FluentIterableMatcher`

A [`TypeSafeMatcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/TypeSafeMatcher.html) with fluently formulatable expectations about an `Iterable`:
* Size
* Items
* (Sort) order
* Uniqueness

Matchers can be nested. 
If expected items are represented by implementations of the [`ScorableMatcher`](https://github.com/objecttrouve/convenience-matchers/blob/master/src/main/java/org/objecttrouve/testing/matchers/api/ScorableMatcher.java) interface, the best match is presented first.

Example:

```java
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

(See also the [full code](https://github.com/objecttrouve/convenience-matchers/blob/master/src/test/java/org/objecttrouve/testing/matchers/fluentits/Examples.java).)

In case of an assertion failure, you get immediate feedback about the nature of the mismatch.

The above example produces the following error message: 

![Error description by FluentIterableMatcher](https://github.com/objecttrouve/convenience-matchers/blob/master/doc/img/FluentIterableMatcher-test-output.png)

The mismatch description starts with a summary of expectations followed by a summary of findings.
The actual items are presented in the order in which they were iterated.   
After each actual item there's a sequence of symbols that indicate if the actual item was matched or (in what way) not matched.  
At the end of each line there's a sequence of the expected values or matchers for the item that were not matched.  
If items are represented by `ScorableMatcher`s and multiple such matchers match only partially, the best match is presented first.  
(Unless you call `exactly().ordered()`. In this case the value or matcher added exactly at the item's position is assumed to be the correct one.)  

### Matching Maps: `FluentMapMatcher`

You can also use the fluent API to formulate expectation about `Map`s: Items, size and sortedness.

```java

    @Test
    public void mapMatcher() {
    
        final Map<String, String> map = new TreeMap<>();
        map.put("key2", "value2");
        map.put("key1", "value1");

        assertThat(map, is(
                aMapLike(map)
                        .sorted()
                        .ofSize(2)
                        .withKeyVal("key1", "value1")
                        .withKeyValMatching(equalTo("key2"), equalTo("value2"))
        ));
    }

```

### Customization

No faible for fancy symbols? Crappy `toString`? Need some debug output? Here you go...

```java

    @Test
    public void customized() {

        final Function<String, String> truncated = s -> s.substring(0, 3) + "...";
        final Function<String, String> detailed = s -> s + " (" + s.length() + ")";
        final Attribute<String, String> prefix = attribute("prefix", s -> s.substring(0, 4));

        final MatcherFactory an = ConvenientMatchers.customized()
            .debugging()
            .withAsciiSymbols()
            .withStringifiers(stringifiers()
                .withShortStringifier(String.class, truncated)
                .withDebugStringifier(String.class, detailed)
            )
            .build();

        final List<String> items = asList("This", "prints", "pretty", ".....");

        assertThat(items, is(
            an.iterableOf(String.class)
                .withItemsMatching(
                    an.instanceOf(String.class)
                        .with(prefix, "Xxxx"))));
    }

```



Why (not)?
-----------

### Benefits 

#### Matching

* Fluent DSL that allows for focusing on the relevant aspects
* Human friendly configurable output in case of a mismatch
* Compensate poor or absent `toString` implementations

#### Matching Objects

* Check all relevant properties of an object on one go
* Ignore irrelevant properties at the same time
* Match nested structures in a uniform way, aligned with the test object structure

#### Matching Maps And Iterables

* Instantaneous overview of which items in an `Iterable` matched and which didn't
* Clear indication of which actual item breaks sort order or expected order of items
* Easy identification of unwanted duplicates
* Easy identification of unwanted items
* Revealing error descriptions

### Drawbacks

* Performance tradeoffs
* Needs some syntactic sugaring


Alternatives
------------

There's choice!
* [Google Truth](https://google.github.io/truth/)
* [AssertJ](http://joel-costigliola.github.io/assertj/)
* [JUnit 5, `assertAll`](https://junit.org/junit5/docs/5.0.0-M2/api/org/junit/jupiter/api/Assertions.html#assertAll-org.junit.jupiter.api.Executable...-)

Download
---------

Artifacts available at the [Maven Repository](https://mvnrepository.com/artifact/org.objecttrouve/convenience-matchers).

Build
------

To build at home, run

```./gradlew clean build -x signArchives```

Contributions welcome!

License
-------
[MIT License](https://opensource.org/licenses/MIT)

