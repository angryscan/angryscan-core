package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object StateRegContract : IHyperMatcher, IKotlinMatcher {
    override val name = "State Registration Contract"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:
          номер\s+государственной\s+регистрации\s+договора|
          номер\s+регистрации\s+договора\s+в\s+Росреестре|
          госрегистрационный\s+номер\s+договора\s+с\s+ФЛ
        )?
        \s*[:\-]?\s*
        (\d{2}\s?[-:]\s?\d{2}\s?[-:]\s?\d{2}/\d{3,4}/\d{4}\s?[-:]\s?\d{1,3})
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?<![\p{L}\d\p{S}\p{P}])(\d{2}\s?[-:]\s?\d{2}\s?[-:]\s?\d{2}/\d{3,4}/\d{4}\s?[-:]\s?\d{1,3})"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
