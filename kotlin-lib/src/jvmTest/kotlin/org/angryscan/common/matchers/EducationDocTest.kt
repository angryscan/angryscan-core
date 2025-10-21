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
 * Тесты для проверки крайних позиций и пограничных значений матчера EducationDoc
 */
internal class EducationDocTest {

    @Test
    fun testEducationDocAtStart() {
        val text = " 123456 1234567 это диплом"
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ об образовании в начале должен быть найден")
    }

    @Test
    fun testEducationDocAtEnd() {
        val text = "Диплом: 123456 1234567 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ об образовании в конце должен быть найден")
    }

    @Test
    fun testEducationDocInMiddle() {
        val text = "Выпускник с дипломом 123456 1234567 работает"
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ об образовании в середине должен быть найден")
    }

    @Test
    fun testEducationDocStandalone() {
        val text = " 123456 1234567 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ об образовании отдельно должен быть найден")
    }

    @Test
    fun testEducationDoc13Digits() {
        val text = " 123456 1234567 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ с 13 цифрами должен быть найден")
    }

    @Test
    fun testEducationDocWithLetters2Digits() {
        val text = " 12 АБ 123456 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ формата 12 АБ 123456 должен быть найден")
    }

    @Test
    fun testEducationDocWithLetters7Digits() {
        val text = " 12 АБ 1234567 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ формата 12 АБ 1234567 должен быть найден")
    }

    @Test
    fun testEducationDocRomanI() {
        val text = " I-АБ 123456 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ с римской I должен быть найден")
    }

    @Test
    fun testEducationDocRomanII() {
        val text = " II-АБ 123456 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ с римской II должен быть найден")
    }

    @Test
    fun testEducationDocRomanIII() {
        val text = " III-АБ 123456 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ с римской III должен быть найден")
    }

    @Test
    fun testEducationDocWithDash() {
        val text = " I-АБ 123456 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ с дефисом должен быть найден")
    }

    @Test
    fun testEducationDocWithLabel() {
        val text = "документ об образовании: 123456 1234567 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ с меткой должен быть найден")
    }

    @Test
    fun testEducationDocDiplom() {
        val text = "диплом: 123456 1234567 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Диплом должен быть найден")
    }

    @Test
    fun testEducationDocAttestat() {
        val text = "аттестат: 12 АБ 123456 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Аттестат должен быть найден")
    }

    @Test
    fun testEducationDocPrilozhenie() {
        val text = "приложение к диплому: 123456 1234567 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Приложение к диплому должно быть найдено")
    }

    @Test
    fun testEducationDocUpperCase() {
        val text = "ДИПЛОМ: 123456 1234567 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ в верхнем регистре должен быть найден")
    }

    @Test
    fun testEducationDocLowerCase() {
        val text = "диплом: 123456 1234567 "
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ в нижнем регистре должен быть найден")
    }

    @Test
    fun testEducationDocInParentheses() {
        val text = "(123456 1234567)"
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ в скобках должен быть найден")
    }

    @Test
    fun testEducationDocInQuotes() {
        val text = "\"12 АБ 123456\""
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ в кавычках должен быть найден")
    }

    @Test
    fun testEducationDocWithPunctuation() {
        val text = "Диплом: 123456 1234567."
        assertTrue(scanText(text, EducationDoc) >= 1, "Документ с точкой должен быть найден")
    }

    @Test
    fun testMultipleEducationDocs() {
        val text = """
            Первый: 123456 1234567
            Второй: 12 АБ 123456
            Третий: I-ВГ 234567
        """.trimIndent()
        assertTrue(scanText(text, EducationDoc) >= 3, "Несколько документов должны быть найдены")
    }

    @Test
    fun testEducationDocInvalidTooShort() {
        val text = " 12345 123456 "
        assertEquals(0, scanText(text, EducationDoc), "Слишком короткий номер не должен быть найден")
    }

    @Test
    fun testEducationDocInvalidTooLong() {
        val text = " 1234567 12345678 "
        assertEquals(0, scanText(text, EducationDoc), "Слишком длинный номер не должен быть найден")
    }

    @Test
    fun testEducationDocEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, EducationDoc), "Пустая строка не должна содержать документа об образовании")
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

