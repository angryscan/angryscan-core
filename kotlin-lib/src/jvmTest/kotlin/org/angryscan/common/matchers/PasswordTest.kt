package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера Password
 */
internal class PasswordTest : MatcherTestBase(Password) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "User password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The password: MyPass123 is valid"
        assertTrue(scanText(text) >= 1, "Пароль в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "password: MyPass123\nSecond line"
        assertTrue(scanText(text) >= 1, "Пароль в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\npassword: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with password: MyPass123 pass"
        assertTrue(scanText(text) >= 1, "Пароль в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\npassword: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "password: MyPass123\n"
        assertTrue(scanText(text) >= 1, "Пароль перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\npassword: MyPass123\r\n"
        assertTrue(scanText(text) >= 1, "Пароль с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\npassword: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "password: MyPass123\n\n"
        assertTrue(scanText(text) >= 1, "Пароль перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testWithSpaceBefore() {
        val text = "User password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "password: MyPass123 is valid"
        assertTrue(scanText(text) >= 1, "Пароль с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "User, password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "password: MyPass123, next"
        assertTrue(scanText(text) >= 1, "Пароль с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "User. password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с точкой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "User; password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "password: MyPass123; next"
        assertTrue(scanText(text) >= 1, "Пароль с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "User: password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( password: MyPass123 )"
        assertTrue(scanText(text) >= 1, "Пароль в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(password: MyPass123)"
        assertTrue(scanText(text) >= 1, "Пароль в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" password: MyPass123 \""
        assertTrue(scanText(text) >= 1, "Пароль в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"password: MyPass123\""
        assertTrue(scanText(text) >= 1, "Пароль в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithColonAsPartOfFormat() {
        val text = "password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с двоеточием как часть формата должен быть найден")
    }

    @Test
    fun testWithSpaceAsPartOfFormat() {
        val text = "password MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с пробелом как часть формата должен быть найден")
    }

    @Test
    fun testWithCyrillicKeyword() {
        val text = "пароль: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с кириллическим ключевым словом должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "password: Pass1 password: Pass2"
        assertTrue(scanText(text) >= 2, "Несколько паролей через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "password: Pass1, password: Pass2"
        assertTrue(scanText(text) >= 2, "Несколько паролей через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "password: Pass1; password: Pass2"
        assertTrue(scanText(text) >= 2, "Несколько паролей через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "password: Pass1\npassword: Pass2"
        assertTrue(scanText(text) >= 2, "Несколько паролей через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать паролей")
    }

    @Test
    fun testMinimalFormat() {
        val text = "password: abc"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testMaximalFormat() {
        val text = "password: abcdefghijklmnopqrstuvwxy"
        assertTrue(scanText(text) >= 1, "Максимальный формат должен быть найден")
    }

    @Test
    fun testWithSpecialChars() {
        val text = "password: Pass!@#\$%"
        assertTrue(scanText(text) >= 1, "Пароль со спецсимволами должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "password: ab"
        assertEquals(0, scanText(text), "Слишком короткий пароль не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "password: abcdefghijklmnopqrstuvwxyz"
        assertEquals(0, scanText(text), "Слишком длинный пароль не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "User    password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "User\tpassword: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "password: MyPass123\tnext"
        assertTrue(scanText(text) >= 1, "Пароль с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Пользователь password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "User password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "password: MyPass123 text"
        assertTrue(scanText(text) >= 1, "Пароль в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text password: MyPass123"
        assertTrue(scanText(text) >= 1, "Пароль в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testWithNewlines() {
        val text = "password: My\nPass123"
        assertEquals(0, scanText(text), "Пароль с переносами строк не должен находиться")
    }

    @Test
    fun testEmptyPassword() {
        val text = "password: "
        assertEquals(0, scanText(text), "Пустой пароль не должен находиться")
    }
}

