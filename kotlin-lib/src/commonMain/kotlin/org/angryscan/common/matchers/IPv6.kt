package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object IPv6 : IHyperMatcher, IKotlinMatcher {
    override val name = "IPv6"
    override val javaPatterns = listOf(
        """(^|\s)(([0-9a-fA-F]{4}:){7}[0-9a-fA-F]{4})($|\s)"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(^|\s)(([0-9a-fA-F]{4}:){7}[0-9a-fA-F]{4})($|\s)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
    )

    override fun check(value: String) = true

    override fun toString() = name
}