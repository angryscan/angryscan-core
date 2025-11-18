package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера SocialUserId
 */
internal class SocialUserIdTest: MatcherTestBase(SocialUserId) {

    @Test
    fun testSocialUserIdAtStart() {
        val text = " @username123 профиль пользователя"
        assertTrue(scanText(text) >= 1, "ID в соцсети в начале должен быть найден")
    }

    @Test
    fun testSocialUserIdAtEnd() {
        val text = "Профиль в VK: @username123 "
        assertTrue(scanText(text) >= 1, "ID в соцсети в конце должен быть найден")
    }

    @Test
    fun testSocialUserIdInMiddle() {
        val text = "Пользователь @username123 написал сообщение"
        assertTrue(scanText(text) >= 1, "ID в соцсети в середине должен быть найден")
    }

    @Test
    fun testSocialUserIdStandalone() {
        val text = " @username123 "
        assertTrue(scanText(text) >= 1, "ID в соцсети отдельно должен быть найден")
    }

    @Test
    fun testSocialUserIdMinLength() {
        val text = " @abc "
        assertTrue(scanText(text) >= 1, "ID минимальной длины (3 символа) должен быть найден")
    }

    @Test
    fun testSocialUserIdMaxLength() {
        val text = " @username_123456789_123456789012 "
        assertTrue(scanText(text) >= 1, "ID максимальной длины (32 символа) должен быть найден")
    }

    @Test
    fun testSocialUserIdWithUnderscore() {
        val text = " @user_name "
        assertTrue(scanText(text) >= 1, "ID с подчеркиванием должен быть найден")
    }

    @Test
    fun testSocialUserIdWithNumbers() {
        val text = " @user123 "
        assertTrue(scanText(text) >= 1, "ID с цифрами должен быть найден")
    }

    @Test
    fun testSocialUserIdOnlyNumbers() {
        val text = " @12345 "
        assertTrue(scanText(text) >= 1, "ID только из цифр должен быть найден")
    }

    @Test
    fun testSocialUserIdOnlyLetters() {
        val text = " @username "
        assertTrue(scanText(text) >= 1, "ID только из букв должен быть найден")
    }

    @Test
    fun testSocialUserIdMixed() {
        val text = " @user_name_123 "
        assertTrue(scanText(text) >= 1, "ID со смешанными символами должен быть найден")
    }

    @Test
    fun testSocialUserIdVK() {
        val text = "ID в VK: @username "
        assertTrue(scanText(text) >= 1, "ID VK должен быть найден")
    }

    @Test
    fun testSocialUserIdTelegram() {
        val text = "username в Telegram: @username "
        assertTrue(scanText(text) >= 1, "ID Telegram должен быть найден")
    }

    @Test
    fun testSocialUserIdInstagram() {
        val text = "аккаунт в Instagram: @username "
        assertTrue(scanText(text) >= 1, "ID Instagram должен быть найден")
    }

    @Test
    fun testSocialUserIdOdnoklassniki() {
        val text = "ID в Одноклассниках: @username "
        assertTrue(scanText(text) >= 1, "ID Одноклассников должен быть найден")
    }

    @Test
    fun testSocialUserIdFacebook() {
        val text = "аккаунт в Facebook: @username "
        assertTrue(scanText(text) >= 1, "ID Facebook должен быть найден")
    }

    @Test
    fun testSocialUserIdWithProfileLabel() {
        val text = "профиль ФЛ в соцсетях: @username "
        assertTrue(scanText(text) >= 1, "Профиль в соцсетях должен быть найден")
    }

    @Test
    fun testSocialUserIdUpperCase() {
        val text = "ID В VK: @username "
        assertTrue(scanText(text) >= 1, "ID в верхнем регистре должен быть найден")
    }

    @Test
    fun testSocialUserIdLowerCase() {
        val text = "id в vk: @username "
        assertTrue(scanText(text) >= 1, "ID в нижнем регистре должен быть найден")
    }

    @Test
    fun testSocialUserIdInText() {
        val text = "Напишите @username для связи"
        assertTrue(scanText(text) >= 1, "ID в тексте должен быть найден")
    }

    @Test
    fun testMultipleSocialUserIds() {
        val text = """
            VK: @user1
            Telegram: @user2
            Instagram: @user3
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько ID должны быть найдены")
    }

    @Test
    fun testSocialUserIdInParentheses() {
        val text = "(@username)"
        assertTrue(scanText(text) >= 1, "ID в скобках должен быть найден")
    }

    @Test
    fun testSocialUserIdInQuotes() {
        val text = "\"@username\""
        assertTrue(scanText(text) >= 1, "ID в кавычках должен быть найден")
    }

    @Test
    fun testSocialUserIdTooShort() {
        val text = " @ab "
        assertEquals(0, scanText(text), "Слишком короткий ID не должен быть найден")
    }

    @Test
    fun testSocialUserIdTooLong() {
        val text = " @username_12345678901234567890123456 "
        assertEquals(0, scanText(text), "Слишком длинный ID не должен быть найден")
    }

    @Test
    fun testSocialUserIdWithoutAt() {
        val text = " username123 "
        assertEquals(0, scanText(text), "ID без @ не должен быть найден")
    }

    @Test
    fun testSocialUserIdWithSpecialChars() {
        val text = " @user-name "
        assertEquals(0, scanText(text), "ID с дефисом не должен быть найден")
    }

    @Test
    fun testSocialUserIdWithDot() {
        val text = " @user.name "
        assertEquals(0, scanText(text), "ID с точкой не должен быть найден")
    }

    @Test
    fun testSocialUserIdEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ID в соцсетях")
    }
}
