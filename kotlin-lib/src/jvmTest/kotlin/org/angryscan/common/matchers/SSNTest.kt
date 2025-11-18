package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера SSN (Social Security Number)
 */
internal class SSNTest: MatcherTestBase(SSN) {

    @Test
    fun testSSNAtStart() {
        val text = " 123-45-6780 is my social security number"
        assertEquals(1, scanText(text), "SSN at the beginning should be found")
    }

    @Test
    fun testSSNAtEnd() {
        val text = "Social Security Number: 123-45-6780 "
        assertEquals(1, scanText(text), "SSN at the end should be found")
    }

    @Test
    fun testSSNInMiddle() {
        val text = "Citizen has SSN 123-45-6780 for employment"
        assertEquals(1, scanText(text), "SSN in the middle should be found")
    }

    @Test
    fun testSSNStandalone() {
        val text = " 123-45-6780 "
        assertEquals(1, scanText(text), "SSN as standalone string should be found")
    }

    @Test
    fun testSSNWithDashes() {
        val text = " 123-45-6780 "
        assertEquals(1, scanText(text), "SSN with dashes should be found")
    }

    @Test
    fun testSSNWithSpaces() {
        val text = "123 45 6780"
        assertEquals(0, scanText(text), "SSN with spaces should not be found (pattern requires dashes)")
    }

    @Test
    fun testSSNWithoutSeparators() {
        val text = "123456780"
        assertEquals(0, scanText(text), "SSN without separators should not be found (pattern requires dashes)")
    }

    @Test
    fun testSSNMixedSeparators() {
        val text = "123-45 6780"
        assertEquals(0, scanText(text), "SSN with mixed separators should not be found (pattern requires dashes)")
    }

    @Test
    fun testSSNValidArea001() {
        val text = "001-23-4567"
        assertEquals(1, scanText(text), "SSN with valid area 001 should be found")
    }

    @Test
    fun testSSNValidArea899() {
        val text = "899-12-3456"
        assertEquals(1, scanText(text), "SSN with valid area 899 should be found")
    }

    @Test
    fun testSSNValidArea500() {
        val text = "500-12-3456"
        assertEquals(1, scanText(text), "SSN with valid area 500 should be found")
    }

    @Test
    fun testSSNInParentheses() {
        val text = "Number (123-45-6780) provided"
        assertEquals(1, scanText(text), "SSN in parentheses should be found")
    }

    @Test
    fun testSSNInQuotes() {
        val text = "SSN \"123-45-6780\" verified"
        assertEquals(1, scanText(text), "SSN in quotes should be found")
    }

    @Test
    fun testSSNWithPunctuation() {
        val text = "Number: 123-45-6780."
        assertEquals(1, scanText(text), "SSN with punctuation should be found")
    }

    @Test
    fun testSSNInBrackets() {
        val text = "Number [123-45-6780] shown"
        assertEquals(1, scanText(text), "SSN in brackets should be found")
    }

    @Test
    fun testSSNWithColon() {
        val text = "SSN: 123-45-6780"
        assertEquals(1, scanText(text), "SSN with colon should be found")
    }

    @Test
    fun testSSNWithSemicolon() {
        val text = "SSN; 123-45-6780"
        assertEquals(1, scanText(text), "SSN with semicolon should be found")
    }

    @Test
    fun testSSNWithComma() {
        val text = "SSN, 123-45-6780"
        assertEquals(1, scanText(text), "SSN with comma should be found")
    }

    @Test
    fun testSSNWithExclamation() {
        val text = "SSN! 123-45-6780"
        assertEquals(1, scanText(text), "SSN with exclamation should be found")
    }

    @Test
    fun testSSNWithQuestion() {
        val text = "SSN? 123-45-6780"
        assertEquals(1, scanText(text), "SSN with question mark should be found")
    }

    @Test
    fun testSSNMultiline() {
        val text = """SSN:
123-45-6780"""
        assertEquals(1, scanText(text), "SSN on next line should be found")
    }

    @Test
    fun testMultipleSSNs() {
        val text = """
            First: 123-45-6780 
            Second: 234-56-7890 
            Third: 345-67-8901 
        """.trimIndent()
        assertEquals(3, scanText(text), "Multiple SSNs should be found")
    }

    @Test
    fun testSSNValidGroup01() {
        val text = "123-01-4567"
        assertEquals(1, scanText(text), "SSN with valid group 01 should be found")
    }

    @Test
    fun testSSNValidGroup99() {
        val text = "123-99-4567"
        assertEquals(1, scanText(text), "SSN with valid group 99 should be found")
    }

    @Test
    fun testSSNValidSerial0001() {
        val text = "123-45-0001"
        assertEquals(1, scanText(text), "SSN with valid serial 0001 should be found")
    }

    @Test
    fun testSSNValidSerial9999() {
        val text = "123-45-9999"
        assertEquals(1, scanText(text), "SSN with valid serial 9999 should be found")
    }

    @Test
    fun testSSNInvalidArea000() {
        val text = "000-12-3456"
        assertEquals(0, scanText(text), "SSN with invalid area 000 should not be found")
    }

    @Test
    fun testSSNInvalidArea666() {
        val text = "666-12-3456"
        assertEquals(0, scanText(text), "SSN with invalid area 666 should not be found")
    }

    @Test
    fun testSSNInvalidArea900() {
        val text = "900-12-3456"
        assertEquals(0, scanText(text), "SSN with invalid area 900 should not be found")
    }

    @Test
    fun testSSNInvalidArea999() {
        val text = "999-12-3456"
        assertEquals(0, scanText(text), "SSN with invalid area 999 should not be found")
    }

    @Test
    fun testSSNInvalidGroup00() {
        val text = "123-00-4567"
        assertEquals(0, scanText(text), "SSN with invalid group 00 should not be found")
    }

    @Test
    fun testSSNInvalidSerial0000() {
        val text = "123-45-0000"
        assertEquals(0, scanText(text), "SSN with invalid serial 0000 should not be found")
    }

    @Test
    fun testSSNInvalidFormat() {
        val text = "12-345-6789"
        assertEquals(0, scanText(text), "SSN with incorrect format should not be found")
    }

    @Test
    fun testSSNTooShort() {
        val text = "123-45"
        assertEquals(0, scanText(text), "Too short SSN should not be found")
    }

    @Test
    fun testSSNTooLong() {
        val text = "123-45-6780-01"
        assertEquals(0, scanText(text), "Too long SSN should not be found")
    }

    @Test
    fun testSSNWithLetters() {
        val text = "ABC-DE-FGHI"
        assertEquals(0, scanText(text), "SSN with letters should not be found")
    }

    @Test
    fun testSSNEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Empty string should not contain SSN")
    }

    @Test
    fun testSSNPartOfLongerNumber() {
        val text = "12345678901234"
        assertEquals(0, scanText(text), "SSN as part of longer number should not be found")
    }

    @Test
    fun testSSNMultipleInSentence() {
        val text = "John's SSN is 123-45-6780 and Jane's is 234-56-7890"
        assertEquals(2, scanText(text), "Multiple SSNs in sentence should be found")
    }

    @Test
    fun testSSNRealWorldExample1() {
        val text = "Please provide your SSN (123-45-6780) for verification. "
        assertEquals(1, scanText(text), "Real world example 1 should be found")
    }

    @Test
    fun testSSNRealWorldExample2() {
        val text = """
            Employee Information:
            Name: John Doe
            SSN: 123-45-6780
            Department: IT
        """.trimIndent()
        assertEquals(1, scanText(text), "Real world example 2 should be found")
    }

    @Test
    fun testSSNBoundaryCheck() {
        val text = " 123-45-6780 "
        assertEquals(1, scanText(text), "SSN with boundary spaces should be found")
    }
}

