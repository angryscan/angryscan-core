package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера IPv6
 */
internal class IPv6Test : MatcherTestBase(IPv6) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334 is an IP"
        assertTrue(scanText(text) >= 1, "IPv6 в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "IP address is 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The IP 2001:0db8:85a3:0000:0000:8a2e:0370:7334 is valid"
        assertTrue(scanText(text) >= 1, "IPv6 в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334\nSecond line"
        assertTrue(scanText(text) >= 1, "IPv6 в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 2001:0db8:85a3:0000:0000:8a2e:0370:7334 IP"
        assertTrue(scanText(text) >= 1, "IPv6 в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334\n"
        assertTrue(scanText(text) >= 1, "IPv6 перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n2001:0db8:85a3:0000:0000:8a2e:0370:7334\r\n"
        assertTrue(scanText(text) >= 1, "IPv6 с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334\n\n"
        assertTrue(scanText(text) >= 1, "IPv6 перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc2001:0db8:85a3:0000:0000:8a2e:0370:7334def"
        assertEquals(0, scanText(text), "IPv6 внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "1232001:0db8:85a3:0000:0000:8a2e:0370:7334456"
        assertEquals(0, scanText(text), "IPv6 внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc1232001:0db8:85a3:0000:0000:8a2e:0370:7334def456"
        assertEquals(0, scanText(text), "IPv6 внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "IP 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334 is valid"
        assertTrue(scanText(text) >= 1, "IPv6 с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "IP, 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334, next"
        assertTrue(scanText(text) >= 1, "IPv6 с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "IP. 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334."
        assertTrue(scanText(text) >= 1, "IPv6 с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "IP; 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334; next"
        assertTrue(scanText(text) >= 1, "IPv6 с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "IP: 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 2001:0db8:85a3:0000:0000:8a2e:0370:7334 )"
        assertTrue(scanText(text) >= 1, "IPv6 в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(2001:0db8:85a3:0000:0000:8a2e:0370:7334)"
        assertTrue(scanText(text) >= 1, "IPv6 в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 2001:0db8:85a3:0000:0000:8a2e:0370:7334 \""
        assertTrue(scanText(text) >= 1, "IPv6 в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\""
        assertTrue(scanText(text) >= 1, "IPv6 в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "ip = 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "ip # 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 2001:0db8:85a3:0000:0000:8a2e:0370:7334 ]"
        assertTrue(scanText(text) >= 1, "IPv6 в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 2001:0db8:85a3:0000:0000:8a2e:0370:7334 }"
        assertTrue(scanText(text) >= 1, "IPv6 в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithColonAsPartOfFormat() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с двоеточием как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334 2001:0db8:85a3:0000:0000:8a2e:0370:7335"
        assertTrue(scanText(text) >= 2, "Несколько IPv6 через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334, 2001:0db8:85a3:0000:0000:8a2e:0370:7335"
        assertTrue(scanText(text) >= 2, "Несколько IPv6 через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334; 2001:0db8:85a3:0000:0000:8a2e:0370:7335"
        assertTrue(scanText(text) >= 2, "Несколько IPv6 через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334\n2001:0db8:85a3:0000:0000:8a2e:0370:7335"
        assertTrue(scanText(text) >= 2, "Несколько IPv6 через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать IPv6")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no IP addresses at all"
        assertEquals(0, scanText(text), "Текст без IPv6 не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "0000:0000:0000:0000:0000:0000:0000:0000"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testMaximalFormat() {
        val text = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"
        assertTrue(scanText(text) >= 1, "Максимальный формат должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370"
        assertEquals(0, scanText(text), "Слишком короткий адрес не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334:1234"
        assertEquals(0, scanText(text), "Слишком длинный адрес не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "IP    2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "IP\t2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334\tnext"
        assertTrue(scanText(text) >= 1, "IPv6 с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "IP адрес 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "IP address 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334 text"
        assertTrue(scanText(text) >= 1, "IPv6 в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertTrue(scanText(text) >= 1, "IPv6 в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidHexValue() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:GGGG"
        assertEquals(0, scanText(text), "IPv6 с недопустимыми hex-значениями не должен находиться")
    }

    @Test
    fun testTooManySegments() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:7334:1234"
        assertEquals(0, scanText(text), "IPv6 с слишком большим количеством сегментов не должен находиться")
    }

    @Test
    fun testTooFewSegments() {
        val text = "2001:0db8:85a3:0000:0000:8a2e"
        assertEquals(0, scanText(text), "IPv6 с недостаточным количеством сегментов не должен находиться")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "2001:0db8:85a3:0000:0000:8a2e:0370:733G"
        assertEquals(0, scanText(text), "IPv6 с недопустимыми символами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "20010db885a3000000008a2e03707334"
        assertEquals(0, scanText(text), "IPv6 внутри длинной последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "ip2001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertEquals(0, scanText(text), "IPv6, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "1232001:0db8:85a3:0000:0000:8a2e:0370:7334"
        assertEquals(0, scanText(text), "IPv6, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "2001@0db8@85a3@0000@0000@8a2e@0370@7334"
        assertEquals(0, scanText(text), "IPv6 с неправильными разделителями не должен находиться")
    }

    @Test
    fun testPartialIPv6() {
        val text = "2001:0db8:85a3"
        assertEquals(0, scanText(text), "Частичный IPv6 не должен находиться")
    }

    @Test
    fun testIPv6InCode() {
        val text = "function2001:0db8:85a3:0000:0000:8a2e:0370:7334()"
        assertEquals(0, scanText(text), "IPv6 внутри кода не должен находиться")
    }

    @Test
    fun testWithTooLongSegment() {
        val text = "2001:0db8:85a3:00000:0000:8a2e:0370:7334"
        assertEquals(0, scanText(text), "IPv6 с слишком длинным сегментом не должен находиться")
    }

    @Test
    fun testWithMultipleDoubleColons() {
        val text = "2001::0db8::85a3"
        assertEquals(0, scanText(text), "IPv6 с несколькими двойными двоеточиями не должен находиться")
    }
}

