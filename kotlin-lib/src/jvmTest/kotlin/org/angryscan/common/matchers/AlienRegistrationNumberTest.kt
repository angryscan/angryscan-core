package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера Alien Registration Number
 */
internal class AlienRegistrationNumberTest : MatcherTestBase(AlienRegistrationNumber) {

    // Валидные A-Numbers для тестов (не последовательные, не повторяющиеся)
    private val valid7Digit = "A4827591" // 7 цифр
    private val valid8Digit = "A28475913" // 8 цифр
    private val valid9Digit = "A173849265" // 9 цифр
    private val validWithHyphen = "A-28475913" // С дефисом
    private val validWithSpace = "A 28475913" // С пробелом

    // ========== 1. Базовые тесты формата ==========

    @Test
    fun test7DigitFormat() {
        val text = "A4827591"
        assertTrue(scanText(text) >= 1, "A-Number с 7 цифрами должен быть найден")
    }

    @Test
    fun test8DigitFormat() {
        val text = "A28475913"
        assertTrue(scanText(text) >= 1, "A-Number с 8 цифрами должен быть найден")
    }

    @Test
    fun test9DigitFormat() {
        val text = "A173849265"
        assertTrue(scanText(text) >= 1, "A-Number с 9 цифрами должен быть найден")
    }

    @Test
    fun testWithHyphen() {
        val text = "A-28475913"
        assertTrue(scanText(text) >= 1, "A-Number с дефисом должен быть найден")
    }

    @Test
    fun testWithSpace() {
        val text = "A 28475913"
        assertTrue(scanText(text) >= 1, "A-Number с пробелом должен быть найден")
    }

    @Test
    fun testLowerCaseA() {
        val text = "a28475913"
        assertTrue(scanText(text) >= 1, "A-Number с маленькой буквой 'a' должен быть найден")
    }

    @Test
    fun testMixedCase() {
        val text = "A28475913"
        assertTrue(scanText(text) >= 1, "A-Number в смешанном регистре должен быть найден")
    }

    // ========== 2. Позиция совпадения в тексте ==========

    @Test
    fun testAtStartOfText() {
        val text = "A28475913"
        assertTrue(scanText(text) >= 1, "A-Number в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "A-Number: A28475913"
        assertTrue(scanText(text) >= 1, "A-Number в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The A-Number A28475913 is valid"
        assertTrue(scanText(text) >= 1, "A-Number в середине текста должен быть найден")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Number A28475913"
        assertTrue(scanText(text) >= 1, "A-Number с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "A28475913 is valid"
        assertTrue(scanText(text) >= 1, "A-Number с пробелом после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "A-Number: A28475913"
        assertTrue(scanText(text) >= 1, "A-Number с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithComma() {
        val text = "A-Number: A28475913, next"
        assertTrue(scanText(text) >= 1, "A-Number с запятой должен быть найден")
    }

    @Test
    fun testWithDot() {
        val text = "A-Number: A28475913."
        assertTrue(scanText(text) >= 1, "A-Number с точкой должен быть найден")
    }

    @Test
    fun testWithParentheses() {
        val text = "(A28475913)"
        assertTrue(scanText(text) >= 1, "A-Number в скобках должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nA28475913"
        assertTrue(scanText(text) >= 1, "A-Number после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "A28475913\n"
        assertTrue(scanText(text) >= 1, "A-Number перед \\n должен быть найден")
    }

    // ========== 3. Негативные сценарии ==========

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abcA28475913def"
        assertEquals(0, scanText(text), "A-Number внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123A28475913456"
        assertEquals(0, scanText(text), "A-Number внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать A-Number")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no A-Numbers"
        assertEquals(0, scanText(text), "Текст без A-Number не должен находить совпадения")
    }

    @Test
    fun testTooShort() {
        val text = "A123456" // 6 цифр вместо 7-9
        assertEquals(0, scanText(text), "Слишком короткий A-Number не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "A2847591390" // 10 цифр вместо 7-9
        assertEquals(0, scanText(text), "Слишком длинный A-Number не должен находиться")
    }

    @Test
    fun testLettersInDigits() {
        val text = "A4827591A" // Буква вместо цифры
        assertEquals(0, scanText(text), "A-Number с буквами в цифровой части не должен находиться")
    }

    @Test
    fun testNoLetterPrefix() {
        val text = "12345678" // Нет буквы A
        assertEquals(0, scanText(text), "A-Number без буквы A не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "A0000000" // Все нули в цифровой части
        assertEquals(0, scanText(text), "A-Number со всеми нулями не должен находиться")
    }

    @Test
    fun testAllSameDigits() {
        val text = "A1111111" // Все единицы
        assertEquals(0, scanText(text), "A-Number с одинаковыми цифрами не должен находиться")
    }

    @Test
    fun testOnlyA() {
        val text = "A" // Только буква A
        assertEquals(0, scanText(text), "Только буква A не должна находиться")
    }

    @Test
    fun testMultipleANumbers() {
        val text = "A4827591 and A82749531"
        assertTrue(scanText(text) >= 2, "Несколько A-Numbers должны быть найдены")
    }

    @Test
    fun testVariousSeparators() {
        val text = "A4827591 A-28475913 A 173849265"
        assertTrue(scanText(text) >= 3, "A-Numbers с разными разделителями должны быть найдены")
    }

    // ========== 4. Тесты дополнительной фильтрации для снижения false positives ==========

    @Test
    fun testSequentialAscending() {
        val text = "A1234567" // Последовательные цифры по возрастанию
        assertEquals(0, scanText(text), "Последовательные цифры по возрастанию должны быть отфильтрованы")
    }

    @Test
    fun testSequentialDescending() {
        val text = "A87654321" // Последовательные цифры по убыванию
        assertEquals(0, scanText(text), "Последовательные цифры по убыванию должны быть отфильтрованы")
    }

    @Test
    fun testTooFewUniqueDigits() {
        // A-Number с очень малым количеством уникальных цифр должны быть отфильтрованы
        val text1 = "A1111122" // Только 1 и 2
        assertEquals(0, scanText(text1), "A-Number с слишком малым количеством уникальных цифр должен быть отфильтрован")
        
        val text2 = "A8888999" // Только 8 и 9
        assertEquals(0, scanText(text2), "A-Number с слишком малым количеством уникальных цифр должен быть отфильтрован")
    }
}