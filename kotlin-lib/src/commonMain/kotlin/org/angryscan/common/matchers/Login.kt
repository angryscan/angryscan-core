package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for login usernames.
 * Matches usernames preceded by keywords "логин" or "login" followed by colon or space.
 * Format: login: username or логин: username
 * Username must be 3-25 characters (letters, digits, underscore, hyphen).
 */
@Serializable
object Login : IHyperMatcher, IKotlinMatcher {
    override val name = "Login"
    override val javaPatterns = listOf(
        """(логин|login):?\s+[a-z0-9_-]{3,25}(?:\s|$|[<"])"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(логин|login):?\s+[a-z0-9_-]{3,25}(?:\s|$|[<"])"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.CASELESS,
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String) = true

    override fun toString() = name
}
