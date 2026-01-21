package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера TCN (Transportation Control Number)
 */
internal class TCNTest : MatcherTestBase(TCN) {

    // Валидные TCN для тестов
    private val validTCN1 = "TCN: HKR0627116X001XXX" // Пример из документации (17 символов)
    private val validTCN2 = "TCN ABCDEF5123A042XXX" // Другой пример (17 символов)
    private val validTCN3 = "TCN 12345678901234567" // Цифровой пример (17 символов)

    // ========== 1. Базовые тесты формата = ключевыми словами ==========

    @Test
    fun testWithTCNKeyword() {
        val text = "TCN: HKR0627116X001XXX"
        assertTrue(scanText(text) >= 1, "TCN с ключевым словом 'TCN' должен быть найден")
    }

    @Test
    fun testWithTCNKeywordAndColon() {
        val text = "TCN: ABCDEF5123A042XXX" // 17 символов после разделителя
        assertTrue(scanText(text) >= 1, "TCN с двоеточием должен быть найден")
    }

    @Test
    fun testWithTCNKeywordAndSpace() {
        val text = "TCN ABCDEF5123A042XXX"
        assertTrue(scanText(text) >= 1, "TCN с пробелом должен быть найден")
    }

    @Test
    fun testWithTCNKeywordAndHyphen() {
        val text = "TCN - ABCDEF5123A042XXX"
        assertTrue(scanText(text) >= 1, "TCN с дефисом должен быть найден")
    }

    @Test
    fun testLowerCaseKeyword() {
        val text = "tcn: HKR0627116X001XXX"
        assertTrue(scanText(text) >= 1, "TCN с ключевым словом в нижнем регистре должен быть найден")
    }

    @Test
    fun testMixedCaseKeyword() {
        val text = "Tcn: HKR0627116X001XXX"
        assertTrue(scanText(text) >= 1, "TCN с ключевым словом в смешанном регистре должен быть найден")
    }

    @Test
    fun testAlphanumericTCN() {
        val text = "TCN: HKR0627116X001XXX"
        assertTrue(scanText(text) >= 1, "TCN с буквами и цифрами должен быть найден")
    }

    @Test
    fun testNumericOnlyTCN() {
        val text = "TCN: 12345678901234567"
        assertTrue(scanText(text) >= 1, "TCN только с цифрами должен быть найден")
    }

    @Test
    fun testLettersOnlyTCN() {
        val text = "TCN: ABCDEFGHIJKLMNOPQ" // 17 букв
        assertTrue(scanText(text) >= 1, "TCN только с буквами должен быть найден")
    }

    // ========== 2. Позиция совпадения в тексте ==========

    @Test
    fun testAtStartOfText() {
        val text = "TCN: HKR0627116X001XXX"
        assertTrue(scanText(text) >= 1, "TCN в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "The TCN: HKR0627116X001XXX"
        assertTrue(scanText(text) >= 1, "TCN в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The TCN HKR0627116X001XXX is valid"
        assertTrue(scanText(text) >= 1, "TCN в середине текста должен быть найден")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Number TCN: HKR0627116X001XXX"
        assertTrue(scanText(text) >= 1, "TCN с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "TCN: HKR0627116X001XXX is valid"
        assertTrue(scanText(text) >= 1, "TCN с пробелом после должен быть найден")
    }

    @Test
    fun testWithComma() {
        val text = "TCN: HKR0627116X001XXX, next"
        assertTrue(scanText(text) >= 1, "TCN с запятой должен быть найден")
    }

    @Test
    fun testWithDot() {
        val text = "TCN: HKR0627116X001XXX."
        assertTrue(scanText(text) >= 1, "TCN с точкой должен быть найден")
    }

    @Test
    fun testWithParentheses() {
        val text = "(TCN: HKR0627116X001XXX)"
        assertTrue(scanText(text) >= 1, "TCN в скобках должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nTCN: HKR0627116X001XXX"
        assertTrue(scanText(text) >= 1, "TCN после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "TCN: HKR0627116X001XXX\n"
        assertTrue(scanText(text) >= 1, "TCN перед \\n должен быть найден")
    }

    @Test
    fun testWithMultipleSeparators() {
        val text = "TCN - : HKR0627116X001XXX" // Разделитель " - : " (4 символа, в пределах 20)
        assertTrue(scanText(text) >= 1, "TCN с несколькими разделителями должен быть найден")
    }

    // ========== 3. Негативные сценарии ==========

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abcTCN: HKR0627116X001XXXdef"
        assertEquals(0, scanText(text), "TCN внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123TCN: HKR0627116X001XXX456"
        assertEquals(0, scanText(text), "TCN внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать TCN")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no TCNs"
        assertEquals(0, scanText(text), "Текст без TCN не должен находить совпадения")
    }

    @Test
    fun testNoKeyword() {
        val text = "HKR0627116X001XXX" // Нет ключевого слова TCN
        assertEquals(0, scanText(text), "TCN без ключевого слова не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "TCN: HKR0627116X001XX" // 16 символов вместо 17
        assertEquals(0, scanText(text), "Слишком короткий TCN не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "TCN: HKR0627116X001XXXX" // 18 символов вместо 17
        assertEquals(0, scanText(text), "Слишком длинный TCN не должен находиться")
    }

    @Test
    fun testInvalidCharacters() {
        val text = "TCN: HKR0627116X001XX-" // Дефис в номере
        assertEquals(0, scanText(text), "TCN с недопустимыми символами не должен находиться")
    }

    @Test
    fun testAllSameCharacters() {
        val text = "TCN: AAAAAAAAAAAAAAAAA" // Все одинаковые буквы
        assertEquals(0, scanText(text), "TCN с одинаковыми символами не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "TCN: 00000000000000000" // Все нули
        assertEquals(0, scanText(text), "TCN со всеми нулями не должен находиться")
    }

    @Test
    fun testMultipleTCNs() {
        val text = "TCN: HKR0627116X001XXX and TCN: ABCDEF5123A042XXX"
        assertTrue(scanText(text) >= 2, "Несколько TCN должны быть найдены")
    }

    @Test
    fun testInShippingDocument() {
        val text = "Shipping Document\nTCN: HKR0627116X001XXX\nDestination: APO AE 09355"
        assertTrue(scanText(text) >= 1, "TCN в документе доставки должен быть найден")
    }

    @Test
    fun testWithLongSeparator() {
        val text = "TCN  -  :  -  HKR0627116X001XXX" // Длинный разделитель с пробелами "  -  :  -  " (11 символов, в пределах 20)
        assertTrue(scanText(text) >= 1, "TCN с длинным разделителем должен быть найден")
    }
}