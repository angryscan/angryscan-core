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
 * Тесты для проверки крайних позиций и пограничных значений матчера VehicleRegNumber
 */
internal class VehicleRegNumberTest {

    @Test
    fun testVehicleRegNumberAtStart() {
        val text = " А123ВС77 регистрационный знак транспортного средства"
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Регистрационный знак в начале должен быть найден")
    }

    @Test
    fun testVehicleRegNumberAtEnd() {
        val text = "Регистрационный номер ТС: А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Регистрационный знак в конце должен быть найден")
    }

    @Test
    fun testVehicleRegNumberInMiddle() {
        val text = "Автомобиль с номером А123ВС77 зарегистрирован"
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Регистрационный знак в середине должен быть найден")
    }

    @Test
    fun testVehicleRegNumberStandalone() {
        val text = " А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Регистрационный знак отдельно должен быть найден")
    }

    @Test
    fun testVehicleRegNumberTypePrivate() {
        val text = " А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер легкового автомобиля должен быть найден")
    }

    @Test
    fun testVehicleRegNumberTypeTaxi() {
        val text = " 1234ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер такси должен быть найден")
    }

    @Test
    fun testVehicleRegNumberRegion77() {
        val text = " А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с регионом 77 (Москва) должен быть найден")
    }

    @Test
    fun testVehicleRegNumberRegion78() {
        val text = " А123ВС78 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с регионом 78 (СПб) должен быть найден")
    }

    @Test
    fun testVehicleRegNumberRegion177() {
        val text = " А123ВС177 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с 3-значным регионом 177 должен быть найден")
    }

    @Test
    fun testVehicleRegNumberRegion01() {
        val text = " А123ВС01 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с регионом 01 должен быть найден")
    }

    @Test
    fun testVehicleRegNumberRegion99() {
        val text = " А123ВС99 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с регионом 99 должен быть найден")
    }

    @Test
    fun testVehicleRegNumberWithoutSpaces() {
        val text = " А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер без пробелов должен быть найден")
    }

    @Test
    fun testVehicleRegNumberWithFullLabel() {
        val text = "регистрационный номер ТС: А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с полной меткой должен быть найден")
    }

    @Test
    fun testVehicleRegNumberWithGosNomer() {
        val text = "госномер: А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с меткой 'госномер' должен быть найден")
    }

    @Test
    fun testVehicleRegNumberWithNomerZnak() {
        val text = "номерной знак ТС: А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с меткой 'номерной знак' должен быть найден")
    }

    @Test
    fun testVehicleRegNumberWithAvtoNomer() {
        val text = "автомобильный номер: А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с меткой 'автомобильный номер' должен быть найден")
    }

    @Test
    fun testVehicleRegNumberWithRegZnak() {
        val text = "регистрационный знак транспортного средства: А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с полной меткой ТС должен быть найден")
    }

    @Test
    fun testVehicleRegNumberUpperCase() {
        val text = "ГОСНОМЕР: А123ВС77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер в верхнем регистре должен быть найден")
    }

    @Test
    fun testVehicleRegNumberLowerCase() {
        val text = "госномер: а123вс77 "
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер в нижнем регистре должен быть найден")
    }

    @Test
    fun testVehicleRegNumberInParentheses() {
        val text = "(А123ВС77)"
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер в скобках должен быть найден")
    }

    @Test
    fun testVehicleRegNumberInQuotes() {
        val text = "\"А123ВС77\""
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер в кавычках должен быть найден")
    }

    @Test
    fun testVehicleRegNumberWithPunctuation() {
        val text = "Номер: А123ВС77."
        assertTrue(scanText(text, VehicleRegNumber) >= 1, "Номер с точкой должен быть найден")
    }

    @Test
    fun testMultipleVehicleRegNumbers() {
        val text = """
            Первый: А123ВС77
            Второй: В456ЕК99
            Третий: 1234ТР177
        """.trimIndent()
        assertTrue(scanText(text, VehicleRegNumber) >= 3, "Несколько номеров должны быть найдены")
    }

    @Test
    fun testVehicleRegNumberDifferentLetters() {
        val text = """
             А123ВС77
             В234ЕК77
             О345РС77
             М456ТХ77
        """.trimIndent()
        assertTrue(scanText(text, VehicleRegNumber) >= 4, "Номера с разными буквами должны быть найдены")
    }

    @Test
    fun testVehicleRegNumberInvalidRegion00() {
        val text = " А123ВС00 "
        assertEquals(0, scanText(text, VehicleRegNumber), "Номер с неверным регионом 00 не должен быть найден")
    }

    @Test
    fun testVehicleRegNumberInvalidRegion100() {
        val text = " А123ВС100 "
        assertEquals(0, scanText(text, VehicleRegNumber), "Номер с неверным регионом 100 не должен быть найден")
    }

    @Test
    fun testVehicleRegNumberInvalidLetter() {
        val text = " Ф123ВС77 "
        assertEquals(0, scanText(text, VehicleRegNumber), "Номер с некорректной буквой не должен быть найден")
    }

    @Test
    fun testVehicleRegNumberEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, VehicleRegNumber), "Пустая строка не должна содержать регистрационного знака")
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
