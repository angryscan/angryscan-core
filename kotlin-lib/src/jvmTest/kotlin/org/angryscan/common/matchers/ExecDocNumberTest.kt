package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера ExecDocNumber
 */
internal class ExecDocNumberTest : MatcherTestBase(ExecDocNumber) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Document number: 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The document 1234/12/12345-ИП is valid"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "1234/12/12345-ИП\nSecond line"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 1234/12/12345-ИП document"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "1234/12/12345-ИП\n"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n1234/12/12345-ИП\r\n"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "1234/12/12345-ИП\n\n"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc1234/12/12345-ИПdef"
        assertEquals(0, scanText(text), "Номер исполнительного документа внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "1231234/12/12345-ИП456"
        assertEquals(0, scanText(text), "Номер исполнительного документа внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Document 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "1234/12/12345-ИП is valid"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Document, 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "1234/12/12345-ИП, next"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Document. 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "1234/12/12345-ИП."
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Document; 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "1234/12/12345-ИП; next"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Document: 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 1234/12/12345-ИП )"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(1234/12/12345-ИП)"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 1234/12/12345-ИП \""
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"1234/12/12345-ИП\""
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithSlashAsPartOfFormat() {
        val text = "1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа со слэшем как часть формата должен быть найден")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с дефисом как часть формата должен быть найден")
    }

    @Test
    fun testWithNumberSign() {
        val text = "№ 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа со знаком № должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "1234/12/12345-ИП 5678/34/56789-СВ"
        assertTrue(scanText(text) >= 2, "Несколько номеров исполнительных документов через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "1234/12/12345-ИП, 5678/34/56789-СВ"
        assertTrue(scanText(text) >= 2, "Несколько номеров исполнительных документов через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "1234/12/12345-ИП; 5678/34/56789-СВ"
        assertTrue(scanText(text) >= 2, "Несколько номеров исполнительных документов через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "1234/12/12345-ИП\n5678/34/56789-СВ"
        assertTrue(scanText(text) >= 2, "Несколько номеров исполнительных документов через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать номеров исполнительных документов")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no executive document numbers at all"
        assertEquals(0, scanText(text), "Текст без номеров исполнительных документов не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testMaximalFormat() {
        val text = "12345/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Максимальный формат должен быть найден")
    }

    @Test
    fun testWithIPType() {
        val text = "1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер с типом ИП должен быть найден")
    }

    @Test
    fun testWithSVType() {
        val text = "1234/12/12345-СВ"
        assertTrue(scanText(text) >= 1, "Номер с типом СВ должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Document    1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Document\t1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "1234/12/12345-ИП\tnext"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Документ 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Document 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "1234/12/12345-ИП text"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер исполнительного документа в конце текста должен быть найден")
    }

    @Test
    fun testWithIspolnitelnyListKeyword() {
        val text = "исполнительный лист: 1234/12/12345-ИП"
        assertTrue(scanText(text) >= 1, "Номер с ключевым словом должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidFormat() {
        val text = "1234@12@12345@ИП"
        assertEquals(0, scanText(text), "Номер исполнительного документа с неправильными разделителями не должен находиться")
    }

    @Test
    fun testTooShort() {
        val text = "123/12/12345-ИП"
        assertEquals(0, scanText(text), "Слишком короткий номер исполнительного документа не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "123456/12/12345-ИП"
        assertEquals(0, scanText(text), "Слишком длинный номер исполнительного документа не должен находиться")
    }

    @Test
    fun testWithInvalidType() {
        val text = "1234/12/12345-XX"
        assertEquals(0, scanText(text), "Номер исполнительного документа с недопустимым типом не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "doc1234/12/12345-ИП"
        assertEquals(0, scanText(text), "Номер исполнительного документа, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "1231234/12/12345-ИП"
        assertEquals(0, scanText(text), "Номер исполнительного документа, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testPartialExecDocNumber() {
        val text = "1234/12"
        assertEquals(0, scanText(text), "Частичный номер исполнительного документа не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function1234/12/12345-ИП()"
        assertEquals(0, scanText(text), "Номер исполнительного документа внутри кода не должен находиться")
    }
}

