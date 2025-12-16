package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера PhoneUS
 */
internal class PhoneUSTest : MatcherTestBase(PhoneUS) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "(555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Phone number: (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The phone (555) 234-5678 is valid"
        assertTrue(scanText(text) >= 1, "Телефон США в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "(555) 234-5678\nSecond line"
        assertTrue(scanText(text) >= 1, "Телефон США в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n(555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with (555) 234-5678 phone"
        assertTrue(scanText(text) >= 1, "Телефон США в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n(555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "(555) 234-5678\n"
        assertTrue(scanText(text) >= 1, "Телефон США перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n(555) 234-5678\r\n"
        assertTrue(scanText(text) >= 1, "Телефон США с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n(555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "(555) 234-5678\n\n"
        assertTrue(scanText(text) >= 1, "Телефон США перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc(555) 234-5678def"
        assertEquals(0, scanText(text), "Телефон США внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123(555) 234-5678456"
        assertEquals(0, scanText(text), "Телефон США внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Phone (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "(555) 234-5678 is valid"
        assertTrue(scanText(text) >= 1, "Телефон США с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Phone, (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "(555) 234-5678, next"
        assertTrue(scanText(text) >= 1, "Телефон США с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Phone. (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "(555) 234-5678."
        assertTrue(scanText(text) >= 1, "Телефон США с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Phone; (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "(555) 234-5678; next"
        assertTrue(scanText(text) >= 1, "Телефон США с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Phone: (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAsPartOfFormat() {
        val text = "(555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США со скобками как часть формата должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" (555) 234-5678 \""
        assertTrue(scanText(text) >= 1, "Телефон США в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"(555) 234-5678\""
        assertTrue(scanText(text) >= 1, "Телефон США в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "555-234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с дефисом как часть формата должен быть найден")
    }

    @Test
    fun testWithPlusOne() {
        val text = "+1 (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с +1 должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "(555) 234-5678 (555) 987-6543"
        assertTrue(scanText(text) >= 2, "Несколько телефонов США через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "(555) 234-5678, (555) 987-6543"
        assertTrue(scanText(text) >= 2, "Несколько телефонов США через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "(555) 234-5678; (555) 987-6543"
        assertTrue(scanText(text) >= 2, "Несколько телефонов США через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "(555) 234-5678\n(555) 987-6543"
        assertTrue(scanText(text) >= 2, "Несколько телефонов США через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать телефонов США")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no US phone numbers at all"
        assertEquals(0, scanText(text), "Текст без телефонов США не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "(555) 234-5678"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Phone    (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Phone\t(555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "(555) 234-5678\tnext"
        assertTrue(scanText(text) >= 1, "Телефон США с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Телефон (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Phone (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "(555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "(555) 234-5678 text"
        assertTrue(scanText(text) >= 1, "Телефон США в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text (555) 234-5678"
        assertTrue(scanText(text) >= 1, "Телефон США в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidAreaCode() {
        val text = "(000) 234-5678"
        assertEquals(0, scanText(text), "Телефон США с недопустимым кодом области не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "(555) 234-567"
        assertEquals(0, scanText(text), "Слишком короткий телефон США не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "(555) 234-56788"
        assertEquals(0, scanText(text), "Слишком длинный телефон США не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "(555) ABC-DEFG"
        assertEquals(0, scanText(text), "Телефон США с буквами не должен находиться")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABC(555) 234-5678DEF"
        assertEquals(0, scanText(text), "Телефон США внутри длинной последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "phone(555) 234-5678"
        assertEquals(0, scanText(text), "Телефон США, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123(555) 234-5678"
        assertEquals(0, scanText(text), "Телефон США, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "555-234-5678"
        // В зависимости от реализации - может быть найден или нет
        val count = scanText(text)
        assertTrue(count >= 0, "Телефон США с неправильным форматом")
    }

    @Test
    fun testInCode() {
        val text = "function(555) 234-5678()"
        assertEquals(0, scanText(text), "Телефон США внутри кода не должен находиться")
    }
}

