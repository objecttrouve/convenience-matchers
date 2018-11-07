/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
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
     * @return {@code Symbols} with telling signs and revealing emojis.</p>
     */
    public static SymbolsConfig defaultSymbols() {
        return defaultSymbolsBuilder()
            .build();
    }

    /**
     * <p>Builder preconfigured with the default set of symbols used in error descriptions.
     * Favoring fanciness over nice rendering in ancient shells.</p>
     * @return {@code Symbols.Builder} preconfigured with telling signs and revealing emojis.
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
     * @return {@code Symbols} with robust signs.
     */
    @SuppressWarnings("WeakerAccess")
    public static SymbolsConfig asciiSymbols() {
        return asciiSymbolsBuilder()
            .build();
    }
    /**
     * <p>Builder preconfigured with alternative symbols for error descriptions, ASCII only,
     * for environments in which emojis don't print so well.</p>
     * @return {@code Symbols.Builder} preconfigured with robust signs.
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
         * Like {@link Builder#withExpectedMatches(java.lang.String)}, just with value expectations.
         * @param symbolExpectedEquals The symbol between the attribute description and the expected value.
         * @return this Builder
         */
        public Builder withExpectedEquals(final String symbolExpectedEquals) {
            this.expEquals = symbolExpectedEquals;
            return this;
        }

        /**
         * <p>Setter.</p>
         * @param actNotEquals The symbol between the expected value and the actual value.
         * @return this Builder
         */
        public Builder withActualNotEquals(final String actNotEquals) {
            this.actNotEquals = actNotEquals;
            return this;
        }

        /**
         * <p>Setter.</p>
         * Like {@link Builder#withExpectedEquals(java.lang.String)}, just with {@code org.hamcrest.Matcher}  expectations.
         * @param expMatches The symbol between the attribute description and the matcher description of the expected value.
         * @return this Builder
         */
        public Builder withExpectedMatches(final String expMatches) {
            this.expMatches = expMatches;
            return this;
        }

        /**
         * <p>Setter.</p>
         * @param pointerToNested Separator between nested attribute descriptions.
         * @return this Builder
         */
        public Builder withPointingNested(final String pointerToNested) {
            this.pointNested = pointerToNested;
            return this;
        }

        /**
         * <p>Setter.</p>
         * The arg may be truncated in the output.
         * @param itemInIterableMatches Indicates that the item matched the expectation.
         * @return this Builder
         */
        public Builder withIterableItemMatches(final String itemInIterableMatches) {
            this.itemInIterableMatches = itemInIterableMatches;
            return this;
        }

        /**
         * <p>Setter.</p>
         * @param itemInIterableNotMatches Indicates that a {@code Matcher} didn't match the item.
         * @return this Builder
         */
        public Builder withIterableItemNotMatches(final String itemInIterableNotMatches) {
            this.itemInIterableNotMatches = itemInIterableNotMatches;
            return this;
        }

        /**
         * <p>Setter.</p>
         * The arg may be truncated in the output.
         * @param itemInIterableBadItemOrder Indicates that the item should appear elsewhere in the {@code Iterable} given the expected order of items.
         * @return this Builder
         */
        public Builder withIterableItemBadItemOrder(final String itemInIterableBadItemOrder) {
            this.itemInIterableBadItemOrder = itemInIterableBadItemOrder;
            return this;
        }

        /**
         * <p>Setter.</p>
         * The arg may be truncated in the output.
         * @param itemInIterableBadSortOrder Indicates that the item should appear elsewhere in the {@code Iterable} given the expected sort order.
         * @return this Builder
         */
        public Builder withIterableItemBadSortOrder(final String itemInIterableBadSortOrder) {
            this.itemInIterableBadSortOrder = itemInIterableBadSortOrder;
            return this;
        }

        /**
         * <p>Setter.</p>
         * The arg may be truncated in the output.
         * @param itemInIterableDuplicate Indicates that the item is duplicated.
         * @return this Builder
         */
        public Builder withIterableItemDuplicate(final String itemInIterableDuplicate) {
            this.itemInIterableDuplicate = itemInIterableDuplicate;
            return this;
        }

        /**
         * <p>Setter.</p>
         * The arg may be truncated in the output.
         * @param itemInIterableUnwanted Indicates that the item should not appear in the {@code Iterable} at all. (Or at least not in the actual position.)
         * @return this Builder
         */
        public Builder withIterableItemUnwanted(final String itemInIterableUnwanted) {
            this.itemInIterableUnwanted = itemInIterableUnwanted;
            return this;
        }

        /**
         * <p>Setter.</p>
         * @param leftBracket Left bracket used to group objects.
         * @param rightBracket Right bracket used to group objects.
         * @return this Builder
         */
        public Builder withBrackets(final String leftBracket, final String rightBracket) {
            this.rightBracket = rightBracket;
            this.leftBracket = leftBracket;
            return this;
        }

        /**
         * <p>Builds the {@code Symbols} with the configured settings.</p>
         * @return the final  {@code Symbols}
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

    /**
     * Like {@link SymbolsConfig#getExpectedMatches()}, just with value expectations.
     * @return The symbol between the attribute description and the expected value.
     */
    @Override
    public String getExpectedEquals() {
        return expEquals;
    }

    /**
     * @return The symbol between the expected value and the actual value.
     */
    @Override
    public String getActualNotEquals() {
        return actNotEquals;
    }

    /**
     * Setter.
     * Like {@link SymbolsConfig#getExpectedEquals()}, just with {@code org.hamcrest.Matcher}  expectations.
     * @return The symbol between the attribute description and the matcher description of the expected value.
     */
    @Override
    public String getExpectedMatches() {
        return expMatches;
    }

    /**
     * @return Separator between nested attribute descriptions.
     */
    @Override
    public String getPointingNested() {
        return pointNested;
    }

    /**
     * @return Symbol to indicate that the item matched the expectation.
     */
    @Override
    public String getIterableItemMatchesSymbol() {
        return itemInIterableMatches;
    }

    /**
     * @return Symbol to indicate that a {@code Matcher} failed on the item.
     */
    @Override
    public String getIterableItemNotMatchesSymbol() {
        return itemInIterableNotMatches;
    }

    /**
     * @return Symbol to indicate that the item should appear elsewhere given the expected order of items.
     */
    @Override
    public String getIterableItemBadItemOrderSymbol() {
        return itemInIterableBadItemOrder;
    }

    /**
     * @return Symbol to indicate that the item should appear elsewhere given the expected sort order.
     */
    @Override
    public String getIterableItemBadSortOrderSymbol() {
        return itemInIterableBadSortOrder;
    }

    /**
     * @return Symbol to indicate that the item is duplicated.
     */
    @Override
    public String getIterableItemDuplicateSymbol() {
        return itemInIterableDuplicate;
    }

    /**
     * @return Symbol to indicate that the item should not occur in the {@code Iterable} at all.
     */
    @Override
    public String getIterableItemUnwantedSymbol() {
        return itemInIterableUnwanted;
    }

    /**
     * @return Right bracket to group objects.
     */
    @Override
    public String getRightBracket() {
        return rightBracket;
    }

    /**
     * @return Left bracket to group objects.
     */
    @Override
    public String getLeftBracket() {
        return leftBracket;
    }
}
