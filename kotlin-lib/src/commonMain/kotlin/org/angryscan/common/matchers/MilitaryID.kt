package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object MilitaryID : IHyperMatcher, IKotlinMatcher {
    override val name = "Military ID"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?:^|[\s\r\n\(\)\"\'\[\]\{\};,.:])
        (?:удостоверение\s+личности\s+военнослужащего\s*[:\-]?\s*)?
        ([А-ЯA-Z]{2}[\s№\-]*\d{7})
        \b
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n\(\)\"\'\[\]\{\};,.:\.!?])[А-ЯA-Z]{2}[\s№\-]*\d{7}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
