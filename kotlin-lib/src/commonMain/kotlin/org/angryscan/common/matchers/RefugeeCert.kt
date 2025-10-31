package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object RefugeeCert : IHyperMatcher, IKotlinMatcher {
    override val name = "Refugee Certificate"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:
          свидетельство\s+о\s+рассмотрении\s+ходатайства\s+о\s+признании\s+(?:лица\s+)?беженцем|
          свидетельство\s+беженца
        )?
        \s*[:\-]?\s*
        (\d{2}\s*(?:№|N)?\s*\d{7})
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\b\d{2}\s*(?:№|N)?\s*\d{7}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
