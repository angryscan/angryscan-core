package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера Email
 */
internal class EmailTest {

    @Test
    fun testEmailAtStart() {
        val text = "user@example.com это мой email"
        assertTrue(scanText(text, Email) >= 1, "Email в начале должен быть найден")
    }

    @Test
    fun testEmailAtEnd() {
        val text = "Мой email: user@example.com"
        assertTrue(scanText(text, Email) >= 1, "Email в конце должен быть найден")
    }

    @Test
    fun testEmailInMiddle() {
        val text = "Напишите на user@example.com для связи"
        assertTrue(scanText(text, Email) >= 1, "Email в середине должен быть найден")
    }

    @Test
    fun testEmailStandalone() {
        val text = "user@example.com"
        assertTrue(scanText(text, Email) >= 1, "Email отдельной строкой должен быть найден")
    }

    @Test
    fun testEmailWithDots() {
        val text = "john.doe@example.com"
        assertTrue(scanText(text, Email) >= 1, "Email с точками должен быть найден")
    }

    @Test
    fun testEmailWithPlus() {
        val text = "user+tag@example.com"
        assertTrue(scanText(text, Email) >= 1, "Email с плюсом должен быть найден")
    }

    @Test
    fun testEmailWithUnderscore() {
        val text = "user_name@example.com"
        assertTrue(scanText(text, Email) >= 1, "Email с подчеркиванием должен быть найден")
    }

    @Test
    fun testEmailWithDash() {
        val text = "user-name@example.com"
        assertTrue(scanText(text, Email) >= 1, "Email с дефисом должен быть найден")
    }

    @Test
    fun testEmailWithNumbers() {
        val text = "user123@example.com"
        assertTrue(scanText(text, Email) >= 1, "Email с цифрами должен быть найден")
    }

    @Test
    fun testEmailDomainWithDash() {
        val text = "user@my-domain.com"
        assertTrue(scanText(text, Email) >= 1, "Email с дефисом в домене должен быть найден")
    }

    @Test
    fun testEmailDomainWithSubdomain() {
        val text = "user@mail.example.com"
        assertTrue(scanText(text, Email) >= 1, "Email с поддоменом должен быть найден")
    }

    @Test
    fun testEmailLongTLD() {
        val text = "user@example.info"
        assertTrue(scanText(text, Email) >= 1, "Email с длинным TLD должен быть найден")
    }

    @Test
    fun testEmailShortTLD() {
        val text = "user@example.ru"
        assertTrue(scanText(text, Email) >= 1, "Email с коротким TLD должен быть найден")
    }

    @Test
    fun testEmailRussianDomain() {
        val text = "user@yandex.ru"
        assertTrue(scanText(text, Email) >= 1, "Email на .ru должен быть найден")
    }

    @Test
    fun testEmailGmail() {
        val text = "user@gmail.com"
        assertTrue(scanText(text, Email) >= 1, "Gmail адрес должен быть найден")
    }

    @Test
    fun testEmailYandex() {
        val text = "user@yandex.ru"
        assertTrue(scanText(text, Email) >= 1, "Yandex адрес должен быть найден")
    }

    @Test
    fun testEmailMailRu() {
        val text = "user@mail.ru"
        assertTrue(scanText(text, Email) >= 1, "Mail.ru адрес должен быть найден")
    }

    @Test
    fun testEmailInParentheses() {
        val text = "(user@example.com)"
        assertTrue(scanText(text, Email) >= 1, "Email в скобках должен быть найден")
    }

    @Test
    fun testEmailInQuotes() {
        val text = "\"user@example.com\""
        assertTrue(scanText(text, Email) >= 1, "Email в кавычках должен быть найден")
    }

    @Test
    fun testEmailWithPunctuation() {
        val text = "Email: user@example.com."
        assertTrue(scanText(text, Email) >= 1, "Email с точкой должен быть найден")
    }

    @Test
    fun testEmailWithEquals() {
        val text = "email=user@example.com"
        assertTrue(scanText(text, Email) >= 1, "Email после знака равенства должен быть найден")
    }

    @Test
    fun testMultipleEmails() {
        val text = """
            Первый: user1@example.com
            Второй: user2@example.com
            Третий: user3@example.com
        """.trimIndent()
        assertTrue(scanText(text, Email) >= 3, "Несколько email должны быть найдены")
    }

    @Test
    fun testEmailUpperCase() {
        val text = "USER@EXAMPLE.COM"
        assertTrue(scanText(text, Email) >= 1, "Email в верхнем регистре должен быть найден")
    }

    @Test
    fun testEmailMixedCase() {
        val text = "UsEr@ExAmPlE.CoM"
        assertTrue(scanText(text, Email) >= 1, "Email в смешанном регистре должен быть найден")
    }

    @Test
    fun testEmailInvalidNoAt() {
        val text = "userexample.com"
        assertEquals(0, scanText(text, Email), "Email без @ не должен быть найден")
    }

    @Test
    fun testEmailInvalidNoDomain() {
        val text = "user@"
        assertEquals(0, scanText(text, Email), "Email без домена не должен быть найден")
    }

    @Test
    fun testEmailInvalidNoTLD() {
        val text = "user@example"
        assertEquals(0, scanText(text, Email), "Email без TLD не должен быть найден")
    }

    @Test
    fun testEmailEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, Email), "Пустая строка не должна содержать email")
    }

    private fun scanText(text: String, matcher: IMatcher): Int {
        val kotlinEngine = KotlinEngine(listOf(matcher).filterIsInstance<IKotlinMatcher>())
        val hyperEngine = HyperScanEngine(listOf(matcher).filterIsInstance<IHyperMatcher>())

        val kotlinRes = kotlinEngine.scan(text)
        val hyperRes = hyperEngine.scan(text)
        
        assertEquals(
            kotlinRes.count(),
            hyperRes.count(),
            "Количество совпадений для ${matcher.name} должно быть одинаковым для обоих движков. " +
            "Kotlin: ${kotlinRes.count()}, Hyper: ${hyperRes.count()}\nText: $text"
        )
        
        return kotlinRes.count()
    }
}

