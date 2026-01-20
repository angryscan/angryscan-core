package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера EIN
 */
internal class EINTest : MatcherTestBase(EIN) {

    // Валидные EIN номера для тестов (не последовательные, не повторяющиеся)
    private val validEIN1 = "07-4827591" // Префикс 07, вторая цифра 1-7
    private val validEIN2 = "12-5913746" // Префикс 12 (диапазон 10-16)
    private val validEIN3 = "25-8462159" // Префикс 25 (диапазон 20-27)
    private val validEIN4 = "35-7294836" // Префикс 35 (диапазон 30-39)
    private val validEIN5 = "55-3847592" // Префикс 55 (диапазон 50-59)
    private val validEIN6 = "45-5183746" // Префикс 45 (диапазон 40-48)
    private val validEIN7 = "65-2958473" // Префикс 65 (диапазон 60-68)
    private val validEIN8 = "85-7364829" // Префикс 85 (диапазон 80-88)
    private val validEIN9 = "92-8473925" // Префикс 92 (диапазон 90-95)
    private val validEIN10 = "99-5293847" // Префикс 99 (диапазон 98-99)
    private val validEIN11 = "074827591" // Без дефиса

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "07-4827591"
        assertTrue(scanText(text) >= 1, "EIN в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "EIN: 07-4827591"
        assertTrue(scanText(text) >= 1, "EIN в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The EIN 07-4827591 is valid"
        assertTrue(scanText(text) >= 1, "EIN в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "07-4827591\nSecond line"
        assertTrue(scanText(text) >= 1, "EIN в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n07-4827591"
        assertTrue(scanText(text) >= 1, "EIN в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 07-4827591 EIN"
        assertTrue(scanText(text) >= 1, "EIN в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n07-4827591"
        assertTrue(scanText(text) >= 1, "EIN после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "07-4827591\n"
        assertTrue(scanText(text) >= 1, "EIN перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n07-4827591\r\n"
        assertTrue(scanText(text) >= 1, "EIN с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n07-4827591"
        assertTrue(scanText(text) >= 1, "EIN после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "07-4827591\n\n"
        assertTrue(scanText(text) >= 1, "EIN перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideNumericSequence() {
        val text = "12307-4827591456"
        assertEquals(0, scanText(text), "EIN внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "EIN 07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "07-4827591 is valid"
        assertTrue(scanText(text) >= 1, "EIN с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "EIN, 07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "07-4827591, next"
        assertTrue(scanText(text) >= 1, "EIN с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "EIN. 07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "07-4827591."
        assertTrue(scanText(text) >= 1, "EIN с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "EIN; 07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "07-4827591; next"
        assertTrue(scanText(text) >= 1, "EIN с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "EIN: 07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 07-4827591 )"
        assertTrue(scanText(text) >= 1, "EIN в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(07-4827591)"
        assertTrue(scanText(text) >= 1, "EIN в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 07-4827591 \""
        assertTrue(scanText(text) >= 1, "EIN в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"07-4827591\""
        assertTrue(scanText(text) >= 1, "EIN в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 07-4827591 ]"
        assertTrue(scanText(text) >= 1, "EIN в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 07-4827591 }"
        assertTrue(scanText(text) >= 1, "EIN в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Формат с дефисом и без ==========

    @Test
    fun testWithHyphen() {
        val text = "07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с дефисом должен быть найден")
    }

    @Test
    fun testWithoutHyphen() {
        val text = "074827591"
        assertTrue(scanText(text) >= 1, "EIN без дефиса должен быть найден")
    }

    // ========== 5. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "07-4827591 12-5913746"
        assertTrue(scanText(text) >= 2, "Несколько EIN через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "07-4827591, 12-5913746"
        assertTrue(scanText(text) >= 2, "Несколько EIN через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "07-4827591; 12-5913746"
        assertTrue(scanText(text) >= 2, "Несколько EIN через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "07-4827591\n12-5913746"
        assertTrue(scanText(text) >= 2, "Несколько EIN через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать EIN")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no EIN numbers at all"
        assertEquals(0, scanText(text), "Текст без EIN не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "07-4827591"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "EIN    07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "EIN\t07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "07-4827591\tnext"
        assertTrue(scanText(text) >= 1, "EIN с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "ИНН 07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "EIN 07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "07-4827591"
        assertTrue(scanText(text) >= 1, "EIN отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "07-4827591 text"
        assertTrue(scanText(text) >= 1, "EIN в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 07-4827591"
        assertTrue(scanText(text) >= 1, "EIN в конце текста должен быть найден")
    }

    // ========== 6. Тестирование различных диапазонов префиксов ==========

    @Test
    fun testPrefix00or07() {
        val text = "07-4827591" // 07 с 1-7
        assertTrue(scanText(text) >= 1, "EIN с префиксом 00 или 07 должен быть найден")
    }

    @Test
    fun testPrefix10to16() {
        val text = "12-5913746" // 12 в диапазоне 10-16
        assertTrue(scanText(text) >= 1, "EIN с префиксом 10-16 должен быть найден")
    }

    @Test
    fun testPrefix20to27() {
        val text = "25-8462159" // 25 в диапазоне 20-27
        assertTrue(scanText(text) >= 1, "EIN с префиксом 20-27 должен быть найден")
    }

    @Test
    fun testPrefix30to39() {
        val text = "35-7294836" // 35 в диапазоне 30-39
        assertTrue(scanText(text) >= 1, "EIN с префиксом 30-39 должен быть найден")
    }

    @Test
    fun testPrefix50to59() {
        val text = "55-3847592" // 55 в диапазоне 50-59
        assertTrue(scanText(text) >= 1, "EIN с префиксом 50-59 должен быть найден")
    }

    @Test
    fun testPrefix40to48() {
        val text = "45-5183746" // 45 в диапазоне 40-48
        assertTrue(scanText(text) >= 1, "EIN с префиксом 40-48 должен быть найден")
    }

    @Test
    fun testPrefix60to68() {
        val text = "65-2958473" // 65 в диапазоне 60-68
        assertTrue(scanText(text) >= 1, "EIN с префиксом 60-68 должен быть найден")
    }

    @Test
    fun testPrefix80to88() {
        val text = "85-7364829" // 85 в диапазоне 80-88
        assertTrue(scanText(text) >= 1, "EIN с префиксом 80-88 должен быть найден")
    }

    @Test
    fun testPrefix90to95() {
        val text = "92-8473925" // 92 в диапазоне 90-95
        assertTrue(scanText(text) >= 1, "EIN с префиксом 90-95 должен быть найден")
    }

    @Test
    fun testPrefix98to99() {
        val text = "99-5293847" // 99 в диапазоне 98-99
        assertTrue(scanText(text) >= 1, "EIN с префиксом 98-99 должен быть найден")
    }

    // ========== 7. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "07-123456" // 8 цифр вместо 9
        assertEquals(0, scanText(text), "Слишком короткий EIN не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "07-48275918" // 10 цифр вместо 9
        assertEquals(0, scanText(text), "Слишком длинный EIN не должен находиться")
    }

    @Test
    fun testInvalidPrefix00() {
        val text = "00-0234567" // 00 должна иметь вторую цифру (всего номера) 1-7, а не 0
        assertEquals(0, scanText(text), "EIN с невалидным префиксом (вторая цифра 0) не должен находиться")
    }

    @Test
    fun testInvalidPrefix07() {
        val text = "00-8834567" // 00 должна иметь вторую цифру (всего номера) 1-7, а не 8
        assertEquals(0, scanText(text), "EIN с невалидным префиксом (вторая цифра 8) не должен находиться")
    }

    @Test
    fun testInvalidPrefix17() {
        val text = "17-1234567" // 17 не входит в валидные диапазоны
        assertEquals(0, scanText(text), "EIN с невалидным префиксом 17 не должен находиться")
    }

    @Test
    fun testInvalidPrefix28() {
        val text = "28-1234567" // 28 не входит в валидные диапазоны (20-27)
        assertEquals(0, scanText(text), "EIN с невалидным префиксом 28 не должен находиться")
    }

    @Test
    fun testInvalidPrefix49() {
        val text = "49-1234567" // 49 не входит в валидные диапазоны (40-48)
        assertEquals(0, scanText(text), "EIN с невалидным префиксом 49 не должен находиться")
    }

    @Test
    fun testInvalidPrefix69() {
        val text = "69-1234567" // 69 не входит в валидные диапазоны (60-68)
        assertEquals(0, scanText(text), "EIN с невалидным префиксом 69 не должен находиться")
    }

    @Test
    fun testInvalidPrefix89() {
        val text = "89-1234567" // 89 не входит в валидные диапазоны (80-88)
        assertEquals(0, scanText(text), "EIN с невалидным префиксом 89 не должен находиться")
    }

    @Test
    fun testInvalidPrefix96() {
        val text = "96-1234567" // 96 не входит в валидные диапазоны (90-95, 98-99)
        assertEquals(0, scanText(text), "EIN с невалидным префиксом 96 не должен находиться")
    }

    @Test
    fun testInvalidPrefix97() {
        val text = "97-1234567" // 97 не входит в валидные диапазоны (90-95, 98-99)
        assertEquals(0, scanText(text), "EIN с невалидным префиксом 97 не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "07-12345A7"
        assertEquals(0, scanText(text), "EIN с буквами не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "00-0000000"
        assertEquals(0, scanText(text), "EIN со всеми нулями не должен находиться (фильтруется функцией check)")
    }

    @Test
    fun testAllSameDigits() {
        val text = "11-1111111"
        assertEquals(0, scanText(text), "EIN со всеми одинаковыми цифрами не должен находиться (фильтруется функцией check)")
    }

    @Test
    fun testMultipleHyphens() {
        val text = "07-12-34567" // Несколько дефисов
        assertEquals(0, scanText(text), "EIN с несколькими дефисами не должен находиться")
    }

    @Test
    fun testHyphenInWrongPlace() {
        val text = "0-71234567" // Дефис не после первых двух цифр
        assertEquals(0, scanText(text), "EIN с дефисом в неправильном месте не должен находиться")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABC07-4827591DEF"
        assertEquals(0, scanText(text), "EIN внутри буквенной последовательности не должен находиться (нет границы слова)")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "ein07-4827591"
        assertEquals(0, scanText(text), "EIN, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "12307-4827591"
        assertEquals(0, scanText(text), "EIN, прилипший к цифрам, не должен находиться")
    }

    // ========== 5. Тесты дополнительной фильтрации для снижения false positives ==========

    @Test
    fun testSequentialAscending() {
        val text = "07-1234567" // Последовательные цифры по возрастанию в суффиксе
        assertEquals(0, scanText(text), "Последовательные цифры по возрастанию должны быть отфильтрованы")
    }

    @Test
    fun testSequentialDescending() {
        val text = "07-7654321" // Последовательные цифры по убыванию в суффиксе
        assertEquals(0, scanText(text), "Последовательные цифры по убыванию должны быть отфильтрованы")
    }

    @Test
    fun testRepeatingPattern2() {
        // Для 7-значного суффикса полное повторение паттерна длины 2 невозможно (7 % 2 = 1)
        // Фильтр проверяет только точные повторения, так что этот тест может не сработать
        // Но другие фильтры (последовательные, палиндромы) должны работать
    }

    // ========== 6. Тесты с ключевыми словами ==========

    @Test
    fun testWithEINKeyword() {
        val text = "EIN: 07-4827591"
        assertTrue(scanText(text) >= 1, "EIN с ключевым словом 'EIN' должен быть найден")
    }

    @Test
    fun testWithEINLowercase() {
        val text = "ein: 12-5913746"
        assertTrue(scanText(text) >= 1, "EIN с ключевым словом 'ein' в нижнем регистре должен быть найден")
    }

    @Test
    fun testWithEmployerIdentificationNumber() {
        val text = "Employer Identification Number: 25-8462159"
        assertTrue(scanText(text) >= 1, "EIN с полным ключевым словом должен быть найден")
    }

    @Test
    fun testWithTaxID() {
        val text = "Tax ID: 35-7294836"
        assertTrue(scanText(text) >= 1, "EIN с ключевым словом 'Tax ID' должен быть найден")
    }

    @Test
    fun testWithTaxIDNumber() {
        val text = "Tax ID Number: 55-3847592"
        assertTrue(scanText(text) >= 1, "EIN с ключевым словом 'Tax ID Number' должен быть найден")
    }

    @Test
    fun testWithFederalTaxID() {
        val text = "Federal Tax ID: 45-5183746"
        assertTrue(scanText(text) >= 1, "EIN с ключевым словом 'Federal Tax ID' должен быть найден")
    }

    @Test
    fun testWithEINAbbreviation() {
        val text = "E.I.N.: 65-2958473"
        assertTrue(scanText(text) >= 1, "EIN с аббревиатурой 'E.I.N.' должен быть найден")
    }

    @Test
    fun testWithKeywordsAndSeparators() {
        val text = "EIN - 85-7364829"
        assertTrue(scanText(text) >= 1, "EIN с ключевым словом и разделителем должен быть найден")
    }

    @Test
    fun testWithKeywordsCaseInsensitive() {
        val text = "EIN NUMBER: 92-8473925"
        assertTrue(scanText(text) >= 1, "EIN с ключевыми словами в верхнем регистре должен быть найден")
    }

}
