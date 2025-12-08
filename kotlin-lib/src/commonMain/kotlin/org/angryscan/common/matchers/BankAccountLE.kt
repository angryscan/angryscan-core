package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian legal entity bank account numbers.
 * Matches account numbers starting with 407 followed by 17 digits (total 20 digits).
 * Format: 407XXXXXXXXXXXXXXX
 */
@Serializable
object BankAccountLE : IHyperMatcher, IKotlinMatcher {
    override val name = "Bank Account LE"
    override val javaPatterns = listOf(
        """\b407\d{17}\b"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\b407\d{17}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
