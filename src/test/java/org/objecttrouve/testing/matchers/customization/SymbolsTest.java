/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.customization;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SymbolsTest {

    @Test
    public void testDefautSymbols(){
        assertThat(SymbolsConfig.defaultSymbols().getExpectedEquals(), is(" = "));
        assertThat(SymbolsConfig.defaultSymbols().getActualNotEquals(), is(" ≠ "));
        assertThat(SymbolsConfig.defaultSymbols().getExpectedMatches(), is(" ⩳ "));
        assertThat(SymbolsConfig.defaultSymbols().getPointingNested(), is(" ▶ "));
        assertThat(SymbolsConfig.defaultSymbols().getIterableItemMatchesSymbol(), is("💕"));
        assertThat(SymbolsConfig.defaultSymbols().getIterableItemNotMatchesSymbol(), is("💔"));
        assertThat(SymbolsConfig.defaultSymbols().getIterableItemBadItemOrderSymbol(), is("↔"));
        assertThat(SymbolsConfig.defaultSymbols().getIterableItemBadSortOrderSymbol(), is("↕"));
        assertThat(SymbolsConfig.defaultSymbols().getIterableItemDuplicateSymbol(), is("👯"));
        assertThat(SymbolsConfig.defaultSymbols().getIterableItemUnwantedSymbol(), is("🚯"));
        assertThat(SymbolsConfig.defaultSymbols().getLeftBracket(), is("⦗"));
        assertThat(SymbolsConfig.defaultSymbols().getRightBracket(), is("⦘"));
    }

    @Test
    public void testAsciiSymbols(){
        assertThat(SymbolsConfig.asciiSymbols().getExpectedEquals(), is(" = "));
        assertThat(SymbolsConfig.asciiSymbols().getActualNotEquals(), is(" != "));
        assertThat(SymbolsConfig.asciiSymbols().getExpectedMatches(), is(" =~ "));
        assertThat(SymbolsConfig.asciiSymbols().getPointingNested(), is(" >> "));
        assertThat(SymbolsConfig.asciiSymbols().getIterableItemMatchesSymbol(), is("OK"));
        assertThat(SymbolsConfig.asciiSymbols().getIterableItemNotMatchesSymbol(), is("FAIL"));
        assertThat(SymbolsConfig.asciiSymbols().getIterableItemBadItemOrderSymbol(), is("<>"));
        assertThat(SymbolsConfig.asciiSymbols().getIterableItemBadSortOrderSymbol(), is("^v"));
        assertThat(SymbolsConfig.asciiSymbols().getIterableItemDuplicateSymbol(), is("2+"));
        assertThat(SymbolsConfig.asciiSymbols().getIterableItemUnwantedSymbol(), is("--"));
        assertThat(SymbolsConfig.asciiSymbols().getLeftBracket(), is("["));
        assertThat(SymbolsConfig.asciiSymbols().getRightBracket(), is("]"));
    }
}