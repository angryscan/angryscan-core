package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера EducationDoc
 */
internal class EducationDocTest : MatcherTestBase(EducationDoc) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Education document: 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The document 123456 1234567 is valid"
        assertTrue(scanText(text) >= 1, "Документ об образовании в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "123456 1234567\nSecond line"
        assertTrue(scanText(text) >= 1, "Документ об образовании в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 123456 1234567 document"
        assertTrue(scanText(text) >= 1, "Документ об образовании в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "123456 1234567\n"
        assertTrue(scanText(text) >= 1, "Документ об образовании перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n123456 1234567\r\n"
        assertTrue(scanText(text) >= 1, "Документ об образовании с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "123456 1234567\n\n"
        assertTrue(scanText(text) >= 1, "Документ об образовании перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc123456 1234567def"
        assertEquals(0, scanText(text), "Документ об образовании внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123123456 1234567456"
        assertEquals(0, scanText(text), "Документ об образовании внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Document 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "123456 1234567 is valid"
        assertTrue(scanText(text) >= 1, "Документ об образовании с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Document, 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "123456 1234567, next"
        assertTrue(scanText(text) >= 1, "Документ об образовании с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Document. 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "123456 1234567."
        assertTrue(scanText(text) >= 1, "Документ об образовании с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Document; 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "123456 1234567; next"
        assertTrue(scanText(text) >= 1, "Документ об образовании с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Document: 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 123456 1234567 )"
        assertTrue(scanText(text) >= 1, "Документ об образовании в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(123456 1234567)"
        assertTrue(scanText(text) >= 1, "Документ об образовании в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 123456 1234567 \""
        assertTrue(scanText(text) >= 1, "Документ об образовании в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"123456 1234567\""
        assertTrue(scanText(text) >= 1, "Документ об образовании в кавычках без пробелов должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "123456 1234567 654321 7654321"
        assertTrue(scanText(text) >= 2, "Несколько документов об образовании через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "123456 1234567, 654321 7654321"
        assertTrue(scanText(text) >= 2, "Несколько документов об образовании через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "123456 1234567; 654321 7654321"
        assertTrue(scanText(text) >= 2, "Несколько документов об образовании через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "123456 1234567\n654321 7654321"
        assertTrue(scanText(text) >= 2, "Несколько документов об образовании через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать документов об образовании")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no education documents at all"
        assertEquals(0, scanText(text), "Текст без документов об образовании не должен находить совпадения")
    }

    @Test
    fun testFormat1() {
        val text = "123456 1234567"
        assertTrue(scanText(text) >= 1, "Формат 1 (6 цифр + 7 цифр) должен быть найден")
    }

    @Test
    fun testFormat2() {
        val text = "12 АБ 1234567"
        assertTrue(scanText(text) >= 1, "Формат 2 (2 цифры + 2 буквы + 6-7 цифр) должен быть найден")
    }

    @Test
    fun testFormat3() {
        val text = "IV-АБ 123456"
        assertTrue(scanText(text) >= 1, "Формат 3 (римские цифры + 2 буквы + 6 цифр) должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Document    123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Document\t123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "123456 1234567\tnext"
        assertTrue(scanText(text) >= 1, "Документ об образовании с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Документ 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Education document 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "123456 1234567 text"
        assertTrue(scanText(text) >= 1, "Документ об образовании в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "12345 123456"
        assertEquals(0, scanText(text), "Слишком короткий документ об образовании не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "1234567 12345678"
        assertEquals(0, scanText(text), "Слишком длинный документ об образовании не должен находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "123456@1234567"
        assertEquals(0, scanText(text), "Документ об образовании с неправильными разделителями не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "doc123456 1234567"
        assertEquals(0, scanText(text), "Документ об образовании, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123123456 1234567"
        assertEquals(0, scanText(text), "Документ об образовании, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testPartialEducationDoc() {
        val text = "123456"
        assertEquals(0, scanText(text), "Частичный документ об образовании не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function123456 1234567()"
        assertEquals(0, scanText(text), "Документ об образовании внутри кода не должен находиться")
    }
}

