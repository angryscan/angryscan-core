package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера SNILS
 */
internal class SNILSTest {

    @Test
    fun testSNILSAtStart() {
        val text = " 112-233-445 95 страховой номер"
        assertEquals(1, scanText(text, SNILS), "СНИЛС в начале должен быть найден")
    }

    @Test
    fun testSNILSAtEnd() {
        val text = "Страховой номер: 112-233-445 95 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС в конце должен быть найден")
    }

    @Test
    fun testSNILSInMiddle() {
        val text = "Гражданин имеет СНИЛС 112-233-445 95 для работы"
        assertEquals(1, scanText(text, SNILS), "СНИЛС в середине должен быть найден")
    }

    @Test
    fun testSNILSStandalone() {
        val text = " 112-233-445 95 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС отдельной строкой должен быть найден")
    }

    @Test
    fun testSNILSWithDashes() {
        val text = " 112-233-445-95 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС с дефисами должен быть найден")
    }

    @Test
    fun testSNILSWithSpaces() {
        val text = " 112 233 445 95 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС с пробелами должен быть найден")
    }

    @Test
    fun testSNILSMixedSeparators() {
        val text = " 112-233 445-95 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС со смешанными разделителями должен быть найден")
    }

    @Test
    fun testSNILSBoundary000() {
        val text = "000-000-000 00"
        assertEquals(1, scanText(text, SNILS), "Граничный СНИЛС 000-000-000 00 должен быть найден")
    }

    @Test
    fun testSNILSInParentheses() {
        val text = "Номер (112-233-445 95) указан"
        assertEquals(1, scanText(text, SNILS), "СНИЛС в скобках должен быть найден")
    }

    @Test
    fun testSNILSInQuotes() {
        val text = "СНИЛС \"112-233-445 95\" проверен"
        assertEquals(1, scanText(text, SNILS), "СНИЛС в кавычках должен быть найден")
    }

    @Test
    fun testSNILSWithPunctuation() {
        val text = "Номер: 112-233-445 95. "
        assertEquals(1, scanText(text, SNILS), "СНИЛС с точкой должен быть найден")
    }

    @Test
    fun testMultipleSNILS() {
        val text = """
            Первый: 112-233-445 95
            Второй: 123-456-789 64
            Третий: 111-111-111 45
        """.trimIndent()
        assertEquals(3, scanText(text, SNILS), "Несколько СНИЛС должны быть найдены")
    }

    @Test
    fun testSNILSInvalidChecksum() {
        val text = "112-233-445 99"
        assertEquals(0, scanText(text, SNILS), "СНИЛС с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testSNILSInvalidFormat() {
        val text = " 12-34-567-89 "
        assertEquals(0, scanText(text, SNILS), "Некорректный формат не должен быть найден")
    }

    @Test
    fun testSNILSTooShort() {
        val text = "112-233"
        assertEquals(0, scanText(text, SNILS), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testSNILSEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, SNILS), "Пустая строка не должна содержать СНИЛС")
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

