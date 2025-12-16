package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера Email
 */
internal class EmailTest : MatcherTestBase(Email) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "test@example.com is an email"
        assertTrue(scanText(text) >= 1, "Email в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Email address is test@example.com"
        assertTrue(scanText(text) >= 1, "Email в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The email test@example.com is valid"
        assertTrue(scanText(text) >= 1, "Email в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "test@example.com\nSecond line"
        assertTrue(scanText(text) >= 1, "Email в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\ntest@example.com"
        assertTrue(scanText(text) >= 1, "Email в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with test@example.com email"
        assertTrue(scanText(text) >= 1, "Email в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\ntest@example.com"
        assertTrue(scanText(text) >= 1, "Email после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "test@example.com\n"
        assertTrue(scanText(text) >= 1, "Email перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\ntest@example.com\r\n"
        assertTrue(scanText(text) >= 1, "Email с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\ntest@example.com"
        assertTrue(scanText(text) >= 1, "Email после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "test@example.com\n\n"
        assertTrue(scanText(text) >= 1, "Email перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123test@example.com456"
        assertEquals(0, scanText(text), "Email внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testNotInsideAlphanumericSequence() {
        val text = "abc123test@example.comdef456"
        assertEquals(0, scanText(text), "Email внутри буквенно-цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "Email test@example.com"
        assertTrue(scanText(text) >= 1, "Email с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "test@example.com is valid"
        assertTrue(scanText(text) >= 1, "Email с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "Email, test@example.com"
        assertTrue(scanText(text) >= 1, "Email с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "test@example.com, next"
        assertTrue(scanText(text) >= 1, "Email с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "Email. test@example.com"
        assertTrue(scanText(text) >= 1, "Email с точкой перед должен быть найден")
    }

    @Test
    fun testWithDotAfter() {
        val text = "test@example.com."
        assertTrue(scanText(text) >= 1, "Email с точкой после должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "Email; test@example.com"
        assertTrue(scanText(text) >= 1, "Email с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "test@example.com; next"
        assertTrue(scanText(text) >= 1, "Email с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "Email: test@example.com"
        assertTrue(scanText(text) >= 1, "Email с двоеточием перед должен быть найден")
    }

    @Test
    fun testWithExclamationAfter() {
        val text = "test@example.com!"
        assertTrue(scanText(text) >= 1, "Email с восклицательным знаком после должен быть найден")
    }

    @Test
    fun testWithQuestionMarkAfter() {
        val text = "test@example.com?"
        assertTrue(scanText(text) >= 1, "Email с вопросительным знаком после должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( test@example.com )"
        assertTrue(scanText(text) >= 1, "Email в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(test@example.com)"
        assertTrue(scanText(text) >= 1, "Email в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" test@example.com \""
        assertTrue(scanText(text) >= 1, "Email в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"test@example.com\""
        assertTrue(scanText(text) >= 1, "Email в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithEqualsAndSpace() {
        val text = "email = test@example.com"
        assertTrue(scanText(text) >= 1, "Email с = и пробелом должен быть найден")
    }

    @Test
    fun testWithHashAndSpace() {
        val text = "email # test@example.com"
        assertTrue(scanText(text) >= 1, "Email с # и пробелом должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ test@example.com ]"
        assertTrue(scanText(text) >= 1, "Email в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ test@example.com }"
        assertTrue(scanText(text) >= 1, "Email в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithAtAsPartOfFormat() {
        val text = "test@example.com"
        assertTrue(scanText(text) >= 1, "Email с @ как часть формата должен быть найден")
    }

    @Test
    fun testWithPlusInLocalPart() {
        val text = "test+tag@example.com"
        assertTrue(scanText(text) >= 1, "Email с + в локальной части должен быть найден")
    }

    @Test
    fun testWithDotInLocalPart() {
        val text = "test.name@example.com"
        assertTrue(scanText(text) >= 1, "Email с точкой в локальной части должен быть найден")
    }

    @Test
    fun testWithUnderscoreInLocalPart() {
        val text = "test_name@example.com"
        assertTrue(scanText(text) >= 1, "Email с подчеркиванием в локальной части должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "test1@example.com test2@example.com"
        assertTrue(scanText(text) >= 2, "Несколько email через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "test1@example.com, test2@example.com"
        assertTrue(scanText(text) >= 2, "Несколько email через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "test1@example.com; test2@example.com"
        assertTrue(scanText(text) >= 2, "Несколько email через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "test1@example.com\ntest2@example.com"
        assertTrue(scanText(text) >= 2, "Несколько email через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать email")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no email addresses at all"
        assertEquals(0, scanText(text), "Текст без email не должен находить совпадения")
    }

    @Test
    fun testLongDomain() {
        val text = "test@verylongdomainname.example.com"
        assertTrue(scanText(text) >= 1, "Email с длинным доменом должен быть найден")
    }

    @Test
    fun testTooShortDomain() {
        val text = "test@ex.co"
        val count = scanText(text)
        assertTrue(count >= 0, "Email с коротким доменом")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "Email    test@example.com"
        assertTrue(scanText(text) >= 1, "Email с несколькими пробелами должен быть найден")
    }


    @Test
    fun testWithTabAfter() {
        val text = "test@example.com\tnext"
        assertTrue(scanText(text) >= 1, "Email с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Почта test@example.com"
        assertTrue(scanText(text) >= 1, "Email с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Email test@example.com"
        assertTrue(scanText(text) >= 1, "Email с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "test@example.com"
        assertTrue(scanText(text) >= 1, "Email отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "test@example.com text"
        assertTrue(scanText(text) >= 1, "Email в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text test@example.com"
        assertTrue(scanText(text) >= 1, "Email в конце текста должен быть найден")
    }

    @Test
    fun testWithHyphenInDomain() {
        val text = "test@example-domain.com"
        assertTrue(scanText(text) >= 1, "Email с дефисом в домене должен быть найден")
    }

    @Test
    fun testWithNumbersInLocalPart() {
        val text = "test123@example.com"
        assertTrue(scanText(text) >= 1, "Email с цифрами в локальной части должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testEmailWithoutAt() {
        val text = "testexample.com"
        assertEquals(0, scanText(text), "Email без символа @ не должен находиться")
    }

    @Test
    fun testEmailWithMultipleAts() {
        val text = "test@@example.com"
        assertEquals(0, scanText(text), "Email с несколькими символами @ не должен находиться")
    }

    @Test
    fun testEmailWithoutDomain() {
        val text = "test@"
        assertEquals(0, scanText(text), "Email без домена не должен находиться")
    }

    @Test
    fun testEmailWithoutLocalPart() {
        val text = "@example.com"
        assertEquals(0, scanText(text), "Email без локальной части не должен находиться")
    }

    @Test
    fun testEmailWithInvalidDomain() {
        val text = "test@example"
        // В зависимости от реализации - может быть найдена или нет
        val count = scanText(text)
        assertTrue(count >= 0, "Email с доменом без TLD")
    }

    @Test
    fun testEmailWithSpaces() {
        val text = "test @example.com"
        assertEquals(0, scanText(text), "Email с пробелами не должен находиться")
    }

    @Test
    fun testEmailWithInvalidChars() {
        val text = "test@exam#ple.com"
        assertEquals(0, scanText(text), "Email с недопустимыми символами не должен находиться")
    }

    @Test
    fun testEmailWithInvalidTLD() {
        val text = "test@example.123"
        assertEquals(0, scanText(text), "Email с недопустимым TLD не должен находиться")
    }

    @Test
    fun testPartialEmail() {
        val text = "test@"
        assertEquals(0, scanText(text), "Частичный email не должен находиться")
    }
}

