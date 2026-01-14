package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian OKPO (Общероссийский классификатор предприятий и организаций).
 * Matches 8 or 10-digit classification codes.
 * Validates checksum using weighted sum algorithm.
 * Filters out fake patterns (sequential, repeating, all same digits, years).
 * May be preceded by keywords like "ОКПО", "код ОКПО", "номер ОКПО".
 */
@Serializable
object OKPO : IHyperMatcher, IKotlinMatcher {
    override val name = "OKPO"
    
    private val keywordsPattern = """
        (?:
          ОКПО|
          код\s+ОКПО|
          номер\s+ОКПО|
          общероссийский\s+классификатор\s+предприятий\s+и\s+организаций|
          классификатор\s+предприятий\s+и\s+организаций|
          серия\s+и\s+номер\s+ОКПО
        )
    """.trimIndent()
    
    private val numberPattern = """(\d{8}|\d{10})"""
    
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        $keywordsPattern
        \s*[:\-]?\s*
        $numberPattern
        (?![\p{L}\d])
        """.trimIndent()
    )
    
    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        val keywordsPart = if (requireKeywords) {
            """(?:ОКПО|код\s+ОКПО|номер\s+ОКПО|общероссийский\s+классификатор\s+предприятий\s+и\s+организаций|классификатор\s+предприятий\s+и\s+организаций|серия\s+и\s+номер\s+ОКПО)"""
        } else {
            """(?:ОКПО|код\s+ОКПО|номер\s+ОКПО|общероссийский\s+классификатор\s+предприятий\s+и\s+организаций|классификатор\s+предприятий\s+и\s+организаций|серия\s+и\s+номер\s+ОКПО)?"""
        }
        return listOf(
            """
            (?ix)
            (?<![\p{L}\d])
            $keywordsPart
            \s*[:\-]?\s*
            $numberPattern
            (?![\p{L}\d])
            """.trimIndent()
        )
    }
    
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^\w])(?:ОКПО|код\s+ОКПО|номер\s+ОКПО|общероссийский\s+классификатор\s+предприятий\s+и\s+организаций|классификатор\s+предприятий\s+и\s+организаций|серия\s+и\s+номер\s+ОКПО)\s*[:\-]?\s*\d{8}(?:[^\w]|$)""",
        """(?:^|[^\w])(?:ОКПО|код\s+ОКПО|номер\s+ОКПО|общероссийский\s+классификатор\s+предприятий\s+и\s+организаций|классификатор\s+предприятий\s+и\s+организаций|серия\s+и\s+номер\s+ОКПО)\s*[:\-]?\s*\d{10}(?:[^\w]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        val keywordsPart = if (requireKeywords) {
            """(?:ОКПО|код\s+ОКПО|номер\s+ОКПО|общероссийский\s+классификатор\s+предприятий\s+и\s+организаций|классификатор\s+предприятий\s+и\s+организаций|серия\s+и\s+номер\s+ОКПО)"""
        } else {
            """(?:ОКПО|код\s+ОКПО|номер\s+ОКПО|общероссийский\s+классификатор\s+предприятий\s+и\s+организаций|классификатор\s+предприятий\s+и\s+организаций|серия\s+и\s+номер\s+ОКПО)?"""
        }
        return listOf(
            """(?:^|[^\w])$keywordsPart\s*[:\-]?\s*\d{8}(?:[^\w]|$)""",
            """(?:^|[^\w])$keywordsPart\s*[:\-]?\s*\d{10}(?:[^\w]|$)"""
        )
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
        // Extract OKPO number from the value (which may contain keywords)
        val numberPattern = Regex("""(\d{8}|\d{10})""")
        val match = numberPattern.find(value) ?: return false
        val okpoClean = match.value
        val length = okpoClean.length
        if (length != 8 && length != 10) return false
        
        val zeroCount = okpoClean.count { it == '0' }
        if (zeroCount > okpoClean.length / 2) return false
        
        if (okpoClean.all { it == okpoClean.first() }) return false
        
        if (okpoClean.all { it == '0' || it == '1' }) return false

        if (isSequential(okpoClean, true) || isSequential(okpoClean, false)) return false

        if (length == 8) {
            if (isRepeatingPattern(okpoClean, 2) || isRepeatingPattern(okpoClean, 4)) return false
            val chunks = listOf(okpoClean.substring(0, 2), okpoClean.substring(2, 4), okpoClean.substring(4, 6), okpoClean.substring(6, 8))
            if (chunks.any { it.all { char -> char == it[0] } }) return false
        } else {
            if (isRepeatingPattern(okpoClean, 2) || isRepeatingPattern(okpoClean, 5)) return false
            val chunks = listOf(okpoClean.substring(0, 2), okpoClean.substring(2, 4), okpoClean.substring(4, 6), okpoClean.substring(6, 8), okpoClean.substring(8, 10))
            if (chunks.any { it.all { char -> char == it[0] } }) return false
        }

        if (okpoClean == okpoClean.reversed()) return false

        val digitCounts = okpoClean.groupingBy { it }.eachCount()
        if (length == 8 && digitCounts.values.any { it > 6 }) return false
        if (length == 10 && digitCounts.values.any { it > 7 }) return false
        
        if (length == 8 && okpoClean == "12345678") return false
        if (length == 10 && okpoClean == "1234567890") return false
        if (length == 10 && okpoClean == "0123456789") return false
        
        val lastFourDigits = if (length == 8) {
            okpoClean.substring(4, 8)
        } else {
            okpoClean.substring(6, 10)
        }
        val year = lastFourDigits.toIntOrNull()
        if (year != null && year in 1900..2099) {
            return false
        }
        
        val digits = okpoClean.map { it.toString().toInt() }
        if (length == 8) {
            val weights1 = intArrayOf(1, 2, 3, 4, 5, 6, 7)
            val sum1 = weights1.indices.sumOf { weights1[it] * digits[it] }
            var check = sum1 % 11
            if (check > 9) {
                val weights2 = intArrayOf(3, 4, 5, 6, 7, 8, 9)
                val sum2 = weights2.indices.sumOf { weights2[it] * digits[it] }
                check = sum2 % 11
            }
            if (check == 10) check = 0
            return check == digits[7]
        } else {
            val weights1 = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
            val sum1 = weights1.indices.sumOf { weights1[it] * digits[it] }
            var check = sum1 % 11
            if (check > 9) {
                val weights2 = intArrayOf(3, 4, 5, 6, 7, 8, 9, 10, 11)
                val sum2 = weights2.indices.sumOf { weights2[it] * digits[it] }
                check = sum2 % 11
            }
            if (check == 10) check = 0
            return check == digits[9]
        }
    }

    override fun toString() = name
}