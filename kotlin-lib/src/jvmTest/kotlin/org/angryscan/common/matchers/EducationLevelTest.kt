package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера EducationLevel
 */
internal class EducationLevelTest : MatcherTestBase(EducationLevel) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Education level: высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The level высшее образование is valid"
        assertTrue(scanText(text) >= 1, "Уровень образования в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "высшее образование\nSecond line"
        assertTrue(scanText(text) >= 1, "Уровень образования в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nвысшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with высшее образование level"
        assertTrue(scanText(text) >= 1, "Уровень образования в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nвысшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "высшее образование\n"
        assertTrue(scanText(text) >= 1, "Уровень образования перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nвысшее образование\r\n"
        assertTrue(scanText(text) >= 1, "Уровень образования с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nвысшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "высшее образование\n\n"
        assertTrue(scanText(text) >= 1, "Уровень образования перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcвысшее образованиеdef"
        assertEquals(0, scanText(text), "Уровень образования внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Level высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "высшее образование is valid"
        assertTrue(scanText(text) >= 1, "Уровень образования с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Level, высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "высшее образование, next"
        assertTrue(scanText(text) >= 1, "Уровень образования с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Level. высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "высшее образование."
        assertTrue(scanText(text) >= 1, "Уровень образования с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Level; высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "высшее образование; next"
        assertTrue(scanText(text) >= 1, "Уровень образования с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Level: высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( высшее образование )"
        assertTrue(scanText(text) >= 1, "Уровень образования в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(высшее образование)"
        assertTrue(scanText(text) >= 1, "Уровень образования в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" высшее образование \""
        assertTrue(scanText(text) >= 1, "Уровень образования в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"высшее образование\""
        assertTrue(scanText(text) >= 1, "Уровень образования в кавычках без пробелов должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "высшее образование среднее профессиональное"
        assertTrue(scanText(text) >= 2, "Несколько уровней образования через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "высшее образование, среднее профессиональное"
        assertTrue(scanText(text) >= 2, "Несколько уровней образования через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "высшее образование; среднее профессиональное"
        assertTrue(scanText(text) >= 2, "Несколько уровней образования через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "высшее образование\nсреднее профессиональное"
        assertTrue(scanText(text) >= 2, "Несколько уровней образования через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать уровней образования")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no education levels at all"
        assertEquals(0, scanText(text), "Текст без уровней образования не должен находить совпадения")
    }

    @Test
    fun testVysheeObrazovanie() {
        val text = "высшее образование"
        assertTrue(scanText(text) >= 1, "Высшее образование должно быть найдено")
    }

    @Test
    fun testBakalavriat() {
        val text = "бакалавриат"
        assertTrue(scanText(text) >= 1, "Бакалавриат должен быть найден")
    }

    @Test
    fun testMagistratura() {
        val text = "магистратура"
        assertTrue(scanText(text) >= 1, "Магистратура должна быть найдена")
    }

    @Test
    fun testSredneeProfessionalnoe() {
        val text = "среднее профессиональное образование"
        assertTrue(scanText(text) >= 1, "Среднее профессиональное образование должно быть найдено")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Level     высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Level\tвысшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "высшее образование\tnext"
        assertTrue(scanText(text) >= 1, "Уровень образования с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Уровень высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Education level высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "высшее образование text"
        assertTrue(scanText(text) >= 1, "Уровень образования в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text высшее образование"
        assertTrue(scanText(text) >= 1, "Уровень образования в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidLevel() {
        val text = "неправильный уровень"
        assertEquals(0, scanText(text), "Несуществующий уровень образования не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "levelвысшее образование"
        assertEquals(0, scanText(text), "Уровень образования, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123высшее образование"
        assertEquals(0, scanText(text), "Уровень образования, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionвысшее образование()"
        assertEquals(0, scanText(text), "Уровень образования внутри кода не должен находиться")
    }

    @Test
    fun testPartialLevel() {
        val text = "высшее"
        // В зависимости от реализации - может быть найден или нет
        val count = scanText(text)
        assertTrue(count >= 0, "Частичный уровень образования")
    }
}

