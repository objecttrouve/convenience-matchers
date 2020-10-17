/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.customization;

import org.objecttrouve.testing.matchers.api.Symbols;

/**
 * Symbols used in error messages when a matcher doesn't match.
 */
public class SymbolsConfig implements Symbols {

    /**
     * <p>The default set of symbols used in error descriptions.
     * Favoring fanciness over nice rendering in ancient shells.</p>
     *
     * @return <code>Symbols</code> with telling signs and revealing emojis
     */
    public static SymbolsConfig defaultSymbols() {
        return defaultSymbolsBuilder()
            .build();
    }

    /**
     * <p>Builder preconfigured with the default set of symbols used in error descriptions.
     * Favoring fanciness over nice rendering in ancient shells.</p>
     *
     * @return <code>Symbols.Builder</code> preconfigured with telling signs and revealing emojis.
     */
    @SuppressWarnings("WeakerAccess")
    public static Builder defaultSymbolsBuilder() {
        return symbols()
            .withExpectedEquals(" = ")
            .withActualNotEquals(" \u2260 ")
            .withExpectedMatches(" \u2A73 ")
            .withPointingNested(" ▶ ")
            .withIterableItemMatches("\uD83D\uDC95")
            .withIterableItemNotMatches("\uD83D\uDC94")
            .withIterableItemBadItemOrder("\u2194")
            .withIterableItemBadSortOrder("\u2195")
            .withIterableItemDuplicate("\uD83D\uDC6F")
            .withIterableItemUnwanted("\uD83D\uDEAF")
            .withBrackets( "⦗", "⦘");
    }

    /**
     * <p>Alternative symbols for error descriptions, ASCII only,
     * for environments in which emojis don't print so well.</p>
     *
     * @return <code>Symbols</code> with robust signs
     */
    @SuppressWarnings("WeakerAccess")
    public static SymbolsConfig asciiSymbols() {
        return asciiSymbolsBuilder()
            .build();
    }
    /**
     * <p>Builder preconfigured with alternative symbols for error descriptions, ASCII only,
     * for environments in which emojis don't print so well.</p>
     *
     * @return <code>Symbols.Builder</code> preconfigured with robust signs
     */
    @SuppressWarnings("WeakerAccess")
    public static Builder asciiSymbolsBuilder() {
        return symbols()
            .withExpectedEquals(" = ")
            .withActualNotEquals(" != ")
            .withExpectedMatches(" =~ ")
            .withPointingNested(" >> ")
            .withIterableItemMatches("OK")
            .withIterableItemNotMatches("FAIL")
            .withIterableItemBadItemOrder("<>")
            .withIterableItemBadSortOrder("^v")
            .withIterableItemDuplicate("2+")
            .withIterableItemUnwanted("--")
            .withBrackets("[", "]");
    }

    /**
     * <p>Builder for the set of sambols to be used in error messages.</p>
     */
    public static class Builder {
        
        private String expEquals;
        private String actNotEquals;
        private String expMatches;
        private String pointNested;
        private String itemInIterableMatches;
        private String itemInIterableNotMatches;
        private String itemInIterableBadItemOrder;
        private String itemInIterableBadSortOrder;
        private String itemInIterableDuplicate;
        private String itemInIterableUnwanted;
        private String rightBracket;
        private String leftBracket;

        /**
         * <p>Setter.</p>
         * <p>Like {@link Builder#withExpectedMatches(java.lang.String)}, just with value expectations.</p>
         *
         * @param symbolExpectedEquals symbol between the attribute description and the expected value
         * @return this <code>Builder</code>
         */
        public Builder withExpectedEquals(final String symbolExpectedEquals) {
            this.expEquals = symbolExpectedEquals;
            return this;
        }

        /**
         * <p>Setter for the symbol that indicates a mismatch.</p>
         *
         * @param actNotEquals The symbol between the expected value and the actual value
         * @return this <code>Builder</code>
         */
        public Builder withActualNotEquals(final String actNotEquals) {
            this.actNotEquals = actNotEquals;
            return this;
        }

        /**
         * <p>Setter.</p>
         * <p>Like {@link Builder#withExpectedEquals(java.lang.String)}, just with {@code org.hamcrest.Matcher} expectations.</p>
         *
         * @param expMatches symbol between the attribute description and the matcher description of the expected value
         * @return this <code>Builder</code>
         */
        public Builder withExpectedMatches(final String expMatches) {
            this.expMatches = expMatches;
            return this;
        }

        /**
         * <p>Setter for the separator between attribute path descriptions.</p>
         *
         * @param pointerToNested separator between nested attribute descriptions.
         * @return this <code>Builder</code>
         */
        public Builder withPointingNested(final String pointerToNested) {
            this.pointNested = pointerToNested;
            return this;
        }

        /**
         * <p>Sets the symbol that indicates an iterable item matches the expectation at the given position.</p>
         * <p>The arg may be truncated in the output.</p>
         *
         * @param itemInIterableMatches match indicatio
         * @return this <code>Builder</code>
         */
        public Builder withIterableItemMatches(final String itemInIterableMatches) {
            this.itemInIterableMatches = itemInIterableMatches;
            return this;
        }

        /**
         * <p>Sets the symbol that indicates a mismatch between a iterable item and the expected item at the same position.</p>
         *
         * @param itemInIterableNotMatches mismatch indicator
         * @return this <code>Builder</code>
         */
        public Builder withIterableItemNotMatches(final String itemInIterableNotMatches) {
            this.itemInIterableNotMatches = itemInIterableNotMatches;
            return this;
        }

        /**
         * <p>Sets the symbol that indicates an unexpected order of iterable items.</p>
         * <p>The arg may be truncated in the output.</p>
         *
         * @param itemInIterableBadItemOrder broken item order indicator
         * @return this <code>Builder</code>
         */
        public Builder withIterableItemBadItemOrder(final String itemInIterableBadItemOrder) {
            this.itemInIterableBadItemOrder = itemInIterableBadItemOrder;
            return this;
        }

        /**
         * <p>Sets the symbol that indicates an unexpected sorting of iterable items.</p>
         * <p>The arg may be truncated in the output.</p>
         *
         * @param itemInIterableBadSortOrder broken sort order indicator
         * @return this <code>Builder</code>
         */
        public Builder withIterableItemBadSortOrder(final String itemInIterableBadSortOrder) {
            this.itemInIterableBadSortOrder = itemInIterableBadSortOrder;
            return this;
        }

        /**
         * <p>Sets the duplicate indicator symbol.</p>
         * <p>The arg may be truncated in the output.</p>
         *
         * @param itemInIterableDuplicate duplicate indicator
         * @return this <code>Builder</code>
         */
        public Builder withIterableItemDuplicate(final String itemInIterableDuplicate) {
            this.itemInIterableDuplicate = itemInIterableDuplicate;
            return this;
        }

        /**
         * <p>Sets the symbol that indicates an item that is not expected to be in the iterable at all.</p>
         * <p>The arg may be truncated in the output.</p>
         * @param itemInIterableUnwanted unwanted item indicator
         * @return this <code>Builder</code>
         */
        public Builder withIterableItemUnwanted(final String itemInIterableUnwanted) {
            this.itemInIterableUnwanted = itemInIterableUnwanted;
            return this;
        }

        /**
         * <p>Setter for bracket symbols.</p>
         *
         * @param leftBracket left bracket used to group objects
         * @param rightBracket right bracket used to group objects
         * @return this <code>Builder</code>
         */
        public Builder withBrackets(final String leftBracket, final String rightBracket) {
            this.rightBracket = rightBracket;
            this.leftBracket = leftBracket;
            return this;
        }

        /**
         * <p>Builds the {@code Symbols} with the configured settings.</p>
         * @return the final <code>Symbols</code>
         */
        public SymbolsConfig build() {
            return new SymbolsConfig(this);
        }
    }
    
    private final String expEquals;
    private final String actNotEquals;
    private final String expMatches;
    private final String pointNested;
    private final String itemInIterableMatches;
    private final String itemInIterableNotMatches;
    private final String itemInIterableBadItemOrder;
    private final String itemInIterableBadSortOrder;
    private final String itemInIterableDuplicate;
    private final String itemInIterableUnwanted;
    private final String rightBracket;
    private final String leftBracket;
    
    public static Builder symbols() {
        return new Builder();
    }
    
    private SymbolsConfig(final Builder builder){
        this.expEquals = builder.expEquals;
        this.actNotEquals = builder.actNotEquals;
        this.expMatches = builder.expMatches;
        this.pointNested = builder.pointNested;
        this.itemInIterableMatches = builder.itemInIterableMatches;
        this.itemInIterableNotMatches = builder.itemInIterableNotMatches;
        this.itemInIterableBadItemOrder = builder.itemInIterableBadItemOrder;
        this.itemInIterableBadSortOrder = builder.itemInIterableBadSortOrder;
        this.itemInIterableDuplicate = builder.itemInIterableDuplicate;
        this.itemInIterableUnwanted = builder.itemInIterableUnwanted;
        this.rightBracket = builder.rightBracket;
        this.leftBracket = builder.leftBracket;
    }

    @Override
    public String getExpectedEquals() {
        return expEquals;
    }

    @Override
    public String getActualNotEquals() {
        return actNotEquals;
    }

    @Override
    public String getExpectedMatches() {
        return expMatches;
    }

    @Override
    public String getPointingNested() {
        return pointNested;
    }

    @Override
    public String getIterableItemMatchesSymbol() {
        return itemInIterableMatches;
    }

    @Override
    public String getIterableItemNotMatchesSymbol() {
        return itemInIterableNotMatches;
    }

    @Override
    public String getIterableItemBadItemOrderSymbol() {
        return itemInIterableBadItemOrder;
    }

    @Override
    public String getIterableItemBadSortOrderSymbol() {
        return itemInIterableBadSortOrder;
    }

    @Override
    public String getIterableItemDuplicateSymbol() {
        return itemInIterableDuplicate;
    }

    @Override
    public String getIterableItemUnwantedSymbol() {
        return itemInIterableUnwanted;
    }

    @Override
    public String getRightBracket() {
        return rightBracket;
    }

    @Override
    public String getLeftBracket() {
        return leftBracket;
    }
}
