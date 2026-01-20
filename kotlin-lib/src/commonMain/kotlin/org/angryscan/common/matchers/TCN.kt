package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Transportation Control Number (TCN).
 * Matches 17-character alphanumeric codes used to track shipments in the Defense Transportation System.
 * Format: Keyword "TCN" + optional separators (0-20 non-digit characters) + 17 alphanumeric characters.
 * Total: 20+ characters (keyword + separators + 17 alphanumeric)
 * Examples: TCN: HKR0627116X001XXX, TCN ABCDE5123A042XXX
 * 
 * TCN Structure (17 characters):
 * - Positions 1-6: DODAAC (Department of Defense Activity Address Code) - 6 alphanumeric characters
 * - Position 7: Last digit of calendar year (0-9)
 * - Positions 8-10: Julian day of year (001-366) - 3 digits
 * - Position 11: Tracking code letter (A-Z, typically "X")
 * - Positions 12-14: Serial/increment number (001-999, possibly alphanumeric if overflow)
 * - Position 15: Shipping point/warehouse code (usually "X")
 * - Position 16: Partial shipment code ("X" if unit shipped together, or "A", "B", "C", etc.)
 * - Position 17: Split shipment code (usually "X")
 * 
 * Note: TCNs do not use a checksum algorithm, only format validation.
 * Letters "I" and "O" are generally avoided in certain positions to avoid confusion with "1" and "0".
 */
@Serializable
object TCN : IHyperMatcher, IKotlinMatcher {
    override val name = "TCN"
    
    private val keywordPattern = """TCN"""
    private val numberPattern = """[A-Z0-9]{17}"""
    
    override val javaPatterns = listOf(
        """(?ix)(?<![\p{L}\d])\b$keywordPattern\b[^0-9A-Za-z]{0,20}$numberPattern(?!\w)"""
    )
    
    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        return javaPatterns
    }
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^a-zA-Z0-9])TCN[^0-9A-Za-z]{0,20}[A-Z0-9]{17}(?:[^a-zA-Z0-9]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        return hyperPatterns
    }
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Extract 17-character number from match (may include keyword and separators)
        val numberPattern = Regex("""[A-Z0-9]{17}""", RegexOption.IGNORE_CASE)
        val match = numberPattern.find(value)
        if (match == null) return false
        
        val tcn = match.value.uppercase()
        
        // Must be exactly 17 characters
        if (tcn.length != 17) return false
        
        // All characters must be letters or digits
        if (!tcn.all { it.isLetterOrDigit() }) return false
        
        // Filter out obvious fake patterns
        // Check that not all characters are the same
        if (tcn.all { it == tcn[0] }) return false
        
        // Check that not all digits are zeros (if all characters are digits)
        if (tcn.all { it.isDigit() } && tcn.all { it == '0' }) return false
        
        // Check that not all letters are the same (if all characters are letters)
        if (tcn.all { it.isLetter() } && tcn.all { it == tcn[0] }) return false
        
        return true
    }

    override fun toString() = name
}