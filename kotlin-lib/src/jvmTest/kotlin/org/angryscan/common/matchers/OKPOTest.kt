package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера OKPO
 */
internal class OKPOTest {

    @Test
    fun testOKPOAtStart() {
        val text = "00032537 это ОКПО"
        assertEquals(1, scanText(text, OKPO), "ОКПО в начале должен быть найден")
    }

    @Test
    fun testOKPOAtEnd() {
        val text = "ОКПО организации: 00032537"
        assertEquals(1, scanText(text, OKPO), "ОКПО в конце должен быть найден")
    }

    @Test
    fun testOKPOInMiddle() {
        val text = "Организация с ОКПО 00032537 зарегистрирована"
        assertEquals(1, scanText(text, OKPO), "ОКПО в середине должен быть найден")
    }

    @Test
    fun testOKPOStandalone() {
        val text = "00032537"
        assertEquals(1, scanText(text, OKPO), "ОКПО отдельной строкой должен быть найден")
    }

    @Test
    fun testOKPOWithLabel() {
        val text = "код ОКПО: 00032537"
        assertEquals(1, scanText(text, OKPO), "ОКПО с меткой 'код ОКПО' должен быть найден")
    }

    @Test
    fun testOKPOWithOKPOYuL() {
        val text = "ОКПО ЮЛ: 00032537"
        assertEquals(1, scanText(text, OKPO), "ОКПО с меткой 'ОКПО ЮЛ' должен быть найден")
    }

    @Test
    fun testOKPOWithOrganization() {
        val text = "ОКПО организации: 00032537"
        assertEquals(1, scanText(text, OKPO), "ОКПО с меткой 'организации' должен быть найден")
    }

    @Test
    fun testOKPOUpperCase() {
        val text = "ОКПО: 00032537"
        assertEquals(1, scanText(text, OKPO), "ОКПО в верхнем регистре должен быть найден")
    }

    @Test
    fun testOKPOLowerCase() {
        val text = "окпо: 00032537"
        assertEquals(1, scanText(text, OKPO), "ОКПО в нижнем регистре должен быть найден")
    }

    @Test
    fun testOKPOMixedCase() {
        val text = "ОкПо: 00032537"
        assertEquals(1, scanText(text, OKPO), "ОКПО в смешанном регистре должен быть найден")
    }

    @Test
    fun testOKPOInParentheses() {
        val text = "(00032537)"
        assertEquals(1, scanText(text, OKPO), "ОКПО в скобках должен быть найден")
    }

    @Test
    fun testOKPOInQuotes() {
        val text = "\"00032537\""
        assertEquals(1, scanText(text, OKPO), "ОКПО в кавычках должен быть найден")
    }

    @Test
    fun testOKPOWithPunctuation() {
        val text = "ОКПО: 00032537."
        assertEquals(1, scanText(text, OKPO), "ОКПО с точкой должен быть найден")
    }

    @Test
    fun testMultipleOKPO() {
        val text = """
            Первый: 00032537
            Второй: 00032543
            Третий: 00032543
        """.trimIndent()
        assertEquals(3, scanText(text, OKPO), "Несколько ОКПО должны быть найдены")
    }

    @Test
    fun testOKPOInvalidChecksum() {
        val text = "00032536"
        assertEquals(0, scanText(text, OKPO), "ОКПО с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testOKPOTooShort() {
        val text = "0003253"
        assertEquals(0, scanText(text, OKPO), "Слишком короткий ОКПО не должен быть найден")
    }

    @Test
    fun testOKPOTooLong() {
        val text = "000325370"
        assertEquals(0, scanText(text, OKPO), "Слишком длинный ОКПО не должен быть найден")
    }

    @Test
    fun testOKPOWithLetters() {
        val text = "0003253A"
        assertEquals(0, scanText(text, OKPO), "ОКПО с буквами не должен быть найден")
    }

    @Test
    fun testOKPOEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, OKPO), "Пустая строка не должна содержать ОКПО")
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
