package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for US National Provider Identifier (NPI).
 * Matches 10-digit numeric identifiers assigned to healthcare providers by CMS.
 * Format: First digit is 1 or 2, followed by 9 digits (first 9 digits are base, last digit is check digit).
 * Total: 10 digits
 * Validates using Luhn (MOD-10) checksum algorithm with prefix 80840:
 * - Prepend "80840" to the first 9 digits to form the payload
 * - Apply standard Luhn algorithm: double every second digit from right, subtract 9 if result > 9
 * - Sum all digits, check digit = (10 - (sum mod 10)) mod 10
 * - Valid if 10th digit equals computed check digit
 * 
 * Additional filters to reduce false positives:
 * - Sequential numbers (ascending/descending)
 * - Repeating patterns
 * - Palindrome patterns
 * - Alternating patterns
 * - Numbers with too few unique digits
 */
@Serializable
object NPI : IHyperMatcher, IKotlinMatcher {
    override val name = "NPI"
    
    override val javaPatterns = listOf(
        """(?<!\d)[12]\d{9}(?!\d)"""
    )
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^0-9a-zA-Z])[12][0-9]{9}(?:[^0-9a-zA-Z]|$)"""
    )
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    /**
     * Validates NPI checksum using Luhn algorithm with prefix 80840.
     * The prefix 80840 is prepended to the first 9 digits for checksum calculation.
     * Check digit is computed and compared with 10th digit.
     */
    private fun validateChecksum(digits: String): Boolean {
        if (digits.length != 10) return false
        
        // First 9 digits (base)
        val base9 = digits.substring(0, 9)
        // 10th digit (check digit)
        val checkDigit = digits[9].digitToInt()
        
        // Prepend prefix 80840 to base9 to form payload
        val payload = "80840$base9"
        
        // Apply Luhn algorithm: double every second digit from right (1-indexed from right)
        // Iterate from rightmost to leftmost
        var sum = 0
        val reversed = payload.reversed()
        
        for (i in reversed.indices) {
            val digit = reversed[i].digitToInt()
            // Position from right: i=0 is rightmost (position 1), i=1 is position 2, etc.
            // Double every second digit: positions 2, 4, 6, 8... (i=1, 3, 5, 7...)
            if (i % 2 == 1) {
                // Double this digit
                var doubled = digit * 2
                if (doubled > 9) {
                    doubled -= 9 // Equivalent to sum of digits
                }
                sum += doubled
            } else {
                sum += digit
            }
        }
        
        // Calculate check digit: (10 - (sum mod 10)) mod 10
        val computedCheckDigit = (10 - (sum % 10)) % 10
        
        return computedCheckDigit == checkDigit
    }

    private fun isSequential(digits: String, ascending: Boolean): Boolean {
        // Check if the 9-digit base (excluding check digit) is sequential
        val base = digits.substring(0, 9)
        for (i in 1 until base.length) {
            val current = base[i].digitToInt()
            val previous = base[i - 1].digitToInt()
            if (ascending) {
                if (current != previous + 1) return false
            } else {
                if (current != previous - 1) return false
            }
        }
        return true
    }

    private fun isRepeatingPattern(digits: String, patternLength: Int): Boolean {
        val base = digits.substring(0, 9) // Check only the base, not check digit
        if (base.length % patternLength != 0) return false
        val pattern = base.substring(0, patternLength)
        for (i in patternLength until base.length step patternLength) {
            if (base.substring(i, i + patternLength) != pattern) return false
        }
        return true
    }

    override fun check(value: String): Boolean {
        // Extract only digits
        val digits = value.filter { it.isDigit() }
        
        // Must be exactly 10 digits
        if (digits.length != 10) return false
        
        // First digit must be 1 or 2
        if (digits[0] != '1' && digits[0] != '2') return false
        
        // Filter out obvious fake patterns
        if (digits.all { it == '0' }) return false
        if (digits.all { it == digits[0] }) return false
        
        // Check checksum first (most important validation)
        if (!validateChecksum(digits)) return false
        
        // Additional filters to reduce false positives
        // Filter sequential numbers in base (ascending or descending)
        if (isSequential(digits, true) || isSequential(digits, false)) return false
        
        // Filter repeating patterns (e.g., 121212121, 123123123)
        if (isRepeatingPattern(digits, 2) || isRepeatingPattern(digits, 3)) return false
        
        // Filter palindrome patterns (check base 9 digits)
        val base9 = digits.substring(0, 9)
        if (base9 == base9.reversed()) return false
        
        // Filter alternating patterns (e.g., 101010101, 010101010)
        if (base9.length >= 3) {
            val first = base9[0]
            val second = base9[1]
            if (first != second) {
                var alternating = true
                for (i in 2 until base9.length) {
                    if (i % 2 == 0) {
                        if (base9[i] != first) {
                            alternating = false
                            break
                        }
                    } else {
                        if (base9[i] != second) {
                            alternating = false
                            break
                        }
                    }
                }
                if (alternating) return false
            }
        }
        
        // Filter numbers with too few unique digits (e.g., 1111222233)
        val uniqueDigits = base9.toSet()
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