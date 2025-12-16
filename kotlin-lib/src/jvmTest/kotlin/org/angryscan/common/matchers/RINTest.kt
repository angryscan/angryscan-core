package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера RIN
 */
internal class RINTest : MatcherTestBase(RIN) {

    // Валидный RIN для тестов (18 символов, правильная контрольная сумма)
    private val validRIN = "11010119900307891X" // Пример валидного RIN

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "RIN number: 11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The RIN 11010119900307891X is valid"
        assertTrue(scanText(text) >= 1, "RIN в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "11010119900307891X\nSecond line"
        assertTrue(scanText(text) >= 1, "RIN в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 11010119900307891X RIN"
        assertTrue(scanText(text) >= 1, "RIN в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "11010119900307891X\n"
        assertTrue(scanText(text) >= 1, "RIN перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n11010119900307891X\r\n"
        assertTrue(scanText(text) >= 1, "RIN с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "11010119900307891X\n\n"
        assertTrue(scanText(text) >= 1, "RIN перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc11010119900307891Xdef"
        assertEquals(0, scanText(text), "RIN внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12311010119900307891X456"
        assertEquals(0, scanText(text), "RIN внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "RIN 11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "11010119900307891X is valid"
        assertTrue(scanText(text) >= 1, "RIN с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "RIN, 11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "11010119900307891X, next"
        assertTrue(scanText(text) >= 1, "RIN с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "RIN. 11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "11010119900307891X."
        assertTrue(scanText(text) >= 1, "RIN с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "RIN; 11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "11010119900307891X; next"
        assertTrue(scanText(text) >= 1, "RIN с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "RIN: 11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 11010119900307891X )"
        assertTrue(scanText(text) >= 1, "RIN в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(11010119900307891X)"
        assertTrue(scanText(text) >= 1, "RIN в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 11010119900307891X \""
        assertTrue(scanText(text) >= 1, "RIN в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"11010119900307891X\""
        assertTrue(scanText(text) >= 1, "RIN в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 11010119900307891X ]"
        assertTrue(scanText(text) >= 1, "RIN в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 11010119900307891X }"
        assertTrue(scanText(text) >= 1, "RIN в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithXAsPartOfFormat() {
        val text = "11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с X как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithCommas() {
        val text = "11010119900307891X, 22020220000408902X"
        assertTrue(scanText(text) >= 2, "Несколько RIN через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "11010119900307891X; 22020220000408902X"
        assertTrue(scanText(text) >= 2, "Несколько RIN через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "11010119900307891X\n22020220000408902X"
        assertTrue(scanText(text) >= 2, "Несколько RIN через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать RIN")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no RIN numbers at all"
        assertEquals(0, scanText(text), "Текст без RIN не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "11010119900307891X"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "11010119900307891"
        assertEquals(0, scanText(text), "Слишком короткий RIN не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "11010119900307891XX"
        assertEquals(0, scanText(text), "Слишком длинный RIN не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "RIN    11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "RIN\t11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "11010119900307891X\tnext"
        assertTrue(scanText(text) >= 1, "RIN с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Идентификатор 11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "RIN number 11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "11010119900307891X text"
        assertTrue(scanText(text) >= 1, "RIN в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN в конце текста должен быть найден")
    }

    @Test
    fun testWithChineseIDKeyword() {
        val text = "китайский идентификационный номер: 11010119900307891X"
        assertTrue(scanText(text) >= 1, "RIN с ключевым словом должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidChecksum() {
        val text = "11010119900307891A"
        assertEquals(0, scanText(text), "RIN с неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "11010119900307891I"
        assertEquals(0, scanText(text), "RIN с недопустимыми символами не должен находиться")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABC11010119900307891XDEF"
        assertEquals(0, scanText(text), "RIN внутри длинной последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "rin11010119900307891X"
        assertEquals(0, scanText(text), "RIN, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12311010119900307891X"
        assertEquals(0, scanText(text), "RIN, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function11010119900307891X()"
        assertEquals(0, scanText(text), "RIN внутри кода не должен находиться")
    }

    @Test
    fun testWithSpaces() {
        val text = "110101 199003 07891X"
        assertEquals(0, scanText(text), "RIN с пробелами не должен находиться")
    }
}

