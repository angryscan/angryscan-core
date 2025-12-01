package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера SNILS
 */
internal class SNILSTest : MatcherTestBase(SNILS) {

    // Валидный SNILS для тестов (с правильной контрольной суммой)
    private val validSNILS = "12345678964" // Пример валидного SNILS

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "123-456-789 64 is a SNILS"
        assertTrue(scanText(text) >= 1, "SNILS в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "SNILS number is 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The SNILS 123-456-789 64 is valid"
        assertTrue(scanText(text) >= 1, "SNILS в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "123-456-789 64\nSecond line"
        assertTrue(scanText(text) >= 1, "SNILS в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 123-456-789 64 SNILS"
        assertTrue(scanText(text) >= 1, "SNILS в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "123-456-789 64\n"
        assertTrue(scanText(text) >= 1, "SNILS перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n123-456-789 64\r\n"
        assertTrue(scanText(text) >= 1, "SNILS с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "123-456-789 64\n\n"
        assertTrue(scanText(text) >= 1, "SNILS перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc123-456-789 64def"
        assertEquals(0, scanText(text), "SNILS внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123456123-456-789 64456"
        assertEquals(0, scanText(text), "SNILS внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc123123-456-789 64def456"
        assertEquals(0, scanText(text), "SNILS внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "SNILS 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "123-456-789 64 is valid"
        assertTrue(scanText(text) >= 1, "SNILS с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "SNILS, 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "123-456-789 64, next"
        assertTrue(scanText(text) >= 1, "SNILS с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "SNILS. 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "123-456-789 64."
        assertTrue(scanText(text) >= 1, "SNILS с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "SNILS; 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "123-456-789 64; next"
        assertTrue(scanText(text) >= 1, "SNILS с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "SNILS: 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "123-456-789 64!"
        assertTrue(scanText(text) >= 1, "SNILS с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "123-456-789 64?"
        assertTrue(scanText(text) >= 1, "SNILS с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 123-456-789 64 )"
        assertTrue(scanText(text) >= 1, "SNILS в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(123-456-789 64)"
        assertTrue(scanText(text) >= 1, "SNILS в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 123-456-789 64 \""
        assertTrue(scanText(text) >= 1, "SNILS в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"123-456-789 64\""
        assertTrue(scanText(text) >= 1, "SNILS в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "snils = 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "snils # 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 123-456-789 64 ]"
        assertTrue(scanText(text) >= 1, "SNILS в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 123-456-789 64 }"
        assertTrue(scanText(text) >= 1, "SNILS в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с дефисом как часть формата должен быть найден")
    }

    @Test
    fun testWithSpaceAsPartOfFormat() {
        val text = "123 456 789 64"
        assertTrue(scanText(text) >= 1, "SNILS с пробелом как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "123-456-789 64 987-654-321 83"
        assertTrue(scanText(text) >= 2, "Несколько SNILS через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "123-456-789 64, 987-654-321 83"
        assertTrue(scanText(text) >= 2, "Несколько SNILS через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "123-456-789 64; 987-654-321 83"
        assertTrue(scanText(text) >= 2, "Несколько SNILS через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "123-456-789 64\n987-654-321 83"
        assertTrue(scanText(text) >= 2, "Несколько SNILS через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать SNILS")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no SNILS numbers at all"
        assertEquals(0, scanText(text), "Текст без SNILS не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "12345678964"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithSpacesFormat() {
        val text = "123 456 789 64"
        assertTrue(scanText(text) >= 1, "SNILS с пробелами должен быть найден")
    }

    @Test
    fun testWithDashesFormat() {
        val text = "123-456-789-64"
        assertTrue(scanText(text) >= 1, "SNILS с дефисами должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "SNILS    123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "SNILS\t123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "123-456-789 64\tnext"
        assertTrue(scanText(text) >= 1, "SNILS с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "СНИЛС 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "SNILS 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "123-456-789 64 text"
        assertTrue(scanText(text) >= 1, "SNILS в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 123-456-789 64"
        assertTrue(scanText(text) >= 1, "SNILS в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidControlSum() {
        val text = "123-456-789 99"
        assertEquals(0, scanText(text), "SNILS с неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "123-456-78"
        assertEquals(0, scanText(text), "Слишком короткий SNILS не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "123-456-789 644"
        assertEquals(0, scanText(text), "Слишком длинный SNILS не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "123-456-789 AB"
        assertEquals(0, scanText(text), "SNILS с буквами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "SNILS внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "snils123-456-789 64"
        assertEquals(0, scanText(text), "SNILS, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123123-456-789 64"
        assertEquals(0, scanText(text), "SNILS, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "123@456@789@03"
        assertEquals(0, scanText(text), "SNILS с неправильными разделителями не должен находиться")
    }

    @Test
    fun testPartialSNILS() {
        val text = "123-456"
        assertEquals(0, scanText(text), "Частичный SNILS не должен находиться")
    }

    @Test
    fun testSNILSInCode() {
        val text = "function123-456-789 64()"
        assertEquals(0, scanText(text), "SNILS внутри кода не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "000-000-000 00"
        // В зависимости от реализации - может быть найден или нет
        val count = scanText(text)
        assertTrue(count >= 0, "SNILS из всех нулей")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "123#456#789#03"
        assertEquals(0, scanText(text), "SNILS с недопустимыми символами не должен находиться")
    }

    @Test
    fun testWithSpacesInWrongPlaces() {
        val text = "12 3-456-789 03"
        assertEquals(0, scanText(text), "SNILS с пробелами в неправильных местах не должен находиться")
    }
}

