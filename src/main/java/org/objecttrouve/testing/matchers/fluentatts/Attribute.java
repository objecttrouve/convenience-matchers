/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import java.util.function.Function;

/**
 * Defines an Object property as pair of a name and a getter function.
 */
public class Attribute<T, O> {
    private final String name;
    private final Function<T, O> getter;

    /**
     * Factory method.
     *
     * @param name   A nice human-friendly name for what the getter is doing
     * @param getter Function returning the value to be checked
     * @param <T>    type of the object on which the getter is called
     * @param <O>    type of the value returned by the getter
     * @return Attribute
     */
    @SuppressWarnings("WeakerAccess")
    public static <T, O> Attribute<T, O> attribute(final String name, final Function<T, O> getter) {
        return new Attribute<>(name, getter);
    }

    private Attribute(final String name, final Function<T, O> getter) {
        this.name = name;
        this.getter = getter;
    }

    String getName() {
        return name;
    }

    Function<T, O> getGetter() {
        return getter;
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "name='" + name + '\'' +
            ", getter=" + getter +
            '}';
    }
}
