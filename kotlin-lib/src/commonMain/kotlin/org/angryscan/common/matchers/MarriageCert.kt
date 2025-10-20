package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object MarriageCert : IHyperMatcher, IKotlinMatcher {
    override val name = "Marriage Certificate"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:свидетельство\s+о\s+браке|свидетельство\s+о\s+заключении\s+брака)?
        \s*[:\-]?\s*
        ([IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2})
        [\s,;:№Nn]*
        (\d{6})
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """[IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2}[\s,;:№Nn]*\d{6}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
