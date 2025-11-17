package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object BirthCert : IHyperMatcher, IKotlinMatcher {
    override val name = "Birth Certificate"
    override val javaPatterns = listOf(
        """\b[IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2}[\s,;:№Nn]*\d{6}\b"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\b[IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2}[\s,;:№Nn]*\d{6}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
