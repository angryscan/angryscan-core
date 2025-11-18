package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки матчера FullNameUS (американские ФИО)
 */
internal class FullNameUSTest: MatcherTestBase(FullNameUS) {

    private fun surround(text: String) = " $text "

    // === Позитивные сценарии ===

    @Test
    fun testSimpleName() {
        val count = scanText(surround("John Smith"))
        assertTrue(count >= 1, "Expected at least one match, got $count")
    }

    @Test
    fun testNameInSentence() {
        val text = "Please contact John Smith for assistance."
        assertTrue(scanText(text) >= 1)
    }

    @Test
    fun testNameWithCommaSeparatedContext() {
        val text = " John Smith ,"
        val count = scanText(text)
        assertTrue(count >= 1, "Expected match, got $count")
    }

    @Test
    fun testNameWithColon() {
        val text = "Name: John Smith "
        assertTrue(scanText(text) >= 1)
    }

    @Test
    fun testNameWithNewline() {
        val text = "Name:\n John Smith "
        assertTrue(scanText(text) >= 1)
    }

    @Test
    fun testNameWithTab() {
        val text = "Name:\t John Smith "
        assertTrue(scanText(text) >= 1)
    }

    @Test
    fun testNameWithMultipleSpaces() {
        val text = surround("John    Smith")
        assertTrue(scanText(text) >= 1)
    }

    @Test
    fun testNameWithMiddleInitial() {
        assertTrue(scanText(surround("John A. Smith")) >= 1)
    }

    @Test
    fun testNameWithMiddleInitialNoPeriod() {
        assertTrue(scanText(surround("John A Smith")) >= 1)
    }

    @Test
    fun testNameInParentheses() {
        val text = " ( John Smith ) "
        assertTrue(scanText(text) >= 1)
    }

    @Test
    fun testNameInQuotes() {
        val text = " \" John Smith \" "
        assertTrue(scanText(text) >= 1)
    }

    @Test
    fun testMultipleNames() {
        val text = " John Smith met Emily Davis and Michael Johnson. "
        assertEquals(3, scanText(text))
    }

    @Test
    fun testNameNearTechnicalWord() {
        val text = "The server incident was resolved by John Smith yesterday."
        assertTrue(scanText(text) >= 1)
    }

    // === Негативные сценарии ===
    
    @Test
    fun testUnknownName() {
        assertEquals(0, scanText(surround("X Æa-12 Zøltar")))
    }

    @Test
    fun testTechnicalPhrase() {
        assertEquals(0, scanText("Pretty Good Privacy"))
    }

    @Test
    fun testCityName() {
        assertEquals(0, scanText("New York City"))
    }

    @Test
    fun testCompanyPhrase() {
        assertEquals(0, scanText("Apple Store"))
    }

    @Test
    fun testEmptyString() {
        assertEquals(0, scanText(""))
    }

    @Test
    fun testOnlyFirstName() {
        assertEquals(0, scanText("John"))
    }

    @Test
    fun testOnlyLastName() {
        assertEquals(0, scanText("Smith"))
    }

    @Test
    fun testAllCaps() {
        assertEquals(0, scanText("JOHN SMITH"))
    }

    @Test
    fun testAllLowercase() {
        assertEquals(0, scanText("john smith"))
    }
    
    @Test
    fun testPhraseWithBrother() {
        assertEquals(0, scanText("Big Brother is watching"))
    }

    // === Дополнительные проверки ===
    
    @Test
    fun testSequenceWithMixedContent() {
        val text = " John Smith, Pretty Good Privacy, Ocean Park, Emily Davis "
        assertEquals(2, scanText(text))
    }

    @Test
    fun testNameAtStartWithSpace() {
        val text = " John Smith went to the store."
        assertTrue(scanText(text) >= 1)
    }

    @Test
    fun testNameAtEndWithSpace() {
        val text = "Meeting with manager and John Smith "
        assertTrue(scanText(text) >= 1)
    }

    @Test
    fun testNameWithDashContext() {
        val text = "Lead engineer - John Smith - reviewed the plan."
        assertTrue(scanText(text) >= 1)
    }

    @Test
    fun testCheckDirect() {
        assertTrue(FullNameUS.check("John Smith"))
    }
}
