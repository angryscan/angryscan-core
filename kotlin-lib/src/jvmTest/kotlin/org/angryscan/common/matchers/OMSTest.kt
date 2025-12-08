package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера OMS
 */
internal class OMSTest : MatcherTestBase(OMS) {

    // Валидный OMS для тестов (с правильной контрольной суммой)
    private val validOMS = "1234567890123452" // Пример валидного OMS

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "страхование 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "ОМС: омс 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The страховка 1234 5678 9012 3452 is valid"
        assertTrue(scanText(text) >= 1, "ОМС в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "ОМС № 1234 5678 9012 3452\nSecond line"
        assertTrue(scanText(text) >= 1, "ОМС в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nстрахование 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with омс 1234 5678 9012 3452 policy"
        assertTrue(scanText(text) >= 1, "ОМС в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nстраховка 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "страховка 1234 5678 9012 3452\n"
        assertTrue(scanText(text) >= 1, "ОМС перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nстрахование 1234 5678 9012 3452\r\n"
        assertTrue(scanText(text) >= 1, "ОМС с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nомс 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "страхование 1234 5678 9012 3452\n\n"
        assertTrue(scanText(text) >= 1, "ОМС перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcОМС 1234 5678 9012 3452def"
        assertEquals(0, scanText(text), "ОМС внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123ОМС 1234 5678 9012 3452456"
        assertEquals(0, scanText(text), "ОМС внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Policy номер полиса 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "полис обязательного медицинского страхования 1234 5678 9012 3452 is valid"
        assertTrue(scanText(text) >= 1, "ОМС с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Policy, серия и номер полиса 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "номер полиса ОМС 1234 5678 9012 3452, next"
        assertTrue(scanText(text) >= 1, "ОМС с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Policy. ОМС № 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "омс 1234 5678 9012 3452."
        assertTrue(scanText(text) >= 1, "ОМС с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Policy; страховка 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "страхование 1234 5678 9012 3452; next"
        assertTrue(scanText(text) >= 1, "ОМС с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Policy: номер полиса обязательного медицинского страхования 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "ОМС 1234 5678 9012 3452!"
        assertTrue(scanText(text) >= 1, "ОМС с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "ОМС 1234 5678 9012 3452?"
        assertTrue(scanText(text) >= 1, "ОМС с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( полис обязательного медицинского страхования 1234 5678 9012 3452 )"
        assertTrue(scanText(text) >= 1, "ОМС в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(номер полиса 1234 5678 9012 3452)"
        assertTrue(scanText(text) >= 1, "ОМС в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" серия и номер полиса 1234 5678 9012 3452 \""
        assertTrue(scanText(text) >= 1, "ОМС в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"ОМС № 1234 5678 9012 3452\""
        assertTrue(scanText(text) >= 1, "ОМС в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "oms = страховка 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "oms # страховка 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ номер полиса ОМС 1234 5678 9012 3452 ]"
        assertTrue(scanText(text) >= 1, "ОМС в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ страхование 1234 5678 9012 3452 }"
        assertTrue(scanText(text) >= 1, "ОМС в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithColonAsPartOfFormat() {
        val text = "полис ОМС: 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с двоеточием как часть формата должен быть найден")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "омс-1234-5678-9012-3452"
        assertTrue(scanText(text) >= 1, "ОМС с дефисом как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "страховка 1234 5678 9012 3452 номер полиса 9876 5432 1098 7658"
        assertTrue(scanText(text) >= 2, "Несколько ОМС через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "серия и номер полиса 1234 5678 9012 3452, ОМС № 9876 5432 1098 7658"
        assertTrue(scanText(text) >= 2, "Несколько ОМС через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "номер полиса ОМС 1234 5678 9012 3452; страховка 9876 5432 1098 7658"
        assertTrue(scanText(text) >= 2, "Несколько ОМС через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "полис обязательного медицинского страхования 1234 5678 9012 3452\nстрахование 9876 5432 1098 7658"
        assertTrue(scanText(text) >= 2, "Несколько ОМС через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ОМС")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no OMS numbers at all"
        assertEquals(0, scanText(text), "Текст без ОМС не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "полис обязательного медицинского страхования 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithSpacesFormat() {
        val text = "номер полиса 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с пробелами должен быть найден")
    }

    @Test
    fun testWithDashesFormat() {
        val text = "серия и номер полиса ОМС 1234-5678-9012-3452"
        assertTrue(scanText(text) >= 1, "ОМС с дефисами должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Policy    номер полиса обязательного медицинского страхования 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Policy\tОМС № 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "ОМС № 1234 5678 9012 3452\tnext"
        assertTrue(scanText(text) >= 1, "ОМС с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Полис номер полиса ОМС 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Policy страховка 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "страховка 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "страхование 1234 5678 9012 3452 text"
        assertTrue(scanText(text) >= 1, "ОМС в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text омс 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС в конце текста должен быть найден")
    }

    @Test
    fun testWithPolisKeyword() {
        val text = "полис ОМС 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с ключевым словом 'полис ОМС' должен быть найден")
    }

    @Test
    fun testWithoutPolisAlone() {
        val text = "полис 1234 5678 9012 3452"
        assertEquals(0, scanText(text), "ОМС с отдельно стоящим 'полис' не должен находиться")
    }

    @Test
    fun testWithStrahovkaKeyword() {
        val text = "страховка 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с ключевым словом 'страховка' должен быть найден")
    }

    @Test
    fun testWithoutKeywords() {
        val text = "1234 5678 9012 3452"
        assertEquals(0, scanText(text), "ОМС без ключевых слов не должен находиться")
    }

    @Test
    fun testWithPolisObyazatelnogo() {
        val text = "полис обязательного медицинского страхования 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с ключевым словом 'полис обязательного медицинского страхования' должен быть найден")
    }

    @Test
    fun testWithNomerPolisaOMS() {
        val text = "номер полиса ОМС 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с ключевым словом 'номер полиса ОМС' должен быть найден")
    }

    @Test
    fun testWithSeriyaINomer() {
        val text = "серия и номер полиса 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с ключевым словом 'серия и номер полиса' должен быть найден")
    }

    @Test
    fun testWithOMS() {
        val text = "ОМС № 1234 5678 9012 3452"
        assertTrue(scanText(text) >= 1, "ОМС с ключевым словом 'ОМС №' должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "1234 5678 9012 345"
        assertEquals(0, scanText(text), "Слишком короткий ОМС не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "1234 5678 9012 34527"
        assertEquals(0, scanText(text), "Слишком длинный ОМС не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "1234 5678 9012 ABCD"
        assertEquals(0, scanText(text), "ОМС с буквами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "ОМС внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "oms1234 5678 9012 3452"
        assertEquals(0, scanText(text), "ОМС, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "1231234 5678 9012 3452"
        assertEquals(0, scanText(text), "ОМС, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "1234@5678@9012@3456"
        assertEquals(0, scanText(text), "ОМС с неправильными разделителями не должен находиться")
    }

    @Test
    fun testPartialOMS() {
        val text = "1234 5678"
        assertEquals(0, scanText(text), "Частичный ОМС не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function1234 5678 9012 3452()"
        assertEquals(0, scanText(text), "ОМС внутри кода не должен находиться")
    }
}

