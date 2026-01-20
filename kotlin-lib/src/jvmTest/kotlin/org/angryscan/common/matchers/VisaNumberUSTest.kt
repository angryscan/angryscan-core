package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера U.S. Visa Number
 */
internal class VisaNumberUSTest : MatcherTestBase(VisaNumberUS) {

    // Валидные Visa Numbers для тестов (не последовательные, не повторяющиеся, не даты)
    private val valid8Digits = "41758293" // 8 цифр
    private val validLetter7Digits = "A7258419" // Буква + 7 цифр

    // ========== 1. Базовые тесты формата ==========

    @Test
    fun test8DigitFormat() {
        val text = "41758293"
        assertTrue(scanText(text) >= 1, "Visa Number с 8 цифрами должен быть найден")
    }

    @Test
    fun testLetter7DigitFormat() {
        val text = "A7258419"
        assertTrue(scanText(text) >= 1, "Visa Number с буквой + 7 цифрами должен быть найден")
    }

    @Test
    fun testLowerCaseLetter() {
        val text = "a7258419"
        assertTrue(scanText(text) >= 1, "Visa Number с маленькой буквой должен быть найден")
    }

    @Test
    fun testVariousLetters() {
        val text1 = "B4827591"
        val text2 = "Z1593746"
        assertTrue(scanText(text1) >= 1, "Visa Number с буквой B должен быть найден")
        assertTrue(scanText(text2) >= 1, "Visa Number с буквой Z должен быть найден")
    }

    // ========== 2. Позиция совпадения в тексте ==========

    @Test
    fun testAtStartOfText() {
        val text = "41758293"
        assertTrue(scanText(text) >= 1, "Visa Number в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Visa Number: 41758293"
        assertTrue(scanText(text) >= 1, "Visa Number в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The visa number 41758293 is valid"
        assertTrue(scanText(text) >= 1, "Visa Number в середине текста должен быть найден")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Number 41758293"
        assertTrue(scanText(text) >= 1, "Visa Number с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "41758293 is valid"
        assertTrue(scanText(text) >= 1, "Visa Number с пробелом после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Visa Number: 41758293"
        assertTrue(scanText(text) >= 1, "Visa Number с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithComma() {
        val text = "Visa Number: 41758293, next"
        assertTrue(scanText(text) >= 1, "Visa Number с запятой должен быть найден")
    }

    @Test
    fun testWithDot() {
        val text = "Visa Number: 41758293."
        assertTrue(scanText(text) >= 1, "Visa Number с точкой должен быть найден")
    }

    @Test
    fun testWithParentheses() {
        val text = "(41758293)"
        assertTrue(scanText(text) >= 1, "Visa Number в скобках должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n41758293"
        assertTrue(scanText(text) >= 1, "Visa Number после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "41758293\n"
        assertTrue(scanText(text) >= 1, "Visa Number перед \\n должен быть найден")
    }

    // ========== 3. Негативные сценарии ==========

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc12345678def"
        assertEquals(0, scanText(text), "Visa Number внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12341758293456"
        assertEquals(0, scanText(text), "Visa Number внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать Visa Number")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no visa numbers"
        assertEquals(0, scanText(text), "Текст без Visa Number не должен находить совпадения")
    }

    @Test
    fun testTooShort8Digit() {
        val text = "1234567" // 7 цифр вместо 8
        assertEquals(0, scanText(text), "Слишком короткий 8-значный Visa Number не должен находиться")
    }

    @Test
    fun testTooLong8Digit() {
        val text = "123456789" // 9 цифр вместо 8
        assertEquals(0, scanText(text), "Слишком длинный 8-значный Visa Number не должен находиться")
    }

    @Test
    fun testTooShortLetterFormat() {
        val text = "A123456" // Буква + 6 цифр вместо 7
        assertEquals(0, scanText(text), "Слишком короткий Visa Number с буквой не должен находиться")
    }

    @Test
    fun testTooLongLetterFormat() {
        val text = "A12345678" // Буква + 8 цифр вместо 7
        assertEquals(0, scanText(text), "Слишком длинный Visa Number с буквой не должен находиться")
    }

    @Test
    fun testTwoLetters() {
        val text = "AB1234567" // Две буквы вместо одной
        assertEquals(0, scanText(text), "Visa Number с двумя буквами не должен находиться")
    }

    @Test
    fun testLettersInDigits() {
        val text = "1234567A" // Буква вместо цифры в 8-значном формате
        assertEquals(0, scanText(text), "Visa Number с буквами в 8-значном формате не должен находиться")
    }

    @Test
    fun testAllZeros8Digit() {
        val text = "00000000" // Все нули в 8-значном формате
        assertEquals(0, scanText(text), "Visa Number со всеми нулями не должен находиться")
    }

    @Test
    fun testAllSameDigits8Digit() {
        val text = "11111111" // Все единицы
        assertEquals(0, scanText(text), "Visa Number с одинаковыми цифрами не должен находиться")
    }

    @Test
    fun testAllZerosLetterFormat() {
        val text = "A0000000" // Все нули в формате буква + 7 цифр
        assertEquals(0, scanText(text), "Visa Number с буквой и всеми нулями не должен находиться")
    }

    @Test
    fun testAllSameDigitsLetterFormat() {
        val text = "A1111111" // Все единицы в формате буква + 7 цифр
        assertEquals(0, scanText(text), "Visa Number с буквой и одинаковыми цифрами не должен находиться")
    }

    @Test
    fun testMultipleVisaNumbers() {
        val text = "41758293 and A7258419"
        assertTrue(scanText(text) >= 2, "Несколько Visa Numbers должны быть найдены")
    }

    @Test
    fun testBothFormats() {
        val text = "Visa 1: 41758293, Visa 2: B4827591"
        assertTrue(scanText(text) >= 2, "Оба формата Visa Number должны быть найдены")
    }

    // ========== 4. Тесты дополнительной фильтрации для снижения false positives ==========

    @Test
    fun testSequentialAscending() {
        val text = "12345678" // Последовательные цифры по возрастанию
        assertEquals(0, scanText(text), "Последовательные цифры по возрастанию должны быть отфильтрованы")
    }

    @Test
    fun testSequentialDescending() {
        val text = "87654321" // Последовательные цифры по убыванию
        assertEquals(0, scanText(text), "Последовательные цифры по убыванию должны быть отфильтрованы")
    }

    @Test
    fun testRepeatingPattern2() {
        val text = "12121212" // Повторяющийся паттерн длины 2
        assertEquals(0, scanText(text), "Повторяющийся паттерн длины 2 должен быть отфильтрован")
    }

    @Test
    fun testRepeatingPattern4() {
        val text = "12341234" // Повторяющийся паттерн длины 4
        assertEquals(0, scanText(text), "Повторяющийся паттерн длины 4 должен быть отфильтрован")
    }

    @Test
    fun testPalindrome() {
        val text = "12344321" // Палиндром
        assertEquals(0, scanText(text), "Палиндром должен быть отфильтрован")
    }

    @Test
    fun testDatePatternYYYYMMDD() {
        val text = "20240115" // Дата в формате YYYYMMDD
        assertEquals(0, scanText(text), "Дата в формате YYYYMMDD должна быть отфильтрована")
    }

    @Test
    fun testDatePatternMMDDYYYY() {
        val text = "01252024" // Дата в формате MMDDYYYY
        assertEquals(0, scanText(text), "Дата в формате MMDDYYYY должна быть отфильтрована")
    }

    @Test
    fun testAlternatingPattern() {
        val text = "10101010" // Чередующийся паттерн
        assertEquals(0, scanText(text), "Чередующийся паттерн должен быть отфильтрован")
    }

    @Test
    fun testTooFewUniqueDigits() {
        val text = "11112222" // Только 2 уникальные цифры, близкие друг к другу
        assertEquals(0, scanText(text), "Номер с слишком малым количеством уникальных цифр должен быть отфильтрован")
    }

    @Test
    fun testInvalidLetterPrefixI() {
        val text = "I1234567" // Буква I недопустима
        assertEquals(0, scanText(text), "Visa Number с буквой I должен быть отфильтрован")
    }

    @Test
    fun testInvalidLetterPrefixO() {
        val text = "O1234567" // Буква O недопустима
        assertEquals(0, scanText(text), "Visa Number с буквой O должен быть отфильтрован")
    }

    // ========== 5. Тесты с ключевыми словами ==========

    @Test
    fun testWithVisaKeyword() {
        val text = "visa: 41758293"
        assertTrue(scanText(text) >= 1, "Visa Number с ключевым словом 'visa' должен быть найден")
    }

    @Test
    fun testWithVisaNumberKeyword() {
        val text = "visa number: A7258419"
        assertTrue(scanText(text) >= 1, "Visa Number с ключевым словом 'visa number' должен быть найден")
    }

    @Test
    fun testWithVisaNoKeyword() {
        val text = "visa no: 41758293"
        assertTrue(scanText(text) >= 1, "Visa Number с ключевым словом 'visa no' должен быть найден")
    }

    @Test
    fun testWithVisaFoilKeyword() {
        val text = "visa foil number: B4827591"
        assertTrue(scanText(text) >= 1, "Visa Number с ключевым словом 'visa foil number' должен быть найден")
    }

    @Test
    fun testWithUSVisaKeyword() {
        val text = "u.s. visa: 41758293"
        assertTrue(scanText(text) >= 1, "Visa Number с ключевым словом 'u.s. visa' должен быть найден")
    }

    @Test
    fun testWithVisaHashtag() {
        val text = "visa #: A7258419"
        assertTrue(scanText(text) >= 1, "Visa Number с ключевым словом 'visa #' должен быть найден")
    }

    @Test
    fun testWithKeywordsAndSeparators() {
        val text = "visa number - 41758293"
        assertTrue(scanText(text) >= 1, "Visa Number с ключевым словом и разделителем должен быть найден")
    }

    @Test
    fun testWithKeywordsCaseInsensitive() {
        val text = "VISA NUMBER: 41758293"
        assertTrue(scanText(text) >= 1, "Visa Number с ключевыми словами в верхнем регистре должен быть найден")
    }
}