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
 * Тесты для проверки крайних позиций и пограничных значений матчера CadastralNumber
 */
internal class CadastralNumberTest {

    @Test
    fun testCadastralNumberAtStart() {
        val text = "77:01:123456:100 кадастровый номер"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер в начале должен быть найден")
    }

    @Test
    fun testCadastralNumberAtEnd() {
        val text = "Кадастровый номер объекта: 77:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер в конце должен быть найден")
    }

    @Test
    fun testCadastralNumberInMiddle() {
        val text = "Земельный участок с КН 77:01:123456:100 продается"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер в середине должен быть найден")
    }

    @Test
    fun testCadastralNumberStandalone() {
        val text = "77:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер отдельной строкой должен быть найден")
    }

    @Test
    fun testCadastralNumberWithSpaces() {
        val text = "77 : 01 : 123456 : 100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер с пробелами должен быть найден")
    }

    @Test
    fun testCadastralNumberWithoutSpaces() {
        val text = "77:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер без пробелов должен быть найден")
    }

    @Test
    fun testCadastralNumberMin6Digits() {
        val text = "77:01:100000:1"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер с минимум 6 цифрами должен быть найден")
    }

    @Test
    fun testCadastralNumberMax7Digits() {
        val text = "77:01:1234567:99999"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер с максимум 7 цифрами должен быть найден")
    }

    @Test
    fun testCadastralNumberWith1DigitLast() {
        val text = "77:01:123456:1"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер с 1 цифрой в конце должен быть найден")
    }

    @Test
    fun testCadastralNumberWith5DigitsLast() {
        val text = "77:01:123456:12345"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер с 5 цифрами в конце должен быть найден")
    }

    @Test
    fun testCadastralNumberWithFullLabel() {
        val text = "кадастровый номер объекта недвижимости: 77:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер с полной меткой должен быть найден")
    }

    @Test
    fun testCadastralNumberWithKN() {
        val text = "КН: 77:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер с КН должен быть найден")
    }

    @Test
    fun testCadastralNumberZemelnySite() {
        val text = "кадастровый номер земельного участка: 77:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "КН земельного участка должен быть найден")
    }

    @Test
    fun testCadastralNumberKvartira() {
        val text = "кадастровый номер квартиры: 77:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "КН квартиры должен быть найден")
    }

    @Test
    fun testCadastralNumberUpperCase() {
        val text = "КАДАСТРОВЫЙ НОМЕР: 77:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер в верхнем регистре должен быть найден")
    }

    @Test
    fun testCadastralNumberLowerCase() {
        val text = "кадастровый номер: 77:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер в нижнем регистре должен быть найден")
    }

    @Test
    fun testCadastralNumberInParentheses() {
        val text = "(77:01:123456:100)"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер в скобках должен быть найден")
    }

    @Test
    fun testCadastralNumberInQuotes() {
        val text = "\"77:01:123456:100\""
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер в кавычках должен быть найден")
    }

    @Test
    fun testCadastralNumberWithPunctuation() {
        val text = "КН: 77:01:123456:100."
        assertTrue(scanText(text, CadastralNumber) >= 1, "Кадастровый номер с точкой должен быть найден")
    }

    @Test
    fun testMultipleCadastralNumbers() {
        val text = """
            Первый: 77:01:123456:100
            Второй: 50:12:234567:200
            Третий: 78:05:345678:300
        """.trimIndent()
        assertTrue(scanText(text, CadastralNumber) >= 3, "Несколько кадастровых номеров должны быть найдены")
    }

    @Test
    fun testCadastralNumberMoscow77() {
        val text = "77:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Московский КН должен быть найден")
    }

    @Test
    fun testCadastralNumberSPb78() {
        val text = "78:01:123456:100"
        assertTrue(scanText(text, CadastralNumber) >= 1, "Петербургский КН должен быть найден")
    }

    @Test
    fun testCadastralNumberInvalidFormat1Digit() {
        val text = "77:1:123456:100"
        assertEquals(0, scanText(text, CadastralNumber), "КН с 1 цифрой во втором блоке не должен быть найден")
    }

    @Test
    fun testCadastralNumberInvalidFormat5Digits() {
        val text = "77:01:12345:100"
        assertEquals(0, scanText(text, CadastralNumber), "КН с 5 цифрами в третьем блоке не должен быть найден")
    }

    @Test
    fun testCadastralNumberInvalidFormat8Digits() {
        val text = "77:01:12345678:100"
        assertEquals(0, scanText(text, CadastralNumber), "КН с 8 цифрами в третьем блоке не должен быть найден")
    }

    @Test
    fun testCadastralNumberInvalidFormat6DigitsLast() {
        val text = "77:01:123456:123456"
        assertEquals(0, scanText(text, CadastralNumber), "КН с 6 цифрами в последнем блоке не должен быть найден")
    }

    @Test
    fun testCadastralNumberEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, CadastralNumber), "Пустая строка не должна содержать кадастрового номера")
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
