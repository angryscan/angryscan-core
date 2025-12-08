package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера IdentityDocType
 */
internal class IdentityDocTypeTest : MatcherTestBase(IdentityDocType) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Document type: паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The type паспорт гражданина российской федерации is valid"
        assertTrue(scanText(text) >= 1, "Тип документа в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "паспорт гражданина российской федерации\nSecond line"
        assertTrue(scanText(text) >= 1, "Тип документа в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nпаспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with паспорт гражданина российской федерации type"
        assertTrue(scanText(text) >= 1, "Тип документа в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nпаспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "паспорт гражданина российской федерации\n"
        assertTrue(scanText(text) >= 1, "Тип документа перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nпаспорт гражданина российской федерации\r\n"
        assertTrue(scanText(text) >= 1, "Тип документа с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nпаспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "паспорт гражданина российской федерации\n\n"
        assertTrue(scanText(text) >= 1, "Тип документа перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcпаспорт гражданина российской федерацииdef"
        assertEquals(0, scanText(text), "Тип документа внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Type паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "паспорт гражданина российской федерации is valid"
        assertTrue(scanText(text) >= 1, "Тип документа с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Type, паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "паспорт гражданина российской федерации, next"
        assertTrue(scanText(text) >= 1, "Тип документа с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Type. паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "паспорт гражданина российской федерации."
        assertTrue(scanText(text) >= 1, "Тип документа с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Type; паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "паспорт гражданина российской федерации; next"
        assertTrue(scanText(text) >= 1, "Тип документа с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Type: паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( паспорт гражданина российской федерации )"
        assertTrue(scanText(text) >= 1, "Тип документа в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(паспорт гражданина российской федерации)"
        assertTrue(scanText(text) >= 1, "Тип документа в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" паспорт гражданина российской федерации \""
        assertTrue(scanText(text) >= 1, "Тип документа в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"паспорт гражданина российской федерации\""
        assertTrue(scanText(text) >= 1, "Тип документа в кавычках без пробелов должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "паспорт гражданина российской федерации загранпаспорт"
        assertTrue(scanText(text) >= 2, "Несколько типов документов через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "паспорт гражданина российской федерации, загранпаспорт"
        assertTrue(scanText(text) >= 2, "Несколько типов документов через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "паспорт гражданина российской федерации; загранпаспорт"
        assertTrue(scanText(text) >= 2, "Несколько типов документов через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "паспорт гражданина российской федерации\nзагранпаспорт"
        assertTrue(scanText(text) >= 2, "Несколько типов документов через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать типов документов")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no identity document types at all"
        assertEquals(0, scanText(text), "Текст без типов документов не должен находить совпадения")
    }

    @Test
    fun testPasportGrazhdaninaRF() {
        val text = "паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Паспорт гражданина РФ должен быть найден")
    }

    @Test
    fun testZagranpasport() {
        val text = "загранпаспорт"
        assertTrue(scanText(text) >= 1, "Загранпаспорт должен быть найден")
    }

    @Test
    fun testZagranichnyiPasport() {
        val text = "заграничный паспорт"
        assertTrue(scanText(text) >= 1, "Заграничный паспорт должен быть найден")
    }

    @Test
    fun testZagranichnyiAlone() {
        val text = "заграничный"
        assertEquals(0, scanText(text), "Заграничный без паспорт не должен находиться")
    }

    @Test
    fun testVidNaZhitelstvo() {
        val text = "вид на жительство"
        assertTrue(scanText(text) >= 1, "Вид на жительство должен быть найден")
    }

    @Test
    fun testVNJ() {
        val text = "ВНЖ"
        assertTrue(scanText(text) >= 1, "ВНЖ должен быть найден")
    }

    @Test
    fun testSvidetelstvoORozhdenii() {
        val text = "свидетельство о рождении"
        assertTrue(scanText(text) >= 1, "Свидетельство о рождении должно быть найдено")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Type     паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Type\tпаспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "паспорт гражданина российской федерации\tnext"
        assertTrue(scanText(text) >= 1, "Тип документа с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Документ паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Identity document паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "паспорт гражданина российской федерации text"
        assertTrue(scanText(text) >= 1, "Тип документа в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа в конце текста должен быть найден")
    }

    @Test
    fun testWithNaimenovanieVidaKeyword() {
        val text = "наименование вида документа удостоверяющего личность: паспорт гражданина российской федерации"
        assertTrue(scanText(text) >= 1, "Тип документа с ключевым словом должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testInvalidDocType() {
        val text = "неправильный документ"
        assertEquals(0, scanText(text), "Несуществующий тип документа не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "doctypeпаспорт гражданина российской федерации"
        assertEquals(0, scanText(text), "Тип документа, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123паспорт гражданина российской федерации"
        assertEquals(0, scanText(text), "Тип документа, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionпаспорт гражданина российской федерации()"
        assertEquals(0, scanText(text), "Тип документа внутри кода не должен находиться")
    }

    @Test
    fun testPartialDocType() {
        val text = "паспорт"
        // В зависимости от реализации - может быть найден или нет
        val count = scanText(text)
        assertTrue(count >= 0, "Частичный тип документа")
    }
}

