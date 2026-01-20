package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for USCIS Alien Registration Number (A-Number).
 * Matches unique identifiers assigned by DHS to non-citizens in the US.
 * Format: Letter "A" followed by 7-9 digits, optionally with hyphen or space.
 * Total: 8-11 characters (1 letter + 7-9 digits, optional separator)
 * Examples: A1234567, A-12345678, A 123456789
 * Note: A-Numbers do not use a checksum algorithm, only format validation.
 * When fewer than 9 digits are present, USCIS pads with leading zeros to reach 9 digits.
 * 
 * Additional filters to reduce false positives:
 * - Sequential numbers (ascending/descending)
 * - Repeating patterns
 * - Palindrome patterns
 * - Alternating patterns
 * - Numbers with too few unique digits
 */
@Serializable
object AlienRegistrationNumber : IHyperMatcher, IKotlinMatcher {
    override val name = "Alien Registration Number"
    
    override val javaPatterns = listOf(
        """(?<![\d\p{L}])(?i)A[- ]?\d{7,9}(?![\d\p{L}])"""
    )
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^0-9a-zA-Z])A[- ]?[0-9]{7,9}(?:[^0-9a-zA-Z]|$)"""
    )
    
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
        // Extract only letter A and digits (remove hyphens, spaces and other characters)
        val cleaned = value.filter { it.isLetterOrDigit() }.uppercase()
        
        // Must start with A
        if (cleaned.isEmpty() || cleaned[0] != 'A') return false
        
        // After A there should be only digits
        val digits = cleaned.substring(1)
        if (digits.isEmpty()) return false
        if (!digits.all { it.isDigit() }) return false
        
        // Must be 7-9 digits
        if (digits.length < 7 || digits.length > 9) return false
        
        // Filter out obvious fake patterns
        if (digits.all { it == '0' }) return false
        if (digits.all { it == digits[0] }) return false
        
        // Additional filters to reduce false positives
        // Filter sequential numbers (ascending or descending)
        if (isSequential(digits, true) || isSequential(digits, false)) return false
        
        // Filter repeating patterns (e.g., 1212121, 1231231)
        if (isRepeatingPattern(digits, 2) || isRepeatingPattern(digits, 3)) return false
        
        // Filter palindrome patterns
        if (digits == digits.reversed()) return false
        
        // Filter alternating patterns (e.g., 1010101, 0101010)
        if (digits.length >= 3) {
            val first = digits[0]
            val second = digits[1]
            if (first != second) {
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
                if (alternating) return false
            }
        }
        
        // Filter numbers with too few unique digits (e.g., 1111222, 2222333)
        val uniqueDigits = digits.toSet()
        if (uniqueDigits.size <= 2) {
            val digitValues = uniqueDigits.map { it.digitToInt() }
            val minDigit = digitValues.minOrNull() ?: 0
            val maxDigit = digitValues.maxOrNull() ?: 9
            // If digits are very close (e.g., only 1 and 2, or 8 and 9), filter it
            if (maxDigit - minDigit <= 2) return false
        }
        
        return true
    }

    override fun toString() = name
}