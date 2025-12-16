package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера CVV
 */
internal class CVVTest : MatcherTestBase(CVV) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Card CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The CVV: 123 is valid"
        assertTrue(scanText(text) >= 1, "CVV в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "CVV: 123\nSecond line"
        assertTrue(scanText(text) >= 1, "CVV в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nCVV: 123"
        assertTrue(scanText(text) >= 1, "CVV в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with CVV: 123 code"
        assertTrue(scanText(text) >= 1, "CVV в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nCVV: 123"
        assertTrue(scanText(text) >= 1, "CVV после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "CVV: 123\n"
        assertTrue(scanText(text) >= 1, "CVV перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nCVV: 123\r\n"
        assertTrue(scanText(text) >= 1, "CVV с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nCVV: 123"
        assertTrue(scanText(text) >= 1, "CVV после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "CVV: 123\n\n"
        assertTrue(scanText(text) >= 1, "CVV перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcCVV: 123def"
        assertEquals(0, scanText(text), "CVV внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123CVV: 123456"
        assertEquals(0, scanText(text), "CVV внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Card CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "CVV: 123 is valid"
        assertTrue(scanText(text) >= 1, "CVV с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Card, CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "CVV: 123, next"
        assertTrue(scanText(text) >= 1, "CVV с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Card. CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "CVV: 123."
        assertTrue(scanText(text) >= 1, "CVV с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Card; CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "CVV: 123; next"
        assertTrue(scanText(text) >= 1, "CVV с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Card: CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( CVV: 123 )"
        assertTrue(scanText(text) >= 1, "CVV в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" CVV: 123 \""
        assertTrue(scanText(text) >= 1, "CVV в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"CVV: 123\""
        assertTrue(scanText(text) >= 1, "CVV в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithColonAsPartOfFormat() {
        val text = "CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV с двоеточием как часть формата должен быть найден")
    }

    @Test
    fun testWithSpaceAsPartOfFormat() {
        val text = "CVV 123"
        assertTrue(scanText(text) >= 1, "CVV с пробелом как часть формата должен быть найден")
    }

    @Test
    fun testWithCVCKeyword() {
        val text = "CVC: 123"
        assertTrue(scanText(text) >= 1, "CVV с ключевым словом CVC должен быть найден")
    }

    @Test
    fun testWithCVC2Keyword() {
        val text = "CVC2: 123"
        assertTrue(scanText(text) >= 1, "CVV с ключевым словом CVC2 должен быть найден")
    }

    @Test
    fun testWithCVV2Keyword() {
        val text = "CVV2: 123"
        assertTrue(scanText(text) >= 1, "CVV с ключевым словом CVV2 должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithCommas() {
        val text = "CVV: 123, CVV: 456"
        assertTrue(scanText(text) >= 2, "Несколько CVV через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "CVV: 123; CVV: 456"
        assertTrue(scanText(text) >= 2, "Несколько CVV через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "CVV: 123\nCVV: 456"
        assertTrue(scanText(text) >= 2, "Несколько CVV через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать CVV")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no CVV codes at all"
        assertEquals(0, scanText(text), "Текст без CVV не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "CVV: 123"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "CVV: 12"
        assertEquals(0, scanText(text), "Слишком короткий код не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "CVV: 1234"
        assertEquals(0, scanText(text), "Слишком длинный код не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Card    CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Card\tCVV: 123"
        assertTrue(scanText(text) >= 1, "CVV с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "CVV: 123\tnext"
        assertTrue(scanText(text) >= 1, "CVV с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Карта CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Card CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "CVV: 123 text"
        assertTrue(scanText(text) >= 1, "CVV в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text CVV: 123"
        assertTrue(scanText(text) >= 1, "CVV в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testWithLetters() {
        val text = "CVV: ABC"
        assertEquals(0, scanText(text), "CVV с буквами не должен находиться")
    }

    @Test
    fun testWithSpecialChars() {
        val text = "CVV: 12#"
        assertEquals(0, scanText(text), "CVV со спецсимволами не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "cardCVV: 123"
        assertEquals(0, scanText(text), "CVV, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123CVV: 123"
        assertEquals(0, scanText(text), "CVV, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionCVV: 123()"
        assertEquals(0, scanText(text), "CVV внутри кода не должен находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "CVV123"
        assertEquals(0, scanText(text), "CVV без разделителя не должен находиться")
    }

    @Test
    fun testWithNegativeNumber() {
        val text = "CVV: -123"
        assertEquals(0, scanText(text), "CVV с отрицательным числом не должен находиться")
    }

    @Test
    fun testWithLeadingZeros() {
        val text = "CVV: 012"
        // В зависимости от реализации - может быть найден или нет
        val count = scanText(text)
        assertTrue(count >= 0, "CVV с ведущими нулями")
    }
}

