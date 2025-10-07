package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.engine.IHyperMatcher
import info.downdetector.bigdatascanner.common.engine.ExpressionOption
import info.downdetector.bigdatascanner.common.engine.IKotlinMatcher

object IPv6 : IHyperMatcher, IKotlinMatcher {
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
}