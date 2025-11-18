package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера ExecDocNumber
 */
internal class ExecDocNumberTest: MatcherTestBase(ExecDocNumber) {

    @Test
    fun testExecDocNumberAtStart() {
        val text = " 12345/21/12345-ИП исполнительное производство"
        assertTrue(scanText(text) >= 1, "Номер ИП в начале должен быть найден")
    }

    @Test
    fun testExecDocNumberAtEnd() {
        val text = "Номер исполнительного производства: 12345/21/12345-ИП "
        assertTrue(scanText(text) >= 1, "Номер ИП в конце должен быть найден")
    }

    @Test
    fun testExecDocNumberInMiddle() {
        val text = "Возбуждено производство 12345/21/12345-ИП в суде"
        assertTrue(scanText(text) >= 1, "Номер ИП в середине должен быть найден")
    }

    @Test
    fun testExecDocNumberStandalone() {
        val text = " 12345/21/12345-ИП "
        assertTrue(scanText(text) >= 1, "Номер ИП отдельно должен быть найден")
    }

    @Test
    fun testExecDocNumberIP() {
        val text = " 12345/21/12345-ИП "
        assertTrue(scanText(text) >= 1, "Номер с суффиксом ИП должен быть найден")
    }

    @Test
    fun testExecDocNumberSV() {
        val text = " 12345/21/12345-СВ "
        assertTrue(scanText(text) >= 1, "Номер с суффиксом СВ должен быть найден")
    }

    @Test
    fun testExecDocNumberFS() {
        val text = " 12345/21/12345-ФС "
        assertTrue(scanText(text) >= 1, "Номер с суффиксом ФС должен быть найден")
    }

    @Test
    fun testExecDocNumberUD() {
        val text = " 1234/21/12345-УД "
        assertTrue(scanText(text) >= 1, "Номер с суффиксом УД должен быть найден")
    }

    @Test
    fun testExecDocNumberAP() {
        val text = " 12345/21/12345-АП "
        assertTrue(scanText(text) >= 1, "Номер с суффиксом АП должен быть найден")
    }

    @Test
    fun testExecDocNumberSD() {
        val text = " 12345/21/12345-СД "
        assertTrue(scanText(text) >= 1, "Номер с суффиксом СД должен быть найден")
    }

    @Test
    fun testExecDocNumberAllSuffixes() {
        val text = """
            12345/21/12345-ИП
            12345/21/12345-СВ
            12345/21/12345-ФС
            12345/21/12345-УД
            12345/21/12345-АП
            12345/21/12345-СД
            12345/21/12345-МС
            12345/21/12345-ПД
            12345/21/12345-АС
            12345/21/12345-ИД
        """.trimIndent()
        assertTrue(scanText(text) >= 10, "Все типы исполнительных документов должны быть найдены")
    }

    @Test
    fun testExecDocNumber4Digits() {
        val text = " 1234/21/12345-ИП "
        assertTrue(scanText(text) >= 1, "Номер с 4 цифрами в начале должен быть найден")
    }

    @Test
    fun testExecDocNumber5Digits() {
        val text = " 12345/21/12345-ИП "
        assertTrue(scanText(text) >= 1, "Номер с 5 цифрами в начале должен быть найден")
    }

    @Test
    fun testExecDocNumberWithNumberSign() {
        val text = " № 12345/21/12345-ИП "
        assertTrue(scanText(text) >= 1, "Номер с № должен быть найден")
    }

    @Test
    fun testExecDocNumberWithLabel() {
        val text = "номер исполнительного документа: 12345/21/12345-ИП "
        assertTrue(scanText(text) >= 1, "Номер с меткой 'документа' должен быть найден")
    }

    @Test
    fun testExecDocNumberWithProizvodstvo() {
        val text = "номер исполнительного производства: 12345/21/12345-ИП "
        assertTrue(scanText(text) >= 1, "Номер с меткой 'производства' должен быть найден")
    }

    @Test
    fun testExecDocNumberWithList() {
        val text = "исполнительный лист: 12345/21/12345-ИП "
        assertTrue(scanText(text) >= 1, "Исполнительный лист должен быть найден")
    }

    @Test
    fun testExecDocNumberUpperCase() {
        val text = "НОМЕР: 12345/21/12345-ИП "
        assertTrue(scanText(text) >= 1, "Номер в верхнем регистре должен быть найден")
    }

    @Test
    fun testExecDocNumberLowerCase() {
        val text = "номер: 12345/21/12345-ип "
        assertTrue(scanText(text) >= 1, "Номер в нижнем регистре должен быть найден")
    }

    @Test
    fun testExecDocNumberInParentheses() {
        val text = "(12345/21/12345-ИП)"
        assertTrue(scanText(text) >= 1, "Номер в скобках должен быть найден")
    }

    @Test
    fun testExecDocNumberInQuotes() {
        val text = "\"12345/21/12345-ИП\""
        assertTrue(scanText(text) >= 1, "Номер в кавычках должен быть найден")
    }

    @Test
    fun testExecDocNumberWithPunctuation() {
        val text = "Номер: 12345/21/12345-ИП."
        assertTrue(scanText(text) >= 1, "Номер с точкой должен быть найден")
    }

    @Test
    fun testMultipleExecDocNumbers() {
        val text = """
            Первое: 12345/21/12345-ИП
            Второе: 23456/22/23456-УД
            Третье: 34567/23/34567-ФС
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько номеров должны быть найдены")
    }

    @Test
    fun testExecDocNumberInvalidFormat() {
        val text = " 12345-21-12345-ИП "
        assertEquals(0, scanText(text), "Номер с неверным разделителем не должен быть найден")
    }

    @Test
    fun testExecDocNumberInvalidSuffix() {
        val text = " 12345/21/12345-ХХ "
        assertEquals(0, scanText(text), "Номер с неверным суффиксом не должен быть найден")
    }

    @Test
    fun testExecDocNumberInvalidTooShort() {
        val text = " 123/21/12345-ИП "
        assertEquals(0, scanText(text), "Номер со слишком коротким первым блоком не должен быть найден")
    }

    @Test
    fun testExecDocNumberInvalidTooLong() {
        val text = " 123456/21/12345-ИП "
        assertEquals(0, scanText(text), "Номер со слишком длинным первым блоком не должен быть найден")
    }

    @Test
    fun testExecDocNumberEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать номера исполнительного документа")
    }
}

