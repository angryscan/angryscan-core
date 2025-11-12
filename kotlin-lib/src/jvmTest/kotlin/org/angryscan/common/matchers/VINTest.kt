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
 * Тесты для проверки крайних позиций и пограничных значений матчера VIN
 */
internal class VINTest {

    @Test
    fun testVINAtStart() {
        val text = "JF1SH92F4CG053823 это VIN номер"
        assertTrue(scanText(text, VIN) >= 1, "VIN в начале должен быть найден")
    }

    @Test
    fun testVINAtEnd() {
        val text = "VIN номер: JF1SH92F4CG053823"
        assertTrue(scanText(text, VIN) >= 1, "VIN в конце должен быть найден")
    }

    @Test
    fun testVINInMiddle() {
        val text = "Автомобиль с VIN JF1SH92F4CG053823 зарегистрирован"
        assertTrue(scanText(text, VIN) >= 1, "VIN в середине должен быть найден")
    }

    @Test
    fun testVINStandalone() {
        val text = "JF1SH92F4CG053823"
        assertTrue(scanText(text, VIN) >= 1, "VIN отдельной строкой должен быть найден")
    }

    @Test
    fun testVINWithPrefixVIN() {
        val text = "VIN: JF1SH92F4CG053823"
        assertTrue(scanText(text, VIN) >= 1, "VIN с префиксом 'VIN' должен быть найден")
    }

    @Test
    fun testVINWithPrefixVINRussian() {
        val text = "ВИН: JF1SH92F4CG053823"
        assertTrue(scanText(text, VIN) >= 1, "VIN с префиксом 'ВИН' должен быть найден")
    }

    @Test
    fun testVINWithPrefixVINCode() {
        val text = "VIN-код: JF1SH92F4CG053823"
        assertTrue(scanText(text, VIN) >= 1, "VIN с префиксом 'VIN-код' должен быть найден")
    }

    @Test
    fun testVINWithLongPrefix() {
        val text = "идентификационный номер транспортного средства: JF1SH92F4CG053823"
        assertTrue(scanText(text, VIN) >= 1, "VIN с длинным префиксом должен быть найден")
    }

    @Test
    fun testVINWithPrefixTS() {
        val text = "идентификационный номер ТС: JF1SH92F4CG053823"
        assertTrue(scanText(text, VIN) >= 1, "VIN с префиксом 'ТС' должен быть найден")
    }

    @Test
    fun testVINUpperCase() {
        val text = "VIN: JF1SH92F4CG053823"
        assertTrue(scanText(text, VIN) >= 1, "VIN в верхнем регистре должен быть найден")
    }


    @Test
    fun testVINAllNumbers() {
        val text = "VIN: 12345678901234567"
        assertTrue(scanText(text, VIN) >= 1, "VIN только из цифр должен быть найден")
    }

    @Test
    fun testVINAllLetters() {
        val text = "VIN: ABCDEFGHJKLMNPRST"
        assertTrue(scanText(text, VIN) >= 1, "VIN только из букв должен быть найден")
    }

    @Test
    fun testVINMixedLettersNumbers() {
        val text = "VIN: 1A2B3C4D5E6F7G8H9"
        assertTrue(scanText(text, VIN) >= 1, "VIN из букв и цифр должен быть найден")
    }

    @Test
    fun testVINInParentheses() {
        val text = "(JF1SH92F4CG053823)"
        assertTrue(scanText(text, VIN) >= 1, "VIN в скобках должен быть найден")
    }

    @Test
    fun testVINInQuotes() {
        val text = "\"JF1SH92F4CG053823\""
        assertTrue(scanText(text, VIN) >= 1, "VIN в кавычках должен быть найден")
    }

    @Test
    fun testVINWithPunctuation() {
        val text = "VIN: JF1SH92F4CG053823."
        assertTrue(scanText(text, VIN) >= 1, "VIN с точкой должен быть найден")
    }

    @Test
    fun testMultipleVINs() {
        val text = """
            Первый: JF1SH92F4CG053823
            Второй: WBAAN71020CK49034
            Третий: 5YJSA1E20HF194568
        """.trimIndent()
        assertTrue(scanText(text, VIN) >= 3, "Несколько VIN должны быть найдены")
    }

    @Test
    fun testVINInvalidLetterI() {
        val text = "VIN: JF1SH92F4CG05382I"
        assertEquals(0, scanText(text, VIN), "VIN с буквой I не должен быть найден")
    }

    @Test
    fun testVINInvalidLetterO() {
        val text = "VIN: JF1SH92F4CG05382O"
        assertEquals(0, scanText(text, VIN), "VIN с буквой O не должен быть найден")
    }

    @Test
    fun testVINInvalidLetterQ() {
        val text = "VIN: JF1SH92F4CG05382Q"
        assertEquals(0, scanText(text, VIN), "VIN с буквой Q не должен быть найден")
    }

    @Test
    fun testVINTooShort() {
        val text = "VIN: JF1SH92F4CG05382"
        assertEquals(0, scanText(text, VIN), "Слишком короткий VIN не должен быть найден")
    }

    @Test
    fun testVINTooLong() {
        val text = "VIN: JF1SH92F4CG0538231"
        assertEquals(0, scanText(text, VIN), "Слишком длинный VIN не должен быть найден")
    }

    @Test
    fun testVINEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, VIN), "Пустая строка не должна содержать VIN")
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

