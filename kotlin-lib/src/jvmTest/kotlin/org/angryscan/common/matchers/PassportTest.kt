package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера Passport
 */
internal class PassportTest : MatcherTestBase(Passport) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Passport number is паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The паспорт 12 34 123456 is valid"
        assertTrue(scanText(text) >= 1, "Паспорт в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "паспорт 12 34 123456\nSecond line"
        assertTrue(scanText(text) >= 1, "Паспорт в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nпаспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with паспорт 12 34 123456 passport"
        assertTrue(scanText(text) >= 1, "Паспорт в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nпаспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "паспорт 12 34 123456\n"
        assertTrue(scanText(text) >= 1, "Паспорт перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nпаспорт 12 34 123456\r\n"
        assertTrue(scanText(text) >= 1, "Паспорт с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nпаспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "паспорт 12 34 123456\n\n"
        assertTrue(scanText(text) >= 1, "Паспорт перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcпаспорт 12 34 123456def"
        assertEquals(0, scanText(text), "Паспорт внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123паспорт 12 34 123456456"
        assertEquals(0, scanText(text), "Паспорт внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Document паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "паспорт 12 34 123456 is valid"
        assertTrue(scanText(text) >= 1, "Паспорт с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Document, паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "паспорт 12 34 123456, next"
        assertTrue(scanText(text) >= 1, "Паспорт с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Document. паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "паспорт 12 34 123456."
        assertTrue(scanText(text) >= 1, "Паспорт с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Document; паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "паспорт 12 34 123456; next"
        assertTrue(scanText(text) >= 1, "Паспорт с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Document: паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "паспорт 12 34 123456!"
        assertTrue(scanText(text) >= 1, "Паспорт с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "паспорт 12 34 123456?"
        assertTrue(scanText(text) >= 1, "Паспорт с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( паспорт 12 34 123456 )"
        assertTrue(scanText(text) >= 1, "Паспорт в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(паспорт 12 34 123456)"
        assertTrue(scanText(text) >= 1, "Паспорт в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" паспорт 12 34 123456 \""
        assertTrue(scanText(text) >= 1, "Паспорт в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"паспорт 12 34 123456\""
        assertTrue(scanText(text) >= 1, "Паспорт в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "passport = паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "passport # паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ паспорт 12 34 123456 ]"
        assertTrue(scanText(text) >= 1, "Паспорт в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ паспорт 12 34 123456 }"
        assertTrue(scanText(text) >= 1, "Паспорт в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "паспорт 12 34 123456 паспорт 56 78 654321"
        assertTrue(scanText(text) >= 2, "Несколько паспортов через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "паспорт 12 34 123456, паспорт 56 78 654321"
        assertTrue(scanText(text) >= 2, "Несколько паспортов через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "паспорт 12 34 123456; паспорт 56 78 654321"
        assertTrue(scanText(text) >= 2, "Несколько паспортов через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "паспорт 12 34 123456\nпаспорт 56 78 654321"
        assertTrue(scanText(text) >= 2, "Несколько паспортов через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать паспортов")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no passport numbers at all"
        assertEquals(0, scanText(text), "Текст без паспортов не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithSpacesFormat() {
        val text = "паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с пробелами должен быть найден")
    }

    @Test
    fun testWithDashesFormat() {
        val text = "паспорт 12-34-123456"
        assertTrue(scanText(text) >= 1, "Паспорт с дефисами должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Document    паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Document\tпаспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "паспорт 12 34 123456\tnext"
        assertTrue(scanText(text) >= 1, "Паспорт с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Документ паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Document паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "паспорт 12 34 123456 text"
        assertTrue(scanText(text) >= 1, "Паспорт в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text паспорт 12 34 123456"
        assertTrue(scanText(text) >= 1, "Паспорт в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidSeries() {
        val text = "99 99 123456"
        assertEquals(0, scanText(text), "Паспорт с недопустимой серией не должен находиться")
    }

    @Test
    fun testTooShortNumber() {
        val text = "12 34 12345"
        assertEquals(0, scanText(text), "Паспорт с слишком коротким номером не должен находиться")
    }

    @Test
    fun testTooLongNumber() {
        val text = "12 34 1234567"
        assertEquals(0, scanText(text), "Паспорт с слишком длинным номером не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "12 34 ABCDEF"
        assertEquals(0, scanText(text), "Паспорт с буквами в номере не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "Паспорт внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "passport12 34 123456"
        assertEquals(0, scanText(text), "Паспорт, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12312 34 123456"
        assertEquals(0, scanText(text), "Паспорт, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "12@34@123456"
        assertEquals(0, scanText(text), "Паспорт с неправильными разделителями не должен находиться")
    }

    @Test
    fun testPartialPassport() {
        val text = "12 34"
        assertEquals(0, scanText(text), "Частичный паспорт не должен находиться")
    }

    @Test
    fun testPassportInCode() {
        val text = "function12 34 123456()"
        assertEquals(0, scanText(text), "Паспорт внутри кода не должен находиться")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "12#34#123456"
        assertEquals(0, scanText(text), "Паспорт с недопустимыми символами не должен находиться")
    }

    @Test
    fun testWithSpacesInWrongPlaces() {
        val text = "1 2 34 123456"
        assertEquals(0, scanText(text), "Паспорт с пробелами в неправильных местах не должен находиться")
    }
}

