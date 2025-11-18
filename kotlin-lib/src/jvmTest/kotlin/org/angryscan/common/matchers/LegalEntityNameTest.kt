package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера LegalEntityName
 */
internal class LegalEntityNameTest: MatcherTestBase(LegalEntityName) {

    @Test
    fun testLegalEntityNameAtStart() {
        val text = " ООО Рога и Копыта является контрагентом"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в начале должно быть найдено")
    }

    @Test
    fun testLegalEntityNameAtEnd() {
        val text = "Контрагент: ООО Рога и Копыта "
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в конце должно быть найдено")
    }

    @Test
    fun testLegalEntityNameInMiddle() {
        val text = "Организация ООО Рога и Копыта работает на рынке"
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ в середине должно быть найдено")
    }

    @Test
    fun testLegalEntityNameStandalone() {
        val text = " ООО Рога и Копыта "
        assertTrue(scanText(text) >= 1, "Наименование ЮЛ отдельной строкой должно быть найдено")
    }

    @Test
    fun testLegalEntityNameOOO() {
        val text = " ООО Рога и Копыта "
        assertTrue(scanText(text) >= 1, "ООО должно быть найдено")
    }

    @Test
    fun testLegalEntityNamePAO() {
        val text = " ПАО Газпром "
        assertTrue(scanText(text) >= 1, "ПАО должно быть найдено")
    }

    @Test
    fun testLegalEntityNameZAO() {
        val text = " ЗАО Техносила "
        assertTrue(scanText(text) >= 1, "ЗАО должно быть найдено")
    }

    @Test
    fun testLegalEntityNameNAO() {
        val text = " НАО Промышленная компания "
        assertTrue(scanText(text) >= 1, "НАО должно быть найдено")
    }

    @Test
    fun testLegalEntityNameAO() {
        val text = " АО Банк "
        assertTrue(scanText(text) >= 1, "АО должно быть найдено")
    }

    @Test
    fun testLegalEntityNameIP() {
        val text = " ИП Иванов Иван Иванович "
        assertTrue(scanText(text) >= 1, "ИП должно быть найдено")
    }

    @Test
    fun testLegalEntityNameFGUP() {
        val text = " ФГУП Почта России "
        assertTrue(scanText(text) >= 1, "ФГУП должно быть найдено")
    }

    @Test
    fun testLegalEntityNameGUP() {
        val text = " ГУП Московский метрополитен "
        assertTrue(scanText(text) >= 1, "ГУП должно быть найдено")
    }

    @Test
    fun testLegalEntityNameMUP() {
        val text = " МУП Водоканал "
        assertTrue(scanText(text) >= 1, "МУП должно быть найдено")
    }

    @Test
    fun testLegalEntityNameFond() {
        val text = " Фонд развития технологий "
        assertTrue(scanText(text) >= 1, "Фонд должен быть найден")
    }

    @Test
    fun testLegalEntityNameAssociation() {
        val text = " Ассоциация предпринимателей "
        assertTrue(scanText(text) >= 1, "Ассоциация должна быть найдена")
    }

    @Test
    fun testLegalEntityNameSoyuz() {
        val text = " Союз производителей "
        assertTrue(scanText(text) >= 1, "Союз должен быть найден")
    }

    @Test
    fun testLegalEntityNameFullOOO() {
        val text = " Общество с ограниченной ответственностью Рога и Копыта "
        assertTrue(scanText(text) >= 1, "Полное наименование ООО должно быть найдено")
    }

    @Test
    fun testLegalEntityNameFullAO() {
        val text = " Акционерное общество Газпром "
        assertTrue(scanText(text) >= 1, "Полное наименование АО должно быть найдено")
    }

    @Test
    fun testLegalEntityNameFullPAO() {
        val text = " Публичное акционерное общество Сбербанк "
        assertTrue(scanText(text) >= 1, "Полное наименование ПАО должно быть найдено")
    }

    @Test
    fun testLegalEntityNameWithQuotes() {
        val text = " ООО \"Рога и Копыта\" "
        assertTrue(scanText(text) >= 1, "Наименование в кавычках должно быть найдено")
    }

    @Test
    fun testLegalEntityNameLongName() {
        val text = " ООО Производственно-коммерческая компания по изготовлению и продаже товаров народного потребления "
        assertTrue(scanText(text) >= 1, "Длинное наименование должно быть найдено")
    }

    @Test
    fun testLegalEntityNameWithNumbers() {
        val text = " ООО Компания-2024 "
        assertTrue(scanText(text) >= 1, "Наименование с цифрами должно быть найдено")
    }

    @Test
    fun testLegalEntityNameWithAmpersand() {
        val text = " ООО Иванов & Партнеры "
        assertTrue(scanText(text) >= 1, "Наименование с амперсандом должно быть найдено")
    }

    @Test
    fun testLegalEntityNameWithDash() {
        val text = " ООО Торгово-промышленная компания "
        assertTrue(scanText(text) >= 1, "Наименование с дефисом должно быть найдено")
    }

    @Test
    fun testLegalEntityNameWithLabel() {
        val text = "наименование ЮЛ: ООО Рога и Копыта "
        assertTrue(scanText(text) >= 1, "Наименование с меткой должно быть найдено")
    }

    @Test
    fun testLegalEntityNameMinLength() {
        val text = " ООО Компа "
        assertTrue(scanText(text) >= 1, "Минимальное наименование (5 символов) должно быть найдено")
    }

    @Test
    fun testMultipleLegalEntityNames() {
        val text = """
            Первое: ООО Рога и Копыта
            Второе: ПАО Газпром
            Третье: ИП Иванов
        """.trimIndent()
        assertTrue(scanText(text) >= 3, "Несколько наименований должны быть найдены")
    }

    @Test
    fun testLegalEntityNameTooShort() {
        val text = " ООО А "
        assertEquals(0, scanText(text), "Слишком короткое наименование не должно быть найдено")
    }

    @Test
    fun testLegalEntityNameEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать наименования ЮЛ")
    }
}

