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
 * Тесты для проверки пограничных значений матчера LicensePlateUS (американские номерные знаки)
 * Основной акцент на различных форматах номеров БЕЗ ключевых слов
 */
internal class LicensePlateUSTest {

    // === Тесты с ключевыми словами (минимальный набор для проверки работоспособности) ===
    
    @Test
    fun testLicensePlateUSWithKeywordPlate() {
        val text = "License plate ABC 1234"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Plate with keyword 'License plate' should be found")
    }

    @Test
    fun testLicensePlateUSWithKeywordCar() {
        val text = "Car ABC 1234"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Plate with keyword 'Car' should be found")
    }

    @Test
    fun testLicensePlateUSWithKeywordTag() {
        val text = "Tag: XYZ-789"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Plate with keyword 'Tag' should be found")
    }

    // === Тесты БЕЗ ключевых слов - различные форматы (3 буквы + цифры) ===
    
    @Test
    fun testFormat3Letters3DigitsWithSpace() {
        val text = "ABC 123"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format ABC 123 should be found")
    }

    @Test
    fun testFormat3Letters3DigitsWithDash() {
        val text = "XYZ-456"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format XYZ-456 should be found")
    }

    @Test
    fun testFormat3Letters3DigitsNoSeparator() {
        val text = "DEF789"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format DEF789 should be found")
    }

    @Test
    fun testFormat3Letters4DigitsWithSpace() {
        val text = "ABC 1234"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format ABC 1234 should be found")
    }

    @Test
    fun testFormat3Letters4DigitsWithDash() {
        val text = "XYZ-5678"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format XYZ-5678 should be found")
    }

    @Test
    fun testFormat3Letters4DigitsNoSeparator() {
        val text = "GHI9012"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format GHI9012 should be found")
    }

    // === Тесты БЕЗ ключевых слов - различные форматы (2 буквы + цифры) ===
    
    @Test
    fun testFormat2Letters4DigitsWithSpace() {
        val text = "AB 1234"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format AB 1234 should be found")
    }

    @Test
    fun testFormat2Letters4DigitsWithDash() {
        val text = "CD-5678"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format CD-5678 should be found")
    }

    @Test
    fun testFormat2Letters4DigitsNoSeparator() {
        val text = "EF9012"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format EF9012 should be found")
    }

    @Test
    fun testFormat2Letters5DigitsWithSpace() {
        val text = "AB 12345"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format AB 12345 (California style) should be found")
    }

    @Test
    fun testFormat2Letters5DigitsWithDash() {
        val text = "TX-98765"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format TX-98765 should be found")
    }

    @Test
    fun testFormat2Letters5DigitsNoSeparator() {
        val text = "NY54321"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format NY54321 should be found")
    }

    // === Тесты БЕЗ ключевых слов - форматы цифры + буквы ===
    
    @Test
    fun testFormat3Digits3LettersWithSpace() {
        val text = "123 ABC"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format 123 ABC should be found")
    }

    @Test
    fun testFormat3Digits3LettersWithDash() {
        val text = "456-XYZ"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format 456-XYZ should be found")
    }

    @Test
    fun testFormat3Digits3LettersNoSeparator() {
        val text = "789DEF"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format 789DEF should be found")
    }

    @Test
    fun testFormat2Digits3LettersWithSpace() {
        val text = "12 ABC"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format 12 ABC should be found")
    }

    @Test
    fun testFormat2Digits3LettersWithDash() {
        val text = "34-XYZ"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format 34-XYZ should be found")
    }

    @Test
    fun testFormat2Digits2LettersWithSpace() {
        val text = "56 AB"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Format 56 AB should be found")
    }

    // === Тесты БЕЗ ключевых слов - California формат (1 цифра + 3 буквы + 3 цифры) ===
    
    @Test
    fun testCaliforniaFormatNoSeparator() {
        val text = "7ABC123"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "California format 7ABC123 should be found")
    }

    @Test
    fun testCaliforniaFormatWithSpace() {
        val text = "5XYZ 789"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "California format with space should be found")
    }

    @Test
    fun testCaliforniaFormatWithDash() {
        val text = "9DEF-456"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "California format with dash should be found")
    }

    // === Тесты на позиции в тексте (БЕЗ ключевых слов) ===
    
    @Test
    fun testPlateAtStartOfText() {
        val text = "ABC 1234 was reported stolen"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Plate at the start of text should be found")
    }

    @Test
    fun testPlateAtEndOfText() {
        val text = "The stolen vehicle is ABC 1234"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Plate at the end of text should be found")
    }

    @Test
    fun testPlateInMiddleOfText() {
        val text = "Vehicle ABC 1234 was seen yesterday"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Plate in the middle of text should be found")
    }

    @Test
    fun testPlateStandalone() {
        val text = "ABC 1234"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Standalone plate should be found")
    }

    @Test
    fun testPlateInParentheses() {
        val text = "(ABC 1234)"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Plate in parentheses should be found")
    }

    @Test
    fun testPlateInQuotes() {
        val text = "\"XYZ 5678\""
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Plate in quotes should be found")
    }

    @Test
    fun testPlateWithPunctuationAround() {
        val text = "Vehicle: ABC 1234."
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Plate with punctuation around should be found")
    }

    @Test
    fun testPlateAfterComma() {
        val text = "Vehicles: ABC 123, XYZ 456, DEF 789"
        assertTrue(scanText(text, LicensePlateUS) >= 3, "Multiple plates separated by commas should be found")
    }

    // === Тесты на регистр (БЕЗ ключевых слов) ===
    
    @Test
    fun testPlateUpperCase() {
        val text = "ABC 1234"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Uppercase plate should be found")
    }

    @Test
    fun testPlateLowerCase() {
        val text = "abc 1234"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Lowercase plate should be found")
    }

    @Test
    fun testPlateMixedCase() {
        val text = "AbC 1234"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "Mixed case plate should be found")
    }

    // === Тесты на несколько номеров ===
    
    @Test
    fun testMultiplePlatesInText() {
        val text = """
            ABC 1234
            XYZ 5678
            DEF 9012
        """.trimIndent()
        assertTrue(scanText(text, LicensePlateUS) >= 3, "Multiple plates should be found")
    }

    @Test
    fun testMultiplePlatesWithDifferentFormats() {
        val text = "AB 12345, XYZ-456, 7DEF890"
        assertTrue(scanText(text, LicensePlateUS) >= 3, "Multiple plates with different formats should be found")
    }

    // === Негативные тесты (пограничные случаи которые НЕ должны находиться) ===
    
    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, LicensePlateUS), "Empty string should not match")
    }

    @Test
    fun testOnlyLetters() {
        val text = "ABCDEF"
        assertEquals(0, scanText(text, LicensePlateUS), "Only letters should not match")
    }

    @Test
    fun testOnlyDigits() {
        val text = "123456"
        assertEquals(0, scanText(text, LicensePlateUS), "Only digits should not match")
    }

    @Test
    fun testFormat2Letters3Digits() {
        val text = "AB 123"
        assertTrue(scanText(text, LicensePlateUS) >= 1, "AB 123 format should match")
    }

    @Test
    fun testTooShort1Letter4Digits() {
        val text = "A 1234"
        assertEquals(0, scanText(text, LicensePlateUS), "A 1234 (1 letter) should not match")
    }

    @Test
    fun testTooLong4Letters5Digits() {
        val text = "ABCD 12345"
        assertEquals(0, scanText(text, LicensePlateUS), "ABCD 12345 (too long) should not match")
    }

    @Test
    fun testTooLong4Letters() {
        val text = "ABCD 123"
        assertEquals(0, scanText(text, LicensePlateUS), "ABCD 123 (4 letters) should not match")
    }

    @Test
    fun testTooShortDigits2() {
        val text = "ABC 12"
        assertEquals(0, scanText(text, LicensePlateUS), "ABC 12 (2 digits) should not match")
    }

    @Test
    fun testTooLongDigits6() {
        val text = "ABC 123456"
        assertEquals(0, scanText(text, LicensePlateUS), "ABC 123456 (6 digits) should not match")
    }

    @Test
    fun testInvalidPattern() {
        val text = "A1B2C3"
        assertEquals(0, scanText(text, LicensePlateUS), "Mixed letters and digits pattern should not match")
    }

    @Test
    fun testPlateWithSpecialCharacters() {
        val text = "AB@1234"
        assertEquals(0, scanText(text, LicensePlateUS), "Plate with @ should not match")
    }

    @Test
    fun testPlateWithDots() {
        val text = "AB.1234"
        assertEquals(0, scanText(text, LicensePlateUS), "Plate with dots should not match")
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


