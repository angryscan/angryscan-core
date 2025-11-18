package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера IPv6
 */
internal class IPv6Test: MatcherTestBase(IPv6) {

    @Test
    fun testIPv6AtStart() {
        val text = " 2001:0db8:85a3:0000:0000:8a2e:0370:7334 это IPv6 адрес"
        assertTrue(scanText(text) >= 1, "IPv6 в начале должен быть найден")
    }

    @Test
    fun testIPv6AtEnd() {
        val text = "IPv6 адрес: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 "
        assertTrue(scanText(text) >= 1, "IPv6 в конце должен быть найден")
    }

    @Test
    fun testIPv6InMiddle() {
        val text = "Сервер на 2001:0db8:85a3:0000:0000:8a2e:0370:7334 доступен"
        assertTrue(scanText(text) >= 1, "IPv6 в середине должен быть найден")
    }

    @Test
    fun testIPv6Standalone() {
        val text = " 2001:0db8:85a3:0000:0000:8a2e:0370:7334 "
        assertTrue(scanText(text) >= 1, "IPv6 отдельно должен быть найден")
    }

    @Test
    fun testIPv6Loopback() {
        val text = " 0000:0000:0000:0000:0000:0000:0000:0001 "
        assertTrue(scanText(text) >= 1, "IPv6 loopback должен быть найден")
    }

    @Test
    fun testIPv6AllZeros() {
        val text = " 0000:0000:0000:0000:0000:0000:0000:0000 "
        assertTrue(scanText(text) >= 1, "IPv6 из нулей должен быть найден")
    }

    @Test
    fun testIPv6AllF() {
        val text = " ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff "
        assertTrue(scanText(text) >= 1, "IPv6 из f должен быть найден")
    }

    @Test
    fun testIPv6UpperCase() {
        val text = " 2001:0DB8:85A3:0000:0000:8A2E:0370:7334 "
        assertTrue(scanText(text) >= 1, "IPv6 в верхнем регистре должен быть найден")
    }

    @Test
    fun testIPv6LowerCase() {
        val text = " 2001:0db8:85a3:0000:0000:8a2e:0370:7334 "
        assertTrue(scanText(text) >= 1, "IPv6 в нижнем регистре должен быть найден")
    }

    @Test
    fun testIPv6MixedCase() {
        val text = " 2001:0Db8:85A3:0000:0000:8a2E:0370:7334 "
        assertTrue(scanText(text) >= 1, "IPv6 в смешанном регистре должен быть найден")
    }

    @Test
    fun testIPv6InParentheses() {
        val text = "(2001:0db8:85a3:0000:0000:8a2e:0370:7334)"
        assertTrue(scanText(text) >= 1, "IPv6 в скобках должен быть найден")
    }

    @Test
    fun testIPv6InQuotes() {
        val text = "\"2001:0db8:85a3:0000:0000:8a2e:0370:7334\""
        assertTrue(scanText(text) >= 1, "IPv6 в кавычках должен быть найден")
    }

    @Test
    fun testMultipleIPv6() {
        val text = """
            Первый: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 
            Второй: fe80:0000:0000:0000:0202:b3ff:fe1e:8329 
        """.trimIndent()
        assertTrue(scanText(text) >= 2, "Несколько IPv6 должны быть найдены")
    }

    @Test
    fun testIPv6LinkLocal() {
        val text = " fe80:0000:0000:0000:0202:b3ff:fe1e:8329 "
        assertTrue(scanText(text) >= 1, "Link-local IPv6 должен быть найден")
    }

    @Test
    fun testIPv6Documentation() {
        val text = " 2001:0db8:0000:0000:0000:0000:0000:0001 "
        assertTrue(scanText(text) >= 1, "Documentation IPv6 должен быть найден")
    }

    @Test
    fun testIPv6InvalidTooShort() {
        val text = " 2001:0db8:85a3:0000:0000:8a2e:0370 "
        assertEquals(0, scanText(text), "Слишком короткий IPv6 не должен быть найден")
    }

    @Test
    fun testIPv6InvalidTooLong() {
        val text = " 2001:0db8:85a3:0000:0000:8a2e:0370:7334:1234 "
        assertEquals(0, scanText(text), "Слишком длинный IPv6 не должен быть найден")
    }

    @Test
    fun testIPv6InvalidCharacter() {
        val text = " 2001:0db8:85g3:0000:0000:8a2e:0370:7334 "
        assertEquals(0, scanText(text), "IPv6 с некорректным символом не должен быть найден")
    }

    @Test
    fun testIPv6EmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать IPv6")
    }
}

