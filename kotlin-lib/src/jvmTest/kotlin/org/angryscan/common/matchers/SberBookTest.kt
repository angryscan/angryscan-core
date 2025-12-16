package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера SberBook
 */
internal class SberBookTest : MatcherTestBase(SberBook) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка в начале текста должна быть найдена")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Sberbook number: 42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка в конце текста должна быть найдена")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The number 42301123456789012345 is valid"
        assertTrue(scanText(text) >= 1, "Сберкнижка в середине текста должна быть найдена")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "42301123456789012345\nSecond line"
        assertTrue(scanText(text) >= 1, "Сберкнижка в начале строки должна быть найдена")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка в конце строки должна быть найдена")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 42301123456789012345 number"
        assertTrue(scanText(text) >= 1, "Сберкнижка в середине строки должна быть найдена")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка после \\n должна быть найдена")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "42301123456789012345\n"
        assertTrue(scanText(text) >= 1, "Сберкнижка перед \\n должна быть найдена")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n42301123456789012345\r\n"
        assertTrue(scanText(text) >= 1, "Сберкнижка с \\r\\n должна быть найдена")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка после пустой строки должна быть найдена")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "42301123456789012345\n\n"
        assertTrue(scanText(text) >= 1, "Сберкнижка перед пустой строкой должна быть найдена")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc42301123456789012345def"
        assertEquals(0, scanText(text), "Сберкнижка внутри буквенной последовательности не должна находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12342301123456789012345456"
        assertEquals(0, scanText(text), "Сберкнижка внутри цифровой последовательности не должна находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Number 42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с пробелом перед должна быть найдена")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "42301123456789012345 is valid"
        assertTrue(scanText(text) >= 1, "Сберкнижка с пробелом после должна быть найдена")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Number, 42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с запятой перед должна быть найдена")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "42301123456789012345, next"
        assertTrue(scanText(text) >= 1, "Сберкнижка с запятой после должна быть найдена")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Number. 42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с точкой перед должна быть найдена")
    }

    @Test
    fun testWithDotAfter() {
        val text = "42301123456789012345."
        assertTrue(scanText(text) >= 1, "Сберкнижка с точкой после должна быть найдена")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Number; 42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с точкой с запятой перед должна быть найдена")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "42301123456789012345; next"
        assertTrue(scanText(text) >= 1, "Сберкнижка с точкой с запятой после должна быть найдена")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Number: 42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с двоеточием перед должна быть найдена")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 42301123456789012345 )"
        assertTrue(scanText(text) >= 1, "Сберкнижка в скобках с пробелами должна быть найдена")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(42301123456789012345)"
        assertTrue(scanText(text) >= 1, "Сберкнижка в скобках без пробелов должна быть найдена")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 42301123456789012345 \""
        assertTrue(scanText(text) >= 1, "Сберкнижка в кавычках с пробелами должна быть найдена")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"42301123456789012345\""
        assertTrue(scanText(text) >= 1, "Сберкнижка в кавычках без пробелов должна быть найдена")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 42301123456789012345 ]"
        assertTrue(scanText(text) >= 1, "Сберкнижка в квадратных скобках с пробелами должна быть найдена")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 42301123456789012345 }"
        assertTrue(scanText(text) >= 1, "Сберкнижка в фигурных скобках с пробелами должна быть найдена")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "42301-123-4-5678-9012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с дефисом как часть формата должна быть найдена")
    }

    @Test
    fun testWithSpaceAsPartOfFormat() {
        val text = "42301 123 4 5678 9012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с пробелом как часть формата должна быть найдена")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "42301123456789012345 42301123456789012356"
        assertTrue(scanText(text) >= 2, "Несколько сберкнижек через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "42301123456789012345, 42301123456789012356"
        assertTrue(scanText(text) >= 2, "Несколько сберкнижек через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "42301123456789012345; 42301123456789012356"
        assertTrue(scanText(text) >= 2, "Несколько сберкнижек через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "42301123456789012345\n42301123456789012356"
        assertTrue(scanText(text) >= 2, "Несколько сберкнижек через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать сберкнижек")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no sberbook numbers at all"
        assertEquals(0, scanText(text), "Текст без сберкнижек не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "42301123456789012345"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Number    42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с несколькими пробелами должна быть найдена")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Number\t42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с табуляцией перед должна быть найдена")
    }

    @Test
    fun testWithTabAfter() {
        val text = "42301123456789012345\tnext"
        assertTrue(scanText(text) >= 1, "Сберкнижка с табуляцией после должна быть найдена")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Сберкнижка 42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с кириллицей рядом должна быть найдена")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Sberbook number 42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с латиницей рядом должна быть найдена")
    }

    @Test
    fun testStandalone() {
        val text = "42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка отдельной строкой должна быть найдена")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "42301123456789012345 text"
        assertTrue(scanText(text) >= 1, "Сберкнижка в начале текста должна быть найдена")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка в конце текста должна быть найдена")
    }

    @Test
    fun testWithNomerSberknizhkiKeyword() {
        val text = "номер сберкнижки: 42301123456789012345"
        assertTrue(scanText(text) >= 1, "Сберкнижка с ключевым словом должна быть найдена")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "423011234567890123"
        assertEquals(0, scanText(text), "Слишком короткая сберкнижка не должна находиться")
    }

    @Test
    fun testTooLong() {
        val text = "423011234567890123455"
        assertEquals(0, scanText(text), "Слишком длинная сберкнижка не должна находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "423011234567890123A"
        assertEquals(0, scanText(text), "Сберкнижка с буквами не должна находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "Сберкнижка внутри длинной цифровой последовательности не должна находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "book42301123456789012345"
        assertEquals(0, scanText(text), "Сберкнижка, прилипшая к буквам, не должна находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12342301123456789012345"
        assertEquals(0, scanText(text), "Сберкнижка, прилипшая к цифрам, не должна находиться")
    }

    @Test
    fun testWithInvalidPrefix() {
        val text = "9990112345678901234"
        assertEquals(0, scanText(text), "Сберкнижка с недопустимым префиксом не должна находиться")
    }

    @Test
    fun testInCode() {
        val text = "function42301123456789012345()"
        assertEquals(0, scanText(text), "Сберкнижка внутри кода не должна находиться")
    }
}

