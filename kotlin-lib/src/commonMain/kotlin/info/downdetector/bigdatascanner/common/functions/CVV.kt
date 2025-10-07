package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.engine.IHyperMatcher
import info.downdetector.bigdatascanner.common.engine.ExpressionOption
import info.downdetector.bigdatascanner.common.engine.IKotlinMatcher

object CVV : IHyperMatcher, IKotlinMatcher {
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

