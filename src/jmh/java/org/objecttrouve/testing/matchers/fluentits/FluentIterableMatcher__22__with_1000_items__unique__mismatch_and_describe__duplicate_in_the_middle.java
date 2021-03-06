/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentits;

import org.hamcrest.CoreMatchers;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.objecttrouve.testing.boilerplate.Boilerplate.matchAndDescribe;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.anIterableOf;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentIterableMatcher__22__with_1000_items__unique__mismatch_and_describe__duplicate_in_the_middle {

    private final List<Integer> input = range(1, 1001).boxed().collect(toList());
    private final Integer[] expectedItemsArray = input.toArray(new Integer[0]);
    private final List<Integer> expectedItemsList = new ArrayList<>(input);

    @Setup(Level.Trial)
    public void checkFails() {
        expectedItemsArray[500] = 400;
        expectedItemsList.set(500, 400);
        assertThat(matcher(), not(is("")));
        assertThat(control(), not(is("")));
    }

    @Benchmark
    public String matcher() {
        return matchAndDescribe(
            anIterableOf(Integer.class)
            .withItems(expectedItemsArray),
            input);
    }

    @Benchmark
    public String control() {
        return matchAndDescribe(
            CoreMatchers.is(expectedItemsList),
            input);
    }
}
