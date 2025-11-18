package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера FullName
 */
internal class FullNameTest: MatcherTestBase(FullName) {

    @Test
    fun testFullNameAtStart() {
        val text = "Иванов Иван Иванович работает в компании"
        assertTrue(scanText(text) >= 1, "ФИО в начале должно быть найдено")
    }

    @Test
    fun testFullNameAtEnd() {
        val text = "Сотрудник: Иванов Иван Иванович"
        assertTrue(scanText(text) >= 1, "ФИО в конце должно быть найдено")
    }

    @Test
    fun testFullNameInMiddle() {
        val text = "Директор Петров Петр Петрович утвердил план"
        assertTrue(scanText(text) >= 1, "ФИО в середине должно быть найдено")
    }

    @Test
    fun testFullNameStandalone() {
        val text = "Сидоров Сидор Сидорович"
        assertTrue(scanText(text) >= 1, "ФИО отдельной строкой должно быть найдено")
    }

    @Test
    fun testFullNameMalePatronymic() {
        val text = """
            Иванов Иван Иванович
            Петров Петр Петрович
            Сидоров Алексей Александрович
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Мужские отчества на -ич должны быть найдены")
    }

    @Test
    fun testFullNameFemalePatronymic() {
        val text = """
            Иванова Мария Ивановна
            Петрова Елена Петровна
            Сидорова Анна Александровна
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Женские отчества на -на должны быть найдены")
    }

    @Test
    fun testFullNameFemaleSurname() {
        val text = """
            Иванова Мария Ивановна
            Петрова Елена Петровна
        """.trimIndent()
        assertTrue(scanText(text) >= 2, "Женские фамилии на -ова/-ева должны быть найдены")
    }

    @Test
    fun testFullNameMaleSurname() {
        val text = """
            Иванов Иван Иванович
            Петров Петр Петрович
        """.trimIndent()
        assertTrue(scanText(text) >= 2, "Мужские фамилии на -ов/-ев должны быть найдены")
    }

    @Test
    fun testFullNameWithSoftSign() {
        val text = "Кузнецов Игорь Игоревич"
        assertTrue(scanText(text) >= 1, "Отчество на мягкий знак должно быть найдено")
    }

    @Test
    fun testFullNameInParentheses() {
        val text = "(Иванов Иван Иванович)"
        assertTrue(scanText(text) >= 1, "ФИО в скобках должно быть найдено")
    }

    @Test
    fun testFullNameWithComma() {
        val text = "Сотрудник: Иванов Иван Иванович, работает"
        assertTrue(scanText(text) >= 1, "ФИО с запятой должно быть найдено")
    }

    @Test
    fun testFullNameWithPeriod() {
        val text = "Директор Иванов Иван Иванович."
        assertTrue(scanText(text) >= 1, "ФИО с точкой должно быть найдено")
    }

    @Test
    fun testFullNameWithDash() {
        val text = "ФИО: Иванов Иван Иванович-Смирнов"
        assertTrue(scanText(text) >= 1, "ФИО с дефисом должно быть найдено")
    }

    @Test
    fun testMultipleFullNames() {
        val text = """
            Иванов Иван Иванович
            Петров Петр Петрович
            Сидорова Анна Александровна
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько ФИО должны быть найдены")
    }

    @Test
    fun testFullNameNotAddress() {
        val text = "Республика Татарстан Город Казань"
        assertEquals(0, scanText(text), "Географические названия не должны быть найдены")
    }

    @Test
    fun testFullNameNotStreet() {
        val text = "Улица Ленина Проспект Мира"
        assertEquals(0, scanText(text), "Названия улиц не должны быть найдены")
    }

    @Test
    fun testFullNameCapitalized() {
        val text = "ИВАНОВ ИВАН ИВАНОВИЧ"
        assertTrue(scanText(text) >= 1, "ФИО заглавными буквами должно быть найдено")
    }

    @Test
    fun testFullNameLongSurname() {
        val text = "Александров Александр Александрович"
        assertTrue(scanText(text) >= 1, "ФИО с длинными словами должно быть найдено")
    }

    @Test
    fun testFullNameShortSurname() {
        val text = "Лис Лев Львович"
        assertTrue(scanText(text) >= 1, "ФИО с короткими словами должно быть найдено")
    }

    @Test
    fun testFullNameEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ФИО")
    }
}

