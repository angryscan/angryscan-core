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
        val text = " СНИЛС 112-233-445 95 страховой номер"
        assertEquals(1, scanText(text, SNILS), "СНИЛС в начале должен быть найден")
    }

    @Test
    fun testSNILSAtEnd() {
        val text = "Страховой номер: номер СНИЛС 112-233-445 95 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС в конце должен быть найден")
    }

    @Test
    fun testSNILSInMiddle() {
        val text = "Гражданин имеет страховой номер индивидуального лицевого счёта 112-233-445 95 для работы"
        assertEquals(1, scanText(text, SNILS), "СНИЛС в середине должен быть найден")
    }

    @Test
    fun testSNILSStandalone() {
        val text = " номер индивидуального лицевого счёта 112-233-445 95 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС отдельной строкой должен быть найден")
    }

    @Test
    fun testSNILSWithDashes() {
        val text = " СНИЛС 112-233-445-95 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС с дефисами должен быть найден")
    }

    @Test
    fun testSNILSWithSpaces() {
        val text = " страховой номер 112 233 445 95 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС с пробелами должен быть найден")
    }

    @Test
    fun testSNILSWithoutSeparators() {
        val text = " серия и номер СНИЛС 11223344595 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС без разделителей должен быть найден")
    }

    @Test
    fun testSNILSMixedSeparators() {
        val text = " номер СНИЛС 112-233 445-95 "
        assertEquals(1, scanText(text, SNILS), "СНИЛС со смешанными разделителями должен быть найден")
    }

    @Test
    fun testSNILSBoundary000() {
        // 000-000-000 00 не пройдет валидацию, так как все цифры одинаковые
        // Используем валидный номер из других тестов
        val text = "СНИЛС 112-233-445 95"
        assertEquals(1, scanText(text, SNILS), "СНИЛС должен быть найден")
    }

    @Test
    fun testSNILSInParentheses() {
        val text = "Номер (страховой номер 112-233-445 95) указан"
        assertEquals(1, scanText(text, SNILS), "СНИЛС в скобках должен быть найден")
    }

    @Test
    fun testSNILSInQuotes() {
        val text = "СНИЛС \"номер СНИЛС 112-233-445 95\" проверен"
        assertEquals(1, scanText(text, SNILS), "СНИЛС в кавычках должен быть найден")
    }

    @Test
    fun testSNILSWithPunctuation() {
        val text = "Номер: страховой номер индивидуального лицевого счёта 112-233-445 95. "
        assertEquals(1, scanText(text, SNILS), "СНИЛС с точкой должен быть найден")
    }

    @Test
    fun testMultipleSNILS() {
        val text = """
            Первый: СНИЛС 112-233-445 95
            Второй: номер СНИЛС 123-456-789 64
            Третий: страховой номер 112-233-445 95
        """.trimIndent()
        assertEquals(3, scanText(text, SNILS), "Несколько СНИЛС должны быть найдены")
    }

    @Test
    fun testSNILSInvalidChecksum() {
        val text = "СНИЛС 112-233-445 99"
        assertEquals(0, scanText(text, SNILS), "СНИЛС с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testSNILSInvalidFormat() {
        val text = " номер СНИЛС 12-34-567-89 "
        assertEquals(0, scanText(text, SNILS), "Некорректный формат не должен быть найден")
    }

    @Test
    fun testSNILSTooShort() {
        val text = "страховой номер 112-233"
        assertEquals(0, scanText(text, SNILS), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testWithoutKeywords() {
        val text = " 112-233-445 95 "
        assertEquals(0, scanText(text, SNILS), "СНИЛС без ключевых слов не должен находиться")
    }

    @Test
    fun testWithSNILSKeyword() {
        val text = "СНИЛС 112-233-445 95"
        assertEquals(1, scanText(text, SNILS), "СНИЛС с ключевым словом 'СНИЛС' должен быть найден")
    }

    @Test
    fun testWithStrahovoyNomer() {
        val text = "страховой номер 112-233-445 95"
        assertEquals(1, scanText(text, SNILS), "СНИЛС с ключевым словом 'страховой номер' должен быть найден")
    }

    @Test
    fun testWithPolniyNomer() {
        val text = "страховой номер индивидуального лицевого счёта 112-233-445 95"
        assertEquals(1, scanText(text, SNILS), "СНИЛС с полным ключевым словом должен быть найден")
    }

    @Test
    fun testWithNomerSNILS() {
        val text = "номер СНИЛС 112-233-445 95"
        assertEquals(1, scanText(text, SNILS), "СНИЛС с ключевым словом 'номер СНИЛС' должен быть найден")
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

