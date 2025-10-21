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
 * Тесты для проверки крайних позиций и пограничных значений матчера ResidencePermit
 */
internal class ResidencePermitTest {

    @Test
    fun testResidencePermitAtStart() {
        val text = " 82 1234567 ВНЖ документ"
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ в начале должен быть найден")
    }

    @Test
    fun testResidencePermitAtEnd() {
        val text = "Вид на жительство: 82 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ в конце должен быть найден")
    }

    @Test
    fun testResidencePermitInMiddle() {
        val text = "Иностранец с ВНЖ 82 1234567 проживает"
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ в середине должен быть найден")
    }

    @Test
    fun testResidencePermitStandalone() {
        val text = " 82 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ отдельной строкой должен быть найден")
    }

    @Test
    fun testResidencePermit82() {
        val text = " 82 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ с кодом 82 должен быть найден")
    }

    @Test
    fun testResidencePermit83() {
        val text = " 83 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ с кодом 83 должен быть найден")
    }

    @Test
    fun testResidencePermitWithSpace() {
        val text = " 82 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ с пробелом должен быть найден")
    }

    @Test
    fun testResidencePermitWithoutSpace() {
        val text = " 821234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ без пробела должен быть найден")
    }

    @Test
    fun testResidencePermitWithNumber() {
        val text = " 82 № 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ с № должен быть найден")
    }

    @Test
    fun testResidencePermitWithNumberN() {
        val text = " 82 N 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ с N должен быть найден")
    }

    @Test
    fun testResidencePermitWithLabel() {
        val text = "вид на жительство: 82 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ с полной меткой должен быть найден")
    }

    @Test
    fun testResidencePermitWithVNZh() {
        val text = "ВНЖ: 82 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ с аббревиатурой должен быть найден")
    }

    @Test
    fun testResidencePermitUpperCase() {
        val text = "ВИД НА ЖИТЕЛЬСТВО: 82 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ в верхнем регистре должен быть найден")
    }

    @Test
    fun testResidencePermitLowerCase() {
        val text = "вид на жительство: 82 1234567 "
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ в нижнем регистре должен быть найден")
    }

    @Test
    fun testResidencePermitInParentheses() {
        val text = "(82 1234567)"
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ в скобках должен быть найден")
    }

    @Test
    fun testResidencePermitInQuotes() {
        val text = "\"82 1234567\""
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ в кавычках должен быть найден")
    }

    @Test
    fun testResidencePermitWithPunctuation() {
        val text = "ВНЖ: 82 1234567."
        assertTrue(scanText(text, ResidencePermit) >= 1, "ВНЖ с точкой должен быть найден")
    }

    @Test
    fun testMultipleResidencePermits() {
        val text = """
            Первый: 82 1234567
            Второй: 83 2345678
            Третий: 82 3456789
        """.trimIndent()
        assertTrue(scanText(text, ResidencePermit) >= 3, "Несколько ВНЖ должны быть найдены")
    }

    @Test
    fun testResidencePermitInvalidCode81() {
        val text = " 81 1234567 "
        assertEquals(0, scanText(text, ResidencePermit), "ВНЖ с неверным кодом 81 не должен быть найден")
    }

    @Test
    fun testResidencePermitInvalidCode84() {
        val text = " 84 1234567 "
        assertEquals(0, scanText(text, ResidencePermit), "ВНЖ с неверным кодом 84 не должен быть найден")
    }

    @Test
    fun testResidencePermitInvalidTooShort() {
        val text = " 82 123456 "
        assertEquals(0, scanText(text, ResidencePermit), "ВНЖ со слишком коротким номером не должен быть найден")
    }

    @Test
    fun testResidencePermitInvalidTooLong() {
        val text = " 82 12345678 "
        assertEquals(0, scanText(text, ResidencePermit), "ВНЖ со слишком длинным номером не должен быть найден")
    }

    @Test
    fun testResidencePermitEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, ResidencePermit), "Пустая строка не должна содержать ВНЖ")
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

