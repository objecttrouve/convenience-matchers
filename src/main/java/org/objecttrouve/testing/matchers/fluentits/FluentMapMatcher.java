package org.objecttrouve.testing.matchers.fluentits;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.objecttrouve.testing.matchers.ConvenientMatchers;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

public class FluentMapMatcher<K, V> extends TypeSafeMatcher<Map<K, V>> {


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

    private final FluentIterableMatcher<Map.Entry<K, V>, Set<Map.Entry<K, V>>> delegate;

    FluentMapMatcher(final Map<K, V> typedMap) {
        this.delegate = ConvenientMatchers.anIterableLike(typedMap.entrySet());
    }

    public FluentMapMatcher<K, V> withKeyVal(final K key, final V value){
        MapEntry<K, V> entry = new MapEntry<>(key, value);
        delegate.withItems(entry);
        return this;
    }

    @SuppressWarnings("unchecked")
    public FluentMapMatcher<K, V> withKeyValMatching(final Matcher<K> keyMatcher, final Matcher<V> valueMatcher){
        delegate.withItemsMatching(a(Map.Entry.class)
                .withMatching(attribute("key", m -> (K) m.getKey()), keyMatcher)
                .withMatching(attribute("value", m -> (V) m.getValue()), valueMatcher));
        return this;
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
