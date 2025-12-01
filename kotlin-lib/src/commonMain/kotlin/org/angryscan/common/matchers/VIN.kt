package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object VIN : IHyperMatcher, IKotlinMatcher {
    override val name = "VIN"
    override val javaPatterns = listOf(
        """(?i)(?<![\p{L}\d])([A-HJ-NPR-Z0-9]{17})(?![\p{L}\d])(?=[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\(\)\[\]\"'.,;:])[A-HJ-NPR-Z0-9]{17}(?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val cleaned = value.replace(Regex("[^A-HJ-NPR-Z0-9]"), "").uppercase()
        
        if (cleaned.length != 17) return false
        
        if (cleaned.contains('I') || cleaned.contains('O') || cleaned.contains('Q')) return false
        
        val weights = intArrayOf(8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2)
        
        val values = mapOf(
            'A' to 1, 'B' to 2, 'C' to 3, 'D' to 4, 'E' to 5, 'F' to 6, 'G' to 7, 'H' to 8,
            'J' to 1, 'K' to 2, 'L' to 3, 'M' to 4, 'N' to 5, 'P' to 7, 'R' to 9,
            'S' to 2, 'T' to 3, 'U' to 4, 'V' to 5, 'W' to 6, 'X' to 7, 'Y' to 8, 'Z' to 9
        )
        
        var sum = 0
        for (i in cleaned.indices) {
            val char = cleaned[i]
            val value = if (char.isDigit()) char.digitToInt() else values[char] ?: return false
            sum += value * weights[i]
        }
        
        val checkDigit = sum % 11
        val expectedChar = if (checkDigit == 10) 'X' else checkDigit.toString()[0]
        
        return cleaned[8] == expectedChar
    }

    override fun toString() = name
}
