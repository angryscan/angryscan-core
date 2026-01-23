package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Sberbank savings book numbers (сберкнижка).
 * Matches account numbers in format: 423XX XXX X XXXX XXXXXXX
 * Starts with 423 followed by specific digit groups.
 * May be preceded by keywords like "номер сберкнижки", "номер сберегательной книжки ФЛ".
 */
@Serializable
object SberBook : IHyperMatcher, IKotlinMatcher {
    override val name = "Sberbank Book"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:номер\s+сберкнижки|номер\s+сберегательной\s+книжки\s+ФЛ|счет\s+сберкнижки)
        \s*[:\-]?\s*
        (423\d{2}[\s\-]?\d{3}[\s\-]?\d{1}[\s\-]?\d{4}[\s\-]?\d{7})
        (?![\p{L}\d])
        """.trimIndent()
    )
    
    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        val keywordsPart = if (requireKeywords) {
            """(?:номер\s+сберкнижки|номер\s+сберегательной\s+книжки\s+ФЛ|счет\s+сберкнижки)"""
        } else {
            """(?:номер\s+сберкнижки|номер\s+сберегательной\s+книжки\s+ФЛ|счет\s+сберкнижки)?"""
        }
        return listOf(
            """
            (?ix)
            (?<![\p{L}\d])
            $keywordsPart
            \s*[:\-]?\s*
            (423\d{2}[\s\-]?\d{3}[\s\-]?\d{1}[\s\-]?\d{4}[\s\-]?\d{7})
            (?![\p{L}\d])
            """.trimIndent()
        )
    }
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        // Simplified pattern for HyperScan (shorter keywords to avoid "Pattern is too large" error)
        """(?:^|[^\p{L}\d])(?:номер\s+сберкнижки|счет\s+сберкнижки)\s*[:\-]?\s*423\d{2}[\s\-]?\d{3}[\s\-]?\d{1}[\s\-]?\d{4}[\s\-]?\d{7}(?:[^\p{L}\d]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        return if (requireKeywords) {
            // Simplified pattern for HyperScan (shorter keywords to avoid "Pattern is too large" error)
            listOf(
                """(?:^|[^\p{L}\d])(?:номер\s+сберкнижки|счет\s+сберкнижки)\s*[:\-]?\s*423\d{2}[\s\-]?\d{3}[\s\-]?\d{1}[\s\-]?\d{4}[\s\-]?\d{7}(?:[^\p{L}\d]|$)"""
            )
        } else {
            // Return pattern without keywords when requireKeywords = false
            listOf(
                """(?:^|[^\p{L}\d])423\d{2}[\s\-]?\d{3}[\s\-]?\d{1}[\s\-]?\d{4}[\s\-]?\d{7}(?:[^\p{L}\d]|$)"""
            )
        }
    }
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
