/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2018 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */
package org.objecttrouve.testing.matchers.fluentits

import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import org.junit.Assert
import org.junit.Test
import org.objecttrouve.testing.matchers.api.Symbols
import org.objecttrouve.testing.matchers.customization.StringifiersConfig
import org.objecttrouve.testing.matchers.customization.SymbolsConfig
import org.objecttrouve.testing.matchers.fluentits.ItemResult.MatcherWithIndex
import java.util.*

class ProseKotlinTest {

    @Test
    fun test__describeExpectations__no_expectations() {
        val description = StringBuilder()
        stringProse.describeExpectations(Settings<Any?>()) { s: String? -> description.append(s) }
        Assert.assertThat(description.toString(), CoreMatchers.`is`<String>("""
    an Iterable with the following properties:
    - Iterable of Object
    
    
    """.trimIndent()))
    }

    @Test
    fun test__describeExpectations__class() {
        val settings: Settings<*> = Settings<Any?>()
        settings.klass = Int::class.java
        val description = StringBuilder()
        stringProse.describeExpectations(settings) { s: String? -> description.append(s) }
        Assert.assertThat(description.toString(), CoreMatchers.`is`<String>("""
    an Iterable with the following properties:
    - Iterable of int
    
    
    """.trimIndent()))
    }

    @Test
    fun test__describeExpectations__explicit_size() {
        val settings: Settings<*> = Settings<Any?>()
        settings.expectedSize = 0
        val description = StringBuilder()
        stringProse.describeExpectations(settings) { s: String? -> description.append(s) }
        Assert.assertThat(description.toString(), CoreMatchers.`is`<String>("""
    an Iterable with the following properties:
    - Iterable of Object
    - exactly 0 item(s)
    
    
    """.trimIndent()))
    }

    @Test
    fun test__describeExpectations__nr_of_expected_items() {
        val settings: Settings<*> = Settings<Any?>()
        settings.expectations = arrayOf<Matcher<*>>(CoreMatchers.nullValue(), CoreMatchers.nullValue(), CoreMatchers.nullValue())
        val description = StringBuilder()
        stringProse.describeExpectations(settings) { s: String? -> description.append(s) }
        Assert.assertThat(description.toString(), CoreMatchers.`is`<String>("""
    an Iterable with the following properties:
    - Iterable of Object
    - at least 3 matching item(s)
    
    
    """.trimIndent()))
    }

    @Test
    fun test__describeExpectations__without_any_unexpected_items() {
        val settings: Settings<*> = Settings<Any?>()
        settings.mustNotHaveUnexpectedItems = true
        val description = StringBuilder()
        stringProse.describeExpectations(settings) { s: String? -> description.append(s) }
        Assert.assertThat(description.toString(), CoreMatchers.`is`<String>("""
    an Iterable with the following properties:
    - Iterable of Object
    - no unexpected items
    
    
    """.trimIndent()))
    }

    @Test
    fun test__describeExpectations__ordered() {
        val settings: Settings<*> = Settings<Any?>()
        settings.ordered = true
        val description = StringBuilder()
        stringProse.describeExpectations(settings) { s: String? -> description.append(s) }
        Assert.assertThat(description.toString(), CoreMatchers.`is`<String>("""
    an Iterable with the following properties:
    - Iterable of Object
    - ordered
    
    
    """.trimIndent()))
    }

    @Test
    fun test__describeExpectations__sorted() {
        val settings: Settings<*> = Settings<Any?>()
        settings.sorted = true
        val description = StringBuilder()
        stringProse.describeExpectations(settings) { s: String? -> description.append(s) }
        Assert.assertThat(description.toString(), CoreMatchers.`is`<String>("""
    an Iterable with the following properties:
    - Iterable of Object
    - sorted
    
    
    """.trimIndent()))
    }

    @Test
    fun test__describeExpectations__unique() {
        val settings: Settings<*> = Settings<Any?>()
        settings.unique = true
        val description = StringBuilder()
        stringProse.describeExpectations(settings) { s: String? -> description.append(s) }
        Assert.assertThat(description.toString(), CoreMatchers.`is`<String>("""
    an Iterable with the following properties:
    - Iterable of Object
    - no duplicates
    
    
    """.trimIndent()))
    }

    @Test
    fun test__describeExpectations__all_expectations() {
        val settings: Settings<*> = Settings<Any?>()
        settings.klass = String::class.java
        settings.expectations = arrayOf<Matcher<*>>(CoreMatchers.nullValue(), CoreMatchers.nullValue(), CoreMatchers.nullValue())
        settings.expectedSize = 3
        settings.mustNotHaveUnexpectedItems = true
        settings.sorted = true
        settings.ordered = true
        settings.unique = true
        val description = StringBuilder()
        stringProse.describeExpectations(settings) { s: String? -> description.append(s) }
        Assert.assertThat(description.toString(), CoreMatchers.`is`<String>("""
    an Iterable with the following properties:
    - Iterable of String
    - exactly 3 item(s)
    - at least 3 matching item(s)
    - no unexpected items
    - sorted
    - ordered
    - no duplicates
    
    
    """.trimIndent()))
    }

    @Test
    fun test__actualItemString__null_arg() {
        val s = stringProse.actualItemString(null, 15)
        Assert.assertThat(s, CoreMatchers.`is`("null           "))
    }

    @Test
    fun test__actualItemString__empty_arg() {
        val s = stringProse.actualItemString("", 15)
        Assert.assertThat(s, CoreMatchers.`is`("               "))
    }

    @Test
    fun test__actualItemString__happy_arg() {
        val s = stringProse.actualItemString("xxxxxxxxxxxxxxx", 15)
        Assert.assertThat(s, CoreMatchers.`is`("xxxxxxxxxxxxxxx"))
    }

    @Test
    fun test__actualItemString__newln_arg() {
        val s = stringProse.actualItemString("xxxxxxx\nxxxxxxx", 15)
        Assert.assertThat(s, CoreMatchers.`is`("xxxxxxx; xxxxxx"))
    }

    @Test
    fun test__actualItemString__trunc_arg() {
        val s = stringProse.actualItemString("xxxxxxxxxxxxxxxXXXXXXXX", 15)
        Assert.assertThat(s, CoreMatchers.`is`("xxxxxxxxxxxxxxx"))
    }

    @Test
    fun test__actualItemString__pad_arg() {
        val s = stringProse.actualItemString("xxxxx", 15)
        Assert.assertThat(s, CoreMatchers.`is`("xxxxx          "))
    }

    @Test
    fun test__actualItemString__pad_arg__0() {
        val s = stringProse.actualItemString("xxxxx", 1)
        Assert.assertThat(s, CoreMatchers.`is`("x"))
    }

    @Test
    fun test__matcherSaying__equals_matcher() {
        val matcher = CoreMatchers.equalTo("Y")
        matcher.matches("X")
        val self = StringDescription()
        val mismatch = StringDescription()
        matcher.describeTo(self)
        matcher.describeMismatch("X", mismatch)
        val matcherSaying = stringProse.matcherSaying(self.toString())
        Assert.assertThat(matcherSaying, CoreMatchers.`is`("\"Y\""))
    }

    private class MatcherWithNewLines : Matcher<Any?> {
        override fun matches(item: Any): Boolean {
            return false
        }

        override fun describeMismatch(item: Any, mismatchDescription: Description) {
            mismatchDescription.appendText("x\nx")
        }

        override fun _dont_implement_Matcher___instead_extend_BaseMatcher_() {}
        override fun describeTo(description: Description) {
            description.appendText("y\ny")
        }
    }

    @Test
    fun test__matcherSaying__matcher_with_newlines() {
        val matcher: MatcherWithNewLines = MatcherWithNewLines()
        val self = StringDescription()
        val mismatch = StringDescription()
        matcher.describeTo(self)
        matcher.describeMismatch("X", mismatch)
        val matcherSaying = stringProse.matcherSaying(self.toString())
        Assert.assertThat(matcherSaying, CoreMatchers.`is`("y; y"))
    }

    @Test
    fun line__nothing_to_report() {
        val r1: ItemResult<String> = ItemResult.builder<String>(null)
                .withIndex(0)
                .matched(false)
                .build()
        val line = stringProse.line(r1, 1, 1, "null")
        Assert.assertThat(line, CoreMatchers.`is`("⦗0⦘⦗n⦘          "))
    }

    @Test
    fun line__nothing_to_report__item_length_4() {
        val r1: ItemResult<String> = ItemResult.builder<String>(null)
                .withIndex(0)
                .matched(false)
                .build()
        val line = stringProse.line(r1, 1, 4, "null")
        Assert.assertThat(line, CoreMatchers.`is`("⦗0⦘⦗null⦘          "))
    }

    @Test
    fun line__matched_item() {
        val r1 = ItemResult.builder(true)
                .withIndex(0)
                .matched(true)
                .build()
        val line = boolProse.line(r1, 1, 4, "true")
        Assert.assertThat(line, CoreMatchers.`is`("⦗0⦘⦗true⦘💕        "))
    }

    @Test
    fun line__index_0__item_breaking_item_order() {
        val r1 = ItemResult.builder(true)
                .withIndex(0)
                .breakingItemOrder(true)
                .build()
        val line = boolProse.line(r1, 1, 4, "true")
        Assert.assertThat(line, CoreMatchers.`is`("⦗0⦘⦗true⦘    ↔     "))
    }

    @Test
    fun line__index_1__item_breaking_sort_order() {
        val r1 = ItemResult.builder(true)
                .withIndex(1)
                .breakingSortOrder(true)
                .build()
        val line = boolProse.line(r1, 1, 4, "true")
        Assert.assertThat(line, CoreMatchers.`is`("⦗1⦘⦗true⦘  ↕       "))
    }

    @Test
    fun line__index_22__duplicate_item__of__100() {
        val r = ItemResult.builder(true)
                .withIndex(22)
                .duplicate(true)
                .build()
        val line = boolProse.line(r, 100, 4, "true")
        Assert.assertThat(line, CoreMatchers.`is`("⦗ 22⦘⦗true⦘      👯  "))
    }

    @Test
    fun line__index_22__unwanted_item__of__1000__among_items_of_10() {
        val r = ItemResult.builder(true)
                .withIndex(22)
                .unwanted(true)
                .build()
        val line = boolProse.line(r, 1000, 10, "true")
        Assert.assertThat(line, CoreMatchers.`is`("⦗  22⦘⦗true      ⦘        🚯"))
    }

    @Test
    fun line__index_22__mismatched_item__of__1000__imaginary_truncated() {
        val r = ItemResult.builder(true)
                .withIndex(22)
                .withMatchers(listOf(mwi(CoreMatchers.nullValue(), 3)))
                .build()
        val line = boolProse.line(r, 1000, 3, "true")
        Assert.assertThat(line, CoreMatchers.`is`("⦗  22⦘⦗tru⦘           💔⦗3⦘⦗null⦘"))
    }

    private fun mwi(matcher: Matcher<*>, index: Int): MatcherWithIndex {
        return MatcherWithIndex(matcher, index)
    }

    @Test
    fun line__s__with_all_of_it() {
        val r1 = ItemResult.builder("scene de menage")
                .withIndex(0)
                .matched(false)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r2 = ItemResult.builder("scene de manège")
                .withIndex(1)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .withMatchers(Arrays.asList(mwi(CoreMatchers.equalTo("scène de ménage"), 2), mwi(CoreMatchers.endsWith("age"), 3)))
                .build()
        val r3 = ItemResult.builder("le mariage")
                .withIndex(99)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r4 = ItemResult.builder("scène de ménage")
                .withIndex(9999)
                .matched(true)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .build()
        val line1 = stringProse.line(r1, 100, 15, "scene de menage")
        val line2 = stringProse.line(r2, 100, 15, "scene de manège")
        val line3 = stringProse.line(r3, 100, 15, "le mariage")
        val line4 = stringProse.line(r4, 100, 15, "scène de ménage")
        Assert.assertThat(line1, CoreMatchers.`is`("⦗  0⦘⦗scene de menage⦘           💔⦗0⦘⦗\"scène de ménage\"⦘"))
        Assert.assertThat(line2, CoreMatchers.`is`("⦗  1⦘⦗scene de manège⦘  ↕ ↔      💔⦗2⦘⦗\"scène de ménage\"⦘ 💔⦗3⦘⦗a string ending with \"age\"⦘"))
        Assert.assertThat(line3, CoreMatchers.`is`("⦗ 99⦘⦗le mariage     ⦘  ↕ ↔ 👯🚯 💔⦗0⦘⦗\"scène de ménage\"⦘"))
        Assert.assertThat(line4, CoreMatchers.`is`("⦗999⦘⦗scène de ménage⦘💕↕ ↔ 👯🚯"))
    }

    @Test
    fun line__s__with_all_of_it__and_custom_Symbols() {
        val symbols: Symbols = SymbolsConfig.symbols()
                .withIterableItemMatches("\uD83D\uDC98")
                .withIterableItemNotMatches("\uD83D\uDC80")
                .withIterableItemBadItemOrder("⥨")
                .withIterableItemBadSortOrder("⑄")
                .withIterableItemDuplicate("♊")
                .withIterableItemUnwanted("\uD83D\uDEAE")
                .withBrackets("⧼", "⧽")
                .build()
        val prose = Prose<String>(symbols, StringifiersConfig.stringifiers().build())
        val r3 = ItemResult.builder("le mariage")
                .withIndex(99)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r4 = ItemResult.builder("scène de ménage")
                .withIndex(9999)
                .matched(true)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .build()
        val line3 = prose.line(r3, 100, 15, "le mariage")
        val line4 = prose.line(r4, 100, 15, "scène de ménage")
        Assert.assertThat(line3, CoreMatchers.`is`("⧼ 99⧽⧼le mariage     ⧽  ⑄ ⥨ ♊ 🚮 💀⧼0⧽⧼\"scène de ménage\"⧽"))
        Assert.assertThat(line4, CoreMatchers.`is`("⧼999⧽⧼scène de ménage⧽💘⑄ ⥨ ♊ 🚮"))
    }

    @Test
    fun describe__without_stringifiers() {
        val r1 = ItemResult.builder("scene de menage")
                .withIndex(0)
                .matched(false)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r2 = ItemResult.builder("scene de manège")
                .withIndex(1)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .withMatchers(Arrays.asList(mwi(CoreMatchers.equalTo("scène de ménage"), 2), mwi(CoreMatchers.endsWith("age"), 3)))
                .build()
        val r3 = ItemResult.builder("le mariage")
                .withIndex(99)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r4 = ItemResult.builder("scène de ménage")
                .withIndex(9999)
                .matched(true)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .build()
        val findings = Arrays.asList(Finding("Something isn't right."), Finding("There seems to be chaos..."))
        val itemResults: List<ItemResult<*>> = Arrays.asList(r1, r2, r3, r4)
        val prose = Prose<String>(SymbolsConfig.defaultSymbols(), StringifiersConfig.stringifiers().build())
        val description = StringDescription()
        prose.describe(findings.stream(), itemResults, description)
        val result = description.toString()
        Assert.assertThat(result, CoreMatchers.`is`<String>("""
    
    Findings:
    "Something isn't right."
    "There seems to be chaos..."
    
    ⦗0⦘⦗scene de menage⦘           💔⦗0⦘⦗"scène de ménage"⦘
    ⦗1⦘⦗scene de manège⦘  ↕ ↔      💔⦗2⦘⦗"scène de ménage"⦘ 💔⦗3⦘⦗a string ending with "age"⦘
    ⦗9⦘⦗le mariage     ⦘  ↕ ↔ 👯🚯 💔⦗0⦘⦗"scène de ménage"⦘
    ⦗9⦘⦗scène de ménage⦘💕↕ ↔ 👯🚯
    
    
    """.trimIndent()))
    }

    @Test
    fun describe__with_shortStringifier_for_actual_items() {
        val r1 = ItemResult.builder("scene de menage")
                .withIndex(0)
                .matched(false)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r2 = ItemResult.builder("scene de manège")
                .withIndex(1)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .withMatchers(Arrays.asList(mwi(CoreMatchers.equalTo("scène de ménage"), 2), mwi(CoreMatchers.endsWith("age"), 3)))
                .build()
        val r3 = ItemResult.builder("le mariage")
                .withIndex(99)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r4 = ItemResult.builder("scène de ménage")
                .withIndex(9999)
                .matched(true)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .build()
        val findings = Arrays.asList(Finding("Something isn't right."), Finding("There seems to be chaos..."))
        val itemResults: List<ItemResult<*>> = Arrays.asList(r1, r2, r3, r4)
        val prose = Prose<String>(
                SymbolsConfig.defaultSymbols(),
                StringifiersConfig.stringifiers()
                        .withShortStringifier(String::class.java) { obj: String -> obj.toUpperCase() }
                        .build()
        )
        val description = StringDescription()
        prose.describe(findings.stream(), itemResults, description)
        val result = description.toString()
        Assert.assertThat(result, CoreMatchers.`is`<String>("""
    
    Findings:
    "Something isn't right."
    "There seems to be chaos..."
    
    ⦗0⦘⦗SCENE DE MENAGE⦘           💔⦗0⦘⦗"scène de ménage"⦘
    ⦗1⦘⦗SCENE DE MANÈGE⦘  ↕ ↔      💔⦗2⦘⦗"scène de ménage"⦘ 💔⦗3⦘⦗a string ending with "age"⦘
    ⦗9⦘⦗LE MARIAGE     ⦘  ↕ ↔ 👯🚯 💔⦗0⦘⦗"scène de ménage"⦘
    ⦗9⦘⦗SCÈNE DE MÉNAGE⦘💕↕ ↔ 👯🚯
    
    
    """.trimIndent()))
    }

    @Test
    fun describe__with_debugStringifier_for_actual_items() {
        val r1 = ItemResult.builder("scene de menage")
                .withIndex(0)
                .matched(false)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r2 = ItemResult.builder("scene de manège")
                .withIndex(1)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .withMatchers(Arrays.asList(mwi(CoreMatchers.equalTo("scène de ménage"), 2), mwi(CoreMatchers.endsWith("age"), 3)))
                .build()
        val r3 = ItemResult.builder("le mariage")
                .withIndex(99)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r4 = ItemResult.builder("scène de ménage")
                .withIndex(9999)
                .matched(true)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .build()
        val findings = Arrays.asList(Finding("Something isn't right."), Finding("There seems to be chaos..."))
        val itemResults: List<ItemResult<*>> = Arrays.asList(r1, r2, r3, r4)
        val prose = Prose<String>(
                SymbolsConfig.defaultSymbols(),
                StringifiersConfig.stringifiers()
                        .withDebugStringifier(String::class.java) { obj: String -> obj.toUpperCase() }
                        .build()
        )
        val description = StringDescription()
        prose.describe(findings.stream(), itemResults, description)
        val result = description.toString()
        // Not relevant for overview.
        Assert.assertThat(result, CoreMatchers.`is`<String>("""
    
    Findings:
    "Something isn't right."
    "There seems to be chaos..."
    
    ⦗0⦘⦗scene de menage⦘           💔⦗0⦘⦗"scène de ménage"⦘
    ⦗1⦘⦗scene de manège⦘  ↕ ↔      💔⦗2⦘⦗"scène de ménage"⦘ 💔⦗3⦘⦗a string ending with "age"⦘
    ⦗9⦘⦗le mariage     ⦘  ↕ ↔ 👯🚯 💔⦗0⦘⦗"scène de ménage"⦘
    ⦗9⦘⦗scène de ménage⦘💕↕ ↔ 👯🚯
    
    
    """.trimIndent()))
    }

    @Test
    fun describeDebugging__with_debugStringifier() {
        val r1 = ItemResult.builder("scene de menage")
                .withIndex(0)
                .matched(false)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r2 = ItemResult.builder("scene de manège")
                .withIndex(1)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .withMatchers(Arrays.asList(mwi(CoreMatchers.equalTo("scène de ménage"), 2), mwi(CoreMatchers.endsWith("age"), 3)))
                .build()
        val r3 = ItemResult.builder("le mariage")
                .withIndex(99)
                .matched(false)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .withMatchers(listOf(mwi(CoreMatchers.equalTo("scène de ménage"), 0)))
                .build()
        val r4 = ItemResult.builder("scène de ménage")
                .withIndex(9999)
                .matched(true)
                .breakingItemOrder(true)
                .breakingSortOrder(true)
                .duplicate(true)
                .unwanted(true)
                .build()
        val itemResults: List<ItemResult<*>> = Arrays.asList(r1, r2, r3, r4)
        val prose = Prose<String>(
                SymbolsConfig.defaultSymbols(),
                StringifiersConfig.stringifiers()
                        .withDebugStringifier(String::class.java) { obj: String -> obj.toUpperCase() }
                        .build()
        )
        val description = StringDescription()
        prose.describeDebugging(itemResults, description)
        val length = description.toString().length
        if (length != 1536) {
            Assert.fail("Length was $length. Debug output changed in an unexpected way:\n\n$description")
        }
    }

    companion object {
        private val stringProse = Prose<String>(SymbolsConfig.defaultSymbols(), StringifiersConfig.stringifiers().build())
        private val boolProse = Prose<Boolean>(SymbolsConfig.defaultSymbols(), StringifiersConfig.stringifiers().build())
    }
}