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
 * Тесты для проверки крайних позиций и пограничных значений матчера ForeignTIN
 */
internal class ForeignTINTest {

    @Test
    fun testForeignTINAtStart() {
        val text = " 123-45-6789 американский TIN"
        assertTrue(scanText(text, ForeignTIN) >= 1, "Иностранный TIN в начале должен быть найден")
    }

    @Test
    fun testForeignTINAtEnd() {
        val text = "TIN US: 123-45-6789 "
        assertTrue(scanText(text, ForeignTIN) >= 1, "Иностранный TIN в конце должен быть найден")
    }

    @Test
    fun testForeignTINInMiddle() {
        val text = "Налогоплательщик с TIN 123-45-6789 зарегистрирован"
        assertTrue(scanText(text, ForeignTIN) >= 1, "Иностранный TIN в середине должен быть найден")
    }

    @Test
    fun testForeignTINStandalone() {
        val text = " 123-45-6789 "
        assertTrue(scanText(text, ForeignTIN) >= 1, "Иностранный TIN отдельно должен быть найден")
    }

    @Test
    fun testForeignTINUSAWithDashes() {
        val text = " 123-45-6789 "
        assertTrue(scanText(text, ForeignTIN) >= 1, "TIN США с дефисами должен быть найден")
    }

    @Test
    fun testForeignTINUSAWithSpaces() {
        val text = " 123 45 6789 "
        assertTrue(scanText(text, ForeignTIN) >= 1, "TIN США с пробелами должен быть найден")
    }

    @Test
    fun testForeignTINChinese18() {
        val text = " 110101199001011234 "
        assertEquals(1, scanText(text, ForeignTIN), "Китайский TIN должен быть найден")
    }

    @Test
    fun testForeignTINChineseWithX() {
        val text = " 11010119900101123X "
        assertEquals(1, scanText(text, ForeignTIN), "Китайский TIN с X должен быть найден")
    }

    @Test
    fun testForeignTINWithLabel() {
        val text = "иностранный налоговый идентификационный номер: 123-45-6789 "
        assertTrue(scanText(text, ForeignTIN) >= 1, "TIN с полной меткой должен быть найден")
    }

    @Test
    fun testForeignTINWithShortLabel() {
        val text = "Foreign TIN: 123-45-6789 "
        assertTrue(scanText(text, ForeignTIN) >= 1, "TIN с короткой меткой должен быть найден")
    }

    @Test
    fun testForeignTINUSLabel() {
        val text = "TIN US: 123-45-6789 "
        assertTrue(scanText(text, ForeignTIN) >= 1, "TIN США с меткой должен быть найден")
    }

    @Test
    fun testForeignTINChinaLabel() {
        val text = "TIN China: 110101199001011234 "
        assertTrue(scanText(text, ForeignTIN) >= 1, "TIN Китая с меткой должен быть найден")
    }

    @Test
    fun testForeignTINUpperCase() {
        val text = "FOREIGN TIN: 123-45-6789 "
        assertTrue(scanText(text, ForeignTIN) >= 1, "TIN в верхнем регистре должен быть найден")
    }

    @Test
    fun testForeignTINLowerCase() {
        val text = "foreign tin: 123-45-6789 "
        assertTrue(scanText(text, ForeignTIN) >= 1, "TIN в нижнем регистре должен быть найден")
    }

    @Test
    fun testForeignTINInParentheses() {
        val text = "(123-45-6789)"
        assertTrue(scanText(text, ForeignTIN) >= 1, "TIN в скобках должен быть найден")
    }

    @Test
    fun testForeignTINInQuotes() {
        val text = "\"123-45-6789\""
        assertTrue(scanText(text, ForeignTIN) >= 1, "TIN в кавычках должен быть найден")
    }

    @Test
    fun testMultipleForeignTINs() {
        val text = """
            Первый: 123-45-6789
            Второй: 234-56-7890
            Третий: 110101199001011234
        """.trimIndent()
        assertTrue(scanText(text, ForeignTIN) >= 3, "Несколько TIN должны быть найдены")
    }

    @Test
    fun testForeignTINChineseInvalidDate() {
        val text = " 110101199913011234 "
        assertEquals(0, scanText(text, ForeignTIN), "Китайский TIN с некорректной датой не должен быть найден")
    }

    @Test
    fun testForeignTINChineseInvalidChecksum() {
        val text = " 110101199001011235 "
        assertEquals(0, scanText(text, ForeignTIN), "Китайский TIN с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testForeignTINEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, ForeignTIN), "Пустая строка не должна содержать иностранного TIN")
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

