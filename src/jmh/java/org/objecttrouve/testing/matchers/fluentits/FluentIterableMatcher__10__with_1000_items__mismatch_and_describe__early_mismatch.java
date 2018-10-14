/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentits;

import org.hamcrest.CoreMatchers;
import org.openjdk.jmh.annotations.*;

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
public class FluentIterableMatcher__10__with_1000_items__mismatch_and_describe__early_mismatch {

    private final List<Integer> input = range(1, 1001).boxed().collect(toList());
    private final Integer[] expectedItems = input.toArray(new Integer[0]);

    @Setup(Level.Trial)
    public void checkFails() {
        expectedItems[0] = -1;
        assertThat(matcher(), not(is("")));
        assertThat(control(), not(is("")));
    }

    @Benchmark
    public String matcher() {
        return matchAndDescribe(
            anIterableOf(Integer.class)
            .withItems(expectedItems),
            input);
    }

    @Benchmark
    public String control() {
        return matchAndDescribe(
            CoreMatchers.hasItems(expectedItems),
            input);
    }
}
