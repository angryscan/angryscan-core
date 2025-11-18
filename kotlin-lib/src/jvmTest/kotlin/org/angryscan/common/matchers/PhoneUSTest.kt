package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера PhoneUS (американские номера телефонов)
 */
internal class PhoneUSTest: MatcherTestBase(PhoneUS) {

    @Test
    fun testPhoneUSAtStart() {
        val text = "+1 (415) 555-1234 is my phone number"
        assertTrue(scanText(text) >= 1, "US phone at the beginning should be found")
    }

    @Test
    fun testPhoneUSAtEnd() {
        val text = "Contact number: +1 (415) 555-1234"
        assertTrue(scanText(text) >= 1, "US phone at the end should be found")
    }

    @Test
    fun testPhoneUSInMiddle() {
        val text = "Call me at +1 (415) 555-1234 for more info"
        assertTrue(scanText(text) >= 1, "US phone in the middle should be found")
    }

    @Test
    fun testPhoneUSStandalone() {
        val text = "+1 (415) 555-1234"
        assertTrue(scanText(text) >= 1, "US phone as standalone string should be found")
    }

    @Test
    fun testPhoneUSWithPlus1() {
        val text = "+1 415 555 1234"
        assertTrue(scanText(text) >= 1, "US phone with +1 should be found")
    }

    @Test
    fun testPhoneUSWith1() {
        val text = "1 415 555 1234"
        assertTrue(scanText(text) >= 1, "US phone with 1 should be found")
    }

    @Test
    fun testPhoneUSWithoutCountryCode() {
        val text = "(415) 555-1234"
        assertTrue(scanText(text) >= 1, "US phone without country code should be found")
    }

    @Test
    fun testPhoneUSWithDashes() {
        val text = "+1-415-555-1234"
        assertTrue(scanText(text) >= 1, "US phone with dashes should be found")
    }

    @Test
    fun testPhoneUSWithDashesNoCountryCode() {
        val text = "415-555-1234"
        assertTrue(scanText(text) >= 1, "US phone with dashes and no country code should be found")
    }

    @Test
    fun testPhoneUSWithParentheses() {
        val text = "+1 (415) 555-1234"
        assertTrue(scanText(text) >= 1, "US phone with parentheses should be found")
    }


    @Test
    fun testPhoneUSAreaCode415() {
        val text = "+1 415 555 1234"
        assertTrue(scanText(text) >= 1, "US phone with area code 415 should be found")
    }

    @Test
    fun testPhoneUSAreaCode212() {
        val text = "+1 212 555 1234"
        assertTrue(scanText(text) >= 1, "US phone with area code 212 (NYC) should be found")
    }

    @Test
    fun testPhoneUSAreaCode310() {
        val text = "+1 310 555 1234"
        assertTrue(scanText(text) >= 1, "US phone with area code 310 (LA) should be found")
    }

    @Test
    fun testPhoneUSAreaCode702() {
        val text = "+1 702 555 1234"
        assertTrue(scanText(text) >= 1, "US phone with area code 702 (Las Vegas) should be found")
    }

    @Test
    fun testPhoneUSAreaCode305() {
        val text = "+1 305 555 1234"
        assertTrue(scanText(text) >= 1, "US phone with area code 305 (Miami) should be found")
    }

    @Test
    fun testPhoneUSInParenthesesFull() {
        val text = "(+1 415 555 1234)"
        assertTrue(scanText(text) >= 1, "US phone in full parentheses should be found")
    }

    @Test
    fun testPhoneUSInQuotes() {
        val text = "\"+1 415 555 1234\""
        assertTrue(scanText(text) >= 1, "US phone in quotes should be found")
    }

    @Test
    fun testPhoneUSWithPunctuation() {
        val text = "Phone: +1 415 555 1234."
        assertTrue(scanText(text) >= 1, "US phone with punctuation should be found")
    }


    @Test
    fun testMultiplePhoneUS() {
        val text = """
            First: +1 415 555 1234
            Second: (212) 555-9876
            Third: 310-555-4567
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Multiple US phones should be found")
    }

    @Test
    fun testPhoneUSWithSpaceFormat1() {
        val text = "1 415 555 1234"
        assertTrue(scanText(text) >= 1, "US phone with spaces format should be found")
    }

    @Test
    fun testPhoneUSWithSpaceFormat2() {
        val text = "415 555 1234"
        assertTrue(scanText(text) >= 1, "US phone with spaces format (no country code) should be found")
    }

    @Test
    fun testPhoneUSInvalidAreaCodeStartsWith0() {
        val text = "+1 015 555 1234"
        assertEquals(0, scanText(text), "US phone with invalid area code (starts with 0) should not be found")
    }

    @Test
    fun testPhoneUSInvalidAreaCodeStartsWith1() {
        val text = "+1 115 555 1234"
        assertEquals(0, scanText(text), "US phone with invalid area code (starts with 1) should not be found")
    }

    @Test
    fun testPhoneUSInvalidExchangeStartsWith0() {
        val text = "+1 415 055 1234"
        assertEquals(0, scanText(text), "US phone with invalid exchange (starts with 0) should not be found")
    }

    @Test
    fun testPhoneUSInvalidExchangeStartsWith1() {
        val text = "+1 415 155 1234"
        assertEquals(0, scanText(text), "US phone with invalid exchange (starts with 1) should not be found")
    }

    @Test
    fun testPhoneUSTooShort() {
        val text = "+1 415 555"
        assertEquals(0, scanText(text), "Too short US phone number should not be found")
    }

    @Test
    fun testPhoneUSEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Empty string should not contain US phone number")
    }

    @Test
    fun testPhoneUSWithoutSeparators() {
        val text = "4155551234"
        assertEquals(0, scanText(text), "US phone without separators should not be found")
    }

    @Test
    fun testPhoneUSWithoutSeparatorsWithCountryCode() {
        val text = "+14155551234"
        assertEquals(0, scanText(text), "US phone without separators (even with country code) should not be found")
    }

    @Test
    fun testPhoneUSInLongNumber() {
        val text = "123456789012344155551234567890"
        assertEquals(0, scanText(text), "US phone pattern inside long number should not be found")
    }

    @Test
    fun testPhoneUS800Number() {
        val text = "1-800-555-1234"
        assertTrue(scanText(text) >= 1, "US toll-free 800 number should be found")
    }

    @Test
    fun testPhoneUS888Number() {
        val text = "1-888-555-1234"
        assertTrue(scanText(text) >= 1, "US toll-free 888 number should be found")
    }

    @Test
    fun testPhoneUS877Number() {
        val text = "1-877-555-1234"
        assertTrue(scanText(text) >= 1, "US toll-free 877 number should be found")
    }
}

