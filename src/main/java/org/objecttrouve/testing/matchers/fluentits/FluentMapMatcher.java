package org.objecttrouve.testing.matchers.fluentits;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.objecttrouve.testing.matchers.ConvenientMatchers;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import org.objecttrouve.testing.matchers.api.Config;
import org.objecttrouve.testing.matchers.api.ScorableMatcher;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

/**
 * <p>
 * A {@code org.hamcrest.TypeSafeMatcher} implementation
 * to check multiple characteristics of an <i>actual</i> {@code Map}
 * at the same time.
 * </p>
 * <p>Offers a fluent API to express expectations about the actual {@code Map}
 * such as size, sortedness or the expected items.</p>
 * <p>
 */
public class FluentMapMatcher<K, V> extends TypeSafeMatcher<Map<K, V>> implements ScorableMatcher {


    private static class MapEntry<K, V> implements Map.Entry<K, V>{

        private final K key;
        private final V value;

        private MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V v) {
            throw new UnsupportedOperationException("not mutable");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MapEntry<?, ?> mapEntry = (MapEntry<?, ?>) o;
            return Objects.equals(key, mapEntry.key) && Objects.equals(value, mapEntry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return "{" +
                    "k=" + key +
                    ", v=" + value +
                    '}';
        }

    }

    private static class MapEntryComparator<K, V> implements Comparator<Map.Entry<K, V>>{

        private final Comparator<K> keyComparator;

        private MapEntryComparator(Comparator<K> keyComparator) {
            this.keyComparator = keyComparator;

        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public int compare(Map.Entry<K, V> entry1, Map.Entry<K, V> entry2) {
            if(keyComparator != null){
                return keyComparator.compare(Optional.ofNullable(entry1).map(Map.Entry::getKey).orElse(null), Optional.ofNullable(entry2).map(Map.Entry::getKey).orElse(null));
            }
            if (entry1 == null || entry2 == null){
                throw new IllegalArgumentException("null entry not supported");
            }
            K k1 = entry1.getKey();
            K k2 = entry2.getKey();
            if (!(k1 instanceof Comparable && k2 instanceof Comparable)){
                throw new IllegalArgumentException("key not comparable");
            }
            Comparable c1 = (Comparable) k1;
            Comparable c2 = (Comparable) k2;
            return c1.compareTo(c2);
        }
    }

    private final FluentIterableMatcher<Map.Entry<K, V>, Set<Map.Entry<K, V>>> delegate;

    FluentMapMatcher(final Map<K, V> typedMap) {
        this.delegate = ConvenientMatchers.anIterableLike(typedMap.entrySet());
    }


    FluentMapMatcher(@SuppressWarnings("unused") Map<K, V> map, Prose<Map.Entry<K, V>> prose, Config config) {
        this.delegate = new FluentIterableMatcher<>(null, prose, config);
    }


    /**
     * <p>Adds expected key/value pair.</p>
     * <p>For each expected key/value pair, there must be at least one matching entry.</p>
     *
     * @param key Expected key.
     * @param value Expected value for {code key}.
     * @return The {@code FluentMapMatcher} instance on which the method was called.
     */
    public FluentMapMatcher<K, V> withKeyVal(final K key, final V value){
        MapEntry<K, V> entry = new MapEntry<>(key, value);
        delegate.withItems(entry);
        return this;
    }

    /**
     * <p>Sets the expected number of items in the <i>actual</i> {@code Map}.</p>
     *
     * @param expectedSize The expected number of items in the {@code Iterable}.
     * @return The {@code FluentMapMatcher} instance on which the method was called.
     */
    @SuppressWarnings("WeakerAccess")
    public FluentMapMatcher<K, V> ofSize(final int expectedSize) {
       delegate.ofSize(expectedSize);
       return this;
    }

    /**
     * <p>Expect the {@code Map} to be sorted by key in the natural item order.</p>
     * <p>Applicable only if the keys in the {@code Map} implement {@code java.lang.Comparable}.</p>
     * <p>If the items aren't {@code Comparable}, use {@link FluentMapMatcher#sorted(java.util.Comparator)}.</p>
     *
     * @return The {@code FluentMapMatcher} instance on which the method was called.
     */
    public FluentMapMatcher<K, V> sorted(){
        return sorted(null);
    }

    /**
     * <p>Expect the {@code Map} to be sorted by key according to the order defined by the {@code keyComparator}.</p>
     *
     * @param keyComparator {@code Comparator} defining how the {@code Map}'s items should be sorted.
     * @return The {@code FluentMapMatcher} instance on which the method was called.
     */
    public FluentMapMatcher<K, V> sorted(final Comparator<K> keyComparator){
        delegate.sorted(new MapEntryComparator<>(keyComparator));
        return this;
    }

    /**
     * <p>Adds {@code Matcher}s for the {@code Map}'s key/value pairs.</p>
     * <p>For each {@code Matcher}, there must be at least one matching item.</p>
     *
     * @param keyMatcher {@code Matcher}s to be applied to the {@code Map}'s keys.
     * @param valueMatcher {@code Matcher}s to be applied to the {@code Map}'s values, corresponding to the matched key.
     * @return The {@code FluentMapMatcher} instance on which the method was called.
     */
    @SuppressWarnings("unchecked")
    public FluentMapMatcher<K, V> withKeyValMatching(final Matcher<K> keyMatcher, final Matcher<V> valueMatcher){
        delegate.withItemsMatching(a(Map.Entry.class)
                .withMatching(attribute("key", m -> (K) m.getKey()), keyMatcher)
                .withMatching(attribute("value", m -> (V) m.getValue()), valueMatcher));
        return this;
    }

    FluentMapMatcher<K,V> debugging(boolean inDebugMode) {
        delegate.debugging(inDebugMode);
        return this;
    }


    @Override
    public double getScore() {
        return delegate.getScore();
    }

    @Override
    protected boolean matchesSafely(Map<K, V> map) {
        return delegate.matches(map.entrySet());
    }

    @Override
    public void describeTo(Description description) {
        delegate.describeTo(description);
    }

    @Override
    protected void describeMismatchSafely(Map<K, V> map, Description mismatchDescription) {
        delegate.describeMismatchSafely(map.entrySet(), mismatchDescription);
    }
}
