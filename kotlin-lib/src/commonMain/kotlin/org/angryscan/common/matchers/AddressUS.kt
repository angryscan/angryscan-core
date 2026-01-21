package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for US addresses.
 * Matches addresses containing house number (1-8 digits), address text (10-100 characters),
 * US state code (2-letter abbreviation), and ZIP code (5 digits).
 * Format: [House Number] [Street Name] [State] [ZIP Code]
 */
@Serializable
object AddressUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Address US"
    
    private val stateCodes = """AK|AL|AR|AZ|CA|CO|CT|DC|DE|FL|GA|HI|IA|ID|IL|IN|KS|KY|LA|MA|MD|ME|MI|MN|MO|MS|MT|NC|ND|NE|NH|NJ|NM|NV|NY|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VA|VT|WA|WI|WV|WY"""
    
    override val javaPatterns = listOf(
        """\b\d{1,8}\b[\s\S]{7,100}?\b($stateCodes)\b\s\d{5}\b""",
        """(?i)\b\d{1,8}\b[\s\S]{7,100}?\b([a-zA-Z]{2})\b\s\d{5}\b"""
    )
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^0-9])\d{1,8}[a-zA-Z0-9\s.,#\\-]{7,30}[A-Z]{2}\s\d{5}(?:[^0-9]|$)""",
        """(?:^|[^0-9])\d{1,8}[a-zA-Z0-9\s.,#\\-]{7,30}[a-z]{2}\s\d{5}(?:[^0-9]|$)""",
        """(?:^|[^0-9])\d{1,8}[a-zA-Z0-9\s.,#\\-]{7,30}[A-Z][a-z]\s\d{5}(?:[^0-9]|$)"""
    )
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    private val validStateCodes = setOf(
        "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", 
        "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", 
        "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", 
        "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", 
        "UT", "VA", "VT", "WA", "WI", "WV", "WY"
    )

    override fun check(value: String): Boolean {
        // Check that state code is valid (support any case)
        val stateMatch = Regex("""\s+([a-zA-Z]{2})\s+(\d{5})""", RegexOption.IGNORE_CASE).find(value)
        if (stateMatch == null) return false
        
        val stateCode = stateMatch.groupValues[1].uppercase()
        val zipCode = stateMatch.groupValues[2]
        
        // Check validity of state code
        if (stateCode !in validStateCodes) return false
        
        // Check that ZIP code contains exactly 5 digits
        if (zipCode.length != 5 || !zipCode.all { it.isDigit() }) return false
        
        // Check minimum length of address part (must be at least 7 characters between house number and state)
        // Extract part before state code
        val beforeState = stateMatch.range.first
        val addressWithNumber = value.substring(0, beforeState)
        val addressPart = addressWithNumber.replace(Regex("""^\d{1,8}\s*"""), "").trim()
        if (addressPart.length < 7) return false
        
        return true
    }

    override fun toString() = name
}
