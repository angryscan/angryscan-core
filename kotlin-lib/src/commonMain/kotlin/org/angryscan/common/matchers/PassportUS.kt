package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object PassportUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Passport US"
    override val javaPatterns = listOf(
        """(?i)(passport|pass|pass\.|pass\s*no|pass\s*number|passport\s*#|passport\s*no|passport\s*number)[\s\-:]*([A-Z][0-9]{8}|[0-9]{9})(?=\W|$)""",
        """(?<=[-, ()=*>"]|^)([A-Z][0-9]{8}|[0-9]{9})(?=\W|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?i)(passport|pass|pass\.|pass\s*no|pass\s*number|passport\s*#|passport\s*no|passport\s*number)[\s\-:]*([A-Z][0-9]{8}|[0-9]{9})\b""",
        """(?:[-, ()=*>"]|^)([A-Z][0-9]{8}|[0-9]{9})\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}

