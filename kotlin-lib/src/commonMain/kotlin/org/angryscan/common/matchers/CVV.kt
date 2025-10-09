package org.angryscan.common.matchers

import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

object CVV : IHyperMatcher, IKotlinMatcher {
    override val name = "CVV"
    override val javaPatterns = listOf(
        """(\.|\s|^)(cvc|cvv|cav|cvc2|cvv2|cav2)(:|\s|:\s)[0-9]{3}(\.|\s|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(\.|\s|^)(cvc|cvv|cav|cvc2|cvv2|cav2)(:|\s|:\s)[0-9]{3}(\.|\s|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.CASELESS,
        ExpressionOption.MULTILINE,
    )

    override fun check(value: String) = true

}

