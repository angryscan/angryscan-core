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
 * Тесты для проверки крайних позиций и пограничных значений матчера TemporaryID
 */
internal class TemporaryIDTest {

    @Test
    fun testTemporaryIDAtStart() {
        val text = " 123456789012 это ВУЛ"
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ в начале должно быть найдено")
    }

    @Test
    fun testTemporaryIDAtEnd() {
        val text = "Временное удостоверение личности: 123456789012 "
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ в конце должно быть найдено")
    }

    @Test
    fun testTemporaryIDInMiddle() {
        val text = "Гражданин с ВУЛ 123456789012 получил документ"
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ в середине должно быть найдено")
    }

    @Test
    fun testTemporaryIDStandalone() {
        val text = " 123456789012 "
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ отдельной строкой должно быть найдено")
    }

    @Test
    fun testTemporaryIDWithLabel() {
        val text = "временное удостоверение личности: 123456789012 "
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ с полной меткой должно быть найдено")
    }

    @Test
    fun testTemporaryIDWithVUL() {
        val text = "ВУЛ: 123456789012 "
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ с аббревиатурой должно быть найдено")
    }

    @Test
    fun testTemporaryIDBoundaryAllZeros() {
        val text = " 000000000000 "
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ из нулей должно быть найдено")
    }

    @Test
    fun testTemporaryIDBoundaryAllNines() {
        val text = " 999999999999 "
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ из девяток должно быть найдено")
    }

    @Test
    fun testTemporaryIDUpperCase() {
        val text = "ВУЛ: 123456789012 "
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ в верхнем регистре должно быть найдено")
    }

    @Test
    fun testTemporaryIDLowerCase() {
        val text = "вул: 123456789012 "
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ в нижнем регистре должно быть найдено")
    }

    @Test
    fun testTemporaryIDInParentheses() {
        val text = "(123456789012)"
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ в скобках должно быть найдено")
    }

    @Test
    fun testTemporaryIDInQuotes() {
        val text = "\"123456789012\""
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ в кавычках должно быть найдено")
    }

    @Test
    fun testTemporaryIDWithPunctuation() {
        val text = "ВУЛ: 123456789012."
        assertTrue(scanText(text, TemporaryID) >= 1, "ВУЛ с точкой должно быть найдено")
    }

    @Test
    fun testMultipleTemporaryIDs() {
        val text = """
            Первое: 123456789012
            Второе: 234567890123
            Третье: 345678901234
        """.trimIndent()
        assertTrue(scanText(text, TemporaryID) >= 3, "Несколько ВУЛ должны быть найдены")
    }

    @Test
    fun testTemporaryIDInvalidTooShort() {
        val text = " 12345678901 "
        assertEquals(0, scanText(text, TemporaryID), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testTemporaryIDInvalidTooLong() {
        val text = " 1234567890123 "
        assertEquals(0, scanText(text, TemporaryID), "Слишком длинный номер не должен быть найден")
    }

    @Test
    fun testTemporaryIDInvalidWithLetters() {
        val text = " 12345678901A "
        assertEquals(0, scanText(text, TemporaryID), "Номер с буквами не должен быть найден")
    }

    @Test
    fun testTemporaryIDEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, TemporaryID), "Пустая строка не должна содержать ВУЛ")
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

