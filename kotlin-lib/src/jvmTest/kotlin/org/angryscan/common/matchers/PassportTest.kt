package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера Passport
 */
internal class PassportTest: MatcherTestBase(Passport) {

    @Test
    fun testPassportAtStart() {
        val text = "паспорт 45 14 123456 выдан в 2015"
        assertTrue(scanText(text) >= 1, "Паспорт в начале должен быть найден")
    }

    @Test
    fun testPassportAtEnd() {
        val text = "Документ: паспорт 45 14 123456"
        assertTrue(scanText(text) >= 1, "Паспорт в конце должен быть найден")
    }

    @Test
    fun testPassportInMiddle() {
        val text = "Гражданин предъявил паспорт 45 14 123456 для проверки"
        assertTrue(scanText(text) >= 1, "Паспорт в середине должен быть найден")
    }

    @Test
    fun testPassportStandalone() {
        val text = "паспорт 45 14 123456"
        assertTrue(scanText(text) >= 1, "Паспорт отдельной строкой должен быть найден")
    }

    @Test
    fun testPassportWithSpaces() {
        val text = "паспорт 45 14 123456"
        assertTrue(scanText(text) >= 1, "Паспорт с пробелами должен быть найден")
    }

    @Test
    fun testPassportWithoutSpaces() {
        val text = "паспорт 4514123456"
        assertTrue(scanText(text) >= 1, "Паспорт без пробелов должен быть найден")
    }

    @Test
    fun testPassportWithDashes() {
        val text = "паспорт 45-14-123456"
        assertTrue(scanText(text) >= 1, "Паспорт с дефисами должен быть найден")
    }

    @Test
    fun testPassportWithTabs() {
        val text = "паспорт\t45\t14\t123456"
        assertTrue(scanText(text) >= 1, "Паспорт с табуляцией должен быть найден")
    }

    @Test
    fun testPassportSeriesFormat() {
        val text = "серия 45 14 номер 123456"
        assertTrue(scanText(text) >= 1, "Формат 'серия номер' должен быть найден")
    }

    @Test
    fun testPassportSeriesWithoutNomer() {
        val text = "серия 45 14 123456"
        assertTrue(scanText(text) >= 1, "Формат 'серия' без слова 'номер' должен быть найден")
    }

    @Test
    fun testPassportSeriesWithComma() {
        val text = "серия 45 14, номер 123456"
        assertTrue(scanText(text) >= 1, "Формат с запятой должен быть найден")
    }

    @Test
    fun testPassportBoundarySeries00() {
        val text = "паспорт 00 00 000000"
        assertTrue(scanText(text) >= 1, "Граничная серия 00 00 должна быть найдена")
    }

    @Test
    fun testPassportBoundarySeries99() {
        val text = "паспорт 99 99 999999"
        assertTrue(scanText(text) >= 1, "Граничная серия 99 99 должна быть найдена")
    }

    @Test
    fun testPassportWithRFWords() {
        val text = "паспорт гражданина РФ 45 14 123456"
        assertTrue(scanText(text) >= 1, "Паспорт со словами 'гражданина РФ' должен быть найден")
    }

    @Test
    fun testPassportWithRussianFederation() {
        val text = "паспорт Российской Федерации 45 14 123456"
        assertTrue(scanText(text) >= 1, "Паспорт со словами 'Российской Федерации' должен быть найден")
    }

    @Test
    fun testPassportInParentheses() {
        val text = "(паспорт 45 14 123456)"
        assertTrue(scanText(text) >= 1, "Паспорт в скобках должен быть найден")
    }

    @Test
    fun testPassportInQuotes() {
        val text = "\"паспорт 45 14 123456\""
        assertTrue(scanText(text) >= 1, "Паспорт в кавычках должен быть найден")
    }

    @Test
    fun testPassportWithPunctuation() {
        val text = "Документ: паспорт 45 14 123456."
        assertTrue(scanText(text) >= 1, "Паспорт с точкой должен быть найден")
    }

    @Test
    fun testPassportUpperCase() {
        val text = "ПАСПОРТ 45 14 123456"
        assertTrue(scanText(text) >= 1, "Паспорт в верхнем регистре должен быть найден")
    }

    @Test
    fun testPassportMixedCase() {
        val text = "ПаСпОрТ 45 14 123456"
        assertTrue(scanText(text) >= 1, "Паспорт в смешанном регистре должен быть найден")
    }

    @Test
    fun testMultiplePassports() {
        val text = """
            Первый: паспорт 45 14 123456
            Второй: паспорт 46 15 234567
            Третий: паспорт 47 16 345678
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько паспортов должны быть найдены")
    }

    @Test
    fun testPassportInvalidFormat() {
        val text = "паспорт 123 456"
        assertEquals(0, scanText(text), "Некорректный формат не должен быть найден")
    }

    @Test
    fun testPassportEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать паспорта")
    }
}

