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
 * Тесты для проверки крайних позиций и пограничных значений матчера DriverLicense
 */
internal class DriverLicenseTest {

    @Test
    fun testDriverLicenseAtStart() {
        val text = "77 12 123456 это номер ВУ"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ в начале должно быть найдено")
    }

    @Test
    fun testDriverLicenseAtEnd() {
        val text = "Номер водительского удостоверения: 77 12 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ в конце должно быть найдено")
    }

    @Test
    fun testDriverLicenseInMiddle() {
        val text = "Водитель с ВУ 77 12 123456 зарегистрирован"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ в середине должно быть найдено")
    }

    @Test
    fun testDriverLicenseStandalone() {
        val text = "77 12 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ отдельной строкой должно быть найдено")
    }

    @Test
    fun testDriverLicenseWithSpaces() {
        val text = "77 12 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ с пробелами должно быть найдено")
    }

    @Test
    fun testDriverLicenseWithoutSpaces() {
        val text = "7712123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ без пробелов должно быть найдено")
    }

    @Test
    fun testDriverLicenseWithSpaceInSeries() {
        val text = "77 12 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ с пробелом в серии должно быть найдено")
    }

    @Test
    fun testDriverLicenseWithoutSpaceInSeries() {
        val text = "7712 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ без пробела в серии должно быть найдено")
    }

    @Test
    fun testDriverLicenseWithPrefix() {
        val text = "водительское удостоверение: 77 12 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ с префиксом должно быть найдено")
    }

    @Test
    fun testDriverLicenseWithAbbreviation() {
        val text = "ВУ: 77 12 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ с аббревиатурой должно быть найдено")
    }

    @Test
    fun testDriverLicenseBoundary00() {
        val text = "00 00 000000"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ с нулями должно быть найдено")
    }

    @Test
    fun testDriverLicenseBoundary99() {
        val text = "99 99 999999"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ с девятками должно быть найдено")
    }

    @Test
    fun testDriverLicenseMoscow() {
        val text = "77 12 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "Московское ВУ должно быть найдено")
    }

    @Test
    fun testDriverLicenseSPb() {
        val text = "78 12 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "Петербургское ВУ должно быть найдено")
    }

    @Test
    fun testDriverLicenseInParentheses() {
        val text = "(77 12 123456)"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ в скобках должно быть найдено")
    }

    @Test
    fun testDriverLicenseInQuotes() {
        val text = "\"77 12 123456\""
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ в кавычках должно быть найдено")
    }

    @Test
    fun testDriverLicenseWithPunctuation() {
        val text = "ВУ: 77 12 123456."
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ с точкой должно быть найдено")
    }

    @Test
    fun testDriverLicenseUpperCase() {
        val text = "ВОДИТЕЛЬСКОЕ УДОСТОВЕРЕНИЕ: 77 12 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ в верхнем регистре должно быть найдено")
    }

    @Test
    fun testDriverLicenseLowerCase() {
        val text = "водительское удостоверение: 77 12 123456"
        assertTrue(scanText(text, DriverLicense) >= 1, "ВУ в нижнем регистре должно быть найдено")
    }

    @Test
    fun testMultipleDriverLicenses() {
        val text = """
            Первое: 77 12 123456
            Второе: 78 34 567890
            Третье: 50 56 789012
        """.trimIndent()
        assertTrue(scanText(text, DriverLicense) >= 3, "Несколько ВУ должны быть найдены")
    }

    @Test
    fun testDriverLicenseTooShort() {
        val text = "77 12 12345"
        assertEquals(0, scanText(text, DriverLicense), "Слишком короткое ВУ не должно быть найдено")
    }

    @Test
    fun testDriverLicenseTooLong() {
        val text = "77 12 1234567"
        assertEquals(0, scanText(text, DriverLicense), "Слишком длинное ВУ не должно быть найдено")
    }

    @Test
    fun testDriverLicenseWithLetters() {
        val text = "77 AB 123456"
        assertEquals(0, scanText(text, DriverLicense), "ВУ с буквами не должно быть найдено")
    }

    @Test
    fun testDriverLicenseEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, DriverLicense), "Пустая строка не должна содержать ВУ")
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

