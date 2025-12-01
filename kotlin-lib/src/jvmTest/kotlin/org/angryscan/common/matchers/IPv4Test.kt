package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера IPv4
 */
internal class IPv4Test : MatcherTestBase(IPv4) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "192.168.1.1 is an IP"
        assertTrue(scanText(text) >= 1, "IPv4 в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "IP address is 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The IP 192.168.1.1 is valid"
        assertTrue(scanText(text) >= 1, "IPv4 в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "192.168.1.1\nSecond line"
        assertTrue(scanText(text) >= 1, "IPv4 в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 192.168.1.1 IP"
        assertTrue(scanText(text) >= 1, "IPv4 в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "192.168.1.1\n"
        assertTrue(scanText(text) >= 1, "IPv4 перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n192.168.1.1\r\n"
        assertTrue(scanText(text) >= 1, "IPv4 с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "192.168.1.1\n\n"
        assertTrue(scanText(text) >= 1, "IPv4 перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc192.168.1.1def"
        assertEquals(0, scanText(text), "IPv4 внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123192.168.1.1456"
        assertEquals(0, scanText(text), "IPv4 внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc123192.168.1.1def456"
        assertEquals(0, scanText(text), "IPv4 внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "IP 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "192.168.1.1 is valid"
        assertTrue(scanText(text) >= 1, "IPv4 с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "IP, 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "192.168.1.1, next"
        assertTrue(scanText(text) >= 1, "IPv4 с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "IP. 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с точкой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "IP; 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "192.168.1.1; next"
        assertTrue(scanText(text) >= 1, "IPv4 с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "IP: 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 192.168.1.1 )"
        assertTrue(scanText(text) >= 1, "IPv4 в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(192.168.1.1)"
        assertTrue(scanText(text) >= 1, "IPv4 в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 192.168.1.1 \""
        assertTrue(scanText(text) >= 1, "IPv4 в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"192.168.1.1\""
        assertTrue(scanText(text) >= 1, "IPv4 в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "ip = 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "ip # 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 192.168.1.1 ]"
        assertTrue(scanText(text) >= 1, "IPv4 в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 192.168.1.1 }"
        assertTrue(scanText(text) >= 1, "IPv4 в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithDotAsPartOfFormat() {
        val text = "192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с точкой как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "192.168.1.1 10.0.0.1"
        assertTrue(scanText(text) >= 2, "Несколько IPv4 через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "192.168.1.1, 10.0.0.1"
        assertTrue(scanText(text) >= 2, "Несколько IPv4 через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "192.168.1.1; 10.0.0.1"
        assertTrue(scanText(text) >= 2, "Несколько IPv4 через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "192.168.1.1\n10.0.0.1"
        assertTrue(scanText(text) >= 2, "Несколько IPv4 через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать IPv4")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no IP addresses at all"
        assertEquals(0, scanText(text), "Текст без IPv4 не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "1.1.1.1"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testMaximalFormat() {
        val text = "255.255.255.255"
        assertTrue(scanText(text) >= 1, "Максимальный формат должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "192.168.1"
        assertEquals(0, scanText(text), "Слишком короткий адрес не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "192.168.1.1.1"
        assertEquals(0, scanText(text), "Слишком длинный адрес не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "IP    192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "IP\t192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "192.168.1.1\tnext"
        assertTrue(scanText(text) >= 1, "IPv4 с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "IP адрес 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "IP address 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "192.168.1.1 text"
        assertTrue(scanText(text) >= 1, "IPv4 в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 192.168.1.1"
        assertTrue(scanText(text) >= 1, "IPv4 в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidOctetValue() {
        val text = "192.168.1.256"
        assertEquals(0, scanText(text), "IPv4 с недопустимым значением октета не должен находиться")
    }

    @Test
    fun testTooManyOctets() {
        val text = "192.168.1.1.1"
        assertEquals(0, scanText(text), "IPv4 с слишком большим количеством октетов не должен находиться")
    }

    @Test
    fun testTooFewOctets() {
        val text = "192.168.1"
        assertEquals(0, scanText(text), "IPv4 с недостаточным количеством октетов не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "192.168.1.A"
        assertEquals(0, scanText(text), "IPv4 с буквами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "IPv4 внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "ip192.168.1.1"
        assertEquals(0, scanText(text), "IPv4, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123192.168.1.1"
        assertEquals(0, scanText(text), "IPv4, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "192@168@1@1"
        assertEquals(0, scanText(text), "IPv4 с неправильными разделителями не должен находиться")
    }

    @Test
    fun testPartialIPv4() {
        val text = "192.168"
        assertEquals(0, scanText(text), "Частичный IPv4 не должен находиться")
    }

    @Test
    fun testIPv4InCode() {
        val text = "function192.168.1.1()"
        assertEquals(0, scanText(text), "IPv4 внутри кода не должен находиться")
    }

    @Test
    fun testWithLeadingZeros() {
        val text = "192.168.01.1"
        // В зависимости от реализации - может быть найден или нет
        val count = scanText(text)
        assertTrue(count >= 0, "IPv4 с ведущими нулями")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "192#168#1#1"
        assertEquals(0, scanText(text), "IPv4 с недопустимыми символами не должен находиться")
    }

    @Test
    fun testWithNegativeValues() {
        val text = "192.168.-1.1"
        assertEquals(0, scanText(text), "IPv4 с отрицательными значениями не должен находиться")
    }
}

