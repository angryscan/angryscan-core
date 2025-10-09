package org.angryscan.common.matchers

import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

object IP : IHyperMatcher, IKotlinMatcher {
    override val name = "IP"
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