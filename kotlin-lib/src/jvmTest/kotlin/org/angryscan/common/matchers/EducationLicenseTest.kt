package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера EducationLicense
 */
internal class EducationLicenseTest : MatcherTestBase(EducationLicense) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в начале текста должна быть найдена")
    }

    @Test
    fun testAtEndOfText() {
        val text = "License: Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в конце текста должна быть найдена")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The license Л 035-12345-12/12345678 is valid"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в середине текста должна быть найдена")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "Л 035-12345-12/12345678\nSecond line"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в начале строки должна быть найдена")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nЛ 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в конце строки должна быть найдена")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with Л 035-12345-12/12345678 license"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в середине строки должна быть найдена")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nЛ 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность после \\n должна быть найдена")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "Л 035-12345-12/12345678\n"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность перед \\n должна быть найдена")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nЛ 035-12345-12/12345678\r\n"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с \\r\\n должна быть найдена")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nЛ 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность после пустой строки должна быть найдена")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "Л 035-12345-12/12345678\n\n"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность перед пустой строкой должна быть найдена")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcЛ 035-12345-12/12345678def"
        assertEquals(0, scanText(text), "Лицензия на образовательную деятельность внутри буквенной последовательности не должна находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123Л 035-12345-12/12345678456"
        assertEquals(0, scanText(text), "Лицензия на образовательную деятельность внутри цифровой последовательности не должна находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "License Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с пробелом перед должна быть найдена")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "Л 035-12345-12/12345678 is valid"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с пробелом после должна быть найдена")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "License, Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с запятой перед должна быть найдена")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "Л 035-12345-12/12345678, next"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с запятой после должна быть найдена")
    }

    @Test
    fun testWithDotBefore() {
        val text = "License. Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с точкой перед должна быть найдена")
    }

    @Test
    fun testWithDotAfter() {
        val text = "Л 035-12345-12/12345678."
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с точкой после должна быть найдена")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "License; Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с точкой с запятой перед должна быть найдена")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "Л 035-12345-12/12345678; next"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с точкой с запятой после должна быть найдена")
    }

    @Test
    fun testWithColonBefore() {
        val text = "License: Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с двоеточием перед должна быть найдена")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( Л 035-12345-12/12345678 )"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в скобках с пробелами должна быть найдена")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(Л 035-12345-12/12345678)"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в скобках без пробелов должна быть найдена")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" Л 035-12345-12/12345678 \""
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в кавычках с пробелами должна быть найдена")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"Л 035-12345-12/12345678\""
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в кавычках без пробелов должна быть найдена")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с дефисом как часть формата должна быть найдена")
    }

    @Test
    fun testWithSlashAsPartOfFormat() {
        val text = "Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность со слэшем как часть формата должна быть найдена")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "Л 035-12345-12/12345678 Л 035-54321-34/87654321"
        assertTrue(scanText(text) >= 2, "Несколько лицензий на образовательную деятельность через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "Л 035-12345-12/12345678, Л 035-54321-34/87654321"
        assertTrue(scanText(text) >= 2, "Несколько лицензий на образовательную деятельность через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "Л 035-12345-12/12345678; Л 035-54321-34/87654321"
        assertTrue(scanText(text) >= 2, "Несколько лицензий на образовательную деятельность через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "Л 035-12345-12/12345678\nЛ 035-54321-34/87654321"
        assertTrue(scanText(text) >= 2, "Несколько лицензий на образовательную деятельность через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать лицензий на образовательную деятельность")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no education licenses at all"
        assertEquals(0, scanText(text), "Текст без лицензий на образовательную деятельность не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithLRusAsPartOfFormat() {
        val text = "Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия с Л должна быть найдена")
    }

    @Test
    fun testWithLAsPartOfFormat() {
        val text = "L 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия с L должна быть найдена")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "License     Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с несколькими пробелами должна быть найдена")
    }

    @Test
    fun testWithTabBefore() {
        val text = "License\tЛ 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с табуляцией перед должна быть найдена")
    }

    @Test
    fun testWithTabAfter() {
        val text = "Л 035-12345-12/12345678\tnext"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с табуляцией после должна быть найдена")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Лицензия Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с кириллицей рядом должна быть найдена")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Education license Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность с латиницей рядом должна быть найдена")
    }

    @Test
    fun testStandalone() {
        val text = "Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность отдельной строкой должна быть найдена")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "Л 035-12345-12/12345678 text"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в начале текста должна быть найдена")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия на образовательную деятельность в конце текста должна быть найдена")
    }

    @Test
    fun testWithLicenseKeyword() {
        val text = "лицензия на образовательную деятельность: Л 035-12345-12/12345678"
        assertTrue(scanText(text) >= 1, "Лицензия с ключевым словом должна быть найдена")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidFormat() {
        val text = "Л 035@12345@12@12345678"
        assertEquals(0, scanText(text), "Лицензия с неправильными разделителями не должна находиться")
    }

    @Test
    fun testTooShort() {
        val text = "Л 035-1234-12/12345678"
        assertEquals(0, scanText(text), "Слишком короткая лицензия не должна находиться")
    }

    @Test
    fun testTooLong() {
        val text = "Л 035-123456-12/12345678"
        assertEquals(0, scanText(text), "Слишком длинная лицензия не должна находиться")
    }

    @Test
    fun testWithInvalidPrefix() {
        val text = "X 035-12345-12/12345678"
        assertEquals(0, scanText(text), "Лицензия с недопустимым префиксом не должна находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "licenseЛ 035-12345-12/12345678"
        assertEquals(0, scanText(text), "Лицензия, прилипшая к буквам, не должна находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123Л 035-12345-12/12345678"
        assertEquals(0, scanText(text), "Лицензия, прилипшая к цифрам, не должна находиться")
    }

    @Test
    fun testPartialEducationLicense() {
        val text = "Л 035"
        assertEquals(0, scanText(text), "Частичная лицензия не должна находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionЛ 035-12345-12/12345678()"
        assertEquals(0, scanText(text), "Лицензия внутри кода не должна находиться")
    }
}

