package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера SberBook
 */
internal class SberBookTest: MatcherTestBase(SberBook) {

    @Test
    fun testSberBookAtStart() {
        val text = "42300-810-1-2345-6789012 счет сберкнижки"
        assertTrue(scanText(text) >= 1, "Номер сберкнижки в начале должен быть найден")
    }

    @Test
    fun testSberBookAtEnd() {
        val text = "Номер счета: 42300-810-1-2345-6789012"
        assertTrue(scanText(text) >= 1, "Номер сберкнижки в конце должен быть найден")
    }

    @Test
    fun testSberBookInMiddle() {
        val text = "Счет 42300-810-1-2345-6789012 активен"
        assertTrue(scanText(text) >= 1, "Номер сберкнижки в середине должен быть найден")
    }

    @Test
    fun testSberBookStandalone() {
        val text = "42300-810-1-2345-6789012"
        assertTrue(scanText(text) >= 1, "Номер сберкнижки отдельной строкой должен быть найден")
    }

    @Test
    fun testSberBookWithoutDashes() {
        val text = "42300810123456789012"
        assertTrue(scanText(text) >= 1, "Номер сберкнижки без дефисов должен быть найден")
    }

    @Test
    fun testSberBookWithSpaces() {
        val text = "42300 810 1 2345 6789012"
        assertTrue(scanText(text) >= 1, "Номер сберкнижки с пробелами должен быть найден")
    }

    @Test
    fun testSberBookDifferentFormats() {
        val text = """
            42300-810-1-2345-6789012
            42300810123456789012
            42300 810 1 2345 6789012
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Разные форматы номера сберкнижки должны быть найдены")
    }

    @Test
    fun testSberBookWithLabel() {
        val text = "Номер сберкнижки: 42300-810-1-2345-6789012"
        assertTrue(scanText(text) >= 1, "Номер сберкнижки с меткой должен быть найден")
    }

    @Test
    fun testSberBookInParentheses() {
        val text = "Счет (42300-810-1-2345-6789012) активен"
        assertTrue(scanText(text) >= 1, "Номер сберкнижки в скобках должен быть найден")
    }

    @Test
    fun testSberBookInQuotes() {
        val text = "Счет \"42300-810-1-2345-6789012\" открыт"
        assertTrue(scanText(text) >= 1, "Номер сберкнижки в кавычках должен быть найден")
    }

    @Test
    fun testSberBookWithPunctuation() {
        val text = "Номер: 42300-810-1-2345-6789012."
        assertTrue(scanText(text) >= 1, "Номер сберкнижки с точкой должен быть найден")
    }

    @Test
    fun testSberBookMultiple() {
        val text = """
            Первый счет: 42300-810-1-2345-6789012
            Второй счет: 42301-810-1-9876-5432100
        """.trimIndent()
        assertTrue(scanText(text) >= 2, "Несколько номеров сберкнижек должны быть найдены")
    }

    @Test
    fun testSberBookStartsWith423() {
        val text = "42300123456789012345"
        assertTrue(scanText(text) >= 1, "Номер, начинающийся с 423, должен быть найден")
    }

    @Test
    fun testSberBookEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать номера сберкнижки")
    }
}

