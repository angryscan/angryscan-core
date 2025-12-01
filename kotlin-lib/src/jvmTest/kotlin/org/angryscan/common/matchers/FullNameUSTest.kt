package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера FullNameUS
 */
internal class FullNameUSTest : MatcherTestBase(FullNameUS) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Name: John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The name John Smith is valid"
        assertTrue(scanText(text) >= 1, "ФИО США в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "John Smith\nSecond line"
        assertTrue(scanText(text) >= 1, "ФИО США в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nJohn Smith"
        assertTrue(scanText(text) >= 1, "ФИО США в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with John Smith name"
        assertTrue(scanText(text) >= 1, "ФИО США в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nJohn Smith"
        assertTrue(scanText(text) >= 1, "ФИО США после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "John Smith\n"
        assertTrue(scanText(text) >= 1, "ФИО США перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nJohn Smith\r\n"
        assertTrue(scanText(text) >= 1, "ФИО США с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nJohn Smith"
        assertTrue(scanText(text) >= 1, "ФИО США после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "John Smith\n\n"
        assertTrue(scanText(text) >= 1, "ФИО США перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcJohn Smithdef"
        assertEquals(0, scanText(text), "ФИО США внутри буквенной последовательности не должно находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Name John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "John Smith is valid"
        assertTrue(scanText(text) >= 1, "ФИО США с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Name, John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "John Smith, next"
        assertTrue(scanText(text) >= 1, "ФИО США с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Name. John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "John Smith."
        assertTrue(scanText(text) >= 1, "ФИО США с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Name; John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "John Smith; next"
        assertTrue(scanText(text) >= 1, "ФИО США с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Name: John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США с двоеточием перед должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( John Smith )"
        assertTrue(scanText(text) >= 1, "ФИО США в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(John Smith)"
        assertTrue(scanText(text) >= 1, "ФИО США в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" John Smith \""
        assertTrue(scanText(text) >= 1, "ФИО США в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"John Smith\""
        assertTrue(scanText(text) >= 1, "ФИО США в кавычках без пробелов должно быть найдено")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ John Smith ]"
        assertTrue(scanText(text) >= 1, "ФИО США в квадратных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ John Smith }"
        assertTrue(scanText(text) >= 1, "ФИО США в фигурных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithMiddleInitial() {
        val text = "John A. Smith"
        assertTrue(scanText(text) >= 1, "ФИО США с инициалом должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "John Smith Jane Doe"
        assertTrue(scanText(text) >= 2, "Несколько ФИО США через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "John Smith\nJane Doe"
        assertTrue(scanText(text) >= 2, "Несколько ФИО США через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ФИО США")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no US full names at all"
        assertEquals(0, scanText(text), "Текст без ФИО США не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "John Smith"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Name    John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Name\tJohn Smith"
        assertTrue(scanText(text) >= 1, "ФИО США с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "John Smith\tnext"
        assertTrue(scanText(text) >= 1, "ФИО США с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Имя John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Name John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "John Smith text"
        assertTrue(scanText(text) >= 1, "ФИО США в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text John Smith"
        assertTrue(scanText(text) >= 1, "ФИО США в конце текста должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "J S"
        assertEquals(0, scanText(text), "Слишком короткое ФИО США не должно находиться")
    }

    @Test
    fun testWithOnlyNumbers() {
        val text = "123 456"
        assertEquals(0, scanText(text), "Только цифры не должны находиться как ФИО США")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "nameJohn Smith"
        assertEquals(0, scanText(text), "ФИО США, прилипшее к буквам, не должно находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123John Smith"
        assertEquals(0, scanText(text), "ФИО США, прилипшее к цифрам, не должно находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionJohn Smith()"
        assertEquals(0, scanText(text), "ФИО США внутри кода не должно находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "JohnSmith"
        assertEquals(0, scanText(text), "ФИО США без пробела не должно находиться")
    }

    @Test
    fun testSingleWord() {
        val text = "John"
        assertEquals(0, scanText(text), "Одно слово не должно находиться как ФИО США")
    }
}

