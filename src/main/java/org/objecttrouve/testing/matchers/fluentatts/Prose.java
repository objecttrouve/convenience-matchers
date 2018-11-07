/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.objecttrouve.testing.matchers.api.Stringifiers;
import org.objecttrouve.testing.matchers.api.Symbols;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("Convert2streamapi")
class Prose {

    static final String typeMatchExpectationDescription = "CHECK TEST FOR TYPE-SAFETY: Expected class";
    private static final String methodPathSeparator = "/";
    private static final String methodCallsSeparator = "&";
    private static final String unknown = "???";
    private final Symbols symbols;
    private final Stringifiers stringifiers;

    Prose(final Symbols symbols, final Stringifiers stringifiers) {
        this.symbols = symbols;
        this.stringifiers = stringifiers;
    }

    private static String describe(final TrackingTree calls) {
        return describeAll(calls).stream().collect(Collectors.joining(methodCallsSeparator));
    }

    private static List<String> describeAll(final TrackingTree calls) {
        if (calls.tracked().size() == 0) {
            return new LinkedList<>();
        }
        final List<String> result = new LinkedList<>();
        final Set<String> alreadyAdded = new HashSet<>();
        if (!calls.getArray().isEmpty()) {
            for (final TrackingTree arrayCall : calls.getArray()) {
                final List<String> suffixes = describeAll(arrayCall);
                //noinspection ConstantConditions
                final String methodName = arrayCall.method().   get().getName();
                if (suffixes.isEmpty()) {
                    result.add(methodName);
                } else {
                    final String tracked = methodName + "[...]";
                    addWithSuffixes(tracked, result, suffixes);
                }
                alreadyAdded.add(methodName);
            }
        }
        calls.tracked().forEach(tt -> {
            final List<String> suffixes = describeAll(tt);
            final String tracked = tt.error()//
                    .map(err -> unknown)//
                    .orElse(//
                            tt.method()//
                                    .map(Method::getName)//
                                    .orElse(unknown));
            if (suffixes.isEmpty()) {
                if (!alreadyAdded.contains(tracked)) {
                    result.add(tracked);
                }
            }
            addWithSuffixes(tracked, result, suffixes);
        });
        return result;
    }

    private static void addWithSuffixes(final String tracked, final List<String> result, final List<String> suffixes) {
        for (final String s : suffixes) {
            result.add(tracked + methodPathSeparator + s);
        }
    }

    static <T>String typeMismatchMsg(final T target) {
        return "NOT " + target.getClass().getSimpleName() + " but what the type parameters define";
    }

    Stream<String> prefixTail(final Result result, final Stream<String> tail) {
        return prefixTail(tail, getKey(result));
    }

    Stream<String> prefixTail(final Expectation expectation, final Stream<String> tail) {
        return prefixTail(tail, getKey(expectation));
    }

   private  Stream<String> prefixTail(final Stream<String> tail, final String key) {
        return tail.map(s -> key +symbols.getPointingNested()+ s);
    }

    String matcherMismatch(final Result result){
        final StringDescription description = new StringDescription();
        final Matcher matcher = result.getExpectation().getMatcher();
        final String key = getKey(result);
        final String self = matcherSelf(key, matcher);
        description.appendText(self);
        description.appendText(symbols.getActualNotEquals());
        description.appendText("'");
        matcher.describeMismatch(result.getActual(), description);
        description.appendText("'");
        return description.toString();
    }

    private String stringify(final Object actual) {
        final Optional<Function<Object, String>> shortStringifier = stringifiers.getShortStringifier(actual);
        return shortStringifier.map(stringifier -> stringifier.apply(actual)).orElseGet(() -> Objects.toString(actual));
    }

    String matcherExpectation(final Expectation expectation){
        return matcherSelf(getKey(expectation), expectation.getMatcher());
    }

    private static String getKey(final Expectation expectation) {
        return Optional.ofNullable(expectation.getDescription()).orElse(unknown);
    }

    private String matcherSelf(final String key, final Matcher matcher) {
        final StringDescription description = new StringDescription();
        description.appendText(key);
        description.appendText(symbols.getExpectedMatches());
        description.appendText("'");
        matcher.describeTo(description);
        description.appendText("'");
        return description.toString();
    }

    private static String getKey(final Result result) {
        final Expectation expectation = result.getExpectation();
        final String getterDescription = expectation.getDescription();
        final Function getter = expectation.getGetter();
        if (getterDescription != null){
            return getterDescription;
        } else if (result.getCalled() != null) {
            return describe(result.getCalled());
        } else {
            return getter.toString();
        }
    }

    String valueMismatch(final Result result) {
        return valueExpectation(getKey(result), result.getExpectation()) + symbols.getActualNotEquals() + "'" + stringify(result.getActual()) + "'";
    }

    private String valueExpectation(final String key, final Expectation expectation) {
        return key + symbols.getExpectedEquals() + "'" + stringify(expectation.getExpectedValue()) + "'";
    }

    String valueExpectation(final Expectation expectation){
        return valueExpectation(getKey(expectation), expectation);
    }

    static void join(final List<Stream<String>> mismatches, final Description mismatchDescription) {
       mismatches.forEach(mism -> {
           mismatchDescription.appendText("\n\t");
           mismatchDescription.appendText(mism.collect(Collectors.joining()));
       });
       mismatchDescription.appendText("\n");
    }
}
