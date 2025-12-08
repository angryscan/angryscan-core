package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian birth/marriage certificates.
 * Matches certificate numbers in format: IVX-АБ 123456
 * Where IVX is Roman numeral series, АБ are Cyrillic letters, and 123456 is the number.
 * May be preceded by keywords like "свидетельство о рождении", "свидетельство о браке".
 */
@Serializable
object Certificate : IHyperMatcher, IKotlinMatcher {
    override val name = "Certificate"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:свидетельство\s+о\s+(?:рождении|браке|заключении\s+брака))?\s*[:\-]?\s*
        ([IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2})
        [\s,;:№Nn]*
        (\d{6})
        (?![\p{L}\d])
        """.trimIndent(),
        """
        (?ix)
        (?<![\p{L}\d])
        ([IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2}[\s,;:№Nn]*\d{6})
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^А-ЯЁA-Za-z0-9IVX])(?:свидетельство\s+о\s+(?:рождении|браке|заключении\s+брака))?\s*[:\-]?\s*[IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2}[\s,;:№Nn]*\d{6}(?:[^А-ЯЁA-Za-z0-9]|$)""",
        """(?:^|[^a-zA-Z0-9А-ЯЁа-яё])[IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2}[\s,;:№Nn]*\d{6}(?:[^0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}

