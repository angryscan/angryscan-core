package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера INN
 */
internal class INNTest: MatcherTestBase(INN) {

    @Test
    fun testINNAtStart() {
        val text = "526317984689 это ИНН физического лица"
        assertEquals(1, scanText(text), "ИНН в начале должен быть найден")
    }

    @Test
    fun testINNAtEnd() {
        val text = "ИНН физического лица: 526317984689"
        assertEquals(1, scanText(text), "ИНН в конце должен быть найден")
    }

    @Test
    fun testINNInMiddle() {
        val text = "Гражданин имеет ИНН 526317984689 для налоговых целей"
        assertEquals(1, scanText(text), "ИНН в середине должен быть найден")
    }

    @Test
    fun testINNStandalone() {
        val text = "526317984689"
        assertEquals(1, scanText(text), "ИНН отдельной строкой должен быть найден")
    }

    @Test
    fun testINNWithSpacesFormat1() {
        val text = "52 63 179846 89"
        assertEquals(1, scanText(text), "ИНН с пробелами формат 1 должен быть найден")
    }

    @Test
    fun testINNWithSpacesFormat2() {
        val text = "5263 17984689"
        assertEquals(1, scanText(text), "ИНН с пробелами формат 2 должен быть найден")
    }

    @Test
    fun testINNBoundary000() {
        val text = "000000000000"
        assertEquals(0, scanText(text), "ИНН из нулей не должен быть найден (неверная контрольная сумма)")
    }

    @Test
    fun testINNInParentheses() {
        val text = "ИНН (526317984689) указан"
        assertEquals(1, scanText(text), "ИНН в скобках должен быть найден")
    }

    @Test
    fun testINNInQuotes() {
        val text = "ИНН \"526317984689\" проверен"
        assertEquals(1, scanText(text), "ИНН в кавычках должен быть найден")
    }

    @Test
    fun testINNWithPunctuation() {
        val text = "ИНН: 526317984689."
        assertEquals(1, scanText(text), "ИНН с точкой должен быть найден")
    }

    @Test
    fun testINNWithColon() {
        val text = "ИНН:526317984689"
        assertEquals(1, scanText(text), "ИНН с двоеточием должен быть найден")
    }

    @Test
    fun testINNWithDash() {
        val text = "ИНН-526317984689"
        assertEquals(1, scanText(text), "ИНН с дефисом должен быть найден")
    }

    @Test
    fun testMultipleINNs() {
        val text = """
            Первый: 526317984689
            Второй: 500100732259
        """.trimIndent()
        assertEquals(2, scanText(text), "Несколько ИНН должны быть найдены")
    }

    @Test
    fun testINNInvalidChecksum() {
        val text = "526317984688"
        assertEquals(0, scanText(text), "ИНН с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testINNTooShort() {
        val text = "52631798468"
        assertEquals(0, scanText(text), "Слишком короткий ИНН не должен быть найден")
    }

    @Test
    fun testINNTooLong() {
        val text = "5263179846891"
        assertEquals(0, scanText(text), "Слишком длинный ИНН не должен быть найден")
    }

    @Test
    fun testINNInTable() {
        val text = "| ИНН | 526317984689 |"
        assertEquals(1, scanText(text), "ИНН в таблице должен быть найден")
    }

    @Test
    fun testINNEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ИНН")
    }
}

