package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера AddressUS
 */
internal class AddressUSTest : MatcherTestBase(AddressUS) {

    // Валидные примеры адресов США
    private val validAddress1 = "123 Main Street CA 90210"
    private val validAddress2 = "4567 Oak Avenue NY 10001"
    private val validAddress3 = "890 Washington Boulevard TX 77001"

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Address: 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The address 123 Main Street CA 90210 is valid"
        assertTrue(scanText(text) >= 1, "Адрес США в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "123 Main Street CA 90210\nSecond line"
        assertTrue(scanText(text) >= 1, "Адрес США в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 123 Main Street CA 90210 address"
        assertTrue(scanText(text) >= 1, "Адрес США в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "123 Main Street CA 90210\n"
        assertTrue(scanText(text) >= 1, "Адрес США перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n123 Main Street CA 90210\r\n"
        assertTrue(scanText(text) >= 1, "Адрес США с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "123 Main Street CA 90210\n\n"
        assertTrue(scanText(text) >= 1, "Адрес США перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testWithSpaceBefore() {
        val text = "Address 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "123 Main Street CA 90210 is valid"
        assertTrue(scanText(text) >= 1, "Адрес США с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Address, 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "123 Main Street CA 90210, next"
        assertTrue(scanText(text) >= 1, "Адрес США с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Address. 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "123 Main Street CA 90210."
        assertTrue(scanText(text) >= 1, "Адрес США с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Address; 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "123 Main Street CA 90210; next"
        assertTrue(scanText(text) >= 1, "Адрес США с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Address: 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "123 Main Street CA 90210!"
        assertTrue(scanText(text) >= 1, "Адрес США с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "123 Main Street CA 90210?"
        assertTrue(scanText(text) >= 1, "Адрес США с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 123 Main Street CA 90210 )"
        assertTrue(scanText(text) >= 1, "Адрес США в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(123 Main Street CA 90210)"
        assertTrue(scanText(text) >= 1, "Адрес США в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 123 Main Street CA 90210 \""
        assertTrue(scanText(text) >= 1, "Адрес США в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"123 Main Street CA 90210\""
        assertTrue(scanText(text) >= 1, "Адрес США в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "address = 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "address # 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 123 Main Street CA 90210 ]"
        assertTrue(scanText(text) >= 1, "Адрес США в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 123 Main Street CA 90210 }"
        assertTrue(scanText(text) >= 1, "Адрес США в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "123 Main Street CA 90210 4567 Oak Avenue NY 10001"
        assertTrue(scanText(text) >= 2, "Несколько адресов США через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "123 Main Street CA 90210, 4567 Oak Avenue NY 10001"
        assertTrue(scanText(text) >= 2, "Несколько адресов США через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "123 Main Street CA 90210; 4567 Oak Avenue NY 10001"
        assertTrue(scanText(text) >= 2, "Несколько адресов США через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "123 Main Street CA 90210\n4567 Oak Avenue NY 10001"
        assertTrue(scanText(text) >= 2, "Несколько адресов США через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать адресов США")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no US addresses at all"
        assertEquals(0, scanText(text), "Текст без адресов США не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "1 Main St CA 90210"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Address    123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Address\t123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "123 Main Street CA 90210\tnext"
        assertTrue(scanText(text) >= 1, "Адрес США с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Адрес 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Address 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "123 Main Street CA 90210 text"
        assertTrue(scanText(text) >= 1, "Адрес США в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес США в конце текста должен быть найден")
    }

    // ========== 5. Тестирование различных штатов ==========

    @Test
    fun testWithCalifornia() {
        val text = "123 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес с Калифорнией должен быть найден")
    }

    @Test
    fun testWithNewYork() {
        val text = "4567 Oak Avenue NY 10001"
        assertTrue(scanText(text) >= 1, "Адрес с Нью-Йорком должен быть найден")
    }

    @Test
    fun testWithTexas() {
        val text = "890 Washington Boulevard TX 77001"
        assertTrue(scanText(text) >= 1, "Адрес с Техасом должен быть найден")
    }

    @Test
    fun testWithFlorida() {
        val text = "100 Beach Road FL 33101"
        assertTrue(scanText(text) >= 1, "Адрес с Флоридой должен быть найден")
    }

    @Test
    fun testWithDistrictOfColumbia() {
        val text = "200 Pennsylvania Avenue DC 20001"
        assertTrue(scanText(text) >= 1, "Адрес с округом Колумбия должен быть найден")
    }

    // ========== 6. Различные форматы адресов ==========

    @Test
    fun testWithAvenue() {
        val text = "123 Park Avenue NY 10001"
        assertTrue(scanText(text) >= 1, "Адрес с Avenue должен быть найден")
    }

    @Test
    fun testWithStreet() {
        val text = "456 Oak Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес с Street должен быть найден")
    }

    @Test
    fun testWithRoad() {
        val text = "789 Main Road TX 77001"
        assertTrue(scanText(text) >= 1, "Адрес с Road должен быть найден")
    }

    @Test
    fun testWithBoulevard() {
        val text = "321 Sunset Boulevard CA 90028"
        assertTrue(scanText(text) >= 1, "Адрес с Boulevard должен быть найден")
    }

    @Test
    fun testWithDrive() {
        val text = "654 Maple Drive FL 33101"
        assertTrue(scanText(text) >= 1, "Адрес с Drive должен быть найден")
    }

    @Test
    fun testWithLane() {
        val text = "987 Elm Lane NY 10001"
        assertTrue(scanText(text) >= 1, "Адрес с Lane должен быть найден")
    }

    @Test
    fun testWithApartment() {
        val text = "123 Main Street Apt 4B CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес с квартирой должен быть найден")
    }

    @Test
    fun testWithSuite() {
        val text = "456 Business Park Suite 200 TX 77001"
        assertTrue(scanText(text) >= 1, "Адрес с Suite должен быть найден")
    }

    @Test
    fun testWithNumberSign() {
        val text = "789 Main Street #5 NY 10001"
        assertTrue(scanText(text) >= 1, "Адрес с # должен быть найден")
    }

    // ========== 7. Различные длины номеров домов ==========

    @Test
    fun testWithSingleDigitHouseNumber() {
        val text = "1 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес с однозначным номером дома должен быть найден")
    }

    @Test
    fun testWithEightDigitHouseNumber() {
        val text = "12345678 Main Street CA 90210"
        assertTrue(scanText(text) >= 1, "Адрес с восьмизначным номером дома должен быть найден")
    }

    // ========== 8. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "1 St CA 90210"
        assertEquals(0, scanText(text), "Слишком короткий адрес США не должен находиться")
    }

    @Test
    fun testInvalidStateCode() {
        val text = "123 Main Street XX 90210"
        assertEquals(0, scanText(text), "Адрес США с неверным кодом штата не должен находиться")
    }

    @Test
    fun testInvalidZipCode() {
        val text = "123 Main Street CA 123"
        assertEquals(0, scanText(text), "Адрес США с неверным ZIP кодом (менее 5 цифр) не должен находиться")
    }

    @Test
    fun testWithOnlyNumbers() {
        val text = "123 456 789 90210"
        assertEquals(0, scanText(text), "Только цифры не должны находиться как адрес США")
    }

    @Test
    fun testWithoutStateCode() {
        val text = "123 Main Street 90210"
        assertEquals(0, scanText(text), "Адрес США без кода штата не должен находиться")
    }

    @Test
    fun testWithoutZipCode() {
        val text = "123 Main Street CA"
        assertEquals(0, scanText(text), "Адрес США без ZIP кода не должен находиться")
    }

    @Test
    fun testWithLowerCaseState() {
        val text = "123 Main Street ca 90210"
        assertTrue(scanText(text) >= 1, "Адрес США со строчными буквами кода штата должен быть найден (CASELESS)")
    }

    @Test
    fun testWithMixedCaseState() {
        val text = "123 Main Street Ca 90210"
        assertTrue(scanText(text) >= 1, "Адрес США со смешанным регистром кода штата должен быть найден")
    }
}
