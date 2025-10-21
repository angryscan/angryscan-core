package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object ForeignPassports : IHyperMatcher, IKotlinMatcher {
    override val name = "Foreign Passport"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:
          [иИ]ностранный\s+[пП]аспорт|
          [fF]oreign\s+[pP]assport|
          [pP]assport\s+(EU|US|China|Japan|Israel|Iran|UAE|Qatar)
        )?
        \s*[:\-]?\s*
        (?:
          ([A-Z]\d{8})|
          ([EG]\d{8})|
          ([A-Z]{2}\d{7})|
          ([A-Z]{1,2}[-]?\d{7,8})
        )
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n])(?:[A-Z]\d{8}|[EG]\d{8}|[A-Z]{2}\d{7}|[A-Z]{1,2}[-]?\d{7,8})\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
