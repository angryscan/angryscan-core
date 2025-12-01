package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера SSN
 */
internal class SSNTest : MatcherTestBase(SSN) {

    // Валидный SSN для тестов (правильный формат)
    private val validSSN = "234-56-7890" // Пример валидного SSN

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "234-56-7890 is a SSN"
        assertTrue(scanText(text) >= 1, "SSN в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "SSN number is 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The SSN 234-56-7890 is valid"
        assertTrue(scanText(text) >= 1, "SSN в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "234-56-7890\nSecond line"
        assertTrue(scanText(text) >= 1, "SSN в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 234-56-7890 SSN"
        assertTrue(scanText(text) >= 1, "SSN в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "234-56-7890\n"
        assertTrue(scanText(text) >= 1, "SSN перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n234-56-7890\r\n"
        assertTrue(scanText(text) >= 1, "SSN с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "234-56-7890\n\n"
        assertTrue(scanText(text) >= 1, "SSN перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc234-56-7890def"
        assertEquals(0, scanText(text), "SSN внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123456234-56-7890456"
        assertEquals(0, scanText(text), "SSN внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc123234-56-7890def456"
        assertEquals(0, scanText(text), "SSN внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "SSN 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "234-56-7890 is valid"
        assertTrue(scanText(text) >= 1, "SSN с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "SSN, 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "234-56-7890, next"
        assertTrue(scanText(text) >= 1, "SSN с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "SSN. 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "234-56-7890."
        assertTrue(scanText(text) >= 1, "SSN с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "SSN; 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "234-56-7890; next"
        assertTrue(scanText(text) >= 1, "SSN с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "SSN: 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "234-56-7890!"
        assertTrue(scanText(text) >= 1, "SSN с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "234-56-7890?"
        assertTrue(scanText(text) >= 1, "SSN с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 234-56-7890 )"
        assertTrue(scanText(text) >= 1, "SSN в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(234-56-7890)"
        assertTrue(scanText(text) >= 1, "SSN в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 234-56-7890 \""
        assertTrue(scanText(text) >= 1, "SSN в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"234-56-7890\""
        assertTrue(scanText(text) >= 1, "SSN в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "ssn = 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "ssn # 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 234-56-7890 ]"
        assertTrue(scanText(text) >= 1, "SSN в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 234-56-7890 }"
        assertTrue(scanText(text) >= 1, "SSN в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с дефисом как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "234-56-7890 345-67-8901"
        assertTrue(scanText(text) >= 2, "Несколько SSN через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "234-56-7890, 345-67-8901"
        assertTrue(scanText(text) >= 2, "Несколько SSN через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "234-56-7890; 345-67-8901"
        assertTrue(scanText(text) >= 2, "Несколько SSN через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "234-56-7890\n345-67-8901"
        assertTrue(scanText(text) >= 2, "Несколько SSN через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать SSN")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no SSN numbers at all"
        assertEquals(0, scanText(text), "Текст без SSN не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "234-56-7890"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "SSN    234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "SSN\t234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "234-56-7890\tnext"
        assertTrue(scanText(text) >= 1, "SSN с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "SSN номер 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "SSN number 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "234-56-7890 text"
        assertTrue(scanText(text) >= 1, "SSN в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 234-56-7890"
        assertTrue(scanText(text) >= 1, "SSN в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidFormat() {
        val text = "123-456-789"
        assertEquals(0, scanText(text), "SSN с неправильным форматом не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "123-45-678"
        assertEquals(0, scanText(text), "Слишком короткий SSN не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "234-56-78900"
        assertEquals(0, scanText(text), "Слишком длинный SSN не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "123-45-ABCD"
        assertEquals(0, scanText(text), "SSN с буквами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "SSN внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "ssn234-56-7890"
        assertEquals(0, scanText(text), "SSN, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123234-56-7890"
        assertEquals(0, scanText(text), "SSN, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "123@45@6789"
        assertEquals(0, scanText(text), "SSN с неправильными разделителями не должен находиться")
    }

    @Test
    fun testPartialSSN() {
        val text = "123-45"
        assertEquals(0, scanText(text), "Частичный SSN не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function234-56-7890()"
        assertEquals(0, scanText(text), "SSN внутри кода не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "000-00-0000"
        assertEquals(0, scanText(text), "SSN из всех нулей не должен находиться")
    }
}

