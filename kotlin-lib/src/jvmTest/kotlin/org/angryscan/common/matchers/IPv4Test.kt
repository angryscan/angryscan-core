package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера IPv4
 */
internal class IPv4Test: MatcherTestBase(IPv4) {

    @Test
    fun testIPv4AtStart() {
        val text = " 192.168.1.1 это IP адрес"
        assertTrue(scanText(text) >= 1, "IPv4 в начале должен быть найден")
    }

    @Test
    fun testIPv4AtEnd() {
        val text = "IP адрес: 192.168.1.1 "
        assertTrue(scanText(text) >= 1, "IPv4 в конце должен быть найден")
    }

    @Test
    fun testIPv4InMiddle() {
        val text = "Сервер на 192.168.1.1 доступен"
        assertTrue(scanText(text) >= 1, "IPv4 в середине должен быть найден")
    }

    @Test
    fun testIPv4Standalone() {
        val text = " 192.168.1.1 "
        assertTrue(scanText(text) >= 1, "IPv4 отдельно должен быть найден")
    }

    @Test
    fun testIPv4Minimum() {
        val text = " 0.0.0.0 "
        assertTrue(scanText(text) >= 1, "Минимальный IPv4 должен быть найден")
    }

    @Test
    fun testIPv4Maximum() {
        val text = " 255.255.255.255 "
        assertTrue(scanText(text) >= 1, "Максимальный IPv4 должен быть найден")
    }

    @Test
    fun testIPv4Localhost() {
        val text = " 127.0.0.1 "
        assertTrue(scanText(text) >= 1, "Localhost должен быть найден")
    }

    @Test
    fun testIPv4PrivateClassA() {
        val text = " 10.0.0.1 "
        assertTrue(scanText(text) >= 1, "Приватный адрес класса A должен быть найден")
    }

    @Test
    fun testIPv4PrivateClassB() {
        val text = " 172.16.0.1 "
        assertTrue(scanText(text) >= 1, "Приватный адрес класса B должен быть найден")
    }

    @Test
    fun testIPv4PrivateClassC() {
        val text = " 192.168.0.1 "
        assertTrue(scanText(text) >= 1, "Приватный адрес класса C должен быть найден")
    }

    @Test
    fun testIPv4PublicGoogle() {
        val text = " 8.8.8.8 "
        assertTrue(scanText(text) >= 1, "Публичный DNS Google должен быть найден")
    }

    @Test
    fun testIPv4PublicCloudflare() {
        val text = " 1.1.1.1 "
        assertTrue(scanText(text) >= 1, "Публичный DNS Cloudflare должен быть найден")
    }

    @Test
    fun testIPv4BoundaryOctet255() {
        val text = " 255.255.255.254 "
        assertTrue(scanText(text) >= 1, "IPv4 с граничным октетом 255 должен быть найден")
    }

    @Test
    fun testIPv4BoundaryOctet250() {
        val text = " 250.250.250.250 "
        assertTrue(scanText(text) >= 1, "IPv4 с октетом 250 должен быть найден")
    }

    @Test
    fun testIPv4SingleDigitOctets() {
        val text = " 1.2.3.4 "
        assertTrue(scanText(text) >= 1, "IPv4 с однозначными октетами должен быть найден")
    }

    @Test
    fun testIPv4MixedDigitOctets() {
        val text = " 192.168.1.100 "
        assertTrue(scanText(text) >= 1, "IPv4 со смешанными октетами должен быть найден")
    }

    @Test
    fun testIPv4InParentheses() {
        val text = "(192.168.1.1)"
        assertTrue(scanText(text) >= 1, "IPv4 в скобках должен быть найден")
    }

    @Test
    fun testIPv4InQuotes() {
        val text = "\"192.168.1.1\""
        assertTrue(scanText(text) >= 1, "IPv4 в кавычках должен быть найден")
    }

    @Test
    fun testIPv4WithSpace() {
        val text = "IP: 192.168.1.1 port"
        assertTrue(scanText(text) >= 1, "IPv4 с пробелами должен быть найден")
    }

    @Test
    fun testMultipleIPv4() {
        val text = """
            Первый: 192.168.1.1 
            Второй: 10.0.0.1 
            Третий: 172.16.0.1 
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько IPv4 должны быть найдены")
    }

    @Test
    fun testIPv4InvalidOctet256() {
        val text = " 256.168.1.1 "
        assertEquals(0, scanText(text), "IPv4 с октетом 256 не должен быть найден")
    }

    @Test
    fun testIPv4InvalidOctet300() {
        val text = " 300.168.1.1 "
        assertEquals(0, scanText(text), "IPv4 с октетом 300 не должен быть найден")
    }

    @Test
    fun testIPv4InvalidTooFewOctets() {
        val text = " 192.168.1 "
        assertEquals(0, scanText(text), "IPv4 с 3 октетами не должен быть найден")
    }

    @Test
    fun testIPv4InvalidTooManyOctets() {
        val text = " 192.168.1.1.1 "
        assertEquals(0, scanText(text), "IPv4 с 5 октетами не должен быть найден")
    }

    @Test
    fun testIPv4EmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать IPv4")
    }
}

