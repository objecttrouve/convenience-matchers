/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import org.hamcrest.StringDescription;

import java.util.Objects;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

class Prose<X> {

    private static final String match = "\uD83D\uDC95";
    private static final String mismatch = "\uD83D\uDC94";

    private static final String breakingSortOrder = "\u2195";
    private static final String breakingItemOrder = "\u2194";
    private static final String duplicate = "\uD83D\uDC6F";
    private static final String obsolete = "\uD83D\uDEAF";
    private static final int actualItemMaxLength = 30;
    private static final String iterable = Iterable.class.getSimpleName();

    Prose() {
    }

    void describeExpectations(final Settings settings, final Consumer<String> description) {
        description.accept("an " + iterable + " with the following properties:\n");
        description.accept("- "+ iterable + " of " + ofNullable(settings.klass).map(Class::getSimpleName).orElse(Object.class.getSimpleName()) + "\n");
        if (settings.expectedSize >= 0) {
            description.accept("- exactly " + settings.expectedSize + " item(s)\n");
        }
        if (settings.expectations.length > 0) {
            description.accept("- at least " + settings.expectations.length + " matching item(s)\n");
        }
        if (settings.mustNotHaveUnexpectedItems) {
            description.accept("- no unexpected items\n");
        }
        if (settings.sorted) {
            description.accept("- sorted\n");
        }
        if (settings.ordered) {
            description.accept("- ordered\n");
        }
        if (settings.unique) {
            description.accept("- no duplicates\n");
        }
        description.accept("\n");
    }

    String actualItemString(final X actual, final int limit) {
        return format("%1$-" + limit + "." + limit + "s", Objects.toString(actual).replaceAll("\n", " "));
    }

    String matcherSaying(final String self, final String mismatch) {
        return self.replaceAll("\n", " ");
    }

    String line(final ItemResult<X> result, final int collectionLength, final int longestActual) {
        final StringBuilder line = new StringBuilder();
        final int digits = Double.valueOf(Math.log10(collectionLength)).intValue() + 1;
        final String ix = format("%1$" + digits + "." + digits + "s", result.getIndex());
        line.append("[").append(ix).append("]");
        final X actual = result.getActual();
        line.append("[").append(actualItemString(actual, Math.min(longestActual, actualItemMaxLength))).append("]");
        appendSymbol(line, result.isMatched(), match);
        appendSymbol(line, result.isBreakingSortOrder(), breakingSortOrder);
        appendSymbol(line, result.isBreakingItemOrder(), breakingItemOrder);
        appendSymbol(line, result.isDuplicate(), duplicate);
        appendSymbol(line, result.isUnwanted(), obsolete);
        if (!result.isMatched()) {
            result.getMismatchedItemMatchers().forEach(m -> {
                m.matches(result.getActual());
                line.append(" " + mismatch + "[");
                final StringDescription selfDescription = new StringDescription();
                m.describeTo(selfDescription);
                line.append(matcherSaying(selfDescription.toString(), ""));
                line.append("]");
            });

        }

        return line.toString();
    }

    private void appendSymbol(final StringBuilder line, final boolean pred, final String symbol) {
        line.append(format("%1$-2.2s", (pred ? symbol : " ")));


    }

}
