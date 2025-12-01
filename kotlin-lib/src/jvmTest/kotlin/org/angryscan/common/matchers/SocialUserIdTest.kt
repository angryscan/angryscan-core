package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера SocialUserId
 */
internal class SocialUserIdTest : MatcherTestBase(SocialUserId) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "@username"
        assertTrue(scanText(text) >= 1, "ID соцсети в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Social ID: @username"
        assertTrue(scanText(text) >= 1, "ID соцсети в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The ID @username is valid"
        assertTrue(scanText(text) >= 1, "ID соцсети в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "@username\nSecond line"
        assertTrue(scanText(text) >= 1, "ID соцсети в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n@username"
        assertTrue(scanText(text) >= 1, "ID соцсети в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with @username ID"
        assertTrue(scanText(text) >= 1, "ID соцсети в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n@username"
        assertTrue(scanText(text) >= 1, "ID соцсети после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "@username\n"
        assertTrue(scanText(text) >= 1, "ID соцсети перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n@username\r\n"
        assertTrue(scanText(text) >= 1, "ID соцсети с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n@username"
        assertTrue(scanText(text) >= 1, "ID соцсети после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "@username\n\n"
        assertTrue(scanText(text) >= 1, "ID соцсети перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc@usernamedef"
        assertEquals(0, scanText(text), "ID соцсети внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123@username456"
        assertEquals(0, scanText(text), "ID соцсети внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "ID @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "@username is valid"
        assertTrue(scanText(text) >= 1, "ID соцсети с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "ID, @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "@username, next"
        assertTrue(scanText(text) >= 1, "ID соцсети с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "ID. @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с точкой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "ID; @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "@username; next"
        assertTrue(scanText(text) >= 1, "ID соцсети с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "ID: @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( @username )"
        assertTrue(scanText(text) >= 1, "ID соцсети в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(@username)"
        assertTrue(scanText(text) >= 1, "ID соцсети в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" @username \""
        assertTrue(scanText(text) >= 1, "ID соцсети в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"@username\""
        assertTrue(scanText(text) >= 1, "ID соцсети в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ @username ]"
        assertTrue(scanText(text) >= 1, "ID соцсети в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ @username }"
        assertTrue(scanText(text) >= 1, "ID соцсети в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithAtSignAsPartOfFormat() {
        val text = "@username"
        assertTrue(scanText(text) >= 1, "ID соцсети со знаком @ как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "@username @user2"
        assertTrue(scanText(text) >= 2, "Несколько ID соцсетей через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "@username, @user2"
        assertTrue(scanText(text) >= 2, "Несколько ID соцсетей через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "@username; @user2"
        assertTrue(scanText(text) >= 2, "Несколько ID соцсетей через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "@username\n@user2"
        assertTrue(scanText(text) >= 2, "Несколько ID соцсетей через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ID соцсетей")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no social user IDs at all"
        assertEquals(0, scanText(text), "Текст без ID соцсетей не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "@abc"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testMaximalFormat() {
        val text = "@abcdefghijklmnopqrstuvwxyz123456"
        assertTrue(scanText(text) >= 1, "Максимальный формат должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "@ab"
        assertEquals(0, scanText(text), "Слишком короткий ID не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "@abcdefghijklmnopqrstuvwxyz1234567"
        assertEquals(0, scanText(text), "Слишком длинный ID не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "ID     @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "ID\t@username"
        assertTrue(scanText(text) >= 1, "ID соцсети с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "@username\tnext"
        assertTrue(scanText(text) >= 1, "ID соцсети с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Профиль @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Social ID @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "@username"
        assertTrue(scanText(text) >= 1, "ID соцсети отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "@username text"
        assertTrue(scanText(text) >= 1, "ID соцсети в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text @username"
        assertTrue(scanText(text) >= 1, "ID соцсети в конце текста должен быть найден")
    }

    @Test
    fun testWithVKKeyword() {
        val text = "ID в VK: @username"
        assertTrue(scanText(text) >= 1, "ID с ключевым словом должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testWithoutAtSign() {
        val text = "username"
        assertEquals(0, scanText(text), "ID соцсети без символа @ не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "id@username"
        assertEquals(0, scanText(text), "ID соцсети, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123@username"
        assertEquals(0, scanText(text), "ID соцсети, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function@username()"
        assertEquals(0, scanText(text), "ID соцсети внутри кода не должен находиться")
    }

    @Test
    fun testOnlyAtSign() {
        val text = "@"
        assertEquals(0, scanText(text), "Только символ @ не должен находиться")
    }
}

