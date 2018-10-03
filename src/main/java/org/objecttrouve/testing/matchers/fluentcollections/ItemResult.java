/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentcollections;

import org.hamcrest.Matcher;

import java.util.Collections;
import java.util.List;

class ItemResult<X> {

    private final int index;
    private final boolean matched;
    private final X actual;
    private final List<Matcher> matchers;
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
        private List<Matcher> matchers = Collections.emptyList();
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

        Builder<X> withMatchers(final List<Matcher> matchers) {
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

    List<Matcher> getMismatchedItemMatchers() {
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
}
