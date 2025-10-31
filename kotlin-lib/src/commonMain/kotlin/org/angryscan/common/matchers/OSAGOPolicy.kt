package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object OSAGOPolicy : IHyperMatcher, IKotlinMatcher {
    override val name = "OSAGO Policy"
    override val javaPatterns = listOf(
        """(?<![A-ZА-Яa-zа-я])[A-ZА-Я]{3}\s?№?\s?\d{10}(?!\d)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^A-ZА-Яa-zа-я0-9])[A-ZА-Я]{3}\s?№?\s?\d{10}(?:$|[^0-9])"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
