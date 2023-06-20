/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.customization;

import java.util.Map;
import org.objecttrouve.testing.matchers.api.Config;
import org.objecttrouve.testing.matchers.api.Stringifiers;
import org.objecttrouve.testing.matchers.api.Symbols;
import org.objecttrouve.testing.matchers.fluentatts.FlamFactory;
import org.objecttrouve.testing.matchers.fluentatts.FluentAttributeMatcher;
import org.objecttrouve.testing.matchers.fluentits.FlimFactory;
import org.objecttrouve.testing.matchers.fluentits.FluentIterableMatcher;
import org.objecttrouve.testing.matchers.fluentits.FluentMapMatcher;

/**
 * Factory for {@link FluentAttributeMatcher} and {@link FluentIterableMatcher} instances.
 */
public class MatcherFactory {

    /**
     * <p>
     * Builder to configure {@link FluentAttributeMatcher} and {@link FluentIterableMatcher} instances
     * created by the <code>MatcherFactory</code>. Via a fluent DSL.
     * </p>
     */
    public static class Builder {

        private final MatcherConfig.Builder config = MatcherConfig.config();

        /**
         * <p>
         *     Configures default {@link Symbols} for error messages.
         * </p>
         * <p>
         *     The default symbols are fancy in modern shells but potentially poor in more primitive environments.
         * </p>
         *
         * @return <code>this</code> <code>Builder</code>
         */
        public Builder withDefaultSymbols() {
            return withSymbols(SymbolsConfig.defaultSymbols());
        }

        /**
         * <p>
         *     Configures ASCII {@link Symbols} for error messages.
         * </p>
         * <p>
         *     Less fancy than the default symbols but robust in primitive environments.
         * </p>
         *
         * @return <code>this</code> <code>Builder</code>
         */
        public Builder withAsciiSymbols() {
            return withSymbols(SymbolsConfig.asciiSymbols());
        }

        /**
         * <p>
         *     Configures custom {@link Symbols} for error messages.
         * </p>
         * <p>
         *     Whatever pleases the developer.
         * </p>
         *
         * @param symbolsBuilder <code>SymbolsConfig.Builder</code> preconfigured with custom symbols
         * @return <code>this</code> <code>Builder</code>
         */
        public Builder withSymbols(final SymbolsConfig.Builder symbolsBuilder) {
            return withSymbols(symbolsBuilder.build());
        }

        /**
         * <p>
         *     Configures custom {@link Symbols} for error messages.
         * </p>
         * <p>
         *     Whatever pleases the developer.
         * </p>
         *
         * @param symbols <code>SymbolsConfig</code> with custom symbols
         * @return <code>this</code> <code>Builder</code>
         */
        @SuppressWarnings("WeakerAccess")
        public Builder withSymbols(final SymbolsConfig symbols) {
            config.withSymbols(symbols);
            return this;
        }

        /**
         * <p>
         *     Configures custom {@link Stringifiers} for objects involved in a test.
         * </p>
         * <p>
         *     Stringifier functions supersede the <code>toString</code>-implementations of the respective objects.
         * </p>
         *
         * @param stringifiersBuilder <code>StringifiersConfig.Builder</code> preconfigured with custom stringifier mappings
         * @return <code>this</code> <code>Builder</code>
         */
        public Builder withStringifiers(final StringifiersConfig.Builder stringifiersBuilder){
            return withStringifiers(stringifiersBuilder.build());
        }

        /**
         * <p>
         *     Configures custom {@link Stringifiers} for objects involved in a test.
         * </p>
         * <p>
         *     Stringifier functions supersede the <code>toString</code>-implementations of the respective objects.
         * </p>
         *
         * @param stringifiers <code>StringifiersConfig</code> with custom stringifier mappings
         * @return <code>this</code> <code>Builder</code>
         */
        @SuppressWarnings("WeakerAccess")
        public Builder withStringifiers(final Stringifiers stringifiers) {
            this.config.withStringifiers(stringifiers);
            return this;
        }

        /**
         * <p>Switches on debug mode for all matchers created by the {@code MatcherFactory}.</p>
         *
         * @return <code>this</code> <code>Builder</code>
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

    /**
     * <p>
     * Returns a builder to configure a <code>MatcherFactory</code> with a fluent DSL.
     * </p>
     *
     * @return new <code>Builder</code>
     */
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
     * @param klass expected class of the actual object
     * @param <T> expected type of the actual object
     * @return FluentAttributeMatcher for an actual object
     */
    public <T> FluentAttributeMatcher<T> instanceOf(final Class<T> klass) {
       return FlamFactory.fluentAttributeMatcher(klass, config);
    }

    /**
     * <p>Factory method for a {@link FluentIterableMatcher}
     * to match an <i>actual</i> {@code Iterable}'s properties.</p>
     *
     * @param klass expected class of the actual {@code Iterable}
     * @param <X> expected type of the actual {@code Iterable}'s items
     * @param <C> expected type of the actual {@code Iterable}
     * @return FluentAttributeMatcher for an actual iterable
     */
    public <X, C extends Iterable<X>> FluentIterableMatcher<X, C> iterableOf(final Class<X> klass){
        return FlimFactory.fluentIterableMatcher(klass, config);
    }

    /**
     * <p>Factory method for a {@link FluentIterableMatcher}
     * to match an <i>actual</i> {@code Iterable}'s properties.</p>
     *
     * @param iterable for typping
     * @param <C> expected type of the actual {@code Iterable}
     * @return FluentAttributeMatcher for an actual iterable
     */
    public <X, C extends Iterable<X>> FluentIterableMatcher<X, C> iterableLike(final C iterable){
        return FlimFactory.fluentIterableMatcherLike(iterable, config);
    }

    /**
     * <p>Factory method for a {@link FluentMapMatcher}
     * to match an <i>actual</i> {@code Map}'s properties.</p>
     *
     * @param map for type inference
     * @param <K> Expected type of the actual {@code Map}'s keys.
     * @param <V> Expected type of the actual {@code Map}s values.
     * @return FluentMapMatcher.
     */
    public <K, V> FluentMapMatcher<K, V> aMapLike(final Map<K, V> map){
        return FlimFactory.aMapLike(map);
    }

    /**
     * <p>Factory method for a {@link FluentMapMatcher}
     * to match an <i>actual</i> {@code Map}'s properties.</p>
     *
     * @param map for type inference
     * @param <K> Expected type of the actual {@code Map}'s keys.
     * @param <V> Expected type of the actual {@code Map}s values.
     * @return FluentMapMatcher.
     */
    public <K, V> FluentMapMatcher<K, V> mapLike(final Map<K, V> map){
        return FlimFactory.aMapLike(map, config);
    }

}
