/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import java.util.function.Function;

/**
 * <p>
 * Defines an {@code Object} property as pair of a name and a getter function.
 * </p>
 * <p>
 * The getter retrieves the value of interest from the {@code Object}.</p>
 * <p>The name represents the the property in the mismatch description if the value is not as expected.
 * </p>
 */
public class Attribute<T, O> {
    private final String name;
    private final Function<T, O> getter;

    /**
     * <p>Factory method to create an {@code Attribute}.</p>
     *
     * @param name   A nice human-friendly name for what the getter is doing.
     * @param getter Function returning the value to be checked.
     * @param <T>    Type of the object on which the getter is called.
     * @param <O>    Type of the value returned by the getter.
     * @return Attribute.
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
