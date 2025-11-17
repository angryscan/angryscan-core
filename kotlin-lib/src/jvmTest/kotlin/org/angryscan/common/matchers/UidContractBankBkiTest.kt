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
 * Тесты для проверки крайних позиций и пограничных значений матчера UidContractBankBki
 */
internal class UidContractBankBkiTest {

    @Test
    fun testUidContractAtStart() {
        val text = " 12345678-1234-4a67-8901-123456789012 УИД договора"
        assertTrue(scanText(text, UidContractBankBki) >= 1, "УИД в начале должен быть найден")
    }

    @Test
    fun testUidContractAtEnd() {
        val text = "УИД договора для БКИ: 12345678-1234-4a67-8901-123456789012 "
        assertTrue(scanText(text, UidContractBankBki) >= 1, "УИД в конце должен быть найден")
    }

    @Test
    fun testUidContractInMiddle() {
        val text = "Договор с УИД 12345678-1234-4a67-8901-123456789012 заключен"
        assertTrue(scanText(text, UidContractBankBki) >= 1, "УИД в середине должен быть найден")
    }

    @Test
    fun testUidContractStandalone() {
        val text = " 12345678-1234-4a67-8901-123456789012 "
        assertTrue(scanText(text, UidContractBankBki) >= 1, "УИД отдельно должен быть найден")
    }

    @Test
    fun testUidContractWithDashes() {
        val text = " 12345678-1234-4a67-8901-123456789012 "
        assertTrue(scanText(text, UidContractBankBki) >= 1, "УИД с дефисами должен быть найден")
    }

    @Test
    fun testUidContractWithSpaces() {
        val text = " 12345678 1234 4567 8901 123456789012 "
        assertTrue(scanText(text, UidContractBankBki) >= 1, "УИД с пробелами должен быть найден")
    }

    @Test
    fun testUidContractWithBraces() {
        val text = " {12345678-1234-4a67-8901-123456789012} "
        assertTrue(scanText(text, UidContractBankBki) >= 1, "УИД в фигурных скобках должен быть найден")
    }


    @Test
    fun testUidContractWithLabel() {
        val text = " УИД договора: 12345678-1234-4a67-8901-123456789012 "
        assertTrue(scanText(text, UidContractBankBki) >= 1, "УИД с меткой должен быть найден")
    }

    @Test
    fun testUidContractUpperCase() {
        val text = "УИД: 12345678-1234-4a67-8901-123456789012 "
        assertTrue(scanText(text, UidContractBankBki) >= 1, "УИД в верхнем регистре должен быть найден")
    }

    @Test
    fun testUidContractLowerCase() {
        val text = "уид: 12345678-1234-4a67-8901-123456789012 "
        assertTrue(scanText(text, UidContractBankBki) >= 1, "УИД в нижнем регистре должен быть найден")
    }

    @Test
    fun testMultipleUidContracts() {
        val text = """
            Первый: 12345678-1234-4a67-8901-123456789012 
            Второй: 23456789-2345-4678-9012-234567890123 
            Третий: 34567890-3456-4789-A012-345678901234 
        """.trimIndent()
        assertTrue(scanText(text, UidContractBankBki) >= 3, "Несколько УИД должны быть найдены")
    }

    @Test
    fun testUidContractInvalidVersion3() {
        val text = " 12345678-1234-3567-8901-123456789012 "
        assertEquals(0, scanText(text, UidContractBankBki), "УИД с неверной версией 3 не должен быть найден")
    }

    @Test
    fun testUidContractInvalidVersion5() {
        val text = " 12345678-1234-5567-8901-123456789012 "
        assertEquals(0, scanText(text, UidContractBankBki), "УИД с неверной версией 5 не должен быть найден")
    }

    @Test
    fun testUidContractInvalidVariant7() {
        val text = " 12345678-1234-4567-7901-123456789012 "
        assertEquals(0, scanText(text, UidContractBankBki), "УИД с неверным вариантом 7 не должен быть найден")
    }

    @Test
    fun testUidContractInvalidVariantC() {
        val text = " 12345678-1234-4567-C901-123456789012 "
        assertEquals(0, scanText(text, UidContractBankBki), "УИД с неверным вариантом C не должен быть найден")
    }

    @Test
    fun testUidContractEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, UidContractBankBki), "Пустая строка не должна содержать УИД")
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
