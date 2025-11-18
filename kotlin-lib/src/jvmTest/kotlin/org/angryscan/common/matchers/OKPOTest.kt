package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера OKPO
 */
internal class OKPOTest: MatcherTestBase(OKPO) {

    @Test
    fun testOKPOAtStart() {
        val text = " 00032537 это ОКПО"
        assertEquals(1, scanText(text), "ОКПО в начале должен быть найден")
    }

    @Test
    fun testOKPOAtEnd() {
        val text = " : 00032537"
        assertEquals(1, scanText(text), "ОКПО в конце должен быть найден")
    }

    @Test
    fun testOKPOInMiddle() {
        val text = "Организация с (00032537) зарегистрирована"
        assertEquals(1, scanText(text), "ОКПО в середине должен быть найден")
    }

    @Test
    fun testOKPOStandalone() {
        val text = " 00032537 "
        assertEquals(1, scanText(text), "ОКПО отдельной строкой должен быть найден")
    }

    @Test
    fun testOKPOWithLabel() {
        val text = "код (00032537) "
        assertEquals(1, scanText(text), "ОКПО с меткой 'код ОКПО' должен быть найден")
    }

    @Test
    fun testOKPOWithOKPOYuL() {
        val text = " : 00032537 "
        assertEquals(1, scanText(text), "ОКПО с меткой 'ОКПО ЮЛ' должен быть найден")
    }

    @Test
    fun testOKPOWithOrganization() {
        val text = " : 00032537 "
        assertEquals(1, scanText(text), "ОКПО с меткой 'организации' должен быть найден")
    }

    @Test
    fun testOKPOUpperCase() {
        val text = " : 00032537 "
        assertEquals(1, scanText(text), "ОКПО в верхнем регистре должен быть найден")
    }

    @Test
    fun testOKPOLowerCase() {
        val text = " ( 00032537) "
        assertEquals(1, scanText(text), "ОКПО в нижнем регистре должен быть найден")
    }

    @Test
    fun testOKPOMixedCase() {
        val text = " : 00032537 "
        assertEquals(1, scanText(text), "ОКПО в смешанном регистре должен быть найден")
    }

    @Test
    fun testOKPOInParentheses() {
        val text = "(00032537) "
        assertEquals(1, scanText(text), "ОКПО в скобках должен быть найден")
    }

    @Test
    fun testOKPOInQuotes() {
        val text = "\"00032537\" "
        assertEquals(1, scanText(text), "ОКПО в кавычках должен быть найден")
    }

    @Test
    fun testOKPOWithPunctuation() {
        val text = " : 00032537. "
        assertEquals(1, scanText(text), "ОКПО с точкой должен быть найден")
    }

    @Test
    fun testMultipleOKPO() {
        val text = """
            ( 00032537) 
            ( 98765438) 
            ( 99999994) 
        """.trimIndent()
        assertEquals(3, scanText(text), "Несколько ОКПО должны быть найдены")
    }

    @Test
    fun testOKPOInvalidChecksum() {
        val text = " 00032536 "
        assertEquals(0, scanText(text), "ОКПО с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testOKPOTooShort() {
        val text = "0003253"
        assertEquals(0, scanText(text), "Слишком короткий ОКПО не должен быть найден")
    }

    @Test
    fun testOKPOTooLong() {
        val text = "000325370"
        assertEquals(0, scanText(text), "Слишком длинный ОКПО не должен быть найден")
    }

    @Test
    fun testOKPOWithLetters() {
        val text = "0003253A"
        assertEquals(0, scanText(text), "ОКПО с буквами не должен быть найден")
    }

    @Test
    fun testOKPOEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ОКПО")
    }
}
