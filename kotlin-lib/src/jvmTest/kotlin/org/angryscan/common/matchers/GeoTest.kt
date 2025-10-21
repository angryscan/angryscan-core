package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
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
internal class GeoTest {

    @Test
    fun testGeoAtStartOfLine() {
        val text = "55.7558, 37.6173 - это координаты Москвы"
        assertEquals(1, scanText(text, Geo), "Координаты в начале строки должны быть найдены")
    }

    @Test
    fun testGeoAtEndOfLine() {
        val text = "Координаты Москвы: 55.7558, 37.6173"
        assertEquals(1, scanText(text, Geo), "Координаты в конце строки должны быть найдены")
    }

    @Test
    fun testGeoInMiddle() {
        val text = "Место встречи 55.7558, 37.6173 будет удобным"
        assertEquals(1, scanText(text, Geo), "Координаты в середине строки должны быть найдены")
    }

    @Test
    fun testGeoStandalone() {
        val text = "55.7558, 37.6173"
        assertEquals(1, scanText(text, Geo), "Координаты отдельной строкой должны быть найдены")
    }

    @Test
    fun testGeoMinLatitude() {
        val text = "-90.0, 0.0"
        assertEquals(1, scanText(text, Geo), "Минимальная широта должна быть найдена")
    }

    @Test
    fun testGeoMaxLatitude() {
        val text = "90.0, 0.0"
        assertEquals(1, scanText(text, Geo), "Максимальная широта должна быть найдена")
    }

    @Test
    fun testGeoMinLongitude() {
        val text = "0.0, -180.0"
        assertEquals(1, scanText(text, Geo), "Минимальная долгота должна быть найдена")
    }

    @Test
    fun testGeoMaxLongitude() {
        val text = "0.0, 180.0"
        assertEquals(1, scanText(text, Geo), "Максимальная долгота должна быть найдена")
    }

    @Test
    fun testGeoInvalidLatitude() {
        val text = "91.0, 0.0"
        assertEquals(0, scanText(text, Geo), "Некорректная широта не должна быть найдена")
    }

    @Test
    fun testGeoInvalidLongitude() {
        val text = "0.0, 181.0"
        assertEquals(0, scanText(text, Geo), "Некорректная долгота не должна быть найдена")
    }

    @Test
    fun testGeoHighPrecision() {
        val text = "55.7558123456, 37.6173987654"
        assertEquals(1, scanText(text, Geo), "Координаты с высокой точностью должны быть найдены")
    }

    @Test
    fun testGeoZeroCoordinates() {
        val text = "0.0, 0.0"
        assertEquals(1, scanText(text, Geo), "Нулевые координаты должны быть найдены")
    }

    @Test
    fun testGeoMultipleOnDifferentLines() {
        val text = """
            Первая точка: 55.7558, 37.6173
            Вторая точка: 59.9343, 30.3351
            Третья точка: 43.2567, 76.9286
        """.trimIndent()
        assertEquals(3, scanText(text, Geo), "Несколько координат на разных строках должны быть найдены")
    }

    @Test
    fun testGeoNegativeCoordinates() {
        val text = "-33.8688, -151.2093"
        assertEquals(1, scanText(text, Geo), "Отрицательные координаты должны быть найдены")
    }

    @Test
    fun testGeoWithLabel() {
        val text = "Геолокация ФЛ: 55.7558, 37.6173"
        assertEquals(1, scanText(text, Geo), "Координаты с меткой должны быть найдены")
    }

    @Test
    fun testGeoWithFullLabel() {
        val text = "Координаты физического лица: 55.7558, 37.6173"
        assertEquals(1, scanText(text, Geo), "Координаты с полной меткой должны быть найдены")
    }

    @Test
    fun testGeoBoundaryLatitudePositive() {
        val text = "89.9999999999, 0.0"
        assertEquals(1, scanText(text, Geo), "Граничная положительная широта должна быть найдена")
    }

    @Test
    fun testGeoBoundaryLatitudeNegative() {
        val text = "-89.9999999999, 0.0"
        assertEquals(1, scanText(text, Geo), "Граничная отрицательная широта должна быть найдена")
    }

    @Test
    fun testGeoBoundaryLongitudePositive() {
        val text = "0.0, 179.9999999999"
        assertEquals(1, scanText(text, Geo), "Граничная положительная долгота должна быть найдена")
    }

    @Test
    fun testGeoBoundaryLongitudeNegative() {
        val text = "0.0, -179.9999999999"
        assertEquals(1, scanText(text, Geo), "Граничная отрицательная долгота должна быть найдена")
    }

    @Test
    fun testGeoInParentheses() {
        val text = "Место (55.7558, 37.6173) на карте"
        assertEquals(1, scanText(text, Geo), "Координаты в скобках должны быть найдены")
    }

    @Test
    fun testGeoInBrackets() {
        val text = "Точка [55.7558, 37.6173] отмечена"
        assertEquals(1, scanText(text, Geo), "Координаты в квадратных скобках должны быть найдены")
    }

    @Test
    fun testGeoInQuotes() {
        val text = "Координаты \"55.7558, 37.6173\" сохранены"
        assertEquals(1, scanText(text, Geo), "Координаты в кавычках должны быть найдены")
    }

    @Test
    fun testGeoWithPunctuation() {
        val text = "Место: 55.7558, 37.6173."
        assertEquals(1, scanText(text, Geo), "Координаты с точкой в конце должны быть найдены")
    }

    @Test
    fun testGeoMinimalDecimal() {
        val text = "0.1, 0.1"
        assertEquals(1, scanText(text, Geo), "Минимальные десятичные координаты должны быть найдены")
    }

    @Test
    fun testGeoEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, Geo), "Пустая строка не должна содержать координат")
    }

    @Test
    fun testGeoWhitespaceOnly() {
        val text = "   \n\t\r\n   "
        assertEquals(0, scanText(text, Geo), "Строка с пробелами не должна содержать координат")
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

