package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера SecurityAffiliation
 */
internal class SecurityAffiliationTest: MatcherTestBase(SecurityAffiliation) {

    @Test
    fun testSecurityAffiliationAtStart() {
        val text = "МВД России - основное место работы"
        assertTrue(scanText(text) >= 1, "Силовая структура в начале должна быть найдена")
    }

    @Test
    fun testSecurityAffiliationAtEnd() {
        val text = "Служит в ФСБ"
        assertTrue(scanText(text) >= 1, "Силовая структура в конце должна быть найдена")
    }

    @Test
    fun testSecurityAffiliationInMiddle() {
        val text = "Работал в МВД России с 2015 по 2020"
        assertTrue(scanText(text) >= 1, "Силовая структура в середине должна быть найдена")
    }

    @Test
    fun testSecurityAffiliationStandalone() {
        val text = "ФСБ"
        assertTrue(scanText(text) >= 1, "Силовая структура отдельной строкой должна быть найдена")
    }

    @Test
    fun testSecurityAffiliationAllAbbreviations() {
        val text = """
            МВД
            МО
            СВР
            ФСБ
            ФСО
        """.trimIndent()
        assertTrue(scanText(text) >= 5, "Все аббревиатуры силовых структур должны быть найдены")
    }

    @Test
    fun testSecurityAffiliationFullNames() {
        val text = """
            Министерство внутренних дел
            Министерство обороны
            Служба внешней разведки
            Федеральная служба безопасности
            Федеральная служба охраны
        """.trimIndent()
        assertTrue(scanText(text) >= 5, "Все полные названия силовых структур должны быть найдены")
    }

    @Test
    fun testSecurityAffiliationWithRF() {
        val text = """
            МВД РФ
            МВД России
            ФСБ РФ
            ФСБ России
        """.trimIndent()
        assertTrue(scanText(text) >= 4, "Силовые структуры с РФ/России должны быть найдены")
    }

    @Test
    fun testSecurityAffiliationWithContext() {
        val text = """
            сотрудник МВД
            служащий ФСБ
            работник МО
            офицер СВР
            агент ФСО
        """.trimIndent()
        assertTrue(scanText(text) >= 5, "Силовые структуры с контекстом должны быть найдены")
    }

    @Test
    fun testSecurityAffiliationMVD() {
        val text = """
            МВД
            Министерство внутренних дел
            МВД России
            Министерство внутренних дел РФ
        """.trimIndent()
        assertTrue(scanText(text) >= 4, "Все варианты МВД должны быть найдены")
    }

    @Test
    fun testSecurityAffiliationFSB() {
        val text = """
            ФСБ
            Федеральная служба безопасности
            ФСБ России
            Федеральная служба безопасности РФ
        """.trimIndent()
        assertTrue(scanText(text) >= 4, "Все варианты ФСБ должны быть найдены")
    }

    @Test
    fun testSecurityAffiliationSVR() {
        val text = """
            СВР
            Служба внешней разведки
            СВР России
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Все варианты СВР должны быть найдены")
    }

    @Test
    fun testSecurityAffiliationFSO() {
        val text = """
            ФСО
            Федеральная служба охраны
            ФСО России
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Все варианты ФСО должны быть найдены")
    }

    @Test
    fun testSecurityAffiliationMO() {
        val text = """
            МО
            Министерство обороны
            МО РФ
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Все варианты МО должны быть найдены")
    }

    @Test
    fun testSecurityAffiliationInParentheses() {
        val text = "Сотрудник (ФСБ) выполнил задание"
        assertTrue(scanText(text) >= 1, "Силовая структура в скобках должна быть найдена")
    }

    @Test
    fun testSecurityAffiliationInQuotes() {
        val text = "Служба: \"МВД\""
        assertTrue(scanText(text) >= 1, "Силовая структура в кавычках должна быть найдена")
    }

    @Test
    fun testSecurityAffiliationWithPunctuation() {
        val text = "Место службы: ФСБ."
        assertTrue(scanText(text) >= 1, "Силовая структура с точкой должна быть найдена")
    }

    @Test
    fun testSecurityAffiliationUpperCase() {
        val text = "МИНИСТЕРСТВО ВНУТРЕННИХ ДЕЛ"
        assertTrue(scanText(text) >= 1, "Силовая структура в верхнем регистре должна быть найдена")
    }

    @Test
    fun testSecurityAffiliationLowerCase() {
        val text = "министерство внутренних дел"
        assertTrue(scanText(text) >= 1, "Силовая структура в нижнем регистре должна быть найдена")
    }

    @Test
    fun testSecurityAffiliationEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать силовой структуры")
    }
}

