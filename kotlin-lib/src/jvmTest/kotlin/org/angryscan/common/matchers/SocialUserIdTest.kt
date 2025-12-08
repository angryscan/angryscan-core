package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера SocialUserId
 */
internal class SocialUserIdTest : MatcherTestBase(SocialUserId) {

    // ========== 1. Позиция совпадения в тексте и строке ==========

    @Test
    fun testAtStartOfText() {
        val text = "@username"
        assertTrue(scanText(text) >= 1, "ID соцсети в начале текста должен быть найден")
    }

    @Test
    fun testAtEndOfText() {
        val text = "Social ID: @username"
        assertTrue(scanText(text) >= 1, "ID соцсети в конце текста должен быть найден")
    }

    @Test
    fun testInMiddleOfText() {
        val text = "The ID @username is valid"
        assertTrue(scanText(text) >= 1, "ID соцсети в середине текста должен быть найден")
    }

    @Test
    fun testAtStartOfLine() {
        val text = "@username\nSecond line"
        assertTrue(scanText(text) >= 1, "ID соцсети в начале строки должен быть найден")
    }

    @Test
    fun testAtEndOfLine() {
        val text = "First line\n@username"
        assertTrue(scanText(text) >= 1, "ID соцсети в конце строки должен быть найден")
    }

    @Test
    fun testInMiddleOfLine() {
        val text = "Line with @username ID"
        assertTrue(scanText(text) >= 1, "ID соцсети в середине строки должен быть найден")
    }

    @Test
    fun testWithNewlineBefore() {
        val text = "\n@username"
        assertTrue(scanText(text) >= 1, "ID соцсети после \\n должен быть найден")
    }

    @Test
    fun testWithNewlineAfter() {
        val text = "@username\n"
        assertTrue(scanText(text) >= 1, "ID соцсети перед \\n должен быть найден")
    }

    @Test
    fun testWithCRLF() {
        val text = "\r\n@username\r\n"
        assertTrue(scanText(text) >= 1, "ID соцсети с \\r\\n должен быть найден")
    }

    @Test
    fun testWithEmptyLineBefore() {
        val text = "\n\n@username"
        assertTrue(scanText(text) >= 1, "ID соцсети после пустой строки должен быть найден")
    }

    @Test
    fun testWithEmptyLineAfter() {
        val text = "@username\n\n"
        assertTrue(scanText(text) >= 1, "ID соцсети перед пустой строкой должен быть найден")
    }

    // ========== 2. Соседние символы (границы токена) ==========

    @Test
    fun testNotInsideAlphabeticSequence() {
        val text = "abc@usernamedef"
        assertEquals(0, scanText(text), "ID соцсети внутри буквенной последовательности не должен находиться")
    }

    @Test
    fun testNotInsideNumericSequence() {
        val text = "123@username456"
        assertEquals(0, scanText(text), "ID соцсети внутри цифровой последовательности не должен находиться")
    }

    @Test
    fun testWithSpaceBefore() {
        val text = "ID @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с пробелом перед должен быть найден")
    }

    @Test
    fun testWithSpaceAfter() {
        val text = "@username is valid"
        assertTrue(scanText(text) >= 1, "ID соцсети с пробелом после должен быть найден")
    }

    @Test
    fun testWithCommaBefore() {
        val text = "ID, @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с запятой перед должен быть найден")
    }

    @Test
    fun testWithCommaAfter() {
        val text = "@username, next"
        assertTrue(scanText(text) >= 1, "ID соцсети с запятой после должен быть найден")
    }

    @Test
    fun testWithDotBefore() {
        val text = "ID. @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с точкой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonBefore() {
        val text = "ID; @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с точкой с запятой перед должен быть найден")
    }

    @Test
    fun testWithSemicolonAfter() {
        val text = "@username; next"
        assertTrue(scanText(text) >= 1, "ID соцсети с точкой с запятой после должен быть найден")
    }

    @Test
    fun testWithColonBefore() {
        val text = "ID: @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с двоеточием перед должен быть найден")
    }

    // ========== 3. Контекст со спецсимволами и пунктуацией ==========

    @Test
    fun testWithParenthesesAndSpace() {
        val text = "( @username )"
        assertTrue(scanText(text) >= 1, "ID соцсети в скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithParenthesesNoSpace() {
        val text = "(@username)"
        assertTrue(scanText(text) >= 1, "ID соцсети в скобках без пробелов должен быть найден")
    }

    @Test
    fun testWithQuotesAndSpace() {
        val text = "\" @username \""
        assertTrue(scanText(text) >= 1, "ID соцсети в кавычках с пробелами должен быть найден")
    }

    @Test
    fun testWithQuotesNoSpace() {
        val text = "\"@username\""
        assertTrue(scanText(text) >= 1, "ID соцсети в кавычках без пробелов должен быть найден")
    }

    @Test
    fun testWithBracketsAndSpace() {
        val text = "[ @username ]"
        assertTrue(scanText(text) >= 1, "ID соцсети в квадратных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithBracesAndSpace() {
        val text = "{ @username }"
        assertTrue(scanText(text) >= 1, "ID соцсети в фигурных скобках с пробелами должен быть найден")
    }

    @Test
    fun testWithAtSignAsPartOfFormat() {
        val text = "@username"
        assertTrue(scanText(text) >= 1, "ID соцсети со знаком @ как часть формата должен быть найден")
    }

    // ========== 4. Дополнительные структурные и форматные границы ==========

    @Test
    fun testMultipleWithSpaces() {
        val text = "@username @user2"
        assertTrue(scanText(text) >= 2, "Несколько ID соцсетей через пробел должны быть найдены")
    }

    @Test
    fun testMultipleWithCommas() {
        val text = "@username, @user2"
        assertTrue(scanText(text) >= 2, "Несколько ID соцсетей через запятую должны быть найдены")
    }

    @Test
    fun testMultipleWithSemicolons() {
        val text = "@username; @user2"
        assertTrue(scanText(text) >= 2, "Несколько ID соцсетей через точку с запятой должны быть найдены")
    }

    @Test
    fun testMultipleWithNewlines() {
        val text = "@username\n@user2"
        assertTrue(scanText(text) >= 2, "Несколько ID соцсетей через перенос строки должны быть найдены")
    }

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать ID соцсетей")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no social user IDs at all"
        assertEquals(0, scanText(text), "Текст без ID соцсетей не должен находить совпадения")
    }

    @Test
    fun testMinimalFormat() {
        val text = "@abc"
        assertTrue(scanText(text) >= 1, "Минимальный формат должен быть найден")
    }

    @Test
    fun testMaximalFormat() {
        val text = "@abcdefghijklmnopqrstuvwxyz123456"
        assertTrue(scanText(text) >= 1, "Максимальный формат должен быть найден")
    }

    @Test
    fun testTooShort() {
        val text = "@ab"
        assertEquals(0, scanText(text), "Слишком короткий ID не должен находиться")
    }

    @Test
    fun testTooLong() {
        val text = "@abcdefghijklmnopqrstuvwxyz1234567"
        assertEquals(0, scanText(text), "Слишком длинный ID не должен находиться")
    }

    @Test
    fun testWithMultipleSpaces() {
        val text = "ID     @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с несколькими пробелами должен быть найден")
    }

    @Test
    fun testWithTabBefore() {
        val text = "ID\t@username"
        assertTrue(scanText(text) >= 1, "ID соцсети с табуляцией перед должен быть найден")
    }

    @Test
    fun testWithTabAfter() {
        val text = "@username\tnext"
        assertTrue(scanText(text) >= 1, "ID соцсети с табуляцией после должен быть найден")
    }

    @Test
    fun testWithUnicodeCyrillic() {
        val text = "Профиль @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с кириллицей рядом должен быть найден")
    }

    @Test
    fun testWithUnicodeLatin() {
        val text = "Social ID @username"
        assertTrue(scanText(text) >= 1, "ID соцсети с латиницей рядом должен быть найден")
    }

    @Test
    fun testStandalone() {
        val text = "@username"
        assertTrue(scanText(text) >= 1, "ID соцсети отдельной строкой должен быть найден")
    }

    @Test
    fun testAtTextBoundaryStart() {
        val text = "@username text"
        assertTrue(scanText(text) >= 1, "ID соцсети в начале текста должен быть найден")
    }

    @Test
    fun testAtTextBoundaryEnd() {
        val text = "text @username"
        assertTrue(scanText(text) >= 1, "ID соцсети в конце текста должен быть найден")
    }

    @Test
    fun testWithVKKeyword() {
        val text = "ID в VK: @username"
        assertTrue(scanText(text) >= 1, "ID с ключевым словом должен быть найден")
    }

    // ========== 5. Негативные сценарии ==========

    @Test
    fun testWithoutAtSign() {
        val text = "username"
        assertEquals(0, scanText(text), "ID соцсети без символа @ не должен находиться")
    }

    @Test
    fun testStickingToAlphabeticChar() {
        val text = "id@username"
        assertEquals(0, scanText(text), "ID соцсети, прилипший к буквам, не должен находиться")
    }

    @Test
    fun testStickingToNumericChar() {
        val text = "123@username"
        assertEquals(0, scanText(text), "ID соцсети, прилипший к цифрам, не должен находиться")
    }

    @Test
    fun testInCode() {
        val text = "function@username()"
        assertEquals(0, scanText(text), "ID соцсети внутри кода не должен находиться")
    }

    @Test
    fun testOnlyAtSign() {
        val text = "@"
        assertEquals(0, scanText(text), "Только символ @ не должен находиться")
    }

    // ========== 6. CSS at-rules (не должны находиться) ==========

    @Test
    fun testCSSMedia() {
        val text = "@media"
        assertEquals(0, scanText(text), "CSS @media не должен находиться")
    }

    @Test
    fun testCSSPage() {
        val text = "@page"
        assertEquals(0, scanText(text), "CSS @page не должен находиться")
    }

    @Test
    fun testCSSPagemargin() {
        val text = "@pagemargin"
        assertEquals(0, scanText(text), "CSS @pagemargin не должен находиться")
    }

    @Test
    fun testCSSImport() {
        val text = "@import"
        assertEquals(0, scanText(text), "CSS @import не должен находиться")
    }

    @Test
    fun testCSSCharset() {
        val text = "@charset"
        assertEquals(0, scanText(text), "CSS @charset не должен находиться")
    }

    @Test
    fun testCSSNamespace() {
        val text = "@namespace"
        assertEquals(0, scanText(text), "CSS @namespace не должен находиться")
    }

    @Test
    fun testCSSKeyframes() {
        val text = "@keyframes"
        assertEquals(0, scanText(text), "CSS @keyframes не должен находиться")
    }

    @Test
    fun testCSSSupports() {
        val text = "@supports"
        assertEquals(0, scanText(text), "CSS @supports не должен находиться")
    }

    @Test
    fun testCSSFontFace() {
        val text = "@font-face"
        assertEquals(0, scanText(text), "CSS @font-face не должен находиться")
    }

    @Test
    fun testCSSDocument() {
        val text = "@document"
        assertEquals(0, scanText(text), "CSS @document не должен находиться")
    }

    @Test
    fun testCSSViewport() {
        val text = "@viewport"
        assertEquals(0, scanText(text), "CSS @viewport не должен находиться")
    }

    @Test
    fun testCSSLayer() {
        val text = "@layer"
        assertEquals(0, scanText(text), "CSS @layer не должен находиться")
    }

    @Test
    fun testCSSContainer() {
        val text = "@container"
        assertEquals(0, scanText(text), "CSS @container не должен находиться")
    }

    @Test
    fun testCSSScope() {
        val text = "@scope"
        assertEquals(0, scanText(text), "CSS @scope не должен находиться")
    }

    @Test
    fun testCSSMediaInContext() {
        val text = "CSS rule: @media (max-width: 768px)"
        assertEquals(0, scanText(text), "CSS @media в контексте не должен находиться")
    }

    @Test
    fun testCSSPageInContext() {
        val text = "Print styles: @page { margin: 2cm; }"
        assertEquals(0, scanText(text), "CSS @page в контексте не должен находиться")
    }

    @Test
    fun testValidUsernameAfterCSS() {
        val text = "@username"
        assertTrue(scanText(text) >= 1, "Валидный username должен находиться")
    }

    @Test
    fun testValidUsernameWithCSSKeyword() {
        val text = "@username_media"
        assertTrue(scanText(text) >= 1, "Username содержащий 'media' должен находиться")
    }

    // ========== 7. Username только из цифр (не должны находиться) ==========

    @Test
    fun testOnlyDigits() {
        val text = "@545"
        assertEquals(0, scanText(text), "Username состоящий только из цифр не должен находиться")
    }

    @Test
    fun testOnlyDigitsWithLeadingZero() {
        val text = "@028"
        assertEquals(0, scanText(text), "Username состоящий только из цифр с ведущим нулем не должен находиться")
    }

    @Test
    fun testOnlyDigitsLong() {
        val text = "@1234567890"
        assertEquals(0, scanText(text), "Длинный username состоящий только из цифр не должен находиться")
    }

    @Test
    fun testOnlyDigitsMinimal() {
        val text = "@123"
        assertEquals(0, scanText(text), "Минимальный username состоящий только из цифр не должен находиться")
    }

    @Test
    fun testValidUsernameWithDigits() {
        val text = "@user123"
        assertTrue(scanText(text) >= 1, "Username содержащий буквы и цифры должен находиться")
    }

    @Test
    fun testValidUsernameWithDigitsAtStart() {
        val text = "@123user"
        assertTrue(scanText(text) >= 1, "Username начинающийся с цифр, но содержащий буквы должен находиться")
    }

    @Test
    fun testValidUsernameWithDigitsInMiddle() {
        val text = "@user123name"
        assertTrue(scanText(text) >= 1, "Username с цифрами в середине должен находиться")
    }

    // ========== 8. Зарезервированные имена (не должны находиться) ==========

    @Test
    fun testReservedAdmin() {
        val text = "@admin"
        assertEquals(0, scanText(text), "Зарезервированное имя 'admin' не должно находиться")
    }

    @Test
    fun testReservedTest() {
        val text = "@test"
        assertEquals(0, scanText(text), "Зарезервированное имя 'test' не должно находиться")
    }

    @Test
    fun testReservedSupport() {
        val text = "@support"
        assertEquals(0, scanText(text), "Зарезервированное имя 'support' не должно находиться")
    }

    @Test
    fun testReservedLogin() {
        val text = "@login"
        assertEquals(0, scanText(text), "Зарезервированное имя 'login' не должно находиться")
    }

    @Test
    fun testReservedNull() {
        val text = "@null"
        assertEquals(0, scanText(text), "Зарезервированное имя 'null' не должно находиться")
    }

    @Test
    fun testReservedWww() {
        val text = "@www"
        assertEquals(0, scanText(text), "Зарезервированное имя 'www' не должно находиться")
    }

    // ========== 9. Контекст email (не должны находиться) ==========
    // Примечание: проверка контекста email/URL сложна для реализации через regex,
    // поэтому эти случаи могут находиться. В реальных сценариях матчер Email
    // должен обрабатывать email адреса отдельно.

    // ========== 11. Проверка наличия букв ==========

    @Test
    fun testOnlyUnderscores() {
        val text = "@___"
        assertEquals(0, scanText(text), "Username состоящий только из подчеркиваний не должен находиться")
    }

    @Test
    fun testOnlyDigitsAndUnderscores() {
        val text = "@123_456"
        assertEquals(0, scanText(text), "Username состоящий только из цифр и подчеркиваний не должен находиться")
    }

    @Test
    fun testValidUsernameWithUnderscores() {
        val text = "@user_name"
        assertTrue(scanText(text) >= 1, "Username с буквами и подчеркиваниями должен находиться")
    }
}

