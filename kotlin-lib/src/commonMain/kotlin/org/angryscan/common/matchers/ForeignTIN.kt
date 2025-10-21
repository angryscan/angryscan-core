package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object ForeignTIN : IHyperMatcher, IKotlinMatcher {
    override val name = "Foreign TIN"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:
          [иИ]ностранный\s+[нН]алоговый\s+[иИ]дентификационный\s+[нН]омер|
          [fF]oreign\s+TIN|
          TIN\s+(US|China)
        )?
        \s*[:\-]?\s*
        (?:
          (\d{3}[-\s]\d{2}[-\s]\d{4})|
          ([A-Z0-9]{18})
        )
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n])(?:\d{3}[-\s]\d{2}[-\s]\d{4}|[A-Z0-9]{18})\b"""
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

        return true
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
        return id[17] == expectedChecksum
    }

    override fun toString() = name
}
