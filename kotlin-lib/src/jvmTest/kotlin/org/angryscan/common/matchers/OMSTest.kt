package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера OMS
 */
internal class OMSTest {

    @Test
    fun testOMSAtStart() {
        val text = "ОМС 1234567890123452 выдан"
        assertEquals(1, scanText(text, OMS), "Полис ОМС в начале должен быть найден")
    }

    @Test
    fun testOMSAtEnd() {
        val text = "Номер полиса ОМС 1234567890123452"
        assertEquals(1, scanText(text, OMS), "Полис ОМС в конце должен быть найден")
    }

    @Test
    fun testOMSInMiddle() {
        val text = "Пациент имеет полис 1234567890123452 действующий"
        assertEquals(1, scanText(text, OMS), "Полис ОМС в середине должен быть найден")
    }

    @Test
    fun testOMSStandalone() {
        val text = "полис 1234567890123452"
        assertEquals(1, scanText(text, OMS), "Полис ОМС отдельной строкой должен быть найден")
    }

    @Test
    fun testOMSWithoutSpaces() {
        val text = "ОМС 1234567890123452"
        assertEquals(1, scanText(text, OMS), "Полис ОМС без пробелов должен быть найден")
    }

    @Test
    fun testOMSWithSpaces() {
        val text = "ОМС 1234 5678 9012 3452"
        assertEquals(1, scanText(text, OMS), "Полис ОМС с пробелами должен быть найден")
    }

    @Test
    fun testOMSWithDashes() {
        val text = "ОМС 1234-5678-9012-3452"
        assertEquals(1, scanText(text, OMS), "Полис ОМС с дефисами должен быть найден")
    }

    @Test
    fun testOMSWithTabs() {
        val text = "ОМС\t1234\t5678\t9012\t3452"
        assertEquals(1, scanText(text, OMS), "Полис ОМС с табуляцией должен быть найден")
    }

    @Test
    fun testOMSPrefixOMS() {
        val text = "ОМС 1234567890123452"
        assertEquals(1, scanText(text, OMS), "Полис с префиксом 'ОМС' должен быть найден")
    }

    @Test
    fun testOMSPrefixPolis() {
        val text = "полис 1234567890123452"
        assertEquals(1, scanText(text, OMS), "Полис с префиксом 'полис' должен быть найден")
    }

    @Test
    fun testOMSPrefixStrakhovka() {
        val text = "страховка 1234567890123452"
        assertEquals(1, scanText(text, OMS), "Полис с префиксом 'страховка' должен быть найден")
    }

    @Test
    fun testOMSPrefixStrakhovanie() {
        val text = "страхование 1234567890123452"
        assertEquals(1, scanText(text, OMS), "Полис с префиксом 'страхование' должен быть найден")
    }

    @Test
    fun testOMSUpperCase() {
        val text = "ОМС 1234567890123452"
        assertEquals(1, scanText(text, OMS), "Полис в верхнем регистре должен быть найден")
    }

    @Test
    fun testOMSLowerCase() {
        val text = "омс 1234567890123452"
        assertEquals(1, scanText(text, OMS), "Полис в нижнем регистре должен быть найден")
    }

    @Test
    fun testOMSMixedCase() {
        val text = "ОмС 1234567890123452"
        assertEquals(1, scanText(text, OMS), "Полис в смешанном регистре должен быть найден")
    }

    @Test
    fun testOMSInParentheses() {
        val text = "(ОМС 1234567890123452)"
        assertEquals(1, scanText(text, OMS), "Полис в скобках должен быть найден")
    }

    @Test
    fun testOMSInQuotes() {
        val text = "\"ОМС 1234567890123452\""
        assertEquals(1, scanText(text, OMS), "Полис в кавычках должен быть найден")
    }

    @Test
    fun testOMSWithPunctuation() {
        val text = "ОМС: 1234567890123452."
        assertEquals(1, scanText(text, OMS), "Полис с пунктуацией должен быть найден")
    }

    @Test
    fun testMultipleOMS() {
        val text = """
            Первый: ОМС 1234567890123452
            Второй: полис 9876543210987658
        """.trimIndent()
        assertEquals(2, scanText(text, OMS), "Несколько полисов ОМС должны быть найдены")
    }

    @Test
    fun testOMSInvalidChecksum() {
        val text = "ОМС 1234567890123457"
        assertEquals(0, scanText(text, OMS), "Полис с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testOMSTooShort() {
        val text = "ОМС 123456789012345"
        assertEquals(0, scanText(text, OMS), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testOMSTooLong() {
        val text = "ОМС 12345678901234567"
        assertEquals(0, scanText(text, OMS), "Слишком длинный номер не должен быть найден")
    }

    @Test
    fun testOMSWithoutPrefix() {
        val text = "1234567890123456"
        assertEquals(0, scanText(text, OMS), "Номер без префикса не должен быть найден")
    }

    @Test
    fun testOMSEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, OMS), "Пустая строка не должна содержать полиса ОМС")
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

