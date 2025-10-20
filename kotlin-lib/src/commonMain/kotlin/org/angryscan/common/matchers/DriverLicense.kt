package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object DriverLicense : IHyperMatcher, IKotlinMatcher {
    override val name = "Driver License"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:водительское\s+удостоверение|ВУ)?
        \s*[:\-]?\s*
        (\d{2}\s?\d{2}\s\d{6})
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\d{2}\s?\d{2}\s\d{6}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
