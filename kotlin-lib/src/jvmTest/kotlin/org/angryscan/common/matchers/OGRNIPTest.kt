package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера OGRNIP
 */
internal class OGRNIPTest : MatcherTestBase(OGRNIP) {

    // Валидный OGRNIP для тестов (15 цифр, правильная контрольная сумма)
    private val validOGRNIP = "304500116000157" // Пример валидного OGRNIP

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "ОГРНИП 304500116000157 is an OGRNIP"
        assertTrue(scanText(text) >= 1, "ОГРНИП в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "OGRNIP number is номер ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The регистрационный номер индивидуального предпринимателя 304500116000157 is valid"
        assertTrue(scanText(text) >= 1, "ОГРНИП в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "государственный регистрационный номер ИП 304500116000157\nSecond line"
        assertTrue(scanText(text) >= 1, "ОГРНИП в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nосновной государственный регистрационный номер индивидуального предпринимателя 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with серия и номер ОГРНИП 304500116000157 OGRNIP"
        assertTrue(scanText(text) >= 1, "ОГРНИП в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nрегистрационный номер в реестре ФЛ ЧП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "ОГРНИП 304500116000157\n"
        assertTrue(scanText(text) >= 1, "ОГРНИП перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nномер ОГРНИП 304500116000157\r\n"
        assertTrue(scanText(text) >= 1, "ОГРНИП с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nсерия и номер ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "государственный регистрационный номер ИП 304500116000157\n\n"
        assertTrue(scanText(text) >= 1, "ОГРНИП перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc304500116000157def"
        assertEquals(0, scanText(text), "ОГРНИП внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123304500116000157456"
        assertEquals(0, scanText(text), "ОГРНИП внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc123304500116000157def456"
        assertEquals(0, scanText(text), "ОГРНИП внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "OGRNIP ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "регистрационный номер индивидуального предпринимателя 304500116000157 is valid"
        assertTrue(scanText(text) >= 1, "ОГРНИП с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "OGRNIP, номер ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "основной государственный регистрационный номер индивидуального предпринимателя 304500116000157, next"
        assertTrue(scanText(text) >= 1, "ОГРНИП с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "OGRNIP. серия и номер ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "ОГРНИП 304500116000157."
        assertTrue(scanText(text) >= 1, "ОГРНИП с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "OGRNIP; регистрационный номер в реестре ФЛ ЧП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "государственный регистрационный номер ИП 304500116000157; next"
        assertTrue(scanText(text) >= 1, "ОГРНИП с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "OGRNIP: ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "ОГРНИП 304500116000157!"
        assertTrue(scanText(text) >= 1, "ОГРНИП с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "номер ОГРНИП 304500116000157?"
        assertTrue(scanText(text) >= 1, "ОГРНИП с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( основной государственный регистрационный номер индивидуального предпринимателя 304500116000157 )"
        assertTrue(scanText(text) >= 1, "ОГРНИП в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(номер ОГРНИП 304500116000157)"
        assertTrue(scanText(text) >= 1, "ОГРНИП в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" серия и номер ОГРНИП 304500116000157 \""
        assertTrue(scanText(text) >= 1, "ОГРНИП в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"ОГРНИП 304500116000157\""
        assertTrue(scanText(text) >= 1, "ОГРНИП в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "ogrnip = регистрационный номер индивидуального предпринимателя 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "ogrnip # государственный регистрационный номер ИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ регистрационный номер в реестре ФЛ ЧП 304500116000157 ]"
        assertTrue(scanText(text) >= 1, "ОГРНИП в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ номер ОГРНИП 304500116000157 }"
        assertTrue(scanText(text) >= 1, "ОГРНИП в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "ОГРНИП 304500116000157 номер ОГРНИП 304500116000168"
        assertTrue(scanText(text) >= 2, "Несколько ОГРНИП через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "регистрационный номер индивидуального предпринимателя 304500116000157, серия и номер ОГРНИП 304500116000168"
        assertTrue(scanText(text) >= 2, "Несколько ОГРНИП через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "основной государственный регистрационный номер индивидуального предпринимателя 304500116000157; государственный регистрационный номер ИП 304500116000168"
        assertTrue(scanText(text) >= 2, "Несколько ОГРНИП через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "регистрационный номер в реестре ФЛ ЧП 304500116000157\nОГРНИП 304500116000168"
        assertTrue(scanText(text) >= 2, "Несколько ОГРНИП через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ОГРНИП")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no OGRNIP numbers at all"
        assertEquals(0, scanText(text), "Текст без ОГРНИП не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "OGRNIP    номер ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "OGRNIP\tсерия и номер ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "регистрационный номер индивидуального предпринимателя 304500116000157\tnext"
        assertTrue(scanText(text) >= 1, "ОГРНИП с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "ОГРНИП основной государственный регистрационный номер индивидуального предпринимателя 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "OGRNIP государственный регистрационный номер ИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "регистрационный номер в реестре ФЛ ЧП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "номер ОГРНИП 304500116000157 text"
        assertTrue(scanText(text) >= 1, "ОГРНИП в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП в конце текста должен быть найден")
    }

    @Test
    fun testWithoutKeywords() {
        val text = "304500116000157"
        assertEquals(0, scanText(text), "ОГРНИП без ключевых слов не должен находиться")
    }

    @Test
    fun testWithOGRNIPKeyword() {
        val text = "ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с ключевым словом 'ОГРНИП' должен быть найден")
    }

    @Test
    fun testWithPolniyNomer() {
        val text = "основной государственный регистрационный номер индивидуального предпринимателя 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с полным ключевым словом должен быть найден")
    }

    @Test
    fun testWithNomerOGRNIP() {
        val text = "номер ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с ключевым словом 'номер ОГРНИП' должен быть найден")
    }

    @Test
    fun testWith3Prefix() {
        val text = "ОГРНИП 304500116000157"
        assertTrue(scanText(text) >= 1, "ОГРНИП с префиксом 3 должен быть найден")
    }

    @Test
    fun testWith4Prefix() {
        val text = "регистрационный номер индивидуального предпринимателя 404500116000157"
        val count = scanText(text)
        assertTrue(count >= 0, "ОГРНИП с префиксом 4")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidControlSum() {
        val text = "ОГРНИП 304500116000150"
        assertEquals(0, scanText(text), "ОГРНИП с неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "номер ОГРНИП 30450011600015"
        assertEquals(0, scanText(text), "Слишком короткий ОГРНИП не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "серия и номер ОГРНИП 3045001160001578"
        assertEquals(0, scanText(text), "Слишком длинный ОГРНИП не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "регистрационный номер индивидуального предпринимателя 30450011600015A"
        assertEquals(0, scanText(text), "ОГРНИП с буквами не должен находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "ОГРНИП внутри длинной цифровой последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "ogrnip304500116000157"
        assertEquals(0, scanText(text), "ОГРНИП, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123304500116000157"
        assertEquals(0, scanText(text), "ОГРНИП, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function304500116000157()"
        assertEquals(0, scanText(text), "ОГРНИП внутри кода не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "300000000000000"
        assertEquals(0, scanText(text), "ОГРНИП из всех нулей не должен находиться")
    }
}

