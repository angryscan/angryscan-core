package org.angryscan.common.functions

import org.angryscan.common.engine.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.IKotlinMatcher

object Login : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """(логин|login):?\s*[a-z0-9_-]{3,25}"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(логин|login):?\s*([a-z0-9_-]{3,25})($|\W)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.CASELESS,
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String) = true

}

