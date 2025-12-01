package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object PhoneUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Phone US"
    override val javaPatterns = listOf(
        """(?<![\p{L}\d])(?:(?:\+?1[ \t\-]?)?(?:\([2-9][0-9]{2}\)[ \t\-][2-9][0-9]{2}[ \t\-][0-9]{4}|[2-9][0-9]{2}[ \t\-][2-9][0-9]{2}[ \t\-][0-9]{4}))(?![0-9])"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^a-zA-Z0-9])(?:(?:\+?1[ \t\-]?)?(?:\([2-9][0-9]{2}\)[ \t\-][2-9][0-9]{2}[ \t\-][0-9]{4}|[2-9][0-9]{2}[ \t\-][2-9][0-9]{2}[ \t\-][0-9]{4}))(?:[^0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
