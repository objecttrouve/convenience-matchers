/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.customization;

import org.objecttrouve.testing.matchers.api.Config;
import org.objecttrouve.testing.matchers.api.Stringifiers;
import org.objecttrouve.testing.matchers.fluentatts.FlamFactory;
import org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher;
import org.objecttrouve.testing.matchers.fluentits.FlimFactory;
import org.objecttrouve.testing.matchers.fluentits.FluentIterableMatcher;

import java.util.function.Function;

/**
 * Factory for {@link FluentAttributeMatcher} and {@link FluentIterableMatcher} instances.
 */
public class MatcherFactory {

    /**
     * Builder to configure a customization for {@link FluentAttributeMatcher} and {@link FluentIterableMatcher} instances.
     */
    public static class Builder {

        private final MatcherConfig.Builder config = MatcherConfig.config();


        /**
         * Use the default symbols in error messages.
         * Fancy but potentially poor in primitive shells.
         * @return {@code this Builder}.
         */
        public Builder withDefaultSymbols() {
            return withSymbols(SymbolsConfig.defaultSymbols());
        }

        /**
         * Use only ASCII symbols in error messages.
         * Less fancy but suitable for primitive shells.
         * @return {@code this Builder}.
         */
        public Builder withAsciiSymbols() {
            return withSymbols(SymbolsConfig.asciiSymbols());
        }

        /**
         * Define a set of custom symbols to be used in error messages.
         * @param symbolsBuilder A {@link SymbolsConfig.Builder} preconfigured with the desired set of symbols.
         * @return {@code this Builder}.
         */
        public Builder withSymbols(final SymbolsConfig.Builder symbolsBuilder) {
            return withSymbols(symbolsBuilder.build());
        }

        /**
         * Define a set of custom symbols to be used in error messages.
         * @param symbols The desired set of  {@link SymbolsConfig}.
         * @return {@code this Builder}.
         */
        @SuppressWarnings("WeakerAccess")
        public Builder withSymbols(final SymbolsConfig symbols) {
            config.withSymbols(symbols);
            return this;
        }

        /**
         * <p>Define a set of {@link Function}s that provide a short String description of an object.</p>
         * <p>(Useful when a tested class doesn't have a useful {@code Object#toString()} method.)</p>
         * @param stringifiersBuilder A {@link StringifiersConfig.Builder} preconfigured with mappings for classes and stringifier functions.
         * @return {@code this Builder}.
         */
        public Builder withStringifiers(final StringifiersConfig.Builder stringifiersBuilder){
            return withStringifiers(stringifiersBuilder.build());
        }

        /**
         * <p>Define a set of {@link Function}s that provide a short String description of an object.</p>
         * <p>(Useful when a tested class doesn't have a useful {@code Object#toString()} method.)</p>
         * @param stringifiers A set of {@link Stringifiers} preconfigured with mappings for classes and stringifier functions.
         * @return {@code this Builder}.
         */
        @SuppressWarnings("WeakerAccess")
        public Builder withStringifiers(final Stringifiers stringifiers) {
            this.config.withStringifiers(stringifiers);
            return this;
        }

        /**
         * <p>Switches on debug mode for all matchers created by the resulting {@code MatcherFactory}.</p>
         *
         * @return {@code this Builder}.
         */
        public Builder debugging() {
            this.config.debugging();
            return this;
        }

        public MatcherFactory build(){
            return new MatcherFactory(config.build());
        }
    }

    private final Config config;

    public static Builder factory(){
        return new Builder();
    }

    private MatcherFactory(final Config config){
        this.config = config;
    }

    /**
     * <p>
     * Factory method for a {@link FluentAttributeMatcher}
     * to match an <i>actual</i> object's properties.</p>
     *
     * @param klass The expected class of the actual object.
     * @param <T> Expected type of the actual object.
     * @return FluentAttributeMatcher.
     */
    public <T> FluentAttributeMatcher<T> instanceOf(final Class<T> klass) {
       return FlamFactory.fluentAttributeMatcher(klass, config);
    }

    /**
     * <p>Factory method for a {@link FluentIterableMatcher}
     * to match an <i>actual</i> {@code Iterable}'s properties.</p>
     *
     * @param klass The expected class of the actual {@code Iterable}.
     * @param <X> Expected type of the actual {@code Iterable}'s items.
     * @param <C> Expected type of the actual {@code Iterable}.
     * @return FluentAttributeMatcher.
     */
    public <X, C extends Iterable<X>> FluentIterableMatcher<X, C> iterableOf(final Class<X> klass){
        return FlimFactory.fluentIterableMatcher(klass, config);
    }

}
