package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера EducationDoc
 */
internal class EducationDocTest : MatcherTestBase(EducationDoc) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "диплом 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Education document: номер диплома 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The document серия и номер диплома 123456 1234567 is valid"
        assertTrue(scanText(text) >= 1, "Документ об образовании в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "аттестат 123456 1234567\nSecond line"
        assertTrue(scanText(text) >= 1, "Документ об образовании в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nномер аттестата 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with свидетельство об образовании 123456 1234567 document"
        assertTrue(scanText(text) >= 1, "Документ об образовании в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nсертификат 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "удостоверение 123456 1234567\n"
        assertTrue(scanText(text) >= 1, "Документ об образовании перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nдокумент об образовании 123456 1234567\r\n"
        assertTrue(scanText(text) >= 1, "Документ об образовании с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nобразовательный документ 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "серия и номер диплома 123456 1234567\n\n"
        assertTrue(scanText(text) >= 1, "Документ об образовании перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc123456 1234567def"
        assertEquals(0, scanText(text), "Документ об образовании внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123123456 1234567456"
        assertEquals(0, scanText(text), "Документ об образовании внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Document номер диплома 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "диплом 123456 1234567 is valid"
        assertTrue(scanText(text) >= 1, "Документ об образовании с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Document, серия и номер диплома 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "аттестат 123456 1234567, next"
        assertTrue(scanText(text) >= 1, "Документ об образовании с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Document. номер аттестата 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "свидетельство об образовании 123456 1234567."
        assertTrue(scanText(text) >= 1, "Документ об образовании с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Document; сертификат 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "удостоверение 123456 1234567; next"
        assertTrue(scanText(text) >= 1, "Документ об образовании с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Document: документ об образовании 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( образовательный документ 123456 1234567 )"
        assertTrue(scanText(text) >= 1, "Документ об образовании в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(номер диплома 123456 1234567)"
        assertTrue(scanText(text) >= 1, "Документ об образовании в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" серия и номер диплома 123456 1234567 \""
        assertTrue(scanText(text) >= 1, "Документ об образовании в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"диплом 123456 1234567\""
        assertTrue(scanText(text) >= 1, "Документ об образовании в кавычках без пробелов должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "диплом 123456 1234567 аттестат 654321 7654321"
        assertTrue(scanText(text) >= 2, "Несколько документов об образовании через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "номер диплома 123456 1234567, серия и номер аттестата 654321 7654321"
        assertTrue(scanText(text) >= 2, "Несколько документов об образовании через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "свидетельство об образовании 123456 1234567; сертификат 654321 7654321"
        assertTrue(scanText(text) >= 2, "Несколько документов об образовании через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "удостоверение 123456 1234567\nдокумент об образовании 654321 7654321"
        assertTrue(scanText(text) >= 2, "Несколько документов об образовании через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать документов об образовании")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no education documents at all"
        assertEquals(0, scanText(text), "Текст без документов об образовании не должен находить совпадения")
    }

    @Test
    fun testFormat1() {
        val text = "диплом 123456 1234567"
        assertTrue(scanText(text) >= 1, "Формат 1 (6 цифр + 7 цифр) должен быть найден")
    }

    @Test
    fun testFormat2() {
        val text = "аттестат 12 АБ 1234567"
        assertTrue(scanText(text) >= 1, "Формат 2 (2 цифры + 2 буквы + 6-7 цифр) должен быть найден")
    }

    @Test
    fun testFormat3() {
        val text = "IV-АБ 123456"
        assertEquals(0, scanText(text), "Формат 3 (римские цифры + 2 буквы + 6 цифр) не должен находиться без ключевого слова")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Document    номер диплома 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "Document\tсерия и номер диплома 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "диплом 123456 1234567\tnext"
        assertTrue(scanText(text) >= 1, "Документ об образовании с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Документ диплом 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Education document аттестат 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "свидетельство об образовании 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "сертификат 123456 1234567 text"
        assertTrue(scanText(text) >= 1, "Документ об образовании в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text удостоверение 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании в конце текста должен быть найден")
    }

    @Test
    fun testWithDiplomKeyword() {
        val text = "диплом 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с ключевым словом 'диплом' должен быть найден")
    }

    @Test
    fun testWithAttestatKeyword() {
        val text = "аттестат 12 АБ 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с ключевым словом 'аттестат' должен быть найден")
    }

    @Test
    fun testWithNomerDiploma() {
        val text = "номер диплома 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с ключевым словом 'номер диплома' должен быть найден")
    }

    @Test
    fun testWithSeriyaINomer() {
        val text = "серия и номер диплома 123456 1234567"
        assertTrue(scanText(text) >= 1, "Документ об образовании с ключевым словом 'серия и номер диплома' должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "12345 123456"
        assertEquals(0, scanText(text), "Слишком короткий документ об образовании не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "1234567 12345678"
        assertEquals(0, scanText(text), "Слишком длинный документ об образовании не должен находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "123456@1234567"
        assertEquals(0, scanText(text), "Документ об образовании с неправильными разделителями не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "doc123456 1234567"
        assertEquals(0, scanText(text), "Документ об образовании, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123123456 1234567"
        assertEquals(0, scanText(text), "Документ об образовании, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testPartialEducationDoc() {
        val text = "123456"
        assertEquals(0, scanText(text), "Частичный документ об образовании не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function123456 1234567()"
        assertEquals(0, scanText(text), "Документ об образовании внутри кода не должен находиться")
    }
}

