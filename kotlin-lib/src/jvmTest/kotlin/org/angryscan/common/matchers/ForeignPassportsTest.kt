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
 * Тесты для проверки крайних позиций и пограничных значений матчера ForeignPassports
 */
internal class ForeignPassportsTest {

    @Test
    fun testForeignPassportAtStart() {
        val text = " A12345678 иностранный паспорт"
        assertTrue(scanText(text, ForeignPassports) >= 1, "Иностранный паспорт в начале должен быть найден")
    }

    @Test
    fun testForeignPassportAtEnd() {
        val text = "Foreign passport: A12345678 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Иностранный паспорт в конце должен быть найден")
    }

    @Test
    fun testForeignPassportInMiddle() {
        val text = "Гражданин с паспортом A12345678 въехал"
        assertTrue(scanText(text, ForeignPassports) >= 1, "Иностранный паспорт в середине должен быть найден")
    }

    @Test
    fun testForeignPassportStandalone() {
        val text = " A12345678 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Иностранный паспорт отдельно должен быть найден")
    }

    @Test
    fun testForeignPassportFormat1Letter8Digits() {
        val text = " A12345678 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Формат A12345678 должен быть найден")
    }

    @Test
    fun testForeignPassportFormatE() {
        val text = " E12345678 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Формат E12345678 должен быть найден")
    }

    @Test
    fun testForeignPassportFormatG() {
        val text = " G12345678 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Формат G12345678 должен быть найден")
    }

    @Test
    fun testForeignPassportFormat2Letters7Digits() {
        val text = " AB1234567 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Формат AB1234567 должен быть найден")
    }

    @Test
    fun testForeignPassportWithDash() {
        val text = " AB-1234567 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Паспорт с дефисом должен быть найден")
    }

    @Test
    fun testForeignPassportWithLabel() {
        val text = "иностранный паспорт: A12345678 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Паспорт с меткой должен быть найден")
    }

    @Test
    fun testForeignPassportUSA() {
        val text = "Passport US: A12345678 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Паспорт США должен быть найден")
    }

    @Test
    fun testForeignPassportEU() {
        val text = "Passport EU: AB1234567 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Паспорт ЕС должен быть найден")
    }

    @Test
    fun testForeignPassportChina() {
        val text = "Passport China: E12345678 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Паспорт Китая должен быть найден")
    }

    @Test
    fun testForeignPassportUpperCase() {
        val text = "FOREIGN PASSPORT: A12345678 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Паспорт в верхнем регистре должен быть найден")
    }

    @Test
    fun testForeignPassportLowerCase() {
        val text = "foreign passport: a12345678 "
        assertTrue(scanText(text, ForeignPassports) >= 1, "Паспорт в нижнем регистре должен быть найден")
    }

    @Test
    fun testForeignPassportInParentheses() {
        val text = "(A12345678)"
        assertTrue(scanText(text, ForeignPassports) >= 1, "Паспорт в скобках должен быть найден")
    }

    @Test
    fun testForeignPassportInQuotes() {
        val text = "\"A12345678\""
        assertTrue(scanText(text, ForeignPassports) >= 1, "Паспорт в кавычках должен быть найден")
    }

    @Test
    fun testMultipleForeignPassports() {
        val text = """
            Первый: A12345678
            Второй: E23456789
            Третий: AB1234567
        """.trimIndent()
        assertTrue(scanText(text, ForeignPassports) >= 3, "Несколько паспортов должны быть найдены")
    }

    @Test
    fun testForeignPassportInvalidTooShort() {
        val text = " A1234567 "
        assertEquals(0, scanText(text, ForeignPassports), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testForeignPassportInvalidTooLong() {
        val text = " A123456789 "
        assertEquals(0, scanText(text, ForeignPassports), "Слишком длинный номер не должен быть найден")
    }

    @Test
    fun testForeignPassportEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, ForeignPassports), "Пустая строка не должна содержать иностранного паспорта")
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

