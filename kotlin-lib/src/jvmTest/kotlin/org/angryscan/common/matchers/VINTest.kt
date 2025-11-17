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
 * Тесты для проверки крайних позиций и пограничных значений матчера VIN
 */
internal class VINTest {

    @Test
    fun testVINInvalidLetterI() {
        val text = "VIN: JF1SH92F4CG05382I"
        assertEquals(0, scanText(text, VIN), "VIN с буквой I не должен быть найден")
    }

    @Test
    fun testVINInvalidLetterO() {
        val text = "VIN: JF1SH92F4CG05382O"
        assertEquals(0, scanText(text, VIN), "VIN с буквой O не должен быть найден")
    }

    @Test
    fun testVINInvalidLetterQ() {
        val text = "VIN: JF1SH92F4CG05382Q"
        assertEquals(0, scanText(text, VIN), "VIN с буквой Q не должен быть найден")
    }

    @Test
    fun testVINTooShort() {
        val text = "VIN: JF1SH92F4CG05382"
        assertEquals(0, scanText(text, VIN), "Слишком короткий VIN не должен быть найден")
    }

    @Test
    fun testVINTooLong() {
        val text = "VIN: JF1SH92F4CG0538231"
        assertEquals(0, scanText(text, VIN), "Слишком длинный VIN не должен быть найден")
    }

    @Test
    fun testVINEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, VIN), "Пустая строка не должна содержать VIN")
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

