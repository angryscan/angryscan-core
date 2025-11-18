package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера MilitaryRank
 */
internal class MilitaryRankTest: MatcherTestBase(MilitaryRank) {

    @Test
    fun testMilitaryRankAtStart() {
        val text = "майор Иванов служит в армии"
        assertEquals(1, scanText(text), "Воинское звание в начале должно быть найдено")
    }

    @Test
    fun testMilitaryRankAtEnd() {
        val text = "Звание: капитан"
        assertEquals(1, scanText(text), "Воинское звание в конце должно быть найдено")
    }

    @Test
    fun testMilitaryRankInMiddle() {
        val text = "Офицер полковник Петров выступил с докладом"
        assertEquals(1, scanText(text), "Воинское звание в середине должно быть найдено")
    }

    @Test
    fun testMilitaryRankStandalone() {
        val text = "сержант"
        assertEquals(1, scanText(text), "Воинское звание отдельной строкой должно быть найдено")
    }

    @Test
    fun testMilitaryRankLowestRanks() {
        val text = """
            рядовой
            матрос
            ефрейтор
            старший матрос
        """.trimIndent()
        assertEquals(4, scanText(text), "Низшие воинские звания должны быть найдены")
    }

    @Test
    fun testMilitaryRankHighestRanks() {
        val text = """
            генерал армии
            адмирал флота
            маршал Российской Федерации
        """.trimIndent()
        assertEquals(3, scanText(text), "Высшие воинские звания должны быть найдены")
    }

    @Test
    fun testMilitaryRankWithService() {
        val text = "майор медицинской службы"
        assertEquals(1, scanText(text), "Воинское звание с родом службы должно быть найдено")
    }

    @Test
    fun testMilitaryRankAllOfficerRanks() {
        val text = """
            младший лейтенант
            лейтенант
            старший лейтенант
            капитан
            майор
            подполковник
            полковник
        """.trimIndent()
        assertEquals(7, scanText(text), "Все офицерские звания должны быть найдены")
    }

    @Test
    fun testMilitaryRankNavyRanks() {
        val text = """
            капитан-лейтенант
            капитан 3 ранга
            капитан 2 ранга
            капитан 1 ранга
            контр-адмирал
            вице-адмирал
            адмирал
        """.trimIndent()
        assertEquals(7, scanText(text), "Все морские звания должны быть найдены")
    }

    @Test
    fun testMilitaryRankSergeantRanks() {
        val text = """
            младший сержант
            сержант
            старший сержант
            старшина
        """.trimIndent()
        assertEquals(4, scanText(text), "Все сержантские звания должны быть найдены")
    }

    @Test
    fun testMilitaryRankNavySergeantRanks() {
        val text = """
            старшина 2 статьи
            старшина 1 статьи
            главный старшина
            главный корабельный старшина
        """.trimIndent()
        assertEquals(4, scanText(text), "Все морские сержантские звания должны быть найдены")
    }

    @Test
    fun testMilitaryRankWarrantOfficerRanks() {
        val text = """
            прапорщик
            старший прапорщик
            мичман
            старший мичман
        """.trimIndent()
        assertEquals(4, scanText(text), "Все прапорщики и мичманы должны быть найдены")
    }

    @Test
    fun testMilitaryRankGeneralRanks() {
        val text = """
            генерал-майор
            генерал-лейтенант
            генерал-полковник
            генерал армии
        """.trimIndent()
        assertEquals(4, scanText(text), "Все генеральские звания должны быть найдены")
    }

    @Test
    fun testMilitaryRankAdmiralRanks() {
        val text = """
            контр-адмирал
            вице-адмирал
            адмирал
            адмирал флота
        """.trimIndent()
        assertEquals(4, scanText(text), "Все адмиральские звания должны быть найдены")
    }

    @Test
    fun testMilitaryRankWithLabel() {
        val text = "Воинское звание: капитан"
        assertEquals(1, scanText(text), "Звание с меткой должно быть найдено")
    }

    @Test
    fun testMilitaryRankInParentheses() {
        val text = "Иванов (полковник) в отставке"
        assertEquals(1, scanText(text), "Звание в скобках должно быть найдено")
    }

    @Test
    fun testMilitaryRankWithPunctuation() {
        val text = "Звание: майор."
        assertEquals(1, scanText(text), "Звание с точкой должно быть найдено")
    }

    @Test
    fun testMilitaryRankUpperCase() {
        val text = "КАПИТАН"
        assertEquals(1, scanText(text), "Звание в верхнем регистре должно быть найдено")
    }

    @Test
    fun testMilitaryRankMixedCase() {
        val text = "МаЙоР"
        assertEquals(1, scanText(text), "Звание в смешанном регистре должно быть найдено")
    }

    @Test
    fun testMilitaryRankEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать воинского звания")
    }
}

