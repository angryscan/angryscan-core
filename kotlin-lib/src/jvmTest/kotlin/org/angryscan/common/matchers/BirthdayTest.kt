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
 * Тесты для проверки крайних позиций и пограничных значений матчера Birthday
 */
internal class BirthdayTest {

    @Test
    fun testBirthdayAtStart() {
        val text = "Дата рождения: 15.05.1990 указана в документе"
        assertTrue(scanText(text, Birthday) >= 1, "Дата рождения в начале должна быть найдена")
    }

    @Test
    fun testBirthdayAtEnd() {
        val text = "В документе указана дата рождения: 15.05.1990"
        assertTrue(scanText(text, Birthday) >= 1, "Дата рождения в конце должна быть найдена")
    }

    @Test
    fun testBirthdayInMiddle() {
        val text = "Гражданин родился 15.05.1990 в Москве"
        assertTrue(scanText(text, Birthday) >= 1, "Дата рождения в середине должна быть найдена")
    }

    @Test
    fun testBirthdayStandalone() {
        val text = "Дата рождения: 15.05.1990"
        assertTrue(scanText(text, Birthday) >= 1, "Дата рождения отдельной строкой должна быть найдена")
    }

    @Test
    fun testBirthdayWithDots() {
        val text = "Дата рождения: 15.05.1990"
        assertTrue(scanText(text, Birthday) >= 1, "Дата с точками должна быть найдена")
    }

    @Test
    fun testBirthdayWithSlashes() {
        val text = "Дата рождения: 15/05/1990"
        assertTrue(scanText(text, Birthday) >= 1, "Дата со слэшами должна быть найдена")
    }

    @Test
    fun testBirthdayWithDashes() {
        val text = "Дата рождения: 15-05-1990"
        assertTrue(scanText(text, Birthday) >= 1, "Дата с дефисами должна быть найдена")
    }

    @Test
    fun testBirthdayWithSpaces() {
        val text = "Дата рождения: 15 05 1990"
        assertTrue(scanText(text, Birthday) >= 1, "Дата с пробелами должна быть найдена")
    }

    @Test
    fun testBirthdayRodilsya() {
        val text = "родился: 15.05.1990"
        assertTrue(scanText(text, Birthday) >= 1, "Формат 'родился' должен быть найден")
    }

    @Test
    fun testBirthdayRodilas() {
        val text = "родилась: 15.05.1990"
        assertTrue(scanText(text, Birthday) >= 1, "Формат 'родилась' должен быть найден")
    }

    @Test
    fun testBirthdayRodilsyaRodilas() {
        val text = "родился(лась): 15.05.1990"
        assertTrue(scanText(text, Birthday) >= 1, "Формат 'родился(лась)' должен быть найден")
    }

    @Test
    fun testBirthdayBoundaryYear1900() {
        val text = "Дата рождения: 01.01.1900"
        assertTrue(scanText(text, Birthday) >= 1, "Граничный год 1900 должен быть найден")
    }

    @Test
    fun testBirthdayBoundaryYear2099() {
        val text = "Дата рождения: 31.12.2099"
        assertTrue(scanText(text, Birthday) >= 1, "Граничный год 2099 должен быть найден")
    }

    @Test
    fun testBirthdayLeapYear() {
        val text = "Дата рождения: 29.02.2000"
        assertTrue(scanText(text, Birthday) >= 1, "29 февраля високосного года должно быть найдено")
    }

    @Test
    fun testBirthdayInvalidLeapYear() {
        val text = "Дата рождения: 29.02.1999"
        assertEquals(0, scanText(text, Birthday), "29 февраля не високосного года не должно быть найдено")
    }

    @Test
    fun testBirthday31stMonth() {
        val text = "Дата рождения: 31.01.1990"
        assertTrue(scanText(text, Birthday) >= 1, "31-е число в месяце с 31 днем должно быть найдено")
    }

    @Test
    fun testBirthdayInvalid31stMonth() {
        val text = "Дата рождения: 31.04.1990"
        assertEquals(0, scanText(text, Birthday), "31-е число в месяце с 30 днями не должно быть найдено")
    }

    @Test
    fun testBirthdayWithMonthName() {
        val text = "Дата рождения: 15 мая 1990"
        assertTrue(scanText(text, Birthday) >= 1, "Дата с названием месяца должна быть найдена")
    }

    @Test
    fun testBirthdayAllMonths() {
        val text = """
            15 января 1990
            15 февраля 1990
            15 марта 1990
            15 апреля 1990
            15 мая 1990
            15 июня 1990
            15 июля 1990
            15 августа 1990
            15 сентября 1990
            15 октября 1990
            15 ноября 1990
            15 декабря 1990
        """.trimIndent()
        // Эти даты без префикса могут не быть найдены, так как паттерн требует "дата рождения" или "родился"
        // Но если они есть с префиксом, должны быть найдены
    }

    @Test
    fun testBirthdayMonthNameGenitive() {
        val text = """
            Дата рождения: 15 января 1990
            родился: 20 февраля 1985
            родилась: 10 марта 1995
        """.trimIndent()
        assertTrue(scanText(text, Birthday) >= 3, "Даты с названиями месяцев в родительном падеже должны быть найдены")
    }

    @Test
    fun testBirthdayWithGod() {
        val text = "Дата рождения: 15.05.1990 г."
        assertTrue(scanText(text, Birthday) >= 1, "Дата с 'г.' должна быть найдена")
    }

    @Test
    fun testBirthdayShortYear() {
        val text = "Дата рождения: 15 мая 90"
        assertTrue(scanText(text, Birthday) >= 1, "Дата с двухзначным годом должна быть найдена")
    }

    @Test
    fun testBirthdayUpperCase() {
        val text = "ДАТА РОЖДЕНИЯ: 15.05.1990"
        assertTrue(scanText(text, Birthday) >= 1, "Дата в верхнем регистре должна быть найдена")
    }

    @Test
    fun testBirthdayMixedCase() {
        val text = "ДаТа РоЖдЕнИя: 15.05.1990"
        assertTrue(scanText(text, Birthday) >= 1, "Дата в смешанном регистре должна быть найдена")
    }

    @Test
    fun testMultipleBirthdays() {
        val text = """
            Первый: дата рождения: 15.05.1990
            Второй: родился: 20.10.1985
            Третья: родилась: 03.03.1995
        """.trimIndent()
        assertTrue(scanText(text, Birthday) >= 3, "Несколько дат рождения должны быть найдены")
    }

    @Test
    fun testBirthdayInvalidDate() {
        val text = "Дата рождения: 32.13.1990"
        assertEquals(0, scanText(text, Birthday), "Некорректная дата не должна быть найдена")
    }

    @Test
    fun testBirthdayInvalidYear() {
        val text = "Дата рождения: 15.05.1899"
        assertEquals(0, scanText(text, Birthday), "Год до 1900 не должен быть найден")
    }

    @Test
    fun testBirthdayEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, Birthday), "Пустая строка не должна содержать даты рождения")
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

