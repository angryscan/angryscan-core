package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object InheritanceDoc : IHyperMatcher, IKotlinMatcher {
    override val name = "Inheritance Document"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?:^|(?<=\s)|(?<=[\(\[\{«"']))
        \s*
        (\d{2}\s?[А-Я]{2}\s?\d{6,7})
        (?:$|(?=\s)|(?=[\)\]\}»"'\.,;:!?]))
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|\s|[\(\[\{«"'])\s*(?:[\(\[\{«"'])?\d{2}\s?[А-Я]{2}\s?\d{6,7}(?:$|[\s\)\]\}»"'\.,;:!?])"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
