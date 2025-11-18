package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера MaritalStatus
 */
internal class MaritalStatusTest: MatcherTestBase(MaritalStatus) {

    @Test
    fun testMaritalStatusAtStart() {
        val text = "женат с 2020 года"
        assertEquals(1, scanText(text), "Семейное положение в начале должно быть найдено")
    }

    @Test
    fun testMaritalStatusAtEnd() {
        val text = "Иванов Иван Иванович женат"
        assertEquals(1, scanText(text), "Семейное положение в конце должно быть найдено")
    }

    @Test
    fun testMaritalStatusInMiddle() {
        val text = "Сотрудник женат и имеет двоих детей"
        assertEquals(1, scanText(text), "Семейное положение в середине должно быть найдено")
    }

    @Test
    fun testMaritalStatusStandalone() {
        val text = "холост"
        assertEquals(1, scanText(text), "Семейное положение отдельной строкой должно быть найдено")
    }

    @Test
    fun testMaritalStatusWithLabel() {
        val text = "Семейное положение: разведен"
        assertEquals(1, scanText(text), "Семейное положение с меткой должно быть найдено")
    }

    @Test
    fun testMaritalStatusAllVariants() {
        val text = """
            женат
            замужем
            холост
            разведен
            вдовец
            вдова
            не женат
            в браке
            состоит в браке
            гражданский брак
        """.trimIndent()
        assertTrue(scanText(text) >= 10, "Все варианты семейного положения должны быть найдены")
    }

    @Test
    fun testMaritalStatusInParentheses() {
        val text = "Петров И.И. (женат) работает в компании"
        assertEquals(1, scanText(text), "Семейное положение в скобках должно быть найдено")
    }

    @Test
    fun testMaritalStatusWithDash() {
        val text = "Семейное положение - холост"
        assertEquals(1, scanText(text), "Семейное положение с тире должно быть найдено")
    }

    @Test
    fun testMaritalStatusWithColon() {
        val text = "Статус: замужем"
        assertEquals(1, scanText(text), "Семейное положение с двоеточием должно быть найдено")
    }

    @Test
    fun testMaritalStatusCivilMarriage() {
        val text = "состоит в гражданском браке"
        assertEquals(1, scanText(text), "Гражданский брак должен быть найден")
    }

    @Test
    fun testMaritalStatusInQuotes() {
        val text = "Статус: \"женат\""
        assertEquals(1, scanText(text), "Семейное положение в кавычках должно быть найдено")
    }

    @Test
    fun testMaritalStatusWithPunctuation() {
        val text = "Статус: женат."
        assertEquals(1, scanText(text), "Семейное положение с точкой должно быть найдено")
    }

    @Test
    fun testMaritalStatusUpperCase() {
        val text = "ЖЕНАТ"
        assertEquals(1, scanText(text), "Семейное положение в верхнем регистре должно быть найдено")
    }

    @Test
    fun testMaritalStatusMixedCase() {
        val text = "ЖеНаТ"
        assertEquals(1, scanText(text), "Семейное положение в смешанном регистре должно быть найдено")
    }

    @Test
    fun testMaritalStatusWithNewlines() {
        val text = "Данные:\nженат\nс 2020"
        assertEquals(1, scanText(text), "Семейное положение с переносами строк должно быть найдено")
    }

    @Test
    fun testMaritalStatusEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать семейного положения")
    }
}

