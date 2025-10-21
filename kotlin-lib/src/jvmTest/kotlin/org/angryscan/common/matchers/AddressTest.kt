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
 * Тесты для проверки крайних позиций и пограничных значений матчера Address
 */
internal class AddressTest {

    @Test
    fun testAddressAtStart() {
        val text = "г. Москва, ул. Ленина, д. 10 является адресом"
        assertTrue(scanText(text, Address) >= 1, "Адрес в начале должен быть найден")
    }

    @Test
    fun testAddressAtEnd() {
        val text = "Адрес проживания: г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text, Address) >= 1, "Адрес в конце должен быть найден")
    }

    @Test
    fun testAddressInMiddle() {
        val text = "Гражданин проживает по адресу г. Москва, ул. Ленина, д. 10 постоянно"
        assertTrue(scanText(text, Address) >= 1, "Адрес в середине должен быть найден")
    }

    @Test
    fun testAddressStandalone() {
        val text = "г. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text, Address) >= 1, "Адрес отдельной строкой должен быть найден")
    }

    @Test
    fun testAddressWithGor() {
        val text = "гор. Москва, ул. Ленина, д. 10"
        assertTrue(scanText(text, Address) >= 1, "Адрес с 'гор.' должен быть найден")
    }

    @Test
    fun testAddressWithObl() {
        val text = "обл. Московская, г. Химки, ул. Ленина, д. 10"
        assertTrue(scanText(text, Address) >= 1, "Адрес с 'обл.' должен быть найден")
    }

    @Test
    fun testAddressWithRayon() {
        val text = "р-н Центральный, ул. Ленина, д. 10"
        assertTrue(scanText(text, Address) >= 1, "Адрес с 'р-н' должен быть найден")
    }

    @Test
    fun testAddressWithDom() {
        val text = "г. Москва, ул. Ленина, дом 10"
        assertTrue(scanText(text, Address) >= 1, "Адрес с 'дом' должен быть найден")
    }

    @Test
    fun testAddressShort() {
        val text = "г. Москва, дом 5"
        assertTrue(scanText(text, Address) >= 1, "Короткий адрес должен быть найден")
    }

    @Test
    fun testAddressLong() {
        val text = "г. Москва, ул. Ленина, д. 10, корп. 2, стр. 3, кв. 45"
        assertTrue(scanText(text, Address) >= 1, "Длинный адрес должен быть найден")
    }

    @Test
    fun testAddressWithKorpus() {
        val text = "г. Москва, ул. Ленина, д. 10, корп. 2"
        assertTrue(scanText(text, Address) >= 1, "Адрес с корпусом должен быть найден")
    }

    @Test
    fun testAddressWithKvartira() {
        val text = "г. Москва, ул. Ленина, д. 10, кв. 5"
        assertTrue(scanText(text, Address) >= 1, "Адрес с квартирой должен быть найден")
    }

    @Test
    fun testAddressUpperCase() {
        val text = "Г. МОСКВА, УЛ. ЛЕНИНА, Д. 10"
        assertTrue(scanText(text, Address) >= 1, "Адрес в верхнем регистре должен быть найден")
    }

    @Test
    fun testAddressLowerCase() {
        val text = "г. москва, ул. ленина, д. 10"
        assertTrue(scanText(text, Address) >= 1, "Адрес в нижнем регистре должен быть найден")
    }

    @Test
    fun testAddressMixedCase() {
        val text = "Г. МоСкВа, Ул. ЛеНиНа, Д. 10"
        assertTrue(scanText(text, Address) >= 1, "Адрес в смешанном регистре должен быть найден")
    }

    @Test
    fun testMultipleAddresses() {
        val text = """
            Первый: г. Москва, ул. Ленина, д. 10
            Второй: г. Санкт-Петербург, ул. Невский, д. 20
            Третий: обл. Московская, г. Химки, ул. Победы, д. 5
        """.trimIndent()
        assertTrue(scanText(text, Address) >= 3, "Несколько адресов должны быть найдены")
    }

    @Test
    fun testAddressTooShort() {
        val text = "г. д."
        assertEquals(0, scanText(text, Address), "Слишком короткий адрес не должен быть найден")
    }

    @Test
    fun testAddressWithoutD() {
        val text = "г. Москва, ул. Ленина"
        assertEquals(0, scanText(text, Address), "Адрес без 'д.' не должен быть найден")
    }

    @Test
    fun testAddressEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, Address), "Пустая строка не должна содержать адреса")
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

