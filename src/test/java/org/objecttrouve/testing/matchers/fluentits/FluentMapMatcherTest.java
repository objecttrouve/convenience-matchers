package org.objecttrouve.testing.matchers.fluentits;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.objecttrouve.testing.matchers.ConvenientMatchers;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.aMapLike;
import org.objecttrouve.testing.matchers.customization.MatcherFactory;
import static org.objecttrouve.testing.matchers.customization.StringifiersConfig.stringifiers;


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

    @Test
    public void testMapMatcherSorted(){
        final Map<Integer, String> map = new TreeMap<>();
        map.put(1, "1");
        map.put(3, "3");
        map.put(2, "2");

        assertThat(map, is(aMapLike(map).sorted()));
    }

    @Test(expected = AssertionError.class)
    public void testMapMatcherSortedFail(){
        final Map<Integer, String> map = new LinkedHashMap<>();
        map.put(3, "3");
        map.put(2, "2");
        map.put(1, "1");

        assertThat(map, is(aMapLike(map).sorted()));
    }

    @Test
    public void testMapMatcherSortedComparator(){
        Comparator<Integer> comparator = (i1, i2) -> i2 - i1;
        final Map<Integer, String> map = new TreeMap<>(comparator);
        map.put(3, "3");
        map.put(2, "2");
        map.put(1, "1");

        assertThat(map, is(aMapLike(map).sorted(comparator)));
    }

    @Test(expected = AssertionError.class)
    public void testMapMatcherSortedComparatorFail(){
        Comparator<Integer> comparator = (i1, i2) -> i2 - i1;
        final Map<Integer, String> map = new TreeMap<>();
        map.put(2, "2");
        map.put(3, "3");
        map.put(1, "1");

        assertThat(map, is(aMapLike(map).sorted(comparator)));
    }

    @Test
    public void describeMismatchSafelyKeyValMismatch() {
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");

        String expectedDescription = "\n" +
                "Findings:\n" +
                "\"Not all expectations were fulfilled.\"\n" +
                "\n" +
                "⦗0⦘⦗1=1⦘           \uD83D\uDC94⦗0⦘⦗<{k=2, v=1}>⦘ \uD83D\uDC94⦗1⦘⦗<{k=1, v=2}>⦘\n" +
                "⦗1⦘⦗2=2⦘           \uD83D\uDC94⦗1⦘⦗<{k=1, v=2}>⦘ \uD83D\uDC94⦗0⦘⦗<{k=2, v=1}>⦘\n\n";

        FluentMapMatcher<Integer, String> matcher = aMapLike(map)
                .withKeyVal(2, "1")
                .withKeyVal(1, "2");

        checkMismatchDescription(map, matcher, expectedDescription);
    }

    @Test
    public void describeMismatchSafelyKeyValMatcherMismatch(){
        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");

        //noinspection ConcatenationWithEmptyString
        String expectedDescription = "" +
                "\nFindings:\n\"" +
                "Not all expectations were fulfilled.\"\n\n" +
                "⦗0⦘⦗1=1⦘           💔⦗0⦘⦗key ⩳ '<3>'; value ⩳ '\"1\"'⦘ 💔⦗1⦘⦗key ⩳ '<2>'; value ⩳ '\"2\"'⦘\n" +
                "⦗1⦘⦗2=2⦘💕        \n\n";

        FluentMapMatcher<Integer, String> matcher = aMapLike(map)
                .withKeyValMatching(equalTo(3), equalTo("1"))
                .withKeyValMatching(equalTo(2), equalTo("2"));

        checkMismatchDescription(map, matcher, expectedDescription);
    }

    @Test
    public void describeMismatchSafelySortOrder(){
        final Map<Integer, String> map = new LinkedHashMap<>();
        map.put(3, "3");
        map.put(2, "2");
        map.put(1, "1");

        //noinspection ConcatenationWithEmptyString
        String expectedDescription = "" +
                "\nFindings:\n" +
                "\"Collection is not sorted.\"\n\n" +
                "⦗0⦘⦗3=3⦘          \n" +
                "⦗1⦘⦗2=2⦘  ↕       \n" +
                "⦗2⦘⦗1=1⦘  ↕       \n\n";

        FluentMapMatcher<Integer, String> matcher = aMapLike(map).sorted();

        checkMismatchDescription(map, matcher, expectedDescription);
    }

    @Test
    public void describeMismatchSafelySortOrderWithComparator(){
        Comparator<Integer> comparator = (i1, i2) -> i2 - i1;
        final Map<Integer, String> map = new TreeMap<>();
        map.put(2, "2");
        map.put(3, "3");
        map.put(1, "1");

        //noinspection ConcatenationWithEmptyString
        String expectedDescription = "" +
                "\nFindings:\n" +
                "\"Collection is not sorted.\"\n\n" +
                "⦗0⦘⦗1=1⦘          \n" +
                "⦗1⦘⦗2=2⦘  ↕       \n" +
                "⦗2⦘⦗3=3⦘  ↕       \n\n";

        FluentMapMatcher<Integer, String> matcher = aMapLike(map).sorted(comparator);

        checkMismatchDescription(map, matcher, expectedDescription);
    }

    @Test
    public void describeMismatchSafelyCustomStringifier() {

        //noinspection rawtypes
        Function<Map.Entry, String> stringifier = e -> e.getKey() + " ↘️ " + e.getValue();
        final MatcherFactory a = ConvenientMatchers.customized()
                .withStringifiers(
                        stringifiers()
                                .withShortStringifier(Map.Entry.class, stringifier)
                ).build();


        final Map<Integer, String> map = new HashMap<>();
        map.put(1, "1");
        map.put(2, "2");

        //noinspection ConcatenationWithEmptyString
        String expectedDescription = "" +
                "\nFindings:\n" +
                "\"Not all expectations were fulfilled.\"\n\n" +
                "⦗0⦘⦗1 ↘️ 1⦘           💔⦗0⦘⦗<2 ↘️ 1>⦘ 💔⦗1⦘⦗<1 ↘️ 2>⦘\n" +
                "⦗1⦘⦗2 ↘️ 2⦘           💔⦗1⦘⦗<1 ↘️ 2>⦘ 💔⦗0⦘⦗<2 ↘️ 1>⦘\n\n";

        FluentMapMatcher<Integer, String> matcher = a.mapLike(map)
                .withKeyVal(2, "1")
                .withKeyVal(1, "2");

        checkMismatchDescription(map, matcher, expectedDescription);
    }

    private static void checkMismatchDescription(Map<Integer, String> map, FluentMapMatcher<Integer, String> matcher, String expectedDescription) {
        boolean matches = matcher.matchesSafely(map);
        assertThat(matches, is(false));
        final StringDescription description = new StringDescription();
        matcher.describeMismatchSafely(map, description);
        assertThat(description.toString(), is(expectedDescription));
    }
}