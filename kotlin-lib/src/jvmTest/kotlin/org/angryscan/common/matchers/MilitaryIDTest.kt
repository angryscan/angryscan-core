package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера MilitaryID
 */
internal class MilitaryIDTest : MatcherTestBase(MilitaryID) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "удостоверение личности военнослужащего АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ в начале текста должно быть найдено")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Military ID: номер УЛВ АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ в конце текста должно быть найдено")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The ID УЛВ № АБ 3847291 is valid"
        assertTrue(scanText(text) >= 1, "УЛВ в середине текста должно быть найдено")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "военный билет АБ 3847291\nSecond line"
        assertTrue(scanText(text) >= 1, "УЛВ в начале строки должно быть найдено")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\nномер военного билета АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ в конце строки должно быть найдено")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with номер удостоверения личности военнослужащего АБ 3847291 ID"
        assertTrue(scanText(text) >= 1, "УЛВ в середине строки должно быть найдено")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\nсерия и номер удостоверения личности военнослужащего АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ после \\n должно быть найдено")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "удостоверение личности военнослужащего АБ 3847291\n"
        assertTrue(scanText(text) >= 1, "УЛВ перед \\n должно быть найдено")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\nномер УЛВ АБ 3847291\r\n"
        assertTrue(scanText(text) >= 1, "УЛВ с \\r\\n должно быть найдено")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\nУЛВ № АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ после пустой строки должно быть найдено")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "военный билет АБ 3847291\n\n"
        assertTrue(scanText(text) >= 1, "УЛВ перед пустой строкой должно быть найдено")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abcАБ 1234567def"
        assertEquals(0, scanText(text), "УЛВ внутри буквенной последовательности не должно находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123АБ 1234567456"
        assertEquals(0, scanText(text), "УЛВ внутри цифровой последовательности не должно находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "ID удостоверение личности военнослужащего АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с пробелом перед должно быть найдено")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "номер УЛВ АБ 3847291 is valid"
        assertTrue(scanText(text) >= 1, "УЛВ с пробелом после должно быть найдено")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "ID, УЛВ № АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с запятой перед должно быть найдено")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "военный билет АБ 3847291, next"
        assertTrue(scanText(text) >= 1, "УЛВ с запятой после должно быть найдено")
    }

    @Test
    fun testWithDotBefore() {
        val text = "ID. номер военного билета АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с точкой перед должно быть найдено")
    }

    @Test
    fun testWithDotAfter() {
        val text = "номер удостоверения личности военнослужащего АБ 3847291."
        assertTrue(scanText(text) >= 1, "УЛВ с точкой после должно быть найдено")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "ID; серия и номер удостоверения личности военнослужащего АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с точкой с запятой перед должно быть найдено")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "удостоверение личности военнослужащего АБ 3847291; next"
        assertTrue(scanText(text) >= 1, "УЛВ с точкой с запятой после должно быть найдено")
    }

    @Test
    fun testWithColonBefore() {
        val text = "ID: номер УЛВ АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с двоеточием перед должно быть найдено")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( удостоверение личности военнослужащего АБ 3847291 )"
        assertTrue(scanText(text) >= 1, "УЛВ в скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(номер УЛВ АБ 3847291)"
        assertTrue(scanText(text) >= 1, "УЛВ в скобках без пробелов должно быть найдено")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" УЛВ № АБ 3847291 \""
        assertTrue(scanText(text) >= 1, "УЛВ в кавычках с пробелами должно быть найдено")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"военный билет АБ 3847291\""
        assertTrue(scanText(text) >= 1, "УЛВ в кавычках без пробелов должно быть найдено")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ номер военного билета АБ 3847291 ]"
        assertTrue(scanText(text) >= 1, "УЛВ в квадратных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ номер удостоверения личности военнослужащего АБ 3847291 }"
        assertTrue(scanText(text) >= 1, "УЛВ в фигурных скобках с пробелами должно быть найдено")
    }

    @Test
    fun testWithDashAsPartOfFormat() {
        val text = "серия и номер удостоверения личности военнослужащего АБ-3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с дефисом как часть формата должно быть найдено")
    }

    @Test
    fun testWithNumberSignAsPartOfFormat() {
        val text = "удостоверение личности военнослужащего АБ № 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ со знаком № как часть формата должно быть найдено")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "удостоверение личности военнослужащего АБ 3847291 номер УЛВ ВГ 2913847"
        assertTrue(scanText(text) >= 2, "Несколько УЛВ через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "УЛВ № АБ 3847291, военный билет ВГ 2913847"
        assertTrue(scanText(text) >= 2, "Несколько УЛВ через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "номер удостоверения личности военнослужащего АБ 3847291; номер военного билета ВГ 2913847"
        assertTrue(scanText(text) >= 2, "Несколько УЛВ через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "серия и номер удостоверения личности военнослужащего АБ 3847291\nномер УЛВ ВГ 2913847"
        assertTrue(scanText(text) >= 2, "Несколько УЛВ через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать УЛВ")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no military IDs at all"
        assertEquals(0, scanText(text), "Текст без УЛВ не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "удостоверение личности военнослужащего АБ 3847291"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "ID    номер УЛВ АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с несколькими пробелами должно быть найдено")
    }

    @Test
    fun testWithTabBefore() {
        val text = "ID\tУЛВ № АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с табуляцией перед должно быть найдено")
    }

    @Test
    fun testWithTabAfter() {
        val text = "военный билет АБ 3847291\tnext"
        assertTrue(scanText(text) >= 1, "УЛВ с табуляцией после должно быть найдено")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Удостоверение номер удостоверения личности военнослужащего АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с кириллицей рядом должно быть найдено")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Military ID удостоверение личности военнослужащего АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с латиницей рядом должно быть найдено")
    }

    @Test
    fun testStandalone() {
        val text = "номер военного билета АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ отдельной строкой должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "серия и номер удостоверения личности военнослужащего АБ 3847291 text"
        assertTrue(scanText(text) >= 1, "УЛВ в начале текста должно быть найдено")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text удостоверение личности военнослужащего АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ в конце текста должно быть найдено")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "АБ 123456"
        assertEquals(0, scanText(text), "Слишком короткое УЛВ не должно находиться")
    }

    @Test
    fun testTooLong() {
        val text = "АБ 12345678"
        assertEquals(0, scanText(text), "Слишком длинное УЛВ не должно находиться")
    }

    @Test
    fun testWithInvalidFormat() {
        val text = "АБ@1234567"
        assertEquals(0, scanText(text), "УЛВ с неправильными разделителями не должно находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "idАБ 1234567"
        assertEquals(0, scanText(text), "УЛВ, прилипшее к буквам, не должно находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123АБ 1234567"
        assertEquals(0, scanText(text), "УЛВ, прилипшее к цифрам, не должно находиться")
    }

    @Test
    fun testPartialMilitaryID() {
        val text = "АБ"
        assertEquals(0, scanText(text), "Частичное УЛВ не должно находиться")
    }

    @Test
    fun testInCode() {
        val text = "functionАБ 1234567()"
        assertEquals(0, scanText(text), "УЛВ внутри кода не должно находиться")
    }

    // ========== 6. Валидация содержимого ==========

    @Test
    fun testValidMilitaryID() {
        val text = "удостоверение личности военнослужащего АБ 3847291"
        assertTrue(scanText(text) >= 1, "Валидное УЛВ должно быть найдено")
    }

    @Test
    fun testWithUdoverenieLichnosti() {
        val text = "удостоверение личности военнослужащего АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с ключевым словом 'удостоверение личности военнослужащего' должно быть найдено")
    }

    @Test
    fun testWithNomerULV() {
        val text = "номер УЛВ АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с ключевым словом 'номер УЛВ' должно быть найдено")
    }

    @Test
    fun testWithULV() {
        val text = "УЛВ № АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с ключевым словом 'УЛВ №' должно быть найдено")
    }

    @Test
    fun testWithVoenniyBilet() {
        val text = "военный билет АБ 3847291"
        assertTrue(scanText(text) >= 1, "УЛВ с ключевым словом 'военный билет' должно быть найдено")
    }

    @Test
    fun testTooManyZeros() {
        val text = "удостоверение личности военнослужащего АБ 0001234"
        assertEquals(0, scanText(text), "УЛВ с слишком большим количеством нулей не должно находиться")
    }

    @Test
    fun testAllSameDigits() {
        val text = "номер УЛВ АБ 2222222"
        assertEquals(0, scanText(text), "УЛВ со всеми одинаковыми цифрами не должно находиться")
    }

    @Test
    fun testOnlyZerosAndOnes() {
        val text = "УЛВ № АБ 0101010"
        assertEquals(0, scanText(text), "УЛВ только с нулями и единицами не должно находиться")
    }

    @Test
    fun testSequentialAscending() {
        val text = "военный билет АБ 1234567"
        assertEquals(0, scanText(text), "УЛВ с последовательными возрастающими цифрами не должно находиться")
    }

    @Test
    fun testSequentialDescending() {
        val text = "номер военного билета АБ 7654321"
        assertEquals(0, scanText(text), "УЛВ с последовательными убывающими цифрами не должно находиться")
    }

    @Test
    fun testRepeatingPattern2() {
        val text = "номер удостоверения личности военнослужащего АБ 1212121"
        assertEquals(0, scanText(text), "УЛВ с повторяющимся паттерном длиной 2 не должно находиться")
    }

    @Test
    fun testBlockOfSameDigits3() {
        val text = "серия и номер удостоверения личности военнослужащего АБ 1112345"
        assertEquals(0, scanText(text), "УЛВ с блоком из 3 одинаковых цифр не должно находиться")
    }

    @Test
    fun testBlockOfSameDigits4() {
        val text = "удостоверение личности военнослужащего АБ 1234444"
        assertEquals(0, scanText(text), "УЛВ с блоком из 4 одинаковых цифр не должно находиться")
    }

    @Test
    fun testPalindrome() {
        val text = "номер УЛВ АБ 1234321"
        assertEquals(0, scanText(text), "УЛВ-палиндром не должно находиться")
    }

    @Test
    fun testTooManySameDigits() {
        val text = "УЛВ № АБ 1111123"
        assertEquals(0, scanText(text), "УЛВ с слишком большим количеством одинаковых цифр не должно находиться")
    }


    @Test
    fun testValidComplexNumber() {
        val text = "военный билет АБ 3847291"
        assertTrue(scanText(text) >= 1, "Валидное УЛВ со сложным номером должно быть найдено")
    }

    @Test
    fun testValidWithSomeZeros() {
        val text = "номер военного билета АБ 1203456"
        assertTrue(scanText(text) >= 1, "Валидное УЛВ с некоторым количеством нулей должно быть найдено")
    }
}

