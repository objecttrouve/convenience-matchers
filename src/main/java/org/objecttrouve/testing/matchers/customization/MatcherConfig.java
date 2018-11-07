/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.customization;

import org.objecttrouve.testing.matchers.api.Config;
import org.objecttrouve.testing.matchers.api.Stringifiers;
import org.objecttrouve.testing.matchers.api.Symbols;

import java.util.function.Function;

public class MatcherConfig implements Config {

    public static class Builder {

        private Symbols symbols = SymbolsConfig.defaultSymbols();
        private Stringifiers stringifiers = StringifiersConfig.stringifiers().build();
        /**
         * Define a set of custom symbols to be used in error messages.
         * @param symbolsBuilder A {@link SymbolsConfig.Builder} preconfigured with the desired set of symbols.
         * @return {@code this Builder}.
         */
        @SuppressWarnings("unused")
        public Builder withSymbols(final SymbolsConfig.Builder symbolsBuilder ) {
            return withSymbols(symbolsBuilder.build());
        }

        /**
         * Define a set of custom symbols to be used in error messages.
         * @param symbols The desired set of  {@link SymbolsConfig}.
         * @return {@code this Builder}.
         */
        @SuppressWarnings("WeakerAccess")
        public Builder withSymbols(final Symbols symbols) {
            this.symbols = symbols;
            return this;
        }

        /**
         * <p>Define a set of {@link Function}s that provide a short String description of an object.</p>
         * <p>(Useful when a tested class doesn't have a useful {@code Object#toString()} method.)</p>
         * @param stringifiersBuilder A {@link StringifiersConfig.Builder} preconfigured with mappings for classes and stringifier functions.
         * @return {@code this Builder}.
         */
        @SuppressWarnings("unused")
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
            this.stringifiers = stringifiers;
            return this;
        }

        public Config build(){
            return new MatcherConfig(this);
        }

    }

    private final Symbols symbols;
    private final Stringifiers stringifiers;

    static Builder config(){
        return new Builder();
    }

    private MatcherConfig(final Builder builder) {
        this.symbols = builder.symbols;
        this.stringifiers = builder.stringifiers;
    }

    /**
     * @return Set of {@code Symbols} to be used in error descriptions.
     */
    @Override
    public Symbols getSymbols() {
        return symbols;
    }

    /**
     * @return {@code Stringifiers} to pretty-print actual instances.
     */
    @Override
    public Stringifiers getStringifiers() {
        return stringifiers;
    }
}
