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
 * Тесты для проверки крайних позиций и пограничных значений матчера SberBook
 */
internal class SberBookTest {

    @Test
    fun testSberBookAtStart() {
        val text = "40817-810-1-2345-6789012 счет сберкнижки"
        assertTrue(scanText(text, SberBook) >= 1, "Номер сберкнижки в начале должен быть найден")
    }

    @Test
    fun testSberBookAtEnd() {
        val text = "Номер счета: 40817-810-1-2345-6789012"
        assertTrue(scanText(text, SberBook) >= 1, "Номер сберкнижки в конце должен быть найден")
    }

    @Test
    fun testSberBookInMiddle() {
        val text = "Счет 40817-810-1-2345-6789012 активен"
        assertTrue(scanText(text, SberBook) >= 1, "Номер сберкнижки в середине должен быть найден")
    }

    @Test
    fun testSberBookStandalone() {
        val text = "40817-810-1-2345-6789012"
        assertTrue(scanText(text, SberBook) >= 1, "Номер сберкнижки отдельной строкой должен быть найден")
    }

    @Test
    fun testSberBookWithoutDashes() {
        val text = "40817810123456789012"
        assertTrue(scanText(text, SberBook) >= 1, "Номер сберкнижки без дефисов должен быть найден")
    }

    @Test
    fun testSberBookWithSpaces() {
        val text = "40817 810 1 2345 6789012"
        assertTrue(scanText(text, SberBook) >= 1, "Номер сберкнижки с пробелами должен быть найден")
    }

    @Test
    fun testSberBookDifferentFormats() {
        val text = """
            40817-810-1-2345-6789012
            40817810123456789012
            40817 810 1 2345 6789012
        """.trimIndent()
        assertTrue(scanText(text, SberBook) >= 3, "Разные форматы номера сберкнижки должны быть найдены")
    }

    @Test
    fun testSberBookWithLabel() {
        val text = "Номер сберкнижки: 40817-810-1-2345-6789012"
        assertTrue(scanText(text, SberBook) >= 1, "Номер сберкнижки с меткой должен быть найден")
    }

    @Test
    fun testSberBookInParentheses() {
        val text = "Счет (40817-810-1-2345-6789012) активен"
        assertTrue(scanText(text, SberBook) >= 1, "Номер сберкнижки в скобках должен быть найден")
    }

    @Test
    fun testSberBookInQuotes() {
        val text = "Счет \"40817-810-1-2345-6789012\" открыт"
        assertTrue(scanText(text, SberBook) >= 1, "Номер сберкнижки в кавычках должен быть найден")
    }

    @Test
    fun testSberBookWithPunctuation() {
        val text = "Номер: 40817-810-1-2345-6789012."
        assertTrue(scanText(text, SberBook) >= 1, "Номер сберкнижки с точкой должен быть найден")
    }

    @Test
    fun testSberBookMultiple() {
        val text = """
            Первый счет: 40817-810-1-2345-6789012
            Второй счет: 40817-810-1-9876-5432100
        """.trimIndent()
        assertTrue(scanText(text, SberBook) >= 2, "Несколько номеров сберкнижек должны быть найдены")
    }

    @Test
    fun testSberBookStartsWith40817() {
        val text = "40817123456789012345"
        assertTrue(scanText(text, SberBook) >= 1, "Номер, начинающийся с 40817, должен быть найден")
    }

    @Test
    fun testSberBookEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, SberBook), "Пустая строка не должна содержать номера сберкнижки")
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

