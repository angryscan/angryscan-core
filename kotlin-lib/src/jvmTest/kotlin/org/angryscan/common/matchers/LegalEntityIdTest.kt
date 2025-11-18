package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера LegalEntityId (LEI)
 */
internal class LegalEntityIdTest: MatcherTestBase(LegalEntityId) {

    @Test
    fun testLegalEntityIdLEIAtStart() {
        val text = " 529900T8BM49AURSDO55 это LEI код"
        assertTrue(scanText(text) >= 1, "LEI в начале должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEIAtEnd() {
        val text = "Код LEI организации: 529900T8BM49AURSDO55 "
        assertTrue(scanText(text) >= 1, "LEI в конце должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEIInMiddle() {
        val text = "Компания с кодом LEI 529900T8BM49AURSDO55 зарегистрирована"
        assertTrue(scanText(text) >= 1, "LEI в середине должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEIStandalone() {
        val text = " 529900T8BM49AURSDO55 "
        assertTrue(scanText(text) >= 1, "LEI отдельно должен быть найден")
    }

    @Test
    fun testLegalEntityIdWithLabelLEI() {
        val text = " LEI: 529900T8BM49AURSDO55 "
        assertTrue(scanText(text) >= 1, "LEI с меткой должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEIUpperCase() {
        val text = " 529900T8BM49AURSDO55 "
        assertTrue(scanText(text) >= 1, "LEI в верхнем регистре должен быть найден")
    }

    @Test
    fun testLegalEntityIdInParentheses() {
        val text = "(529900T8BM49AURSDO55)"
        assertTrue(scanText(text) >= 1, "LEI в скобках должен быть найден")
    }

    @Test
    fun testLegalEntityIdInQuotes() {
        val text = "\"529900T8BM49AURSDO55\""
        assertTrue(scanText(text) >= 1, "LEI в кавычках должен быть найден")
    }

    @Test
    fun testLegalEntityIdInSquareBrackets() {
        val text = "[529900T8BM49AURSDO55]"
        assertTrue(scanText(text) >= 1, "LEI в квадратных скобках должен быть найден")
    }

    @Test
    fun testLegalEntityIdInCurlyBrackets() {
        val text = "{529900T8BM49AURSDO55}"
        assertTrue(scanText(text) >= 1, "LEI в фигурных скобках должен быть найден")
    }

    @Test
    fun testLegalEntityIdWithPunctuation() {
        val text = " Код LEI: 529900T8BM49AURSDO55."
        assertTrue(scanText(text) >= 1, "LEI с точкой должен быть найден")
    }

    @Test
    fun testLegalEntityIdWithComma() {
        val text = " Код LEI: 529900T8BM49AURSDO55,"
        assertTrue(scanText(text) >= 1, "LEI с запятой должен быть найден")
    }

    @Test
    fun testLegalEntityIdWithSemicolon() {
        val text = " Код LEI: 529900T8BM49AURSDO55;"
        assertTrue(scanText(text) >= 1, "LEI с точкой с запятой должен быть найден")
    }

    @Test
    fun testMultipleLegalEntityIds() {
        val text = """
            Код первый: 529900T8BM49AURSDO55
            Второй: 5493000JABBMWE2H4P16
        """.trimIndent()
        assertTrue(scanText(text) >= 2, "Несколько LEI должны быть найдены")
    }

    @Test
    fun testLegalEntityIdLEIInvalidChecksum() {
        val text = " Код LEI: 529900T8BM49AURSDO56 "
        assertEquals(0, scanText(text), "LEI с неверной контрольной суммой не должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEILowerCase() {
        val text = " Код LEI: 529900t8bm49aursdo55 "
        assertEquals(0, scanText(text), "LEI в нижнем регистре не должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEIWithoutDoubleZero() {
        val text = " Код LEI: 5299A1T8BM49AURSDO55 "
        assertEquals(0, scanText(text), "LEI без 00 в позициях 4-5 не должен быть найден паттерном")
    }

    @Test
    fun testLegalEntityIdLEITooShort() {
        val text = " Код LEI: 529900T8BM49AURSDO5 "
        assertEquals(0, scanText(text), "Слишком короткий LEI не должен быть найден")
    }

    @Test
    fun testLegalEntityIdLEITooLong() {
        val text = " Код LEI: 529900T8BM49AURSDO555 "
        assertEquals(0, scanText(text), "Слишком длинный LEI не должен быть найден")
    }

    @Test
    fun testLegalEntityIdEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать идентификатора ЮЛ")
    }

    @Test
    fun testLegalEntityIdLEIWithSpaces() {
        val text = " Код LEI: 5299 00T8 BM49 AURS DO55 "
        assertEquals(0, scanText(text), "LEI с пробелами не должен быть найден паттерном")
    }
}

