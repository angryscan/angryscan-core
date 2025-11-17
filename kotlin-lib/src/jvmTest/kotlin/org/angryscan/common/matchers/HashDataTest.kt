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
 * Тесты для проверки крайних позиций и пограничных значений матчера HashData
 */
internal class HashDataTest {

    @Test
    fun testHashDataMD5AtStart() {
        val text = " 5d41402abc4b2a76b9719d911017c592 это MD5 хэш"
        assertTrue(scanText(text, HashData) >= 1, "MD5 хэш в начале должен быть найден")
    }

    @Test
    fun testHashDataMD5AtEnd() {
        val text = "MD5 хэш файла: 5d41402abc4b2a76b9719d911017c592 "
        assertTrue(scanText(text, HashData) >= 1, "MD5 хэш в конце должен быть найден")
    }

    @Test
    fun testHashDataMD5Standalone() {
        val text = " 5d41402abc4b2a76b9719d911017c592 "
        assertTrue(scanText(text, HashData) >= 1, "MD5 хэш отдельно должен быть найден")
    }

    @Test
    fun testHashDataSHA1() {
        val text = " 356a192b7913b04c54574d18c28d46e6395428ab "
        assertTrue(scanText(text, HashData) >= 1, "SHA1 хэш должен быть найден")
    }

    @Test
    fun testHashDataSHA256() {
        val text = " e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855 "
        assertTrue(scanText(text, HashData) >= 1, "SHA256 хэш должен быть найден")
    }

    @Test
    fun testHashDataSHA384() {
        val text = " 38b060a751ac96384cd9327eb1b1e36a21fdb71114be07434c0cc7bf63f6e1da274edebfe76f65fbd51ad2f14898b95b "
        assertTrue(scanText(text, HashData) >= 1, "SHA384 хэш должен быть найден")
    }

    @Test
    fun testHashDataSHA512() {
        val text = " cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e "
        assertTrue(scanText(text, HashData) >= 1, "SHA512 хэш должен быть найден")
    }

    @Test
    fun testHashDataUpperCase() {
        val text = " 5D41402ABC4B2A76B9719D911017C592 "
        assertTrue(scanText(text, HashData) >= 1, "Хэш в верхнем регистре должен быть найден")
    }

    @Test
    fun testHashDataLowerCase() {
        val text = " 5d41402abc4b2a76b9719d911017c592 "
        assertTrue(scanText(text, HashData) >= 1, "Хэш в нижнем регистре должен быть найден")
    }

    @Test
    fun testHashDataMixedCase() {
        val text = " 5D41402aBc4B2a76B9719D911017c592 "
        assertTrue(scanText(text, HashData) >= 1, "Хэш в смешанном регистре должен быть найден")
    }

    @Test
    fun testHashDataWithLabelHash() {
        val text = "хеш: 5d41402abc4b2a76b9719d911017c592 "
        assertTrue(scanText(text, HashData) >= 1, "Хэш с меткой 'хеш' должен быть найден")
    }

    @Test
    fun testHashDataWithLabelMD5() {
        val text = "MD5: 5d41402abc4b2a76b9719d911017c592 "
        assertTrue(scanText(text, HashData) >= 1, "Хэш с меткой 'MD5' должен быть найден")
    }

    @Test
    fun testHashDataWithLabelSHA1() {
        val text = "SHA1: 356a192b7913b04c54574d18c28d46e6395428ab "
        assertTrue(scanText(text, HashData) >= 1, "Хэш с меткой 'SHA1' должен быть найден")
    }

    @Test
    fun testHashDataWithLabelSHA256() {
        val text = "SHA256: e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855 "
        assertTrue(scanText(text, HashData) >= 1, "Хэш с меткой 'SHA256' должен быть найден")
    }

    @Test
    fun testHashDataWithLabelSHA512() {
        val text = "SHA512: cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e "
        assertTrue(scanText(text, HashData) >= 1, "Хэш с меткой 'SHA512' должен быть найден")
    }

    @Test
    fun testHashDataWithRussianLabel() {
        val text = "хешированные данные: 5d41402abc4b2a76b9719d911017c592 "
        assertTrue(scanText(text, HashData) >= 1, "Хэш с русской меткой должен быть найден")
    }

    @Test
    fun testHashDataInParentheses() {
        val text = "(5d41402abc4b2a76b9719d911017c592)"
        assertTrue(scanText(text, HashData) >= 1, "Хэш в скобках должен быть найден")
    }

    @Test
    fun testHashDataInQuotes() {
        val text = "\"5d41402abc4b2a76b9719d911017c592\""
        assertTrue(scanText(text, HashData) >= 1, "Хэш в кавычках должен быть найден")
    }

    @Test
    fun testHashDataWithPunctuation() {
        val text = "MD5: 5d41402abc4b2a76b9719d911017c592."
        assertTrue(scanText(text, HashData) >= 1, "Хэш с точкой должен быть найден")
    }

    @Test
    fun testMultipleHashData() {
        val text = """
            MD5: 5d41402abc4b2a76b9719d911017c592
            SHA1: 356a192b7913b04c54574d18c28d46e6395428ab
            SHA256: e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855
        """.trimIndent()
        assertTrue(scanText(text, HashData) >= 3, "Несколько хэшей должны быть найдены")
    }

    @Test
    fun testHashDataAllZeros() {
        val text = " 00000000000000000000000000000000 "
        assertEquals(0, scanText(text, HashData), "Хэш из нулей не должен быть найден (исключается валидацией)")
    }

    @Test
    fun testHashDataAllF() {
        val text = " ffffffffffffffffffffffffffffffff "
        assertEquals(0, scanText(text, HashData), "Хэш из f не должен быть найден (исключается валидацией)")
    }

    @Test
    fun testHashDataAllSameChar() {
        val text = " aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "
        assertEquals(0, scanText(text, HashData), "Хэш из одинаковых символов не должен быть найден")
    }

    @Test
    fun testHashDataOnlyDigits() {
        val text = " 12345678901234567890123456789012 "
        assertEquals(0, scanText(text, HashData), "Хэш только из цифр не должен быть найден")
    }

    @Test
    fun testHashDataLessThanFourUniqueChars() {
        val text = " aaaabbbbcccccccaaaabbbbccccccca "
        assertEquals(0, scanText(text, HashData), "Хэш с менее чем 4 уникальными символами (3 символа: a, b, c) не должен быть найден")
    }

    @Test
    fun testHashDataSequentialPattern1() {
        val text = " 0123456789abcdef0123456789abcdef "
        assertEquals(0, scanText(text, HashData), "Хэш с последовательным паттерном 0123456789abcdef не должен быть найден")
    }

    @Test
    fun testHashDataSequentialPattern2() {
        val text = " fedcba9876543210fedcba9876543210 "
        assertEquals(0, scanText(text, HashData), "Хэш с последовательным паттерном fedcba9876543210 не должен быть найден")
    }

    @Test
    fun testHashDataValidHash() {
        val text = " 5d41402abc4b2a76b9719d911017c592 "
        assertEquals(1, scanText(text, HashData), "Валидный хэш должен быть найден")
    }

    @Test
    fun testHashDataInvalidLength31() {
        val text = " 5d41402abc4b2a76b9719d911017c59 "
        assertEquals(0, scanText(text, HashData), "Хэш длиной 31 символ не должен быть найден")
    }

    @Test
    fun testHashDataInvalidLength33() {
        val text = " 5d41402abc4b2a76b9719d911017c5921 "
        assertEquals(0, scanText(text, HashData), "Хэш длиной 33 символа не должен быть найден")
    }

    @Test
    fun testHashDataInvalidChar() {
        val text = " 5d41402abc4b2a76b9719d911017c59g "
        assertEquals(0, scanText(text, HashData), "Хэш с некорректным символом не должен быть найден")
    }

    @Test
    fun testHashDataEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, HashData), "Пустая строка не должна содержать хэша")
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
