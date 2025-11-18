package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера BankAccount
 */
internal class BankAccountTest: MatcherTestBase(BankAccount) {

    @Test
    fun testBankAccountAtStart() {
        val text = "40817810123456789012 банковский счет"
        assertTrue(scanText(text) >= 1, "Банковский счет в начале должен быть найден")
    }

    @Test
    fun testBankAccountAtEnd() {
        val text = "Расчетный счет ФЛ: 40817810123456789012"
        assertTrue(scanText(text) >= 1, "Банковский счет в конце должен быть найден")
    }

    @Test
    fun testBankAccountInMiddle() {
        val text = "Счет 40817810123456789012 открыт"
        assertTrue(scanText(text) >= 1, "Банковский счет в середине должен быть найден")
    }

    @Test
    fun testBankAccountStandalone() {
        val text = "40817810123456789012"
        assertTrue(scanText(text) >= 1, "Банковский счет отдельной строкой должен быть найден")
    }

    @Test
    fun testBankAccountWithPrefix408() {
        val text = "40817810123456789012"
        assertTrue(scanText(text) >= 1, "Счет с префиксом 408 должен быть найден")
    }

    @Test
    fun testBankAccountBoundary40800() {
        val text = "40800000000000000000"
        assertTrue(scanText(text) >= 1, "Счет 40800... должен быть найден")
    }

    @Test
    fun testBankAccountBoundary40899() {
        val text = "40899999999999999999"
        assertTrue(scanText(text) >= 1, "Счет 40899... должен быть найден")
    }

    @Test
    fun testBankAccountWithLabel() {
        val text = "номер банковского счета: 40817810123456789012"
        assertTrue(scanText(text) >= 1, "Счет с меткой должен быть найден")
    }

    @Test
    fun testBankAccountWithRS() {
        val text = "р/с: 40817810123456789012"
        assertTrue(scanText(text) >= 1, "Счет с 'р/с' должен быть найден")
    }

    @Test
    fun testBankAccountWithRaschetnyShet() {
        val text = "расчетный счет ФЛ: 40817810123456789012"
        assertTrue(scanText(text) >= 1, "Счет с 'расчетный счет ФЛ' должен быть найден")
    }

    @Test
    fun testBankAccountWithFizlitsa() {
        val text = "номер счета физлица: 40817810123456789012"
        assertTrue(scanText(text) >= 1, "Счет с 'физлица' должен быть найден")
    }

    @Test
    fun testBankAccountInParentheses() {
        val text = "(40817810123456789012)"
        assertTrue(scanText(text) >= 1, "Счет в скобках должен быть найден")
    }

    @Test
    fun testBankAccountInQuotes() {
        val text = "\"40817810123456789012\""
        assertTrue(scanText(text) >= 1, "Счет в кавычках должен быть найден")
    }

    @Test
    fun testBankAccountWithPunctuation() {
        val text = "Счет: 40817810123456789012."
        assertTrue(scanText(text) >= 1, "Счет с точкой должен быть найден")
    }

    @Test
    fun testBankAccountUpperCase() {
        val text = "Р/С: 40817810123456789012"
        assertTrue(scanText(text) >= 1, "Счет в верхнем регистре должен быть найден")
    }

    @Test
    fun testBankAccountLowerCase() {
        val text = "р/с: 40817810123456789012"
        assertTrue(scanText(text) >= 1, "Счет в нижнем регистре должен быть найден")
    }

    @Test
    fun testMultipleBankAccounts() {
        val text = """
            Первый: 40817810123456789012
            Второй: 40817810987654321098
            Третий: 40817810111111111111
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько счетов должны быть найдены")
    }

    @Test
    fun testBankAccountInvalidPrefix407() {
        val text = "40717810123456789012"
        assertEquals(0, scanText(text), "Счет с неверным префиксом не должен быть найден")
    }

    @Test
    fun testBankAccountInvalidPrefix409() {
        val text = "40917810123456789012"
        assertEquals(0, scanText(text), "Счет с неверным префиксом не должен быть найден")
    }

    @Test
    fun testBankAccountTooShort() {
        val text = "4081781012345678901"
        assertEquals(0, scanText(text), "Слишком короткий счет не должен быть найден")
    }

    @Test
    fun testBankAccountTooLong() {
        val text = "408178101234567890123"
        assertEquals(0, scanText(text), "Слишком длинный счет не должен быть найден")
    }

    @Test
    fun testBankAccountEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать банковского счета")
    }
}

