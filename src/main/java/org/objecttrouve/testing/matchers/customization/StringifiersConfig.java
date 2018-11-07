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
 * Mappings of classes to stringifying {@code java.util.function.Function}s.
 * Intended to provide useful string descriptions of an instance when the respective class doesn't have a suitable {@code Object#toString()} implementation.
 */
public class StringifiersConfig implements Stringifiers {


    /**
     * Builder to mutably define stringifier mappings.
     */
    public static class Builder {

        private Map<Class<?>, Function> shortStringifiers = new HashMap<>();
        private Map<Class<?>, Function> debugStringifiers = new HashMap<>();

        /**
         * Define a short stringifier for a class.
         * The value returned by the stringifier {@code Function} should not be (much) longer than 30 characters.
         * @param klass The class whose instances to stringify.
         * @param stringifier The function to stringify the class.
         * @return {@code this Builder}.
         */
        public <X> Builder withShortStringifier(final Class<X> klass, final Function<X, String> stringifier){
           shortStringifiers.put(klass, stringifier);
           return this;

        }

        /**
         * Define an exhaustive stringifier for a class.
         * The value returned by the stringifier {@code Function} should reveal all interesting properties of the class.
         * Used in debug mode.
         * @param klass The class whose instances to stringify.
         * @param stringifier The function to stringify the class.
         * @return {@code this Builder}.
         */
        @SuppressWarnings("WeakerAccess")
        public <X> Builder withDebugStringifier(final Class<X> klass, final Function<X, String> stringifier){
           debugStringifiers.put(klass, stringifier);
           return this;
       }

       /**
        * @return The finalized {@code Stringifiers}.
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
     * Factory method.
     * @return {@link Builder}.
     */
    public static Builder stringifiers(){
        return new Builder();
    }
    private StringifiersConfig(final Builder builder) {
        this.shortStringifiers = Collections.unmodifiableMap(builder.shortStringifiers);
        this.debugStringifiers = Collections.unmodifiableMap(builder.debugStringifiers);
    }

    /**
     * Short description {@code java.util.function.Function} for a class.
     * @param object the instance to stringify.
     */
    @Override
    public <X> Optional<Function<X, String>> getShortStringifier(final X object) {
        return get(this.shortStringifiers, object);
    }


    /**
     * Exhaustive description {@code java.util.function.Function} for a class.
     * @param object the instance to stringify.
     */
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
