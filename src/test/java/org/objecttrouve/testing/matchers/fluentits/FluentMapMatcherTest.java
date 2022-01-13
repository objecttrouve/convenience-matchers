package org.objecttrouve.testing.matchers.fluentits;

import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.aMapLike;


public class FluentMapMatcherTest {

    @Test
    public void testMapMatcher(){
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");

        assertThat(map, is(aMapLike(map).withKeyVal(1, "1").withKeyVal(2,"2")));
    }

    @Test(expected = AssertionError.class)
    public void testMapMatcherFail(){
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");

        assertThat(map, is(aMapLike(map).withKeyVal(2, "1").withKeyVal(1,"2")));
    }

    @Test
    public void testMapMatcherMatcherArg(){
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");

        assertThat(map, is(aMapLike(map)
                .withKeyValMatching(equalTo(1), equalTo("1"))
                .withKeyValMatching(equalTo(2), equalTo("2"))));
    }

    @Test(expected = AssertionError.class)
    public void testMapMatcherMatcherArgFail(){
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");

        assertThat(map, is(aMapLike(map)
                .withKeyValMatching(equalTo(3), equalTo("1"))
                .withKeyValMatching(equalTo(2), equalTo("2"))));
    }
}