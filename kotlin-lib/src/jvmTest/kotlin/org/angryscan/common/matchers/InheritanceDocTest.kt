package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера InheritanceDoc
 */
internal class InheritanceDocTest : MatcherTestBase(InheritanceDoc) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Inheritance document: 12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The document 12 АБ 123456 is valid"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "12 АБ 123456\nSecond line"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 12 АБ 123456 document"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "12 АБ 123456\n"
        assertTrue(scanText(text) >= 1, "Документ о наследовании перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n12 АБ 123456\r\n"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "12 АБ 123456\n\n"
        assertTrue(scanText(text) >= 1, "Документ о наследовании перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc12 АБ 123456def"
        assertEquals(0, scanText(text), "Документ о наследовании внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12312 АБ 123456456"
        assertEquals(0, scanText(text), "Документ о наследовании внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Document 12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "12 АБ 123456 is valid"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Document, 12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "12 АБ 123456, next"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Document. 12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "12 АБ 123456."
        assertTrue(scanText(text) >= 1, "Документ о наследовании с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Document; 12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "12 АБ 123456; next"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Document: 12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 12 АБ 123456 )"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(12 АБ 123456)"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 12 АБ 123456 \""
        assertTrue(scanText(text) >= 1, "Документ о наследовании в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"12 АБ 123456\""
        assertTrue(scanText(text) >= 1, "Документ о наследовании в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 12 АБ 123456 ]"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 12 АБ 123456 }"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "12 АБ 123456 34 ВГ 654321"
        assertTrue(scanText(text) >= 2, "Несколько документов о наследовании через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "12 АБ 123456, 34 ВГ 654321"
        assertTrue(scanText(text) >= 2, "Несколько документов о наследовании через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "12 АБ 123456; 34 ВГ 654321"
        assertTrue(scanText(text) >= 2, "Несколько документов о наследовании через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "12 АБ 123456\n34 ВГ 654321"
        assertTrue(scanText(text) >= 2, "Несколько документов о наследовании через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать документов о наследовании")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no inheritance documents at all"
        assertEquals(0, scanText(text), "Текст без документов о наследовании не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWith6Digits() {
        val text = "12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Формат с 6 цифрами должен быть найден")
    }

    @Test
    fun testWith7Digits() {
        val text = "12 АБ 1234567"
        assertTrue(scanText(text) >= 1, "Формат с 7 цифрами должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Document    12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Document\t12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "12 АБ 123456\tnext"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Документ 12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Inheritance document 12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "12 АБ 123456 text"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 12 АБ 123456"
        assertTrue(scanText(text) >= 1, "Документ о наследовании в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "12 АБ 12345"
        assertEquals(0, scanText(text), "Слишком короткий документ о наследовании не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "12 АБ 12345678"
        assertEquals(0, scanText(text), "Слишком длинный документ о наследовании не должен находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "12@АБ@123456"
        assertEquals(0, scanText(text), "Документ о наследовании с неправильными разделителями не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "doc12 АБ 123456"
        assertEquals(0, scanText(text), "Документ о наследовании, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12312 АБ 123456"
        assertEquals(0, scanText(text), "Документ о наследовании, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testPartialInheritanceDoc() {
        val text = "12 АБ"
        assertEquals(0, scanText(text), "Частичный документ о наследовании не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function12 АБ 123456()"
        assertEquals(0, scanText(text), "Документ о наследовании внутри кода не должен находиться")
    }
}

