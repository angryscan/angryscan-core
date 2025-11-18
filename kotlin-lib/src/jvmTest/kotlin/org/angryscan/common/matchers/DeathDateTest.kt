package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера DeathDate
 */
internal class DeathDateTest: MatcherTestBase(DeathDate) {

    @Test
    fun testDeathDateAtStart() {
        val text = "Дата смерти: 15.05.1990 указана в документе"
        assertTrue(scanText(text) >= 1, "Дата смерти в начале должна быть найдена")
    }

    @Test
    fun testDeathDateAtEnd() {
        val text = "В свидетельстве указана дата смерти: 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Дата смерти в конце должна быть найдена")
    }

    @Test
    fun testDeathDateInMiddle() {
        val text = "Гражданин умер 15.05.1990 в Москве"
        assertTrue(scanText(text) >= 1, "Дата смерти в середине должна быть найдена")
    }

    @Test
    fun testDeathDateStandalone() {
        val text = "дата смерти: 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Дата смерти отдельной строкой должна быть найдена")
    }

    @Test
    fun testDeathDateUmer() {
        val text = "умер: 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Формат 'умер' должен быть найден")
    }

    @Test
    fun testDeathDateUmerla() {
        val text = "умерла: 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Формат 'умерла' должен быть найден")
    }

    @Test
    fun testDeathDateSkonchalsa() {
        val text = "скончался: 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Формат 'скончался' должен быть найден")
    }

    @Test
    fun testDeathDateSkonchalas() {
        val text = "скончалась: 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Формат 'скончалась' должен быть найден")
    }

    @Test
    fun testDeathDateKonchina() {
        val text = "дата кончины: 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Формат 'дата кончины' должен быть найден")
    }

    @Test
    fun testDeathDateUmerUmerla() {
        val text = "умер(ла): 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Формат 'умер(ла)' должен быть найден")
    }

    @Test
    fun testDeathDateWithDots() {
        val text = "дата смерти: 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Дата с точками должна быть найдена")
    }

    @Test
    fun testDeathDateWithSlashes() {
        val text = "дата смерти: 15/05/1990 "
        assertTrue(scanText(text) >= 1, "Дата со слэшами должна быть найдена")
    }

    @Test
    fun testDeathDateWithDashes() {
        val text = "дата смерти: 15-05-1990 "
        assertTrue(scanText(text) >= 1, "Дата с дефисами должна быть найдена")
    }

    @Test
    fun testDeathDateWithSpaces() {
        val text = "дата смерти: 15 05 1990 "
        assertTrue(scanText(text) >= 1, "Дата с пробелами должна быть найдена")
    }

    @Test
    fun testDeathDateWithMonthName() {
        val text = "дата смерти: 15 мая 1990 "
        assertTrue(scanText(text) >= 1, "Дата с названием месяца должна быть найдена")
    }

    @Test
    fun testDeathDateWithGod() {
        val text = "дата смерти: 15.05.1990 г. "
        assertTrue(scanText(text) >= 1, "Дата с 'г.' должна быть найдена")
    }

    @Test
    fun testDeathDateUpperCase() {
        val text = "ДАТА СМЕРТИ: 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Дата в верхнем регистре должна быть найдена")
    }

    @Test
    fun testDeathDateLowerCase() {
        val text = "дата смерти: 15.05.1990 "
        assertTrue(scanText(text) >= 1, "Дата в нижнем регистре должна быть найдена")
    }

    @Test
    fun testMultipleDeathDates() {
        val text = """
            Первый: умер: 15.05.1990
            Вторая: умерла: 20.10.1995
            Третий: скончался: 03.03.2000
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько дат смерти должны быть найдены")
    }

    @Test
    fun testDeathDateInvalidDate() {
        val text = "дата смерти: 32.13.1990 "
        assertEquals(0, scanText(text), "Некорректная дата не должна быть найдена")
    }

    @Test
    fun testDeathDateInvalidYear() {
        val text = "дата смерти: 15.05.1899 "
        assertEquals(0, scanText(text), "Год до 1900 не должен быть найден")
    }

    @Test
    fun testDeathDateEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать даты смерти")
    }
}

