package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера USCIS
 */
internal class USCISTest : MatcherTestBase(USCIS) {

    // Валидные USCIS Receipt Numbers для тестов
    private val validEAC = "EAC1234567890" // Eastern Adjudication Center
    private val validWAC = "WAC1234567890" // Western Adjudication Center
    private val validLIN = "LIN1234567890" // Lincoln Service Center
    private val validSRC = "SRC1234567890" // Texas Service Center
    private val validNBC = "NBC1234567890" // National Benefits Center
    private val validMSC = "MSC1234567890" // Missouri Service Center
    private val validIOE = "IOE1234567890" // Electronic filing

    // ========== 1. Базовые тесты формата для каждого префикса ==========

    @Test
    fun testEACFormat() {
        val text = "EAC1234567890"
        assertTrue(scanText(text) >= 1, "EAC формат должен быть найден")
    }

    @Test
    fun testWACFormat() {
        val text = "WAC1234567890"
        assertTrue(scanText(text) >= 1, "WAC формат должен быть найден")
    }

    @Test
    fun testLINFormat() {
        val text = "LIN1234567890"
        assertTrue(scanText(text) >= 1, "LIN формат должен быть найден")
    }

    @Test
    fun testSRCFormat() {
        val text = "SRC1234567890"
        assertTrue(scanText(text) >= 1, "SRC формат должен быть найден")
    }

    @Test
    fun testNBCFormat() {
        val text = "NBC1234567890"
        assertTrue(scanText(text) >= 1, "NBC формат должен быть найден")
    }

    @Test
    fun testMSCFormat() {
        val text = "MSC1234567890"
        assertTrue(scanText(text) >= 1, "MSC формат должен быть найден")
    }

    @Test
    fun testIOEFormat() {
        val text = "IOE1234567890"
        assertTrue(scanText(text) >= 1, "IOE формат должен быть найден")
    }

    @Test
    fun testLowerCasePrefix() {
        val text = "eac1234567890"
        assertTrue(scanText(text) >= 1, "USCIS с префиксом в нижнем регистре должен быть найден")
    }

    @Test
    fun testMixedCasePrefix() {
        val text = "EaC1234567890"
        assertTrue(scanText(text) >= 1, "USCIS со смешанным регистром префикса должен быть найден")
    }

    // ========== 2. Позиция совпадения в тексте ==========

    @Test
    fun testAtStartOfText() {
        val text = "EAC1234567890"
        assertTrue(scanText(text) >= 1, "USCIS в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "USCIS: EAC1234567890"
        assertTrue(scanText(text) >= 1, "USCIS в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The receipt number EAC1234567890 is valid"
        assertTrue(scanText(text) >= 1, "USCIS в середине текста должен быть найден")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Receipt EAC1234567890"
        assertTrue(scanText(text) >= 1, "USCIS с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "EAC1234567890 is valid"
        assertTrue(scanText(text) >= 1, "USCIS с пробелом после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "USCIS: EAC1234567890"
        assertTrue(scanText(text) >= 1, "USCIS с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithComma() {
        val text = "Receipt: EAC1234567890, next"
        assertTrue(scanText(text) >= 1, "USCIS с запятой должен быть найден")
    }

    @Test
    fun testWithDot() {
        val text = "Receipt: EAC1234567890."
        assertTrue(scanText(text) >= 1, "USCIS с точкой должен быть найден")
    }

    @Test
    fun testWithParentheses() {
        val text = "(EAC1234567890)"
        assertTrue(scanText(text) >= 1, "USCIS в скобках должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nEAC1234567890"
        assertTrue(scanText(text) >= 1, "USCIS после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "EAC1234567890\n"
        assertTrue(scanText(text) >= 1, "USCIS перед \\n должен быть найден")
    }

    // ========== 3. Негативные сценарии ==========

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abcEAC1234567890def"
        assertEquals(0, scanText(text), "USCIS внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123EAC1234567890456"
        assertEquals(0, scanText(text), "USCIS внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать USCIS")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no USCIS receipt numbers"
        assertEquals(0, scanText(text), "Текст без USCIS не должен находить совпадения")
    }

    @Test
    fun testInvalidPrefix() {
        val text = "ABC1234567890" // Неправильный префикс
        assertEquals(0, scanText(text), "USCIS с неправильным префиксом не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "EAC123456789" // 12 символов вместо 13 (не хватает одной цифры)
        assertEquals(0, scanText(text), "Слишком короткий USCIS не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "EAC12345678901" // 14 символов вместо 13 (лишняя цифра)
        assertEquals(0, scanText(text), "Слишком длинный USCIS не должен находиться")
    }

    @Test
    fun testLettersInDigits() {
        val text = "EAC123456789A" // Буква вместо цифры
        assertEquals(0, scanText(text), "USCIS с буквами в цифровой части не должен находиться")
    }

    @Test
    fun testDigitsInPrefix() {
        val text = "EA11234567890" // Цифра вместо буквы в префиксе
        assertEquals(0, scanText(text), "USCIS с цифрами в префиксе не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "EAC0000000000" // Все нули в цифровой части
        assertEquals(0, scanText(text), "USCIS со всеми нулями не должен находиться")
    }

    @Test
    fun testAllSameDigits() {
        val text = "EAC1111111111" // Все единицы
        assertEquals(0, scanText(text), "USCIS с одинаковыми цифрами не должен находиться")
    }

    @Test
    fun testMultipleReceipts() {
        val text = "EAC1234567890 and WAC9876543210"
        assertTrue(scanText(text) >= 2, "Несколько USCIS должны быть найдены")
    }
}