package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера Password
 */
internal class PasswordTest: MatcherTestBase(Password) {

    @Test
    fun testPasswordAtStart() {
        val text = "пароль: Password123 активен"
        assertTrue(scanText(text) >= 1, "Пароль в начале должен быть найден")
    }

    @Test
    fun testPasswordAtEnd() {
        val text = "Ваш пароль: Password123"
        assertTrue(scanText(text) >= 1, "Пароль в конце должен быть найден")
    }

    @Test
    fun testPasswordInMiddle() {
        val text = "Пользователь password: Password123 установлен"
        assertTrue(scanText(text) >= 1, "Пароль в середине должен быть найден")
    }

    @Test
    fun testPasswordStandalone() {
        val text = "пароль: Password123"
        assertTrue(scanText(text) >= 1, "Пароль отдельной строкой должен быть найден")
    }

    @Test
    fun testPasswordWithPrefixRussian() {
        val text = "пароль: Password123"
        assertTrue(scanText(text) >= 1, "Пароль с русским префиксом должен быть найден")
    }

    @Test
    fun testPasswordWithPrefixEnglish() {
        val text = "password: Password123"
        assertTrue(scanText(text) >= 1, "Пароль с английским префиксом должен быть найден")
    }

    @Test
    fun testPasswordWithColon() {
        val text = "пароль: Password123"
        assertTrue(scanText(text) >= 1, "Пароль с двоеточием должен быть найден")
    }

    @Test
    fun testPasswordWithoutColon() {
        val text = "пароль Password123"
        assertTrue(scanText(text) >= 1, "Пароль без двоеточия должен быть найден")
    }

    @Test
    fun testPasswordMinLength() {
        val text = "пароль: Abc"
        assertTrue(scanText(text) >= 1, "Пароль минимальной длины (3 символа) должен быть найден")
    }

    @Test
    fun testPasswordMaxLength() {
        val text = "пароль: A234567890123456789012345"
        assertTrue(scanText(text) >= 1, "Пароль максимальной длины (25 символов) должен быть найден")
    }

    @Test
    fun testPasswordWithUpperLower() {
        val text = "пароль: AbCdEf"
        assertTrue(scanText(text) >= 1, "Пароль с заглавными и строчными буквами должен быть найден")
    }

    @Test
    fun testPasswordWithNumbers() {
        val text = "пароль: Password123"
        assertTrue(scanText(text) >= 1, "Пароль с цифрами должен быть найден")
    }

    @Test
    fun testPasswordWithSpecialChars() {
        val text = "пароль: Password!@#"
        assertTrue(scanText(text) >= 1, "Пароль со спецсимволами должен быть найден")
    }

    @Test
    fun testPasswordAllSpecialChars() {
        val text = "пароль: !@#$%^&*()"
        assertTrue(scanText(text) >= 1, "Пароль только из спецсимволов должен быть найден")
    }

    @Test
    fun testPasswordWithUnderscore() {
        val text = "пароль: Pass_word123"
        assertTrue(scanText(text) >= 1, "Пароль с подчеркиванием должен быть найден")
    }

    @Test
    fun testPasswordWithDash() {
        val text = "пароль: Pass-word123"
        assertTrue(scanText(text) >= 1, "Пароль с дефисом должен быть найден")
    }

    @Test
    fun testPasswordWithDot() {
        val text = "пароль: Pass.word123"
        assertTrue(scanText(text) >= 1, "Пароль с точкой должен быть найден")
    }

    @Test
    fun testPasswordWithSlash() {
        val text = "пароль: Pass/word123"
        assertTrue(scanText(text) >= 1, "Пароль со слэшем должен быть найден")
    }

    @Test
    fun testPasswordWithBackslash() {
        val text = "пароль: Pass\\word123"
        assertTrue(scanText(text) >= 1, "Пароль с обратным слэшем должен быть найден")
    }

    @Test
    fun testPasswordUpperCase() {
        val text = "ПАРОЛЬ: Password123"
        assertTrue(scanText(text) >= 1, "Пароль в верхнем регистре должен быть найден")
    }

    @Test
    fun testPasswordLowerCase() {
        val text = "пароль: password123"
        assertTrue(scanText(text) >= 1, "Пароль в нижнем регистре должен быть найден")
    }

    @Test
    fun testPasswordMixedCase() {
        val text = "ПаРоЛь: Password123"
        assertTrue(scanText(text) >= 1, "Пароль в смешанном регистре должен быть найден")
    }

    @Test
    fun testMultiplePasswords() {
        val text = """
            Первый: пароль: Password1
            Второй: password: Pass2word
            Третий: пароль Admin123
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько паролей должны быть найдены")
    }

    @Test
    fun testPasswordTooShort() {
        val text = "пароль: ab"
        assertEquals(0, scanText(text), "Слишком короткий пароль не должен быть найден")
    }

    @Test
    fun testPasswordTooLong() {
        val text = "пароль: A2345678901234567890123456"
        assertEquals(0, scanText(text), "Слишком длинный пароль не должен быть найден")
    }

    @Test
    fun testPasswordEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать пароля")
    }
}

