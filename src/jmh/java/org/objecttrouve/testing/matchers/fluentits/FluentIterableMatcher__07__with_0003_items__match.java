/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.anIterableOf;

@SuppressWarnings("unused")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FluentIterableMatcher__07__with_0003_items__match {


    private final List<Integer> input = range(1, 4).boxed().collect(toList());

    @Setup(Level.Trial)
    public void checkMatches() {
        assertThat(matcher(), is(true));
        assertThat(control(), is(true));
    }


    @Benchmark
    public boolean matcher() {
        return anIterableOf(Integer.class)
            .withItems(1, 2, 3)
            .matches(input);
    }

    @Benchmark
    public boolean control() {
        return CoreMatchers.hasItems(1, 2, 3)
            .matches(input);
    }
}
