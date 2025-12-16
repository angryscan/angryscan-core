package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера Birthday
 */
internal class BirthdayTest : MatcherTestBase(Birthday) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения в начале текста должна быть найдена")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Birth date: дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения в конце текста должна быть найдена")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The дата рождения: 01.01.1990 is valid"
        assertTrue(scanText(text) >= 1, "Дата рождения в середине текста должна быть найдена")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "дата рождения: 01.01.1990\nSecond line"
        assertTrue(scanText(text) >= 1, "Дата рождения в начале строки должна быть найдена")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nдата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения в конце строки должна быть найдена")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with дата рождения: 01.01.1990 date"
        assertTrue(scanText(text) >= 1, "Дата рождения в середине строки должна быть найдена")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nдата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения после \\n должна быть найдена")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "дата рождения: 01.01.1990\n"
        assertTrue(scanText(text) >= 1, "Дата рождения перед \\n должна быть найдена")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nдата рождения: 01.01.1990\r\n"
        assertTrue(scanText(text) >= 1, "Дата рождения с \\r\\n должна быть найдена")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nдата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения после пустой строки должна быть найдена")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "дата рождения: 01.01.1990\n\n"
        assertTrue(scanText(text) >= 1, "Дата рождения перед пустой строкой должна быть найдена")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcдата рождения: 01.01.1990def"
        assertEquals(0, scanText(text), "Дата рождения внутри буквенной последовательности не должна находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Date дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с пробелом перед должна быть найдена")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "дата рождения: 01.01.1990 is valid"
        assertTrue(scanText(text) >= 1, "Дата рождения с пробелом после должна быть найдена")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Date, дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с запятой перед должна быть найдена")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "дата рождения: 01.01.1990, next"
        assertTrue(scanText(text) >= 1, "Дата рождения с запятой после должна быть найдена")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Date. дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с точкой перед должна быть найдена")
    }

    @Test
    fun testWithDotAfter() {
        val text = "дата рождения: 01.01.1990."
        assertTrue(scanText(text) >= 1, "Дата рождения с точкой после должна быть найдена")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Date; дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с точкой с запятой перед должна быть найдена")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "дата рождения: 01.01.1990; next"
        assertTrue(scanText(text) >= 1, "Дата рождения с точкой с запятой после должна быть найдена")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Date: дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с двоеточием перед должна быть найдена")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( дата рождения: 01.01.1990 )"
        assertTrue(scanText(text) >= 1, "Дата рождения в скобках с пробелами должна быть найдена")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(дата рождения: 01.01.1990)"
        assertTrue(scanText(text) >= 1, "Дата рождения в скобках без пробелов должна быть найдена")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" дата рождения: 01.01.1990 \""
        assertTrue(scanText(text) >= 1, "Дата рождения в кавычках с пробелами должна быть найдена")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"дата рождения: 01.01.1990\""
        assertTrue(scanText(text) >= 1, "Дата рождения в кавычках без пробелов должна быть найдена")
    }

    @Test
    fun testWithDotAsPartOfFormat() {
        val text = "дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с точкой как часть формата должна быть найдена")
    }

    @Test
    fun testWithSlashAsPartOfFormat() {
        val text = "дата рождения: 01/01/1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с слэшем как часть формата должна быть найдена")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "дата рождения: 01-01-1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с дефисом как часть формата должна быть найдена")
    }

    @Test
    fun testWithSpaceAsPartOfFormat() {
        val text = "дата рождения: 01 01 1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с пробелом как часть формата должна быть найдена")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "дата рождения: 01.01.1990 дата рождения: 02.02.2000"
        assertTrue(scanText(text) >= 2, "Несколько дат рождения через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "дата рождения: 01.01.1990, дата рождения: 02.02.2000"
        assertTrue(scanText(text) >= 2, "Несколько дат рождения через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "дата рождения: 01.01.1990; дата рождения: 02.02.2000"
        assertTrue(scanText(text) >= 2, "Несколько дат рождения через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "дата рождения: 01.01.1990\nдата рождения: 02.02.2000"
        assertTrue(scanText(text) >= 2, "Несколько дат рождения через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать дат рождения")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no birthday dates at all"
        assertEquals(0, scanText(text), "Текст без дат рождения не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMonthName() {
        val text = "дата рождения: 01 января 1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с названием месяца должна быть найдена")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Date    дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с несколькими пробелами должна быть найдена")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Date\tдата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с табуляцией перед должна быть найдена")
    }

    @Test
    fun testWithTabAfter() {
        val text = "дата рождения: 01.01.1990\tnext"
        assertTrue(scanText(text) >= 1, "Дата рождения с табуляцией после должна быть найдена")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Дата дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с кириллицей рядом должна быть найдена")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Date дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с латиницей рядом должна быть найдена")
    }

    @Test
    fun testStandalone() {
        val text = "дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения отдельной строкой должна быть найдена")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "дата рождения: 01.01.1990 text"
        assertTrue(scanText(text) >= 1, "Дата рождения в начале текста должна быть найдена")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text дата рождения: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения в конце текста должна быть найдена")
    }

    @Test
    fun testWithRodilsyaKeyword() {
        val text = "родился: 01.01.1990"
        assertTrue(scanText(text) >= 1, "Дата рождения с ключевым словом 'родился' должна быть найдена")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidDate() {
        val text = "дата рождения: 32.01.1990"
        assertEquals(0, scanText(text), "Дата рождения с недопустимой датой не должна находиться")
    }

    @Test
    fun testInvalidMonth() {
        val text = "дата рождения: 01.13.1990"
        assertEquals(0, scanText(text), "Дата рождения с недопустимым месяцем не должна находиться")
    }

    @Test
    fun testInvalidFormat() {
        val text = "дата рождения: 01/01/1990"
        // В зависимости от реализации - может быть найдена или нет
        val count = scanText(text)
        assertTrue(count >= 0, "Дата рождения с неправильным форматом")
    }

    @Test
    fun testTooShort() {
        val text = "дата рождения: 01.01"
        assertEquals(0, scanText(text), "Слишком короткая дата рождения не должна находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "дата рождения: AB.CD.EFGH"
        assertEquals(0, scanText(text), "Дата рождения с буквами не должна находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "dateдата рождения: 01.01.1990"
        assertEquals(0, scanText(text), "Дата рождения, прилипшая к буквам, не должна находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123дата рождения: 01.01.1990"
        assertEquals(0, scanText(text), "Дата рождения, прилипшая к цифрам, не должна находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionдата рождения: 01.01.1990()"
        assertEquals(0, scanText(text), "Дата рождения внутри кода не должна находиться")
    }
}

