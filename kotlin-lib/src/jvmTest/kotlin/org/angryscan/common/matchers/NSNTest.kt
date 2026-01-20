package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера NSN (National Stock Number)
 */
internal class NSNTest : MatcherTestBase(NSN) {

    // Валидные NSN для тестов (не последовательные, не повторяющиеся)
    private val valid13Digits = "7515847293648" // 13 цифр
    private val validWithHyphens = "7515-84-729-3648" // С дефисами

    // ========== 1. Базовые тесты формата ==========

    @Test
    fun test13DigitFormat() {
        val text = "7515847293648"
        assertTrue(scanText(text) >= 1, "NSN с 13 цифрами должен быть найден")
    }

    @Test
    fun testWithHyphensFormat() {
        val text = "7515-84-729-3648"
        assertTrue(scanText(text) >= 1, "NSN с дефисами должен быть найден")
    }

    @Test
    fun testBothFormats() {
        val text1 = "7515847293648"
        val text2 = "7515-84-729-3648"
        assertTrue(scanText(text1) >= 1, "NSN в формате 13 цифр должен быть найден")
        assertTrue(scanText(text2) >= 1, "NSN в формате с дефисами должен быть найден")
    }

    // ========== 2. Позиция совпадения в тексте ==========

    @Test
    fun testAtStartOfText() {
        val text = "7515847293648"
        assertTrue(scanText(text) >= 1, "NSN в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "NSN: 7515847293648"
        assertTrue(scanText(text) >= 1, "NSN в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The NSN 7515847293648 is valid"
        assertTrue(scanText(text) >= 1, "NSN в середине текста должен быть найден")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Number 7515847293648"
        assertTrue(scanText(text) >= 1, "NSN с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "7515847293648 is valid"
        assertTrue(scanText(text) >= 1, "NSN с пробелом после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "NSN: 7515847293648"
        assertTrue(scanText(text) >= 1, "NSN с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithComma() {
        val text = "NSN: 7515847293648, next"
        assertTrue(scanText(text) >= 1, "NSN с запятой должен быть найден")
    }

    @Test
    fun testWithDot() {
        val text = "NSN: 7515847293648."
        assertTrue(scanText(text) >= 1, "NSN с точкой должен быть найден")
    }

    @Test
    fun testWithParentheses() {
        val text = "(7515847293648)"
        assertTrue(scanText(text) >= 1, "NSN в скобках должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n7515847293648"
        assertTrue(scanText(text) >= 1, "NSN после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "7515847293648\n"
        assertTrue(scanText(text) >= 1, "NSN перед \\n должен быть найден")
    }

    @Test
    fun testHyphenatedFormatWithSpaces() {
        val text = "NSN: 7515-84-729-3648"
        assertTrue(scanText(text) >= 1, "NSN с дефисами и пробелами должен быть найден")
    }

    // ========== 3. Негативные сценарии ==========

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc7515847293648def"
        assertEquals(0, scanText(text), "NSN внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "1237515847293648456"
        assertEquals(0, scanText(text), "NSN внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать NSN")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no NSNs"
        assertEquals(0, scanText(text), "Текст без NSN не должен находить совпадения")
    }

    @Test
    fun testTooShort13Digit() {
        val text = "123456789012" // 12 цифр вместо 13
        assertEquals(0, scanText(text), "Слишком короткий 13-значный NSN не должен находиться")
    }

    @Test
    fun testTooLong13Digit() {
        val text = "75158472936484" // 14 цифр вместо 13
        assertEquals(0, scanText(text), "Слишком длинный 13-значный NSN не должен находиться")
    }

    @Test
    fun testTooShortHyphenatedFormat() {
        val text = "123-56-789-0123" // 3-2-3-4 вместо 4-2-3-4
        assertEquals(0, scanText(text), "Слишком короткий NSN с дефисами не должен находиться")
    }

    @Test
    fun testTooLongHyphenatedFormat() {
        val text = "12345-56-789-0123" // 5-2-3-4 вместо 4-2-3-4
        assertEquals(0, scanText(text), "Слишком длинный NSN с дефисами не должен находиться")
    }

    @Test
    fun testWrongHyphenPosition() {
        val text = "1234-5-789-0123" // Неправильная позиция дефисов
        assertEquals(0, scanText(text), "NSN с неправильными позициями дефисов не должен находиться")
    }

    @Test
    fun testMissingHyphens() {
        val text = "7515847293648" // 13 цифр без дефисов - это валидно
        assertTrue(scanText(text) >= 1, "NSN без дефисов должен находиться")
    }

    @Test
    fun testLettersInDigits() {
        val text = "123456789012A" // Буква вместо цифры
        assertEquals(0, scanText(text), "NSN с буквами не должен находиться")
    }

    @Test
    fun testAllZeros13Digit() {
        val text = "0000000000000" // Все нули
        assertEquals(0, scanText(text), "NSN со всеми нулями не должен находиться")
    }

    @Test
    fun testAllSameDigits13Digit() {
        val text = "1111111111111" // Все единицы
        assertEquals(0, scanText(text), "NSN с одинаковыми цифрами не должен находиться")
    }

    @Test
    fun testAllZerosHyphenated() {
        val text = "0000-00-000-0000" // Все нули с дефисами
        assertEquals(0, scanText(text), "NSN со всеми нулями и дефисами не должен находиться")
    }

    @Test
    fun testAllSameDigitsHyphenated() {
        val text = "2222-22-222-2222" // Все двойки
        assertEquals(0, scanText(text), "NSN с одинаковыми цифрами и дефисами не должен находиться")
    }

    @Test
    fun testMultipleNSNs() {
        val text = "7515847293648 and 4827-59-173-6429"
        assertTrue(scanText(text) >= 2, "Несколько NSN должны быть найдены")
    }

    @Test
    fun testMixedFormats() {
        val text = "NSN 1: 7515847293648, NSN 2: 7515-84-729-3648"
        assertTrue(scanText(text) >= 2, "Оба формата NSN должны быть найдены")
    }

    @Test
    fun testHyphenatedFormatInText() {
        val text = "The stock number is 7515-84-729-3648"
        assertTrue(scanText(text) >= 1, "NSN с дефисами в тексте должен быть найден")
    }

    // ========== 4. Тесты дополнительной фильтрации для снижения false positives ==========
    // Примечание: для 13-значных чисел сложно найти полностью последовательные или повторяющиеся паттерны,
    // которые делятся нацело, поэтому некоторые тесты могут быть пропущены

    // ========== 5. Тесты с ключевыми словами ==========

    @Test
    fun testWithNSNKeyword() {
        val text = "NSN: 7515847293648"
        assertTrue(scanText(text) >= 1, "NSN с ключевым словом 'NSN' должен быть найден")
    }

    @Test
    fun testWithNSNLowercase() {
        val text = "nsn: 7515-84-729-3648"
        assertTrue(scanText(text) >= 1, "NSN с ключевым словом 'nsn' в нижнем регистре должен быть найден")
    }

    @Test
    fun testWithNationalStockNumber() {
        val text = "National Stock Number: 4827591736429"
        assertTrue(scanText(text) >= 1, "NSN с полным ключевым словом 'National Stock Number' должен быть найден")
    }

    @Test
    fun testWithNATOStockNumber() {
        val text = "NATO Stock Number: 7515-84-729-3648"
        assertTrue(scanText(text) >= 1, "NSN с ключевым словом 'NATO Stock Number' должен быть найден")
    }

    @Test
    fun testWithStockNumber() {
        val text = "Stock Number: 4827-59-173-6429"
        assertTrue(scanText(text) >= 1, "NSN с ключевым словом 'Stock Number' должен быть найден")
    }

    @Test
    fun testWithKeywordsAndSeparators() {
        val text = "NSN - 7515847293648"
        assertTrue(scanText(text) >= 1, "NSN с ключевым словом и разделителем должен быть найден")
    }

    @Test
    fun testWithKeywordsCaseInsensitive() {
        val text = "NSN NUMBER: 7515847293648"
        assertTrue(scanText(text) >= 1, "NSN с ключевыми словами в верхнем регистре должен быть найден")
    }
}