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
 * Тесты для проверки крайних позиций и пограничных значений матчера MilitaryID
 */
internal class MilitaryIDTest {

    @Test
    fun testMilitaryIDAtStart() {
        val text = " АБ 1234567 удостоверение военнослужащего"
        assertTrue(scanText(text, MilitaryID) >= 1, "Военное удостоверение в начале должно быть найдено")
    }

    @Test
    fun testMilitaryIDAtEnd() {
        val text = "Номер удостоверения: АБ 1234567 "
        assertTrue(scanText(text, MilitaryID) >= 1, "Военное удостоверение в конце должно быть найдено")
    }

    @Test
    fun testMilitaryIDInMiddle() {
        val text = "Офицер с удостоверением АБ 1234567 на службе"
        assertTrue(scanText(text, MilitaryID) >= 1, "Военное удостоверение в середине должно быть найдено")
    }

    @Test
    fun testMilitaryIDStandalone() {
        val text = " АБ 1234567 "
        assertTrue(scanText(text, MilitaryID) >= 1, "Военное удостоверение отдельной строкой должно быть найдено")
    }

    @Test
    fun testMilitaryIDWithSpace() {
        val text = " АБ 1234567 "
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение с пробелом должно быть найдено")
    }

    @Test
    fun testMilitaryIDWithoutSpace() {
        val text = " АБ1234567 "
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение без пробела должно быть найдено")
    }

    @Test
    fun testMilitaryIDWithDash() {
        val text = " АБ-1234567 "
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение с дефисом должно быть найдено")
    }

    @Test
    fun testMilitaryIDWithNumber() {
        val text = " АБ № 1234567 "
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение с № должно быть найдено")
    }

    @Test
    fun testMilitaryIDWithLabel() {
        val text = "Номер: АБ 1234567 "
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение с меткой должно быть найдено")
    }

    @Test
    fun testMilitaryIDCyrillicLetters() {
        val text = " АБ 1234567 "
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение с кириллицей должно быть найдено")
    }

    @Test
    fun testMilitaryIDLatinLetters() {
        val text = " AB 1234567 "
        assertEquals(0, scanText(text, MilitaryID), "Удостоверение с латиницей не должно быть найдено (только кириллица)")
    }

    @Test
    fun testMilitaryIDUpperCase() {
        val text = "УДОСТОВЕРЕНИЕ: АБ 1234567 "
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение в верхнем регистре должно быть найдено")
    }

    @Test
    fun testMilitaryIDLowerCase() {
        val text = "удостоверение: аб 1234567 "
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение в нижнем регистре должно быть найдено")
    }

    @Test
    fun testMilitaryIDInParentheses() {
        val text = "(АБ 1234567)"
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение в скобках должно быть найдено")
    }

    @Test
    fun testMilitaryIDInQuotes() {
        val text = "\"АБ 1234567\""
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение в кавычках должно быть найдено")
    }

    @Test
    fun testMilitaryIDWithPunctuation() {
        val text = "Номер: АБ 1234567."
        assertTrue(scanText(text, MilitaryID) >= 1, "Удостоверение с точкой должно быть найдено")
    }

    @Test
    fun testMultipleMilitaryIDs() {
        val text = """
            Первое: АБ 1234567
            Второе: ВГ 2345678
            Третье: ДЕ 3456789
        """.trimIndent()
        assertTrue(scanText(text, MilitaryID) >= 3, "Несколько удостоверений должны быть найдены")
    }

    @Test
    fun testMilitaryIDInvalidTooShort() {
        val text = " АБ 123456 "
        assertEquals(0, scanText(text, MilitaryID), "Удостоверение со слишком коротким номером не должно быть найдено")
    }

    @Test
    fun testMilitaryIDInvalidTooLong() {
        val text = " АБ 12345678 "
        assertEquals(0, scanText(text, MilitaryID), "Удостоверение со слишком длинным номером не должно быть найдено")
    }

    @Test
    fun testMilitaryIDInvalidOneLetter() {
        val text = " А 1234567 "
        assertEquals(0, scanText(text, MilitaryID), "Удостоверение с одной буквой не должно быть найдено")
    }

    @Test
    fun testMilitaryIDInvalidThreeLetters() {
        val text = " АБВ 1234567 "
        assertEquals(0, scanText(text, MilitaryID), "Удостоверение с тремя буквами не должно быть найдено")
    }

    @Test
    fun testMilitaryIDEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, MilitaryID), "Пустая строка не должна содержать военного удостоверения")
    }

    @Test
    fun testMilitaryIDAllZeros() {
        val text = " АБ 0000000 "
        assertEquals(0, scanText(text, MilitaryID), "Удостоверение с нулями не должно быть найдено")
    }

    @Test
    fun testMilitaryIDAllSameDigits() {
        val text = " АБ 1111111 "
        assertEquals(0, scanText(text, MilitaryID), "Удостоверение с одинаковыми цифрами не должно быть найдено")
    }

    @Test
    fun testMilitaryIDAllSameDigits2() {
        val text = " ВГ 9999999 "
        assertEquals(0, scanText(text, MilitaryID), "Удостоверение с одинаковыми цифрами (9) не должно быть найдено")
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

