package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for user signatures.
 * Matches custom signature patterns provided in searchSignatures list.
 * Used for detecting specific user signatures or text patterns.
 */
@Serializable
@Suppress("Unused")
class UserSignature(
    override val name: String,
    @Suppress("Unused")
    val searchSignatures: MutableList<String>
) : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = searchSignatures.map {
        Regex.escape(it)
    }
    override val hyperPatterns = searchSignatures.map {
        Regex.escape(it)
    }
    override val expressionOptions = setOf(
        ExpressionOption.UTF8,
        ExpressionOption.CASELESS
    )

    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE
    )
    override fun check(value: String) = true

    override fun toString() = name
}