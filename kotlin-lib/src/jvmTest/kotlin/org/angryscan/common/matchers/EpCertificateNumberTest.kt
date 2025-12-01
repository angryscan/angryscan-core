package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера EpCertificateNumber
 */
internal class EpCertificateNumberTest : MatcherTestBase(EpCertificateNumber) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Certificate: 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The certificate 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF is valid"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF\nSecond line"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF certificate"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF\n"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF\r\n"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF\n\n"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EFdef"
        assertEquals(0, scanText(text), "Номер сертификата ЭП внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12312 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF456"
        assertEquals(0, scanText(text), "Номер сертификата ЭП внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Certificate 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF is valid"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Certificate, 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF, next"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Certificate. 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF."
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Certificate; 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF; next"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Certificate: 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF )"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF)"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF \""
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF\""
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в кавычках без пробелов должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithCommas() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF, 98 76 54 32 10 FE DC BA 98 76 54 32 10 FE DC BA"
        assertTrue(scanText(text) >= 2, "Несколько номеров сертификатов ЭП через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF; 98 76 54 32 10 FE DC BA 98 76 54 32 10 FE DC BA"
        assertTrue(scanText(text) >= 2, "Несколько номеров сертификатов ЭП через точку с запятой должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать номеров сертификатов ЭП")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no EP certificate numbers at all"
        assertEquals(0, scanText(text), "Текст без номеров сертификатов ЭП не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testFormatWith4Digits() {
        val text = "1234 5678 90AB CDEF 1234 5678 90AB CDEF"
        assertTrue(scanText(text) >= 1, "Формат с 4 цифрами должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Certificate    12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Certificate\t12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF\tnext"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Сертификат 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Certificate 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF text"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер сертификата ЭП в конце текста должен быть найден")
    }

    @Test
    fun testWithSerialNumberKeyword() {
        val text = "серийный номер сертификата ЭП: 12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertTrue(scanText(text) >= 1, "Номер с ключевым словом должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "12 34 56 78 90 AB CD EF"
        assertEquals(0, scanText(text), "Слишком короткий номер сертификата ЭП не должен находиться")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD GX"
        assertEquals(0, scanText(text), "Номер сертификата ЭП с недопустимыми символами не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00"
        assertEquals(0, scanText(text), "Номер сертификата ЭП из всех нулей не должен находиться")
    }

    @Test
    fun testAllSameChars() {
        val text = "FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF"
        assertEquals(0, scanText(text), "Номер сертификата ЭП из одинаковых символов не должен находиться")
    }

    @Test
    fun testAllDigits() {
        val text = "12 34 56 78 90 12 34 56 12 34 56 78 90 12 34 56"
        assertEquals(0, scanText(text), "Номер сертификата ЭП только из цифр не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "cert12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertEquals(0, scanText(text), "Номер сертификата ЭП, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12312 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF"
        assertEquals(0, scanText(text), "Номер сертификата ЭП, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function12 34 56 78 90 AB CD EF 12 34 56 78 90 AB CD EF()"
        assertEquals(0, scanText(text), "Номер сертификата ЭП внутри кода не должен находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "12@34@56@78@90@AB@CD@EF@12@34@56@78@90@AB@CD@EF"
        assertEquals(0, scanText(text), "Номер сертификата ЭП с неправильными разделителями не должен находиться")
    }
}

