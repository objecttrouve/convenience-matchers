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

/**
 * Standard {@link Config} implementation.
 */
public class MatcherConfig implements Config {

    /**
     * A <code>Config</code> builder with a fluent interface.
     */
    public static class Builder {

        private Symbols symbols = SymbolsConfig.defaultSymbols();
        private Stringifiers stringifiers = StringifiersConfig.stringifiers().build();
        private boolean debugging;

        /**
         * <p>
         *     Sets custom {@link Symbols} to be used in error messages.
         * </p>
         *
         * @param symbolsBuilder <code>SymbolsConfig.Builder</code> preconfigured with the desired set of symbols
         * @return <code>this</code> <code>Builder</code>
         */
        @SuppressWarnings("unused")
        public Builder withSymbols(final SymbolsConfig.Builder symbolsBuilder ) {
            return withSymbols(symbolsBuilder.build());
        }

        /**
         * <p>
         *     Sets custom {@link Symbols} to be used in error messages.
         * </p>
         *
         * @param symbols <code>Symbols</code> with the desired set of signs
         * @return <code>this</code> <code>Builder</code>
         */
        @SuppressWarnings("WeakerAccess")
        public Builder withSymbols(final Symbols symbols) {
            this.symbols = symbols;
            return this;
        }

        /**
         * <p>
         *     Sets custom {@link Stringifiers} that provide helpful <code>String</code> descriptions of objects.
         * </p>
         * <p>
         *     (Useful when a tested class doesn't have a useful <code>Object#toString()</code> method.)
         * </p>
         *
         * @param stringifiersBuilder <code>StringifiersConfig.Builder</code> preconfigured with mappings from <code>Class</code> to stringifier <code>Function</code>
         * @return <code>this</code> <code>Builder</code>
         */
        @SuppressWarnings("unused")
        public Builder withStringifiers(final StringifiersConfig.Builder stringifiersBuilder){
            return withStringifiers(stringifiersBuilder.build());
        }

        /**
         * <p>
         *     Sets custom {@link Stringifiers} that provide helpful <code>String</code> descriptions of objects.
         * </p>
         * <p>
         *     (Useful when a tested class doesn't have a useful <code>Object#toString()</code> method.)
         * </p>
         *
         * @param stringifiers <code>Stringifiers</code> with mappings from <code>Class</code> to stringifier <code>Function</code>
         * @return <code>this</code> <code>Builder</code>
         */
        @SuppressWarnings("WeakerAccess")
        public Builder withStringifiers(final Stringifiers stringifiers) {
            this.stringifiers = stringifiers;
            return this;
        }

        /**
         * <p>
         * Builds a {@link Config} configured with this builder's current settings.
         * </p>
         *
         * @return <code>Config</code> with customized settings
         */
        public Config build(){
            return new MatcherConfig(this);
        }

        /**
         * <p>
         *     Turns on debug mode for all matchers using the resulting {@link Config}.
         * </p>
         *
         * @return <code>this</code> <code>Builder</code>
         */
        public Builder debugging() {
            this.debugging = true;
            return this;
        }
    }

    private final Symbols symbols;
    private final Stringifiers stringifiers;
    private final boolean debugging;


    /**
     * <p>
     * Returns a builder to create a <code>Config</code> with a fluent DSL.
     * </p>
     *
     * @return new <code>Builder</code>
     */
    static Builder config(){
        return new Builder();
    }

    private MatcherConfig(final Builder builder) {
        this.symbols = builder.symbols;
        this.stringifiers = builder.stringifiers;
        this.debugging = builder.debugging;
    }

    @Override
    public Symbols getSymbols() {
        return symbols;
    }

    @Override
    public Stringifiers getStringifiers() {
        return stringifiers;
    }

    @Override
    public boolean isInDebugMode() {
        return debugging;
    }
}
