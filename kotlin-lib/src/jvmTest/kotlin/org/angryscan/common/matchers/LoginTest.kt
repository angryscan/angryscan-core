package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера Login
 */
internal class LoginTest: MatcherTestBase(Login) {

    @Test
    fun testLoginAtStart() {
        val text = "логин: user123 активен"
        assertTrue(scanText(text) >= 1, "Логин в начале должен быть найден")
    }

    @Test
    fun testLoginAtEnd() {
        val text = "Ваш логин: user123"
        assertTrue(scanText(text) >= 1, "Логин в конце должен быть найден")
    }

    @Test
    fun testLoginInMiddle() {
        val text = "Пользователь login: user123 зарегистрирован"
        assertTrue(scanText(text) >= 1, "Логин в середине должен быть найден")
    }

    @Test
    fun testLoginStandalone() {
        val text = "логин: user123"
        assertTrue(scanText(text) >= 1, "Логин отдельной строкой должен быть найден")
    }

    @Test
    fun testLoginWithPrefixRussian() {
        val text = "логин: user123"
        assertTrue(scanText(text) >= 1, "Логин с русским префиксом должен быть найден")
    }

    @Test
    fun testLoginWithPrefixEnglish() {
        val text = "login: user123"
        assertTrue(scanText(text) >= 1, "Логин с английским префиксом должен быть найден")
    }

    @Test
    fun testLoginWithColon() {
        val text = "логин: user123"
        assertTrue(scanText(text) >= 1, "Логин с двоеточием должен быть найден")
    }

    @Test
    fun testLoginWithoutColon() {
        val text = "логин user123"
        assertTrue(scanText(text) >= 1, "Логин без двоеточия должен быть найден")
    }

    @Test
    fun testLoginMinLength() {
        val text = "логин: abc"
        assertTrue(scanText(text) >= 1, "Логин минимальной длины (3 символа) должен быть найден")
    }

    @Test
    fun testLoginMaxLength() {
        val text = "логин: a234567890123456789012345"
        assertTrue(scanText(text) >= 1, "Логин максимальной длины (25 символов) должен быть найден")
    }

    @Test
    fun testLoginWithNumbers() {
        val text = "логин: user123"
        assertTrue(scanText(text) >= 1, "Логин с цифрами должен быть найден")
    }

    @Test
    fun testLoginWithUnderscore() {
        val text = "логин: user_name"
        assertTrue(scanText(text) >= 1, "Логин с подчеркиванием должен быть найден")
    }

    @Test
    fun testLoginWithDash() {
        val text = "логин: user-name"
        assertTrue(scanText(text) >= 1, "Логин с дефисом должен быть найден")
    }

    @Test
    fun testLoginOnlyNumbers() {
        val text = "логин: 123456"
        assertTrue(scanText(text) >= 1, "Логин только из цифр должен быть найден")
    }

    @Test
    fun testLoginOnlyLetters() {
        val text = "логин: username"
        assertTrue(scanText(text) >= 1, "Логин только из букв должен быть найден")
    }

    @Test
    fun testLoginMixed() {
        val text = "логин: user_123-test"
        assertTrue(scanText(text) >= 1, "Логин со смешанными символами должен быть найден")
    }

    @Test
    fun testLoginUpperCase() {
        val text = "ЛОГИН: user123"
        assertTrue(scanText(text) >= 1, "Логин в верхнем регистре должен быть найден")
    }

    @Test
    fun testLoginLowerCase() {
        val text = "логин: user123"
        assertTrue(scanText(text) >= 1, "Логин в нижнем регистре должен быть найден")
    }

    @Test
    fun testLoginMixedCase() {
        val text = "ЛоГиН: user123"
        assertTrue(scanText(text) >= 1, "Логин в смешанном регистре должен быть найден")
    }

    @Test
    fun testMultipleLogins() {
        val text = """
            Первый: логин: user1
            Второй: login: user2
            Третий: логин admin123
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько логинов должны быть найдены")
    }

    @Test
    fun testLoginTooShort() {
        val text = "логин: ab"
        assertEquals(0, scanText(text), "Слишком короткий логин не должен быть найден")
    }

    @Test
    fun testLoginTooLong() {
        val text = "логин: a2345678901234567890123456"
        assertEquals(0, scanText(text), "Слишком длинный логин не должен быть найден")
    }

    @Test
    fun testLoginWithSpecialChars() {
        val text = "логин: user@123"
        assertEquals(0, scanText(text), "Логин со спецсимволами не должен быть найден")
    }

    @Test
    fun testLoginEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать логина")
    }
}

