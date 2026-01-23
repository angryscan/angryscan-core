package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера ResidencePermit
 */
internal class ResidencePermitTest : MatcherTestBase(ResidencePermit) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Residence permit: ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The permit ВНЖ 82 1234567 is valid"
        assertTrue(scanText(text) >= 1, "ВНЖ в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "ВНЖ 82 1234567\nSecond line"
        assertTrue(scanText(text) >= 1, "ВНЖ в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with ВНЖ 82 1234567 permit"
        assertTrue(scanText(text) >= 1, "ВНЖ в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "ВНЖ 82 1234567\n"
        assertTrue(scanText(text) >= 1, "ВНЖ перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nВНЖ 82 1234567\r\n"
        assertTrue(scanText(text) >= 1, "ВНЖ с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "ВНЖ 82 1234567\n\n"
        assertTrue(scanText(text) >= 1, "ВНЖ перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc82 1234567def"
        assertEquals(0, scanText(text), "ВНЖ внутри буквенной последовательности не должно находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12382 1234567456"
        assertEquals(0, scanText(text), "ВНЖ внутри цифровой последовательности не должно находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Permit ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "ВНЖ 82 1234567 is valid"
        assertTrue(scanText(text) >= 1, "ВНЖ с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Permit, ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "ВНЖ 82 1234567, next"
        assertTrue(scanText(text) >= 1, "ВНЖ с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Permit. ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "ВНЖ 82 1234567."
        assertTrue(scanText(text) >= 1, "ВНЖ с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Permit; ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "ВНЖ 82 1234567; next"
        assertTrue(scanText(text) >= 1, "ВНЖ с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Permit: ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с двоеточием перед должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( ВНЖ 82 1234567 )"
        assertTrue(scanText(text) >= 1, "ВНЖ в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(ВНЖ 82 1234567)"
        assertTrue(scanText(text) >= 1, "ВНЖ в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" ВНЖ 82 1234567 \""
        assertTrue(scanText(text) >= 1, "ВНЖ в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"ВНЖ 82 1234567\""
        assertTrue(scanText(text) >= 1, "ВНЖ в кавычках без пробелов должно быть найдено")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ ВНЖ 82 1234567 ]"
        assertTrue(scanText(text) >= 1, "ВНЖ в квадратных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ ВНЖ 82 1234567 }"
        assertTrue(scanText(text) >= 1, "ВНЖ в фигурных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithNumberSignAsPartOfFormat() {
        val text = "ВНЖ 82 № 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ со знаком № как часть формата должно быть найдено")
    }

    @Test
    fun testWithNAsPartOfFormat() {
        val text = "ВНЖ 82 N 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с N как часть формата должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "ВНЖ 82 1234567 ВНЖ 83 7654321"
        assertTrue(scanText(text) >= 2, "Несколько ВНЖ через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "ВНЖ 82 1234567, ВНЖ 83 7654321"
        assertTrue(scanText(text) >= 2, "Несколько ВНЖ через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "ВНЖ 82 1234567; ВНЖ 83 7654321"
        assertTrue(scanText(text) >= 2, "Несколько ВНЖ через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "ВНЖ 82 1234567\nВНЖ 83 7654321"
        assertTrue(scanText(text) >= 2, "Несколько ВНЖ через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ВНЖ")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no residence permits at all"
        assertEquals(0, scanText(text), "Текст без ВНЖ не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWith82Series() {
        val text = "ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с серией 82 должно быть найдено")
    }

    @Test
    fun testWith83Series() {
        val text = "ВНЖ 83 7654321"
        assertTrue(scanText(text) >= 1, "ВНЖ с серией 83 должно быть найдено")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Permit    ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Permit\tВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "ВНЖ 82 1234567\tnext"
        assertTrue(scanText(text) >= 1, "ВНЖ с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Вид на жительство 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Residence permit ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "ВНЖ 82 1234567 text"
        assertTrue(scanText(text) >= 1, "ВНЖ в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ в конце текста должно быть найдено")
    }

    @Test
    fun testWithVidNaZhitelstvoKeyword() {
        val text = "вид на жительство: 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с ключевым словом 'вид на жительство' должно быть найдено")
    }

    @Test
    fun testWithVNJKeyword() {
        val text = "ВНЖ: 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с ключевым словом 'ВНЖ' должно быть найдено")
    }

    @Test
    fun testWithNomerVidaNaZhitelstvoKeyword() {
        val text = "номер вида на жительство 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с ключевым словом 'номер вида на жительство' должно быть найдено")
    }

    @Test
    fun testWithSeriyaINomerVidaNaZhitelstvoKeyword() {
        val text = "серия и номер вида на жительство 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с ключевым словом 'серия и номер вида на жительство' должно быть найдено")
    }

    @Test
    fun testWithSeriyaINomerVNJKeyword() {
        val text = "серия и номер ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с ключевым словом 'серия и номер ВНЖ' должно быть найдено")
    }

    @Test
    fun testWithNomerVNJKeyword() {
        val text = "номер ВНЖ 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с ключевым словом 'номер ВНЖ' должно быть найдено")
    }

    @Test
    fun testWithDokumentVidaNaZhitelstvoKeyword() {
        val text = "документ вида на жительство 82 1234567"
        assertTrue(scanText(text) >= 1, "ВНЖ с ключевым словом 'документ вида на жительство' должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "ВНЖ 82 123456"
        assertEquals(0, scanText(text), "Слишком короткое ВНЖ не должно находиться")
    }

    @Test
    fun testTooLong() {
        val text = "ВНЖ 82 12345678"
        assertEquals(0, scanText(text), "Слишком длинное ВНЖ не должно находиться")
    }

    @Test
    fun testWithInvalidSeries() {
        val text = "ВНЖ 99 1234567"
        assertEquals(0, scanText(text), "ВНЖ с недопустимой серией не должно находиться")
    }

    @Test
    fun testPartialResidencePermit() {
        val text = "ВНЖ 82"
        assertEquals(0, scanText(text), "Частичное ВНЖ не должно находиться")
    }
}

