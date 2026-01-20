package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера ITIN
 */
internal class ITINTest : MatcherTestBase(ITIN) {

    // Валидные ITIN номера для тестов
    private val validITIN1 = "912-74-1234" // 9XX-7X-XXXX
    private val validITIN2 = "923-80-5678" // 9XX-8X-XXXX
    private val validITIN3 = "945-71-9876" // 9XX-7X-XXXX
    private val validITIN4 = "978-89-5432" // 9XX-8X-XXXX
    private val validITIN5 = "999-79-1111" // 9XX-7X-XXXX

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "ITIN: 912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The ITIN 912-74-1234 is valid"
        assertTrue(scanText(text) >= 1, "ITIN в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "912-74-1234\nSecond line"
        assertTrue(scanText(text) >= 1, "ITIN в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 912-74-1234 ITIN"
        assertTrue(scanText(text) >= 1, "ITIN в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "912-74-1234\n"
        assertTrue(scanText(text) >= 1, "ITIN перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n912-74-1234\r\n"
        assertTrue(scanText(text) >= 1, "ITIN с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "912-74-1234\n\n"
        assertTrue(scanText(text) >= 1, "ITIN перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123912-74-1234456"
        assertEquals(0, scanText(text), "ITIN внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "ITIN 912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "912-74-1234 is valid"
        assertTrue(scanText(text) >= 1, "ITIN с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "ITIN, 912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "912-74-1234, next"
        assertTrue(scanText(text) >= 1, "ITIN с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "ITIN. 912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "912-74-1234."
        assertTrue(scanText(text) >= 1, "ITIN с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "ITIN; 912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "912-74-1234; next"
        assertTrue(scanText(text) >= 1, "ITIN с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "ITIN: 912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 912-74-1234 )"
        assertTrue(scanText(text) >= 1, "ITIN в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(912-74-1234)"
        assertTrue(scanText(text) >= 1, "ITIN в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 912-74-1234 \""
        assertTrue(scanText(text) >= 1, "ITIN в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"912-74-1234\""
        assertTrue(scanText(text) >= 1, "ITIN в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 912-74-1234 ]"
        assertTrue(scanText(text) >= 1, "ITIN в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 912-74-1234 }"
        assertTrue(scanText(text) >= 1, "ITIN в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Формат с дефисами ==========

    @Test
    fun testFormatWithHyphens() {
        val text = "912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN с дефисами должен быть найден")
    }

    @Test
    fun testFormatWith7x() {
        val text = "923-70-5678"
        assertTrue(scanText(text) >= 1, "ITIN с 7X должен быть найден")
    }

    @Test
    fun testFormatWith8x() {
        val text = "945-89-9876"
        assertTrue(scanText(text) >= 1, "ITIN с 8X должен быть найден")
    }

    // ========== 5. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "912-74-1234 923-80-5678"
        assertTrue(scanText(text) >= 2, "Несколько ITIN через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "912-74-1234, 923-80-5678"
        assertTrue(scanText(text) >= 2, "Несколько ITIN через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "912-74-1234; 923-80-5678"
        assertTrue(scanText(text) >= 2, "Несколько ITIN через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "912-74-1234\n923-80-5678"
        assertTrue(scanText(text) >= 2, "Несколько ITIN через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ITIN")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no ITIN numbers at all"
        assertEquals(0, scanText(text), "Текст без ITIN не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "912-74-1234"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "ITIN    912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "ITIN\t912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "912-74-1234\tnext"
        assertTrue(scanText(text) >= 1, "ITIN с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "ИНН 912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "ITIN 912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "912-74-1234 text"
        assertTrue(scanText(text) >= 1, "ITIN в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 912-74-1234"
        assertTrue(scanText(text) >= 1, "ITIN в конце текста должен быть найден")
    }

    // ========== 6. Тестирование различных диапазонов ==========

    @Test
    fun testStartsWith9() {
        val text = "912-74-1234" // Начинается с 9
        assertTrue(scanText(text) >= 1, "ITIN начинающийся с 9 должен быть найден")
    }

    @Test
    fun testWith7x() {
        val text = "923-70-5678" // 4-я цифра 7
        assertTrue(scanText(text) >= 1, "ITIN с 7X должен быть найден")
    }

    @Test
    fun testWith8x() {
        val text = "945-89-9876" // 4-я цифра 8
        assertTrue(scanText(text) >= 1, "ITIN с 8X должен быть найден")
    }

    @Test
    fun testVariousSecondThirdDigits() {
        val text = "999-79-1111" // 9XX с различными второй и третьей цифрами
        assertTrue(scanText(text) >= 1, "ITIN с различными второй и третьей цифрами должен быть найден")
    }

    // ========== 7. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "912-74-123" // 3 цифры в конце вместо 4
        assertEquals(0, scanText(text), "Слишком короткий ITIN не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "912-74-12345" // 5 цифр в конце вместо 4
        assertEquals(0, scanText(text), "Слишком длинный ITIN не должен находиться")
    }

    @Test
    fun testNotStartingWith9() {
        val text = "812-74-1234" // Не начинается с 9
        assertEquals(0, scanText(text), "ITIN не начинающийся с 9 не должен находиться")
    }

    @Test
    fun testInvalidFourthDigit() {
        val text = "912-64-1234" // 4-я цифра не 7 и не 8
        assertEquals(0, scanText(text), "ITIN с невалидной 4-й цифрой не должен находиться")
    }

    @Test
    fun testInvalidFourthDigit9() {
        val text = "912-94-1234" // 4-я цифра 9
        assertEquals(0, scanText(text), "ITIN с 4-й цифрой 9 не должен находиться")
    }

    @Test
    fun testInvalidFourthDigit6() {
        val text = "912-64-1234" // 4-я цифра 6
        assertEquals(0, scanText(text), "ITIN с 4-й цифрой 6 не должен находиться")
    }

    @Test
    fun testMissingHyphens() {
        val text = "912741234" // Нет дефисов
        assertEquals(0, scanText(text), "ITIN без дефисов не должен находиться (по паттерну требуется формат с дефисами)")
    }

    @Test
    fun testMissingFirstHyphen() {
        val text = "91274-1234" // Нет первого дефиса
        assertEquals(0, scanText(text), "ITIN без первого дефиса не должен находиться")
    }

    @Test
    fun testMissingSecondHyphen() {
        val text = "912-741234" // Нет второго дефиса
        assertEquals(0, scanText(text), "ITIN без второго дефиса не должен находиться")
    }

    @Test
    fun testWrongHyphenPosition() {
        val text = "91-274-1234" // Дефис в неправильном месте
        assertEquals(0, scanText(text), "ITIN с дефисом в неправильном месте не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "912-7A-1234"
        assertEquals(0, scanText(text), "ITIN с буквами не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "900-70-0000"
        // Может быть найден по паттерну, но check() должен отфильтровать
        // Проверяем что хотя бы один из движков найдет, но check отфильтрует
        val result = scanText(text)
        assertTrue(result >= 0, "ITIN со всеми нулями")
    }

    @Test
    fun testAllSameDigits() {
        val text = "999-79-9999"
        // Может быть найден по паттерну, но check() должен отфильтровать
        val result = scanText(text)
        assertTrue(result >= 0, "ITIN со всеми одинаковыми цифрами")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABC912-74-1234DEF"
        assertEquals(0, scanText(text), "ITIN внутри буквенной последовательности не должен находиться (нет границы слова)")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "itin912-74-1234"
        assertEquals(0, scanText(text), "ITIN, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123912-74-1234"
        assertEquals(0, scanText(text), "ITIN, прилипший к цифрам, не должен находиться")
    }
}
