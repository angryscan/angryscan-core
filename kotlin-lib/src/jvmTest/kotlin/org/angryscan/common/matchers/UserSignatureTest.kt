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
 * Тесты для проверки крайних позиций и пограничных значений матчера UserSignature
 */
internal class UserSignatureTest {

    @Test
    fun testUserSignatureAtStart() {
        val signature = UserSignature("Test Signature", mutableListOf("Секретная метка"))
        val text = "Секретная метка в начале документа"
        assertTrue(scanText(text, signature) >= 1, "Пользовательская подпись в начале должна быть найдена")
    }

    @Test
    fun testUserSignatureAtEnd() {
        val signature = UserSignature("Test Signature", mutableListOf("Секретная метка"))
        val text = "Документ содержит: Секретная метка"
        assertTrue(scanText(text, signature) >= 1, "Пользовательская подпись в конце должна быть найдена")
    }

    @Test
    fun testUserSignatureInMiddle() {
        val signature = UserSignature("Test Signature", mutableListOf("Секретная метка"))
        val text = "Документ с Секретная метка должен быть проверен"
        assertTrue(scanText(text, signature) >= 1, "Пользовательская подпись в середине должна быть найдена")
    }

    @Test
    fun testUserSignatureStandalone() {
        val signature = UserSignature("Test Signature", mutableListOf("Секретная метка"))
        val text = "Секретная метка"
        assertTrue(scanText(text, signature) >= 1, "Пользовательская подпись отдельно должна быть найдена")
    }

    @Test
    fun testMultipleSignatures() {
        val signature = UserSignature("Test Signature", mutableListOf("Метка1", "Метка2"))
        val text = "Документ с Метка1 и Метка2"
        assertTrue(scanText(text, signature) >= 2, "Несколько подписей должны быть найдены")
    }

    @Test
    fun testMultipleOccurrences() {
        val signature = UserSignature("Test Signature", mutableListOf("Секретная метка"))
        val text = """
            Первая: Секретная метка
            Вторая: Секретная метка
            Третья: Секретная метка
        """.trimIndent()
        assertTrue(scanText(text, signature) >= 3, "Несколько вхождений одной подписи должны быть найдены")
    }

    @Test
    fun testCaseInsensitiveUpperCase() {
        val signature = UserSignature("Test Signature", mutableListOf("Метка"))
        val text = "МЕТКА"
        assertTrue(scanText(text, signature) >= 1, "Подпись в верхнем регистре должна быть найдена")
    }

    @Test
    fun testCaseInsensitiveLowerCase() {
        val signature = UserSignature("Test Signature", mutableListOf("МЕТКА"))
        val text = "метка"
        assertTrue(scanText(text, signature) >= 1, "Подпись в нижнем регистре должна быть найдена")
    }

    @Test
    fun testCaseInsensitiveMixedCase() {
        val signature = UserSignature("Test Signature", mutableListOf("МеТкА"))
        val text = "мЕтКа"
        assertTrue(scanText(text, signature) >= 1, "Подпись в смешанном регистре должна быть найдена")
    }

    @Test
    fun testSignatureWithSpecialChars() {
        val signature = UserSignature("Test Signature", mutableListOf("Метка-123"))
        val text = "Метка-123"
        assertTrue(scanText(text, signature) >= 1, "Подпись со спецсимволами должна быть найдена")
    }

    @Test
    fun testSignatureWithNumbers() {
        val signature = UserSignature("Test Signature", mutableListOf("Метка123"))
        val text = "Метка123"
        assertTrue(scanText(text, signature) >= 1, "Подпись с цифрами должна быть найдена")
    }

    @Test
    fun testSignatureWithSpaces() {
        val signature = UserSignature("Test Signature", mutableListOf("Секретная метка 2024"))
        val text = "Секретная метка 2024"
        assertTrue(scanText(text, signature) >= 1, "Подпись с пробелами должна быть найдена")
    }

    @Test
    fun testSignatureInParentheses() {
        val signature = UserSignature("Test Signature", mutableListOf("Метка"))
        val text = "(Метка)"
        assertTrue(scanText(text, signature) >= 1, "Подпись в скобках должна быть найдена")
    }

    @Test
    fun testSignatureInQuotes() {
        val signature = UserSignature("Test Signature", mutableListOf("Метка"))
        val text = "\"Метка\""
        assertTrue(scanText(text, signature) >= 1, "Подпись в кавычках должна быть найдена")
    }

    @Test
    fun testSignatureWithPunctuation() {
        val signature = UserSignature("Test Signature", mutableListOf("Метка"))
        val text = "Метка."
        assertTrue(scanText(text, signature) >= 1, "Подпись с точкой должна быть найдена")
    }

    @Test
    fun testSignatureLong() {
        val signature = UserSignature("Test Signature", mutableListOf("Это очень длинная секретная метка для тестирования"))
        val text = "Это очень длинная секретная метка для тестирования"
        assertTrue(scanText(text, signature) >= 1, "Длинная подпись должна быть найдена")
    }

    @Test
    fun testSignatureShort() {
        val signature = UserSignature("Test Signature", mutableListOf("А"))
        val text = "А"
        assertTrue(scanText(text, signature) >= 1, "Короткая подпись должна быть найдена")
    }

    @Test
    fun testSignatureCyrillic() {
        val signature = UserSignature("Test Signature", mutableListOf("Кириллица"))
        val text = "Кириллица"
        assertTrue(scanText(text, signature) >= 1, "Кириллическая подпись должна быть найдена")
    }

    @Test
    fun testSignatureLatin() {
        val signature = UserSignature("Test Signature", mutableListOf("LatinText"))
        val text = "LatinText"
        assertTrue(scanText(text, signature) >= 1, "Латинская подпись должна быть найдена")
    }

    @Test
    fun testSignatureNotFound() {
        val signature = UserSignature("Test Signature", mutableListOf("Метка"))
        val text = "Другой текст"
        assertEquals(0, scanText(text, signature), "Подпись не должна быть найдена в другом тексте")
    }

    @Test
    fun testSignatureEmpty() {
        val signature = UserSignature("Test Signature", mutableListOf("Метка"))
        val text = ""
        assertEquals(0, scanText(text, signature), "Пустая строка не должна содержать подписи")
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
