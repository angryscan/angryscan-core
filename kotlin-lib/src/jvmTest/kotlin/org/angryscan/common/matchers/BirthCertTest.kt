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
 * Тесты для проверки крайних позиций и пограничных значений матчера BirthCert
 */
internal class BirthCertTest {

    @Test
    fun testBirthCertAtStart() {
        val text = "I-АБ 123456 свидетельство о рождении"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство в начале должно быть найдено")
    }

    @Test
    fun testBirthCertAtEnd() {
        val text = "Свидетельство о рождении: I-АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство в конце должно быть найдено")
    }

    @Test
    fun testBirthCertInMiddle() {
        val text = "Ребенок со свидетельством I-АБ 123456 родился в Москве"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство в середине должно быть найдено")
    }

    @Test
    fun testBirthCertStandalone() {
        val text = "I-АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство отдельной строкой должно быть найдено")
    }

    @Test
    fun testBirthCertRomanI() {
        val text = "I-АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с римской I должно быть найдено")
    }

    @Test
    fun testBirthCertRomanII() {
        val text = "II-АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с римской II должно быть найдено")
    }

    @Test
    fun testBirthCertRomanIII() {
        val text = "III-АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с римской III должно быть найдено")
    }

    @Test
    fun testBirthCertRomanIV() {
        val text = "IV-АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с римской IV должно быть найдено")
    }

    @Test
    fun testBirthCertRomanV() {
        val text = "V-АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с римской V должно быть найдено")
    }

    @Test
    fun testBirthCertRomanX() {
        val text = "X-АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с римской X должно быть найдено")
    }

    @Test
    fun testBirthCertWithDash() {
        val text = "I-АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с дефисом должно быть найдено")
    }

    @Test
    fun testBirthCertWithoutDash() {
        val text = "I АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство без дефиса должно быть найдено")
    }

    @Test
    fun testBirthCertWithNumber() {
        val text = "I-АБ № 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с № должно быть найдено")
    }

    @Test
    fun testBirthCertWithNumberN() {
        val text = "I-АБ N 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с N должно быть найдено")
    }

    @Test
    fun testBirthCertWithLabel() {
        val text = "свидетельство о рождении: I-АБ 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с меткой должно быть найдено")
    }

    @Test
    fun testBirthCertWithSeries() {
        val text = "серия: I-АБ № 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с 'серия' должно быть найдено")
    }

    @Test
    fun testBirthCertUpperCase() {
        val text = "СЕРИЯ I-АБ № 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство в верхнем регистре должно быть найдено")
    }

    @Test
    fun testBirthCertLowerCase() {
        val text = "серия i-аб № 123456"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство в нижнем регистре должно быть найдено")
    }

    @Test
    fun testBirthCertInParentheses() {
        val text = "(I-АБ 123456)"
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство в скобках должно быть найдено")
    }

    @Test
    fun testBirthCertInQuotes() {
        val text = "\"I-АБ 123456\""
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство в кавычках должно быть найдено")
    }

    @Test
    fun testBirthCertWithPunctuation() {
        val text = "Серия: I-АБ 123456."
        assertTrue(scanText(text, BirthCert) >= 1, "Свидетельство с точкой должно быть найдено")
    }

    @Test
    fun testMultipleBirthCerts() {
        val text = """
            Первое: I-АБ 123456
            Второе: II-ВГ 234567
            Третье: III-ДЕ 345678
        """.trimIndent()
        assertTrue(scanText(text, BirthCert) >= 3, "Несколько свидетельств должны быть найдены")
    }

    @Test
    fun testBirthCertInvalidLettersLatin() {
        val text = "I-AB 123456"
        assertEquals(0, scanText(text, BirthCert), "Свидетельство с латинскими буквами не должно быть найдено")
    }

    @Test
    fun testBirthCertInvalidRomanV5() {
        val text = "IIIII-АБ 123456"
        assertEquals(0, scanText(text, BirthCert), "Свидетельство с 5 римскими цифрами не должно быть найдено")
    }

    @Test
    fun testBirthCertTooShort() {
        val text = "I-АБ 12345"
        assertEquals(0, scanText(text, BirthCert), "Свидетельство со слишком коротким номером не должно быть найдено")
    }

    @Test
    fun testBirthCertTooLong() {
        val text = "I-АБ 1234567"
        assertEquals(0, scanText(text, BirthCert), "Свидетельство со слишком длинным номером не должно быть найдено")
    }

    @Test
    fun testBirthCertEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, BirthCert), "Пустая строка не должна содержать свидетельства")
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
