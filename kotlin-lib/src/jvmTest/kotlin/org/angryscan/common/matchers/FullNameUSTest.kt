package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки матчера FullNameUS (американские ФИО)
 */
internal class FullNameUSTest {

    private fun surround(text: String) = " $text "

    // === Позитивные сценарии ===

    @Test
    fun testSimpleName() {
        val count = scanText(surround("John Smith"), FullNameUS)
        assertTrue(count >= 1, "Expected at least one match, got $count")
    }

    @Test
    fun testNameInSentence() {
        val text = "Please contact John Smith for assistance."
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    @Test
    fun testNameWithCommaSeparatedContext() {
        val text = " John Smith ,"
        val count = scanText(text, FullNameUS)
        assertTrue(count >= 1, "Expected match, got $count")
    }

    @Test
    fun testNameWithColon() {
        val text = "Name: John Smith "
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    @Test
    fun testNameWithNewline() {
        val text = "Name:\n John Smith "
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    @Test
    fun testNameWithTab() {
        val text = "Name:\t John Smith "
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    @Test
    fun testNameWithMultipleSpaces() {
        val text = surround("John    Smith")
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    @Test
    fun testNameWithMiddleInitial() {
        assertTrue(scanText(surround("John A. Smith"), FullNameUS) >= 1)
    }

    @Test
    fun testNameWithMiddleInitialNoPeriod() {
        assertTrue(scanText(surround("John A Smith"), FullNameUS) >= 1)
    }

    @Test
    fun testNameInParentheses() {
        val text = " ( John Smith ) "
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    @Test
    fun testNameInQuotes() {
        val text = " \" John Smith \" "
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    @Test
    fun testMultipleNames() {
        val text = " John Smith met Emily Davis and Michael Johnson. "
        assertEquals(3, scanText(text, FullNameUS))
    }

    @Test
    fun testNameNearTechnicalWord() {
        val text = "The server incident was resolved by John Smith yesterday."
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    // === Негативные сценарии ===
    
    @Test
    fun testUnknownName() {
        assertEquals(0, scanText(surround("X Æa-12 Zøltar"), FullNameUS))
    }

    @Test
    fun testTechnicalPhrase() {
        assertEquals(0, scanText("Pretty Good Privacy", FullNameUS))
    }

    @Test
    fun testCityName() {
        assertEquals(0, scanText("New York City", FullNameUS))
    }

    @Test
    fun testCompanyPhrase() {
        assertEquals(0, scanText("Apple Store", FullNameUS))
    }

    @Test
    fun testEmptyString() {
        assertEquals(0, scanText("", FullNameUS))
    }

    @Test
    fun testOnlyFirstName() {
        assertEquals(0, scanText("John", FullNameUS))
    }

    @Test
    fun testOnlyLastName() {
        assertEquals(0, scanText("Smith", FullNameUS))
    }

    @Test
    fun testAllCaps() {
        assertEquals(0, scanText("JOHN SMITH", FullNameUS))
    }

    @Test
    fun testAllLowercase() {
        assertEquals(0, scanText("john smith", FullNameUS))
    }
    
    @Test
    fun testPhraseWithBrother() {
        assertEquals(0, scanText("Big Brother is watching", FullNameUS))
    }

    // === Дополнительные проверки ===
    
    @Test
    fun testSequenceWithMixedContent() {
        val text = " John Smith, Pretty Good Privacy, Ocean Park, Emily Davis "
        assertEquals(2, scanText(text, FullNameUS))
    }

    @Test
    fun testNameAtStartWithSpace() {
        val text = " John Smith went to the store."
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    @Test
    fun testNameAtEndWithSpace() {
        val text = "Meeting with manager and John Smith "
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    @Test
    fun testNameWithDashContext() {
        val text = "Lead engineer - John Smith - reviewed the plan."
        assertTrue(scanText(text, FullNameUS) >= 1)
    }

    @Test
    fun testCheckDirect() {
        assertTrue(FullNameUS.check("John Smith"))
    }

    private fun scanText(text: String, matcher: IMatcher): Int {
        val kotlinEngine = KotlinEngine(listOf(matcher).filterIsInstance<IKotlinMatcher>())
        val hyperEngine = HyperScanEngine(listOf(matcher).filterIsInstance<IHyperMatcher>())

        val kotlinRes = kotlinEngine.scan(text)
        val hyperRes = hyperEngine.scan(text)
        
        assertEquals(
            kotlinRes.count(),
            hyperRes.count(),
            "Number of matches for ${matcher.name} should be the same for both engines. " +
            "Kotlin: ${kotlinRes.count()}, Hyper: ${hyperRes.count()}\nText: $text"
        )
        
        return kotlinRes.count()
    }
}
