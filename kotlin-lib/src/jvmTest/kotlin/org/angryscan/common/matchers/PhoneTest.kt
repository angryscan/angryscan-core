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
 * Тесты для проверки крайних позиций и пограничных значений матчера Phone
 */
internal class PhoneTest {

    @Test
    fun testPhoneAtStart() {
        val text = "+7 912 345 67 89 это мой номер"
        assertTrue(scanText(text, Phone) >= 1, "Телефон в начале должен быть найден")
    }

    @Test
    fun testPhoneAtEnd() {
        val text = "Контактный номер: +7 912 345 67 89"
        assertTrue(scanText(text, Phone) >= 1, "Телефон в конце должен быть найден")
    }

    @Test
    fun testPhoneInMiddle() {
        val text = "Позвоните по номеру +7 912 345 67 89 для связи"
        assertTrue(scanText(text, Phone) >= 1, "Телефон в середине должен быть найден")
    }

    @Test
    fun testPhoneStandalone() {
        val text = "+7 912 345 67 89"
        assertTrue(scanText(text, Phone) >= 1, "Телефон отдельной строкой должен быть найден")
    }

    @Test
    fun testPhoneWithPlus7() {
        val text = "+7 912 345 67 89"
        assertTrue(scanText(text, Phone) >= 1, "Телефон с +7 должен быть найден")
    }

    @Test
    fun testPhoneWith7() {
        val text = "7 912 345 67 89"
        assertTrue(scanText(text, Phone) >= 1, "Телефон с 7 должен быть найден")
    }

    @Test
    fun testPhoneWith8() {
        val text = "8 912 345 67 89"
        assertTrue(scanText(text, Phone) >= 1, "Телефон с 8 должен быть найден")
    }

    @Test
    fun testPhoneWithDashes() {
        val text = "+7-912-345-67-89"
        assertTrue(scanText(text, Phone) >= 1, "Телефон с дефисами должен быть найден")
    }

    @Test
    fun testPhoneWithParentheses() {
        val text = "+7 (912) 345-67-89"
        assertTrue(scanText(text, Phone) >= 1, "Телефон со скобками должен быть найден")
    }

    @Test
    fun testPhoneWithoutSpaces() {
        val text = "+79123456789"
        assertTrue(scanText(text, Phone) >= 1, "Телефон без пробелов должен быть найден")
    }

    @Test
    fun testPhoneCode9xx() {
        val text = "+7 912 345 67 89"
        assertTrue(scanText(text, Phone) >= 1, "Телефон с кодом 9xx должен быть найден")
    }

    @Test
    fun testPhoneCode8xx() {
        val text = "+7 812 345 67 89"
        assertTrue(scanText(text, Phone) >= 1, "Телефон с кодом 8xx должен быть найден")
    }

    @Test
    fun testPhoneCode4xx() {
        val text = "+7 495 345 67 89"
        assertTrue(scanText(text, Phone) >= 1, "Телефон с кодом 4xx должен быть найден")
    }

    @Test
    fun testPhoneMoscow495() {
        val text = "+7 495 123 45 67"
        assertTrue(scanText(text, Phone) >= 1, "Московский номер 495 должен быть найден")
    }

    @Test
    fun testPhoneMoscow499() {
        val text = "+7 499 123 45 67"
        assertTrue(scanText(text, Phone) >= 1, "Московский номер 499 должен быть найден")
    }

    @Test
    fun testPhoneSPb812() {
        val text = "+7 812 123 45 67"
        assertTrue(scanText(text, Phone) >= 1, "Петербургский номер 812 должен быть найден")
    }

    @Test
    fun testPhoneInParenthesesFull() {
        val text = "(+7 912 345 67 89)"
        assertTrue(scanText(text, Phone) >= 1, "Телефон в полных скобках должен быть найден")
    }

    @Test
    fun testPhoneInQuotes() {
        val text = "\"+7 912 345 67 89\""
        assertTrue(scanText(text, Phone) >= 1, "Телефон в кавычках должен быть найден")
    }

    @Test
    fun testPhoneWithPunctuation() {
        val text = "Номер: +7 912 345 67 89."
        assertTrue(scanText(text, Phone) >= 1, "Телефон с точкой должен быть найден")
    }

    @Test
    fun testPhoneWithEquals() {
        val text = "phone=+79123456789"
        assertTrue(scanText(text, Phone) >= 1, "Телефон после знака равенства должен быть найден")
    }

    @Test
    fun testPhoneWithAsterisk() {
        val text = "*+79123456789"
        assertTrue(scanText(text, Phone) >= 1, "Телефон после звездочки должен быть найден")
    }

    @Test
    fun testMultiplePhones() {
        val text = """
            Первый: +7 912 345 67 89
            Второй: 8 923 456 78 90
            Третий: +7 495 123 45 67
        """.trimIndent()
        assertTrue(scanText(text, Phone) >= 3, "Несколько телефонов должны быть найдены")
    }

    @Test
    fun testPhoneInvalidCode() {
        val text = "+7 112 345 67 89"
        assertEquals(0, scanText(text, Phone), "Телефон с некорректным кодом не должен быть найден")
    }

    @Test
    fun testPhoneTooShort() {
        val text = "+7 912 345"
        assertEquals(0, scanText(text, Phone), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testPhoneEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, Phone), "Пустая строка не должна содержать телефона")
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

