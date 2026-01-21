package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for APO/FPO/DPO addresses (U.S. military postal addresses).
 * Matches military addresses used for U.S. Armed Forces, Fleet, and Diplomatic Post Offices.
 * Format: APO/FPO/DPO + space + AA/AE/AP + space + 5-digit ZIP code + optional ZIP+4 extension
 * Examples: APO AE 09355, FPO AP 96691-1234, DPO AA 34012
 * 
 * City codes:
 * - APO: Army/Air Post Office
 * - FPO: Fleet Post Office
 * - DPO: Diplomatic Post Office
 * 
 * State codes:
 * - AA: Armed Forces Americas (excluding Canada)
 * - AE: Armed Forces Europe, Middle East, Africa, and Canada
 * - AP: Armed Forces Pacific
 * 
 * ZIP code ranges by state:
 * - AA: ZIP prefix 340
 * - AE: ZIP prefixes 090-098
 * - AP: ZIP prefixes 962-966
 */
@Serializable
object APOFPODPO : IHyperMatcher, IKotlinMatcher {
    override val name = "APO/FPO/DPO"
    
    override val javaPatterns = listOf(
        """(?i)\b(?:APO|FPO|DPO)\s+(?:AA|AE|AP)\s+\d{5}(?:-\d{4})?(?!\d)\b"""
    )
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^0-9a-zA-Z])(?:APO|FPO|DPO)\s+(?:AA|AE|AP)\s+[0-9]{5}(?:-[0-9]{4})?(?:[^0-9]|$)"""
    )
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    private val validCityCodes = setOf("APO", "FPO", "DPO")
    private val validStateCodes = setOf("AA", "AE", "AP")

    override fun check(value: String): Boolean {
        // Extract address parts
        val match = Regex("""(APO|FPO|DPO)\s+(AA|AE|AP)\s+(\d{5})(?:-(\d{4}))?""", RegexOption.IGNORE_CASE).find(value)
        if (match == null) return false
        
        val cityCode = match.groupValues[1].uppercase()
        val stateCode = match.groupValues[2].uppercase()
        val zipCode = match.groupValues[3]
        val zipPlus4 = match.groupValues[4] // May be empty
        
        // Check validity of codes
        if (cityCode !in validCityCodes) return false
        if (stateCode !in validStateCodes) return false
        
        // Check ZIP code (5 digits)
        if (zipCode.length != 5 || !zipCode.all { it.isDigit() }) return false
        
        // If ZIP+4 exists, check that it contains 4 digits
        if (zipPlus4.isNotEmpty() && (zipPlus4.length != 4 || !zipPlus4.all { it.isDigit() })) {
            return false
        }
        
        // Optional: can check ZIP prefix match to region code
        // AA → 340, AE → 090-098, AP → 962-966
        // But for simplicity, keep only format validation
        
        // Filter out obvious fake patterns
        if (zipCode.all { it == '0' }) return false
        if (zipCode.all { it == zipCode[0] }) return false
        
        return true
    }

    override fun toString() = name
}