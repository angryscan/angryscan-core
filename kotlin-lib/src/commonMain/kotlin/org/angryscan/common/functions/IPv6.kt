package org.angryscan.common.functions

import org.angryscan.common.engine.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.IKotlinMatcher

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