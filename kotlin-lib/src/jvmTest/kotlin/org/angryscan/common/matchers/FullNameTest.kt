package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера FullName
 */
internal class FullNameTest : MatcherTestBase(FullName) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Name: Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The name Иванов Иван Иванович is valid"
        assertTrue(scanText(text) >= 1, "ФИО в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "Иванов Иван Иванович\nSecond line"
        assertTrue(scanText(text) >= 1, "ФИО в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nИванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with Иванов Иван Иванович name"
        assertTrue(scanText(text) >= 1, "ФИО в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nИванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "Иванов Иван Иванович\n"
        assertTrue(scanText(text) >= 1, "ФИО перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nИванов Иван Иванович\r\n"
        assertTrue(scanText(text) >= 1, "ФИО с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nИванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "Иванов Иван Иванович\n\n"
        assertTrue(scanText(text) >= 1, "ФИО перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testWithSpaceBefore() {
        val text = "Name Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "Иванов Иван Иванович is valid"
        assertTrue(scanText(text) >= 1, "ФИО с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Name, Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "Иванов Иван Иванович, next"
        assertTrue(scanText(text) >= 1, "ФИО с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Name. Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "Иванов Иван Иванович."
        assertTrue(scanText(text) >= 1, "ФИО с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Name; Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "Иванов Иван Иванович; next"
        assertTrue(scanText(text) >= 1, "ФИО с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Name: Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с двоеточием перед должно быть найдено")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "Иванов Иван Иванович!"
        assertTrue(scanText(text) >= 1, "ФИО с восклицательным знаком после должно быть найдено")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "Иванов Иван Иванович?"
        assertTrue(scanText(text) >= 1, "ФИО с вопросительным знаком после должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( Иванов Иван Иванович )"
        assertTrue(scanText(text) >= 1, "ФИО в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(Иванов Иван Иванович)"
        assertTrue(scanText(text) >= 1, "ФИО в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" Иванов Иван Иванович \""
        assertTrue(scanText(text) >= 1, "ФИО в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"Иванов Иван Иванович\""
        assertTrue(scanText(text) >= 1, "ФИО в кавычках без пробелов должно быть найдено")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "name = Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с = и пробелом должно быть найдено")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "name # Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с # и пробелом должно быть найдено")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ Иванов Иван Иванович ]"
        assertTrue(scanText(text) >= 1, "ФИО в квадратных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ Иванов Иван Иванович }"
        assertTrue(scanText(text) >= 1, "ФИО в фигурных скобках с пробелами должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithCommas() {
        val text = "Иванов Иван Иванович, Петров Петр Петрович"
        assertTrue(scanText(text) >= 2, "Несколько ФИО через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "Иванов Иван Иванович; Петров Петр Петрович"
        assertTrue(scanText(text) >= 2, "Несколько ФИО через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "Иванов Иван Иванович\nПетров Петр Петрович"
        assertTrue(scanText(text) >= 2, "Несколько ФИО через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ФИО")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no full names at all"
        assertEquals(0, scanText(text), "Текст без ФИО не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Name    Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Name\tИванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "Иванов Иван Иванович\tnext"
        assertTrue(scanText(text) >= 1, "ФИО с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Имя Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Name Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "Иванов Иван Иванович text"
        assertTrue(scanText(text) >= 1, "ФИО в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО в конце текста должно быть найдено")
    }

    @Test
    fun testWithFemalePatronymic() {
        val text = "Иванова Мария Ивановна"
        assertTrue(scanText(text) >= 1, "ФИО с женским отчеством должно быть найдено")
    }

    @Test
    fun testWithMalePatronymic() {
        val text = "Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО с мужским отчеством должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShortName() {
        val text = "И А"
        assertEquals(0, scanText(text), "Слишком короткое ФИО не должно находиться")
    }

    @Test
    fun testWithNumbers() {
        val text = "Иванов Иван123 Иванович"
        assertEquals(0, scanText(text), "ФИО с цифрами не должно находиться")
    }

    @Test
    fun testSingleWord() {
        val text = "Иванов"
        assertEquals(0, scanText(text), "Одно слово не должно находиться как ФИО")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "ИвановИванИванович"
        assertEquals(0, scanText(text), "ФИО без пробелов не должно находиться")
    }
}

