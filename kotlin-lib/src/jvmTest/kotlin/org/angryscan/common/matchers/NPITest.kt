package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера NPI
 */
internal class NPITest : MatcherTestBase(NPI) {

    // Валидные NPI номера для тестов (с правильной контрольной суммой и не отфильтровываемые)
    private val validNPI1 = "1428579639" // Валидный NPI с правильной чексуммой
    private val validNPI2 = "1472935173" // Валидный NPI с правильной чексуммой
    private val validNPI3 = "1738492653" // Валидный NPI с правильной чексуммой
    private val validNPI4 = "1284761938" // Валидный NPI с правильной чексуммой (начинается с 1)
    private val validNPI2x = "2838492651" // Валидный NPI с правильной чексуммой (начинается с 2)

    // ========== 1. Базовые тесты формата ==========

    @Test
    fun testNPIFormat() {
        val text = "1428579639"
        assertTrue(scanText(text) >= 1, "NPI должен быть найден")
    }

    @Test
    fun testNPIStartsWith1() {
        val text = "1472935173"
        assertTrue(scanText(text) >= 1, "NPI начинающийся с 1 должен быть найден")
    }

    @Test
    fun testNPIStartsWith2() {
        val text = "2384926517"
        assertTrue(scanText(text) >= 1, "NPI начинающийся с 2 должен быть найден")
    }

    // ========== 2. Позиция совпадения в тексте ==========

    @Test
    fun testAtStartOfText() {
        val text = "1428579639"
        assertTrue(scanText(text) >= 1, "NPI в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "NPI: 1428579639"
        assertTrue(scanText(text) >= 1, "NPI в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The NPI 1428579639 is valid"
        assertTrue(scanText(text) >= 1, "NPI в середине текста должен быть найден")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "NPI 1428579639"
        assertTrue(scanText(text) >= 1, "NPI с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "1428579639 is valid"
        assertTrue(scanText(text) >= 1, "NPI с пробелом после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "NPI: 1428579639"
        assertTrue(scanText(text) >= 1, "NPI с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithComma() {
        val text = "NPI: 1428579639, next"
        assertTrue(scanText(text) >= 1, "NPI с запятой должен быть найден")
    }

    @Test
    fun testWithDot() {
        val text = "NPI: 1428579639."
        assertTrue(scanText(text) >= 1, "NPI с точкой должен быть найден")
    }

    @Test
    fun testWithParentheses() {
        val text = "(1428579639)"
        assertTrue(scanText(text) >= 1, "NPI в скобках должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n1428579639"
        assertTrue(scanText(text) >= 1, "NPI после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "1428579639\n"
        assertTrue(scanText(text) >= 1, "NPI перед \\n должен быть найден")
    }

    // ========== 3. Негативные сценарии ==========

    @Test
    fun testNotInsideNumericSequence() {
        val text = "1231428579639456"
        assertEquals(0, scanText(text), "NPI внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать NPI")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no NPI numbers"
        assertEquals(0, scanText(text), "Текст без NPI не должен находить совпадения")
    }

    @Test
    fun testWrongFirstDigit() {
        val text = "3123456789" // Начинается с 3 вместо 1 или 2
        assertEquals(0, scanText(text), "NPI начинающийся с 3 не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "123456789" // 9 цифр вместо 10
        assertEquals(0, scanText(text), "Слишком короткий NPI не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "12345678901" // 11 цифр вместо 10
        assertEquals(0, scanText(text), "Слишком длинный NPI не должен находиться")
    }

    @Test
    fun testInvalidChecksum() {
        val text = "1234567890" // Неправильная контрольная сумма
        assertEquals(0, scanText(text), "NPI с неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "1000000000" // Все нули (кроме первой цифры) - должен быть отфильтрован
        // Этот номер может быть валидным по формату, но будет отфильтрован как фейковый паттерн
        assertEquals(0, scanText(text), "NPI с нулями должен быть отфильтрован")
    }

    @Test
    fun testAllSameDigits() {
        val text = "1111111111" // Все единицы (но неправильная контрольная сумма)
        assertEquals(0, scanText(text), "NPI с одинаковыми цифрами и неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testMultipleNPIs() {
        val text = "NPI1: 1428579639, NPI2: 2384926517"
        assertTrue(scanText(text) >= 2, "Несколько NPI должны быть найдены")
    }

    // ========== 4. Тесты дополнительной фильтрации для снижения false positives ==========

    @Test
    fun testSequentialAscending() {
        // "1234567895" имеет правильную чексумму, но последовательные цифры должны быть отфильтрованы
        val text = "1234567895"
        assertEquals(0, scanText(text), "Последовательные цифры по возрастанию должны быть отфильтрованы")
    }

    @Test
    fun testSequentialDescending() {
        // "9876543215" имеет правильную чексумму, но последовательные цифры должны быть отфильтрованы
        val text = "9876543215"
        assertEquals(0, scanText(text), "Последовательные цифры по убыванию должны быть отфильтрованы")
    }

    @Test
    fun testTooFewUniqueDigits() {
        // NPI с очень малым количеством уникальных цифр должны быть отфильтрованы
        // "1000000001" или "2000000000" должны быть отфильтрованы
        val text1 = "1000000001"
        assertEquals(0, scanText(text1), "NPI с слишком малым количеством уникальных цифр должен быть отфильтрован")
        
        val text2 = "2000000000"
        assertEquals(0, scanText(text2), "NPI с слишком малым количеством уникальных цифр должен быть отфильтрован")
    }
}