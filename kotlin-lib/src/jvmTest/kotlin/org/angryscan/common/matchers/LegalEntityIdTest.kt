package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера LegalEntityId
 */
internal class LegalEntityIdTest : MatcherTestBase(LegalEntityId) {

    // Валидный LEI для тестов (20 символов, правильная контрольная сумма)
    private val validLEI = "ABCD0000001234567872" // Валидный LEI с правильной контрольной суммой

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Legal Entity ID: ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The LEI ABCD0000001234567872 is valid"
        assertTrue(scanText(text) >= 1, "LEI в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "ABCD0000001234567872\nSecond line"
        assertTrue(scanText(text) >= 1, "LEI в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with ABCD0000001234567872 LEI"
        assertTrue(scanText(text) >= 1, "LEI в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "ABCD0000001234567872\n"
        assertTrue(scanText(text) >= 1, "LEI перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nABCD0000001234567872\r\n"
        assertTrue(scanText(text) >= 1, "LEI с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "ABCD0000001234567872\n\n"
        assertTrue(scanText(text) >= 1, "LEI перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcABCD0000001234567872def"
        assertEquals(0, scanText(text), "LEI внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123ABCD0000001234567872456"
        assertEquals(0, scanText(text), "LEI внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "LEI ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "ABCD0000001234567872 is valid"
        assertTrue(scanText(text) >= 1, "LEI с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "LEI, ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "ABCD0000001234567872, next"
        assertTrue(scanText(text) >= 1, "LEI с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "LEI. ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "ABCD0000001234567872."
        assertTrue(scanText(text) >= 1, "LEI с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "LEI; ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "ABCD0000001234567872; next"
        assertTrue(scanText(text) >= 1, "LEI с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "LEI: ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( ABCD0000001234567872 )"
        assertTrue(scanText(text) >= 1, "LEI в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(ABCD0000001234567872)"
        assertTrue(scanText(text) >= 1, "LEI в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" ABCD0000001234567872 \""
        assertTrue(scanText(text) >= 1, "LEI в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"ABCD0000001234567872\""
        assertTrue(scanText(text) >= 1, "LEI в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ ABCD0000001234567872 ]"
        assertTrue(scanText(text) >= 1, "LEI в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ ABCD0000001234567872 }"
        assertTrue(scanText(text) >= 1, "LEI в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithCommas() {
        val text = "ABCD0000001234567872, EFGH0000009876543256"
        assertTrue(scanText(text) >= 2, "Несколько LEI через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "ABCD0000001234567872; EFGH0000009876543256"
        assertTrue(scanText(text) >= 2, "Несколько LEI через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "ABCD0000001234567872\nEFGH0000009876543256"
        assertTrue(scanText(text) >= 2, "Несколько LEI через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать LEI")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no Legal Entity IDs at all"
        assertEquals(0, scanText(text), "Текст без LEI не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "LEI    ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "LEI\tABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "ABCD0000001234567872\tnext"
        assertTrue(scanText(text) >= 1, "LEI с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Идентификатор ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Legal Entity ID ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "ABCD0000001234567872 text"
        assertTrue(scanText(text) >= 1, "LEI в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text ABCD0000001234567872"
        assertTrue(scanText(text) >= 1, "LEI в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidChecksum() {
        val text = "ABCD000000123456789X"
        assertEquals(0, scanText(text), "LEI с неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "ABCD000000123456789"
        assertEquals(0, scanText(text), "Слишком короткий LEI не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "ABCD00000012345678721"
        assertEquals(0, scanText(text), "Слишком длинный LEI не должен находиться")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "ABCD000000123456789I"
        assertEquals(0, scanText(text), "LEI с недопустимыми символами не должен находиться")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABCABCD0000001234567872DEF"
        assertEquals(0, scanText(text), "LEI внутри длинной последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "leiABCD0000001234567872"
        assertEquals(0, scanText(text), "LEI, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123ABCD0000001234567872"
        assertEquals(0, scanText(text), "LEI, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionABCD0000001234567872()"
        assertEquals(0, scanText(text), "LEI внутри кода не должен находиться")
    }

    @Test
    fun testWithSpaces() {
        val text = "ABCD 000000 123456 7890"
        assertEquals(0, scanText(text), "LEI с пробелами не должен находиться")
    }
}

