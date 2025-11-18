package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера EducationLevel
 */
internal class EducationLevelTest: MatcherTestBase(EducationLevel) {

    @Test
    fun testEducationLevelAtStart() {
        val text = "высшее образование получил в МГУ"
        assertEquals(1, scanText(text), "Уровень образования в начале должен быть найден")
    }

    @Test
    fun testEducationLevelAtEnd() {
        val text = "Образование: магистратура"
        assertEquals(1, scanText(text), "Уровень образования в конце должен быть найден")
    }

    @Test
    fun testEducationLevelInMiddle() {
        val text = "Кандидат имеет высшее образование и опыт работы"
        assertEquals(1, scanText(text), "Уровень образования в середине должен быть найден")
    }

    @Test
    fun testEducationLevelStandalone() {
        val text = "бакалавриат"
        assertEquals(1, scanText(text), "Уровень образования отдельной строкой должен быть найден")
    }

    @Test
    fun testEducationLevelSchoolLevels() {
        val text = """
            дошкольное образование
            начальное общее образование
            основное общее образование
            среднее общее образование
        """.trimIndent()
        assertEquals(4, scanText(text), "Все уровни школьного образования должны быть найдены")
    }

    @Test
    fun testEducationLevelProfessionalLevels() {
        val text = """
            среднее профессиональное образование
            высшее образование
            бакалавриат
            магистратура
            аспирантура
            специалитет
        """.trimIndent()
        assertEquals(6, scanText(text), "Все уровни профессионального образования должны быть найдены")
    }

    @Test
    fun testEducationLevelAbbreviations() {
        val text = """
            СПО
            НПО
            ВО
            ДПО
        """.trimIndent()
        assertEquals(4, scanText(text), "Аббревиатуры образования должны быть найдены")
    }

    @Test
    fun testEducationLevelDegrees() {
        val text = """
            бакалавр
            магистр
            аспирант
            специалист
        """.trimIndent()
        assertEquals(4, scanText(text), "Степени образования должны быть найдены")
    }

    @Test
    fun testEducationLevelWithPrefix() {
        val text = "Уровень образования: высшее образование"
        assertEquals(1, scanText(text), "Уровень образования с префиксом должен быть найден")
    }

    @Test
    fun testEducationLevelBasicGeneral() {
        val text = """
            начальное общее
            основное общее
            среднее общее
        """.trimIndent()
        assertEquals(3, scanText(text), "Общее образование без слова 'образование' должно быть найдено")
    }

    @Test
    fun testEducationLevelHigherEducation() {
        val text = """
            высшее образование
            высшее
            ВО
        """.trimIndent()
        assertEquals(3, scanText(text), "Все варианты высшего образования должны быть найдены")
    }

    @Test
    fun testEducationLevelSecondaryProfessional() {
        val text = """
            среднее профессиональное образование
            среднее профессиональное
            СПО
        """.trimIndent()
        assertEquals(3, scanText(text), "Все варианты СПО должны быть найдены")
    }

    @Test
    fun testEducationLevelInitialProfessional() {
        val text = """
            начальное профессиональное образование
            начальное профессиональное
            НПО
        """.trimIndent()
        assertEquals(3, scanText(text), "Все варианты НПО должны быть найдены")
    }

    @Test
    fun testEducationLevelPostgraduate() {
        val text = """
            аспирантура
            ординатура
            ассистентура-стажировка
            подготовка кадров высшей квалификации
        """.trimIndent()
        assertEquals(4, scanText(text), "Все варианты послевузовского образования должны быть найдены")
    }

    @Test
    fun testEducationLevelAdditional() {
        val text = """
            дополнительное профессиональное образование
            дополнительное образование
            ДПО
            профессиональная переподготовка
            повышение квалификации
        """.trimIndent()
        assertEquals(5, scanText(text), "Все варианты дополнительного образования должны быть найдены")
    }

    @Test
    fun testEducationLevelBachelorDeclensions() {
        val text = """
            бакалавр
            бакалавра
            бакалавру
            бакалавром
            бакалавре
        """.trimIndent()
        assertEquals(5, scanText(text), "Все склонения слова 'бакалавр' должны быть найдены")
    }

    @Test
    fun testEducationLevelMasterDeclensions() {
        val text = """
            магистр
            магистра
            магистру
            магистром
            магистре
        """.trimIndent()
        assertEquals(5, scanText(text), "Все склонения слова 'магистр' должны быть найдены")
    }

    @Test
    fun testEducationLevelInParentheses() {
        val text = "Сотрудник (высшее образование) имеет опыт"
        assertEquals(1, scanText(text), "Уровень образования в скобках должен быть найден")
    }

    @Test
    fun testEducationLevelInQuotes() {
        val text = "Образование: \"бакалавриат\""
        assertEquals(1, scanText(text), "Уровень образования в кавычках должен быть найден")
    }

    @Test
    fun testEducationLevelWithPunctuation() {
        val text = "Образование: магистратура."
        assertEquals(1, scanText(text), "Уровень образования с точкой должен быть найден")
    }

    @Test
    fun testEducationLevelUpperCase() {
        val text = "ВЫСШЕЕ ОБРАЗОВАНИЕ"
        assertEquals(1, scanText(text), "Уровень образования в верхнем регистре должен быть найден")
    }

    @Test
    fun testEducationLevelMixedCase() {
        val text = "БаКаЛаВрИаТ"
        assertEquals(1, scanText(text), "Уровень образования в смешанном регистре должен быть найден")
    }

    @Test
    fun testEducationLevelEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать уровня образования")
    }
}

