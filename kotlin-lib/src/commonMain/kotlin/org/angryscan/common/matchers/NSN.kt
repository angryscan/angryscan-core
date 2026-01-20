package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for National Stock Number (NSN) / NATO Stock Number.
 * Matches 13-digit stock numbers used by NATO and U.S. Department of Defense.
 * Format: Either 13 digits, or 4-2-3-4 digits with hyphens.
 * Total: 13 digits (either continuous or formatted as XXXX-XX-XXX-XXXX)
 * Examples: 1234567890123, 1234-56-789-0123
 * May be preceded by keywords like "nsn", "national stock number", "nato stock number", "stock number".
 * 
 * Structure breakdown:
 * - First 4 digits: Federal Supply Classification (FSC)
 * - Next 2 digits: National Codification Bureau (NCB) / country code
 * - Last 7 digits: National Item Identification Number (NIIN), displayed as 3-4
 * 
 * Note: NSNs do not use a checksum algorithm.
 * To reduce false positives, filters are applied for:
 * - Sequential numbers
 * - Repeating patterns
 * - Palindrome patterns
 * - Alternating patterns
 * - Numbers with too few unique digits
 */
@Serializable
object NSN : IHyperMatcher, IKotlinMatcher {
    override val name = "NSN"
    
    private val keywordsPattern = """(?:nsn|national\s+stock\s+number|nato\s+stock\s+number|stock\s+number)"""
    
    override val javaPatterns = listOf(
        // Pattern with keywords (more strict)
        """(?i)(?<![\p{L}\d])(?:nsn|national\s+stock\s+number|nato\s+stock\s+number|stock\s+number)\b[\s:=#"'()\[\]\{\}\-]*(?:\d{13}|\d{4}-\d{2}-\d{3}-\d{4})(?![\d\p{L}])""",
        // Pattern without keywords (fallback)
        """(?<![\d\p{L}])(?:\d{13}|\d{4}-\d{2}-\d{3}-\d{4})(?![\d\p{L}])"""
    )
    
    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        return if (requireKeywords) {
            // Return only pattern with keywords (more strict, reduces false positives)
            listOf(javaPatterns[0])
        } else {
            // Return both patterns (with and without keywords)
            javaPatterns
        }
    }
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns = listOf(
        // Pattern with keywords (more strict)
        """(?i)(?:^|[^a-zA-Z0-9])(?:nsn|national\s+stock\s+number|nato\s+stock\s+number|stock\s+number)\b[\s:=#"'\(\)\[\]\{\}\-]*(?:[0-9]{13}|[0-9]{4}-[0-9]{2}-[0-9]{3}-[0-9]{4})(?:[^0-9a-zA-Z]|$)""",
        // Pattern without keywords (fallback)
        """(?:^|[^0-9a-zA-Z])(?:[0-9]{13}|[0-9]{4}-[0-9]{2}-[0-9]{3}-[0-9]{4})(?:[^0-9a-zA-Z]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        return if (requireKeywords) {
            // Return only pattern with keywords (more strict, reduces false positives)
            listOf(hyperPatterns[0])
        } else {
            // Return both patterns (with and without keywords)
            hyperPatterns
        }
    }
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    private fun isSequential(digits: String, ascending: Boolean): Boolean {
        for (i in 1 until digits.length) {
            val current = digits[i].digitToInt()
            val previous = digits[i - 1].digitToInt()
            if (ascending) {
                if (current != previous + 1) return false
            } else {
                if (current != previous - 1) return false
            }
        }
        return true
    }

    private fun isRepeatingPattern(digits: String, patternLength: Int): Boolean {
        if (digits.length % patternLength != 0) return false
        val pattern = digits.substring(0, patternLength)
        for (i in patternLength until digits.length step patternLength) {
            if (digits.substring(i, i + patternLength) != pattern) return false
        }
        return true
    }

    override fun check(value: String): Boolean {
        // Extract NSN number from the match (may include keywords)
        val numberPattern = Regex("""(?:\d{13}|\d{4}-\d{2}-\d{3}-\d{4})""")
        val match = numberPattern.find(value)
        if (match == null) return false
        
        // Extract only digits (remove hyphens and other characters)
        val digits = match.value.filter { it.isDigit() }
        
        // Must be exactly 13 digits
        if (digits.length != 13) return false
        
        // All characters must be digits
        if (!digits.all { it.isDigit() }) return false
        
        // Filter out obvious fake patterns
        if (digits.all { it == '0' }) return false
        if (digits.all { it == digits[0] }) return false
        
        // Filter sequential numbers (ascending or descending)
        if (isSequential(digits, true) || isSequential(digits, false)) return false
        
        // Filter repeating patterns (e.g., 1212121212121, 1231231231231)
        if (isRepeatingPattern(digits, 2) || isRepeatingPattern(digits, 3) || isRepeatingPattern(digits, 4)) return false
        
        // Filter palindrome patterns
        if (digits == digits.reversed()) return false
        
        // Filter alternating patterns (e.g., 1010101010101, 0101010101010)
        if (digits.length >= 5) {
            val first = digits[0]
            val second = digits[1]
            var alternating = true
            for (i in 2 until digits.length) {
                if (i % 2 == 0) {
                    if (digits[i] != first) {
                        alternating = false
                        break
                    }
                } else {
                    if (digits[i] != second) {
                        alternating = false
                        break
                    }
                }
            }
            if (alternating && first != second) return false
        }
        
        // Filter numbers with too few unique digits (e.g., 1111222233334, 2222333344445)
        val uniqueDigits = digits.toSet()
        if (uniqueDigits.size <= 2) {
            val digitValues = uniqueDigits.map { it.digitToInt() }
            val minDigit = digitValues.minOrNull() ?: 0
            val maxDigit = digitValues.maxOrNull() ?: 9
            if (maxDigit - minDigit <= 2) return false
        }
        
        return true
    }

    override fun toString() = name
}