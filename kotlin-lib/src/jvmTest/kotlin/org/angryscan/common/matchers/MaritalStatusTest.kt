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
 * Тесты для проверки крайних позиций и пограничных значений матчера MaritalStatus
 */
internal class MaritalStatusTest {

    @Test
    fun testMaritalStatusAtStart() {
        val text = "женат с 2020 года"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение в начале должно быть найдено")
    }

    @Test
    fun testMaritalStatusAtEnd() {
        val text = "Иванов Иван Иванович женат"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение в конце должно быть найдено")
    }

    @Test
    fun testMaritalStatusInMiddle() {
        val text = "Сотрудник женат и имеет двоих детей"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение в середине должно быть найдено")
    }

    @Test
    fun testMaritalStatusStandalone() {
        val text = "холост"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение отдельной строкой должно быть найдено")
    }

    @Test
    fun testMaritalStatusWithLabel() {
        val text = "Семейное положение: разведен"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение с меткой должно быть найдено")
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
        assertTrue(scanText(text, MaritalStatus) >= 10, "Все варианты семейного положения должны быть найдены")
    }

    @Test
    fun testMaritalStatusInParentheses() {
        val text = "Петров И.И. (женат) работает в компании"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение в скобках должно быть найдено")
    }

    @Test
    fun testMaritalStatusWithDash() {
        val text = "Семейное положение - холост"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение с тире должно быть найдено")
    }

    @Test
    fun testMaritalStatusWithColon() {
        val text = "Статус: замужем"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение с двоеточием должно быть найдено")
    }

    @Test
    fun testMaritalStatusFemaleVariants() {
        val text = """
            замужем
            не замужем
            разведена
            вдова
            одинокая
        """.trimIndent()
        assertEquals(5, scanText(text, MaritalStatus), "Женские варианты семейного положения должны быть найдены")
    }

    @Test
    fun testMaritalStatusMaleVariants() {
        val text = """
            женат
            не женат
            холостой
            разведен
            вдовец
            одинок
        """.trimIndent()
        assertEquals(6, scanText(text, MaritalStatus), "Мужские варианты семейного положения должны быть найдены")
    }

    @Test
    fun testMaritalStatusNotMarried() {
        val text = "не состоит в браке"
        assertEquals(1, scanText(text, MaritalStatus), "Фраза 'не состоит в браке' должна быть найдена")
    }

    @Test
    fun testMaritalStatusCivilMarriage() {
        val text = "состоит в гражданском браке"
        assertEquals(1, scanText(text, MaritalStatus), "Гражданский брак должен быть найден")
    }

    @Test
    fun testMaritalStatusInQuotes() {
        val text = "Статус: \"женат\""
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение в кавычках должно быть найдено")
    }

    @Test
    fun testMaritalStatusWithPunctuation() {
        val text = "Статус: женат."
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение с точкой должно быть найдено")
    }

    @Test
    fun testMaritalStatusUpperCase() {
        val text = "ЖЕНАТ"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение в верхнем регистре должно быть найдено")
    }

    @Test
    fun testMaritalStatusMixedCase() {
        val text = "ЖеНаТ"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение в смешанном регистре должно быть найдено")
    }

    @Test
    fun testMaritalStatusWithNewlines() {
        val text = "Данные:\nженат\nс 2020"
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение с переносами строк должно быть найдено")
    }

    @Test
    fun testMaritalStatusEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, MaritalStatus), "Пустая строка не должна содержать семейного положения")
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

