package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Общие тесты для проверки пограничных случаев, которые применимы ко всем матчерам
 * - Тесты с несколькими матчерами в одном тексте
 * - Тесты с специальными символами
 * - Тесты регистронезависимости
 * - Тесты с пустыми строками
 */
internal class CommonTest {

    @Test
    fun testMultipleMatchersInSameText() {
        val text = """
            ФИО: Иванов Иван Иванович
            Семейное положение: женат
            Образование: высшее образование
            Воинское звание: капитан
            Документ: паспорт гражданина РФ
            Координаты места рождения: 55.7558, 37.6173
            Место службы: МВД России
        """.trimIndent()
        
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение должно быть найдено")
        assertEquals(1, scanText(text, EducationLevel), "Образование должно быть найдено")
        assertEquals(1, scanText(text, MilitaryRank), "Звание должно быть найдено")
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа должен быть найден")
        assertEquals(1, scanText(text, Geo), "Координаты должны быть найдены")
        assertEquals(1, scanText(text, SecurityAffiliation), "Силовая структура должна быть найдена")
    }

    @Test
    fun testMatchersWithSpecialCharacters() {
        val text = """
            (женат)
            [высшее образование]
            {капитан}
            «паспорт гражданина РФ»
            "55.7558, 37.6173"
            (ФСБ)
        """.trimIndent()
        
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение в скобках должно быть найдено")
        assertEquals(1, scanText(text, EducationLevel), "Образование в скобках должно быть найдено")
        assertEquals(1, scanText(text, MilitaryRank), "Звание в скобках должно быть найдено")
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа в кавычках должен быть найден")
        assertEquals(1, scanText(text, Geo), "Координаты в кавычках должны быть найдены")
        assertEquals(1, scanText(text, SecurityAffiliation), "Силовая структура в скобках должна быть найдена")
    }

    @Test
    fun testMatchersWithPunctuation() {
        val text = """
            Статус: женат.
            Образование: магистратура!
            Звание: майор?
            Документ: военный билет;
            Место: 55.7558, 37.6173,
            Служба: МВД:
        """.trimIndent()
        
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение с точкой должно быть найдено")
        assertEquals(1, scanText(text, EducationLevel), "Образование с восклицательным знаком должно быть найдено")
        assertEquals(1, scanText(text, MilitaryRank), "Звание с вопросительным знаком должно быть найдено")
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа с точкой с запятой должен быть найден")
        assertEquals(1, scanText(text, Geo), "Координаты с запятой должны быть найдены")
        assertEquals(1, scanText(text, SecurityAffiliation), "Силовая структура с двоеточием должна быть найдена")
    }

    @Test
    fun testCaseSensitivity() {
        // Нижний регистр
        assertEquals(1, scanText("женат", MaritalStatus), "Нижний регистр должен быть найден")
        assertEquals(1, scanText("высшее образование", EducationLevel), "Нижний регистр должен быть найден")
        assertEquals(1, scanText("капитан", MilitaryRank), "Нижний регистр должен быть найден")
        assertEquals(1, scanText("паспорт гражданина рф", IdentityDocType), "Нижний регистр должен быть найден")
        
        // Верхний регистр
        assertEquals(1, scanText("ЖЕНАТ", MaritalStatus), "Верхний регистр должен быть найден")
        assertEquals(1, scanText("ВЫСШЕЕ ОБРАЗОВАНИЕ", EducationLevel), "Верхний регистр должен быть найден")
        assertEquals(1, scanText("КАПИТАН", MilitaryRank), "Верхний регистр должен быть найден")
        assertEquals(1, scanText("ПАСПОРТ ГРАЖДАНИНА РФ", IdentityDocType), "Верхний регистр должен быть найден")
        
        // Смешанный регистр
        assertEquals(1, scanText("ЖеНаТ", MaritalStatus), "Смешанный регистр должен быть найден")
        assertEquals(1, scanText("ВыСшЕе ОбРаЗоВаНиЕ", EducationLevel), "Смешанный регистр должен быть найден")
        assertEquals(1, scanText("КаПиТаН", MilitaryRank), "Смешанный регистр должен быть найден")
        assertEquals(1, scanText("ПаСпОрТ ГрАжДаНиНа Рф", IdentityDocType), "Смешанный регистр должен быть найден")
    }

    @Test
    fun testEmptyStrings() {
        val text = ""
        assertEquals(0, scanText(text, Geo), "Пустая строка не должна содержать координат")
        assertEquals(0, scanText(text, MaritalStatus), "Пустая строка не должна содержать семейного положения")
        assertEquals(0, scanText(text, EducationLevel), "Пустая строка не должна содержать образования")
        assertEquals(0, scanText(text, MilitaryRank), "Пустая строка не должна содержать воинского звания")
        assertEquals(0, scanText(text, IdentityDocType), "Пустая строка не должна содержать типа документа")
        assertEquals(0, scanText(text, SecurityAffiliation), "Пустая строка не должна содержать силовой структуры")
    }

    @Test
    fun testWhitespaceOnly() {
        val text = "   \n\t\r\n   "
        assertEquals(0, scanText(text, Geo), "Строка с пробелами не должна содержать координат")
        assertEquals(0, scanText(text, MaritalStatus), "Строка с пробелами не должна содержать семейного положения")
        assertEquals(0, scanText(text, EducationLevel), "Строка с пробелами не должна содержать образования")
        assertEquals(0, scanText(text, MilitaryRank), "Строка с пробелами не должна содержать воинского звания")
        assertEquals(0, scanText(text, IdentityDocType), "Строка с пробелами не должна содержать типа документа")
        assertEquals(0, scanText(text, SecurityAffiliation), "Строка с пробелами не должна содержать силовой структуры")
    }

    @Test
    fun testMatchersInTable() {
        val text = """
            | Параметр              | Значение                     |
            |----------------------|------------------------------|
            | Семейное положение   | женат                        |
            | Образование          | высшее образование           |
            | Звание               | капитан                      |
            | Документ             | паспорт гражданина РФ        |
            | Координаты           | 55.7558, 37.6173             |
            | Место службы         | МВД России                   |
        """.trimIndent()
        
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение в таблице должно быть найдено")
        assertEquals(1, scanText(text, EducationLevel), "Образование в таблице должно быть найдено")
        assertEquals(1, scanText(text, MilitaryRank), "Звание в таблице должно быть найдено")
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа в таблице должен быть найден")
        assertEquals(1, scanText(text, Geo), "Координаты в таблице должны быть найдены")
        assertEquals(1, scanText(text, SecurityAffiliation), "Силовая структура в таблице должна быть найдена")
    }

    @Test
    fun testMatchersInJSON() {
        val text = """
            {
                "maritalStatus": "женат",
                "education": "высшее образование",
                "rank": "капитан",
                "documentType": "паспорт гражданина РФ",
                "coordinates": "55.7558, 37.6173",
                "affiliation": "МВД России"
            }
        """.trimIndent()
        
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение в JSON должно быть найдено")
        assertEquals(1, scanText(text, EducationLevel), "Образование в JSON должно быть найдено")
        assertEquals(1, scanText(text, MilitaryRank), "Звание в JSON должно быть найдено")
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа в JSON должен быть найден")
        assertEquals(1, scanText(text, Geo), "Координаты в JSON должны быть найдены")
        assertEquals(1, scanText(text, SecurityAffiliation), "Силовая структура в JSON должна быть найдена")
    }

    @Test
    fun testMatchersWithMultipleSpaces() {
        val text = """
            женат     с     2020
            высшее     образование
            капитан     армии
            паспорт     гражданина     РФ
        """.trimIndent()
        
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение с множественными пробелами должно быть найдено")
        assertEquals(1, scanText(text, EducationLevel), "Образование с множественными пробелами должно быть найдено")
        assertEquals(1, scanText(text, MilitaryRank), "Звание с множественными пробелами должно быть найдено")
        assertEquals(1, scanText(text, IdentityDocType), "Тип документа с множественными пробелами должен быть найден")
    }

    @Test
    fun testMatchersAtLineBreaks() {
        val text = "Статус:\nженат\nОбразование:\nвысшее образование\nЗвание:\nкапитан"
        
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение после переноса строки должно быть найдено")
        assertEquals(1, scanText(text, EducationLevel), "Образование после переноса строки должно быть найдено")
        assertEquals(1, scanText(text, MilitaryRank), "Звание после переноса строки должно быть найдено")
    }

    @Test
    fun testMatchersWithTabs() {
        val text = "Статус:\tженат\tОбразование:\tвысшее образование\tЗвание:\tкапитан"
        
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение с табуляцией должно быть найдено")
        assertEquals(1, scanText(text, EducationLevel), "Образование с табуляцией должно быть найдено")
        assertEquals(1, scanText(text, MilitaryRank), "Звание с табуляцией должно быть найдено")
    }

    @Test
    fun testMatchersWithMixedWhitespace() {
        val text = "Статус:  \t\n женат \n\t  Образование: \t высшее образование"
        
        assertEquals(1, scanText(text, MaritalStatus), "Семейное положение со смешанными пробелами должно быть найдено")
        assertEquals(1, scanText(text, EducationLevel), "Образование со смешанными пробелами должно быть найдено")
    }

    @Test
    fun testMultipleOccurrencesOnSameLine() {
        val text = "женат, холост, разведен"
        assertEquals(3, scanText(text, MaritalStatus), "Несколько значений на одной строке должны быть найдены")
    }

    @Test
    fun testMultipleOccurrencesOnDifferentLines() {
        val text = """
            Первый: женат
            Второй: холост
            Третий: разведен
        """.trimIndent()
        assertEquals(3, scanText(text, MaritalStatus), "Несколько значений на разных строках должны быть найдены")
    }

    private fun scanText(text: String, matcher: IMatcher): Int {
        val kotlinRes = KotlinEngine(listOf(matcher).filterIsInstance<IKotlinMatcher>()).use {
            it.scan(text)
        }
        val hyperRes = HyperScanEngine(listOf(matcher).filterIsInstance<IHyperMatcher>()).use {
            it.scan(text)
        }
        
        assertEquals(
            kotlinRes.count(),
            hyperRes.count(),
            "Количество совпадений для ${matcher.name} должно быть одинаковым для обоих движков. " +
            "Kotlin: ${kotlinRes.count()}, Hyper: ${hyperRes.count()}\nText: $text"
        )
        
        return kotlinRes.count()
    }
}

