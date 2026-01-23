package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера DOD ID / EDIPI
 */
internal class DODIDTest : MatcherTestBase(DODID) {

    // Валидные DOD IDs для тестов
    private val validDODID1 = "1234567890" // 10 цифр
    private val validDODID2 = "0001234567" // С ведущими нулями
    private val validDODID3 = "9876543210" // Обратный порядок

    // ========== 1. Базовые тесты формата = ключевыми словами ==========

    @Test
    fun testWithDODIDKeyword() {
        val text = "DOD ID: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID с ключевым словом 'DOD ID' должен быть найден")
    }

    @Test
    fun testWithEDIPIKeyword() {
        val text = "EDIPI: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID с ключевым словом 'EDIPI' должен быть найден")
    }

    @Test
    fun testWithDODIDKeywordNoSpace() {
        val text = "DODID 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID с ключевым словом 'DODID' должен быть найден")
    }

    @Test
    fun testWithKeywordAndSeparator() {
        val text = "DOD ID - 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID с разделителем должен быть найден")
    }

    @Test
    fun testWithKeywordAndColon() {
        val text = "DOD ID: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID с двоеточием должен быть найден")
    }

    @Test
    fun testLowerCaseKeywords() {
        val text = "dod id: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID с ключевыми словами в нижнем регистре должен быть найден")
    }

    @Test
    fun testWithEDIPILowercase() {
        val text = "edipi: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID с 'edipi' в нижнем регистре должен быть найден")
    }

    // ========== 2. Позиция совпадения в тексте ==========

    @Test
    fun testAtStartOfText() {
        val text = "DOD ID: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "My DOD ID: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The DOD ID 1234567890 is valid"
        assertTrue(scanText(text) >= 1, "DOD ID в середине текста должен быть найден")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "My DOD ID: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "DOD ID: 1234567890 is valid"
        assertTrue(scanText(text) >= 1, "DOD ID с пробелом после должен быть найден")
    }

    @Test
    fun testWithComma() {
        val text = "DOD ID: 1234567890, next"
        assertTrue(scanText(text) >= 1, "DOD ID с запятой должен быть найден")
    }

    @Test
    fun testWithDot() {
        val text = "DOD ID: 1234567890."
        assertTrue(scanText(text) >= 1, "DOD ID с точкой должен быть найден")
    }

    @Test
    fun testWithParentheses() {
        val text = "(DOD ID: 1234567890)"
        assertTrue(scanText(text) >= 1, "DOD ID в скобках должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nDOD ID: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "DOD ID: 1234567890\n"
        assertTrue(scanText(text) >= 1, "DOD ID перед \\n должен быть найден")
    }

    // ========== 3. Негативные сценарии ==========

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc1234567890def"
        assertEquals(0, scanText(text), "DOD ID внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "1231234567890456"
        assertEquals(0, scanText(text), "DOD ID внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать DOD ID")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no DOD IDs"
        assertEquals(0, scanText(text), "Текст без DOD ID не должен находить совпадения")
    }

    @Test
    fun testTooShort() {
        val text = "DOD ID: 123456789" // 9 цифр вместо 10
        assertEquals(0, scanText(text), "Слишком короткий DOD ID не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "DOD ID: 12345678901" // 11 цифр вместо 10
        assertEquals(0, scanText(text), "Слишком длинный DOD ID не должен находиться")
    }

    @Test
    fun testLettersInDigits() {
        val text = "DOD ID: 123456789A" // Буква вместо цифры
        assertEquals(0, scanText(text), "DOD ID с буквами не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "DOD ID: 0000000000" // Все нули
        assertEquals(0, scanText(text), "DOD ID со всеми нулями не должен находиться")
    }

    @Test
    fun testAllSameDigits() {
        val text = "DOD ID: 1111111111" // Все единицы
        assertEquals(0, scanText(text), "DOD ID с одинаковыми цифрами не должен находиться")
    }

    @Test
    fun testMultipleDODIDs() {
        val text = "DOD ID: 1234567890 and EDIPI: 9876543210"
        assertTrue(scanText(text) >= 2, "Несколько DOD IDs должны быть найдены")
    }

    @Test
    fun testOnCACCard() {
        val text = "CAC Card DOD ID: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID на CAC карте должен быть найден")
    }

    @Test
    fun testWithMultipleSeparators() {
        val text = "DOD ID - No.: 1234567890"
        assertTrue(scanText(text) >= 1, "DOD ID с несколькими разделителями должен быть найден")
    }
}