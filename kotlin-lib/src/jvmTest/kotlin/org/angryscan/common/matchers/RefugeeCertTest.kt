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
 * Тесты для проверки крайних позиций и пограничных значений матчера RefugeeCert
 */
internal class RefugeeCertTest {

    @Test
    fun testRefugeeCertAtStart() {
        val text = " 12 1234567 свидетельство беженца"
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство беженца в начале должно быть найдено")
    }

    @Test
    fun testRefugeeCertAtEnd() {
        val text = "Свидетельство беженца: 12 1234567 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство беженца в конце должно быть найдено")
    }

    @Test
    fun testRefugeeCertInMiddle() {
        val text = "Лицо со свидетельством 12 1234567 получило статус"
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство беженца в середине должно быть найдено")
    }

    @Test
    fun testRefugeeCertStandalone() {
        val text = " 12 1234567 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство беженца отдельно должно быть найдено")
    }

    @Test
    fun testRefugeeCertWithSpace() {
        val text = " 12 1234567 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство с пробелом должно быть найдено")
    }

    @Test
    fun testRefugeeCertWithoutSpace() {
        val text = " 121234567 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство без пробела должно быть найдено")
    }

    @Test
    fun testRefugeeCertWithNumber() {
        val text = " 12 № 1234567 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство с № должно быть найдено")
    }

    @Test
    fun testRefugeeCertWithNumberN() {
        val text = " 12 N 1234567 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство с N должно быть найдено")
    }

    @Test
    fun testRefugeeCertWithLabel() {
        val text = "свидетельство беженца: 12 1234567 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство с меткой должно быть найдено")
    }

    @Test
    fun testRefugeeCertWithLongLabel() {
        val text = "свидетельство о рассмотрении ходатайства о признании лица беженцем: 12 1234567 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство с длинной меткой должно быть найдено")
    }

    @Test
    fun testRefugeeCertBoundary00() {
        val text = " 00 0000000 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Граничное свидетельство 00 должно быть найдено")
    }

    @Test
    fun testRefugeeCertBoundary99() {
        val text = " 99 9999999 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Граничное свидетельство 99 должно быть найдено")
    }

    @Test
    fun testRefugeeCertUpperCase() {
        val text = "СВИДЕТЕЛЬСТВО БЕЖЕНЦА: 12 1234567 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство в верхнем регистре должно быть найдено")
    }

    @Test
    fun testRefugeeCertLowerCase() {
        val text = "свидетельство беженца: 12 1234567 "
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство в нижнем регистре должно быть найдено")
    }

    @Test
    fun testRefugeeCertInParentheses() {
        val text = "(12 1234567)"
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство в скобках должно быть найдено")
    }

    @Test
    fun testRefugeeCertInQuotes() {
        val text = "\"12 1234567\""
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство в кавычках должно быть найдено")
    }

    @Test
    fun testRefugeeCertWithPunctuation() {
        val text = "Номер: 12 1234567."
        assertTrue(scanText(text, RefugeeCert) >= 1, "Свидетельство с точкой должно быть найдено")
    }

    @Test
    fun testMultipleRefugeeCerts() {
        val text = """
            Первое: 12 1234567
            Второе: 23 2345678
            Третье: 34 3456789
        """.trimIndent()
        assertTrue(scanText(text, RefugeeCert) >= 3, "Несколько свидетельств должны быть найдены")
    }

    @Test
    fun testRefugeeCertInvalidTooShort() {
        val text = " 12 123456 "
        assertEquals(0, scanText(text, RefugeeCert), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testRefugeeCertInvalidTooLong() {
        val text = " 12 12345678 "
        assertEquals(0, scanText(text, RefugeeCert), "Слишком длинный номер не должен быть найден")
    }

    @Test
    fun testRefugeeCertInvalidOnlyOneDigit() {
        val text = " 1 1234567 "
        assertEquals(0, scanText(text, RefugeeCert), "Номер с 1 цифрой в серии не должен быть найден")
    }

    @Test
    fun testRefugeeCertInvalidThreeDigits() {
        val text = " 123 1234567 "
        assertEquals(0, scanText(text, RefugeeCert), "Номер с 3 цифрами в серии не должен быть найден")
    }

    @Test
    fun testRefugeeCertEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, RefugeeCert), "Пустая строка не должна содержать свидетельства беженца")
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

