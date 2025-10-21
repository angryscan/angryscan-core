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
 * Тесты для проверки крайних позиций и пограничных значений матчера DeathDate
 */
internal class DeathDateTest {

    @Test
    fun testDeathDateAtStart() {
        val text = "Дата смерти: 15.05.1990 указана в документе"
        assertTrue(scanText(text, DeathDate) >= 1, "Дата смерти в начале должна быть найдена")
    }

    @Test
    fun testDeathDateAtEnd() {
        val text = "В свидетельстве указана дата смерти: 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Дата смерти в конце должна быть найдена")
    }

    @Test
    fun testDeathDateInMiddle() {
        val text = "Гражданин умер 15.05.1990 в Москве"
        assertTrue(scanText(text, DeathDate) >= 1, "Дата смерти в середине должна быть найдена")
    }

    @Test
    fun testDeathDateStandalone() {
        val text = "дата смерти: 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Дата смерти отдельной строкой должна быть найдена")
    }

    @Test
    fun testDeathDateUmer() {
        val text = "умер: 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Формат 'умер' должен быть найден")
    }

    @Test
    fun testDeathDateUmerla() {
        val text = "умерла: 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Формат 'умерла' должен быть найден")
    }

    @Test
    fun testDeathDateSkonchalsa() {
        val text = "скончался: 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Формат 'скончался' должен быть найден")
    }

    @Test
    fun testDeathDateSkonchalas() {
        val text = "скончалась: 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Формат 'скончалась' должен быть найден")
    }

    @Test
    fun testDeathDateKonchina() {
        val text = "дата кончины: 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Формат 'дата кончины' должен быть найден")
    }

    @Test
    fun testDeathDateUmerUmerla() {
        val text = "умер(ла): 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Формат 'умер(ла)' должен быть найден")
    }

    @Test
    fun testDeathDateWithDots() {
        val text = "дата смерти: 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Дата с точками должна быть найдена")
    }

    @Test
    fun testDeathDateWithSlashes() {
        val text = "дата смерти: 15/05/1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Дата со слэшами должна быть найдена")
    }

    @Test
    fun testDeathDateWithDashes() {
        val text = "дата смерти: 15-05-1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Дата с дефисами должна быть найдена")
    }

    @Test
    fun testDeathDateWithSpaces() {
        val text = "дата смерти: 15 05 1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Дата с пробелами должна быть найдена")
    }

    @Test
    fun testDeathDateWithMonthName() {
        val text = "дата смерти: 15 мая 1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Дата с названием месяца должна быть найдена")
    }

    @Test
    fun testDeathDateWithGod() {
        val text = "дата смерти: 15.05.1990 г. "
        assertTrue(scanText(text, DeathDate) >= 1, "Дата с 'г.' должна быть найдена")
    }

    @Test
    fun testDeathDateUpperCase() {
        val text = "ДАТА СМЕРТИ: 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Дата в верхнем регистре должна быть найдена")
    }

    @Test
    fun testDeathDateLowerCase() {
        val text = "дата смерти: 15.05.1990 "
        assertTrue(scanText(text, DeathDate) >= 1, "Дата в нижнем регистре должна быть найдена")
    }

    @Test
    fun testMultipleDeathDates() {
        val text = """
            Первый: умер: 15.05.1990
            Вторая: умерла: 20.10.1995
            Третий: скончался: 03.03.2000
        """.trimIndent()
        assertTrue(scanText(text, DeathDate) >= 3, "Несколько дат смерти должны быть найдены")
    }

    @Test
    fun testDeathDateInvalidDate() {
        val text = "дата смерти: 32.13.1990 "
        assertEquals(0, scanText(text, DeathDate), "Некорректная дата не должна быть найдена")
    }

    @Test
    fun testDeathDateInvalidYear() {
        val text = "дата смерти: 15.05.1899 "
        assertEquals(0, scanText(text, DeathDate), "Год до 1900 не должен быть найден")
    }

    @Test
    fun testDeathDateEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, DeathDate), "Пустая строка не должна содержать даты смерти")
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

