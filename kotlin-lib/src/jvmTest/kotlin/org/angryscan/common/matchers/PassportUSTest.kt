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
 * Тесты для проверки крайних позиций и пограничных значений матчера PassportUS (американские паспорта)
 */
internal class PassportUSTest {

    @Test
    fun testPassportUSAtStart() {
        val text = "Passport 123456789 issued in 2020"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport at the beginning should be found")
    }

    @Test
    fun testPassportUSAtEnd() {
        val text = "Document: Passport 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport at the end should be found")
    }

    @Test
    fun testPassportUSInMiddle() {
        val text = "Citizen presented passport 123456789 for verification"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport in the middle should be found")
    }

    @Test
    fun testPassportUSStandalone() {
        val text = "Passport 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport as standalone string should be found")
    }

    @Test
    fun testPassportUS9Digits() {
        val text = "Passport: 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with 9 digits should be found")
    }

    @Test
    fun testPassportUSLetterPlus8Digits() {
        val text = "Passport: C12345678"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with letter + 8 digits should be found")
    }

    @Test
    fun testPassportUSWithColon() {
        val text = "Passport: 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with colon should be found")
    }

    @Test
    fun testPassportUSWithDash() {
        val text = "Passport-123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with dash should be found")
    }

    @Test
    fun testPassportUSWithSpaces() {
        val text = "Passport 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with spaces should be found")
    }

    @Test
    fun testPassportUSWithNumber() {
        val text = "Passport Number: 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with 'Passport Number' should be found")
    }

    @Test
    fun testPassportUSWithNo() {
        val text = "Passport No: 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with 'Passport No' should be found")
    }

    @Test
    fun testPassportUSWithHash() {
        val text = "Passport #123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with '#' should be found")
    }

    @Test
    fun testPassportUSShortForm() {
        val text = "Pass. No. 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with 'Pass. No.' should be found")
    }

    @Test
    fun testPassportUSUpperCase() {
        val text = "PASSPORT 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport in uppercase should be found")
    }

    @Test
    fun testPassportUSLowerCase() {
        val text = "passport 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport in lowercase should be found")
    }

    @Test
    fun testPassportUSMixedCase() {
        val text = "PaSpOrT 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport in mixed case should be found")
    }

    @Test
    fun testPassportUSWithUSPrefix() {
        val text = "US Passport: 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with 'US' prefix should be found")
    }

    @Test
    fun testPassportUSInParentheses() {
        val text = "(Passport 123456789)"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport in parentheses should be found")
    }

    @Test
    fun testPassportUSInQuotes() {
        val text = "\"Passport 123456789\""
        assertTrue(scanText(text, PassportUS) >= 1, "US passport in quotes should be found")
    }

    @Test
    fun testPassportUSWithPunctuation() {
        val text = "Document: Passport 123456789."
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with punctuation should be found")
    }

    @Test
    fun testPassportUSStandaloneNumber() {
        val text = "123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport standalone 9-digit number should be found")
    }

    @Test
    fun testPassportUSStandaloneLetterNumber() {
        val text = "C12345678"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport standalone letter+8 digits should be found")
    }

    @Test
    fun testPassportUSWithMultipleSpaces() {
        val text = "Passport    123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with multiple spaces should be found")
    }

    @Test
    fun testPassportUSAfterEquals() {
        val text = "passport=123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport after equals sign should be found")
    }

    @Test
    fun testPassportUSAfterAsterisk() {
        val text = "*123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport after asterisk should be found")
    }

    @Test
    fun testPassportUSStartsWithA() {
        val text = "Passport A12345678"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport starting with A should be found")
    }

    @Test
    fun testPassportUSStartsWithZ() {
        val text = "Passport Z12345678"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport starting with Z should be found")
    }

    @Test
    fun testPassportUSMultipleInText() {
        val text = """
            First: Passport 123456789
            Second: Passport C12345678
            Third: Passport Number 987654321
        """.trimIndent()
        assertTrue(scanText(text, PassportUS) >= 3, "Multiple US passports should be found")
    }

    @Test
    fun testPassportUSWithNewline() {
        val text = "Passport\n123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with newline should be found")
    }

    @Test
    fun testPassportUSWithTab() {
        val text = "Passport\t123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with tab should be found")
    }

    @Test
    fun testPassportUSBoundary000000000() {
        val text = "Passport 000000000"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with all zeros should be found")
    }

    @Test
    fun testPassportUSBoundary999999999() {
        val text = "Passport 999999999"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with all nines should be found")
    }

    @Test
    fun testPassportUSPassNumberShort() {
        val text = "Pass Number 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with 'Pass Number' should be found")
    }

    @Test
    fun testPassportUSPassNoShort() {
        val text = "Pass No 123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with 'Pass No' should be found")
    }

    @Test
    fun testPassportUSTooShort() {
        val text = "Passport 12345"
        assertEquals(0, scanText(text, PassportUS), "Too short passport number should not be found")
    }

    @Test
    fun testPassportUSTooLong() {
        val text = "Passport 1234567890"
        assertEquals(0, scanText(text, PassportUS), "Too long passport number should not be found")
    }

    @Test
    fun testPassportUSInvalidFormat2Letters() {
        val text = "Passport AB12345678"
        assertEquals(0, scanText(text, PassportUS), "Passport with 2 letters should not be found")
    }

    @Test
    fun testPassportUSEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, PassportUS), "Empty string should not contain US passport")
    }

    @Test
    fun testPassportUSOnlyDigits8() {
        val text = "Passport 12345678"
        assertEquals(0, scanText(text, PassportUS), "8 digits without letter should not be found")
    }

    @Test
    fun testPassportUSRandom() {
        val text = "passport number: A98765432"
        assertTrue(scanText(text, PassportUS) >= 1, "Random valid US passport should be found")
    }

    @Test
    fun testPassportUSInSentence() {
        val text = "Please bring your passport 456789123 to the airport."
        assertTrue(scanText(text, PassportUS) >= 1, "US passport in sentence should be found")
    }

    @Test
    fun testPassportUSWithColonNoSpace() {
        val text = "Passport:123456789"
        assertTrue(scanText(text, PassportUS) >= 1, "US passport with colon and no space should be found")
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

