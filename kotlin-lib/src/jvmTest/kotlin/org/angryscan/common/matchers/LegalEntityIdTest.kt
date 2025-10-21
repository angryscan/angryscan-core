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
 * Тесты для проверки крайних позиций и пограничных значений матчера LegalEntityId (LEI/SWIFT)
 */
internal class LegalEntityIdTest {

    @Test
    fun testLegalEntityIdLEIAtStart() {
        val text = " 549300JE6S2CRWR4OK83 это LEI код"
        assertTrue(scanText(text, LegalEntityId) >= 1, "LEI в начале должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEIAtEnd() {
        val text = "LEI организации: 549300JE6S2CRWR4OK83 "
        assertTrue(scanText(text, LegalEntityId) >= 1, "LEI в конце должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEIInMiddle() {
        val text = "Компания с LEI 549300JE6S2CRWR4OK83 зарегистрирована"
        assertTrue(scanText(text, LegalEntityId) >= 1, "LEI в середине должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEIStandalone() {
        val text = " 549300JE6S2CRWR4OK83 "
        assertTrue(scanText(text, LegalEntityId) >= 1, "LEI отдельно должен быть найден")
    }

    @Test
    fun testLegalEntityIdSWIFT8AtStart() {
        val text = " SABRRUMM банк"
        assertTrue(scanText(text, LegalEntityId) >= 1, "SWIFT-8 в начале должен быть найден")
    }

    @Test
    fun testLegalEntityIdSWIFT8AtEnd() {
        val text = "SWIFT код: SABRRUMM "
        assertTrue(scanText(text, LegalEntityId) >= 1, "SWIFT-8 в конце должен быть найден")
    }

    @Test
    fun testLegalEntityIdSWIFT8Standalone() {
        val text = " SABRRUMM "
        assertTrue(scanText(text, LegalEntityId) >= 1, "SWIFT-8 отдельно должен быть найден")
    }

    @Test
    fun testLegalEntityIdSWIFT11() {
        val text = " SABRRUMMXXX код"
        assertTrue(scanText(text, LegalEntityId) >= 1, "SWIFT-11 должен быть найден")
    }

    @Test
    fun testLegalEntityIdWithLabelLEI() {
        val text = "LEI: 549300JE6S2CRWR4OK83 "
        assertTrue(scanText(text, LegalEntityId) >= 1, "LEI с меткой должен быть найден")
    }

    @Test
    fun testLegalEntityIdWithLabelSWIFT() {
        val text = "SWIFT-код: SABRRUMM "
        assertTrue(scanText(text, LegalEntityId) >= 1, "SWIFT с меткой должен быть найден")
    }

    @Test
    fun testLegalEntityIdWithLabelBIC() {
        val text = "BIC: SABRRUMM "
        assertTrue(scanText(text, LegalEntityId) >= 1, "BIC с меткой должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEIUpperCase() {
        val text = " 549300JE6S2CRWR4OK83 "
        assertTrue(scanText(text, LegalEntityId) >= 1, "LEI в верхнем регистре должен быть найден")
    }

    @Test
    fun testLegalEntityIdSWIFTUpperCase() {
        val text = " SABRRUMM "
        assertTrue(scanText(text, LegalEntityId) >= 1, "SWIFT в верхнем регистре должен быть найден")
    }

    @Test
    fun testLegalEntityIdInParentheses() {
        val text = "(549300JE6S2CRWR4OK83)"
        assertTrue(scanText(text, LegalEntityId) >= 1, "LEI в скобках должен быть найден")
    }

    @Test
    fun testLegalEntityIdInQuotes() {
        val text = "\"SABRRUMM\""
        assertTrue(scanText(text, LegalEntityId) >= 1, "SWIFT в кавычках должен быть найден")
    }

    @Test
    fun testLegalEntityIdWithPunctuation() {
        val text = "LEI: 549300JE6S2CRWR4OK83."
        assertTrue(scanText(text, LegalEntityId) >= 1, "LEI с точкой должен быть найден")
    }

    @Test
    fun testMultipleLegalEntityIds() {
        val text = """
            Первый: 549300JE6S2CRWR4OK83
            Второй: SABRRUMM
            Третий: DEUTDEFF
        """.trimIndent()
        assertTrue(scanText(text, LegalEntityId) >= 3, "Несколько идентификаторов должны быть найдены")
    }

    @Test
    fun testLegalEntityIdSWIFTDifferentCountries() {
        val text = """
            Russia: SABRRUMM
            Germany: DEUTDEFF
            USA: CHASUS33
        """.trimIndent()
        assertTrue(scanText(text, LegalEntityId) >= 3, "SWIFT разных стран должны быть найдены")
    }

    @Test
    fun testLegalEntityIdLEIInvalidChecksum() {
        val text = " 549300JE6S2CRWR4OK84 "
        assertEquals(0, scanText(text, LegalEntityId), "LEI с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEILowerCase() {
        val text = " 549300je6s2crwr4ok83 "
        assertEquals(0, scanText(text, LegalEntityId), "LEI в нижнем регистре не должен быть найден")
    }

    @Test
    fun testLegalEntityIdSWIFTTooShort() {
        val text = " SABRRU "
        assertEquals(0, scanText(text, LegalEntityId), "Слишком короткий SWIFT не должен быть найден")
    }

    @Test
    fun testLegalEntityIdSWIFTTooLong() {
        val text = " SABRRUMMXXXX "
        assertEquals(0, scanText(text, LegalEntityId), "Слишком длинный SWIFT не должен быть найден")
    }

    @Test
    fun testLegalEntityIdEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, LegalEntityId), "Пустая строка не должна содержать идентификатора ЮЛ")
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

