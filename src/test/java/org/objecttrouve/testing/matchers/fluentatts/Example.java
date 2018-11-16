/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.junit.Ignore;
import org.junit.Test;
import org.objecttrouve.testing.matchers.customization.MatcherFactory;
import org.objecttrouve.testing.matchers.customization.StringifiersConfig;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.customized;
import static org.objecttrouve.testing.matchers.customization.StringifiersConfig.stringifiers;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;

@SuppressWarnings("ALL")
@Ignore("Failing intentionally.")
public class Example {

    static class Result {
        private final String stringValue;
        private final int intValue;
        private final boolean boolValue;

        Result(final String stringValue, final int intValue, final boolean boolValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
            this.boolValue = boolValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public boolean isBoolValue() {
            return boolValue;
        }
    }

    static Result methodWithResult(final String s, final int i, final boolean b) {
        return new Result(s, i, b);
    }


    private static final Attribute<Result, Integer> intValue = attribute("intValue", Result::getIntValue);
    private static final Attribute<Result, Boolean> boolValue = attribute("booleanValue", Result::isBoolValue);
    private static final Attribute<String, String> substring = attribute("substring", s -> s.substring(0, 1));
    private static final Attribute<String, Integer> length = attribute("length", String::length);
    private static final Attribute<Result, String> stringValue = attribute("stringValue", Result::getStringValue);

    @Test
    public void test() {

        final Result result = methodWithResult("result", 1, false);

        assertThat(result, is(//
            a(Result.class)//
                .with(stringValue, a(String.class)
                    .with(length, 3)
                    .with(substring, a(String.class)
                        .with(length, 10))) //
                .with(intValue, lessThan(0)) //
                .with(boolValue, true)
        ));
    }

    @Test
    public void testSomething() {

        final Result result = methodWithResult("2=", 1, false);

        assertThat(result, is(//
            a(Result.class)//
                .with(stringValue, "1=") //
                .with(intValue, is(2)) //
                .with(boolValue, true)
        ));
    }

    @Test
    public void debugging__without_stringifier() {

        final Result result = methodWithResult("result", 1, false);

        assertThat(result, is(//
            a(Result.class).debugging()//
                .with(stringValue, a(String.class)
                    .with(length, 3)
                    .with(substring, a(String.class)
                        .with(length, 10))) //
                .with(intValue, lessThan(0)) //
                .with(boolValue, true)
        ));
    }

    @Test
    public void debugging__with_debug_stringifier() {

        StringifiersConfig.Builder stringifiers = stringifiers().withDebugStringifier(Result.class, r -> r.stringValue + "|" + r.boolValue + "|" + r.intValue);
        final MatcherFactory an = customized().withStringifiers(stringifiers).build();
        final Result result = methodWithResult("result", 1, false);

        assertThat(result, is(//
            an.instanceOf(Result.class).debugging()//
                .with(stringValue, a(String.class)
                    .with(length, 3)
                    .with(substring, a(String.class)
                        .with(length, 10))) //
                .with(intValue, lessThan(0)) //
                .with(boolValue, true)
        ));
    }

    @Test
    public void debugging__with_embedded_debug_stringifier() {

        StringifiersConfig.Builder stringifiers = stringifiers()
            .withDebugStringifier(Result.class, r -> r.stringValue + "|" + r.boolValue + "|" + r.intValue)
            .withDebugStringifier(String.class, s -> s.toUpperCase());
        final MatcherFactory an = customized().withStringifiers(stringifiers).build();
        final Result result = methodWithResult("result", 1, false);

        assertThat(result, is(//
            an.instanceOf(Result.class).debugging()//
                .with(stringValue, an.instanceOf(String.class).debugging()
                    .with(length, 3)
                    .with(substring, "fff")
                    .with(length, 10)) //
                .with(intValue, lessThan(0)) //
                .with(boolValue, true)
        ));
    }


    private static class Matryoshka {
        final String name;
        final Matryoshka child;

        private Matryoshka(final String name, final Matryoshka child) {
            this.name = name;
            this.child = child;
        }

        public String getName() {
            return name;
        }

        public Matryoshka getChild() {
            return child;
        }
    }

    private static class MultiMatryoshka extends Matryoshka {
        final List<Matryoshka> children;

        private MultiMatryoshka(final String name, final Matryoshka child, final Matryoshka... children) {
            super(name, child);
            this.children = asList(children);
        }

        public List<Matryoshka> getChildren() {
            return children;
        }
    }

    final Attribute<Matryoshka, Matryoshka> child = Attribute.attribute("child", m -> m.getChild());
    final Attribute<Matryoshka, String> name = Attribute.attribute("child", m -> m.getName());
    final Attribute<Matryoshka, List<Matryoshka>> children = Attribute.attribute("children", m -> ((MultiMatryoshka) m).getChildren());

    @Test
    public void matryoshkas() {
        final MatcherFactory an = customized().build();
        final MultiMatryoshka matryoshka = new MultiMatryoshka(
            "1", new Matryoshka(
            "2", new Matryoshka(
            "3", null)),
            new Matryoshka(
                "4", new Matryoshka(
                "5", new Matryoshka(
                "6", null))),
            new MultiMatryoshka(
                "7", new Matryoshka(
                "8", new Matryoshka(
                "9", null)),
                new Matryoshka(
                    "10", new Matryoshka(
                    "11", new Matryoshka(
                    "12", null))),
                new Matryoshka(
                    "13", new Matryoshka(
                    "14", new Matryoshka(
                    "14", null)))
            ),
            new Matryoshka(
                "16", new Matryoshka(
                "17", new Matryoshka(
                "18", null)))
        );

        assertThat(matryoshka, is(
            an.instanceOf(Matryoshka.class)
                .with(name, "m1")
                .with(child, an.instanceOf(Matryoshka.class)
                    .with(name, "m2")
                    .with(child, an.instanceOf(Matryoshka.class)
                        .with(name, "m3")
                    ))
                .with(children, an.iterableOf(Matryoshka.class)
                    .withItemsMatching(
                        an.instanceOf(Matryoshka.class)
                            .with(name, "m4"),
                        an.instanceOf(Matryoshka.class)
                            .with(children, an.iterableOf(Matryoshka.class)
                                .withItemsMatching(an.instanceOf(Matryoshka.class).with(name, "m20"))
                            )
                    ))
        ));
    }

    @Test
    public void matryoshkas__debugging() {
        final MatcherFactory an = customized()
            .debugging()
            .build();
        final MultiMatryoshka matryoshka = new MultiMatryoshka(
            "1", new Matryoshka(
            "2", new Matryoshka(
            "3", null)),
            new Matryoshka(
                "4", new Matryoshka(
                "5", new Matryoshka(
                "6", null))),
            new MultiMatryoshka(
                "7", new Matryoshka(
                "8", new Matryoshka(
                "9", null)),
                new Matryoshka(
                    "10", new Matryoshka(
                    "11", new Matryoshka(
                    "12", null))),
                new Matryoshka(
                    "13", new Matryoshka(
                    "14", new Matryoshka(
                    "14", null)))
            ),
            new Matryoshka(
                "16", new Matryoshka(
                "17", new Matryoshka(
                "18", null)))
        );

        assertThat(matryoshka, is(
            an.instanceOf(Matryoshka.class)
                .with(name, "m1")
                .with(child, an.instanceOf(Matryoshka.class)
                    .with(name, "m2")
                    .with(child, an.instanceOf(Matryoshka.class)
                        .with(name, "m3")
                    ))
                .with(children, an.iterableOf(Matryoshka.class)
                    .withItemsMatching(
                        an.instanceOf(Matryoshka.class)
                            .with(name, "m4"),
                        an.instanceOf(Matryoshka.class)
                            .with(children, an.iterableOf(Matryoshka.class)
                                .withItemsMatching(an.instanceOf(Matryoshka.class).with(name, "m20"))
                            )
                    ))
        ));
    }
}
