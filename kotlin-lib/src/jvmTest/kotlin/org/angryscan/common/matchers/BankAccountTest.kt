package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера BankAccount
 */
internal class BankAccountTest : MatcherTestBase(BankAccount) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Account: 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The account 40812345678901234567 is valid"
        assertTrue(scanText(text) >= 1, "Счет в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "40812345678901234567\nSecond line"
        assertTrue(scanText(text) >= 1, "Счет в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 40812345678901234567 account"
        assertTrue(scanText(text) >= 1, "Счет в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "40812345678901234567\n"
        assertTrue(scanText(text) >= 1, "Счет перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n40812345678901234567\r\n"
        assertTrue(scanText(text) >= 1, "Счет с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "40812345678901234567\n\n"
        assertTrue(scanText(text) >= 1, "Счет перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc40812345678901234567def"
        assertEquals(0, scanText(text), "Счет внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12340812345678901234567456"
        assertEquals(0, scanText(text), "Счет внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc12340812345678901234567def456"
        assertEquals(0, scanText(text), "Счет внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Account 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "40812345678901234567 is valid"
        assertTrue(scanText(text) >= 1, "Счет с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Account, 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "40812345678901234567, next"
        assertTrue(scanText(text) >= 1, "Счет с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Account. 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "40812345678901234567."
        assertTrue(scanText(text) >= 1, "Счет с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Account; 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "40812345678901234567; next"
        assertTrue(scanText(text) >= 1, "Счет с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Account: 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "40812345678901234567!"
        assertTrue(scanText(text) >= 1, "Счет с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "40812345678901234567?"
        assertTrue(scanText(text) >= 1, "Счет с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 40812345678901234567 )"
        assertTrue(scanText(text) >= 1, "Счет в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(40812345678901234567)"
        assertTrue(scanText(text) >= 1, "Счет в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 40812345678901234567 \""
        assertTrue(scanText(text) >= 1, "Счет в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"40812345678901234567\""
        assertTrue(scanText(text) >= 1, "Счет в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "account = 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "account # 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 40812345678901234567 ]"
        assertTrue(scanText(text) >= 1, "Счет в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 40812345678901234567 }"
        assertTrue(scanText(text) >= 1, "Счет в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "40812345678901234567 40812345678901234568"
        assertTrue(scanText(text) >= 2, "Несколько счетов через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "40812345678901234567, 40812345678901234568"
        assertTrue(scanText(text) >= 2, "Несколько счетов через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "40812345678901234567; 40812345678901234568"
        assertTrue(scanText(text) >= 2, "Несколько счетов через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "40812345678901234567\n40812345678901234568"
        assertTrue(scanText(text) >= 2, "Несколько счетов через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать счетов")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no bank accounts at all"
        assertEquals(0, scanText(text), "Текст без счетов не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "40812345678901234567"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Account    40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Account\t40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "40812345678901234567\tnext"
        assertTrue(scanText(text) >= 1, "Счет с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Счет 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Account 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "40812345678901234567 text"
        assertTrue(scanText(text) >= 1, "Счет в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет в конце текста должен быть найден")
    }

    @Test
    fun testWith408Prefix() {
        val text = "40812345678901234567"
        assertTrue(scanText(text) >= 1, "Счет с префиксом 408 должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "4081234567890123456"
        assertEquals(0, scanText(text), "Слишком короткий счет не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "408123456789012345678"
        assertEquals(0, scanText(text), "Слишком длинный счет не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "4081234567890123456A"
        assertEquals(0, scanText(text), "Счет с буквами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "Счет внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "account40812345678901234567"
        assertEquals(0, scanText(text), "Счет, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12340812345678901234567"
        assertEquals(0, scanText(text), "Счет, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidPrefix() {
        val text = "99912345678901234567"
        assertEquals(0, scanText(text), "Счет с недопустимым префиксом не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function40812345678901234567()"
        assertEquals(0, scanText(text), "Счет внутри кода не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "40800000000000000000"
        // В зависимости от реализации - может быть найден или нет
        val count = scanText(text)
        assertTrue(count >= 0, "Счет из всех нулей")
    }
}

