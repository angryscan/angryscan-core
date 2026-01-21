package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Комплексные тесты для матчера DriverLicenseUS
 */
internal class DriverLicenseUSTest : MatcherTestBase(DriverLicenseUS) {

    // Валидные номера для различных штатов
    private val validCO = "12-345-6789" // Colorado
    private val validFL = "D-123-456-789-012" // Florida
    private val validND = "ABC-12-3456" // North Dakota
    private val validNJ = "A12345678901234" // New Jersey (15 символов: 1 буква + 14 цифр)
    private val validWA = "DOE**MJ501P1" // Washington
    private val validWI = "J525-4209-0465-05" // Wisconsin

    // ========== 1. Тесты для Colorado (##-###-####) ==========

    @Test
    fun testColoradoFormat() {
        val text = "12-345-6789"
        assertTrue(scanText(text) >= 1, "Colorado DL должен быть найден")
    }

    @Test
    fun testColoradoNotInSequence() {
        val text = "12312-345-6789456"
        assertEquals(0, scanText(text), "Colorado DL внутри цифровой последовательности не должен находиться")
    }

    // ========== 2. Тесты для Florida (L-###-###-###-###) ==========

    @Test
    fun testFloridaFormat() {
        val text = "D-123-456-789-012"
        assertTrue(scanText(text) >= 1, "Florida DL должен быть найден")
    }

    @Test
    fun testFloridaLowerCase() {
        val text = "d-123-456-789-012"
        assertTrue(scanText(text) >= 1, "Florida DL в нижнем регистре должен быть найден")
    }

    @Test
    fun testFloridaMixedCase() {
        val text = "D-123-456-789-012"
        assertTrue(scanText(text) >= 1, "Florida DL должен быть найден")
    }

    @Test
    fun testFloridaNotStickingToLetters() {
        val text = "abcD-123-456-789-012def"
        assertEquals(0, scanText(text), "Florida DL прилипший к буквам не должен находиться")
    }

    // ========== 3. Тесты для North Dakota (ABC-12-3456) ==========

    @Test
    fun testNorthDakotaFormat() {
        val text = "ABC-12-3456"
        assertTrue(scanText(text) >= 1, "North Dakota DL должен быть найден")
    }

    @Test
    fun testNorthDakotaLowerCase() {
        val text = "abc-12-3456"
        assertTrue(scanText(text) >= 1, "North Dakota DL в нижнем регистре должен быть найден")
    }

    @Test
    fun testNorthDakotaMixedCase() {
        val text = "AbC-12-3456"
        assertTrue(scanText(text) >= 1, "North Dakota DL со смешанным регистром должен быть найден")
    }

    // ========== 4. Тесты для New Jersey (A + 14 digits) ==========

    @Test
    fun testNewJerseyFormat() {
        val text = "A12345678901234"
        assertTrue(scanText(text) >= 1, "New Jersey DL должен быть найден")
    }

    @Test
    fun testNewJerseyLowerCase() {
        val text = "a12345678901234"
        assertTrue(scanText(text) >= 1, "New Jersey DL в нижнем регистре должен быть найден")
    }

    @Test
    fun testNewJerseyNotStickingToLetters() {
        val text = "abcA12345678901234def"
        assertEquals(0, scanText(text), "New Jersey DL прилипший к буквам не должен находиться")
    }

    @Test
    fun testNewJerseyNotStickingToDigits() {
        val text = "123A12345678901234"
        assertEquals(0, scanText(text), "New Jersey DL прилипший к цифрам не должен находиться")
    }

    // ========== 5. Тесты для Washington (DOE**MJ501P1) ==========

    @Test
    fun testWashingtonFormat() {
        val text = "DOE**MJ501P1"
        assertTrue(scanText(text) >= 1, "Washington DL должен быть найден")
    }

    @Test
    fun testWashingtonLowerCase() {
        val text = "doe**mj501p1"
        assertTrue(scanText(text) >= 1, "Washington DL в нижнем регистре должен быть найден")
    }

    @Test
    fun testWashingtonMixedCase() {
        val text = "Doe**Mj501P1"
        assertTrue(scanText(text) >= 1, "Washington DL со смешанным регистром должен быть найден")
    }

    @Test
    fun testWashingtonNotStickingToLetters() {
        val text = "abcDOE**MJ501P1def"
        assertEquals(0, scanText(text), "Washington DL прилипший к буквам не должен находиться")
    }

    // ========== 6. Тесты для Wisconsin (J525-4209-0465-05) ==========

    @Test
    fun testWisconsinFormat() {
        val text = "J525-4209-0465-05"
        assertTrue(scanText(text) >= 1, "Wisconsin DL должен быть найден")
    }

    @Test
    fun testWisconsinLowerCase() {
        val text = "j525-4209-0465-05"
        assertTrue(scanText(text) >= 1, "Wisconsin DL в нижнем регистре должен быть найден")
    }

    @Test
    fun testWisconsinNotStickingToLetters() {
        val text = "abcJ525-4209-0465-05def"
        assertEquals(0, scanText(text), "Wisconsin DL прилипший к буквам не должен находиться")
    }

    // ========== 7. Негативные сценарии ==========

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать DL")
    }

    @Test
    fun testNoMatches() {
        val text = "This text has no driver license numbers"
        assertEquals(0, scanText(text), "Текст без DL не должен находить совпадения")
    }

    @Test
    fun testColoradoWrongFormat() {
        val text = "123-45-6789" // Неправильный формат для CO
        assertEquals(0, scanText(text), "Неправильный формат Colorado DL не должен находиться")
    }

    @Test
    fun testFloridaWrongFormat() {
        val text = "DD-123-456-789-012" // Две буквы вместо одной
        assertEquals(0, scanText(text), "Неправильный формат Florida DL не должен находиться")
    }

    @Test
    fun testNorthDakotaWrongFormat() {
        val text = "AB-12-3456" // Две буквы вместо трех
        assertEquals(0, scanText(text), "Неправильный формат North Dakota DL не должен находиться")
    }

    @Test
    fun testNewJerseyTooShort() {
        val text = "A1234567890123" // 14 символов вместо 15 (1 буква + 13 цифр)
        assertEquals(0, scanText(text), "Слишком короткий New Jersey DL не должен находиться")
    }

    @Test
    fun testNewJerseyTooLong() {
        val text = "A123456789012345" // 16 символов вместо 15 (1 буква + 15 цифр)
        assertEquals(0, scanText(text), "Слишком длинный New Jersey DL не должен находиться")
    }

    @Test
    fun testWashingtonWrongStars() {
        val text = "DOE*MJ501P1" // Одна звездочка вместо двух
        assertEquals(0, scanText(text), "Washington DL с одной звездочкой не должен находиться")
    }

    @Test
    fun testWashingtonNoStars() {
        val text = "DOEMJ501P1" // Нет звездочек
        assertEquals(0, scanText(text), "Washington DL без звездочек не должен находиться")
    }

    @Test
    fun testWisconsinWrongFormat() {
        val text = "J52-4209-0465-05" // Неправильный формат
        assertEquals(0, scanText(text), "Неправильный формат Wisconsin DL не должен находиться")
    }

    @Test
    fun testWithLetters() {
        val text = "12-345-67AB" // Буквы вместо цифр в CO формате
        assertEquals(0, scanText(text), "DL с буквами в цифровых позициях не должен находиться")
    }
}
