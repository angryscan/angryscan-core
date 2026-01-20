package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for U.S. Visa Number.
 * Matches visa numbers found on U.S. visas (visa foil).
 * Format: Either 8 digits, or 1 uppercase letter followed by 7 digits.
 * Total: 8 characters (either 8 digits or 1 letter + 7 digits)
 * Examples: 12345678, A1234567
 * May be preceded by keywords like "visa number", "visa no", "visa #", "visa foil", "u.s. visa".
 * Note: U.S. visa numbers do not use a checksum algorithm.
 * To reduce false positives, filters are applied for:
 * - Sequential numbers (12345678, 87654321)
 * - Repeating patterns (12121212, 12312312)
 * - Date-like patterns (YYYYMMDD, MMDDYYYY)
 * - Palindrome patterns
 * - Alternating patterns (10101010)
 * - Numbers with too few unique digits
 * - Invalid letter prefixes (I, O are excluded)
 * The visa number is typically printed in red ink on the bottom right of newer visas.
 */
@Serializable
object VisaNumberUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Visa Number US"
    
    private val keywordsPattern = """(?:visa\s+number|visa\s+no|visa\s*#|visa\s+foil\s+number|visa\s+foil|u\.?s\.?\s+visa|visa)"""
    
    private val numberPattern8Digits = """\d{8}"""
    private val numberPatternLetter7Digits = """[A-Z]\d{7}"""
    private val numberPattern = """(?:\d{8}|[A-Z]\d{7})"""
    
    override val javaPatterns = listOf(
        // Pattern with keywords (more strict) - similar to PassportUS
        """(?i)(?<![\p{L}\d!@#\$%^&*()_+=\[\]{}|;:'",.<>?/~`\\])(?:visa\s+number|visa\s+no|visa\s*#|visa\s+foil\s+number|visa\s+foil|u\.?s\.?\s+visa|visa)\b[\s:=#"'()\[\]\{\}\-]*(?:[0-9]{8}|[A-Z][0-9]{7})(?![\d\p{L}!@#\$%^&*()_+=\[\]{}|;:'",.<>?/~`\\])""",
        // Pattern without keywords (fallback)
        """(?<![\d\p{L}])(?:\d{8}|[A-Z]\d{7})(?![\d\p{L}])"""
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
        // Pattern with keywords (more strict) - similar to PassportUS
        """(?i)(?:^|[^a-zA-Z0-9!@#\$%^&*()_+=\[\]{}|;:'",.<>?/~`\\])(?:visa\s+number|visa\s+no|visa\s*#|visa\s+foil\s+number|visa\s+foil|u\.?s\.?\s+visa|visa)\b[\s:=#"'\(\)\[\]\{\}\-]*(?:[0-9]{8}|[A-Z][0-9]{7})(?:[^0-9a-zA-Z!@#\$%^&*()_+=\[\]{}|;:'",.<>?/~`\\]|$)""",
        // Pattern without keywords (fallback)
        """(?:^|[^0-9a-zA-Z])(?:[0-9]{8}|[A-Z][0-9]{7})(?:[^0-9a-zA-Z]|$)"""
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

    private fun looksLikeDate(digits: String): Boolean {
        // Check for YYYYMMDD format (valid year 1900-2100, valid month 01-12, valid day 01-31)
        val year = digits.substring(0, 4).toIntOrNull()
        val month = digits.substring(4, 6).toIntOrNull()
        val day = digits.substring(6, 8).toIntOrNull()
        if (year != null && month != null && day != null) {
            if (year in 1900..2100 && month in 1..12 && day in 1..31) {
                // Additional validation for days in months
                val daysInMonth = when (month) {
                    1, 3, 5, 7, 8, 10, 12 -> 31
                    4, 6, 9, 11 -> 30
                    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
                    else -> 0
                }
                if (day <= daysInMonth) return true
            }
        }

        // Check for MMDDYYYY format
        val month2 = digits.substring(0, 2).toIntOrNull()
        val day2 = digits.substring(2, 4).toIntOrNull()
        val year2 = digits.substring(4, 8).toIntOrNull()
        if (month2 != null && day2 != null && year2 != null) {
            if (month2 in 1..12 && day2 in 1..31 && year2 in 1900..2100) {
                val daysInMonth = when (month2) {
                    1, 3, 5, 7, 8, 10, 12 -> 31
                    4, 6, 9, 11 -> 30
                    2 -> if (year2 % 4 == 0 && (year2 % 100 != 0 || year2 % 400 == 0)) 29 else 28
                    else -> 0
                }
                if (day2 <= daysInMonth) return true
            }
        }

        return false
    }

    override fun check(value: String): Boolean {
        // Extract visa number from the match (may include keywords)
        // Try to find the number pattern in the value
        val numberPattern8Digits = Regex("""\d{8}""")
        val numberPatternLetter7Digits = Regex("""[A-Z]\d{7}""", RegexOption.IGNORE_CASE)
        
        val match8Digits = numberPattern8Digits.find(value)
        val matchLetter7Digits = numberPatternLetter7Digits.find(value)
        
        val number = when {
            match8Digits != null -> match8Digits.value.uppercase()
            matchLetter7Digits != null -> matchLetter7Digits.value.uppercase()
            else -> return false
        }
        
        // Extract only letters and digits (remove other characters)
        val cleaned = number.filter { it.isLetterOrDigit() }.uppercase()
        
        // Must be exactly 8 characters
        if (cleaned.length != 8) return false
        
        // Check two formats: 8 digits or letter + 7 digits
        val is8Digits = cleaned.all { it.isDigit() }
        val isLetterPlus7Digits = cleaned[0].isLetter() && cleaned.substring(1).all { it.isDigit() }
        
        if (!is8Digits && !isLetterPlus7Digits) return false
        
        // Extract digits for validation
        val digits = if (is8Digits) cleaned else cleaned.substring(1)
        
        // Filter out obvious fake patterns
        if (digits.all { it == '0' }) return false
        if (digits.all { it == digits[0] }) return false
        
        // Filter sequential numbers (ascending or descending)
        if (isSequential(digits, true) || isSequential(digits, false)) return false
        
        // Filter repeating patterns (e.g., 12121212, 12312312)
        if (isRepeatingPattern(digits, 2) || isRepeatingPattern(digits, 3) || isRepeatingPattern(digits, 4)) return false
        
        // Filter palindrome patterns
        if (digits == digits.reversed()) return false
        
        // Filter date-like patterns (YYYYMMDD or MMDDYYYY)
        if (is8Digits && looksLikeDate(digits)) return false
        
        // Filter alternating patterns (e.g., 10101010, 01010101)
        if (digits.length >= 4) {
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
        
        // Filter numbers with too few unique digits (e.g., 11112222, 22223333)
        val uniqueDigits = digits.toSet()
        if (uniqueDigits.size <= 2) {
            // If only 1-2 unique digits, check if they're too close together
            val digitValues = uniqueDigits.map { it.digitToInt() }
            val minDigit = digitValues.minOrNull() ?: 0
            val maxDigit = digitValues.maxOrNull() ?: 9
            if (maxDigit - minDigit <= 2) return false
        }
        
        // For letter + 7 digits format, check that letter is valid
        // Common prefixes include A, B, C, D, E, F, G, H, J, K, L, M, N, P, R, S, T, U, V, W, X, Y, Z
        // Avoid I and O to prevent confusion with 1 and 0
        if (isLetterPlus7Digits) {
            val letter = cleaned[0]
            if (letter == 'I' || letter == 'O') return false
            if (letter !in 'A'..'Z') return false
        }
        
        return true
    }

    override fun toString() = name
}