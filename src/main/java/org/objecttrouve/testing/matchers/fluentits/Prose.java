/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.objecttrouve.testing.matchers.api.Stringifiers;
import org.objecttrouve.testing.matchers.api.Symbols;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

class Prose<X> {

    private static final int actualItemMaxLength = 30;
    private static final String iterable = Iterable.class.getSimpleName();
    private final Symbols symbols;
    private final Stringifiers stringifiers;

    Prose(final Symbols symbols, final Stringifiers stringifiers) {
        this.symbols = symbols;
        this.stringifiers = stringifiers;
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

    String actualItemString(final String actual, final int limit) {
        return limit > 0 ? format("%1$-" + limit + "." + limit + "s", linify(Objects.toString(actual))) : "";
    }

    private String linify(final String str) {
        return str.trim().replaceAll("\n", "; ").replaceAll("\\s+", " ");
    }

    String matcherSaying(final String self) {
        return linify(self);
    }

    void describe(final Stream<Finding> findings, final List<ItemResult> itemResults, final Description mismatchDescription) {
        final List<SelfDescribing> fs = findings
            .map(Finding::getDescription)
            .map(s -> (SelfDescribing) description1 -> description1.appendValue(s))
            .collect(toList());
        mismatchDescription.appendList("\nFindings:\n", "\n", "\n", fs);
        final Map<ItemResult, String> stringifiedActuals = new HashMap<>();
        for (final ItemResult itemResult : itemResults) {
            stringifiedActuals.put(itemResult, stringifiers.getShortStringifier(itemResult.getActual()).orElse(Objects::toString).apply(itemResult.getActual()));
        }
        final int longestActual = stringifiedActuals.values().stream().mapToInt(String::length).max().orElse(1);
        mismatchDescription.appendText("\n");
        //noinspection unchecked
        final Stream<String> stringStream = itemResults.stream()
            .map(result -> line(result, itemResults.size(), longestActual, stringifiedActuals.get(result)));
        final String line = stringStream
            .collect(Collectors.joining("\n"));
        mismatchDescription.appendText(line);
        mismatchDescription.appendText("\n\n");
    }

    String line(final ItemResult<X> result, final int collectionLength, final int longestActual, final String stringifiedActual) {
        final String lBrack = symbols.getLeftBracket();
        final String rBrack = symbols.getRightBracket();
        final StringBuilder line = new StringBuilder();
        final int digits = Double.valueOf(Math.log10(collectionLength)).intValue() + 1;
        final String ix = format("%1$" + digits + "." + digits + "s", result.getIndex());
        line.append(lBrack).append(ix).append(rBrack);
        line.append(lBrack).append(
            actualItemString(stringifiedActual, Math.min(longestActual, actualItemMaxLength))
        ).append(rBrack);
        appendSymbol(line, result.isMatched(), symbols.getIterableItemMatchesSymbol());
        appendSymbol(line, result.isBreakingSortOrder(), symbols.getIterableItemBadSortOrderSymbol());
        appendSymbol(line, result.isBreakingItemOrder(), symbols.getIterableItemBadItemOrderSymbol());
        appendSymbol(line, result.isDuplicate(), symbols.getIterableItemDuplicateSymbol());
        appendSymbol(line, result.isUnwanted(), symbols.getIterableItemUnwantedSymbol());
        if (!result.isMatched()) {
            result.getMismatchedItemMatchers().forEach(matcherWithIndex -> {
                line.append(" ").append(symbols.getIterableItemNotMatchesSymbol()).append(lBrack).append(matcherWithIndex.getIndex()).append(rBrack).append(lBrack);
                final StringDescription selfDescription = new StringDescription();
                matcherWithIndex.getMatcher().describeTo(selfDescription);
                line.append(matcherSaying(selfDescription.toString()));
                line.append(rBrack);
            });
        }
        return line.toString();
    }

    private void appendSymbol(final StringBuilder line, final boolean pred, final String symbol) {
        line.append(format("%1$-2.2s", (pred ? symbol : " ")));


    }

}
