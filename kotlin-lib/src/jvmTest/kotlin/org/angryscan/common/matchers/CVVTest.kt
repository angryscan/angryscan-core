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
 * Тесты для проверки крайних позиций и пограничных значений матчера CVV
 */
internal class CVVTest {

    @Test
    fun testCVVAtStart() {
        val text = " CVV: 123 код карты"
        assertTrue(scanText(text, CVV) >= 1, "CVV в начале должен быть найден")
    }

    @Test
    fun testCVVAtEnd() {
        val text = "Код безопасности CVV: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVV в конце должен быть найден")
    }

    @Test
    fun testCVVInMiddle() {
        val text = "Карта с CVV: 123 активна"
        assertTrue(scanText(text, CVV) >= 1, "CVV в середине должен быть найден")
    }

    @Test
    fun testCVVStandalone() {
        val text = " CVV: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVV отдельной строкой должен быть найден")
    }

    @Test
    fun testCVVWithColon() {
        val text = " CVV: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVV с двоеточием должен быть найден")
    }

    @Test
    fun testCVVWithSpace() {
        val text = " CVV 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVV с пробелом должен быть найден")
    }

    @Test
    fun testCVVWithColonSpace() {
        val text = " CVV: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVV с двоеточием и пробелом должен быть найден")
    }

    @Test
    fun testCVCVariant() {
        val text = " CVC: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVC должен быть найден")
    }

    @Test
    fun testCAVVariant() {
        val text = " CAV: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CAV должен быть найден")
    }

    @Test
    fun testCVV2Variant() {
        val text = " CVV2: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVV2 должен быть найден")
    }

    @Test
    fun testCVC2Variant() {
        val text = " CVC2: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVC2 должен быть найден")
    }

    @Test
    fun testCAV2Variant() {
        val text = " CAV2: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CAV2 должен быть найден")
    }

    @Test
    fun testCVVBoundary000() {
        val text = " CVV: 000 "
        assertTrue(scanText(text, CVV) >= 1, "CVV 000 должен быть найден")
    }

    @Test
    fun testCVVBoundary999() {
        val text = " CVV: 999 "
        assertTrue(scanText(text, CVV) >= 1, "CVV 999 должен быть найден")
    }

    @Test
    fun testCVVUpperCase() {
        val text = " CVV: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVV в верхнем регистре должен быть найден")
    }

    @Test
    fun testCVVLowerCase() {
        val text = " cvv: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVV в нижнем регистре должен быть найден")
    }

    @Test
    fun testCVVMixedCase() {
        val text = " CvV: 123 "
        assertTrue(scanText(text, CVV) >= 1, "CVV в смешанном регистре должен быть найден")
    }

    @Test
    fun testCVVInQuotes() {
        val text = "\"CVV: 123\""
        assertTrue(scanText(text, CVV) >= 1, "CVV в кавычках должен быть найден")
    }

    @Test
    fun testCVVWithPunctuation() {
        val text = " CVV: 123, "
        assertTrue(scanText(text, CVV) >= 1, "CVV с запятой должен быть найден")
    }

    @Test
    fun testMultipleCVV() {
        val text = """
            Первая: CVV: 123
            Вторая: CVC: 456
            Третья: CVV2: 789
        """.trimIndent()
        assertTrue(scanText(text, CVV) >= 3, "Несколько CVV должны быть найдены")
    }

    @Test
    fun testCVVInvalidTooShort() {
        val text = " CVV: 12 "
        assertEquals(0, scanText(text, CVV), "Слишком короткий CVV не должен быть найден")
    }

    @Test
    fun testCVVInvalidTooLong() {
        val text = " CVV: 1234 "
        assertEquals(0, scanText(text, CVV), "Слишком длинный CVV не должен быть найден")
    }

    @Test
    fun testCVVInvalidLetters() {
        val text = " CVV: ABC "
        assertEquals(0, scanText(text, CVV), "CVV с буквами не должен быть найден")
    }

    @Test
    fun testCVVEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, CVV), "Пустая строка не должна содержать CVV")
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

