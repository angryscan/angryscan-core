package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object SberBook : IHyperMatcher, IKotlinMatcher {
    override val name = "Sberbank Book"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:номер\s+сберкнижки|номер\s+сберегательной\s+книжки\s+ФЛ|счет\s+сберкнижки)?
        \s*[:\-]?\s*
        (423\d{2}[\s\-]?\d{3}[\s\-]?\d{1}[\s\-]?\d{4}[\s\-]?\d{7})
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\b423\d{2}[\s\-]?\d{3}[\s\-]?\d{1}[\s\-]?\d{4}[\s\-]?\d{7}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
