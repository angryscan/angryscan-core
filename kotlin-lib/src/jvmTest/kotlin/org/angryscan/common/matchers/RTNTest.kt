package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера RTN
 */
internal class RTNTest : MatcherTestBase(RTN) {

    // Валидные RTN номера для тестов (с правильной контрольной суммой и разнообразными цифрами)
    private val validRTN1 = "076401251" // Известный валидный RTN: первые 2 цифры 07 (в диапазоне 00-09)
    private val validRTN2 = "021000005" // Первые 2 цифры: 02 (в диапазоне 00-09), проверена контрольная сумма
    private val validRTN3 = "112847614" // Первые 2 цифры: 11 (в диапазоне 10-12), проверена контрольная сумма
    private val validRTN4 = "234859726" // Первые 2 цифры: 23 (в диапазоне 21-29), проверена контрольная сумма
    private val validRTN5 = "301728492" // Первые 2 цифры: 30 (в диапазоне 30-32), проверена контрольная сумма
    private val validRTN6 = "653847296" // Первые 2 цифры: 65 (в диапазоне 61-69), проверена контрольная сумма
    private val validRTN7 = "715729469" // Первые 2 цифры: 71 (в диапазоне 70-72), проверена контрольная сумма
    private val validRTN8 = "802938477" // Первые 2 цифры: 80, проверена контрольная сумма

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "076401251"
        assertTrue(scanText(text) >= 1, "RTN в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "RTN: 076401251"
        assertTrue(scanText(text) >= 1, "RTN в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The RTN 076401251 is valid"
        assertTrue(scanText(text) >= 1, "RTN в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "076401251\nSecond line"
        assertTrue(scanText(text) >= 1, "RTN в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n076401251"
        assertTrue(scanText(text) >= 1, "RTN в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with 076401251 RTN"
        assertTrue(scanText(text) >= 1, "RTN в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n076401251"
        assertTrue(scanText(text) >= 1, "RTN после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "076401251\n"
        assertTrue(scanText(text) >= 1, "RTN перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n076401251\r\n"
        assertTrue(scanText(text) >= 1, "RTN с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n076401251"
        assertTrue(scanText(text) >= 1, "RTN после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "076401251\n\n"
        assertTrue(scanText(text) >= 1, "RTN перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123076401251456"
        assertEquals(0, scanText(text), "RTN внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "RTN 076401251"
        assertTrue(scanText(text) >= 1, "RTN с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "076401251 is valid"
        assertTrue(scanText(text) >= 1, "RTN с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "RTN, 076401251"
        assertTrue(scanText(text) >= 1, "RTN с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "076401251, next"
        assertTrue(scanText(text) >= 1, "RTN с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "RTN. 076401251"
        assertTrue(scanText(text) >= 1, "RTN с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "076401251."
        assertTrue(scanText(text) >= 1, "RTN с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "RTN; 076401251"
        assertTrue(scanText(text) >= 1, "RTN с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "076401251; next"
        assertTrue(scanText(text) >= 1, "RTN с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "RTN: 076401251"
        assertTrue(scanText(text) >= 1, "RTN с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( 076401251 )"
        assertTrue(scanText(text) >= 1, "RTN в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(076401251)"
        assertTrue(scanText(text) >= 1, "RTN в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" 076401251 \""
        assertTrue(scanText(text) >= 1, "RTN в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"076401251\""
        assertTrue(scanText(text) >= 1, "RTN в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ 076401251 ]"
        assertTrue(scanText(text) >= 1, "RTN в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ 076401251 }"
        assertTrue(scanText(text) >= 1, "RTN в фигурных скобках с пробелами должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "076401251 112847614"
        assertTrue(scanText(text) >= 2, "Несколько RTN через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "076401251, 112847614"
        assertTrue(scanText(text) >= 2, "Несколько RTN через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "076401251; 112847614"
        assertTrue(scanText(text) >= 2, "Несколько RTN через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "076401251\n112847614"
        assertTrue(scanText(text) >= 2, "Несколько RTN через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать RTN")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no RTN numbers at all"
        assertEquals(0, scanText(text), "Текст без RTN не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "076401251"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "RTN    076401251"
        assertTrue(scanText(text) >= 1, "RTN с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "RTN\t076401251"
        assertTrue(scanText(text) >= 1, "RTN с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "076401251\tnext"
        assertTrue(scanText(text) >= 1, "RTN с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "РТН 076401251"
        assertTrue(scanText(text) >= 1, "RTN с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "RTN 076401251"
        assertTrue(scanText(text) >= 1, "RTN с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "076401251"
        assertTrue(scanText(text) >= 1, "RTN отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "076401251 text"
        assertTrue(scanText(text) >= 1, "RTN в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text 076401251"
        assertTrue(scanText(text) >= 1, "RTN в конце текста должен быть найден")
    }

    // ========== 5. Тестирование различных диапазонов первых двух цифр ==========

    @Test
    fun testRange00to09() {
        val text = "076401251" // 07 в диапазоне 00-09, известный валидный RTN
        assertTrue(scanText(text) >= 1, "RTN с первыми цифрами 00-09 должен быть найден")
    }

    @Test
    fun testRange10to12() {
        val text = "112847614" // 11 в диапазоне 10-12, валидная контрольная сумма
        assertTrue(scanText(text) >= 1, "RTN с первыми цифрами 10-12 должен быть найден")
    }

    @Test
    fun testRange21to29() {
        val text = "234859726" // 23 в диапазоне 21-29, валидная контрольная сумма
        assertTrue(scanText(text) >= 1, "RTN с первыми цифрами 21-29 должен быть найден")
    }

    @Test
    fun testRange30to32() {
        val text = "301728492" // 30 в диапазоне 30-32, валидная контрольная сумма
        assertTrue(scanText(text) >= 1, "RTN с первыми цифрами 30-32 должен быть найден")
    }

    @Test
    fun testRange61to69() {
        val text = "653847296" // 65 в диапазоне 61-69, валидная контрольная сумма
        assertTrue(scanText(text) >= 1, "RTN с первыми цифрами 61-69 должен быть найден")
    }

    @Test
    fun testRange70to72() {
        val text = "715729469" // 71 в диапазоне 70-72, валидная контрольная сумма
        assertTrue(scanText(text) >= 1, "RTN с первыми цифрами 70-72 должен быть найден")
    }

    @Test
    fun testExact80() {
        val text = "802938477" // точно 80, валидная контрольная сумма
        assertTrue(scanText(text) >= 1, "RTN с первыми цифрами 80 должен быть найден")
    }

    // ========== 6. Негативные сценарии ==========

    @Test
    fun testTooShort() {
        val text = "02100002" // 8 цифр вместо 9
        assertEquals(0, scanText(text), "Слишком короткий RTN не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "0764012510" // 10 цифр вместо 9
        assertEquals(0, scanText(text), "Слишком длинный RTN не должен находиться")
    }

    @Test
    fun testInvalidFirstDigitPair00() {
        val text = "131000025" // 13 не входит ни в один валидный диапазон
        assertEquals(0, scanText(text), "RTN с невалидными первыми двумя цифрами не должен находиться")
    }

    @Test
    fun testInvalidFirstDigitPair01() {
        val text = "201000025" // 20 не входит ни в один валидный диапазон
        assertEquals(0, scanText(text), "RTN с невалидными первыми двумя цифрами (20) не должен находиться")
    }

    @Test
    fun testInvalidFirstDigitPair02() {
        val text = "331000025" // 33 не входит ни в один валидный диапазон
        assertEquals(0, scanText(text), "RTN с невалидными первыми двумя цифрами (33) не должен находиться")
    }

    @Test
    fun testInvalidFirstDigitPair03() {
        val text = "731000025" // 73 не входит ни в один валидный диапазон
        assertEquals(0, scanText(text), "RTN с невалидными первыми двумя цифрами (73) не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "02100002A"
        assertEquals(0, scanText(text), "RTN с буквами не должен находиться")
    }

    @Test
    fun testAllZeros() {
        val text = "000000000"
        assertEquals(0, scanText(text), "RTN со всеми нулями не должен находиться (фильтруется функцией check)")
    }

    @Test
    fun testAllSameDigits() {
        val text = "111111111"
        assertEquals(0, scanText(text), "RTN со всеми одинаковыми цифрами не должен находиться (фильтруется функцией check)")
    }

    @Test
    fun testInsideLongSequence() {
        val text = "ABC076401251DEF"
        // \b не срабатывает между буквой и цифрой, поэтому RTN не должен находиться
        assertEquals(0, scanText(text), "RTN внутри буквенной последовательности не должен находиться (нет границы слова)")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "rtn076401251"
        // \b граница слова не сработает между буквой и цифрой, поэтому RTN не должен находиться
        assertEquals(0, scanText(text), "RTN, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123076401251"
        assertEquals(0, scanText(text), "RTN, прилипший к цифрам, не должен находиться")
    }

    // ========== 7. Тестирование контрольной суммы ==========

    @Test
    fun testValidChecksum() {
        val text = "076401251" // Известный валидный RTN с правильной контрольной суммой
        assertTrue(scanText(text) >= 1, "RTN с правильной контрольной суммой должен быть найден")
    }

    @Test
    fun testInvalidChecksum() {
        val text = "076401250" // Неправильная контрольная сумма (последняя цифра должна быть 1, а не 0)
        assertEquals(0, scanText(text), "RTN с неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testInvalidChecksumAnother() {
        val text = "021000000" // Первые 8 цифр: 02100000, проверка контрольной суммы должна дать 5, а не 0
        assertEquals(0, scanText(text), "RTN с неправильной контрольной суммой не должен находиться")
    }

    @Test
    fun testValidChecksumMultipleRanges() {
        // Тестируем валидные RTN из разных диапазонов с правильными контрольными суммами
        val validRTNs = listOf(
            "076401251", // 00-09
            "112847614", // 10-12
            "234859726", // 21-29
            "301728492", // 30-32
            "653847296", // 61-69
            "715729469", // 70-72
            "802938477"  // 80
        )
        validRTNs.forEach { rtn ->
            assertTrue(scanText(rtn) >= 1, "RTN $rtn с правильной контрольной суммой должен быть найден")
        }
    }

    // ========== 8. Тесты дополнительной фильтрации для снижения false positives ==========

    @Test
    fun testTooFewUniqueDigits() {
        // RTN с очень малым количеством уникальных цифр в последних 6 должны быть отфильтрованы
        // "111000009" имеет последние 6 цифр "100000" - только 2 уникальные (0 и 1) с разницей 1
        val text1 = "111000009"
        assertEquals(0, scanText(text1), "RTN с слишком малым количеством уникальных цифр должен быть отфильтрован")
        
        val text2 = "231000002"
        assertEquals(0, scanText(text2), "RTN с слишком малым количеством уникальных цифр должен быть отфильтрован")
    }
}
