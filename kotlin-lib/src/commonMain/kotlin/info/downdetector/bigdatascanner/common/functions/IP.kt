package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.engine.IHyperMatcher
import info.downdetector.bigdatascanner.common.engine.ExpressionOption
import info.downdetector.bigdatascanner.common.engine.IKotlinMatcher

object IP : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """(^|\s)((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)\.){3}(25[0-5]|(2[0-4]|1\d|[1-9]|)\d)($|\s)"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(^|\s)((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)\.){3}(25[0-5]|(2[0-4]|1\d|[1-9]|)\d)($|\s)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
    )

    override fun check(value: String) = true
}