package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера IdentityDocType
 */
internal class IdentityDocTypeTest: MatcherTestBase(IdentityDocType) {

    @Test
    fun testIdentityDocTypeAtStart() {
        val text = "паспорт гражданина РФ был выдан в 2015 году"
        assertEquals(1, scanText(text), "Тип документа в начале должен быть найден")
    }

    @Test
    fun testIdentityDocTypeAtEnd() {
        val text = "Документ: паспорт гражданина РФ"
        assertEquals(1, scanText(text), "Тип документа в конце должен быть найден")
    }

    @Test
    fun testIdentityDocTypeInMiddle() {
        val text = "Гражданин предъявил паспорт гражданина РФ для идентификации"
        assertEquals(1, scanText(text), "Тип документа в середине должен быть найден")
    }

    @Test
    fun testIdentityDocTypeStandalone() {
        val text = "военный билет"
        assertEquals(1, scanText(text), "Тип документа отдельной строкой должен быть найден")
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
        assertTrue(scanText(text) >= 8, "Все типы документов должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeWithPrefix() {
        val text = "Вид документа удостоверяющего личность: паспорт гражданина РФ"
        assertEquals(1, scanText(text), "Тип документа с префиксом должен быть найден")
    }

    @Test
    fun testIdentityDocTypePassportVariants() {
        val text = """
            паспорт гражданина РФ
            паспорт гражданина Российской Федерации
            паспорт гражданина России
        """.trimIndent()
        assertEquals(3, scanText(text), "Все варианты паспорта должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeForeignPassport() {
        val text = "паспорт иностранного гражданина"
        assertEquals(1, scanText(text), "Паспорт иностранного гражданина должен быть найден")
    }

    @Test
    fun testIdentityDocTypeInternationalPassport() {
        val text = """
            загранпаспорт
            заграничный паспорт
        """.trimIndent()
        assertEquals(2, scanText(text), "Загранпаспорт должен быть найден")
    }

    @Test
    fun testIdentityDocTypeDiplomaticPassports() {
        val text = """
            дипломатический паспорт
            служебный паспорт
        """.trimIndent()
        assertEquals(2, scanText(text), "Дипломатический и служебный паспорта должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeTemporaryID() {
        val text = "временное удостоверение личности гражданина РФ"
        assertEquals(1, scanText(text), "Временное удостоверение должно быть найдено")
    }

    @Test
    fun testIdentityDocTypeBirthCertificate() {
        val text = "свидетельство о рождении"
        assertEquals(1, scanText(text), "Свидетельство о рождении должно быть найдено")
    }

    @Test
    fun testIdentityDocTypeResidencePermit() {
        val text = """
            вид на жительство
            вид на жительство в РФ
            ВНЖ
        """.trimIndent()
        assertEquals(3, scanText(text), "Все варианты ВНЖ должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeTemporaryResidence() {
        val text = """
            разрешение на временное проживание
            РВП
        """.trimIndent()
        assertEquals(2, scanText(text), "Все варианты РВП должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeMilitaryID() {
        val text = """
            военный билет
            удостоверение личности офицера
            удостоверение личности военнослужащего
            удостоверение личности моряка
        """.trimIndent()
        assertEquals(4, scanText(text), "Все военные документы должны быть найдены")
    }

    @Test
    fun testIdentityDocTypeNationalPassport() {
        val text = "национальный паспорт"
        assertEquals(1, scanText(text), "Национальный паспорт должен быть найден")
    }

    @Test
    fun testIdentityDocTypeInParentheses() {
        val text = "Документ (паспорт гражданина РФ) предъявлен"
        assertEquals(1, scanText(text), "Тип документа в скобках должен быть найден")
    }

    @Test
    fun testIdentityDocTypeInQuotes() {
        val text = "Тип: \"военный билет\""
        assertEquals(1, scanText(text), "Тип документа в кавычках должен быть найден")
    }

    @Test
    fun testIdentityDocTypeWithPunctuation() {
        val text = "Документ: паспорт гражданина РФ."
        assertEquals(1, scanText(text), "Тип документа с точкой должен быть найден")
    }

    @Test
    fun testIdentityDocTypeUpperCase() {
        val text = "ПАСПОРТ ГРАЖДАНИНА РФ"
        assertEquals(1, scanText(text), "Тип документа в верхнем регистре должен быть найден")
    }

    @Test
    fun testIdentityDocTypeEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать типа документа")
    }
}

