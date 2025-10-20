package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object DeathDate : IHyperMatcher, IKotlinMatcher {
    override val name = "Death Date"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<!\p{L})
        (?:дата\s+смерти|умер(?:\s*\(ла\))?|умерла|умер|скончал(?:ся|ась)|дата\s+кончины)
        \s*[:\-]?\s*
        (?:
          (?:31 [ .\-/] (?:0?[13578]|1[02]) [ .\-/] (?:19|20)\d{2})
        | (?:(?:29|30) [ .\-/] (?:0?[13-9]|1[0-2]) [ .\-/] (?:19|20)\d{2})
        | (?:29 [ .\-/] 0?2 [ .\-/] (?:19|20)(?:0[48]|[2468][048]|[13579][26]))
        | (?:(?:0?[1-9]|1\d|2[0-8]) [ .\-/] (?:0?[1-9]|1[0-2]) [ .\-/] (?:19|20)\d{2})
        |
          (?:
            (0?[1-9]|[12]\d|3[01]) \s+
            (?:
              янв(?:арь|аря)?|фев(?:раль|раля)?|мар(?:т|та)?|апр(?:ель|еля)?|
              май|мая|июн(?:ь|я)?|июл(?:ь|я)?|авг(?:уст|уста)?|
              сен(?:тябрь|тября)?|сент(?:\.|)|
              окт(?:ябрь|ября)?|ноя(?:брь|бря)?|дек(?:ябрь|бря)?
            )
            \s+ ((?:19|20)\d{2}|\d{2})
          )
        )
        \s*(?:г\.?)?
        (?!\p{L})
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """дата\s+смерти""",
        """умер(?:ла)?""",
        """скончал(?:ся|ась)""",
        """дата\s+кончины"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
