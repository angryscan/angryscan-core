package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for US Individual Taxpayer Identification Number (ITIN).
 * Matches 9-digit tax identification numbers for individuals who are not eligible for SSN.
 * Format: 9XX-7X-XXXX or 9XX-8X-XXXX (9 digits with hyphens in format NNN-NN-NNNN)
 * Pattern: Starts with 9, then 2 digits, hyphen, digit 7 or 8, one digit, hyphen, 4 digits.
 * Note: ITIN does not use a checksum algorithm, only format validation.
 */
@Serializable
object ITIN : IHyperMatcher, IKotlinMatcher {
    override val name = "ITIN"
    
    override val javaPatterns = listOf(
        """\b9[0-9]{2}-[7-8][0-9]-[0-9]{4}\b"""
    )
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^0-9a-zA-Z])9[0-9]{2}-[7-8][0-9]-[0-9]{4}(?:[^0-9a-zA-Z]|$)"""
    )
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Extract only digits and hyphens
        val cleaned = value.replace(Regex("[^0-9-]"), "")
        
        // Check format: must be digits with hyphens in format 9XX-7X-XXXX or 9XX-8X-XXXX
        if (!cleaned.matches(Regex("""9[0-9]{2}-[7-8][0-9]-[0-9]{4}"""))) return false
        
        // Extract only digits for additional checks
        val digitsOnly = cleaned.replace("-", "")
        if (digitsOnly.length != 9) return false
        
        // Check that the first digit is 9
        if (digitsOnly[0] != '9') return false
        
        // Check that the 4th digit is 7 or 8
        val fourthDigit = digitsOnly[3].digitToInt()
        if (fourthDigit != 7 && fourthDigit != 8) return false
        
        // Filter out obvious fake patterns
        if (digitsOnly.all { it == '0' }) return false
        if (digitsOnly.all { it == digitsOnly[0] }) return false
        
        return true
    }

    override fun toString() = name
}
