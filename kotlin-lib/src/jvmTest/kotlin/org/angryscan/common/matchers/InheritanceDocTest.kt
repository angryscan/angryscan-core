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
 * Тесты для проверки крайних позиций и пограничных значений матчера InheritanceDoc
 */
internal class InheritanceDocTest {

    @Test
    fun testInheritanceDocAtStart() {
        val text = " 12 АБ 123456 свидетельство о наследстве"
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ о наследстве в начале должен быть найден")
    }

    @Test
    fun testInheritanceDocAtEnd() {
        val text = "Свидетельство о праве на наследство: 12 АБ 123456 "
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ о наследстве в конце должен быть найден")
    }

    @Test
    fun testInheritanceDocInMiddle() {
        val text = "Наследник со свидетельством 12 АБ 123456 получил права"
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ о наследстве в середине должен быть найден")
    }

    @Test
    fun testInheritanceDocStandalone() {
        val text = " 12 АБ 123456 "
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ о наследстве отдельно должен быть найден")
    }

    @Test
    fun testInheritanceDocWith6Digits() {
        val text = " 12 АБ 123456 "
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ с 6 цифрами должен быть найден")
    }

    @Test
    fun testInheritanceDocWith7Digits() {
        val text = " 12 АБ 1234567 "
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ с 7 цифрами должен быть найден")
    }

    @Test
    fun testInheritanceDocWithSpace() {
        val text = " 12 АБ 123456 "
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ с пробелами должен быть найден")
    }

    @Test
    fun testInheritanceDocWithoutSpace() {
        val text = " 12АБ123456 "
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ без пробелов должен быть найден")
    }

    @Test
    fun testInheritanceDocWithLabel() {
        val text = "свидетельство о праве на наследство: 12 АБ 123456 "
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ с меткой должен быть найден")
    }

    @Test
    fun testInheritanceDocDifferentLetters() {
        val text = """
             12 АБ 123456
             23 ВГ 234567
             34 ДЕ 345678
             45 ЖЗ 456789
        """.trimIndent()
        assertTrue(scanText(text, InheritanceDoc) >= 4, "Документы с разными буквами должны быть найдены")
    }

    @Test
    fun testInheritanceDocUpperCase() {
        val text = "СВИДЕТЕЛЬСТВО: 12 АБ 123456 "
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ в верхнем регистре должен быть найден")
    }

    @Test
    fun testInheritanceDocLowerCase() {
        val text = "свидетельство: 12 аб 123456 "
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ в нижнем регистре должен быть найден")
    }

    @Test
    fun testInheritanceDocMixedCase() {
        val text = "СвИдЕтЕлЬсТвО: 12 АБ 123456 "
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ в смешанном регистре должен быть найден")
    }

    @Test
    fun testInheritanceDocInParentheses() {
        val text = "(12 АБ 123456)"
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ в скобках должен быть найден")
    }

    @Test
    fun testInheritanceDocInQuotes() {
        val text = "\"12 АБ 123456\""
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ в кавычках должен быть найден")
    }

    @Test
    fun testInheritanceDocWithPunctuation() {
        val text = "Свидетельство: 12 АБ 123456."
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ с точкой должен быть найден")
    }

    @Test
    fun testInheritanceDocWithColon() {
        val text = "Номер: 12 АБ 123456"
        assertTrue(scanText(text, InheritanceDoc) >= 1, "Документ с двоеточием должен быть найден")
    }

    @Test
    fun testMultipleInheritanceDocs() {
        val text = """
            Первый наследник: 12 АБ 123456
            Второй наследник: 23 ВГ 234567
            Третий наследник: 34 ДЕ 345678
        """.trimIndent()
        assertTrue(scanText(text, InheritanceDoc) >= 3, "Несколько документов должны быть найдены")
    }

    @Test
    fun testInheritanceDocInvalidTooShort() {
        val text = " 12 АБ 12345 "
        assertEquals(0, scanText(text, InheritanceDoc), "Документ со слишком коротким номером не должен быть найден")
    }

    @Test
    fun testInheritanceDocInvalidTooLong() {
        val text = " 12 АБ 12345678 "
        assertEquals(0, scanText(text, InheritanceDoc), "Документ со слишком длинным номером не должен быть найден")
    }

    @Test
    fun testInheritanceDocInvalidOneLetter() {
        val text = " 12 А 123456 "
        assertEquals(0, scanText(text, InheritanceDoc), "Документ с одной буквой не должен быть найден")
    }

    @Test
    fun testInheritanceDocInvalidThreeLetters() {
        val text = " 12 АБВ 123456 "
        assertEquals(0, scanText(text, InheritanceDoc), "Документ с тремя буквами не должен быть найден")
    }

    @Test
    fun testInheritanceDocInvalidOneDigitSeries() {
        val text = " 1 АБ 123456 "
        assertEquals(0, scanText(text, InheritanceDoc), "Документ с 1 цифрой в серии не должен быть найден")
    }

    @Test
    fun testInheritanceDocInvalidThreeDigitsSeries() {
        val text = " 123 АБ 123456 "
        assertEquals(0, scanText(text, InheritanceDoc), "Документ с 3 цифрами в серии не должен быть найден")
    }

    @Test
    fun testInheritanceDocEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, InheritanceDoc), "Пустая строка не должна содержать документа о наследстве")
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
