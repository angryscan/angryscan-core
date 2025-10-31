package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object Email : IHyperMatcher, IKotlinMatcher {
    override val name = "Email"
    override val javaPatterns = listOf(
        """(?<=[-, ()=*>"]|^)[a-zA-Z0-9_.+-]+@[a-z0-9-.]+?(\.[a-z]{2,})+(?=\W|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:[-, ()=*>"]|^)[a-zA-Z0-9][a-zA-Z0-9._%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]+\.[a-zA-Z]{2,}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
