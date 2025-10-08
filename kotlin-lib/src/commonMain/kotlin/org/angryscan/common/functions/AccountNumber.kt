package org.angryscan.common.functions

import org.angryscan.common.engine.IHyperMatcher
import org.angryscan.common.engine.IKotlinMatcher
import org.angryscan.common.engine.ExpressionOption

object AccountNumber : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """(?<=\D|^)40[0-9]{3}(810|840|978)[0-9]{12}(?=\D|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\b(40[0-9]{3}(810|840|978)[0-9]{12})\b"""
    )
    override val expressionOptions = setOf(ExpressionOption.MULTILINE)

    override fun check(value: String): Boolean = true

}