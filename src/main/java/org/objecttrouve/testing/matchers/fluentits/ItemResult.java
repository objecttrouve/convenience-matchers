/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import org.hamcrest.Matcher;

import java.util.Collections;
import java.util.List;

class ItemResult<X> {

    static class MatcherWithIndex {
        private final Matcher matcher;
        private final int index;

        MatcherWithIndex(final Matcher matcher, final int index) {
            this.matcher = matcher;
            this.index = index;
        }

        public Matcher getMatcher() {
            return matcher;
        }

        int getIndex() {
            return index;
        }
    }
    private final int index;
    private final boolean matched;
    private final X actual;
    private final List<MatcherWithIndex> matchers;
    private final boolean breakingSort;
    private final boolean breakingItemOrder;
    private final boolean duplicate;
    private final boolean obsolete;

    private ItemResult(final Builder<X> builder) {
        matched = builder.matched;
        index = builder.index;
        actual = builder.actual;
        matchers = builder.matchers;
        breakingSort = builder.breakingSort;
        breakingItemOrder = builder.breakingItemOrder;
        duplicate = builder.duplicate;
        obsolete = builder.obsolete;
    }

    static <X> Builder<X> builder(final X actual){
        return new Builder<>(actual);
    }

    static class Builder<X> {

        private boolean matched;
        private int index;
        private final X actual;
        private List<MatcherWithIndex> matchers = Collections.emptyList();
        private boolean breakingSort;
        private boolean breakingItemOrder;
        private boolean duplicate;
        private boolean obsolete;

        private Builder(final X actual) {
            this.actual = actual;
        }

        ItemResult<X> build(){
            return new ItemResult<>(this);
        }

        Builder<X> withIndex(final int index) {
            this.index = index;
            return this;
        }

        Builder<X> matched(final boolean matched) {
            this.matched = matched;
            return this;
        }

        Builder<X> withMatchers(final List<MatcherWithIndex> matchers) {
            this.matchers = matchers;
            return this;
        }

        Builder<X> breakingSortOrder(final boolean breakingSort) {
            this.breakingSort = breakingSort;
            return this;
        }

        Builder<X> breakingItemOrder(final boolean breakingItemOrder) {
            this.breakingItemOrder = breakingItemOrder;
            return this;
        }

        Builder<X> duplicate(final boolean duplicate) {
            this.duplicate = duplicate;
            return this;
        }

        Builder<X> unwanted(final boolean obsolete) {
            this.obsolete = obsolete;
            return this;
        }
    }

    boolean isMatched() {
        return matched;
    }

    int getIndex() {
        return index;
    }


    public X getActual() {
        return actual;
    }

    List<MatcherWithIndex> getMismatchedItemMatchers() {
        return matchers;
    }

    boolean isBreakingSortOrder() {
        return breakingSort;
    }

    boolean isBreakingItemOrder() {
        return breakingItemOrder;
    }

    boolean isDuplicate() {
        return duplicate;
    }

    boolean isUnwanted() {
        return obsolete;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ItemResult<?> that = (ItemResult<?>) o;

        return index == that.index;
    }

    @Override
    public int hashCode() {
        return index;
    }
}
