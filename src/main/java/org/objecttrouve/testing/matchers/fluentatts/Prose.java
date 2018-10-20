/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("Convert2streamapi")
class Prose {

    static final String typeMatchExpectationDescription = "CHECK TEST FOR TYPE-SAFETY: Expected class";
    private static final String methodPathSeparator = "/";
    private static final String methodCallsSeparator = "&";
    private static final String eq = " = ";
    private static final String neq = " \u2260 ";
    private static final String matching = " \u2A73 ";
    private static final String unknown = "???";

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

    static void wording(final Description description, final Result result) {
        final Expectation expectation = result.getExpectation();
        final String getterDescription = expectation.getDescription();
        if (getterDescription != null){
            description.appendText(getterDescription);
        } else if (result.getCalled() != null) {
            description.appendText(describe(result.getCalled()));
        } else {
            description.appendText(expectation.getGetter().toString());
        }
        if (expectation.isAboutMatcher()) {
            describeMatcher(expectation, description);
        } else {
            final Object expVal = ((Expectation<?, ?>) expectation).getExpectedValue();
            description.appendText(eq);
            description.appendValue(expVal);
            description.appendText(neq);
            description.appendValue(result.getActual());
        }
    }

    private static void describeMatcher(final Expectation expectation, final Description description) {
        final Matcher m = expectation.getMatcher();
        if (m != null) {
            description.appendText(matching);
            m.describeTo(description);
        }
    }


    static <T>String typeMismatchMsg(final T target) {
        return "NOT " + target.getClass().getSimpleName() + " but what the type parameters define";
    }

    static <T> void expectation(final Description description, final Expectation<T, ?> e) {
        final String getterDescription = e.getDescription();
        if (getterDescription != null){
            description.appendText(getterDescription);
        }
        if (e.isAboutMatcher()) {
            describeMatcher(e, description);
        } else {
            final Object expVal = e.getExpectedValue();
            description.appendText(eq);
            description.appendValue(expVal);
        }
    }
}
