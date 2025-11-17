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
 * Тесты для проверки крайних позиций и пограничных значений матчера OGRNIP
 */
internal class OGRNIPTest {

    @Test
    fun testOGRNIPAtStart() {
        val text = "315774600012344 это ОГРНИП"
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП в начале должен быть найден")
    }

    @Test
    fun testOGRNIPAtEnd() {
        val text = "ОГРНИП индивидуального предпринимателя: 315774600012344"
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП в конце должен быть найден")
    }

    @Test
    fun testOGRNIPInMiddle() {
        val text = "ИП с ОГРНИП 315774600012344 работает"
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП в середине должен быть найден")
    }

    @Test
    fun testOGRNIPStandalone() {
        val text = " 315774600012344 "
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП отдельной строкой должен быть найден")
    }

    @Test
    fun testOGRNIPStartsWith3() {
        val text = " 315774600012344 "
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП начинающийся с 3 должен быть найден")
    }

    @Test
    fun testOGRNIPStartsWith4() {
        val text = " 415774600012341 "
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП начинающийся с 4 должен быть найден")
    }

    @Test
    fun testOGRNIPBoundary300() {
        val text = " 300000000000004 "
        assertTrue(scanText(text, OGRNIP) >= 1, "Граничный ОГРНИП 300... должен быть найден")
    }

    @Test
    fun testOGRNIPBoundary399() {
        val text = " 399999999999990 "
        assertTrue(scanText(text, OGRNIP) >= 1, "Граничный ОГРНИП 399... должен быть найден")
    }

    @Test
    fun testOGRNIPBoundary400() {
        val text = " 400000000000001 "
        assertTrue(scanText(text, OGRNIP) >= 1, "Граничный ОГРНИП 400... должен быть найден")
    }

    @Test
    fun testOGRNIPBoundary499() {
        val text = " 499999999999990 "
        assertTrue(scanText(text, OGRNIP) >= 1, "Граничный ОГРНИП 499... должен быть найден")
    }

    @Test
    fun testOGRNIPWithFullLabel() {
        val text = "регистрационный номер индивидуального предпринимателя: 315774600012344"
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП с полной меткой должен быть найден")
    }

    @Test
    fun testOGRNIPWithShortLabel() {
        val text = "ОГРНИП: 315774600012344"
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП с короткой меткой должен быть найден")
    }

    @Test
    fun testOGRNIPWithReestrLabel() {
        val text = "регистрационный номер в реестре ФЛ ЧП: 315774600012344"
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП с меткой реестра должен быть найден")
    }

    @Test
    fun testOGRNIPWithGosregLabel() {
        val text = "государственный регистрационный номер ИП: 315774600012344"
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП с меткой госрегистрации должен быть найден")
    }

    @Test
    fun testOGRNIPUpperCase() {
        val text = "ОГРНИП: 315774600012344"
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП в верхнем регистре должен быть найден")
    }

    @Test
    fun testOGRNIPLowerCase() {
        val text = "огрнип: 315774600012344"
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП в нижнем регистре должен быть найден")
    }

    @Test
    fun testOGRNIPInParentheses() {
        val text = "(315774600012344)"
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП в скобках должен быть найден")
    }

    @Test
    fun testOGRNIPInQuotes() {
        val text = "\"315774600012344\""
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП в кавычках должен быть найден")
    }

    @Test
    fun testOGRNIPWithPunctuation() {
        val text = "ОГРНИП: 315774600012344."
        assertTrue(scanText(text, OGRNIP) >= 1, "ОГРНИП с точкой должен быть найден")
    }

    @Test
    fun testMultipleOGRNIP() {
        val text = """
            Первый: 315774600012344
            Второй: 415774600098760
            Третий: 316234567890120
        """.trimIndent()
        assertTrue(scanText(text, OGRNIP) >= 3, "Несколько ОГРНИП должны быть найдены")
    }

    @Test
    fun testOGRNIPInvalidStartsWith2() {
        val text = "215774600012345"
        assertEquals(0, scanText(text, OGRNIP), "ОГРНИП начинающийся с 2 не должен быть найден")
    }

    @Test
    fun testOGRNIPInvalidStartsWith5() {
        val text = "515774600012345"
        assertEquals(0, scanText(text, OGRNIP), "ОГРНИП начинающийся с 5 не должен быть найден")
    }

    @Test
    fun testOGRNIPInvalidStartsWith1() {
        val text = "115774600012345"
        assertEquals(0, scanText(text, OGRNIP), "ОГРНИП начинающийся с 1 не должен быть найден")
    }

    @Test
    fun testOGRNIPTooShort() {
        val text = "31577460001234"
        assertEquals(0, scanText(text, OGRNIP), "Слишком короткий ОГРНИП не должен быть найден")
    }

    @Test
    fun testOGRNIPTooLong() {
        val text = "3157746000123456"
        assertEquals(0, scanText(text, OGRNIP), "Слишком длинный ОГРНИП не должен быть найден")
    }

    @Test
    fun testOGRNIPWithLetters() {
        val text = "31577460001234A"
        assertEquals(0, scanText(text, OGRNIP), "ОГРНИП с буквами не должен быть найден")
    }

    @Test
    fun testOGRNIPEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, OGRNIP), "Пустая строка не должна содержать ОГРНИП")
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
