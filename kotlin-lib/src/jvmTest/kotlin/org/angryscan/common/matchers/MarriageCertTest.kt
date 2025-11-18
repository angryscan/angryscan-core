package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера MarriageCert
 */
internal class MarriageCertTest: MatcherTestBase(MarriageCert) {

    @Test
    fun testMarriageCertAtStart() {
        val text = "I-МЮ 123456 свидетельство о браке"
        assertTrue(scanText(text) >= 1, "Свидетельство о браке в начале должно быть найдено")
    }

    @Test
    fun testMarriageCertAtEnd() {
        val text = "Свидетельство о браке: I-МЮ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство о браке в конце должно быть найдено")
    }

    @Test
    fun testMarriageCertInMiddle() {
        val text = "Супруги со свидетельством I-МЮ 123456 зарегистрированы"
        assertTrue(scanText(text) >= 1, "Свидетельство о браке в середине должно быть найдено")
    }

    @Test
    fun testMarriageCertStandalone() {
        val text = "I-МЮ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство о браке отдельной строкой должно быть найдено")
    }

    @Test
    fun testMarriageCertRomanI() {
        val text = "I-МЮ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с римской I должно быть найдено")
    }

    @Test
    fun testMarriageCertRomanII() {
        val text = "II-МЮ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с римской II должно быть найдено")
    }

    @Test
    fun testMarriageCertRomanIII() {
        val text = "III-МЮ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с римской III должно быть найдено")
    }

    @Test
    fun testMarriageCertRomanIV() {
        val text = "IV-МЮ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с римской IV должно быть найдено")
    }

    @Test
    fun testMarriageCertWithZakluchenie() {
        val text = "свидетельство о заключении брака: I-МЮ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство о заключении брака должно быть найдено")
    }

    @Test
    fun testMarriageCertWithDash() {
        val text = "I-МЮ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с дефисом должно быть найдено")
    }

    @Test
    fun testMarriageCertWithoutDash() {
        val text = "I МЮ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство без дефиса должно быть найдено")
    }

    @Test
    fun testMarriageCertWithNumber() {
        val text = "I-МЮ № 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с № должно быть найдено")
    }

    @Test
    fun testMarriageCertWithNumberN() {
        val text = "I-МЮ N 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство с N должно быть найдено")
    }

    @Test
    fun testMarriageCertUpperCase() {
        val text = "СВИДЕТЕЛЬСТВО О БРАКЕ: I-МЮ 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство в верхнем регистре должно быть найдено")
    }

    @Test
    fun testMarriageCertLowerCase() {
        val text = "свидетельство о браке: i-мю 123456"
        assertTrue(scanText(text) >= 1, "Свидетельство в нижнем регистре должно быть найдено")
    }

    @Test
    fun testMarriageCertInParentheses() {
        val text = "(I-МЮ 123456)"
        assertTrue(scanText(text) >= 1, "Свидетельство в скобках должно быть найдено")
    }

    @Test
    fun testMarriageCertInQuotes() {
        val text = "\"I-МЮ 123456\""
        assertTrue(scanText(text) >= 1, "Свидетельство в кавычках должно быть найдено")
    }

    @Test
    fun testMarriageCertWithPunctuation() {
        val text = "Серия: I-МЮ 123456."
        assertTrue(scanText(text) >= 1, "Свидетельство с точкой должно быть найдено")
    }

    @Test
    fun testMultipleMarriageCerts() {
        val text = """
            Первое: I-МЮ 123456
            Второе: II-АБ 234567
            Третье: III-ВГ 345678
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько свидетельств должны быть найдены")
    }

    @Test
    fun testMarriageCertDifferentLetters() {
        val text = """
            I-АБ 123456
            I-ВГ 123456
            I-ДЕ 123456
            I-ЖЗ 123456
        """.trimIndent()
        assertTrue(scanText(text) >= 4, "Свидетельства с разными буквами должны быть найдены")
    }

    @Test
    fun testMarriageCertInvalidLettersLatin() {
        val text = "I-AB 123456"
        assertEquals(0, scanText(text), "Свидетельство с латинскими буквами не должно быть найдено")
    }

    @Test
    fun testMarriageCertTooShort() {
        val text = "I-МЮ 12345"
        assertEquals(0, scanText(text), "Свидетельство со слишком коротким номером не должно быть найдено")
    }

    @Test
    fun testMarriageCertTooLong() {
        val text = "I-МЮ 1234567"
        assertEquals(0, scanText(text), "Свидетельство со слишком длинным номером не должно быть найдено")
    }

    @Test
    fun testMarriageCertEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать свидетельства")
    }
}
