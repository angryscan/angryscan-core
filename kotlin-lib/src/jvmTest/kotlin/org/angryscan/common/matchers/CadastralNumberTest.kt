package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера CadastralNumber
 */
internal class CadastralNumberTest : MatcherTestBase(CadastralNumber) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "77:01:123456:100 кадастровый номер"
        assertTrue(scanText(text) >= 1, "КН в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Кадастровый номер: 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "Земельный участок 77:01:123456:100 продается"
        assertTrue(scanText(text) >= 1, "КН в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "77:01:123456:100\nSecond line"
        assertTrue(scanText(text) >= 1, "КН в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 77:01:123456:100 cadastral"
        assertTrue(scanText(text) >= 1, "КН в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "77:01:123456:100\n"
        assertTrue(scanText(text) >= 1, "КН перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n77:01:123456:100\r\n"
        assertTrue(scanText(text) >= 1, "КН с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "77:01:123456:100\n\n"
        assertTrue(scanText(text) >= 1, "КН перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc77:01:123456:100def"
        assertEquals(0, scanText(text), "КН внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12377:01:123456:100456"
        assertEquals(0, scanText(text), "КН внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc12377:01:123456:100def456"
        assertEquals(0, scanText(text), "КН внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "КН 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "77:01:123456:100 is valid"
        assertTrue(scanText(text) >= 1, "КН с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "КН, 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "77:01:123456:100, next"
        assertTrue(scanText(text) >= 1, "КН с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "КН. 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "77:01:123456:100."
        assertTrue(scanText(text) >= 1, "КН с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "КН; 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "77:01:123456:100; next"
        assertTrue(scanText(text) >= 1, "КН с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "КН: 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "77:01:123456:100!"
        assertTrue(scanText(text) >= 1, "КН с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "77:01:123456:100?"
        assertTrue(scanText(text) >= 1, "КН с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 77:01:123456:100 )"
        assertTrue(scanText(text) >= 1, "КН в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(77:01:123456:100)"
        assertTrue(scanText(text) >= 1, "КН в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 77:01:123456:100 \""
        assertTrue(scanText(text) >= 1, "КН в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"77:01:123456:100\""
        assertTrue(scanText(text) >= 1, "КН в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "kn = 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "kn # 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 77:01:123456:100 ]"
        assertTrue(scanText(text) >= 1, "КН в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 77:01:123456:100 }"
        assertTrue(scanText(text) >= 1, "КН в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithColonAsPartOfFormat() {
        val text = "77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с двоеточием как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "77:01:123456:100 50:12:234567:200"
        assertTrue(scanText(text) >= 2, "Несколько КН через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "77:01:123456:100, 50:12:234567:200"
        assertTrue(scanText(text) >= 2, "Несколько КН через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "77:01:123456:100; 50:12:234567:200"
        assertTrue(scanText(text) >= 2, "Несколько КН через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "77:01:123456:100\n50:12:234567:200"
        assertTrue(scanText(text) >= 2, "Несколько КН через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать КН")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no cadastral numbers at all"
        assertEquals(0, scanText(text), "Текст без КН не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "77:01:100000:1"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testMaximalFormat() {
        val text = "77:01:1234567:99999"
        assertTrue(scanText(text) >= 1, "Максимальный формат должен быть найден")
    }

    @Test
    fun testWithSpacesFormat() {
        val text = "77 : 01 : 123456 : 100"
        assertTrue(scanText(text) >= 1, "КН с пробелами должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "КН    77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "КН\t77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "77:01:123456:100\tnext"
        assertTrue(scanText(text) >= 1, "КН с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Кадастровый номер 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Cadastral number 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "77:01:123456:100 text"
        assertTrue(scanText(text) >= 1, "КН в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 77:01:123456:100"
        assertTrue(scanText(text) >= 1, "КН в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidFormat() {
        val text = "77-01-123456-100"
        assertEquals(0, scanText(text), "КН с неправильными разделителями не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "77:01:12345:100"
        assertEquals(0, scanText(text), "Слишком короткий КН не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "77:01:1234567:100"
        assertEquals(0, scanText(text), "Слишком длинный КН не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "77:01:ABCDEF:100"
        assertEquals(0, scanText(text), "КН с буквами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "770112345610012345678901234567890"
        assertEquals(0, scanText(text), "КН внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "cadastral77:01:123456:100"
        assertEquals(0, scanText(text), "КН, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12377:01:123456:100"
        assertEquals(0, scanText(text), "КН, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "77@01@123456@100"
        assertEquals(0, scanText(text), "КН с неправильными разделителями не должен находиться")
    }

    @Test
    fun testPartialCadastralNumber() {
        val text = "77:01:123456"
        assertEquals(0, scanText(text), "Частичный КН не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function77:01:123456:100()"
        assertEquals(0, scanText(text), "КН внутри кода не должен находиться")
    }
}

