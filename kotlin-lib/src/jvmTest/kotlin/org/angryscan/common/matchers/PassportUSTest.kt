package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера PassportUS (американские паспорта)
 */
internal class PassportUSTest: MatcherTestBase(PassportUS) {

    @Test
    fun testPassportUSAtStart() {
        val text = "Passport A98765432 issued in 2020"
        assertTrue(scanText(text) >= 1, "US passport at the beginning should be found")
    }

    @Test
    fun testPassportUSAtEnd() {
        val text = "Document: Passport C98765432"
        assertTrue(scanText(text) >= 1, "US passport at the end should be found")
    }

    @Test
    fun testPassportUSInMiddle() {
        val text = "Citizen presented passport A98765432 for verification"
        assertTrue(scanText(text) >= 1, "US passport in the middle should be found")
    }

    @Test
    fun testPassportUSStandalone() {
        val text = "Passport A98765432"
        assertTrue(scanText(text) >= 1, "US passport as standalone string should be found")
    }

    @Test
    fun testPassportUS9Digits() {
        val text = "Passport: C98765432"
        assertTrue(scanText(text) >= 1, "US passport with letter + 8 digits should be found")
    }

    @Test
    fun testPassportUSLetterPlus8Digits() {
        val text = "Passport: C98765432"
        assertTrue(scanText(text) >= 1, "US passport with letter + 8 digits should be found")
    }

    @Test
    fun testPassportUSWithColon() {
        val text = "Passport: A98765432"
        assertTrue(scanText(text) >= 1, "US passport with colon should be found")
    }

    @Test
    fun testPassportUSWithDash() {
        val text = "Passport - C98765432 "
        assertTrue(scanText(text) >= 1, "US passport with dash should be found")
    }

    @Test
    fun testPassportUSWithSpaces() {
        val text = "Passport A98765432"
        assertTrue(scanText(text) >= 1, "US passport with spaces should be found")
    }

    @Test
    fun testPassportUSWithNumber() {
        val text = "Passport Number: C98765432"
        assertTrue(scanText(text) >= 1, "US passport with 'Passport Number' should be found")
    }

    @Test
    fun testPassportUSWithNo() {
        val text = "Passport No: A98765432"
        assertTrue(scanText(text) >= 1, "US passport with 'Passport No' should be found")
    }

    @Test
    fun testPassportUSWithHash() {
        val text = "Passport # C98765432 "
        assertTrue(scanText(text) >= 1, "US passport with '#' should be found")
    }

    @Test
    fun testPassportUSShortForm() {
        val text = "Pass. No. A98765432"
        assertTrue(scanText(text) >= 1, "US passport with 'Pass. No.' should be found")
    }

    @Test
    fun testPassportUSUpperCase() {
        val text = "PASSPORT C98765432"
        assertTrue(scanText(text) >= 1, "US passport in uppercase should be found")
    }

    @Test
    fun testPassportUSLowerCase() {
        val text = "passport A98765432 "
        assertTrue(scanText(text) >= 1, "US passport with lowercase keyword should be found")
    }

    @Test
    fun testPassportUSMixedCase() {
        val text = "PaSpOrT C98765432"
        assertTrue(scanText(text) >= 1, "US passport in mixed case should be found")
    }

    @Test
    fun testPassportUSWithUSPrefix() {
        val text = "US Passport: A98765432"
        assertTrue(scanText(text) >= 1, "US passport with 'US' prefix should be found")
    }

    @Test
    fun testPassportUSInParentheses() {
        val text = "(A98765432) "
        assertTrue(scanText(text) >= 1, "US passport in parentheses should be found")
    }

    @Test
    fun testPassportUSInQuotes() {
        val text = "\"C98765432\" "
        assertTrue(scanText(text) >= 1, "US passport in quotes should be found")
    }

    @Test
    fun testPassportUSWithPunctuation() {
        val text = "Document: Passport A98765432. "
        assertTrue(scanText(text) >= 1, "US passport with punctuation should be found")
    }

    @Test
    fun testPassportUSStandaloneNumber() {
        val text = "A98765432"
        assertTrue(scanText(text) >= 1, "US passport standalone letter+8 digits should be found")
    }

    @Test
    fun testPassportUSStandaloneLetterNumber() {
        val text = "C98765432"
        assertTrue(scanText(text) >= 1, "US passport standalone letter+8 digits should be found")
    }

    @Test
    fun testPassportUSWithMultipleSpaces() {
        val text = "Passport    A98765432"
        assertTrue(scanText(text) >= 1, "US passport with multiple spaces should be found")
    }

    @Test
    fun testPassportUSAfterEquals() {
        val text = "passport= C98765432 "
        assertTrue(scanText(text) >= 1, "US passport after equals sign should be found")
    }

    @Test
    fun testPassportUSAfterAsterisk() {
        val text = "# A98765432 "
        assertTrue(scanText(text) >= 1, "US passport after hash with space should be found")
    }

    @Test
    fun testPassportUSStartsWithA() {
        val text = "Passport A98765432"
        assertTrue(scanText(text) >= 1, "US passport starting with A should be found")
    }

    @Test
    fun testPassportUSStartsWithZ() {
        val text = "Passport Z12345678"
        assertEquals(0, scanText(text), "US passport starting with Z should not be found (only A or C allowed)")
    }

    @Test
    fun testPassportUSMultipleInText() {
        val text = """
            First: Passport A98765432
            Second: Passport C87654321
            Third: Passport Number A76543210
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Multiple US passports should be found")
    }

    @Test
    fun testPassportUSWithNewline() {
        val text = "Passport\nA98765432"
        assertTrue(scanText(text) >= 1, "US passport with newline should be found")
    }

    @Test
    fun testPassportUSWithTab() {
        val text = "Passport\tC98765432"
        assertTrue(scanText(text) >= 1, "US passport with tab should be found")
    }

    @Test
    fun testPassportUSBoundary000000000() {
        val text = "Passport A00000000"
        assertEquals(0, scanText(text), "US passport with A00000000 should not be found (excluded)")
    }

    @Test
    fun testPassportUSBoundary999999999() {
        val text = "Passport C99999999"
        assertEquals(0, scanText(text), "US passport with C99999999 should not be found (excluded)")
    }

    @Test
    fun testPassportUSPassNumberShort() {
        val text = "Pass Number A98765432"
        assertTrue(scanText(text) >= 1, "US passport with 'Pass Number' should be found")
    }

    @Test
    fun testPassportUSPassNoShort() {
        val text = "Pass No C98765432"
        assertTrue(scanText(text) >= 1, "US passport with 'Pass No' should be found")
    }

    @Test
    fun testPassportUSTooShort() {
        val text = "Passport A12345"
        assertEquals(0, scanText(text), "Too short passport number should not be found")
    }

    @Test
    fun testPassportUSTooLong() {
        val text = "Passport A123456789"
        assertEquals(0, scanText(text), "Too long passport number should not be found")
    }

    @Test
    fun testPassportUSInvalidFormat2Letters() {
        val text = "Passport AB12345678"
        assertEquals(0, scanText(text), "Passport with 2 letters should not be found")
    }

    @Test
    fun testPassportUSEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Empty string should not contain US passport")
    }

    @Test
    fun testPassportUSOnlyDigits8() {
        val text = "Passport 12345678"
        assertEquals(0, scanText(text), "8 digits without letter should not be found")
    }

    @Test
    fun testPassportUSRandom() {
        val text = "passport number: A98765432"
        assertTrue(scanText(text) >= 1, "Random valid US passport should be found")
    }

    @Test
    fun testPassportUSInSentence() {
        val text = "Please bring your passport A98765432 to the airport."
        assertTrue(scanText(text) >= 1, "US passport in sentence should be found")
    }

    @Test
    fun testPassportUSWithColonNoSpace() {
        val text = "Passport: C98765432 "
        assertTrue(scanText(text) >= 1, "US passport with colon and space should be found")
    }

    @Test
    fun testPassportUSExcludedA12345678() {
        val text = "Passport A12345678"
        assertEquals(0, scanText(text), "US passport A12345678 should not be found (excluded)")
    }

    @Test
    fun testPassportUSExcludedC12345678() {
        val text = "Passport C12345678"
        assertEquals(0, scanText(text), "US passport C12345678 should not be found (excluded)")
    }

    @Test
    fun testPassportUSInvalidLetterI() {
        val text = "Passport I12345678"
        assertEquals(0, scanText(text), "US passport with letter I should not be found")
    }

    @Test
    fun testPassportUSInvalidLetterO() {
        val text = "Passport O12345678"
        assertEquals(0, scanText(text), "US passport with letter O should not be found")
    }

    @Test
    fun testPassportUSAllSameCharacters() {
        val text = "Passport AAAAAAAAA"
        assertEquals(0, scanText(text), "US passport with all same characters should not be found")
    }
}

