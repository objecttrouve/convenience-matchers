/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.customization;

import org.junit.Test;
import org.objecttrouve.testing.matchers.api.Stringifiers;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

@SuppressWarnings({"StringOperationCanBeSimplified", "OptionalGetWithoutIsPresent"})
public class StringifiersConfigTest {

    @Test
    public void getShortStringifier__nothing_configured(){

        final Stringifiers stringifiers = StringifiersConfig.stringifiers().build();

        final Optional<Function<Object, String>> stringifier = stringifiers.getShortStringifier(new Object());

        assertFalse(stringifier.isPresent());
    }

    @Test
    public void getDebugStringifier__nothing_configured(){

        final Stringifiers stringifiers = StringifiersConfig.stringifiers().build();

        final Optional<Function<Object, String>> stringifier = stringifiers.getDebugStringifier(new Object());

        assertFalse(stringifier.isPresent());
    }

    @Test
    public void getShortStringifier__finds__by_class(){

        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .build();

        final Optional<Function<String, String>> stringifier = stringifiers.getShortStringifier(new String());

        final String result = stringifier.get().apply("four");
        assertThat(result, is("4"));
    }

    @Test
    public void getDebugStringifier__finds__by_class(){

        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withDebugStringifier(String.class, s -> s + s.length())
            .build();

        final Optional<Function<String, String>> stringifier = stringifiers.getDebugStringifier(new String());

        final String result = stringifier.get().apply("four");
        assertThat(result, is("four4"));
    }

    @Test
    public void getShortStringifier__finds__by_class__despite_distractors(){

        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withShortStringifier(Integer.class, String::valueOf)
            .withDebugStringifier(String.class, s -> s + s.length())
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .withShortStringifier(List.class, String::valueOf)
            .build();

        final Optional<Function<String, String>> stringifier = stringifiers.getShortStringifier(new String());

        final String result = stringifier.get().apply("four");
        assertThat(result, is("4"));
    }

    @Test
    public void getDebugStringifier__finds__by_class__despite_distractors(){

        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withDebugStringifier(Integer.class, String::valueOf)
            .withDebugStringifier(List.class, String::valueOf)
            .withDebugStringifier(String.class, s -> s + s.length())
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .build();

        final Optional<Function<String, String>> stringifier = stringifiers.getDebugStringifier(new String());

        final String result = stringifier.get().apply("four");
        assertThat(result, is("four4"));
    }


    private interface LifeForm {}
    private interface Organic {}
    private interface Yummi {}
    private static class Animal implements LifeForm, Organic {}
    private static class Mammal extends Animal {}
    private static class Whale extends Mammal {}
    private static class Fish extends Animal{}
    private static class Salmon extends Fish implements Yummi{}

    @Test
    public void getShortStringifier__finds__by_superclass(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withShortStringifier(Mammal.class, x -> Mammal.class.getSimpleName().toLowerCase())
            .build();

        final Optional<Function<Mammal, String>> stringifier = stringifiers.getShortStringifier(new Whale());

        final String result = stringifier.get().apply(new Whale());
        assertThat(result, is("mammal"));
    }

    @Test
    public void getDebugStringifier__finds__by_superclass(){

        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withDebugStringifier(Mammal.class, x -> Mammal.class.getSimpleName().toUpperCase())
            .build();

        final Optional<Function<Mammal, String>> stringifier = stringifiers.getDebugStringifier(new Whale());

        final String result = stringifier.get().apply(new Whale());
        assertThat(result, is("MAMMAL"));
    }

    @Test
    public void getShortStringifier__finds__by_interface(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withShortStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toLowerCase())
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getShortStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("yummi"));
    }

    @Test
    public void getDebugStringifier__finds__by_interface(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withDebugStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toUpperCase())
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getDebugStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("YUMMI"));
    }

    @Test
    public void getShortStringifier__finds__by_super_interface(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withShortStringifier(Organic.class, x -> Organic.class.getSimpleName().toLowerCase())
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getShortStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("organic"));
    }

    @Test
    public void getDebugStringifier__finds__by_super_interface(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withDebugStringifier(Organic.class, x -> Organic.class.getSimpleName().toUpperCase())
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getDebugStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("ORGANIC"));
    }

    @Test
    public void getShortStringifier__finds__by_closest_interface__despite_distractors(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withShortStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toLowerCase())
            .withShortStringifier(Fish.class, x -> Fish.class.getSimpleName().toLowerCase())
            .withShortStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toLowerCase())
            .withShortStringifier(Organic.class, x -> Organic.class.getSimpleName().toLowerCase())
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getShortStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("yummi"));
    }

    @Test
    public void getDebugStringifier__finds__by_closest_interface__despite_distractors(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withDebugStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Fish.class, x -> Fish.class.getSimpleName().toUpperCase())
            .withDebugStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Organic.class, x -> Organic.class.getSimpleName().toUpperCase())
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getDebugStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("YUMMI"));
    }


    @Test
    public void getShortStringifier__finds__target__despite_many_distractors__01(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withShortStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toLowerCase())
            .withShortStringifier(Fish.class, x -> Fish.class.getSimpleName().toLowerCase())
            .withShortStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toLowerCase())
            .withShortStringifier(Organic.class, x -> Organic.class.getSimpleName().toLowerCase())
            .withShortStringifier(Integer.class, String::valueOf)
            .withDebugStringifier(String.class, s -> s + s.length())
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .withShortStringifier(List.class, String::valueOf)
            .withDebugStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Fish.class, x -> Fish.class.getSimpleName().toUpperCase())
            .withDebugStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Organic.class, x -> Organic.class.getSimpleName().toUpperCase())
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getShortStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("yummi"));
    }


    @Test
    public void getShortStringifier__finds__target__despite_many_distractors__02(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withShortStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toLowerCase())
            .withShortStringifier(Fish.class, x -> Fish.class.getSimpleName().toLowerCase())
            .withShortStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toLowerCase())
            .withShortStringifier(Organic.class, x -> Organic.class.getSimpleName().toLowerCase())
            .withShortStringifier(Salmon.class, x -> Salmon.class.getSimpleName().toLowerCase())
            .withShortStringifier(Integer.class, String::valueOf)
            .withDebugStringifier(String.class, s -> s + s.length())
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .withShortStringifier(List.class, String::valueOf)
            .withDebugStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Fish.class, x -> Fish.class.getSimpleName().toUpperCase())
            .withDebugStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Organic.class, x -> Organic.class.getSimpleName().toUpperCase())
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getShortStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("salmon"));
    }


    @Test
    public void getShortStringifier__finds__target__despite_many_distractors__03(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withShortStringifier(Fish.class, x -> Fish.class.getSimpleName().toLowerCase())
            .withShortStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toLowerCase())
            .withShortStringifier(Organic.class, x -> Organic.class.getSimpleName().toLowerCase())
            .withShortStringifier(Integer.class, String::valueOf)
            .withDebugStringifier(String.class, s -> s + s.length())
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .withShortStringifier(List.class, String::valueOf)
            .withDebugStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Fish.class, x -> Fish.class.getSimpleName().toUpperCase())
            .withDebugStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Organic.class, x -> Organic.class.getSimpleName().toUpperCase())
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getShortStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("fish"));
    }

    @Test
    public void getShortStringifier__finds__target__despite_many_distractors__04(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withShortStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toLowerCase())
            .withShortStringifier(Organic.class, x -> Organic.class.getSimpleName().toLowerCase())
            .withShortStringifier(Integer.class, String::valueOf)
            .withDebugStringifier(String.class, s -> s + s.length())
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .withShortStringifier(List.class, String::valueOf)
            .withDebugStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Fish.class, x -> Fish.class.getSimpleName().toUpperCase())
            .withDebugStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Organic.class, x -> Organic.class.getSimpleName().toUpperCase())
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getShortStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("lifeform"));
    }

    @Test
    public void getDebugStringifier__finds__target__despite_many_distractors__01(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withDebugStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Salmon.class, x -> Salmon.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Fish.class, x -> Fish.class.getSimpleName().toUpperCase())
            .withDebugStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Organic.class, x -> Organic.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Integer.class, String::valueOf)
            .withDebugStringifier(List.class, String::valueOf)
            .withDebugStringifier(String.class, s -> s + s.length())
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .withShortStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toLowerCase())
            .withShortStringifier(Fish.class, x -> Fish.class.getSimpleName().toLowerCase())
            .withShortStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toLowerCase())
            .withShortStringifier(Organic.class, x -> Organic.class.getSimpleName().toLowerCase())
            .withShortStringifier(Integer.class, String::valueOf)
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getDebugStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("SALMON"));
    }

    @Test
    public void getDebugStringifier__finds__target__despite_many_distractors__02(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withDebugStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Fish.class, x -> Fish.class.getSimpleName().toUpperCase())
            .withDebugStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Organic.class, x -> Organic.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Integer.class, String::valueOf)
            .withDebugStringifier(List.class, String::valueOf)
            .withDebugStringifier(String.class, s -> s + s.length())
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .withShortStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toLowerCase())
            .withShortStringifier(Fish.class, x -> Fish.class.getSimpleName().toLowerCase())
            .withShortStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toLowerCase())
            .withShortStringifier(Organic.class, x -> Organic.class.getSimpleName().toLowerCase())
            .withShortStringifier(Integer.class, String::valueOf)
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getDebugStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("YUMMI"));
    }

    @Test
    public void getDebugStringifier__finds__target__despite_many_distractors__03(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withDebugStringifier(Animal.class, x -> Animal.class.getSimpleName().toUpperCase())
            .withDebugStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Organic.class, x -> Organic.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Integer.class, String::valueOf)
            .withDebugStringifier(List.class, String::valueOf)
            .withDebugStringifier(String.class, s -> s + s.length())
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .withShortStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toLowerCase())
            .withShortStringifier(Fish.class, x -> Fish.class.getSimpleName().toLowerCase())
            .withShortStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toLowerCase())
            .withShortStringifier(Organic.class, x -> Organic.class.getSimpleName().toLowerCase())
            .withShortStringifier(Integer.class, String::valueOf)
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getDebugStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("ANIMAL"));
    }

    @Test
    public void getDebugStringifier__finds__target__despite_many_distractors__04(){
        final Stringifiers stringifiers = StringifiersConfig.stringifiers()
            .withDebugStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Organic.class, x -> Organic.class.getSimpleName().toUpperCase())
            .withDebugStringifier(Integer.class, String::valueOf)
            .withDebugStringifier(List.class, String::valueOf)
            .withDebugStringifier(String.class, s -> s + s.length())
            .withShortStringifier(String.class, s -> String.valueOf(s.length()))
            .withShortStringifier(Yummi.class, x -> Yummi.class.getSimpleName().toLowerCase())
            .withShortStringifier(Fish.class, x -> Fish.class.getSimpleName().toLowerCase())
            .withShortStringifier(LifeForm.class, x -> LifeForm.class.getSimpleName().toLowerCase())
            .withShortStringifier(Organic.class, x -> Organic.class.getSimpleName().toLowerCase())
            .withShortStringifier(Integer.class, String::valueOf)
            .build();

        final Optional<Function<Salmon, String>> stringifier = stringifiers.getDebugStringifier(new Salmon());

        final String result = stringifier.get().apply(new Salmon());
        assertThat(result, is("LIFEFORM"));
    }
}