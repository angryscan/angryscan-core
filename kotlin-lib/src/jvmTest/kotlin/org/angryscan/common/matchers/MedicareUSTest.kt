package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера MedicareUS
 */
internal class MedicareUSTest : MatcherTestBase(MedicareUS) {

    // Валидный Medicare MBI для тестов (11 символов, правильный формат)
    private val validMBI = "1AC2CD3EF45" // Пример валидного MBI (B заменен на C, т.к. B входит в исключенные буквы)

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Medicare number: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The medicare 1AC2CD3EF45 is valid"
        assertTrue(scanText(text) >= 1, "Medicare в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "medicare: 1AC2CD3EF45\nSecond line"
        assertTrue(scanText(text) >= 1, "Medicare в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nmedicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with medicare: 1AC2CD3EF45 number"
        assertTrue(scanText(text) >= 1, "Medicare в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nmedicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "medicare: 1AC2CD3EF45\n"
        assertTrue(scanText(text) >= 1, "Medicare перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nmedicare: 1AC2CD3EF45\r\n"
        assertTrue(scanText(text) >= 1, "Medicare с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nmedicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "medicare: 1AC2CD3EF45\n\n"
        assertTrue(scanText(text) >= 1, "Medicare перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcmedicare: 1AC2CD3EF45def"
        assertEquals(0, scanText(text), "Medicare внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123medicare: 1AC2CD3EF45456"
        assertEquals(0, scanText(text), "Medicare внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Number medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "medicare: 1AC2CD3EF45 is valid"
        assertTrue(scanText(text) >= 1, "Medicare с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Number, medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "medicare: 1AC2CD3EF45, next"
        assertTrue(scanText(text) >= 1, "Medicare с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Number. medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "medicare: 1AC2CD3EF45."
        assertTrue(scanText(text) >= 1, "Medicare с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Number; medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "medicare: 1AC2CD3EF45; next"
        assertTrue(scanText(text) >= 1, "Medicare с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Number: medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( medicare: 1AC2CD3EF45 )"
        assertTrue(scanText(text) >= 1, "Medicare в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(medicare: 1AC2CD3EF45)"
        assertTrue(scanText(text) >= 1, "Medicare в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" medicare: 1AC2CD3EF45 \""
        assertTrue(scanText(text) >= 1, "Medicare в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"medicare: 1AC2CD3EF45\""
        assertTrue(scanText(text) >= 1, "Medicare в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ medicare: 1AC2CD3EF45 ]"
        assertTrue(scanText(text) >= 1, "Medicare в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ medicare: 1AC2CD3EF45 }"
        assertTrue(scanText(text) >= 1, "Medicare в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithMBIKeyword() {
        val text = "MBI: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с ключевым словом MBI должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "medicare: 1AC2CD3EF45 medicare: 2CE3FG4HJ56"
        assertTrue(scanText(text) >= 2, "Несколько Medicare через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "medicare: 1AC2CD3EF45, medicare: 2CE3FG4HJ56"
        assertTrue(scanText(text) >= 2, "Несколько Medicare через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "medicare: 1AC2CD3EF45; medicare: 2CE3FG4HJ56"
        assertTrue(scanText(text) >= 2, "Несколько Medicare через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "medicare: 1AC2CD3EF45\nmedicare: 2CE3FG4HJ56"
        assertTrue(scanText(text) >= 2, "Несколько Medicare через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать Medicare")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no Medicare numbers at all"
        assertEquals(0, scanText(text), "Текст без Medicare не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testStandaloneMBI() {
        val text = "1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "MBI отдельной строкой должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Number    medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Number\tmedicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "medicare: 1AC2CD3EF45\tnext"
        assertTrue(scanText(text) >= 1, "Medicare с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Номер medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Number medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "medicare: 1AC2CD3EF45 text"
        assertTrue(scanText(text) >= 1, "Medicare в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text medicare: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare в конце текста должен быть найден")
    }

    @Test
    fun testWithHealthInsuranceKeyword() {
        val text = "health insurance: 1AC2CD3EF45"
        assertTrue(scanText(text) >= 1, "Medicare с ключевым словом 'health insurance' должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidChecksum() {
        val text = "1AB2CD3EF4X"
        assertEquals(0, scanText(text), "Medicare с неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "1AB2CD3EF4"
        assertEquals(0, scanText(text), "Слишком короткий Medicare не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "1AC2CD3EF456"
        assertEquals(0, scanText(text), "Слишком длинный Medicare не должен находиться")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "1AB2CD3EF4I"
        assertEquals(0, scanText(text), "Medicare с недопустимыми символами не должен находиться")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABC1AC2CD3EF45DEF"
        assertEquals(0, scanText(text), "Medicare внутри длинной последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "medicare1AC2CD3EF45"
        assertEquals(0, scanText(text), "Medicare, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "1231AC2CD3EF45"
        assertEquals(0, scanText(text), "Medicare, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function1AC2CD3EF45()"
        assertEquals(0, scanText(text), "Medicare внутри кода не должен находиться")
    }

    @Test
    fun testWithSpaces() {
        val text = "1AB 2CD 3EF 45"
        assertEquals(0, scanText(text), "Medicare с пробелами не должен находиться")
    }
}

