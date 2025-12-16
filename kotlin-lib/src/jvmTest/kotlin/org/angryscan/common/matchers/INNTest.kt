package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера INN
 */
internal class INNTest : MatcherTestBase(INN) {

    // Валидный INN для тестов (12 цифр с правильной контрольной суммой)
    private val validINN = "500100732259" // Пример валидного INN

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "500100732259 is an INN"
        assertTrue(scanText(text) >= 1, "INN в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "INN number is 500100732259"
        assertTrue(scanText(text) >= 1, "INN в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The INN 500100732259 is valid"
        assertTrue(scanText(text) >= 1, "INN в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "500100732259\nSecond line"
        assertTrue(scanText(text) >= 1, "INN в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n500100732259"
        assertTrue(scanText(text) >= 1, "INN в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 500100732259 INN"
        assertTrue(scanText(text) >= 1, "INN в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n500100732259"
        assertTrue(scanText(text) >= 1, "INN после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "500100732259\n"
        assertTrue(scanText(text) >= 1, "INN перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n500100732259\r\n"
        assertTrue(scanText(text) >= 1, "INN с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n500100732259"
        assertTrue(scanText(text) >= 1, "INN после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "500100732259\n\n"
        assertTrue(scanText(text) >= 1, "INN перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc500100732259def"
        assertEquals(0, scanText(text), "INN внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123500100732259456"
        assertEquals(0, scanText(text), "INN внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc123500100732259def456"
        assertEquals(0, scanText(text), "INN внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "INN 500100732259"
        assertTrue(scanText(text) >= 1, "INN с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "500100732259 is valid"
        assertTrue(scanText(text) >= 1, "INN с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "INN, 500100732259"
        assertTrue(scanText(text) >= 1, "INN с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "500100732259, next"
        assertTrue(scanText(text) >= 1, "INN с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "INN. 500100732259"
        assertTrue(scanText(text) >= 1, "INN с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "500100732259."
        assertTrue(scanText(text) >= 1, "INN с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "INN; 500100732259"
        assertTrue(scanText(text) >= 1, "INN с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "500100732259; next"
        assertTrue(scanText(text) >= 1, "INN с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "INN: 500100732259"
        assertTrue(scanText(text) >= 1, "INN с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 500100732259 )"
        assertTrue(scanText(text) >= 1, "INN в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(500100732259)"
        assertTrue(scanText(text) >= 1, "INN в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 500100732259 \""
        assertTrue(scanText(text) >= 1, "INN в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"500100732259\""
        assertTrue(scanText(text) >= 1, "INN в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "inn = 500100732259"
        assertTrue(scanText(text) >= 1, "INN с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "inn # 500100732259"
        assertTrue(scanText(text) >= 1, "INN с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 500100732259 ]"
        assertTrue(scanText(text) >= 1, "INN в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 500100732259 }"
        assertTrue(scanText(text) >= 1, "INN в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithSpacesInFormat() {
        val text = "50 01 00732259"
        assertTrue(scanText(text) >= 1, "INN с пробелами в формате должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать INN")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no INN numbers at all"
        assertEquals(0, scanText(text), "Текст без INN не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "500100732259"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithSpacesFormat() {
        val text = "50 01 00732259"
        assertTrue(scanText(text) >= 1, "INN с пробелами должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "INN    500100732259"
        assertTrue(scanText(text) >= 1, "INN с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "INN\t500100732259"
        assertTrue(scanText(text) >= 1, "INN с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "500100732259\tnext"
        assertTrue(scanText(text) >= 1, "INN с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "ИНН 500100732259"
        assertTrue(scanText(text) >= 1, "INN с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "INN 500100732259"
        assertTrue(scanText(text) >= 1, "INN с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "500100732259"
        assertTrue(scanText(text) >= 1, "INN отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "500100732259 text"
        assertTrue(scanText(text) >= 1, "INN в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 500100732259"
        assertTrue(scanText(text) >= 1, "INN в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidControlSum() {
        val text = "500100732250"
        assertEquals(0, scanText(text), "INN с неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "5001007322"
        assertEquals(0, scanText(text), "Слишком короткий INN не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "5001007322599"
        assertEquals(0, scanText(text), "Слишком длинный INN не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "5001007322AB"
        assertEquals(0, scanText(text), "INN с буквами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "INN внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "inn500100732259"
        assertEquals(0, scanText(text), "INN, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123500100732259"
        assertEquals(0, scanText(text), "INN, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "5001@0073@2259"
        assertEquals(0, scanText(text), "INN с неправильными разделителями не должен находиться")
    }

    @Test
    fun testPartialINN() {
        val text = "500100"
        assertEquals(0, scanText(text), "Частичный INN не должен находиться")
    }

    @Test
    fun testINNInCode() {
        val text = "function500100732259()"
        assertEquals(0, scanText(text), "INN внутри кода не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "000000000000"
        assertEquals(0, scanText(text), "INN из всех нулей не должен находиться")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "5001#0073#2259"
        assertEquals(0, scanText(text), "INN с недопустимыми символами не должен находиться")
    }
}

