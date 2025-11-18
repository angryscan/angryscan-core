package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера VIN
 */
internal class VINTest: MatcherTestBase(VIN) {

    @Test
    fun testVINInvalidLetterI() {
        val text = "VIN: JF1SH92F4CG05382I"
        assertEquals(0, scanText(text), "VIN с буквой I не должен быть найден")
    }

    @Test
    fun testVINInvalidLetterO() {
        val text = "VIN: JF1SH92F4CG05382O"
        assertEquals(0, scanText(text), "VIN с буквой O не должен быть найден")
    }

    @Test
    fun testVINInvalidLetterQ() {
        val text = "VIN: JF1SH92F4CG05382Q"
        assertEquals(0, scanText(text), "VIN с буквой Q не должен быть найден")
    }

    @Test
    fun testVINTooShort() {
        val text = "VIN: JF1SH92F4CG05382"
        assertEquals(0, scanText(text), "Слишком короткий VIN не должен быть найден")
    }

    @Test
    fun testVINTooLong() {
        val text = "VIN: JF1SH92F4CG0538231"
        assertEquals(0, scanText(text), "Слишком длинный VIN не должен быть найден")
    }

    @Test
    fun testVINEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать VIN")
    }
}

