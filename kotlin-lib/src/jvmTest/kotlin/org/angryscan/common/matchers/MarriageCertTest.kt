package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера MarriageCert
 */
internal class MarriageCertTest : MatcherTestBase(MarriageCert) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Marriage certificate: IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The certificate IV-АБ 123456 is valid"
        assertTrue(scanText(text) >= 1, "Свидетельство в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "IV-АБ 123456\nSecond line"
        assertTrue(scanText(text) >= 1, "Свидетельство в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nIV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with IV-АБ 123456 certificate"
        assertTrue(scanText(text) >= 1, "Свидетельство в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nIV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "IV-АБ 123456\n"
        assertTrue(scanText(text) >= 1, "Свидетельство перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nIV-АБ 123456\r\n"
        assertTrue(scanText(text) >= 1, "Свидетельство с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nIV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "IV-АБ 123456\n\n"
        assertTrue(scanText(text) >= 1, "Свидетельство перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcIV-АБ 123456def"
        assertEquals(0, scanText(text), "Свидетельство внутри буквенной последовательности не должно находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123IV-АБ 123456456"
        assertEquals(0, scanText(text), "Свидетельство внутри цифровой последовательности не должно находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Certificate IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "IV-АБ 123456 is valid"
        assertTrue(scanText(text) >= 1, "Свидетельство с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Certificate, IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "IV-АБ 123456, next"
        assertTrue(scanText(text) >= 1, "Свидетельство с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Certificate. IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "IV-АБ 123456."
        assertTrue(scanText(text) >= 1, "Свидетельство с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Certificate; IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "IV-АБ 123456; next"
        assertTrue(scanText(text) >= 1, "Свидетельство с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Certificate: IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с двоеточием перед должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( IV-АБ 123456 )"
        assertTrue(scanText(text) >= 1, "Свидетельство в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(IV-АБ 123456)"
        assertTrue(scanText(text) >= 1, "Свидетельство в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" IV-АБ 123456 \""
        assertTrue(scanText(text) >= 1, "Свидетельство в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"IV-АБ 123456\""
        assertTrue(scanText(text) >= 1, "Свидетельство в кавычках без пробелов должно быть найдено")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с дефисом как часть формата должно быть найдено")
    }

    @Test
    fun testWithEmDashAsPartOfFormat() {
        val text = "IV–АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с длинным тире как часть формата должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithCommas() {
        val text = "IV-АБ 123456, V-ВГ 654321"
        assertTrue(scanText(text) >= 2, "Несколько свидетельств через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "IV-АБ 123456; V-ВГ 654321"
        assertTrue(scanText(text) >= 2, "Несколько свидетельств через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "IV-АБ 123456\nV-ВГ 654321"
        assertTrue(scanText(text) >= 2, "Несколько свидетельств через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать свидетельств")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no marriage certificates at all"
        assertEquals(0, scanText(text), "Текст без свидетельств не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "I-АБ 123456"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testMaximalFormat() {
        val text = "IVX-АБ 123456"
        assertTrue(scanText(text) >= 1, "Максимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Certificate    IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Certificate\tIV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "IV-АБ 123456\tnext"
        assertTrue(scanText(text) >= 1, "Свидетельство с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Свидетельство IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Certificate IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "IV-АБ 123456 text"
        assertTrue(scanText(text) >= 1, "Свидетельство в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство в конце текста должно быть найдено")
    }

    @Test
    fun testWithSvidetelstvoKeyword() {
        val text = "свидетельство о браке: IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с ключевым словом должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "IV-АБ 12345"
        assertEquals(0, scanText(text), "Слишком короткое свидетельство не должно находиться")
    }

    @Test
    fun testTooLong() {
        val text = "IV-АБ 1234567"
        assertEquals(0, scanText(text), "Слишком длинное свидетельство не должно находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "IV@АБ@123456"
        assertEquals(0, scanText(text), "Свидетельство с неправильными разделителями не должно находиться")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABCIV-АБ 123456DEF"
        assertEquals(0, scanText(text), "Свидетельство внутри длинной последовательности не должно находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "certIV-АБ 123456"
        assertEquals(0, scanText(text), "Свидетельство, прилипшее к буквам, не должно находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123IV-АБ 123456"
        assertEquals(0, scanText(text), "Свидетельство, прилипшее к цифрам, не должно находиться")
    }

    @Test
    fun testPartialMarriageCert() {
        val text = "IV-АБ"
        assertEquals(0, scanText(text), "Частичное свидетельство не должно находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionIV-АБ 123456()"
        assertEquals(0, scanText(text), "Свидетельство внутри кода не должно находиться")
    }
}

