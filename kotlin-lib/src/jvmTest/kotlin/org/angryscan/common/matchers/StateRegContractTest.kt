package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера StateRegContract
 */
internal class StateRegContractTest: MatcherTestBase(StateRegContract) {

    @Test
    fun testStateRegContractAtStart() {
        val text = " 77-12-34/123/2020-456 номер регистрации"
        assertTrue(scanText(text) >= 1, "Номер регистрации в начале должен быть найден")
    }

    @Test
    fun testStateRegContractAtEnd() {
        val text = "Номер государственной регистрации договора: 77-12-34/123/2020-456 "
        assertTrue(scanText(text) >= 1, "Номер регистрации в конце должен быть найден")
    }

    @Test
    fun testStateRegContractInMiddle() {
        val text = "Договор с номером 77-12-34/123/2020-456 зарегистрирован"
        assertTrue(scanText(text) >= 1, "Номер регистрации в середине должен быть найден")
    }

    @Test
    fun testStateRegContractStandalone() {
        val text = " 77-12-34/123/2020-456 "
        assertTrue(scanText(text) >= 1, "Номер регистрации отдельно должен быть найден")
    }

    @Test
    fun testStateRegContractWith3DigitCode() {
        val text = " 77-12-34/123/2020-456 "
        assertTrue(scanText(text) >= 1, "Номер с 3-значным кодом должен быть найден")
    }

    @Test
    fun testStateRegContractWith4DigitCode() {
        val text = " 77-12-34/1234/2020-456 "
        assertTrue(scanText(text) >= 1, "Номер с 4-значным кодом должен быть найден")
    }

    @Test
    fun testStateRegContractWith1DigitEnd() {
        val text = " 77-12-34/123/2020-1 "
        assertTrue(scanText(text) >= 1, "Номер с 1 цифрой в конце должен быть найден")
    }

    @Test
    fun testStateRegContractWith3DigitsEnd() {
        val text = " 77-12-34/123/2020-456 "
        assertTrue(scanText(text) >= 1, "Номер с 3 цифрами в конце должен быть найден")
    }

    @Test
    fun testStateRegContractWithSpaces() {
        val text = " 77 - 12 - 34 / 123 / 2020 - 456 "
        assertTrue(scanText(text) >= 1, "Номер с пробелами должен быть найден")
    }

    @Test
    fun testStateRegContractWithColons() {
        val text = " 77:12:34/123/2020:456 "
        assertTrue(scanText(text) >= 1, "Номер с двоеточиями должен быть найден")
    }

    @Test
    fun testStateRegContractWithFullLabel() {
        val text = "номер государственной регистрации договора: 77-12-34/123/2020-456 "
        assertTrue(scanText(text) >= 1, "Номер с полной меткой должен быть найден")
    }

    @Test
    fun testStateRegContractWithRosreestr() {
        val text = "номер регистрации договора в Росреестре: 77-12-34/123/2020-456 "
        assertTrue(scanText(text) >= 1, "Номер с меткой Росреестра должен быть найден")
    }

    @Test
    fun testStateRegContractWithFL() {
        val text = "госрегистрационный номер договора с ФЛ: 77-12-34/123/2020-456 "
        assertTrue(scanText(text) >= 1, "Номер договора с ФЛ должен быть найден")
    }

    @Test
    fun testStateRegContractUpperCase() {
        val text = "НОМЕР: 77-12-34/123/2020-456 "
        assertTrue(scanText(text) >= 1, "Номер в верхнем регистре должен быть найден")
    }

    @Test
    fun testStateRegContractLowerCase() {
        val text = "номер: 77-12-34/123/2020-456 "
        assertTrue(scanText(text) >= 1, "Номер в нижнем регистре должен быть найден")
    }

    @Test
    fun testMultipleStateRegContracts() {
        val text = """
            Первый: 77-12-34/123/2020-456
            Второй: 50-23-45/234/2021-567
            Третий: 78-34-56/345/2022-678
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько номеров должны быть найдены")
    }

    @Test
    fun testStateRegContractInvalidFormat() {
        val text = " 77/12/34-123-2020-456 "
        assertEquals(0, scanText(text), "Номер с неверным форматом не должен быть найден")
    }

    @Test
    fun testStateRegContractEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать номера регистрации")
    }
}

