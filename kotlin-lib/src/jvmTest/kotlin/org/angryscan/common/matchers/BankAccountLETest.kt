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
 * Тесты для проверки крайних позиций и пограничных значений матчера BankAccountLE
 */
internal class BankAccountLETest {

    @Test
    fun testBankAccountLEAtStart() {
        val text = "40702810123456789012 счет ЮЛ"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет ЮЛ в начале должен быть найден")
    }

    @Test
    fun testBankAccountLEAtEnd() {
        val text = "Счет организации: 40702810123456789012"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет ЮЛ в конце должен быть найден")
    }

    @Test
    fun testBankAccountLEInMiddle() {
        val text = "Организация со счетом 40702810123456789012 работает"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет ЮЛ в середине должен быть найден")
    }

    @Test
    fun testBankAccountLEStandalone() {
        val text = "40702810123456789012"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет ЮЛ отдельной строкой должен быть найден")
    }

    @Test
    fun testBankAccountLEBoundary40700() {
        val text = "40700000000000000000"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Граничный счет 40700 должен быть найден")
    }

    @Test
    fun testBankAccountLEBoundary40799() {
        val text = "40799999999999999999"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Граничный счет 40799 должен быть найден")
    }

    @Test
    fun testBankAccountLEWithLabel() {
        val text = "номер банковского счета ЮЛ: 40702810123456789012"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет с меткой должен быть найден")
    }

    @Test
    fun testBankAccountLEWithRS() {
        val text = "р/с организации: 40702810123456789012"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет с 'р/с организации' должен быть найден")
    }

    @Test
    fun testBankAccountLEWithRaschetny() {
        val text = "расчетный счет ЮЛ: 40702810123456789012"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет с 'расчетный счет ЮЛ' должен быть найден")
    }

    @Test
    fun testBankAccountLEInParentheses() {
        val text = "(40702810123456789012)"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет в скобках должен быть найден")
    }

    @Test
    fun testBankAccountLEInQuotes() {
        val text = "\"40702810123456789012\""
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет в кавычках должен быть найден")
    }

    @Test
    fun testBankAccountLEWithPunctuation() {
        val text = "Счет: 40702810123456789012."
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет с точкой должен быть найден")
    }

    @Test
    fun testBankAccountLEUpperCase() {
        val text = "Р/С ОРГАНИЗАЦИИ: 40702810123456789012"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет в верхнем регистре должен быть найден")
    }

    @Test
    fun testBankAccountLELowerCase() {
        val text = "р/с организации: 40702810123456789012"
        assertTrue(scanText(text, BankAccountLE) >= 1, "Счет в нижнем регистре должен быть найден")
    }

    @Test
    fun testMultipleBankAccountLE() {
        val text = """
            Первый: 40702810123456789012
            Второй: 40702810987654321098
            Третий: 40702810111111111111
        """.trimIndent()
        assertTrue(scanText(text, BankAccountLE) >= 3, "Несколько счетов ЮЛ должны быть найдены")
    }

    @Test
    fun testBankAccountLEInvalidPrefix406() {
        val text = "40602810123456789012"
        assertEquals(0, scanText(text, BankAccountLE), "Счет с неверным префиксом 406 не должен быть найден")
    }

    @Test
    fun testBankAccountLEInvalidPrefix408() {
        val text = "40802810123456789012"
        assertEquals(0, scanText(text, BankAccountLE), "Счет с неверным префиксом 408 не должен быть найден")
    }

    @Test
    fun testBankAccountLEInvalidPrefix405() {
        val text = "40502810123456789012"
        assertEquals(0, scanText(text, BankAccountLE), "Счет с неверным префиксом 405 не должен быть найден")
    }

    @Test
    fun testBankAccountLETooShort() {
        val text = "4070281012345678901"
        assertEquals(0, scanText(text, BankAccountLE), "Слишком короткий счет не должен быть найден")
    }

    @Test
    fun testBankAccountLETooLong() {
        val text = "407028101234567890123"
        assertEquals(0, scanText(text, BankAccountLE), "Слишком длинный счет не должен быть найден")
    }

    @Test
    fun testBankAccountLEWithLetters() {
        val text = "4070281012A456789012"
        assertEquals(0, scanText(text, BankAccountLE), "Счет с буквами не должен быть найден")
    }

    @Test
    fun testBankAccountLEEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, BankAccountLE), "Пустая строка не должна содержать счета ЮЛ")
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
