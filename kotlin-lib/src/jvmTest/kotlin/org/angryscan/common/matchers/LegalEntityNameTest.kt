package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера LegalEntityName
 */
internal class LegalEntityNameTest : MatcherTestBase(LegalEntityName) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Company name: ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The company ООО Рога и Копыта is valid"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "ООО Рога и Копыта\nSecond line"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with ООО Рога и Копыта company"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "ООО Рога и Копыта\n"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nООО Рога и Копыта\r\n"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "ООО Рога и Копыта\n\n"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcООО Рога и Копытаdef"
        assertEquals(0, scanText(text), "Наименование ЮЛ внутри буквенной последовательности не должно находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Company ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "ООО Рога и Копыта is valid"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Company, ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "ООО Рога и Копыта, next"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Company. ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "ООО Рога и Копыта."
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Company; ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "ООО Рога и Копыта; next"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Company: ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с двоеточием перед должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( ООО Рога и Копыта )"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(ООО Рога и Копыта)"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" ООО Рога и Копыта \""
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"ООО Рога и Копыта\""
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в кавычках без пробелов должно быть найдено")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ ООО Рога и Копыта ]"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в квадратных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ ООО Рога и Копыта }"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в фигурных скобках с пробелами должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSemicolons() {
        val text = "ООО Рога и Копыта; АО Другая Компания"
        assertTrue(scanText(text) >= 2, "Несколько наименований ЮЛ через точку с запятой должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать наименований ЮЛ")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no legal entity names at all"
        assertEquals(0, scanText(text), "Текст без наименований ЮЛ не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithAOFormat() {
        val text = "АО Компания"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с АО должно быть найдено")
    }

    @Test
    fun testWithPAOFormat() {
        val text = "ПАО Компания"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с ПАО должно быть найдено")
    }

    @Test
    fun testWithIPFormat() {
        val text = "ИП Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с ИП должно быть найдено")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Company    ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Company\tООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "ООО Рога и Копыта\tnext"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Компания ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Company ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "ООО Рога и Копыта text"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в конце текста должно быть найдено")
    }

    @Test
    fun testWithNaimenovanieKeyword() {
        val text = "наименование ЮЛ: ООО Рога и Копыта"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ с ключевым словом должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "ОО"
        assertEquals(0, scanText(text), "Слишком короткое наименование ЮЛ не должно находиться")
    }

    @Test
    fun testWithOnlyNumbers() {
        val text = "123456"
        assertEquals(0, scanText(text), "Только цифры не должны находиться как наименование ЮЛ")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "companyООО Рога и Копыта"
        assertEquals(0, scanText(text), "Наименование ЮЛ, прилипшее к буквам, не должно находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123ООО Рога и Копыта"
        assertEquals(0, scanText(text), "Наименование ЮЛ, прилипшее к цифрам, не должно находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionООО Рога и Копыта()"
        assertEquals(0, scanText(text), "Наименование ЮЛ внутри кода не должно находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "ОООРогаиКопыта"
        assertEquals(0, scanText(text), "Наименование ЮЛ без пробелов не должно находиться")
    }
}

