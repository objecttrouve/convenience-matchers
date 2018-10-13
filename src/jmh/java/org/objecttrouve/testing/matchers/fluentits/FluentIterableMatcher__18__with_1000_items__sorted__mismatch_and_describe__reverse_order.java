/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentits;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.reverseOrder;
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
public class FluentIterableMatcher__18__with_1000_items__sorted__mismatch_and_describe__reverse_order {

    private final List<Integer> input = range(1, 1001).boxed().sorted(reverseOrder()).collect(toList());
    private final List<Integer> expectedItemsList = range(1, 1001).boxed().collect(toList());

    @Setup(Level.Trial)
    public void checkFails() {
        assertThat(matcher(), not(is("")));
        assertThat(control(), not(is("")));
    }

    @Benchmark
    public Description matcher() {
        return matchAndDescribe(
            anIterableOf(Integer.class)
                .sorted(),
            input);
    }

    @Benchmark
    public Description control() {
        return matchAndDescribe(
            CoreMatchers.is(expectedItemsList),
            input);
    }
}
