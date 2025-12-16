package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for CVV/CVC card security codes.
 * Matches 3-digit security codes preceded by keywords: cvc, cvv, cav, cvc2, cvv2, cav2
 * Format: keyword: 123 or keyword 123
 */
@Serializable
object CVV : IHyperMatcher, IKotlinMatcher {
    override val name = "CVV"
    override val javaPatterns = listOf(
        """([.\s>"]|^)(cvc|cvv|cav|cvc2|cvv2|cav2)(:|\s|:\s)[0-9]{3}([ \t\r.,;()"'<]|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """([.\s>"]|^)(cvc|cvv|cav|cvc2|cvv2|cav2)(:|\s|:\s)[0-9]{3}([ \t\r.,;()"'<]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.CASELESS,
        ExpressionOption.MULTILINE,
    )

    override fun check(value: String) = true

    override fun toString() = name
}
