package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object CadastralNumber : IHyperMatcher, IKotlinMatcher {
    override val name = "Cadastral Number"
    override val javaPatterns = listOf(
        """\b\d{2}\s?:\s?\d{2}\s?:\s?\d{6,7}\s?:\s?\d{1,5}\b"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\b\d{2}\s?:\s?\d{2}\s?:\s?\d{6,7}\s?:\s?\d{1,5}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
