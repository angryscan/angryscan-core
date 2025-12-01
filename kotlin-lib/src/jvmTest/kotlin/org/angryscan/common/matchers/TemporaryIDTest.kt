package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера TemporaryID
 */
internal class TemporaryIDTest : MatcherTestBase(TemporaryID) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Temporary ID: 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The ID 234567890123 is valid"
        assertTrue(scanText(text) >= 1, "ВУЛ в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "234567890123\nSecond line"
        assertTrue(scanText(text) >= 1, "ВУЛ в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 234567890123 ID"
        assertTrue(scanText(text) >= 1, "ВУЛ в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "234567890123\n"
        assertTrue(scanText(text) >= 1, "ВУЛ перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n234567890123\r\n"
        assertTrue(scanText(text) >= 1, "ВУЛ с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "234567890123\n\n"
        assertTrue(scanText(text) >= 1, "ВУЛ перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc234567890123def"
        assertEquals(0, scanText(text), "ВУЛ внутри буквенной последовательности не должно находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123234567890123456"
        assertEquals(0, scanText(text), "ВУЛ внутри цифровой последовательности не должно находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "ID 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "234567890123 is valid"
        assertTrue(scanText(text) >= 1, "ВУЛ с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "ID, 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "234567890123, next"
        assertTrue(scanText(text) >= 1, "ВУЛ с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "ID. 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "234567890123."
        assertTrue(scanText(text) >= 1, "ВУЛ с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "ID; 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "234567890123; next"
        assertTrue(scanText(text) >= 1, "ВУЛ с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "ID: 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с двоеточием перед должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 234567890123 )"
        assertTrue(scanText(text) >= 1, "ВУЛ в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(234567890123)"
        assertTrue(scanText(text) >= 1, "ВУЛ в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 234567890123 \""
        assertTrue(scanText(text) >= 1, "ВУЛ в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"234567890123\""
        assertTrue(scanText(text) >= 1, "ВУЛ в кавычках без пробелов должно быть найдено")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 234567890123 ]"
        assertTrue(scanText(text) >= 1, "ВУЛ в квадратных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 234567890123 }"
        assertTrue(scanText(text) >= 1, "ВУЛ в фигурных скобках с пробелами должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "234567890123 345678901234"
        assertTrue(scanText(text) >= 2, "Несколько ВУЛ через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "234567890123, 345678901234"
        assertTrue(scanText(text) >= 2, "Несколько ВУЛ через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "234567890123; 345678901234"
        assertTrue(scanText(text) >= 2, "Несколько ВУЛ через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "234567890123\n345678901234"
        assertTrue(scanText(text) >= 2, "Несколько ВУЛ через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ВУЛ")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no temporary IDs at all"
        assertEquals(0, scanText(text), "Текст без ВУЛ не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "234567890123"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "12345678901"
        assertEquals(0, scanText(text), "Слишком короткий номер не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "2345678901233"
        assertEquals(0, scanText(text), "Слишком длинный номер не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "ID    234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "ID\t234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "234567890123\tnext"
        assertTrue(scanText(text) >= 1, "ВУЛ с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Временное удостоверение 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Temporary ID 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "234567890123 text"
        assertTrue(scanText(text) >= 1, "ВУЛ в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ в конце текста должно быть найдено")
    }

    @Test
    fun testWithVremennoeUdoverenieKeyword() {
        val text = "временное удостоверение личности: 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с ключевым словом должно быть найдено")
    }

    @Test
    fun testWithVULKeyword() {
        val text = "ВУЛ: 234567890123"
        assertTrue(scanText(text) >= 1, "ВУЛ с ключевым словом ВУЛ должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testWithLetters() {
        val text = "12345678901A"
        assertEquals(0, scanText(text), "ВУЛ с буквами не должно находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "000000000000"
        assertEquals(0, scanText(text), "ВУЛ из всех нулей не должно находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "id234567890123"
        assertEquals(0, scanText(text), "ВУЛ, прилипшее к буквам, не должно находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123234567890123"
        assertEquals(0, scanText(text), "ВУЛ, прилипшее к цифрам, не должно находиться")
    }

    @Test
    fun testInCode() {
        val text = "function234567890123()"
        assertEquals(0, scanText(text), "ВУЛ внутри кода не должно находиться")
    }

    @Test
    fun testWithSpaces() {
        val text = "123456 789012"
        assertEquals(0, scanText(text), "ВУЛ с пробелами не должно находиться")
    }
}

