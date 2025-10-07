package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.engine.IHyperMatcher
import info.downdetector.bigdatascanner.common.engine.IKotlinMatcher
import info.downdetector.bigdatascanner.common.engine.ExpressionOption

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