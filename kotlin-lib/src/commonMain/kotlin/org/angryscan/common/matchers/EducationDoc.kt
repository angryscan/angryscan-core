package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian education documents (diplomas, certificates, attestations).
 * Matches document numbers in formats:
 * - 123456 1234567 (6 digits space 7 digits)
 * - 12 АБ 1234567 (2 digits space 2 Cyrillic letters space 6-7 digits)
 * May be preceded by keywords like "диплом", "аттестат", "свидетельство об образовании".
 * Validates against common fake patterns (all same digits, sequential numbers, repeating patterns).
 */
@Serializable
object EducationDoc : IHyperMatcher, IKotlinMatcher {
    override val name = "Education Document"
    
    private val keywordsPattern = """
        (?:
          диплом|
          аттестат|
          свидетельство\s+об\s+образовании|
          сертификат|
          удостоверение|
          документ\s+об\s+образовании|
          образовательный\s+документ|
          номер\s+диплома|
          серия\s+и\s+номер\s+диплома|
          номер\s+аттестата|
          серия\s+и\s+номер\s+аттестата|
          номер\s+свидетельства|
          серия\s+и\s+номер\s+свидетельства
        )
    """.trimIndent()
    
    private val numberPattern1 = """(\d{6}\s+\d{7})"""
    private val numberPattern2 = """(\d{2}\s+[А-ЯЁ]{2}\s+\d{6,7})"""
    
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        $keywordsPattern
        \s*[:\-]?\s*
        $numberPattern1
        (?![\p{L}\d])
        """.trimIndent(),
        """
        (?ix)
        (?<![\p{L}\d])
        $keywordsPattern
        \s*[:\-]?\s*
        $numberPattern2
        (?![\p{L}\d])
        """.trimIndent()
    )
    
    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        val keywordsPart = if (requireKeywords) {
            keywordsPattern
        } else {
            """(?:$keywordsPattern)?"""
        }
        return listOf(
            """
            (?ix)
            (?<![\p{L}\d])
            $keywordsPart
            \s*[:\-]?\s*
            $numberPattern1
            (?![\p{L}\d])
            """.trimIndent(),
            """
            (?ix)
            (?<![\p{L}\d])
            $keywordsPart
            \s*[:\-]?\s*
            $numberPattern2
            (?![\p{L}\d])
            """.trimIndent()
        )
    }
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^\w])(?:диплом|аттестат|свидетельство\s+об\s+образовании|сертификат|удостоверение|документ\s+об\s+образовании|образовательный\s+документ|номер\s+диплома|серия\s+и\s+номер\s+диплома|номер\s+аттестата|серия\s+и\s+номер\s+аттестата|номер\s+свидетельства|серия\s+и\s+номер\s+свидетельства)\s*[:\-]?\s*\d{6}\s+\d{7}(?:[^\w]|$)""",
        """(?:^|[^\w])(?:диплом|аттестат|свидетельство\s+об\s+образовании|сертификат|удостоверение|документ\s+об\s+образовании|образовательный\s+документ|номер\s+диплома|серия\s+и\s+номер\s+диплома|номер\s+аттестата|серия\s+и\s+номер\s+аттестата|номер\s+свидетельства|серия\s+и\s+номер\s+свидетельства)\s*[:\-]?\s*\d{2}\s+[А-ЯЁ]{2}\s+\d{6,7}(?:[^\w]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        val keywordsPart = if (requireKeywords) {
            """(?:диплом|аттестат|свидетельство\s+об\s+образовании|сертификат|удостоверение|документ\s+об\s+образовании|образовательный\s+документ|номер\s+диплома|серия\s+и\s+номер\s+диплома|номер\s+аттестата|серия\s+и\s+номер\s+аттестата|номер\s+свидетельства|серия\s+и\s+номер\s+свидетельства)"""
        } else {
            """(?:диплом|аттестат|свидетельство\s+об\s+образовании|сертификат|удостоверение|документ\s+об\s+образовании|образовательный\s+документ|номер\s+диплома|серия\s+и\s+номер\s+диплома|номер\s+аттестата|серия\s+и\s+номер\s+аттестата|номер\s+свидетельства|серия\s+и\s+номер\s+свидетельства)?"""
        }
        return listOf(
            """(?:^|[^\w])$keywordsPart\s*[:\-]?\s*\d{6}\s+\d{7}(?:[^\w]|$)""",
            """(?:^|[^\w])$keywordsPart\s*[:\-]?\s*\d{2}\s+[А-ЯЁ]{2}\s+\d{6,7}(?:[^\w]|$)"""
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
        val digits = value.replace(Regex("[^0-9]"), "")
        
        if (digits.isEmpty()) return false
        
        val zeroCount = digits.count { it == '0' }
        if (zeroCount > digits.length / 2) return false
        
        if (digits.all { it == digits[0] }) return false
        
        if (digits.all { it == '0' || it == '1' }) return false

        if (isSequential(digits, true) || isSequential(digits, false)) return false

        if (isRepeatingPattern(digits, 2) || isRepeatingPattern(digits, 3) || isRepeatingPattern(digits, 5)) return false

        val parts = value.split(Regex("\\s+")).filter { it.isNotBlank() }
        for (part in parts) {
            val partDigits = part.replace(Regex("[^0-9]"), "")
            if (partDigits.length >= 6 && partDigits.all { it == partDigits[0] }) return false
        }

        if (digits == digits.reversed()) return false

        val digitCounts = digits.groupingBy { it }.eachCount()
        if (digitCounts.values.any { it > digits.length * 0.7 }) return false

        return true
    }

    override fun toString() = name
}
