package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера SEVIS ID
 */
internal class SEVISIDTest : MatcherTestBase(SEVISID) {

    // Валидные SEVIS IDs для тестов
    private val validSEVIS1 = "N1234567890" // 10 цифр
    private val validSEVIS2 = "N0001234567" // С ведущими нулями
    private val validSEVIS3 = "N9876543210" // Обратный порядок

    // ========== 1. Базовые тесты формата ==========

    @Test
    fun testBasicFormat() {
        val text = "N1234567890"
        assertTrue(scanText(text) >= 1, "SEVIS ID должен быть найден")
    }

    @Test
    fun testWithLeadingZeros() {
        val text = "N0001234567"
        assertTrue(scanText(text) >= 1, "SEVIS ID с ведущими нулями должен быть найден")
    }

    @Test
    fun testLowerCaseN() {
        val text = "n1234567890"
        assertTrue(scanText(text) >= 1, "SEVIS ID с маленькой буквой 'n' должен быть найден")
    }

    // ========== 2. Позиция совпадения в тексте ==========

    @Test
    fun testAtStartOfText() {
        val text = "N1234567890"
        assertTrue(scanText(text) >= 1, "SEVIS ID в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "SEVIS ID: N1234567890"
        assertTrue(scanText(text) >= 1, "SEVIS ID в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The SEVIS ID N1234567890 is valid"
        assertTrue(scanText(text) >= 1, "SEVIS ID в середине текста должен быть найден")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Number N1234567890"
        assertTrue(scanText(text) >= 1, "SEVIS ID с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "N1234567890 is valid"
        assertTrue(scanText(text) >= 1, "SEVIS ID с пробелом после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "SEVIS ID: N1234567890"
        assertTrue(scanText(text) >= 1, "SEVIS ID с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithComma() {
        val text = "SEVIS ID: N1234567890, next"
        assertTrue(scanText(text) >= 1, "SEVIS ID с запятой должен быть найден")
    }

    @Test
    fun testWithDot() {
        val text = "SEVIS ID: N1234567890."
        assertTrue(scanText(text) >= 1, "SEVIS ID с точкой должен быть найден")
    }

    @Test
    fun testWithParentheses() {
        val text = "(N1234567890)"
        assertTrue(scanText(text) >= 1, "SEVIS ID в скобках должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nN1234567890"
        assertTrue(scanText(text) >= 1, "SEVIS ID после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "N1234567890\n"
        assertTrue(scanText(text) >= 1, "SEVIS ID перед \\n должен быть найден")
    }

    // ========== 3. Негативные сценарии ==========

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abcN1234567890def"
        assertEquals(0, scanText(text), "SEVIS ID внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123N1234567890456"
        assertEquals(0, scanText(text), "SEVIS ID внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать SEVIS ID")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no SEVIS IDs"
        assertEquals(0, scanText(text), "Текст без SEVIS ID не должен находить совпадения")
    }

    @Test
    fun testTooShort() {
        val text = "N123456789" // 9 цифр вместо 10
        assertEquals(0, scanText(text), "Слишком короткий SEVIS ID не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "N12345678901" // 11 цифр вместо 10
        assertEquals(0, scanText(text), "Слишком длинный SEVIS ID не должен находиться")
    }

    @Test
    fun testLettersInDigits() {
        val text = "N123456789A" // Буква вместо цифры
        assertEquals(0, scanText(text), "SEVIS ID с буквами в цифровой части не должен находиться")
    }

    @Test
    fun testNoLetterPrefix() {
        val text = "1234567890" // Нет буквы N
        assertEquals(0, scanText(text), "SEVIS ID без буквы N не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "N0000000000" // Все нули в цифровой части
        assertEquals(0, scanText(text), "SEVIS ID со всеми нулями не должен находиться")
    }

    @Test
    fun testAllSameDigits() {
        val text = "N1111111111" // Все единицы
        assertEquals(0, scanText(text), "SEVIS ID с одинаковыми цифрами не должен находиться")
    }

    @Test
    fun testOnlyN() {
        val text = "N" // Только буква N
        assertEquals(0, scanText(text), "Только буква N не должна находиться")
    }

    @Test
    fun testMultipleSEVISIDs() {
        val text = "N1234567890 and N9876543210"
        assertTrue(scanText(text) >= 2, "Несколько SEVIS IDs должны быть найдены")
    }

    @Test
    fun testOnI20Form() {
        val text = "I-20 Form SEVIS ID: N1234567890"
        assertTrue(scanText(text) >= 1, "SEVIS ID на форме I-20 должен быть найден")
    }

    @Test
    fun testOnDS2019Form() {
        val text = "DS-2019 SEVIS ID N1234567890"
        assertTrue(scanText(text) >= 1, "SEVIS ID на форме DS-2019 должен быть найден")
    }
}