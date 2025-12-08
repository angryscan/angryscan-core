package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера DeathDate
 */
internal class DeathDateTest : MatcherTestBase(DeathDate) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти в начале текста должна быть найдена")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Death date: дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти в конце текста должна быть найдена")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The дата смерти: 01.01.1990 is valid"
        assertTrue(scanText(text) >= 1, "Дата смерти в середине текста должна быть найдена")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "дата смерти: 01.01.1990\nSecond line"
        assertTrue(scanText(text) >= 1, "Дата смерти в начале строки должна быть найдена")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nдата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти в конце строки должна быть найдена")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with дата смерти: 01.01.1990 date"
        assertTrue(scanText(text) >= 1, "Дата смерти в середине строки должна быть найдена")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nдата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти после \\n должна быть найдена")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "дата смерти: 01.01.1990\n"
        assertTrue(scanText(text) >= 1, "Дата смерти перед \\n должна быть найдена")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nдата смерти: 01.01.1990\r\n"
        assertTrue(scanText(text) >= 1, "Дата смерти с \\r\\n должна быть найдена")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nдата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти после пустой строки должна быть найдена")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "дата смерти: 01.01.1990\n\n"
        assertTrue(scanText(text) >= 1, "Дата смерти перед пустой строкой должна быть найдена")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcдата смерти: 01.01.1990def"
        assertEquals(0, scanText(text), "Дата смерти внутри буквенной последовательности не должна находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Date дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с пробелом перед должна быть найдена")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "дата смерти: 01.01.1990 is valid"
        assertTrue(scanText(text) >= 1, "Дата смерти с пробелом после должна быть найдена")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Date, дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с запятой перед должна быть найдена")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "дата смерти: 01.01.1990, next"
        assertTrue(scanText(text) >= 1, "Дата смерти с запятой после должна быть найдена")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Date. дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с точкой перед должна быть найдена")
    }

    @Test
    fun testWithDotAfter() {
        val text = "дата смерти: 01.01.1990."
        assertTrue(scanText(text) >= 1, "Дата смерти с точкой после должна быть найдена")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Date; дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с точкой с запятой перед должна быть найдена")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "дата смерти: 01.01.1990; next"
        assertTrue(scanText(text) >= 1, "Дата смерти с точкой с запятой после должна быть найдена")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Date: дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с двоеточием перед должна быть найдена")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( дата смерти: 01.01.1990 )"
        assertTrue(scanText(text) >= 1, "Дата смерти в скобках с пробелами должна быть найдена")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(дата смерти: 01.01.1990)"
        assertTrue(scanText(text) >= 1, "Дата смерти в скобках без пробелов должна быть найдена")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" дата смерти: 01.01.1990 \""
        assertTrue(scanText(text) >= 1, "Дата смерти в кавычках с пробелами должна быть найдена")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"дата смерти: 01.01.1990\""
        assertTrue(scanText(text) >= 1, "Дата смерти в кавычках без пробелов должна быть найдена")
    }

    @Test
    fun testWithDotAsPartOfFormat() {
        val text = "дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с точкой как часть формата должна быть найдена")
    }

    @Test
    fun testWithSlashAsPartOfFormat() {
        val text = "дата смерти: 01/01/1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с слэшем как часть формата должна быть найдена")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "дата смерти: 01-01-1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с дефисом как часть формата должна быть найдена")
    }

    @Test
    fun testWithSpaceAsPartOfFormat() {
        val text = "дата смерти: 01 01 1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с пробелом как часть формата должна быть найдена")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "дата смерти: 01.01.1990 дата смерти: 02.02.2000"
        assertTrue(scanText(text) >= 2, "Несколько дат смерти через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "дата смерти: 01.01.1990, дата смерти: 02.02.2000"
        assertTrue(scanText(text) >= 2, "Несколько дат смерти через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "дата смерти: 01.01.1990; дата смерти: 02.02.2000"
        assertTrue(scanText(text) >= 2, "Несколько дат смерти через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "дата смерти: 01.01.1990\nдата смерти: 02.02.2000"
        assertTrue(scanText(text) >= 2, "Несколько дат смерти через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать дат смерти")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no death dates at all"
        assertEquals(0, scanText(text), "Текст без дат смерти не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMonthName() {
        val text = "дата смерти: 01 января 1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с названием месяца должна быть найдена")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Date    дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с несколькими пробелами должна быть найдена")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Date\tдата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с табуляцией перед должна быть найдена")
    }

    @Test
    fun testWithTabAfter() {
        val text = "дата смерти: 01.01.1990\tnext"
        assertTrue(scanText(text) >= 1, "Дата смерти с табуляцией после должна быть найдена")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Дата дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с кириллицей рядом должна быть найдена")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Date дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с латиницей рядом должна быть найдена")
    }

    @Test
    fun testStandalone() {
        val text = "дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти отдельной строкой должна быть найдена")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "дата смерти: 01.01.1990 text"
        assertTrue(scanText(text) >= 1, "Дата смерти в начале текста должна быть найдена")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text дата смерти: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти в конце текста должна быть найдена")
    }

    @Test
    fun testWithUmerKeyword() {
        val text = "умер: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с ключевым словом 'умер' должна быть найдена")
    }

    @Test
    fun testWithSkonchalsyaKeyword() {
        val text = "скончался: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата смерти с ключевым словом 'скончался' должна быть найдена")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidDate() {
        val text = "дата смерти: 32.01.1990"
        assertEquals(0, scanText(text), "Дата смерти с недопустимой датой не должна находиться")
    }

    @Test
    fun testInvalidMonth() {
        val text = "дата смерти: 01.13.1990"
        assertEquals(0, scanText(text), "Дата смерти с недопустимым месяцем не должна находиться")
    }

    @Test
    fun testTooShort() {
        val text = "дата смерти: 01.01"
        assertEquals(0, scanText(text), "Слишком короткая дата смерти не должна находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "дата смерти: AB.CD.EFGH"
        assertEquals(0, scanText(text), "Дата смерти с буквами не должна находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "fdдата смерти: 01.01.1990"
        assertEquals(0, scanText(text), "Дата смерти, прилипшая к буквам, не должна находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123дата смерти: 01.01.1990"
        assertEquals(0, scanText(text), "Дата смерти, прилипшая к цифрам, не должна находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionдата смерти: 01.01.1990()"
        assertEquals(0, scanText(text), "Дата смерти внутри кода не должна находиться")
    }
}

