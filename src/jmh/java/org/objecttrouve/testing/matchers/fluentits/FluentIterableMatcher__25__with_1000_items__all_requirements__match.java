/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.anIterableOf;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentIterableMatcher__25__with_1000_items__all_requirements__match {


    private final List<Integer> input = range(1, 1001).boxed().collect(toList());
    private final Integer[] expectedItemsArray = input.toArray(new Integer[0]);
    private final List<Integer> expectedItemsList = new ArrayList<>(input);

    @Setup(Level.Trial)
    public void checkMatches() {
        assertThat(matcher(), is(true));
        assertThat(control(), is(true));
    }


    @Benchmark
    public boolean matcher() {
        return anIterableOf(Integer.class)
            .withItems(expectedItemsArray)
            .ordered()
            .sorted()
            .unique()
            .exactly()
            .ofSize(1000)
            .matches(input);
    }

    @Benchmark
    public boolean control() {
        return CoreMatchers.is(expectedItemsList)
            .matches(input);
    }
}
