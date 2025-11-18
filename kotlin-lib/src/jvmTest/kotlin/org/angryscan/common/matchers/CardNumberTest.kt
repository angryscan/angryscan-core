package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера CardNumber
 */
internal class CardNumberTest: MatcherTestBase(CardNumber(false)) {

    @Test
    fun testCardNumberAtStart() {
        val text = "2200770122264882 номер карты"
        assertTrue(scanText(text) >= 1, "Номер карты в начале должен быть найден")
    }

    @Test
    fun testCardNumberAtEnd() {
        val text = "Номер карты: 2200770122264882"
        assertTrue(scanText(text) >= 1, "Номер карты в конце должен быть найден")
    }

    @Test
    fun testCardNumberInMiddle() {
        val text = "Карта 2200770122264882 активна"
        assertTrue(scanText(text) >= 1, "Номер карты в середине должен быть найден")
    }

    @Test
    fun testCardNumberStandalone() {
        val text = "2200770122264882"
        assertTrue(scanText(text) >= 1, "Номер карты отдельной строкой должен быть найден")
    }

    @Test
    fun testCardNumberWithSpaces() {
        val text = "2200 7701 2226 4882"
        assertTrue(scanText(text) >= 1, "Номер карты с пробелами должен быть найден")
    }

    @Test
    fun testCardNumberWithoutSpaces() {
        val text = "2200770122264882"
        assertTrue(scanText(text) >= 1, "Номер карты без пробелов должен быть найден")
    }

    @Test
    fun testCardNumberVisa() {
        val text = "4276380031264495"
        assertTrue(scanText(text) >= 1, "Visa карта должна быть найдена")
    }

    @Test
    fun testCardNumberMasterCard() {
        val text = "5536913887772867"
        assertTrue(scanText(text) >= 1, "MasterCard должна быть найдена")
    }

    @Test
    fun testCardNumberMir() {
        val text = "2200770122264882"
        assertTrue(scanText(text) >= 1, "Карта МИР должна быть найдена")
    }

    @Test
    fun testCardNumberInParentheses() {
        val text = "(2200770122264882)"
        assertTrue(scanText(text) >= 1, "Номер карты в скобках должен быть найден")
    }

    @Test
    fun testCardNumberInQuotes() {
        val text = "\"2200770122264882\""
        assertTrue(scanText(text) >= 1, "Номер карты в кавычках должен быть найден")
    }

    @Test
    fun testCardNumberWithPunctuation() {
        val text = "Карта: 2200770122264882."
        assertTrue(scanText(text) >= 1, "Номер карты с точкой должен быть найден")
    }

    @Test
    fun testCardNumberWithColon() {
        val text = "Карта:2200770122264882"
        assertTrue(scanText(text) >= 1, "Номер карты с двоеточием должен быть найден")
    }

    @Test
    fun testCardNumberWithDash() {
        val text = "Карта-2200770122264882"
        assertTrue(scanText(text) >= 1, "Номер карты с дефисом должен быть найден")
    }

    @Test
    fun testMultipleCardNumbers() {
        val text = """
            Первая: 2200770122264882
            Вторая: 4276380031264495
            Третья: 5536913887772867
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько номеров карт должны быть найдены")
    }

    @Test
    fun testCardNumberInvalidChecksum() {
        val text = "2200770122264881"
        assertEquals(0, scanText(text), "Номер карты с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testCardNumberAllZeros() {
        val text = "0000000000000000"
        assertEquals(0, scanText(text), "Номер карты из нулей не должен быть найден")
    }

    @Test
    fun testCardNumberTooShort() {
        val text = "123456789012345"
        assertEquals(0, scanText(text), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testCardNumberTooLong() {
        val text = "12345678901234567"
        assertEquals(0, scanText(text), "Слишком длинный номер не должен быть найден")
    }

    @Test
    fun testCardNumberEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать номера карты")
    }
}

