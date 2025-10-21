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
 * Тесты для проверки крайних позиций и пограничных значений матчера IdentityDocType
 */
internal class IdentityDocTypeTest {

    @Test
    fun testIdentityDocTypeAtStart() {
        val text = "паспорт гражданина РФ был выдан в 2015 году"
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа в начале должен быть найден")
    }

    @Test
    fun testIdentityDocTypeAtEnd() {
        val text = "Документ: паспорт гражданина РФ"
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа в конце должен быть найден")
    }

    @Test
    fun testIdentityDocTypeInMiddle() {
        val text = "Гражданин предъявил паспорт гражданина РФ для идентификации"
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа в середине должен быть найден")
    }

    @Test
    fun testIdentityDocTypeStandalone() {
        val text = "военный билет"
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа отдельной строкой должен быть найден")
    }

    @Test
    fun testIdentityDocTypeAllVariants() {
        val text = """
            паспорт гражданина РФ
            паспорт гражданина России
            загранпаспорт
            военный билет
            ВНЖ
            РВП
            свидетельство о рождении
            временное удостоверение личности гражданина РФ
        """.trimIndent()
        assertTrue(scanText(text, IdentityDocType) >= 8, "Все типы документов должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeWithPrefix() {
        val text = "Вид документа удостоверяющего личность: паспорт гражданина РФ"
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа с префиксом должен быть найден")
    }

    @Test
    fun testIdentityDocTypePassportVariants() {
        val text = """
            паспорт гражданина РФ
            паспорт гражданина Российской Федерации
            паспорт гражданина России
        """.trimIndent()
        assertEquals(3, scanText(text, IdentityDocType), "Все варианты паспорта должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeForeignPassport() {
        val text = "паспорт иностранного гражданина"
        assertEquals(1, scanText(text, IdentityDocType), "Паспорт иностранного гражданина должен быть найден")
    }

    @Test
    fun testIdentityDocTypeInternationalPassport() {
        val text = """
            загранпаспорт
            заграничный паспорт
        """.trimIndent()
        assertEquals(2, scanText(text, IdentityDocType), "Загранпаспорт должен быть найден")
    }

    @Test
    fun testIdentityDocTypeDiplomaticPassports() {
        val text = """
            дипломатический паспорт
            служебный паспорт
        """.trimIndent()
        assertEquals(2, scanText(text, IdentityDocType), "Дипломатический и служебный паспорта должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeTemporaryID() {
        val text = "временное удостоверение личности гражданина РФ"
        assertEquals(1, scanText(text, IdentityDocType), "Временное удостоверение должно быть найдено")
    }

    @Test
    fun testIdentityDocTypeBirthCertificate() {
        val text = "свидетельство о рождении"
        assertEquals(1, scanText(text, IdentityDocType), "Свидетельство о рождении должно быть найдено")
    }

    @Test
    fun testIdentityDocTypeResidencePermit() {
        val text = """
            вид на жительство
            вид на жительство в РФ
            ВНЖ
        """.trimIndent()
        assertEquals(3, scanText(text, IdentityDocType), "Все варианты ВНЖ должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeTemporaryResidence() {
        val text = """
            разрешение на временное проживание
            РВП
        """.trimIndent()
        assertEquals(2, scanText(text, IdentityDocType), "Все варианты РВП должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeMilitaryID() {
        val text = """
            военный билет
            удостоверение личности офицера
            удостоверение личности военнослужащего
            удостоверение личности моряка
        """.trimIndent()
        assertEquals(4, scanText(text, IdentityDocType), "Все военные документы должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeNationalPassport() {
        val text = "национальный паспорт"
        assertEquals(1, scanText(text, IdentityDocType), "Национальный паспорт должен быть найден")
    }

    @Test
    fun testIdentityDocTypeInParentheses() {
        val text = "Документ (паспорт гражданина РФ) предъявлен"
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа в скобках должен быть найден")
    }

    @Test
    fun testIdentityDocTypeInQuotes() {
        val text = "Тип: \"военный билет\""
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа в кавычках должен быть найден")
    }

    @Test
    fun testIdentityDocTypeWithPunctuation() {
        val text = "Документ: паспорт гражданина РФ."
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа с точкой должен быть найден")
    }

    @Test
    fun testIdentityDocTypeUpperCase() {
        val text = "ПАСПОРТ ГРАЖДАНИНА РФ"
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа в верхнем регистре должен быть найден")
    }

    @Test
    fun testIdentityDocTypeEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, IdentityDocType), "Пустая строка не должна содержать типа документа")
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

