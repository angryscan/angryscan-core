package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера MilitaryID
 */
internal class MilitaryIDTest : MatcherTestBase(MilitaryID) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Military ID: АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The ID АБ 1234567 is valid"
        assertTrue(scanText(text) >= 1, "УЛВ в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "АБ 1234567\nSecond line"
        assertTrue(scanText(text) >= 1, "УЛВ в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nАБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with АБ 1234567 ID"
        assertTrue(scanText(text) >= 1, "УЛВ в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nАБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "АБ 1234567\n"
        assertTrue(scanText(text) >= 1, "УЛВ перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nАБ 1234567\r\n"
        assertTrue(scanText(text) >= 1, "УЛВ с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nАБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "АБ 1234567\n\n"
        assertTrue(scanText(text) >= 1, "УЛВ перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcАБ 1234567def"
        assertEquals(0, scanText(text), "УЛВ внутри буквенной последовательности не должно находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123АБ 1234567456"
        assertEquals(0, scanText(text), "УЛВ внутри цифровой последовательности не должно находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "ID АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "АБ 1234567 is valid"
        assertTrue(scanText(text) >= 1, "УЛВ с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "ID, АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "АБ 1234567, next"
        assertTrue(scanText(text) >= 1, "УЛВ с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "ID. АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "АБ 1234567."
        assertTrue(scanText(text) >= 1, "УЛВ с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "ID; АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "АБ 1234567; next"
        assertTrue(scanText(text) >= 1, "УЛВ с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "ID: АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ с двоеточием перед должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( АБ 1234567 )"
        assertTrue(scanText(text) >= 1, "УЛВ в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(АБ 1234567)"
        assertTrue(scanText(text) >= 1, "УЛВ в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" АБ 1234567 \""
        assertTrue(scanText(text) >= 1, "УЛВ в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"АБ 1234567\""
        assertTrue(scanText(text) >= 1, "УЛВ в кавычках без пробелов должно быть найдено")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ АБ 1234567 ]"
        assertTrue(scanText(text) >= 1, "УЛВ в квадратных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ АБ 1234567 }"
        assertTrue(scanText(text) >= 1, "УЛВ в фигурных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "АБ-1234567"
        assertTrue(scanText(text) >= 1, "УЛВ с дефисом как часть формата должно быть найдено")
    }

    @Test
    fun testWithNumberSignAsPartOfFormat() {
        val text = "АБ № 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ со знаком № как часть формата должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "АБ 1234567 ВГ 7654321"
        assertTrue(scanText(text) >= 2, "Несколько УЛВ через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "АБ 1234567, ВГ 7654321"
        assertTrue(scanText(text) >= 2, "Несколько УЛВ через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "АБ 1234567; ВГ 7654321"
        assertTrue(scanText(text) >= 2, "Несколько УЛВ через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "АБ 1234567\nВГ 7654321"
        assertTrue(scanText(text) >= 2, "Несколько УЛВ через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать УЛВ")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no military IDs at all"
        assertEquals(0, scanText(text), "Текст без УЛВ не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "АБ 1234567"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "ID    АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "ID\tАБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "АБ 1234567\tnext"
        assertTrue(scanText(text) >= 1, "УЛВ с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Удостоверение АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Military ID АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "АБ 1234567 text"
        assertTrue(scanText(text) >= 1, "УЛВ в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text АБ 1234567"
        assertTrue(scanText(text) >= 1, "УЛВ в конце текста должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "АБ 123456"
        assertEquals(0, scanText(text), "Слишком короткое УЛВ не должно находиться")
    }

    @Test
    fun testTooLong() {
        val text = "АБ 12345678"
        assertEquals(0, scanText(text), "Слишком длинное УЛВ не должно находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "АБ@1234567"
        assertEquals(0, scanText(text), "УЛВ с неправильными разделителями не должно находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "idАБ 1234567"
        assertEquals(0, scanText(text), "УЛВ, прилипшее к буквам, не должно находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123АБ 1234567"
        assertEquals(0, scanText(text), "УЛВ, прилипшее к цифрам, не должно находиться")
    }

    @Test
    fun testPartialMilitaryID() {
        val text = "АБ"
        assertEquals(0, scanText(text), "Частичное УЛВ не должно находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionАБ 1234567()"
        assertEquals(0, scanText(text), "УЛВ внутри кода не должно находиться")
    }
}

