package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера MilitaryRank
 */
internal class MilitaryRankTest : MatcherTestBase(MilitaryRank) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Rank: рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The rank рядовой is valid"
        assertTrue(scanText(text) >= 1, "Воинское звание в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "рядовой\nSecond line"
        assertTrue(scanText(text) >= 1, "Воинское звание в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nрядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with рядовой rank"
        assertTrue(scanText(text) >= 1, "Воинское звание в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nрядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "рядовой\n"
        assertTrue(scanText(text) >= 1, "Воинское звание перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nрядовой\r\n"
        assertTrue(scanText(text) >= 1, "Воинское звание с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nрядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "рядовой\n\n"
        assertTrue(scanText(text) >= 1, "Воинское звание перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcрядовойdef"
        assertEquals(0, scanText(text), "Воинское звание внутри буквенной последовательности не должно находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123рядовой456"
        assertEquals(0, scanText(text), "Воинское звание внутри цифровой последовательности не должно находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Rank рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "рядовой is valid"
        assertTrue(scanText(text) >= 1, "Воинское звание с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Rank, рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "рядовой, next"
        assertTrue(scanText(text) >= 1, "Воинское звание с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Rank. рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "рядовой."
        assertTrue(scanText(text) >= 1, "Воинское звание с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Rank; рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "рядовой; next"
        assertTrue(scanText(text) >= 1, "Воинское звание с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Rank: рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание с двоеточием перед должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( рядовой )"
        assertTrue(scanText(text) >= 1, "Воинское звание в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(рядовой)"
        assertTrue(scanText(text) >= 1, "Воинское звание в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" рядовой \""
        assertTrue(scanText(text) >= 1, "Воинское звание в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"рядовой\""
        assertTrue(scanText(text) >= 1, "Воинское звание в кавычках без пробелов должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "рядовой сержант"
        assertTrue(scanText(text) >= 2, "Несколько воинских званий через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "рядовой, сержант"
        assertTrue(scanText(text) >= 2, "Несколько воинских званий через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "рядовой; сержант"
        assertTrue(scanText(text) >= 2, "Несколько воинских званий через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "рядовой\nсержант"
        assertTrue(scanText(text) >= 2, "Несколько воинских званий через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать воинских званий")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no military ranks at all"
        assertEquals(0, scanText(text), "Текст без воинских званий не должен находить совпадения")
    }

    @Test
    fun testRyadovoy() {
        val text = "рядовой"
        assertTrue(scanText(text) >= 1, "Рядовой должно быть найдено")
    }

    @Test
    fun testMatros() {
        val text = "матрос"
        assertTrue(scanText(text) >= 1, "Матрос должно быть найдено")
    }

    @Test
    fun testSerzhant() {
        val text = "сержант"
        assertTrue(scanText(text) >= 1, "Сержант должно быть найдено")
    }

    @Test
    fun testLeytenant() {
        val text = "лейтенант"
        assertTrue(scanText(text) >= 1, "Лейтенант должно быть найдено")
    }

    @Test
    fun testKapitan() {
        val text = "капитан"
        assertTrue(scanText(text) >= 1, "Капитан должно быть найдено")
    }

    @Test
    fun testGeneral() {
        val text = "генерал-майор"
        assertTrue(scanText(text) >= 1, "Генерал-майор должно быть найдено")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Rank     рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Rank\tрядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "рядовой\tnext"
        assertTrue(scanText(text) >= 1, "Воинское звание с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Звание рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Military rank рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "рядовой text"
        assertTrue(scanText(text) >= 1, "Воинское звание в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание в конце текста должно быть найдено")
    }

    @Test
    fun testWithVoinskoeZvanieKeyword() {
        val text = "воинское звание: рядовой"
        assertTrue(scanText(text) >= 1, "Воинское звание с ключевым словом должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidRank() {
        val text = "неправильное звание"
        assertEquals(0, scanText(text), "Несуществующее воинское звание не должно находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "rankрядовой"
        assertEquals(0, scanText(text), "Воинское звание, прилипшее к буквам, не должно находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123рядовой"
        assertEquals(0, scanText(text), "Воинское звание, прилипшее к цифрам, не должно находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionрядовой()"
        assertEquals(0, scanText(text), "Воинское звание внутри кода не должно находиться")
    }

    @Test
    fun testPartialRank() {
        val text = "рядо"
        assertEquals(0, scanText(text), "Частичное воинское звание не должно находиться")
    }
}

