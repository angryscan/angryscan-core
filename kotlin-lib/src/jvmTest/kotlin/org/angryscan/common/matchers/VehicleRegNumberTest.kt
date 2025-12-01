package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера VehicleRegNumber
 */
internal class VehicleRegNumberTest : MatcherTestBase(VehicleRegNumber) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Vehicle number: А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The number А123БВ 77 is valid"
        assertTrue(scanText(text) >= 1, "Госномер в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "А123БВ 77\nSecond line"
        assertTrue(scanText(text) >= 1, "Госномер в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nА123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with А123БВ 77 number"
        assertTrue(scanText(text) >= 1, "Госномер в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nА123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "А123БВ 77\n"
        assertTrue(scanText(text) >= 1, "Госномер перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nА123БВ 77\r\n"
        assertTrue(scanText(text) >= 1, "Госномер с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nА123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "А123БВ 77\n\n"
        assertTrue(scanText(text) >= 1, "Госномер перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcА123БВ 77def"
        assertEquals(0, scanText(text), "Госномер внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123А123БВ 77456"
        assertEquals(0, scanText(text), "Госномер внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Number А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "А123БВ 77 is valid"
        assertTrue(scanText(text) >= 1, "Госномер с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Number, А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "А123БВ 77, next"
        assertTrue(scanText(text) >= 1, "Госномер с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Number. А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "А123БВ 77."
        assertTrue(scanText(text) >= 1, "Госномер с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Number; А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "А123БВ 77; next"
        assertTrue(scanText(text) >= 1, "Госномер с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Number: А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( А123БВ 77 )"
        assertTrue(scanText(text) >= 1, "Госномер в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(А123БВ 77)"
        assertTrue(scanText(text) >= 1, "Госномер в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" А123БВ 77 \""
        assertTrue(scanText(text) >= 1, "Госномер в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"А123БВ 77\""
        assertTrue(scanText(text) >= 1, "Госномер в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ А123БВ 77 ]"
        assertTrue(scanText(text) >= 1, "Госномер в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ А123БВ 77 }"
        assertTrue(scanText(text) >= 1, "Госномер в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "А123БВ 77 В456ГД 99"
        assertTrue(scanText(text) >= 2, "Несколько госномеров через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "А123БВ 77, В456ГД 99"
        assertTrue(scanText(text) >= 2, "Несколько госномеров через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "А123БВ 77; В456ГД 99"
        assertTrue(scanText(text) >= 2, "Несколько госномеров через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "А123БВ 77\nВ456ГД 99"
        assertTrue(scanText(text) >= 2, "Несколько госномеров через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать госномеров")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no vehicle registration numbers at all"
        assertEquals(0, scanText(text), "Текст без госномеров не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "А123БВ 77"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithLatinLetters() {
        val text = "A123BC 77"
        assertTrue(scanText(text) >= 1, "Госномер с латинскими буквами должен быть найден")
    }

    @Test
    fun testWithNumericFormat() {
        val text = "1234АБ 77"
        assertTrue(scanText(text) >= 1, "Госномер с числовым форматом должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Number    А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Number\tА123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "А123БВ 77\tnext"
        assertTrue(scanText(text) >= 1, "Госномер с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Госномер А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Vehicle number А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "А123БВ 77 text"
        assertTrue(scanText(text) >= 1, "Госномер в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер в конце текста должен быть найден")
    }

    @Test
    fun testWithGosnomerKeyword() {
        val text = "госномер: А123БВ 77"
        assertTrue(scanText(text) >= 1, "Госномер с ключевым словом должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidFormat() {
        val text = "А123БВ77"
        assertEquals(0, scanText(text), "Госномер без пробела не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "А12БВ 77"
        assertEquals(0, scanText(text), "Слишком короткий госномер не должен находиться")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "А123БВ 7A"
        assertEquals(0, scanText(text), "Госномер с недопустимыми символами не должен находиться")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABCА123БВ 77DEF"
        assertEquals(0, scanText(text), "Госномер внутри длинной последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "numberА123БВ 77"
        assertEquals(0, scanText(text), "Госномер, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123А123БВ 77"
        assertEquals(0, scanText(text), "Госномер, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionА123БВ 77()"
        assertEquals(0, scanText(text), "Госномер внутри кода не должен находиться")
    }
}

