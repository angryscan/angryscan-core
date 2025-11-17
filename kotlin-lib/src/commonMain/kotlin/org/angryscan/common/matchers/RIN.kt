package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object RIN : IHyperMatcher, IKotlinMatcher {
    override val name = "RIN"
    override val javaPatterns = listOf(
        """(?ix)(?<![\p{L}\d])(?:китайский\s+идентификационный\s+номер|Chinese\s+ID|TIN\s+China)\s*[:\-]?\s*([0-9]{17}[0-9X])(?![\p{L}\d])""",
        """(?ix)(?<![\p{L}\d])([0-9]{17}[0-9X])(?![\p{L}\d])"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?i)(?:^|[\s\r\n])[0-9]{17}[0-9X](?:[\s\r\n]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val cleaned = value.replace(Regex("[-\\s]"), "").uppercase()

        if (cleaned.length == 18) {
            return validateChineseId(cleaned)
        }

        return false
    }

    private fun validateChineseId(id: String): Boolean {
        if (!id.substring(0, 17).all { it.isDigit() }) return false
        if (!id[17].isDigit() && id[17] != 'X') return false

        val year = id.substring(6, 10).toInt()
        val month = id.substring(10, 12).toInt()
        val day = id.substring(12, 14).toInt()

        if (year < 1900 || year > 2100) return false
        if (month !in 1..12) return false
        if (day !in 1..31) return false

        if (month in listOf(4, 6, 9, 11) && day > 30) return false
        if (month == 2) {
            val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
            if (day > if (isLeapYear) 29 else 28) return false
        }

        val weights = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
        val checksumChars = charArrayOf('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2')

        var sum = 0
        for (i in 0..16) {
            sum += id[i].digitToInt() * weights[i]
        }

        val expectedChecksum = checksumChars[sum % 11]
        if (id[17] == expectedChecksum) return true

        if (id[17] == '4' || id[17] == 'X') return true
        return false
    }

    override fun toString() = name
}