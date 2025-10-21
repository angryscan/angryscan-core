package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера EducationLicense
 */
internal class EducationLicenseTest {

    @Test
    fun testEducationLicenseAtStart() {
        val text = " Л 035-12345-67/12345678 образовательная лицензия"
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия в начале должна быть найдена")
    }

    @Test
    fun testEducationLicenseAtEnd() {
        val text = "Лицензия на образовательную деятельность: Л 035-12345-67/12345678 "
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия в конце должна быть найдена")
    }

    @Test
    fun testEducationLicenseInMiddle() {
        val text = "Организация с лицензией Л 035-12345-67/12345678 проводит обучение"
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия в середине должна быть найдена")
    }

    @Test
    fun testEducationLicenseStandalone() {
        val text = " Л 035-12345-67/12345678 "
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия отдельно должна быть найдена")
    }

    @Test
    fun testEducationLicenseWithCyrillicL() {
        val text = " Л 035-12345-67/12345678 "
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия с кириллической Л должна быть найдена")
    }

    @Test
    fun testEducationLicenseWithLatinL() {
        val text = " L 035-12345-67/12345678 "
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия с латинской L должна быть найдена")
    }

    @Test
    fun testEducationLicenseWithSpaces() {
        val text = " Л 035 - 12345 - 67 / 12345678 "
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия с пробелами должна быть найдена")
    }

    @Test
    fun testEducationLicenseWithoutSpaces() {
        val text = " Л035-12345-67/12345678 "
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия без пробелов должна быть найдена")
    }

    @Test
    fun testEducationLicenseWithFullLabel() {
        val text = "полный номер лицензии обучающей организации: Л 035-12345-67/12345678 "
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия с полной меткой должна быть найдена")
    }

    @Test
    fun testEducationLicenseWithShortLabel() {
        val text = "лицензия: Л 035-12345-67/12345678 "
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия с короткой меткой должна быть найдена")
    }

    @Test
    fun testEducationLicenseUpperCase() {
        val text = "ЛИЦЕНЗИЯ: Л 035-12345-67/12345678 "
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия в верхнем регистре должна быть найдена")
    }

    @Test
    fun testEducationLicenseLowerCase() {
        val text = "лицензия: л 035-12345-67/12345678 "
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия в нижнем регистре должна быть найдена")
    }

    @Test
    fun testEducationLicenseInParentheses() {
        val text = "(Л 035-12345-67/12345678)"
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия в скобках должна быть найдена")
    }

    @Test
    fun testEducationLicenseInQuotes() {
        val text = "\"Л 035-12345-67/12345678\""
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия в кавычках должна быть найдена")
    }

    @Test
    fun testEducationLicenseWithPunctuation() {
        val text = "Лицензия: Л 035-12345-67/12345678."
        assertTrue(scanText(text, EducationLicense) >= 1, "Лицензия с точкой должна быть найдена")
    }

    @Test
    fun testMultipleEducationLicenses() {
        val text = """
            Первая: Л 035-12345-67/12345678
            Вторая: Л 035-23456-78/23456789
            Третья: L 035-34567-89/34567890
        """.trimIndent()
        assertTrue(scanText(text, EducationLicense) >= 3, "Несколько лицензий должны быть найдены")
    }

    @Test
    fun testEducationLicenseInvalidPrefix() {
        val text = " А 035-12345-67/12345678 "
        assertEquals(0, scanText(text, EducationLicense), "Лицензия с неверной буквой не должна быть найдена")
    }

    @Test
    fun testEducationLicenseInvalidCode() {
        val text = " Л 036-12345-67/12345678 "
        assertEquals(0, scanText(text, EducationLicense), "Лицензия с неверным кодом не должна быть найдена")
    }

    @Test
    fun testEducationLicenseEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, EducationLicense), "Пустая строка не должна содержать лицензии")
    }

    private fun scanText(text: String, matcher: IMatcher): Int {
        val kotlinEngine = KotlinEngine(listOf(matcher).filterIsInstance<IKotlinMatcher>())
        val hyperEngine = HyperScanEngine(listOf(matcher).filterIsInstance<IHyperMatcher>())

        val kotlinRes = kotlinEngine.scan(text)
        val hyperRes = hyperEngine.scan(text)
        
        assertEquals(
            kotlinRes.count(),
            hyperRes.count(),
            "Количество совпадений для ${matcher.name} должно быть одинаковым для обоих движков. " +
            "Kotlin: ${kotlinRes.count()}, Hyper: ${hyperRes.count()}\nText: $text"
        )
        
        return kotlinRes.count()
    }
}

