package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера OSAGOPolicy
 */
internal class OSAGOPolicyTest : MatcherTestBase(OSAGOPolicy) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "OSAGO policy: номер полиса ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The policy полис ОСАГО BBB 1234567890 is valid"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "серия и номер полиса ОСАГО BBB 1234567890\nSecond line"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nстраховой полис ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with страховка ОСАГО BBB 1234567890 policy"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nполис обязательного страхования автогражданской ответственности BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "ОСАГО BBB 1234567890\n"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nномер полиса ОСАГО BBB 1234567890\r\n"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nсерия и номер полиса ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "страховой полис ОСАГО BBB 1234567890\n\n"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcBBB 1234567890def"
        assertEquals(0, scanText(text), "Полис ОСАГО внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123BBB 1234567890456"
        assertEquals(0, scanText(text), "Полис ОСАГО внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Policy ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "полис ОСАГО BBB 1234567890 is valid"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Policy, номер полиса ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "серия и номер полиса ОСАГО BBB 1234567890, next"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Policy. страховой полис ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "страховка ОСАГО BBB 1234567890."
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Policy; полис обязательного страхования автогражданской ответственности BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "ОСАГО BBB 1234567890; next"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Policy: номер полиса ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( полис обязательного страхования автогражданской ответственности BBB 1234567890 )"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(номер полиса ОСАГО BBB 1234567890)"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" серия и номер полиса ОСАГО BBB 1234567890 \""
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"страховой полис ОСАГО BBB 1234567890\""
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ страховка ОСАГО BBB 1234567890 ]"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ ОСАГО BBB 1234567890 }"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithSpaceAsPartOfFormat() {
        val text = "полис ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с пробелом как часть формата должен быть найден")
    }

    @Test
    fun testWithNumberSign() {
        val text = "номер полиса ОСАГО BBB № 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с знаком № должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать полисов ОСАГО")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no OSAGO policies at all"
        assertEquals(0, scanText(text), "Текст без полисов ОСАГО не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithLatinLetters() {
        val text = "полис ОСАГО AAA 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с латинскими буквами должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "BBB 123456789"
        assertEquals(0, scanText(text), "Слишком короткий полис не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "BBB 12345678901"
        assertEquals(0, scanText(text), "Слишком длинный полис не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Policy    номер полиса ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Policy\tсерия и номер полиса ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "страховой полис ОСАГО BBB 1234567890\tnext"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Полис страховка ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "OSAGO policy полис ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "полис обязательного страхования автогражданской ответственности BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "ОСАГО BBB 1234567890 text"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text номер полиса ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО в конце текста должен быть найден")
    }

    @Test
    fun testWithoutKeywords() {
        val text = "BBB 1234567890"
        assertEquals(0, scanText(text), "Полис ОСАГО без ключевых слов не должен находиться")
    }

    @Test
    fun testWithOSAGOKeyword() {
        val text = "ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с ключевым словом 'ОСАГО' должен быть найден")
    }

    @Test
    fun testWithPolisOSAGOKeyword() {
        val text = "полис ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с ключевым словом 'полис ОСАГО' должен быть найден")
    }

    @Test
    fun testWithPolniyNomer() {
        val text = "полис обязательного страхования автогражданской ответственности BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с полным ключевым словом должен быть найден")
    }

    @Test
    fun testWithNomerPolisaOSAGO() {
        val text = "номер полиса ОСАГО BBB 1234567890"
        assertTrue(scanText(text) >= 1, "Полис ОСАГО с ключевым словом 'номер полиса ОСАГО' должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testWithLettersInNumber() {
        val text = "BBB ABCDEFGHIJ"
        assertEquals(0, scanText(text), "Полис ОСАГО с буквами в номере не должен находиться")
    }

    @Test
    fun testWithInvalidSeries() {
        val text = "999 1234567890"
        assertEquals(0, scanText(text), "Полис ОСАГО с недопустимой серией не должен находиться")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABCBBB 1234567890DEF"
        assertEquals(0, scanText(text), "Полис ОСАГО внутри длинной последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "policyBBB 1234567890"
        assertEquals(0, scanText(text), "Полис ОСАГО, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123BBB 1234567890"
        assertEquals(0, scanText(text), "Полис ОСАГО, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "BBB@1234567890"
        assertEquals(0, scanText(text), "Полис ОСАГО с неправильными разделителями не должен находиться")
    }

    @Test
    fun testPartialOSAGOPolicy() {
        val text = "BBB"
        assertEquals(0, scanText(text), "Частичный полис ОСАГО не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionBBB 1234567890()"
        assertEquals(0, scanText(text), "Полис ОСАГО внутри кода не должен находиться")
    }
}

