package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера Phone
 */
internal class PhoneTest : MatcherTestBase(Phone) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "+79161234567 is a phone"
        assertTrue(scanText(text) >= 1, "Телефон в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Phone number is +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The phone +79161234567 is valid"
        assertTrue(scanText(text) >= 1, "Телефон в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "+79161234567\nSecond line"
        assertTrue(scanText(text) >= 1, "Телефон в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n+79161234567"
        assertTrue(scanText(text) >= 1, "Телефон в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with +79161234567 phone"
        assertTrue(scanText(text) >= 1, "Телефон в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n+79161234567"
        assertTrue(scanText(text) >= 1, "Телефон после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "+79161234567\n"
        assertTrue(scanText(text) >= 1, "Телефон перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n+79161234567\r\n"
        assertTrue(scanText(text) >= 1, "Телефон с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n+79161234567"
        assertTrue(scanText(text) >= 1, "Телефон после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "+79161234567\n\n"
        assertTrue(scanText(text) >= 1, "Телефон перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc+79161234567def"
        assertEquals(0, scanText(text), "Телефон внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123+79161234567456"
        assertEquals(0, scanText(text), "Телефон внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc123+79161234567def456"
        assertEquals(0, scanText(text), "Телефон внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Phone +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "+79161234567 is valid"
        assertTrue(scanText(text) >= 1, "Телефон с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Phone, +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "+79161234567, next"
        assertTrue(scanText(text) >= 1, "Телефон с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Phone. +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "+79161234567."
        assertTrue(scanText(text) >= 1, "Телефон с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Phone; +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "+79161234567; next"
        assertTrue(scanText(text) >= 1, "Телефон с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Phone: +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "+79161234567!"
        assertTrue(scanText(text) >= 1, "Телефон с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "+79161234567?"
        assertTrue(scanText(text) >= 1, "Телефон с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( +79161234567 )"
        assertTrue(scanText(text) >= 1, "Телефон в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(+79161234567)"
        assertTrue(scanText(text) >= 1, "Телефон в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" +79161234567 \""
        assertTrue(scanText(text) >= 1, "Телефон в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"+79161234567\""
        assertTrue(scanText(text) >= 1, "Телефон в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "phone = +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "phone # +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ +79161234567 ]"
        assertTrue(scanText(text) >= 1, "Телефон в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ +79161234567 }"
        assertTrue(scanText(text) >= 1, "Телефон в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithPlusAsPartOfFormat() {
        val text = "+79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с + как часть формата должен быть найден")
    }

    @Test
    fun testWith8InsteadOfPlus() {
        val text = "89161234567"
        assertTrue(scanText(text) >= 1, "Телефон с 8 вместо + должен быть найден")
    }

    @Test
    fun testWithParenthesesInFormat() {
        val text = "+7 (916) 123-45-67"
        assertTrue(scanText(text) >= 1, "Телефон со скобками в формате должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "+79161234567 +79161234568"
        assertTrue(scanText(text) >= 2, "Несколько телефонов через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "+79161234567, +79161234568"
        assertTrue(scanText(text) >= 2, "Несколько телефонов через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "+79161234567; +79161234568"
        assertTrue(scanText(text) >= 2, "Несколько телефонов через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "+79161234567\n+79161234568"
        assertTrue(scanText(text) >= 2, "Несколько телефонов через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать телефонов")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no phone numbers at all"
        assertEquals(0, scanText(text), "Текст без телефонов не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "89161234567"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithSpacesFormat() {
        val text = "+7 916 123 45 67"
        assertTrue(scanText(text) >= 1, "Телефон с пробелами должен быть найден")
    }

    @Test
    fun testWithDashesFormat() {
        val text = "+7-916-123-45-67"
        assertTrue(scanText(text) >= 1, "Телефон с дефисами должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "+7916123456"
        assertEquals(0, scanText(text), "Слишком короткий номер не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "+791612345678"
        assertEquals(0, scanText(text), "Слишком длинный номер не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Phone    +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "+79161234567\tnext"
        assertTrue(scanText(text) >= 1, "Телефон с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Телефон +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Phone +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "+79161234567"
        assertTrue(scanText(text) >= 1, "Телефон отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "+79161234567 text"
        assertTrue(scanText(text) >= 1, "Телефон в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text +79161234567"
        assertTrue(scanText(text) >= 1, "Телефон в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidCountryCode() {
        val text = "+99161234567"
        assertEquals(0, scanText(text), "Телефон с неправильным кодом страны не должен находиться")
    }

    @Test
    fun testTooShortNumber() {
        val text = "+7916123456"
        assertEquals(0, scanText(text), "Слишком короткий номер телефона не должен находиться")
    }

    @Test
    fun testTooLongNumber() {
        val text = "+791612345678"
        assertEquals(0, scanText(text), "Слишком длинный номер телефона не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "+7916123456A"
        assertEquals(0, scanText(text), "Телефон с буквами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "Телефон внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "phone+79161234567"
        assertEquals(0, scanText(text), "Телефон, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123+79161234567"
        assertEquals(0, scanText(text), "Телефон, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInvalidFormatWithWrongSeparators() {
        val text = "+7916@1234@567"
        assertEquals(0, scanText(text), "Телефон с неправильными разделителями не должен находиться")
    }

    @Test
    fun testPartialPhoneNumber() {
        val text = "+7916"
        assertEquals(0, scanText(text), "Частичный номер телефона не должен находиться")
    }

    @Test
    fun testPhoneNumberInCode() {
        val text = "function+79161234567()"
        assertEquals(0, scanText(text), "Телефон внутри кода не должен находиться")
    }

    @Test
    fun testPhoneNumberInEmail() {
        val text = "user+79161234567@example.com"
        assertEquals(0, scanText(text), "Телефон в email не должен находиться")
    }

    @Test
    fun testPhoneNumberWithInvalidChars() {
        val text = "+7916#1234#567"
        assertEquals(0, scanText(text), "Телефон с недопустимыми символами не должен находиться")
    }
}

