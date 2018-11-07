/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.api;

public interface Symbols {
    /**
     * Like {@link Symbols#getExpectedMatches()}, just with value expectations.
     * @return The symbol between the attribute description and the expected value.
     */
    String getExpectedEquals();

    /**
     * @return The symbol between the expected value and the actual value.
     */
    String getActualNotEquals();

    /**
     * Setter.
     * Like {@link Symbols#getExpectedEquals()}, just with {@code org.hamcrest.Matcher}  expectations.
     * @return The symbol between the attribute description and the matcher description of the expected value.
     */
    String getExpectedMatches();

    /**
     * @return Separator between nested attribute descriptions.
     */
    String getPointingNested();

    /**
     * @return Symbol to indicate that the item matched the expectation.
     */
    String getIterableItemMatchesSymbol();

    /**
     * @return Symbol to indicate that a {@code Matcher} failed on the item.
     */
    String getIterableItemNotMatchesSymbol();

    /**
     * @return Symbol to indicate that the item should appear elsewhere given the expected order of items.
     */
    String getIterableItemBadItemOrderSymbol();

    /**
     * @return Symbol to indicate that the item should appear elsewhere given the expected sort order.
     */
    String getIterableItemBadSortOrderSymbol();

    /**
     * @return Symbol to indicate that the item is duplicated.
     */
    String getIterableItemDuplicateSymbol();

    /**
     * @return Symbol to indicate that the item should not occur in the {@code Iterable} at all.
     */
    String getIterableItemUnwantedSymbol();

    /**
     * @return Right bracket to group objects.
     */
    String getRightBracket();

    /**
     * @return Left bracket to group objects.
     */
    String getLeftBracket();
}
