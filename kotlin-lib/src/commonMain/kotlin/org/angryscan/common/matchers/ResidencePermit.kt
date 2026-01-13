package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian residence permit numbers (ВНЖ - Вид на жительство).
 * Matches permit numbers in format: 82 № 1234567 or 83 № 1234567
 * Series: 82 or 83, followed by 7-digit number.
 * May be preceded by keywords like "ВНЖ", "вид на жительство", "номер ВНЖ".
 * Filters out invalid patterns (all zeros, all same digits, all zeros/ones).
 */
@Serializable
object ResidencePermit : IHyperMatcher, IKotlinMatcher {
    override val name = "Residence Permit"
    
    private val keywordsPattern = """
        (?:
          ВНЖ|
          вид\s+на\s+жительство|
          номер\s+вида\s+на\s+жительство|
          серия\s+и\s+номер\s+вида\s+на\s+жительство|
          серия\s+и\s+номер\s+ВНЖ|
          номер\s+ВНЖ|
          документ\s+вида\s+на\s+жительство
        )
    """.trimIndent()
    
    private val numberPattern = """((?:82|83)\s*(?:№|N)?\s*\d{7})"""
    
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
        """(?:^|[^a-zA-Z0-9А-ЯЁа-яё])(?:ВНЖ|вид\s+на\s+жительство|номер\s+вида\s+на\s+жительство|серия\s+и\s+номер\s+вида\s+на\s+жительство|серия\s+и\s+номер\s+ВНЖ|номер\s+ВНЖ|документ\s+вида\s+на\s+жительство)\s*[:\-]?\s*(?:82|83)\s*(?:№|N)?\s*\d{7}(?:[^a-zA-Z0-9А-ЯЁа-яё]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        val keywordsPart = if (requireKeywords) {
            """(?:ВНЖ|вид\s+на\s+жительство|номер\s+вида\s+на\s+жительство|серия\s+и\s+номер\s+вида\s+на\s+жительство|серия\s+и\s+номер\s+ВНЖ|номер\s+ВНЖ|документ\s+вида\s+на\s+жительство)"""
        } else {
            """(?:ВНЖ|вид\s+на\s+жительство|номер\s+вида\s+на\s+жительство|серия\s+и\s+номер\s+вида\s+на\s+жительство|серия\s+и\s+номер\s+ВНЖ|номер\s+ВНЖ|документ\s+вида\s+на\s+жительство)?"""
        }
        return listOf(
            """(?:^|[^a-zA-Z0-9А-ЯЁа-яё])$keywordsPart\s*[:\-]?\s*(?:82|83)\s*(?:№|N)?\s*\d{7}(?:[^a-zA-Z0-9А-ЯЁа-яё]|$)"""
        )
    }
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Extract residence permit number from the value (which may contain keywords)
        val numberPattern = Regex("""(?:82|83)\s*(?:№|N)?\s*(\d{7})""")
        val match = numberPattern.find(value) ?: return false
        val numberPart = match.groupValues[1]
        
        // Form the full number: series + number
        val seriesMatch = Regex("""(82|83)""").find(value)
        val series = seriesMatch?.value ?: return false
        
        val cleaned = series + numberPart
        
        if (cleaned.length != 9) return false
        
        if (series != "82" && series != "83") return false
        
        val number = cleaned.substring(2)
        
        if (number == "0000000") return false
        
        if (number.all { it == number[0] }) return false
        
        if (number.all { it in "01" }) return false
        
        return true
    }

    override fun toString() = name
}