package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object ForeignTIN : IHyperMatcher, IKotlinMatcher {
    override val name = "Foreign TIN"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:
          [иИ]ностранный\s+[нН]алоговый\s+[иИ]дентификационный\s+[нН]омер|
          [fF]oreign\s+TIN|
          TIN\s+(US|China)
        )?
        \s*[:\-]?\s*
        (?:
          (\d{3}[-\s]\d{2}[-\s]\d{4})|
          ([A-Z0-9]{18})
        )
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?<![\p{L}\d\p{S}\p{P}])(?:(\d{3}[-\s]\d{2}[-\s]\d{4})|([A-Z0-9]{18}))(?![\p{L}\d\p{S}\p{P}])"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
