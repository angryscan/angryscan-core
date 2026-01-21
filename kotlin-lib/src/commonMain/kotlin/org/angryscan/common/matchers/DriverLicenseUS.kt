package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for US Driver's License numbers from various states.
 * Supports multiple formats:
 * - Colorado: ##-###-####
 * - Florida: L-###-###-###-### (formatted)
 * - North Dakota: ABC-12-3456
 * - New Jersey: A + 14 digits
 * - Washington: DOE**MJ501P1 (literal **)
 * - Wisconsin: J525-4209-0465-05
 * Note: Driver's license formats vary by state and do not use checksum algorithms.
 */
@Serializable
object DriverLicenseUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Driver License US"
    
    // Colorado: ##-###-####
    private val patternCO = """(?<!\d)\d{2}-\d{3}-\d{4}(?!\d)"""
    
    // Florida: L-###-###-###-###
    private val patternFL = """(?i)\b[A-Z]-\d{3}-\d{3}-\d{3}-\d{3}\b"""
    
    // North Dakota: ABC-12-3456
    private val patternND = """(?i)\b[A-Z]{3}-\d{2}-\d{4}\b"""
    
    // New Jersey: A + 14 digits
    private val patternNJ = """(?i)\b[A-Z]\d{14}\b"""
    
    // Washington: DOE**MJ501P1 (literal **)
    private val patternWA = """(?i)\b[A-Z]{3}\*\*[A-Z]{2}\d{3}[A-Z]\d\b"""
    
    // Wisconsin: J525-4209-0465-05
    private val patternWI = """(?i)\b[A-Z]\d{3}-\d{4}-\d{4}-\d{2}\b"""
    
    override val javaPatterns = listOf(
        patternCO,
        patternFL,
        patternND,
        patternNJ,
        patternWA,
        patternWI
    )
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    // HyperScan patterns - use PhoneUS approach for left boundary, but stricter right boundary
    // PhoneUS works with (?:[^0-9]|$) on the right, but for some patterns a stricter boundary may be needed
    // Try [^0-9a-zA-Z] for right boundary to explicitly exclude letters after the number
    // Colorado: ##-###-#### (starts with digit, hyphen is part of format)
    private val hyperPatternCO = """(?:^|[^a-zA-Z0-9])\d{2}-\d{3}-\d{4}(?:[^0-9a-zA-Z]|$)"""
    
    // Florida: L-###-###-###-### (starts with letter, hyphen is part of format)
    private val hyperPatternFL = """(?:^|[^a-zA-Z0-9])[A-Z]-\d{3}-\d{3}-\d{3}-\d{3}(?:[^0-9a-zA-Z]|$)"""
    
    // North Dakota: ABC-12-3456 (starts with letters, hyphen is part of format)
    private val hyperPatternND = """(?:^|[^a-zA-Z0-9])[A-Z]{3}-\d{2}-\d{4}(?:[^0-9a-zA-Z]|$)"""
    
    // New Jersey: A + 14 digits (no hyphens) - use [^0-9a-zA-Z] for both boundaries like ITIN
    private val hyperPatternNJ = """(?:^|[^0-9a-zA-Z])[A-Z]\d{14}(?:[^0-9a-zA-Z]|$)"""
    
    // Washington: DOE**MJ501P1 (no hyphens, contains asterisks)
    private val hyperPatternWA = """(?:^|[^0-9a-zA-Z\*])[A-Z]{3}\*\*[A-Z]{2}\d{3}[A-Z]\d(?:[^0-9a-zA-Z\*]|$)"""
    
    // Wisconsin: J525-4209-0465-05 (starts with letter, hyphen is part of format)
    private val hyperPatternWI = """(?:^|[^a-zA-Z0-9])[A-Z]\d{3}-\d{4}-\d{4}-\d{2}(?:[^0-9a-zA-Z]|$)"""
    
    override val hyperPatterns = listOf(
        // Order: more specific patterns first (with letters), then more general ones (digits only)
        hyperPatternWA,  // Washington - most specific (contains asterisks)
        hyperPatternWI,  // Wisconsin - long pattern with hyphens
        hyperPatternFL,  // Florida - long pattern with hyphens
        hyperPatternND,  // North Dakota - with letters at the start
        hyperPatternNJ,  // New Jersey - without hyphens
        hyperPatternCO   // Colorado - simplest (digits and hyphens only)
    )
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Check match against one of the formats
        
        // Colorado: ##-###-####
        if (value.matches(Regex("""\d{2}-\d{3}-\d{4}""", RegexOption.IGNORE_CASE))) {
            val digits = value.replace("-", "").filter { it.isDigit() }
            if (digits.length == 9 && !digits.all { it == '0' } && !digits.all { it == digits[0] }) {
                return true
            }
        }
        
        // Florida: L-###-###-###-###
        if (value.matches(Regex("""[A-Z]-\d{3}-\d{3}-\d{3}-\d{3}""", RegexOption.IGNORE_CASE))) {
            val digits = value.filter { it.isDigit() }
            if (digits.length == 12 && !digits.all { it == '0' } && !digits.all { it == digits[0] }) {
                return true
            }
        }
        
        // North Dakota: ABC-12-3456
        if (value.matches(Regex("""[A-Z]{3}-\d{2}-\d{4}""", RegexOption.IGNORE_CASE))) {
            val digits = value.filter { it.isDigit() }
            if (digits.length == 6 && !digits.all { it == '0' } && !digits.all { it == digits[0] }) {
                return true
            }
        }
        
        // New Jersey: A + 14 digits
        if (value.matches(Regex("""[A-Z]\d{14}""", RegexOption.IGNORE_CASE))) {
            val digits = value.filter { it.isDigit() }
            if (digits.length == 14 && !digits.all { it == '0' } && !digits.all { it == digits[0] }) {
                return true
            }
        }
        
        // Washington: DOE**MJ501P1
        if (value.matches(Regex("""[A-Z]{3}\*\*[A-Z]{2}\d{3}[A-Z]\d""", RegexOption.IGNORE_CASE))) {
            val digits = value.filter { it.isDigit() }
            if (digits.length == 4 && !digits.all { it == '0' } && !digits.all { it == digits[0] }) {
                return true
            }
        }
        
        // Wisconsin: J525-4209-0465-05
        if (value.matches(Regex("""[A-Z]\d{3}-\d{4}-\d{4}-\d{2}""", RegexOption.IGNORE_CASE))) {
            val digits = value.filter { it.isDigit() }
            if (digits.length == 13 && !digits.all { it == '0' } && !digits.all { it == digits[0] }) {
                return true
            }
        }
        
        return false
    }

    override fun toString() = name
}
