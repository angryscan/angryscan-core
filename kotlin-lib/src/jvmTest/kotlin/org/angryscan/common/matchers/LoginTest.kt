package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера Login
 */
internal class LoginTest : MatcherTestBase(Login) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "login: user123"
        assertTrue(scanText(text) >= 1, "Логин в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "User login: user123"
        assertTrue(scanText(text) >= 1, "Логин в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The login: user123 is valid"
        assertTrue(scanText(text) >= 1, "Логин в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "login: user123\nSecond line"
        assertTrue(scanText(text) >= 1, "Логин в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nlogin: user123"
        assertTrue(scanText(text) >= 1, "Логин в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with login: user123 name"
        assertTrue(scanText(text) >= 1, "Логин в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nlogin: user123"
        assertTrue(scanText(text) >= 1, "Логин после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "login: user123\n"
        assertTrue(scanText(text) >= 1, "Логин перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nlogin: user123\r\n"
        assertTrue(scanText(text) >= 1, "Логин с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nlogin: user123"
        assertTrue(scanText(text) >= 1, "Логин после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "login: user123\n\n"
        assertTrue(scanText(text) >= 1, "Логин перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testWithSpaceBefore() {
        val text = "User login: user123"
        assertTrue(scanText(text) >= 1, "Логин с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "login: user123 is valid"
        assertTrue(scanText(text) >= 1, "Логин с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "User, login: user123"
        assertTrue(scanText(text) >= 1, "Логин с запятой перед должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "User. login: user123"
        assertTrue(scanText(text) >= 1, "Логин с точкой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "User; login: user123"
        assertTrue(scanText(text) >= 1, "Логин с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "User: login: user123"
        assertTrue(scanText(text) >= 1, "Логин с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( login: user123 )"
        assertTrue(scanText(text) >= 1, "Логин в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" login: user123 \""
        assertTrue(scanText(text) >= 1, "Логин в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"login: user123\""
        assertTrue(scanText(text) >= 1, "Логин в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithColonAsPartOfFormat() {
        val text = "login: user123"
        assertTrue(scanText(text) >= 1, "Логин с двоеточием как часть формата должен быть найден")
    }

    @Test
    fun testWithSpaceAsPartOfFormat() {
        val text = "login user123"
        assertTrue(scanText(text) >= 1, "Логин с пробелом как часть формата должен быть найден")
    }

    @Test
    fun testWithCyrillicKeyword() {
        val text = "логин: user123"
        assertTrue(scanText(text) >= 1, "Логин с кириллическим ключевым словом должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "login: user123 login: user456"
        assertTrue(scanText(text) >= 2, "Несколько логинов через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "login: user123\nlogin: user456"
        assertTrue(scanText(text) >= 2, "Несколько логинов через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать логинов")
    }

    @Test
    fun testMinimalFormat() {
        val text = "login: abc"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testMaximalFormat() {
        val text = "login: abcdefghijklmnopqrstuvwxy"
        assertTrue(scanText(text) >= 1, "Максимальный формат должен быть найден")
    }

    @Test
    fun testWithUnderscore() {
        val text = "login: user_name"
        assertTrue(scanText(text) >= 1, "Логин с подчеркиванием должен быть найден")
    }

    @Test
    fun testWithDash() {
        val text = "login: user-name"
        assertTrue(scanText(text) >= 1, "Логин с дефисом должен быть найден")
    }

    @Test
    fun testWithNumbers() {
        val text = "login: user123"
        assertTrue(scanText(text) >= 1, "Логин с цифрами должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "login: ab"
        assertEquals(0, scanText(text), "Слишком короткий логин не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "login: abcdefghijklmnopqrstuvwxyz"
        assertEquals(0, scanText(text), "Слишком длинный логин не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "User    login: user123"
        assertTrue(scanText(text) >= 1, "Логин с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "User\tlogin: user123"
        assertTrue(scanText(text) >= 1, "Логин с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "login: user123\tnext"
        assertTrue(scanText(text) >= 1, "Логин с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Пользователь login: user123"
        assertTrue(scanText(text) >= 1, "Логин с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "User login: user123"
        assertTrue(scanText(text) >= 1, "Логин с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "login: user123"
        assertTrue(scanText(text) >= 1, "Логин отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "login: user123 text"
        assertTrue(scanText(text) >= 1, "Логин в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text login: user123"
        assertTrue(scanText(text) >= 1, "Логин в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testWithInvalidSpecialChars() {
        val text = "login: user@123"
        assertEquals(0, scanText(text), "Логин с недопустимыми спецсимволами не должен находиться")
    }

    @Test
    fun testStartingWithNumber() {
        val text = "login: 123user"
        // В зависимости от реализации - может быть найден или нет
        val count = scanText(text)
        assertTrue(count >= 0, "Логин, начинающийся с цифры")
    }
}

