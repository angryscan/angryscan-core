package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian passport numbers.
 * Matches passport numbers in format: XX XX XXXXXX (2 digits, space, 2 digits, space, 6 digits)
 * May be preceded by keyword "паспорт" or "серия" (series).
 * Format: series (4 digits) and number (6 digits).
 */
@Serializable
object Passport : IHyperMatcher, IKotlinMatcher {
    override val name = "Passport"
    
    private val passportKeyword = """паспорт"""
    private val seriesKeyword = """[сc]ерия"""
    private val numberPattern = """[0-9]{2}[ \t-]?[0-9]{2}[ \t-]?[0-9]{6}"""
    
    override val javaPatterns = listOf(
        """(?<![\p{L}\d])($passportKeyword[ \t-]?([а-яА-Я]*[ \t-]){0,2}$numberPattern)(?![\p{L}\d])""",
        """(?<![\p{L}\d])$seriesKeyword[ \t-]+[0-9]{2}[ \t-]+[0-9]{2}[ \t,]+номер[ \t-]+[0-9]{6}(?![\p{L}\d])""",
        """(?<![\p{L}\d])$seriesKeyword[ \t-]+[0-9]{2}[ \t-]+[0-9]{2}[ \t,]*[0-9]{6}(?![\p{L}\d])"""
    )
    
    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        val passportPart = if (requireKeywords) passportKeyword else "$passportKeyword?"
        val seriesPart = if (requireKeywords) seriesKeyword else "$seriesKeyword?"
        return listOf(
            """(?<![\p{L}\d])($passportPart[ \t-]?([а-яА-Я]*[ \t-]){0,2}$numberPattern)(?![\p{L}\d])""",
            """(?<![\p{L}\d])$seriesPart[ \t-]+[0-9]{2}[ \t-]+[0-9]{2}[ \t,]+номер[ \t-]+[0-9]{6}(?![\p{L}\d])""",
            """(?<![\p{L}\d])$seriesPart[ \t-]+[0-9]{2}[ \t-]+[0-9]{2}[ \t,]*[0-9]{6}(?![\p{L}\d])"""
        )
    }
    
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^а-яА-Яa-zA-Z0-9])($passportKeyword[ \t-]?([а-яА-Я]*[ \t-]){0,2}[0-9]{2}[ \t-]?[0-9]{2}[ \t-]?[0-9]{6})(?:[^а-яА-Яa-zA-Z0-9]|$)""",
        """(?:^|[^а-яА-Яa-zA-Z0-9])[cс]ерия[ \t-]+[0-9]{2}[ \t-]+[0-9]{2}[ \t,]+номер[ \t-]+[0-9]{6}(?:[^а-яА-Яa-zA-Z0-9]|$)""",
        """(?:^|[^а-яА-Яa-zA-Z0-9])[cс]ерия[ \t-]+[0-9]{2}[ \t-]+[0-9]{2}[ \t,]*[0-9]{6}(?:[^а-яА-Яa-zA-Z0-9]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        val passportPart = if (requireKeywords) passportKeyword else "$passportKeyword?"
        val seriesPart = if (requireKeywords) """[cс]ерия""" else """[cс]ерия?"""
        return listOf(
            """(?:^|[^а-яА-Яa-zA-Z0-9])($passportPart[ \t-]?([а-яА-Я]*[ \t-]){0,2}[0-9]{2}[ \t-]?[0-9]{2}[ \t-]?[0-9]{6})(?:[^а-яА-Яa-zA-Z0-9]|$)""",
            """(?:^|[^а-яА-Яa-zA-Z0-9])$seriesPart[ \t-]+[0-9]{2}[ \t-]+[0-9]{2}[ \t,]+номер[ \t-]+[0-9]{6}(?:[^а-яА-Яa-zA-Z0-9]|$)""",
            """(?:^|[^а-яА-Яa-zA-Z0-9])$seriesPart[ \t-]+[0-9]{2}[ \t-]+[0-9]{2}[ \t,]*[0-9]{6}(?:[^а-яА-Яa-zA-Z0-9]|$)"""
        )
    }
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Extract passport series and number
        val numberPattern = Regex("""([0-9]{2})[ \t-]?([0-9]{2})[ \t-]?([0-9]{6})""")
        val match = numberPattern.find(value) ?: return false
        
        val series1 = match.groupValues[1]
        val series2 = match.groupValues[2]
        val number = match.groupValues[3]
        
        // Validate series: 99 99 is invalid
        if (series1 == "99" && series2 == "99") return false
        
        // Validate number length
        if (number.length != 6) return false
        
        return true
    }

    override fun toString() = name
}
