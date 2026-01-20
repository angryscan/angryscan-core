package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for US Routing Transit Number (RTN).
 * Matches 9-digit routing numbers used to identify financial institutions in the US.
 * Format: First 2 digits in valid ranges (00-09, 10-12, 21-29, 30-32, 61-69, 70-72, 80) followed by 7 digits.
 * Total: 9 digits
 * Validates using MOD-10 checksum algorithm:
 * - Weights for positions 1-8: [3, 7, 1, 3, 7, 1, 3, 7]
 * - Sum = 3*d1 + 7*d2 + 1*d3 + 3*d4 + 7*d5 + 1*d6 + 3*d7 + 7*d8
 * - Check digit = (10 - (sum mod 10)) mod 10
 * - Valid if d9 (9th digit) equals computed check digit
 * 
 * Additional filters to reduce false positives:
 * - Sequential numbers in last 7 digits (ascending/descending)
 * - Repeating patterns
 * - Palindrome patterns in last 7 digits
 * - Alternating patterns
 * - Numbers with too few unique digits
 */
@Serializable
object RTN : IHyperMatcher, IKotlinMatcher {
    override val name = "RTN"
    
    override val javaPatterns = listOf(
        """\b((0[0-9])|(1[0-2])|(2[1-9])|(3[0-2])|(6[1-9])|(7[0-2])|80)([0-9]{7})\b"""
    )
    
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^0-9a-zA-Z])((?:0[0-9])|(?:1[0-2])|(?:2[1-9])|(?:3[0-2])|(?:6[1-9])|(?:7[0-2])|80)([0-9]{7})(?:[^0-9a-zA-Z]|$)"""
    )
    
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    private val validFirstDigitPairs = setOf(
        // 00-09
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
        // 10-12
        "10", "11", "12",
        // 21-29
        "21", "22", "23", "24", "25", "26", "27", "28", "29",
        // 30-32
        "30", "31", "32",
        // 61-69
        "61", "62", "63", "64", "65", "66", "67", "68", "69",
        // 70-72
        "70", "71", "72",
        // 80
        "80"
    )

    /**
     * Validates RTN checksum using MOD-10 algorithm.
     * Weights: [3, 7, 1, 3, 7, 1, 3, 7] for positions 1-8
     * Check digit is computed and compared with 9th digit.
     */
    private fun validateChecksum(digits: String): Boolean {
        if (digits.length != 9) return false
        
        val weights = intArrayOf(3, 7, 1, 3, 7, 1, 3, 7)
        var sum = 0
        
        // Calculate weighted sum for first 8 digits
        for (i in 0 until 8) {
            val digit = digits[i].digitToInt()
            sum += weights[i] * digit
        }
        
        // Calculate check digit: (10 - (sum mod 10)) mod 10
        val computedCheckDigit = (10 - (sum % 10)) % 10
        val actualCheckDigit = digits[8].digitToInt()
        
        return computedCheckDigit == actualCheckDigit
    }

    private fun isSequential(digits: String, ascending: Boolean): Boolean {
        // Check if the last 7 digits (excluding check digit) are sequential
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
        // Check the last 7 digits for repeating patterns
        val lastSeven = digits.substring(2) // Last 7 digits (positions 3-9, excluding check digit at position 9)
        val lastSix = lastSeven.substring(0, 6) // Check first 6 of last 7 (excluding check digit)
        if (lastSix.length % patternLength != 0) return false
        val pattern = lastSix.substring(0, patternLength)
        for (i in patternLength until lastSix.length step patternLength) {
            if (lastSix.substring(i, i + patternLength) != pattern) return false
        }
        return true
    }

    override fun check(value: String): Boolean {
        // Extract only digits
        val digits = value.filter { it.isDigit() }
        
        // Must be exactly 9 digits
        if (digits.length != 9) return false
        
        // Check first 2 digits
        val firstTwo = digits.take(2)
        if (firstTwo !in validFirstDigitPairs) return false
        
        // Check that the remaining 7 digits exist (already checked length)
        val lastSeven = digits.substring(2)
        if (lastSeven.length != 7) return false
        
        // Filter out obvious fake patterns
        if (digits.all { it == '0' }) return false
        if (digits.all { it == digits[0] }) return false
        
        // Check checksum first (most important validation)
        if (!validateChecksum(digits)) return false
        
        // Additional filters to reduce false positives
        // Filter sequential numbers in last 7 digits (ascending or descending)
        val lastSevenDigits = digits.substring(2, 8) // First 6 digits of last 7 (excluding check digit)
        if (lastSevenDigits.length >= 3) {
            var isSeqAsc = true
            var isSeqDesc = true
            for (i in 1 until lastSevenDigits.length) {
                val current = lastSevenDigits[i].digitToInt()
                val previous = lastSevenDigits[i - 1].digitToInt()
                if (current != previous + 1) isSeqAsc = false
                if (current != previous - 1) isSeqDesc = false
            }
            if (isSeqAsc || isSeqDesc) return false
        }
        
        // Filter repeating patterns in last 6 digits (e.g., 121212, 123123)
        if (isRepeatingPattern(digits, 2) || isRepeatingPattern(digits, 3)) return false
        
        // Filter palindrome patterns (check last 6 digits, excluding check digit)
        val lastSixForPalindrome = digits.substring(2, 8)
        if (lastSixForPalindrome == lastSixForPalindrome.reversed()) return false
        
        // Filter alternating patterns (e.g., 101010, 010101)
        if (lastSevenDigits.length >= 3) {
            val first = lastSevenDigits[0]
            val second = lastSevenDigits[1]
            if (first != second) {
                var alternating = true
                for (i in 2 until lastSevenDigits.length) {
                    if (i % 2 == 0) {
                        if (lastSevenDigits[i] != first) {
                            alternating = false
                            break
                        }
                    } else {
                        if (lastSevenDigits[i] != second) {
                            alternating = false
                            break
                        }
                    }
                }
                if (alternating) return false
            }
        }
        
        // Filter numbers with too few unique digits in last 7 (excluding check digit)
        val lastSevenWithoutCheck = digits.substring(2, 8) // First 6 of last 7
        val uniqueDigits = lastSevenWithoutCheck.toSet()
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
