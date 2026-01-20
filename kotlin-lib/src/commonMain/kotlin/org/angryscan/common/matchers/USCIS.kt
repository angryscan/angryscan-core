package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for USCIS Receipt Number.
 * Matches 13-character identifiers assigned by USCIS to applications and petitions.
 * Format: 3-letter prefix (EAC, WAC, LIN, SRC, NBC, MSC, IOE) followed by 10 digits.
 * Total: 13 characters (3 letters + 10 digits)
 * Prefixes represent service centers:
 * - EAC: Eastern Adjudication Center (Vermont Service Center)
 * - WAC: Western Adjudication Center (California Service Center)
 * - LIN: Lincoln / Nebraska Service Center
 * - SRC: Southern Regional Center / Texas Service Center
 * - NBC: National Benefits Center
 * - MSC: Missouri Service Center / National Benefits Center
 * - IOE: Electronic filing / Integrated Operating Environment
 * Note: USCIS Receipt Numbers do not use a checksum algorithm, only format validation.
 */
@Serializable
object USCIS : IHyperMatcher, IKotlinMatcher {
    override val name = "USCIS"
    
    override val javaPatterns = listOf(
        """(?<![\d\p{L}])(?:EAC|WAC|LIN|SRC|NBC|MSC|IOE)\d{10}(?![\d\p{L}])"""
    )
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^0-9a-zA-Z])(?:EAC|WAC|LIN|SRC|NBC|MSC|IOE)[0-9]{10}(?:[^0-9a-zA-Z]|$)"""
    )
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    private val validPrefixes = setOf(
        "EAC", "WAC", "LIN", "SRC", "NBC", "MSC", "IOE"
    )

    override fun check(value: String): Boolean {
        // Extract only letters and digits (remove hyphens and other characters)
        val cleaned = value.filter { it.isLetterOrDigit() }
        
        // Must be exactly 13 characters (3 letters + 10 digits)
        if (cleaned.length != 13) return false
        
        // First 3 characters must be letters (prefix)
        val prefix = cleaned.substring(0, 3).uppercase()
        if (!prefix.all { it.isLetter() }) return false
        
        // Prefix must be from valid list
        if (prefix !in validPrefixes) return false
        
        // Last 10 characters must be digits
        val digits = cleaned.substring(3)
        if (digits.length != 10) return false
        if (!digits.all { it.isDigit() }) return false
        
        // Filter out obvious fake patterns
        if (digits.all { it == '0' }) return false
        if (digits.all { it == digits[0] }) return false
        
        return true
    }

    override fun toString() = name
}