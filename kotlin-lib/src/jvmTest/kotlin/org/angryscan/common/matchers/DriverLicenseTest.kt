package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера DriverLicense
 */
internal class DriverLicenseTest : MatcherTestBase(DriverLicense) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "водительское удостоверение 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Driver license: 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The license number 12 34 123456 is valid"
        assertTrue(scanText(text) >= 1, "ВУ в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "номер удостоверения 12 34 123456\nSecond line"
        assertTrue(scanText(text) >= 1, "ВУ в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nномер удостоверения 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with номер ВУ 12 34 123456 license"
        assertTrue(scanText(text) >= 1, "ВУ в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nВУ № 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "driver license 12 34 123456\n"
        assertTrue(scanText(text) >= 1, "ВУ перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nномер водительского удостоверения 12 34 123456\r\n"
        assertTrue(scanText(text) >= 1, "ВУ с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nномер водительского удостоверения 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "driving license 12 34 123456\n\n"
        assertTrue(scanText(text) >= 1, "ВУ перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcводительское удостоверение 12 34 123456def"
        assertEquals(0, scanText(text), "ВУ внутри буквенной последовательности не должно находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123номер удостоверения 12 34 123456456"
        assertEquals(0, scanText(text), "ВУ внутри цифровой последовательности не должно находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "License номер удостоверения 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "driver license 12 34 123456 is valid"
        assertTrue(scanText(text) >= 1, "ВУ с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "License, номер удостоверения 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "номер ВУ 12 34 123456, next"
        assertTrue(scanText(text) >= 1, "ВУ с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "License. ВУ № 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "license number 12 34 123456."
        assertTrue(scanText(text) >= 1, "ВУ с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "License; номер водительского удостоверения 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "номер водительского удостоверения 12 34 123456; next"
        assertTrue(scanText(text) >= 1, "ВУ с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "License: driving license 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с двоеточием перед должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( водительское удостоверение 12 34 123456 )"
        assertTrue(scanText(text) >= 1, "ВУ в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(номер удостоверения 12 34 123456)"
        assertTrue(scanText(text) >= 1, "ВУ в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" номер удостоверения 12 34 123456 \""
        assertTrue(scanText(text) >= 1, "ВУ в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"номер ВУ 12 34 123456\""
        assertTrue(scanText(text) >= 1, "ВУ в кавычках без пробелов должно быть найдено")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ ВУ № 12 34 123456 ]"
        assertTrue(scanText(text) >= 1, "ВУ в квадратных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ driver license 12 34 123456 }"
        assertTrue(scanText(text) >= 1, "ВУ в фигурных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithSpaceAsPartOfFormat() {
        val text = "license number 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с пробелом как часть формата должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "водительское удостоверение 12 34 123456 номер удостоверения 56 78 654321"
        assertTrue(scanText(text) >= 2, "Несколько ВУ через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "номер удостоверения 12 34 123456, номер ВУ 56 78 654321"
        assertTrue(scanText(text) >= 2, "Несколько ВУ через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "ВУ № 12 34 123456; driver license 56 78 654321"
        assertTrue(scanText(text) >= 2, "Несколько ВУ через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "номер водительского удостоверения 12 34 123456\nlicense number 56 78 654321"
        assertTrue(scanText(text) >= 2, "Несколько ВУ через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ВУ")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no driver licenses at all"
        assertEquals(0, scanText(text), "Текст без ВУ не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "водительское удостоверение 12 34 123456"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "License    номер удостоверения 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "License\tномер удостоверения 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "номер ВУ 12 34 123456\tnext"
        assertTrue(scanText(text) >= 1, "ВУ с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Водительское удостоверение 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Driver license 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "ВУ № 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "license number 12 34 123456 text"
        assertTrue(scanText(text) >= 1, "ВУ в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text driving license 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ в конце текста должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "12 34 12345"
        assertEquals(0, scanText(text), "Слишком короткое ВУ не должно находиться")
    }

    @Test
    fun testTooLong() {
        val text = "12 34 1234567"
        assertEquals(0, scanText(text), "Слишком длинное ВУ не должно находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "12 34 ABCDEF"
        assertEquals(0, scanText(text), "ВУ с буквами не должно находиться")
    }

    @Test
    fun testInsideLongNumericSequence() {
        val text = "123456789012345678901234567890"
        assertEquals(0, scanText(text), "ВУ внутри длинной цифровой последовательности не должно находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "licenseводительское удостоверение 12 34 123456"
        assertEquals(0, scanText(text), "ВУ, прилипшее к буквам, не должно находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123номер удостоверения 12 34 123456"
        assertEquals(0, scanText(text), "ВУ, прилипшее к цифрам, не должно находиться")
    }

    @Test
    fun testWithInvalidSeparators() {
        val text = "водительское удостоверение 12@34@123456"
        assertEquals(0, scanText(text), "ВУ с неправильными разделителями не должно находиться")
    }

    @Test
    fun testPartialDriverLicense() {
        val text = "номер удостоверения 12 34"
        assertEquals(0, scanText(text), "Частичное ВУ не должно находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionводительское удостоверение 12 34 123456()"
        assertEquals(0, scanText(text), "ВУ внутри кода не должно находиться")
    }

    @Test
    fun testWithoutKeywords() {
        val text = "12 34 123456"
        assertEquals(0, scanText(text), "ВУ без ключевых слов не должно находиться")
    }

    @Test
    fun testWithVoditelskoeUdo() {
        val text = "водительское удостоверение 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с ключевым словом 'водительское удостоверение' должно быть найдено")
    }

    @Test
    fun testWithNomerVoditelskogo() {
        val text = "номер водительского удостоверения 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с ключевым словом 'номер водительского удостоверения' должно быть найдено")
    }


    @Test
    fun testWithVU() {
        val text = "ВУ № 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с ключевым словом 'ВУ №' должно быть найдено")
    }

    @Test
    fun testWithDriverLicense() {
        val text = "driver license 12 34 123456"
        assertTrue(scanText(text) >= 1, "ВУ с ключевым словом 'driver license' должно быть найдено")
    }
}

