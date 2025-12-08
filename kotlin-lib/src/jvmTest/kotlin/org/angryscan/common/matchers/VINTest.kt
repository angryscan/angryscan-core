package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера VIN
 */
internal class VINTest : MatcherTestBase(VIN) {

    // Валидный VIN для тестов (17 символов, правильная контрольная сумма)
    private val validVIN = "1HGBH41JXMN109186" // Пример валидного VIN

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "1HGBH41JXMN109186 is a VIN"
        assertTrue(scanText(text) >= 1, "VIN в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "VIN number is 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The VIN 1HGBH41JXMN109186 is valid"
        assertTrue(scanText(text) >= 1, "VIN в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "1HGBH41JXMN109186\nSecond line"
        assertTrue(scanText(text) >= 1, "VIN в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 1HGBH41JXMN109186 VIN"
        assertTrue(scanText(text) >= 1, "VIN в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "1HGBH41JXMN109186\n"
        assertTrue(scanText(text) >= 1, "VIN перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n1HGBH41JXMN109186\r\n"
        assertTrue(scanText(text) >= 1, "VIN с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "1HGBH41JXMN109186\n\n"
        assertTrue(scanText(text) >= 1, "VIN перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc1HGBH41JXMN109186def"
        assertEquals(0, scanText(text), "VIN внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "1231HGBH41JXMN109186456"
        assertEquals(0, scanText(text), "VIN внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc1231HGBH41JXMN109186def456"
        assertEquals(0, scanText(text), "VIN внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "VIN 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "1HGBH41JXMN109186 is valid"
        assertTrue(scanText(text) >= 1, "VIN с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "VIN, 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "1HGBH41JXMN109186, next"
        assertTrue(scanText(text) >= 1, "VIN с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "VIN. 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "1HGBH41JXMN109186."
        assertTrue(scanText(text) >= 1, "VIN с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "VIN; 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "1HGBH41JXMN109186; next"
        assertTrue(scanText(text) >= 1, "VIN с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "VIN: 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "1HGBH41JXMN109186!"
        assertTrue(scanText(text) >= 1, "VIN с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "1HGBH41JXMN109186?"
        assertTrue(scanText(text) >= 1, "VIN с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 1HGBH41JXMN109186 )"
        assertTrue(scanText(text) >= 1, "VIN в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(1HGBH41JXMN109186)"
        assertTrue(scanText(text) >= 1, "VIN в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 1HGBH41JXMN109186 \""
        assertTrue(scanText(text) >= 1, "VIN в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"1HGBH41JXMN109186\""
        assertTrue(scanText(text) >= 1, "VIN в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "vin = 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "vin # 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 1HGBH41JXMN109186 ]"
        assertTrue(scanText(text) >= 1, "VIN в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 1HGBH41JXMN109186 }"
        assertTrue(scanText(text) >= 1, "VIN в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать VIN")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no VIN numbers at all"
        assertEquals(0, scanText(text), "Текст без VIN не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "VIN    1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "VIN\t1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "1HGBH41JXMN109186\tnext"
        assertTrue(scanText(text) >= 1, "VIN с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "VIN номер 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "VIN number 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "1HGBH41JXMN109186 text"
        assertTrue(scanText(text) >= 1, "VIN в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "VIN в конце текста должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidChecksum() {
        val text = "1HGBH41JXMN109180"
        assertEquals(0, scanText(text), "VIN с неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "1HGBH41JXMN10918"
        assertEquals(0, scanText(text), "Слишком короткий VIN не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "1HGBH41JXMN1091867"
        assertEquals(0, scanText(text), "Слишком длинный VIN не должен находиться")
    }

    @Test
    fun testWithInvalidChars() {
        val text = "1HGBH41JXMN10918I"
        assertEquals(0, scanText(text), "VIN с недопустимыми символами (I, O, Q) не должен находиться")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABC1HGBH41JXMN109186DEF"
        assertEquals(0, scanText(text), "VIN внутри длинной последовательности не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "vin1HGBH41JXMN109186"
        assertEquals(0, scanText(text), "VIN, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "1231HGBH41JXMN109186"
        assertEquals(0, scanText(text), "VIN, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function1HGBH41JXMN109186()"
        assertEquals(0, scanText(text), "VIN внутри кода не должен находиться")
    }

    @Test
    fun testWithSpaces() {
        val text = "1HGBH 41JX MN109186"
        assertEquals(0, scanText(text), "VIN с пробелами не должен находиться")
    }

    @Test
    fun testOnlyDigits() {
        val text = "12345678901234567"
        assertEquals(0, scanText(text), "VIN состоящий только из цифр не должен находиться")
    }

    @Test
    fun testOnlyZerosAndOnes() {
        val text = "00000000000000000"
        assertEquals(0, scanText(text), "VIN состоящий только из нулей не должен находиться")
    }

    @Test
    fun testOnlyZerosAndOnesWithLetters() {
        // VIN с буквами, но все цифры - только 0 и 1
        // Используем VIN, который не пройдет проверку контрольной суммы, но имеет только 0 и 1
        val text = "ABCDEFGHJKLMNPR01"
        assertEquals(0, scanText(text), "VIN где все цифры только 0 и 1 не должен находиться")
    }

    @Test
    fun testOnlyOneDigit() {
        // VIN с только одной цифрой (остальные буквы)
        val text = "ABCDEFGHJKLMNPRS2"
        // Этот VIN может пройти паттерн, но должен быть отфильтрован в check
        // Проверяем, что он не находится
        assertEquals(0, scanText(text), "VIN с только одной цифрой не должен находиться")
    }

    @Test
    fun testValidVINWithLettersAndDigits() {
        val text = "1HGBH41JXMN109186"
        assertTrue(scanText(text) >= 1, "Валидный VIN с буквами и цифрами должен быть найден")
    }
}

