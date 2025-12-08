package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера StateRegContract
 */
internal class StateRegContractTest : MatcherTestBase(StateRegContract) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Contract number: 12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The contract 12-34-56/123/2020-1 is valid"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "12-34-56/123/2020-1\nSecond line"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 12-34-56/123/2020-1 contract"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "12-34-56/123/2020-1\n"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n12-34-56/123/2020-1\r\n"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "12-34-56/123/2020-1\n\n"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc12-34-56/123/2020-1def"
        assertEquals(0, scanText(text), "Номер госрегистрации договора внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12312-34-56/123/2020-1456"
        assertEquals(0, scanText(text), "Номер госрегистрации договора внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Contract 12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "12-34-56/123/2020-1 is valid"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Contract, 12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "12-34-56/123/2020-1, next"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Contract. 12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "12-34-56/123/2020-1."
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Contract; 12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "12-34-56/123/2020-1; next"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Contract: 12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 12-34-56/123/2020-1 )"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(12-34-56/123/2020-1)"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 12-34-56/123/2020-1 \""
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"12-34-56/123/2020-1\""
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 12-34-56/123/2020-1 ]"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 12-34-56/123/2020-1 }"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с дефисом как часть формата должен быть найден")
    }

    @Test
    fun testWithColonAsPartOfFormat() {
        val text = "12:34:56/123/2020:1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с двоеточием как часть формата должен быть найден")
    }

    @Test
    fun testWithSlashAsPartOfFormat() {
        val text = "12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора со слэшем как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "12-34-56/123/2020-1 34-56-78/456/2021-2"
        assertTrue(scanText(text) >= 2, "Несколько номеров госрегистрации договоров через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "12-34-56/123/2020-1, 34-56-78/456/2021-2"
        assertTrue(scanText(text) >= 2, "Несколько номеров госрегистрации договоров через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "12-34-56/123/2020-1; 34-56-78/456/2021-2"
        assertTrue(scanText(text) >= 2, "Несколько номеров госрегистрации договоров через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "12-34-56/123/2020-1\n34-56-78/456/2021-2"
        assertTrue(scanText(text) >= 2, "Несколько номеров госрегистрации договоров через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать номеров госрегистрации договоров")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no state registration contract numbers at all"
        assertEquals(0, scanText(text), "Текст без номеров госрегистрации договоров не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWith3DigitNumber() {
        val text = "12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер с 3 цифрами должен быть найден")
    }

    @Test
    fun testWith4DigitNumber() {
        val text = "12-34-56/1234/2020-1"
        assertTrue(scanText(text) >= 1, "Номер с 4 цифрами должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Contract    12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Contract\t12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "12-34-56/123/2020-1\tnext"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Договор 12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Contract 12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "12-34-56/123/2020-1 text"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер госрегистрации договора в конце текста должен быть найден")
    }

    @Test
    fun testWithNomerGosregistraciiKeyword() {
        val text = "номер государственной регистрации договора: 12-34-56/123/2020-1"
        assertTrue(scanText(text) >= 1, "Номер с ключевым словом должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidFormat() {
        val text = "12@34@56@123@2020@1"
        assertEquals(0, scanText(text), "Номер госрегистрации договора с неправильными разделителями не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "12-34-56/123/2020"
        assertEquals(0, scanText(text), "Слишком короткий номер госрегистрации договора не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "contract12-34-56/123/2020-1"
        assertEquals(0, scanText(text), "Номер госрегистрации договора, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12312-34-56/123/2020-1"
        assertEquals(0, scanText(text), "Номер госрегистрации договора, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testPartialStateRegContract() {
        val text = "12-34-56"
        assertEquals(0, scanText(text), "Частичный номер госрегистрации договора не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function12-34-56/123/2020-1()"
        assertEquals(0, scanText(text), "Номер госрегистрации договора внутри кода не должен находиться")
    }
}

