/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.customization;

import org.objecttrouve.testing.matchers.api.Stringifiers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * <p>Mappings of classes to stringifying {@code java.util.function.Function}s.
 * <br>
 * Provides useful string descriptions
 * when the respective class doesn't have a suitable <code>toString</code>-implementation.</p>
 */
public class StringifiersConfig implements Stringifiers {


    /**
     * <p>Builder for stringifier mappings via a fluent DSL.</p>
     */
    public static class Builder {

        private Map<Class<?>, Function> shortStringifiers = new HashMap<>();
        private Map<Class<?>, Function> debugStringifiers = new HashMap<>();

        /**
         * <p>
         * Sets a short pretty-printer (stringifier) for a class.
         * <br>
         * The value returned by the <code>stringifier</code>stringifier <code>Function</code>
         * should not be (much) longer than 30 characters.
         * </p>
         *
         * @param <X> type of <code>klass</code> to stringify
         * @param klass class whose instances to stringify
         * @param stringifier function to stringify instances of <code>klass</code>
         * @return <code>this Builder</code>
         */
        public <X> Builder withShortStringifier(final Class<X> klass, final Function<X, String> stringifier){
           shortStringifiers.put(klass, stringifier);
           return this;

        }

        /**
         * <p>
         * Sets a short pretty-printer (stringifier) for a class.
         * <br>
         * The value returned by the stringifier <code>Function</code>
         * should reveal all interesting properties of the class.
         * Used in debug mode.
         * </p>
         *
         * @param <X> type of <code>klass</code> to stringify
         * @param klass class whose instances to stringify
         * @param stringifier function to stringify instances of <code>klass</code>
         * @return <code>this Builder</code>
         */
        @SuppressWarnings("WeakerAccess")
        public <X> Builder withDebugStringifier(final Class<X> klass, final Function<X, String> stringifier){
           debugStringifiers.put(klass, stringifier);
           return this;
       }

       /**
        * <p>Builds <code>Stringifiers</code> with the configured functions that pretty-print strings.</p>
        * @return custom stringifiers
        */
       public Stringifiers build(){
           final StringifiersConfig stringifiersConfig = new StringifiersConfig(this);
           this.shortStringifiers = new HashMap<>();
           this.debugStringifiers = new HashMap<>();
           return stringifiersConfig;
       }

    }

    private final Map<Class<?>, Function> shortStringifiers;
    private final Map<Class<?>, Function> debugStringifiers;

    /**
     * <p>Returns a stringifier mappings builder to configure pretty-printers with a fluent DSL.</p>
     *
     * @return <code>this</code> <code>Builder</code>
     */
    public static Builder stringifiers(){
        return new Builder();
    }
    private StringifiersConfig(final Builder builder) {
        this.shortStringifiers = Collections.unmodifiableMap(builder.shortStringifiers);
        this.debugStringifiers = Collections.unmodifiableMap(builder.debugStringifiers);
    }

    @Override
    public <X> Optional<Function<X, String>> getShortStringifier(final X object) {
        return get(this.shortStringifiers, object);
    }

    @Override
    public <X> Optional<Function<X, String>> getDebugStringifier(final X object) {
        return get(this.debugStringifiers, object);
    }


    private<X> Optional<Function<X, String>> get(final Map<Class<?>, Function> map, final X object) {
        //noinspection unchecked
        return Optional.ofNullable((Function<X, String>) lookup(map, object));
    }

    private Function lookup(final Map<Class<?>, Function> map, final Object object) {
        if (object == null) {
            return null;
        }
        return lookup(map, object.getClass());
    }

    private Function lookup(final Map<Class<?>, Function> map, final Class<?> klass) {
        final Function function = map.get(klass);
        if (function != null){
            return function;
        }
        final Class[] interfaces = klass.getInterfaces();
        for (final Class interFace : interfaces) {
            final Function f = lookup(map, interFace);
            if (f != null) {
                return f;
            }
        }
        final Class superclass = klass.getSuperclass();
        if (superclass != null) {
            final Function f = lookup(map, superclass);
            if (f != null) {
                return f;
            }
        }
        return null;
    }
}
