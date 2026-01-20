package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian driver's license numbers.
 * Matches license numbers in format: XX XX XXXXXX (2 digits, space, 2 digits, space, 6 digits)
 * May be preceded by keywords like "водительское удостоверение", "номер ВУ", "driver license".
 */
@Serializable
object DriverLicense : IHyperMatcher, IKotlinMatcher {
    override val name = "Driver License"
    
    private val keywordsPattern = """
        (?:
          водительское\s+удостоверение|
          номер\s+водительского\s+удостоверения|
          номер\s+удостоверения|
          номер\s+ВУ|
          ВУ\s+№|
          driver\s+license|
          license\s+number|
          driving\s+license
        )
    """.trimIndent()
    
    private val numberPattern = """(\d{2}\s\d{2}\s\d{6})"""
    
    override val javaPatterns = listOf(
        """
        (?ix)
        (?:(?<=^)|(?<=[\s\(\[\{«"'])|(?<![\p{L}\d]))
        $keywordsPattern
        \s*[:\-]?\s*
        $numberPattern
        (?![\p{L}\d])
        """.trimIndent()
    )
    
    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        // Always require keywords for driver license to avoid false positives
        val keywordsPart = """(?:водительское\s+удостоверение|номер\s+водительского\s+удостоверения|номер\s+удостоверения|номер\s+ВУ|ВУ\s+№|driver\s+license|license\s+number|driving\s+license)"""
        return listOf(
            """
            (?ix)
            (?:(?<=^)|(?<=[\s\(\[\{«"'])|(?<![\p{L}\d]))
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
        """(?:^|[\s\(\[\{«"'])(?:водительское\s+удостоверение|номер\s+водительского\s+удостоверения|номер\s+удостоверения|номер\s+ВУ|ВУ\s+№|driver\s+license|license\s+number|driving\s+license)\s*[:\-]?\s*\d{2}\s\d{2}\s\d{6}(?:[^0-9a-zA-ZА-ЯЁа-яё]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        // Always require keywords for driver license to avoid false positives
        val keywordsPart = """(?:водительское\s+удостоверение|номер\s+водительского\s+удостоверения|номер\s+удостоверения|номер\s+ВУ|ВУ\s+№|driver\s+license|license\s+number|driving\s+license)"""
        return listOf(
            """(?:^|[\s\(\[\{«"'])$keywordsPart\s*[:\-]?\s*\d{2}\s\d{2}\s\d{6}(?:[^0-9a-zA-ZА-ЯЁа-яё]|$)"""
        )
    }
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}