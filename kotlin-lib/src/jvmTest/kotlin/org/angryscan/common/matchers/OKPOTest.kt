package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера OKPO
 */
internal class OKPOTest : MatcherTestBase(OKPO) {

    // Валидный OKPO для тестов (8 или 10 цифр, правильная контрольная сумма)
    private val validOKPO8 = "23456783" // Пример валидного OKPO (8 цифр)
    private val validOKPO10 = "2345678909" // Пример валидного OKPO (10 цифр)

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "23456783"
        assertTrue(scanText(text) >= 1, "ОКПО в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "OKPO number is 23456783"
        assertTrue(scanText(text) >= 1, "ОКПО в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The OKPO 23456783 is valid"
        assertTrue(scanText(text) >= 1, "ОКПО в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "23456783\nSecond line"
        assertTrue(scanText(text) >= 1, "ОКПО в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n23456783"
        assertTrue(scanText(text) >= 1, "ОКПО в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 23456783 OKPO"
        assertTrue(scanText(text) >= 1, "ОКПО в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n23456783"
        assertTrue(scanText(text) >= 1, "ОКПО после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "23456783\n"
        assertTrue(scanText(text) >= 1, "ОКПО перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n23456783\r\n"
        assertTrue(scanText(text) >= 1, "ОКПО с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n23456783"
        assertTrue(scanText(text) >= 1, "ОКПО после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "23456783\n\n"
        assertTrue(scanText(text) >= 1, "ОКПО перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc23456783def"
        assertEquals(0, scanText(text), "ОКПО внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12323456783456"
        assertEquals(0, scanText(text), "ОКПО внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "OKPO 23456783"
        assertTrue(scanText(text) >= 1, "ОКПО с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "23456783 is valid"
        assertTrue(scanText(text) >= 1, "ОКПО с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "OKPO, 23456783"
        assertTrue(scanText(text) >= 1, "ОКПО с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "23456783, next"
        assertTrue(scanText(text) >= 1, "ОКПО с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "OKPO. 23456783"
        assertTrue(scanText(text) >= 1, "ОКПО с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "23456783."
        assertTrue(scanText(text) >= 1, "ОКПО с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "OKPO; 23456783"
        assertTrue(scanText(text) >= 1, "ОКПО с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "23456783; next"
        assertTrue(scanText(text) >= 1, "ОКПО с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "OKPO: 23456783"
        assertTrue(scanText(text) >= 1, "ОКПО с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 23456783 )"
        assertTrue(scanText(text) >= 1, "ОКПО в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(23456783)"
        assertTrue(scanText(text) >= 1, "ОКПО в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 23456783 \""
        assertTrue(scanText(text) >= 1, "ОКПО в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"23456783\""
        assertTrue(scanText(text) >= 1, "ОКПО в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 23456783 ]"
        assertTrue(scanText(text) >= 1, "ОКПО в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 23456783 }"
        assertTrue(scanText(text) >= 1, "ОКПО в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "23456783 34567899"
        assertTrue(scanText(text) >= 2, "Несколько ОКПО через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "23456783, 34567899"
        assertTrue(scanText(text) >= 2, "Несколько ОКПО через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "23456783; 34567899"
        assertTrue(scanText(text) >= 2, "Несколько ОКПО через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "23456783\n34567899"
        assertTrue(scanText(text) >= 2, "Несколько ОКПО через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ОКПО")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no OKPO numbers at all"
        assertEquals(0, scanText(text), "Текст без ОКПО не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat8() {
        val text = "23456783"
        assertTrue(scanText(text) >= 1, "Минимальный формат (8 цифр) должен быть найден")
    }

    @Test
    fun testFormat10() {
        val text = "2345678909"
        assertTrue(scanText(text) >= 1, "Формат (10 цифр) должен быть найден")
    }


    @Test
    fun testWithMultipleSpaces() {
        val text = "OKPO    23456783"
        assertTrue(scanText(text) >= 1, "ОКПО с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "OKPO\t23456783"
        assertTrue(scanText(text) >= 1, "ОКПО с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "23456783\tnext"
        assertTrue(scanText(text) >= 1, "ОКПО с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "ОКПО 23456783"
        assertTrue(scanText(text) >= 1, "ОКПО с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "OKPO 23456783"
        assertTrue(scanText(text) >= 1, "ОКПО с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "23456783"
        assertTrue(scanText(text) >= 1, "ОКПО отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "23456783 text"
        assertTrue(scanText(text) >= 1, "ОКПО в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 23456783"
        assertTrue(scanText(text) >= 1, "ОКПО в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "1234567"
        assertEquals(0, scanText(text), "Слишком короткий ОКПО не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "234567839"
        assertEquals(0, scanText(text), "Слишком длинный ОКПО не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "1234567A"
        assertEquals(0, scanText(text), "ОКПО с буквами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "234567839023456783902345678390"
        assertEquals(0, scanText(text), "ОКПО внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "okpo23456783"
        assertEquals(0, scanText(text), "ОКПО, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12323456783"
        assertEquals(0, scanText(text), "ОКПО, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function23456783()"
        assertEquals(0, scanText(text), "ОКПО внутри кода не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "00000000"
        assertEquals(0, scanText(text), "ОКПО из всех нулей не должен находиться")
    }
}

