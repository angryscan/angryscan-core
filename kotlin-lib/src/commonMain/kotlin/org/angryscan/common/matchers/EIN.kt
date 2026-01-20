package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for US Employer Identification Number (EIN).
 * Matches 9-digit tax identification numbers for businesses in the US.
 * Format: XX-XXXXXXX (2 digits, optional hyphen, 7 digits)
 * First two digits must match specific ranges:
 * - 00 or 07: second digit 1-7
 * - 10-16: valid range
 * - 20-27: valid range
 * - 30-39 or 50-59: valid range
 * - 40-48 or 60-68 or 80-88: valid range
 * - 90-95, 98-99: valid range
 * May be preceded by keywords like "ein", "employer identification number", "tax id", "tax id number", "federal tax id".
 * Note: EIN does not use a checksum algorithm.
 * To reduce false positives, filters are applied for:
 * - Sequential numbers (123456789, 987654321)
 * - Repeating patterns (121212121, 123123123)
 * - Palindrome patterns
 * - Alternating patterns (101010101)
 * - Numbers with too few unique digits
 */
@Serializable
object EIN : IHyperMatcher, IKotlinMatcher {
    override val name = "EIN"
    
    private val keywordsPattern = """(?:ein|employer\s+identification\s+number|tax\s+id(?:entification)?(?:\s+number)?|federal\s+tax\s+id(?:entification)?(?:\s+number)?|e\.?i\.?n\.?)"""
    
    private val numberPattern = """(?:[07][1-7]|1[0-6]|2[0-7]|[35][0-9]|[468][0-8]|9[0-589])-?\d{7}"""
    
    override val javaPatterns = listOf(
        // Pattern with keywords (more strict)
        """(?i)(?<![\p{L}\d])(?:ein|employer\s+identification\s+number|tax\s+id(?:entification)?(?:\s+number)?|federal\s+tax\s+id(?:entification)?(?:\s+number)?|e\.?i\.?n\.?)\b[\s:=#"'()\[\]\{\}\-]*(?:[07][1-7]|1[0-6]|2[0-7]|[35][0-9]|[468][0-8]|9[0-589])-?\d{7}(?![\d\w])""",
        // Pattern without keywords (fallback)
        """(?<![\d\w])(?:[07][1-7]|1[0-6]|2[0-7]|[35][0-9]|[468][0-8]|9[0-589])-?\d{7}(?![\d\w])"""
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
        """(?i)(?:^|[^a-zA-Z0-9])(?:ein|employer\s+identification\s+number|tax\s+id(?:entification)?(?:\s+number)?|federal\s+tax\s+id(?:entification)?(?:\s+number)?|e\.?i\.?n\.?)\b[\s:=#"'\(\)\[\]\{\}\-]*(?:[07][1-7]|1[0-6]|2[0-7]|[35][0-9]|[468][0-8]|9[0-589])-?\d{7}(?:[^0-9a-zA-Z]|$)""",
        // Pattern without keywords (fallback)
        """(?:^|[^0-9a-zA-Z])(?:[07][1-7]|1[0-6]|2[0-7]|[35][0-9]|[468][0-8]|9[0-589])-?\d{7}(?:[^0-9a-zA-Z]|$)"""
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

    /**
     * Validates EIN prefix (first two digits of the 9-digit number).
     * The pattern [07][1-7] means: first digit 0 or 7, second digit (of the whole number) 1-7.
     * Returns true if the prefix matches valid EIN patterns.
     */
    private fun isValidPrefix(digits: String): Boolean {
        if (digits.length < 2) return false
        
        val first = digits[0].digitToInt()
        val second = digits[1].digitToInt() // This is the second digit of the entire number, not the prefix
        
        return when {
            // [07][1-7]: first digit 0 or 7, second digit 1-7
            (first == 0 || first == 7) && second in 1..7 -> true
            // 1[0-6]: first digit 1, second digit 0-6
            first == 1 && second in 0..6 -> true
            // 2[0-7]: first digit 2, second digit 0-7
            first == 2 && second in 0..7 -> true
            // [35][0-9]: first digit 3 or 5, second digit 0-9
            (first == 3 || first == 5) && second in 0..9 -> true
            // [468][0-8]: first digit 4, 6, or 8, second digit 0-8
            (first == 4 || first == 6 || first == 8) && second in 0..8 -> true
            // 9[0-589]: first digit 9, second digit 0-5, 8, or 9
            first == 9 && (second in 0..5 || second == 8 || second == 9) -> true
            else -> false
        }
    }

    override fun check(value: String): Boolean {
        // Extract EIN number from the match (may include keywords)
        val numberPattern = Regex("""(?:[07][1-7]|1[0-6]|2[0-7]|[35][0-9]|[468][0-8]|9[0-589])-?\d{7}""")
        val match = numberPattern.find(value)
        if (match == null) return false
        
        // Extract only digits and hyphens from the matched number
        val cleaned = match.value.replace(Regex("[^0-9-]"), "")
        
        // Check format: must be digits, possibly with one hyphen
        val digitsOnly = cleaned.replace("-", "")
        if (digitsOnly.length != 9) return false
        
        // Check that if there is a hyphen, it is only one and after the first two digits
        val hyphenCount = cleaned.count { it == '-' }
        if (hyphenCount > 1) return false
        if (hyphenCount == 1) {
            // Hyphen must be after the first two digits
            if (!cleaned.matches(Regex("""\d{2}-\d{7}"""))) return false
        }
        
        // Check prefix (first two digits of the entire number)
        if (!isValidPrefix(digitsOnly)) return false
        
        // Filter out obvious fake patterns
        if (digitsOnly.all { it == '0' }) return false
        if (digitsOnly.all { it == digitsOnly[0] }) return false
        
        // Filter sequential numbers (ascending or descending)
        // Skip first 2 digits (prefix) when checking for sequential pattern
        val suffixDigits = digitsOnly.substring(2)
        if (isSequential(suffixDigits, true) || isSequential(suffixDigits, false)) return false
        
        // Filter repeating patterns (e.g., 121212121, 123123123)
        if (isRepeatingPattern(suffixDigits, 2) || isRepeatingPattern(suffixDigits, 3)) return false
        
        // Filter palindrome patterns (for the full number)
        if (digitsOnly == digitsOnly.reversed()) return false
        
        // Filter alternating patterns (e.g., 101010101, 010101010)
        if (digitsOnly.length >= 5) {
            val first = digitsOnly[0]
            val second = digitsOnly[1]
            var alternating = true
            for (i in 2 until digitsOnly.length) {
                if (i % 2 == 0) {
                    if (digitsOnly[i] != first) {
                        alternating = false
                        break
                    }
                } else {
                    if (digitsOnly[i] != second) {
                        alternating = false
                        break
                    }
                }
            }
            if (alternating && first != second) return false
        }
        
        // Filter numbers with too few unique digits (e.g., 111122222, 222233333)
        val uniqueDigits = digitsOnly.toSet()
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
