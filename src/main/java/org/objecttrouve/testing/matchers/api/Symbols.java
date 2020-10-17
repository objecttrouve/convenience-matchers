/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.api;

/**
 * <p>
 * Convenient matchers use lovely expressive symbols to highlight particular characteristics of a test result.
 * <br>
 * Such as <code>:two_hearts:</code> to indicate a match. Or <code>:broken_heart:</code> to indicate a mismatch.
 * </p>
 * <p>
 * Depending on the environment, these fancy symbols aren't always displayed nicely.
 * <br>
 * Some developers might even prefer mathematic notation over romantic.
 * </p>
 * <p>
 * Therefore the set of symbols can be customized.
 * <br>
 * The customized symbols are transported as an instance of <code>Symbols</code>.
 * </p>
 */
public interface Symbols {

    /**
     * <p>
     *     Gets a symbol with vaguely the semantics of "equals".
     * </p>
     * <p>
     *     Inserted between the description of an attribute and the <i>expected</i> value of that attribute.
     * </p>
     *
     * @return symbol between attribute description and the expected value
     */

    String getExpectedEquals();

    /**
     * <p>
     *     Gets a symbol with vaguely the semantics of "not equals".
     * </p>
     * <p>
     *     Inserted between the the <i>expected</i> value of an attribute and the <i>actual</i> value of that attribute.
     *     <br>
     *     Inserted between the <i>self</i> description of a matcher and the <i>mismatch</i> descripton of that same matcher.
     *  </p>
     *
     * @return symbol between (description of) expected and (description of) actual
     */
    String getActualNotEquals();

    /**
     * <p>
     *     Gets a symbol with vaguely the semantics of "matches".
     * </p>
     * <p>
     *     Inserted between the description of an attribute and the <i>self</i> description of a matcher provided for that attribute's value.
     * </p>
     *
     * @return symbol between attribute description and matcher self description
     */
    String getExpectedMatches();

    /**
     * <p>
     *     Gets a separator symbol to segregate nested attribute names.
     * </p>
     * @return separator between nested attribute descriptions
     */
    String getPointingNested();

    /**
     * <p>
     *     Gets a symbol that indicates an item matched all expectations.
     * </p>
     *
     * @return symbol to indicate a match
     */
    String getIterableItemMatchesSymbol();

    /**
     * <p>
     *     Gets a symbol that indicates an item didn't match all expectations.
     * </p>
     *
     * @return symbol to indicate a mismatch
     */
    String getIterableItemNotMatchesSymbol();

    /**
     * <p>
     *     Gets a symbol that indicates an item is not at the expected position, given a particular item order.
     * </p>
     *
     * @return symbol to indicate an unexpected order of items
     */
    String getIterableItemBadItemOrderSymbol();

    /**
     * <p>
     *     Gets a symbol that indicates an item is not at the expected position, given a particular sort order.
     * </p>
     *
     * @return symbol to indicate an unexpected sorting of items
     */
    String getIterableItemBadSortOrderSymbol();

    /**
     * <p>
     *     Gets a symbol that indicates an item is duplicated.
     * </p>
     *
     * @return symbol to highlight a duplicate
     */
    String getIterableItemDuplicateSymbol();


    /**
     * <p>
     *     Gets a symbol that indicates an item is unexpected and unwanted.
     * </p>
     *
     * @return symbol to highlight an unwanted item
     */
    String getIterableItemUnwantedSymbol();

    /**
     * <p>
     *     Gets a right bracket used to group descriptions.
     * </p>
     *
     * @return right bracket
     */
    String getRightBracket();

    /**
     * <p>
     *     Gets a left bracket used to group descriptions.
     * </p>
     *
     * @return left bracket
     */
    String getLeftBracket();
}
