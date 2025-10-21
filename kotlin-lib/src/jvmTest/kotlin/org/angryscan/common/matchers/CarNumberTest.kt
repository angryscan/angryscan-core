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
 * Тесты для проверки крайних позиций и пограничных значений матчера CarNumber
 */
internal class CarNumberTest {

    @Test
    fun testCarNumberAtStart() {
        val text = "гос номер А123ВС77 зарегистрирован"
        assertTrue(scanText(text, CarNumber) >= 1, "Автомобильный номер в начале должен быть найден")
    }

    @Test
    fun testCarNumberAtEnd() {
        val text = "Гос номер: А123ВС77"
        assertTrue(scanText(text, CarNumber) >= 1, "Автомобильный номер в конце должен быть найден")
    }

    @Test
    fun testCarNumberInMiddle() {
        val text = "Машина с номером А123ВС77 припаркована"
        assertTrue(scanText(text, CarNumber) >= 1, "Автомобильный номер в середине должен быть найден")
    }

    @Test
    fun testCarNumberWithSpaces() {
        val text = "гос номер А 123 ВС 77"
        assertTrue(scanText(text, CarNumber) >= 1, "Автомобильный номер с пробелами должен быть найден")
    }

    @Test
    fun testCarNumberWithoutSpaces() {
        val text = "гос номер А123ВС77"
        assertTrue(scanText(text, CarNumber) >= 1, "Автомобильный номер без пробелов должен быть найден")
    }

    @Test
    fun testCarNumberRegion2Digits() {
        val text = "гос номер А123ВС77"
        assertTrue(scanText(text, CarNumber) >= 1, "Номер с 2-значным регионом должен быть найден")
    }

    @Test
    fun testCarNumberRegion3Digits() {
        val text = "гос номер А123ВС177"
        assertTrue(scanText(text, CarNumber) >= 1, "Номер с 3-значным регионом должен быть найден")
    }

    @Test
    fun testCarNumberMoscow() {
        val text = "гос номер А123ВС77"
        assertTrue(scanText(text, CarNumber) >= 1, "Московский номер должен быть найден")
    }

    @Test
    fun testCarNumberSPb() {
        val text = "гос номер А123ВС78"
        assertTrue(scanText(text, CarNumber) >= 1, "Петербургский номер должен быть найден")
    }

    @Test
    fun testCarNumberPrefixGos() {
        val text = "гос номер А123ВС77"
        assertTrue(scanText(text, CarNumber) >= 1, "Номер с префиксом 'гос' должен быть найден")
    }

    @Test
    fun testCarNumberPrefixNomer() {
        val text = "номер А123ВС77"
        assertTrue(scanText(text, CarNumber) >= 1, "Номер с префиксом 'номер' должен быть найден")
    }

    @Test
    fun testCarNumberPrefixAvto() {
        val text = "авто А123ВС77"
        assertTrue(scanText(text, CarNumber) >= 1, "Номер с префиксом 'авто' должен быть найден")
    }

    @Test
    fun testCarNumberPrefixReg() {
        val text = "рег номер А123ВС77"
        assertTrue(scanText(text, CarNumber) >= 1, "Номер с префиксом 'рег' должен быть найден")
    }

    @Test
    fun testCarNumberAllCyrillicLetters() {
        val text = """
            гос номер А123ВС77
            номер В456ЕК99
            авто О789РС123
        """.trimIndent()
        assertTrue(scanText(text, CarNumber) >= 3, "Номера с разными буквами должны быть найдены")
    }

    @Test
    fun testCarNumberAllLatinLetters() {
        val text = """
            гос номер A123BC77
            номер B456EK99
            авто O789PC123
        """.trimIndent()
        assertTrue(scanText(text, CarNumber) >= 3, "Номера с латинскими буквами должны быть найдены")
    }

    @Test
    fun testCarNumberMixedLetters() {
        val text = "гос номер А123BC77"
        assertTrue(scanText(text, CarNumber) >= 1, "Номер со смешанными буквами должен быть найден")
    }

    @Test
    fun testCarNumberUpperCase() {
        val text = "ГОС НОМЕР А123ВС77"
        assertTrue(scanText(text, CarNumber) >= 1, "Номер в верхнем регистре должен быть найден")
    }

    @Test
    fun testCarNumberLowerCase() {
        val text = "гос номер а123вс77"
        assertTrue(scanText(text, CarNumber) >= 1, "Номер в нижнем регистре должен быть найден")
    }

    @Test
    fun testMultipleCarNumbers() {
        val text = """
            Первая: гос номер А123ВС77
            Вторая: номер В456ЕК99
            Третья: авто О789РС123
        """.trimIndent()
        assertTrue(scanText(text, CarNumber) >= 3, "Несколько номеров должны быть найдены")
    }

    @Test
    fun testCarNumberEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, CarNumber), "Пустая строка не должна содержать автомобильного номера")
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

