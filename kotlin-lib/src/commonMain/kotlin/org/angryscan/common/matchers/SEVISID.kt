package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for SEVIS ID (Student and Exchange Visitor Information System ID).
 * Matches unique identifiers assigned by SEVIS/SEVP to non-immigrant students and exchange visitors in the US.
 * Format: Letter "N" followed by exactly 10 digits.
 * Total: 11 characters (1 letter + 10 digits)
 * Examples: N0001234567, N1234567890
 * Note: SEVIS IDs do not use a checksum algorithm, only format validation.
 * The SEVIS ID appears on documents like I-20 and DS-2019 forms.
 */
@Serializable
object SEVISID : IHyperMatcher, IKotlinMatcher {
    override val name = "SEVIS ID"
    
    override val javaPatterns = listOf(
        """(?<![\d\p{L}])N\d{10}(?![\d\p{L}])"""
    )
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^0-9a-zA-Z])N[0-9]{10}(?:[^0-9a-zA-Z]|$)"""
    )
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Extract only letters and digits (remove other characters)
        val cleaned = value.filter { it.isLetterOrDigit() }.uppercase()
        
        // Must start with N
        if (cleaned.isEmpty() || cleaned[0] != 'N') return false
        
        // After N there should be only digits
        val digits = cleaned.substring(1)
        if (digits.isEmpty()) return false
        if (!digits.all { it.isDigit() }) return false
        
        // Must be exactly 10 digits
        if (digits.length != 10) return false
        
        // Filter out obvious fake patterns
        if (digits.all { it == '0' }) return false
        if (digits.all { it == digits[0] }) return false
        
        return true
    }

    override fun toString() = name
}