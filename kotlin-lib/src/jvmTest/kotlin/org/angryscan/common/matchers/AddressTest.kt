package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера Address
 */
internal class AddressTest : MatcherTestBase(Address) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Address: г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The address г. Москва, ул. Ленина, д. 10 is valid"
        assertTrue(scanText(text) >= 1, "Адрес в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "г. Москва, ул. Ленина, д. 10\nSecond line"
        assertTrue(scanText(text) >= 1, "Адрес в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nг. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with г. Москва, ул. Ленина, д. 10 address"
        assertTrue(scanText(text) >= 1, "Адрес в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nг. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "г. Москва, ул. Ленина, д. 10\n"
        assertTrue(scanText(text) >= 1, "Адрес перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nг. Москва, ул. Ленина, д. 10\r\n"
        assertTrue(scanText(text) >= 1, "Адрес с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nг. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "г. Москва, ул. Ленина, д. 10\n\n"
        assertTrue(scanText(text) >= 1, "Адрес перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testWithSpaceBefore() {
        val text = "Address г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "г. Москва, ул. Ленина, д. 10 is valid"
        assertTrue(scanText(text) >= 1, "Адрес с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Address, г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "г. Москва, ул. Ленина, д. 10, next"
        assertTrue(scanText(text) >= 1, "Адрес с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Address. г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "г. Москва, ул. Ленина, д. 10."
        assertTrue(scanText(text) >= 1, "Адрес с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Address; г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "г. Москва, ул. Ленина, д. 10; next"
        assertTrue(scanText(text) >= 1, "Адрес с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Address: г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "г. Москва, ул. Ленина, д. 10!"
        assertTrue(scanText(text) >= 1, "Адрес с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "г. Москва, ул. Ленина, д. 10?"
        assertTrue(scanText(text) >= 1, "Адрес с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( г. Москва, ул. Ленина, д. 10 )"
        assertTrue(scanText(text) >= 1, "Адрес в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(г. Москва, ул. Ленина, д. 10)"
        assertTrue(scanText(text) >= 1, "Адрес в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" г. Москва, ул. Ленина, д. 10 \""
        assertTrue(scanText(text) >= 1, "Адрес в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"г. Москва, ул. Ленина, д. 10\""
        assertTrue(scanText(text) >= 1, "Адрес в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "address = г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "address # г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ г. Москва, ул. Ленина, д. 10 ]"
        assertTrue(scanText(text) >= 1, "Адрес в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ г. Москва, ул. Ленина, д. 10 }"
        assertTrue(scanText(text) >= 1, "Адрес в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithDotAsPartOfFormat() {
        val text = "г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с точкой как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "г. Москва, ул. Ленина, д. 10 г. СПб, ул. Невский, д. 20"
        assertTrue(scanText(text) >= 2, "Несколько адресов через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "г. Москва, ул. Ленина, д. 10, г. СПб, ул. Невский, д. 20"
        assertTrue(scanText(text) >= 2, "Несколько адресов через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "г. Москва, ул. Ленина, д. 10; г. СПб, ул. Невский, д. 20"
        assertTrue(scanText(text) >= 2, "Несколько адресов через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "г. Москва, ул. Ленина, д. 10\nг. СПб, ул. Невский, д. 20"
        assertTrue(scanText(text) >= 2, "Несколько адресов через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать адресов")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no addresses at all"
        assertEquals(0, scanText(text), "Текст без адресов не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Address    г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Address\tг. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "г. Москва, ул. Ленина, д. 10\tnext"
        assertTrue(scanText(text) >= 1, "Адрес с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Адрес г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Address г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "г. Москва, ул. Ленина, д. 10 text"
        assertTrue(scanText(text) >= 1, "Адрес в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес в конце текста должен быть найден")
    }

    @Test
    fun testWithOblFormat() {
        val text = "обл. Московская, г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с обл. должен быть найден")
    }

    @Test
    fun testWithRaionFormat() {
        val text = "р-н Центральный, г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text) >= 1, "Адрес с р-н должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "г. М"
        assertEquals(0, scanText(text), "Слишком короткий адрес не должен находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "Москва@Ленина@10"
        assertEquals(0, scanText(text), "Адрес с неправильными разделителями не должен находиться")
    }

    @Test
    fun testWithOnlyNumbers() {
        val text = "123 456 789"
        assertEquals(0, scanText(text), "Только цифры не должны находиться как адрес")
    }
}

