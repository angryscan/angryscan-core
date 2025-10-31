package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object Password : IHyperMatcher, IKotlinMatcher {
    override val name = "Password"
    override val javaPatterns = listOf(
        """(password|пароль):?\s*\S{3,25}(?:$|[\s<"])"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(password|пароль):?\s*\S{3,25}(?:$|[\s<"])"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.CASELESS,
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        return true
    }

    override fun toString() = name
}
