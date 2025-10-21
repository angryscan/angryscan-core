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
 * Тесты для проверки крайних позиций и пограничных значений матчера AccountNumber
 */
internal class AccountNumberTest {

    @Test
    fun testAccountNumberAtStart() {
        val text = "40817810123456789012 счет физического лица"
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета в начале должен быть найден")
    }

    @Test
    fun testAccountNumberAtEnd() {
        val text = "Номер счета: 40817810123456789012"
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета в конце должен быть найден")
    }

    @Test
    fun testAccountNumberInMiddle() {
        val text = "Счет 40817810123456789012 активен"
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета в середине должен быть найден")
    }

    @Test
    fun testAccountNumberStandalone() {
        val text = "40817810123456789012"
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета отдельной строкой должен быть найден")
    }

    @Test
    fun testAccountNumberWithCurrency810() {
        val text = "40817810123456789012"
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета с валютой 810 (RUB) должен быть найден")
    }

    @Test
    fun testAccountNumberWithCurrency840() {
        val text = "40817840123456789012"
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета с валютой 840 (USD) должен быть найден")
    }

    @Test
    fun testAccountNumberWithCurrency978() {
        val text = "40817978123456789012"
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета с валютой 978 (EUR) должен быть найден")
    }

    @Test
    fun testAccountNumberAllPrefixes() {
        val text = """
            40000810123456789012
            40001810123456789012
            40999810123456789012
        """.trimIndent()
        assertTrue(scanText(text, AccountNumber) >= 3, "Номера счетов с разными префиксами должны быть найдены")
    }

    @Test
    fun testAccountNumberBoundaryPrefix40000() {
        val text = "40000810123456789012"
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета с префиксом 40000 должен быть найден")
    }

    @Test
    fun testAccountNumberBoundaryPrefix40999() {
        val text = "40999810123456789012"
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета с префиксом 40999 должен быть найден")
    }

    @Test
    fun testAccountNumberInParentheses() {
        val text = "(40817810123456789012)"
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета в скобках должен быть найден")
    }

    @Test
    fun testAccountNumberInQuotes() {
        val text = "\"40817810123456789012\""
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета в кавычках должен быть найден")
    }

    @Test
    fun testAccountNumberWithPunctuation() {
        val text = "Счет: 40817810123456789012."
        assertTrue(scanText(text, AccountNumber) >= 1, "Номер счета с точкой должен быть найден")
    }

    @Test
    fun testMultipleAccountNumbers() {
        val text = """
            Первый: 40817810123456789012
            Второй: 40817840987654321098
            Третий: 40817978111111111111
        """.trimIndent()
        assertTrue(scanText(text, AccountNumber) >= 3, "Несколько номеров счетов должны быть найдены")
    }

    @Test
    fun testAccountNumberInvalidPrefix39() {
        val text = "39817810123456789012"
        assertEquals(0, scanText(text, AccountNumber), "Номер счета с неверным префиксом не должен быть найден")
    }

    @Test
    fun testAccountNumberInvalidPrefix41() {
        val text = "41817810123456789012"
        assertEquals(0, scanText(text, AccountNumber), "Номер счета с неверным префиксом не должен быть найден")
    }

    @Test
    fun testAccountNumberInvalidCurrency() {
        val text = "40817999123456789012"
        assertEquals(0, scanText(text, AccountNumber), "Номер счета с неверной валютой не должен быть найден")
    }

    @Test
    fun testAccountNumberTooShort() {
        val text = "4081781012345678901"
        assertEquals(0, scanText(text, AccountNumber), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testAccountNumberTooLong() {
        val text = "408178101234567890123"
        assertEquals(0, scanText(text, AccountNumber), "Слишком длинный номер не должен быть найден")
    }

    @Test
    fun testAccountNumberEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, AccountNumber), "Пустая строка не должна содержать номера счета")
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

