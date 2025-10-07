package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.engine.IHyperMatcher
import info.downdetector.bigdatascanner.common.engine.ExpressionOption
import info.downdetector.bigdatascanner.common.engine.IKotlinMatcher

object Email : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """(?<=[-, ()=*]|^)[a-zA-Z0-9_.+-]+@[a-z0-9-.]+?(\.[a-z]{2,})+(?=\W|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\b[a-zA-Z0-9][a-zA-Z0-9._%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]+\.[a-zA-Z]{2,}\b"""
    )
    override val expressionOptions: Set<ExpressionOption> = setOf(
        ExpressionOption.MULTILINE
    )

    override fun check(value: String): Boolean = true
}
