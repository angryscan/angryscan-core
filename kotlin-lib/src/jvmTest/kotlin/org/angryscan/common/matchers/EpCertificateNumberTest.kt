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
 * Тесты для проверки крайних позиций и пограничных значений матчера EpCertificateNumber
 */
internal class EpCertificateNumberTest {

    @Test
    fun testEpCertificateNumberAtStart() {
        val text = " 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF сертификат ЭП"
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер сертификата ЭП в начале должен быть найден")
    }

    @Test
    fun testEpCertificateNumberAtEnd() {
        val text = "Номер сертификата ЭП: 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер сертификата ЭП в конце должен быть найден")
    }

    @Test
    fun testEpCertificateNumberInMiddle() {
        val text = "Сертификат 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF действует"
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер сертификата ЭП в середине должен быть найден")
    }

    @Test
    fun testEpCertificateNumberStandalone() {
        val text = " 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер сертификата ЭП отдельно должен быть найден")
    }

    @Test
    fun testEpCertificateNumberWithSpaces() {
        val text = " 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер с пробелами должен быть найден")
    }

    @Test
    fun testEpCertificateNumberInvalidWithoutSpaces() {
        val text = " 00112233445566778899AABBCCDDEEFF "
        assertEquals(0, scanText(text, EpCertificateNumber), "Номер без пробелов не должен быть найден")
    }

    @Test
    fun testEpCertificateNumberUpperCase() {
        val text = " 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер в верхнем регистре должен быть найден")
    }

    @Test
    fun testEpCertificateNumberLowerCase() {
        val text = " 00 11 22 33 44 55 66 77 88 99 aa bb cc dd ee ff "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер в нижнем регистре должен быть найден")
    }

    @Test
    fun testEpCertificateNumberMixedCase() {
        val text = " 00 11 22 33 44 55 66 77 88 99 Aa Bb Cc Dd Ee Ff "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер в смешанном регистре должен быть найден")
    }

    @Test
    fun testEpCertificateNumberWithSerialLabel() {
        val text = "серийный номер сертификата ЭП: 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер с меткой 'серийный номер' должен быть найден")
    }

    @Test
    fun testEpCertificateNumberWithFullLabel() {
        val text = "номер сертификата электронной подписи: 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер с полной меткой должен быть найден")
    }

    @Test
    fun testEpCertificateNumberWithSerialNumberEP() {
        val text = "serial number ЭП: 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер с меткой 'serial number ЭП' должен быть найден")
    }

    @Test
    fun testEpCertificateNumberWithKeyLabel() {
        val text = "номер сертификата ключа проверки ЭП: 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер сертификата ключа должен быть найден")
    }

    @Test
    fun testEpCertificateNumberWithUniqueLabel() {
        val text = "уникальный номер сертификата: 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Уникальный номер сертификата должен быть найден")
    }

    @Test
    fun testEpCertificateNumberInParentheses() {
        val text = "(00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF)"
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер в скобках должен быть найден")
    }

    @Test
    fun testEpCertificateNumberInQuotes() {
        val text = "\"00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF\""
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер в кавычках должен быть найден")
    }

    @Test
    fun testEpCertificateNumberWithPunctuation() {
        val text = "Номер: 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF."
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер с точкой должен быть найден")
    }

    @Test
    fun testEpCertificateNumberWithColon() {
        val text = "Сертификат: 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF"
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер с двоеточием должен быть найден")
    }

    @Test
    fun testEpCertificateNumberGroupByFour() {
        val text = " Сертификат: 0011 2233 4455 6677 8899 AABB CCDD EEFF "
        assertTrue(scanText(text, EpCertificateNumber) >= 1, "Номер с группировкой по 4 символа должен быть найден")
    }

    @Test
    fun testMultipleEpCertificateNumbers() {
        val text = """
            Первый: 00 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF
            Второй: 11 22 33 44 55 66 77 88 99 AA BB CC DD EE FF 00
            Третий: 22 33 44 55 66 77 88 99 AA BB CC DD EE FF 00 11
        """.trimIndent()
        assertTrue(scanText(text, EpCertificateNumber) >= 3, "Несколько номеров сертификатов должны быть найдены")
    }

    @Test
    fun testEpCertificateNumberAllZeros() {
        val text = " 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 "
        assertEquals(0, scanText(text, EpCertificateNumber), "Номер из нулей не должен быть найден")
    }

    @Test
    fun testEpCertificateNumberAllF() {
        val text = " FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF "
        assertEquals(0, scanText(text, EpCertificateNumber), "Номер из одинаковых символов не должен быть найден")
    }

    @Test
    fun testEpCertificateNumberInvalidTooShort() {
        val text = " 00112233445566778899AABBCCDDEE "
        assertEquals(0, scanText(text, EpCertificateNumber), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testEpCertificateNumberInvalidWithInvalidChar() {
        val text = " 00112233445566778899AABBCCDDEEG "
        assertEquals(0, scanText(text, EpCertificateNumber), "Номер с некорректным символом не должен быть найден")
    }

    @Test
    fun testEpCertificateNumberEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, EpCertificateNumber), "Пустая строка не должна содержать номера сертификата ЭП")
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
