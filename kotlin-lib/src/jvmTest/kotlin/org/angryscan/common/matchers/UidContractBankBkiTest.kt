package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера UidContractBankBki
 */
internal class UidContractBankBkiTest : MatcherTestBase(UidContractBankBki) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Contract UID: 12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The UID 12345678-1234-4123-8123-123456789012 is valid"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "12345678-1234-4123-8123-123456789012\nSecond line"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в начале строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 12345678-1234-4123-8123-123456789012 UID"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "12345678-1234-4123-8123-123456789012\n"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ перед \\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "12345678-1234-4123-8123-123456789012\n\n"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc12345678-1234-4123-8123-123456789012def"
        assertEquals(0, scanText(text), "UID контракта банка БКИ внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12312345678-1234-4123-8123-123456789012456"
        assertEquals(0, scanText(text), "UID контракта банка БКИ внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "UID 12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "12345678-1234-4123-8123-123456789012 is valid"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "UID, 12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ с запятой перед должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "UID. 12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ с точкой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "UID; 12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "UID: 12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 12345678-1234-4123-8123-123456789012 )"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 12345678-1234-4123-8123-123456789012 \""
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 12345678-1234-4123-8123-123456789012 ]"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 12345678-1234-4123-8123-123456789012 }"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAsPartOfFormat() {
        val text = "{12345678-1234-4123-8123-123456789012}"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в фигурных скобках как часть формата должен быть найден")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ с дефисом как часть формата должен быть найден")
    }

    @Test
    fun testWithSpaceAsPartOfFormat() {
        val text = "12345678 1234 4123 8123 123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ с пробелом как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "12345678-1234-4123-8123-123456789012 87654321-4321-4123-9123-210987654321"
        assertTrue(scanText(text) >= 2, "Несколько UID контрактов банка БКИ через пробел должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать UID контрактов банка БКИ")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no UID contract bank BKI at all"
        assertEquals(0, scanText(text), "Текст без UID контрактов банка БКИ не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Контракт 12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Contract UID 12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "12345678-1234-4123-8123-123456789012 text"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 12345678-1234-4123-8123-123456789012"
        assertTrue(scanText(text) >= 1, "UID контракта банка БКИ в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidFormat() {
        val text = "12345678@1234@4123@8123@123456789012"
        assertEquals(0, scanText(text), "UID контракта банка БКИ с неправильными разделителями не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "12345678-1234-4123-8123-12345678901"
        assertEquals(0, scanText(text), "Слишком короткий UID контракта банка БКИ не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "12345678-1234-4123-8123-1234567890123"
        assertEquals(0, scanText(text), "Слишком длинный UID контракта банка БКИ не должен находиться")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "12345678-1234-4123-8123-12345678901G"
        assertEquals(0, scanText(text), "UID контракта банка БКИ с недопустимыми символами не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "uid12345678-1234-4123-8123-123456789012"
        assertEquals(0, scanText(text), "UID контракта банка БКИ, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12312345678-1234-4123-8123-123456789012"
        assertEquals(0, scanText(text), "UID контракта банка БКИ, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testPartialUidContractBankBki() {
        val text = "12345678-1234"
        assertEquals(0, scanText(text), "Частичный UID контракта банка БКИ не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function12345678-1234-4123-8123-123456789012()"
        assertEquals(0, scanText(text), "UID контракта банка БКИ внутри кода не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "00000000-0000-0000-0000-000000000000"
        assertEquals(0, scanText(text), "UID контракта банка БКИ из всех нулей не должен находиться")
    }
}

