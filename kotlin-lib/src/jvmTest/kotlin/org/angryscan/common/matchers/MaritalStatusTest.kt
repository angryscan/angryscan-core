package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера MaritalStatus
 */
internal class MaritalStatusTest : MatcherTestBase(MaritalStatus) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "женат"
        assertTrue(scanText(text) >= 1, "Семейное положение в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Marital status: женат"
        assertTrue(scanText(text) >= 1, "Семейное положение в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The status женат is valid"
        assertTrue(scanText(text) >= 1, "Семейное положение в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "женат\nSecond line"
        assertTrue(scanText(text) >= 1, "Семейное положение в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nженат"
        assertTrue(scanText(text) >= 1, "Семейное положение в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with женат status"
        assertTrue(scanText(text) >= 1, "Семейное положение в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nженат"
        assertTrue(scanText(text) >= 1, "Семейное положение после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "женат\n"
        assertTrue(scanText(text) >= 1, "Семейное положение перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nженат\r\n"
        assertTrue(scanText(text) >= 1, "Семейное положение с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nженат"
        assertTrue(scanText(text) >= 1, "Семейное положение после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "женат\n\n"
        assertTrue(scanText(text) >= 1, "Семейное положение перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcженатdef"
        assertEquals(0, scanText(text), "Семейное положение внутри буквенной последовательности не должно находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Status женат"
        assertTrue(scanText(text) >= 1, "Семейное положение с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "женат is valid"
        assertTrue(scanText(text) >= 1, "Семейное положение с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Status, женат"
        assertTrue(scanText(text) >= 1, "Семейное положение с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "женат, next"
        assertTrue(scanText(text) >= 1, "Семейное положение с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Status. женат"
        assertTrue(scanText(text) >= 1, "Семейное положение с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "женат."
        assertTrue(scanText(text) >= 1, "Семейное положение с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Status; женат"
        assertTrue(scanText(text) >= 1, "Семейное положение с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "женат; next"
        assertTrue(scanText(text) >= 1, "Семейное положение с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Status: женат"
        assertTrue(scanText(text) >= 1, "Семейное положение с двоеточием перед должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( женат )"
        assertTrue(scanText(text) >= 1, "Семейное положение в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(женат)"
        assertTrue(scanText(text) >= 1, "Семейное положение в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" женат \""
        assertTrue(scanText(text) >= 1, "Семейное положение в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"женат\""
        assertTrue(scanText(text) >= 1, "Семейное положение в кавычках без пробелов должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "женат замужем"
        assertTrue(scanText(text) >= 2, "Несколько семейных положений через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "женат, замужем"
        assertTrue(scanText(text) >= 2, "Несколько семейных положений через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "женат; замужем"
        assertTrue(scanText(text) >= 2, "Несколько семейных положений через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "женат\nзамужем"
        assertTrue(scanText(text) >= 2, "Несколько семейных положений через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать семейных положений")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no marital statuses at all"
        assertEquals(0, scanText(text), "Текст без семейных положений не должен находить совпадения")
    }

    @Test
    fun testZhenat() {
        val text = "женат"
        assertTrue(scanText(text) >= 1, "Женат должно быть найдено")
    }

    @Test
    fun testZamuzhem() {
        val text = "замужем"
        assertTrue(scanText(text) >= 1, "Замужем должно быть найдено")
    }

    @Test
    fun testSostoitVBrake() {
        val text = "состоит в браке"
        assertTrue(scanText(text) >= 1, "Состоит в браке должно быть найдено")
    }

    @Test
    fun testRazveden() {
        val text = "разведен"
        assertTrue(scanText(text) >= 1, "Разведен должно быть найдено")
    }

    @Test
    fun testKholost() {
        val text = "холост"
        assertTrue(scanText(text) >= 1, "Холост должно быть найдено")
    }

    @Test
    fun testVdovets() {
        val text = "вдовец"
        assertTrue(scanText(text) >= 1, "Вдовец должно быть найдено")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Status     женат"
        assertTrue(scanText(text) >= 1, "Семейное положение с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Status\tженат"
        assertTrue(scanText(text) >= 1, "Семейное положение с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "женат\tnext"
        assertTrue(scanText(text) >= 1, "Семейное положение с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Статус женат"
        assertTrue(scanText(text) >= 1, "Семейное положение с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Marital status женат"
        assertTrue(scanText(text) >= 1, "Семейное положение с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "женат"
        assertTrue(scanText(text) >= 1, "Семейное положение отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "женат text"
        assertTrue(scanText(text) >= 1, "Семейное положение в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text женат"
        assertTrue(scanText(text) >= 1, "Семейное положение в конце текста должно быть найдено")
    }

    @Test
    fun testWithSemeinoePolozhenieKeyword() {
        val text = "семейное положение: женат"
        assertTrue(scanText(text) >= 1, "Семейное положение с ключевым словом должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidStatus() {
        val text = "неправильный статус"
        assertEquals(0, scanText(text), "Несуществующее семейное положение не должно находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "statusженат"
        assertEquals(0, scanText(text), "Семейное положение, прилипшее к буквам, не должно находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123женат"
        assertEquals(0, scanText(text), "Семейное положение, прилипшее к цифрам, не должно находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionженат()"
        assertEquals(0, scanText(text), "Семейное положение внутри кода не должно находиться")
    }

    @Test
    fun testPartialStatus() {
        val text = "жен"
        assertEquals(0, scanText(text), "Частичное семейное положение не должно находиться")
    }
}

