package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера PassportUS
 */
internal class PassportUSTest : MatcherTestBase(PassportUS) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Passport number: A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The passport A12345678 is valid"
        assertTrue(scanText(text) >= 1, "Паспорт США в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "A12345678\nSecond line"
        assertTrue(scanText(text) >= 1, "Паспорт США в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nA12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with A12345678 passport"
        assertTrue(scanText(text) >= 1, "Паспорт США в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nA12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "A12345678\n"
        assertTrue(scanText(text) >= 1, "Паспорт США перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nA12345678\r\n"
        assertTrue(scanText(text) >= 1, "Паспорт США с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nA12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "A12345678\n\n"
        assertTrue(scanText(text) >= 1, "Паспорт США перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcA12345678def"
        assertEquals(0, scanText(text), "Паспорт США внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123A12345678456"
        assertEquals(0, scanText(text), "Паспорт США внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Passport A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "A12345678 is valid"
        assertTrue(scanText(text) >= 1, "Паспорт США с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Passport, A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "A12345678, next"
        assertTrue(scanText(text) >= 1, "Паспорт США с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Passport. A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "A12345678."
        assertTrue(scanText(text) >= 1, "Паспорт США с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Passport; A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "A12345678; next"
        assertTrue(scanText(text) >= 1, "Паспорт США с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Passport: A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( A12345678 )"
        assertTrue(scanText(text) >= 1, "Паспорт США в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(A12345678)"
        assertTrue(scanText(text) >= 1, "Паспорт США в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" A12345678 \""
        assertTrue(scanText(text) >= 1, "Паспорт США в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"A12345678\""
        assertTrue(scanText(text) >= 1, "Паспорт США в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ A12345678 ]"
        assertTrue(scanText(text) >= 1, "Паспорт США в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ A12345678 }"
        assertTrue(scanText(text) >= 1, "Паспорт США в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithPassportKeyword() {
        val text = "passport: A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с ключевым словом должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "A12345678 C98765432"
        assertTrue(scanText(text) >= 2, "Несколько паспортов США через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "A12345678, C98765432"
        assertTrue(scanText(text) >= 2, "Несколько паспортов США через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "A12345678; C98765432"
        assertTrue(scanText(text) >= 2, "Несколько паспортов США через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "A12345678\nC98765432"
        assertTrue(scanText(text) >= 2, "Несколько паспортов США через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать паспортов США")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no US passport numbers at all"
        assertEquals(0, scanText(text), "Текст без паспортов США не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "A12345678"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Passport    A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Passport\tA12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "A12345678\tnext"
        assertTrue(scanText(text) >= 1, "Паспорт США с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Паспорт A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Passport A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "A12345678 text"
        assertTrue(scanText(text) >= 1, "Паспорт США в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США в конце текста должен быть найден")
    }

    @Test
    fun testWithAPrefix() {
        val text = "A12345678"
        assertTrue(scanText(text) >= 1, "Паспорт США с префиксом A должен быть найден")
    }

    @Test
    fun testWithCPrefix() {
        val text = "C98765432"
        assertTrue(scanText(text) >= 1, "Паспорт США с префиксом C должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "A1234567"
        assertEquals(0, scanText(text), "Слишком короткий паспорт США не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "A123456789"
        assertEquals(0, scanText(text), "Слишком длинный паспорт США не должен находиться")
    }

    @Test
    fun testWithInvalidPrefix() {
        val text = "X12345678"
        assertEquals(0, scanText(text), "Паспорт США с недопустимым префиксом не должен находиться")
    }

    @Test
    fun testWithLettersInNumber() {
        val text = "A1234567A"
        assertEquals(0, scanText(text), "Паспорт США с буквами в номере не должен находиться")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABCA12345678DEF"
        assertEquals(0, scanText(text), "Паспорт США внутри длинной последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "passportA12345678"
        assertEquals(0, scanText(text), "Паспорт США, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123A12345678"
        assertEquals(0, scanText(text), "Паспорт США, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionA12345678()"
        assertEquals(0, scanText(text), "Паспорт США внутри кода не должен находиться")
    }
}

