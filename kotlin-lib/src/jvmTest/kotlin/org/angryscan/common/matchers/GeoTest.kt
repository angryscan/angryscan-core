package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера Geo
 */
internal class GeoTest : MatcherTestBase(Geo) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты в начале текста должны быть найдены")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Coordinates: 55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты в конце текста должны быть найдены")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The coordinates 55.7558, 37.6173 are valid"
        assertTrue(scanText(text) >= 1, "Геокоординаты в середине текста должны быть найдены")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "55.7558, 37.6173\nSecond line"
        assertTrue(scanText(text) >= 1, "Геокоординаты в начале строки должны быть найдены")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты в конце строки должны быть найдены")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 55.7558, 37.6173 coordinates"
        assertTrue(scanText(text) >= 1, "Геокоординаты в середине строки должны быть найдены")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты после \\n должны быть найдены")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "55.7558, 37.6173\n"
        assertTrue(scanText(text) >= 1, "Геокоординаты перед \\n должны быть найдены")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n55.7558, 37.6173\r\n"
        assertTrue(scanText(text) >= 1, "Геокоординаты с \\r\\n должны быть найдены")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты после пустой строки должны быть найдены")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "55.7558, 37.6173\n\n"
        assertTrue(scanText(text) >= 1, "Геокоординаты перед пустой строкой должны быть найдены")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc55.7558, 37.6173def"
        assertEquals(0, scanText(text), "Геокоординаты внутри буквенной последовательности не должны находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12355.7558, 37.6173456"
        assertEquals(0, scanText(text), "Геокоординаты внутри цифровой последовательности не должны находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Coordinates 55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с пробелом перед должны быть найдены")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "55.7558, 37.6173 are valid"
        assertTrue(scanText(text) >= 1, "Геокоординаты с пробелом после должны быть найдены")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Coordinates, 55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с запятой перед должны быть найдены")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "55.7558, 37.6173, next"
        assertTrue(scanText(text) >= 1, "Геокоординаты с запятой после должны быть найдены")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Coordinates. 55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с точкой перед должны быть найдены")
    }

    @Test
    fun testWithDotAfter() {
        val text = "55.7558, 37.6173."
        assertTrue(scanText(text) >= 1, "Геокоординаты с точкой после должны быть найдены")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Coordinates; 55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с точкой с запятой перед должны быть найдены")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "55.7558, 37.6173; next"
        assertTrue(scanText(text) >= 1, "Геокоординаты с точкой с запятой после должны быть найдены")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Coordinates: 55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с двоеточием перед должны быть найдены")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 55.7558, 37.6173 )"
        assertTrue(scanText(text) >= 1, "Геокоординаты в скобках с пробелами должны быть найдены")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(55.7558, 37.6173)"
        assertTrue(scanText(text) >= 1, "Геокоординаты в скобках без пробелов должны быть найдены")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 55.7558, 37.6173 \""
        assertTrue(scanText(text) >= 1, "Геокоординаты в кавычках с пробелами должны быть найдены")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"55.7558, 37.6173\""
        assertTrue(scanText(text) >= 1, "Геокоординаты в кавычках без пробелов должны быть найдены")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 55.7558, 37.6173 ]"
        assertTrue(scanText(text) >= 1, "Геокоординаты в квадратных скобках с пробелами должны быть найдены")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 55.7558, 37.6173 }"
        assertTrue(scanText(text) >= 1, "Геокоординаты в фигурных скобках с пробелами должны быть найдены")
    }

    @Test
    fun testWithCommaAsPartOfFormat() {
        val text = "55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с запятой как часть формата должны быть найдены")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "55.7558, 37.6173 59.9343, 30.3351"
        assertTrue(scanText(text) >= 2, "Несколько геокоординат через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "55.7558, 37.6173; 59.9343, 30.3351"
        assertTrue(scanText(text) >= 2, "Несколько геокоординат через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "55.7558, 37.6173\n59.9343, 30.3351"
        assertTrue(scanText(text) >= 2, "Несколько геокоординат через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать геокоординат")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no geo coordinates at all"
        assertEquals(0, scanText(text), "Текст без геокоординат не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithNegativeLatitude() {
        val text = "-55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с отрицательной широтой должны быть найдены")
    }

    @Test
    fun testWithNegativeLongitude() {
        val text = "55.7558, -37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с отрицательной долготой должны быть найдены")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Coordinates    55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с несколькими пробелами должны быть найдены")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Coordinates\t55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с табуляцией перед должны быть найдены")
    }

    @Test
    fun testWithTabAfter() {
        val text = "55.7558, 37.6173\tnext"
        assertTrue(scanText(text) >= 1, "Геокоординаты с табуляцией после должны быть найдены")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Геолокация 55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с кириллицей рядом должны быть найдены")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Geo coordinates 55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с латиницей рядом должны быть найдены")
    }

    @Test
    fun testStandalone() {
        val text = "55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты отдельной строкой должны быть найдены")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "55.7558, 37.6173 text"
        assertTrue(scanText(text) >= 1, "Геокоординаты в начале текста должны быть найдены")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты в конце текста должны быть найдены")
    }

    @Test
    fun testWithGeolokatsiyaKeyword() {
        val text = "геолокация фл: 55.7558, 37.6173"
        assertTrue(scanText(text) >= 1, "Геокоординаты с ключевым словом должны быть найдены")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidLatitude() {
        val text = "91.0, 37.6173"
        assertEquals(0, scanText(text), "Геокоординаты с недопустимой широтой не должны находиться")
    }

    @Test
    fun testInvalidLongitude() {
        val text = "55.7558, 181.0"
        assertEquals(0, scanText(text), "Геокоординаты с недопустимой долготой не должны находиться")
    }

    @Test
    fun testZeroZero() {
        val text = "0.0, 0.0"
        assertEquals(0, scanText(text), "Геокоординаты (0,0) не должны находиться")
    }

    @Test
    fun testIntegerCoordinatesWithoutDecimals() {
        val text = "55, 37"
        assertEquals(0, scanText(text), "Целочисленные координаты без десятичных знаков не должны находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "AB.CD, EF.GH"
        assertEquals(0, scanText(text), "Геокоординаты с буквами не должны находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "coord55.7558, 37.6173"
        assertEquals(0, scanText(text), "Геокоординаты, прилипшие к буквам, не должны находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12355.7558, 37.6173"
        assertEquals(0, scanText(text), "Геокоординаты, прилипшие к цифрам, не должны находиться")
    }

    @Test
    fun testInCode() {
        val text = "function55.7558, 37.6173()"
        assertEquals(0, scanText(text), "Геокоординаты внутри кода не должны находиться")
    }

    @Test
    fun testWithInvalidSeparator() {
        val text = "55.7558; 37.6173"
        assertEquals(0, scanText(text), "Геокоординаты с неправильным разделителем не должны находиться")
    }
}

