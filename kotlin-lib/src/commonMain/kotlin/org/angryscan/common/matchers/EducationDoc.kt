package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object EducationDoc : IHyperMatcher, IKotlinMatcher {
    override val name = "Education Document"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:документ\s+об\s+образовании|диплом|аттестат|приложение\s+к\s+диплому)?
        \s*[:\-]?\s*
        (?:
          (\d{6}\s?\d{7})|
          (\d{2}\s?[А-Я]{2}\s?\d{6,7})|
          ([IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2}\s*\d{6})
        )
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:\d{6}\s?\d{7}|\d{2}\s?[А-Я]{2}\s?\d{6,7}|[IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2}\s*\d{6})\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
