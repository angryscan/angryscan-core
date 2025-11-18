package org.angryscan.common.matchers

import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера Geo
 * 
 * Крайние позиции:
 * - В начале строки
 * - В конце строки
 * - В середине строки
 * - Отдельной строкой
 * 
 * Пограничные значения:
 * - Минимальные/максимальные допустимые значения широты и долготы
 * - Высокая точность координат
 * - Нулевые координаты
 */
internal class GeoTest: MatcherTestBase(Geo) {
    
    @Test
    fun testGeoAtStartOfLine() {
        val text = "55.755812, 37.617345 - это координаты Москвы"
        assertEquals(1, scanText(text), "Координаты в начале строки должны быть найдены")
    }

    @Test
    fun testGeoAtEndOfLine() {
        val text = "Координаты Москвы: 55.755812, 37.617345"
        assertEquals(1, scanText(text), "Координаты в конце строки должны быть найдены")
    }

    @Test
    fun testGeoInMiddle() {
        val text = "Место встречи 55.755812, 37.617345 будет удобным"
        assertEquals(1, scanText(text), "Координаты в середине строки должны быть найдены")
    }

    @Test
    fun testGeoStandalone() {
        val text = "55.755812, 37.617345"
        assertEquals(1, scanText(text), "Координаты отдельной строкой должны быть найдены")
    }

    @Test
    fun testGeoMinLatitude() {
        val text = "-90.000, 10.000"
        assertEquals(1, scanText(text), "Минимальная широта должна быть найдена")
    }

    @Test
    fun testGeoMaxLatitude() {
        val text = "90.000, 10.000"
        assertEquals(1, scanText(text), "Максимальная широта должна быть найдена")
    }

    @Test
    fun testGeoMinLongitude() {
        val text = "10.000, -180.000"
        assertEquals(1, scanText(text), "Минимальная долгота должна быть найдена")
    }

    @Test
    fun testGeoMaxLongitude() {
        val text = "10.000, 180.000"
        assertEquals(1, scanText(text), "Максимальная долгота должна быть найдена")
    }

    @Test
    fun testGeoInvalidLatitude() {
        val text = "91.000, 10.000"
        assertEquals(0, scanText(text), "Некорректная широта не должна быть найдена")
    }

    @Test
    fun testGeoInvalidLongitude() {
        val text = "10.000, 181.000"
        assertEquals(0, scanText(text), "Некорректная долгота не должна быть найдена")
    }

    @Test
    fun testGeoHighPrecision() {
        val text = "55.7558123456, 37.6173987654"
        assertEquals(1, scanText(text), "Координаты с высокой точностью должны быть найдены")
    }

    @Test
    fun testGeoZeroCoordinates() {
        val text = "0.0, 0.0"
        assertEquals(0, scanText(text), "Нулевые координаты не должны быть найдены")
    }

    @Test
    fun testGeoMultipleOnDifferentLines() {
        val text = """
            Первая точка: 55.755812, 37.617345
            Вторая точка: 59.934312, 30.335123
            Третья точка: 43.256712, 76.928645
        """.trimIndent()
        assertEquals(3, scanText(text), "Несколько координат на разных строках должны быть найдены")
    }

    @Test
    fun testGeoNegativeCoordinates() {
        val text = "-33.868812, -151.209345"
        assertEquals(1, scanText(text), "Отрицательные координаты должны быть найдены")
    }

    @Test
    fun testGeoWithLabel() {
        val text = "Геолокация ФЛ: 55.755812, 37.617345"
        assertEquals(1, scanText(text), "Координаты с меткой должны быть найдены")
    }

    @Test
    fun testGeoWithFullLabel() {
        val text = "Координаты физического лица: 55.755812, 37.617345"
        assertEquals(1, scanText(text), "Координаты с полной меткой должны быть найдены")
    }

    @Test
    fun testGeoBoundaryLatitudePositive() {
        val text = "89.9999999999, 10.000"
        assertEquals(1, scanText(text), "Граничная положительная широта должна быть найдена")
    }

    @Test
    fun testGeoBoundaryLatitudeNegative() {
        val text = "-89.9999999999, 10.000"
        assertEquals(1, scanText(text), "Граничная отрицательная широта должна быть найдена")
    }

    @Test
    fun testGeoBoundaryLongitudePositive() {
        val text = "10.000, 179.9999999999"
        assertEquals(1, scanText(text), "Граничная положительная долгота должна быть найдена")
    }

    @Test
    fun testGeoBoundaryLongitudeNegative() {
        val text = "10.000, -179.9999999999"
        assertEquals(1, scanText(text), "Граничная отрицательная долгота должна быть найдена")
    }

    @Test
    fun testGeoInParentheses() {
        val text = "Место (55.755812, 37.617345) на карте"
        assertEquals(1, scanText(text), "Координаты в скобках должны быть найдены")
    }

    @Test
    fun testGeoInBrackets() {
        val text = "Точка (55.755812, 37.617345) отмечена"
        assertEquals(1, scanText(text), "Координаты в скобках должны быть найдены (квадратные скобки исключаются)")
    }

    @Test
    fun testGeoInQuotes() {
        val text = "Координаты \"55.755812, 37.617345\" сохранены"
        assertEquals(1, scanText(text), "Координаты в кавычках должны быть найдены")
    }

    @Test
    fun testGeoWithPunctuation() {
        val text = "Место: 55.755812, 37.617345."
        assertEquals(1, scanText(text), "Координаты с точкой в конце должны быть найдены")
    }

    @Test
    fun testGeoMinimalDecimal() {
        val text = "1.123, 1.123"
        assertEquals(1, scanText(text), "Минимальные десятичные координаты с >= 3 знаками должны быть найдены")
    }

    @Test
    fun testGeoLessThanThreeDecimals() {
        val text = "55.75, 37.61"
        assertEquals(0, scanText(text), "Координаты с < 3 знаками после запятой без метки не должны быть найдены")
    }

    @Test
    fun testGeoVerySmallValues() {
        val text = "0.048, 0.158"
        assertEquals(0, scanText(text), "Очень маленькие координаты (< 1.0) не должны быть найдены")
    }

    @Test
    fun testGeoMathematicalNotation() {
        val text = "λ~N(1.856, 0.047)"
        assertEquals(0, scanText(text), "Математические обозначения не должны быть найдены")
    }

    @Test
    fun testGeoStatisticalTermsInValue() {
        val text = "iter 55.755812, 37.617345 ewma"
        val kotlinEngine = KotlinEngine(listOf(Geo))
        val kotlinRes = kotlinEngine.scan(text)
        assertEquals(1, kotlinRes.count(), "Координаты должны быть найдены, даже если рядом есть статистические термины (check() проверяет только найденное значение)")
    }

    @Test
    fun testGeoIntegerCoordinates() {
        val text = "55, 37"
        assertEquals(0, scanText(text), "Целые координаты без метки не должны быть найдены")
    }

    @Test
    fun testGeoEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать координат")
    }

    @Test
    fun testGeoWhitespaceOnly() {
        val text = "   \n\t\r\n   "
        assertEquals(0, scanText(text), "Строка с пробелами не должна содержать координат")
    }
}

