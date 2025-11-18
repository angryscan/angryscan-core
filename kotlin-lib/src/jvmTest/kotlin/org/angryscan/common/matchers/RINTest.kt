package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера RIN
 */
internal class RINTest: MatcherTestBase(RIN) {

    @Test
    fun testRINAtStart() {
        val text = " 110101199001011234 китайский ID"
        assertTrue(scanText(text) >= 1, "RIN в начале должен быть найден")
    }

    @Test
    fun testRINAtEnd() {
        val text = " Chinese ID: 110101199001011234"
        assertTrue(scanText(text) >= 1, "RIN в конце должен быть найден")
    }

    @Test
    fun testRINInMiddle() {
        val text = "Гражданин с ID 110101199001011234 зарегистрирован"
        assertTrue(scanText(text) >= 1, "Китайский TIN в середине должен быть найден")
    }

    @Test
    fun testRINStandalone() {
        val text = " 110101199001011234 "
        assertTrue(scanText(text) >= 1, "Китайский TIN отдельно должен быть найден")
    }

    @Test
    fun testRIN18Digits() {
        val text = " 110101199001011234 "
        assertEquals(1, scanText(text), "Китайский TIN (18 цифр) должен быть найден")
    }

    @Test
    fun testRINWithX() {
        val text = " 11010119900101123X "
        assertEquals(1, scanText(text), "Китайский TIN с X должен быть найден")
    }

    @Test
    fun testRINWithLabel() {
        val text = " китайский идентификационный номер: 110101199001011234"
        assertTrue(scanText(text) >= 1, "Китайский TIN с меткой должен быть найден")
    }

    @Test
    fun testRINChinaLabel() {
        val text = " TIN China: 110101199001011234"
        assertTrue(scanText(text) >= 1, "TIN Китая с меткой должен быть найден")
    }

    @Test
    fun testRINUpperCase() {
        val text = " CHINESE ID: 110101199001011234"
        assertTrue(scanText(text) >= 1, "Китайский TIN в верхнем регистре должен быть найден")
    }

    @Test
    fun testRINLowerCase() {
        val text = " chinese id: 110101199001011234"
        assertTrue(scanText(text) >= 1, "Китайский TIN в нижнем регистре должен быть найден")
    }

    @Test
    fun testMultipleRINs() {
        val text = """
            Первый: 110101199001011234
            Второй: 11010119900101123X
            Третий: 110101198001011234
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько китайских TIN должны быть найдены")
    }

    @Test
    fun testRINInvalidDate() {
        val text = " 110101199913011234 "
        assertEquals(0, scanText(text), "Китайский TIN с некорректной датой не должен быть найден")
    }

    @Test
    fun testRINInvalidMonth() {
        val text = " 110101199000011234 "
        assertEquals(0, scanText(text), "Китайский TIN с некорректным месяцем не должен быть найден")
    }

    @Test
    fun testRINInvalidDay() {
        val text = " 110101199001321234 "
        assertEquals(0, scanText(text), "Китайский TIN с некорректным днем не должен быть найден")
    }

    @Test
    fun testRINInvalidChecksum() {
        val text = " 110101199001011235 "
        assertEquals(0, scanText(text), "Китайский TIN с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testRINEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать китайского TIN")
    }
}

