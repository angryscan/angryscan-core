package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for DOD ID / EDIPI (Electronic Data Interchange Personal Identifier).
 * Matches unique identifiers assigned by the Department of Defense to personnel in DEERS.
 * Format: 10-digit numeric identifier.
 * The EDIPI is used on Common Access Cards (CAC) and other DoD ID credentials.
 * May be preceded by keywords like "dod id", "edipi".
 * Note: EDIPI uses a check digit algorithm (10th digit validates the first 9 digits),
 * but the specific algorithm is not publicly documented.
 */
@Serializable
object DODID : IHyperMatcher, IKotlinMatcher {
    override val name = "DOD ID"
    
    private val keywordsPattern = """
        (?:
          dod\s*id|
          edipi|
          department\s+of\s+defense\s+id|
          electronic\s+data\s+interchange\s+personal\s+identifier
        )
    """.trimIndent()
    
    private val numberPattern = """\d{10}"""
    
    override val javaPatterns = listOf(
        """(?ix)(?<![\p{L}\d])$keywordsPattern[^0-9]{0,10}$numberPattern(?![\d\p{L}])"""
    )
    
    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        return javaPatterns
    }
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^a-zA-Z0-9])(?:dod\s*id|edipi)[^0-9]{0,10}[0-9]{10}(?:[^0-9a-zA-Z]|$)"""
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
        // Extract 10-digit number from match (may include keywords)
        val numberPattern = Regex("""\d{10}""")
        val match = numberPattern.find(value)
        if (match == null) return false
        
        val digits = match.value
        
        // Must be exactly 10 digits
        if (digits.length != 10) return false
        
        // All characters must be digits
        if (!digits.all { it.isDigit() }) return false
        
        // Filter out obvious fake patterns
        if (digits.all { it == '0' }) return false
        if (digits.all { it == digits[0] }) return false
        
        // Note: EDIPI uses a check digit (10th digit validates first 9),
        // but the algorithm is not publicly documented, so we skip checksum validation
        
        return true
    }

    override fun toString() = name
}