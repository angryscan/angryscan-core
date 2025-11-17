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
 * Тесты для проверки крайних позиций и пограничных значений матчера OSAGOPolicy
 */
internal class OSAGOPolicyTest {

    @Test
    fun testOSAGOPolicyAtStart() {
        val text = " ХХХ 1234567890 полис ОСАГО"
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис ОСАГО в начале должен быть найден")
    }

    @Test
    fun testOSAGOPolicyAtEnd() {
        val text = "Номер полиса ОСАГО: ХХХ 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис ОСАГО в конце должен быть найден")
    }

    @Test
    fun testOSAGOPolicyInMiddle() {
        val text = "Автомобиль застрахован по полису ХХХ 1234567890 на год"
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис ОСАГО в середине должен быть найден")
    }

    @Test
    fun testOSAGOPolicyStandalone() {
        val text = " ХХХ 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис ОСАГО отдельной строкой должен быть найден")
    }

    @Test
    fun testOSAGOPolicyWithSpace() {
        val text = " ХХХ 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис ОСАГО с пробелом должен быть найден")
    }

    @Test
    fun testOSAGOPolicyWithoutSpace() {
        val text = " ХХХ1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис ОСАГО без пробела должен быть найден")
    }

    @Test
    fun testOSAGOPolicyWithNumber() {
        val text = " ХХХ № 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис ОСАГО с № должен быть найден")
    }

    @Test
    fun testOSAGOPolicyWithLabel() {
        val text = "полис ОСАГО: ХХХ 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис с меткой 'полис ОСАГО' должен быть найден")
    }

    @Test
    fun testOSAGOPolicyWithOSAGO() {
        val text = "ОСАГО: ХХХ 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис с меткой 'ОСАГО' должен быть найден")
    }

    @Test
    fun testOSAGOPolicyWithNomer() {
        val text = "номер полиса ОСАГО: ХХХ 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис с меткой 'номер полиса ОСАГО' должен быть найден")
    }

    @Test
    fun testOSAGOPolicyElectronic() {
        val text = "е-ОСАГО: ХХХ 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Электронный полис должен быть найден")
    }

    @Test
    fun testOSAGOPolicyElectronicFull() {
        val text = "электронный полис ОСАГО: ХХХ 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полное название электронного полиса должно быть найдено")
    }

    @Test
    fun testOSAGOPolicyStrakhovka() {
        val text = "страховка ОСАГО: ХХХ 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис с меткой 'страховка' должен быть найден")
    }

    @Test
    fun testOSAGOPolicyUpperCase() {
        val text = "ОСАГО: ХХХ 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис в верхнем регистре должен быть найден")
    }

    @Test
    fun testOSAGOPolicyLowerCase() {
        val text = "осаго: ххх 1234567890 "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис в нижнем регистре должен быть найден")
    }

    @Test
    fun testOSAGOPolicyDifferentSeries() {
        val text = """
            ААА 1234567890 
            ВВВ 2345678901 
            МММ 3456789012 
        """.trimIndent()
        assertTrue(scanText(text, OSAGOPolicy) >= 2, "Полисы с разными сериями должны быть найдены")
    }

    @Test
    fun testOSAGOPolicyInParentheses() {
        val text = "(ХХХ 1234567890) "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис в скобках должен быть найден")
    }

    @Test
    fun testOSAGOPolicyInQuotes() {
        val text = "\"ХХХ 1234567890\" "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис в кавычках должен быть найден")
    }

    @Test
    fun testOSAGOPolicyWithPunctuation() {
        val text = "ОСАГО: ХХХ 1234567890. "
        assertTrue(scanText(text, OSAGOPolicy) >= 1, "Полис с точкой должен быть найден")
    }

    @Test
    fun testMultipleOSAGOPolicies() {
        val text = """
            Первый: ХХХ 1234567890 
            Второй: ААА 9876543210 
            Третий: МММ 1111111111 
        """.trimIndent()
        assertTrue(scanText(text, OSAGOPolicy) >= 3, "Несколько полисов должны быть найдены")
    }

    @Test
    fun testOSAGOPolicyInvalidSeries2Letters() {
        val text = " ХХ 1234567890 "
        assertEquals(0, scanText(text, OSAGOPolicy), "Полис с 2 буквами не должен быть найден")
    }

    @Test
    fun testOSAGOPolicyInvalidSeries4Letters() {
        val text = " ХХХХ 1234567890 "
        assertEquals(0, scanText(text, OSAGOPolicy), "Полис с 4 буквами не должен быть найден")
    }

    @Test
    fun testOSAGOPolicyInvalidNumberShort() {
        val text = " ХХХ 123456789 "
        assertEquals(0, scanText(text, OSAGOPolicy), "Полис со слишком коротким номером не должен быть найден")
    }

    @Test
    fun testOSAGOPolicyInvalidNumberLong() {
        val text = " ХХХ 12345678901 "
        assertEquals(0, scanText(text, OSAGOPolicy), "Полис со слишком длинным номером не должен быть найден")
    }

    @Test
    fun testOSAGOPolicyEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, OSAGOPolicy), "Пустая строка не должна содержать полиса ОСАГО")
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

