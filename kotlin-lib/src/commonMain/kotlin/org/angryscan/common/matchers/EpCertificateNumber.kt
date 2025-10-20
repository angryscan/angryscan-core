package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object EpCertificateNumber : IHyperMatcher, IKotlinMatcher {
    override val name = "EP Certificate Number"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:
          серийный\s+номер\s+сертификата\s+ЭП|
          номер\s+сертификата\s+электронной\s+подписи|
          serial\s+number\s+ЭП|
          номер\s+сертификата\s+ключа\s+проверки\s+ЭП|
          уникальный\s+номер\s+сертификата
        )?
        \s*[:\-]?\s*
        (([0-9A-Fa-f]{2}\s?){15}[0-9A-Fa-f]{2}([0-9A-Fa-f]{2}\s?){0,4})
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """([0-9A-Fa-f]{2}\s?){15}[0-9A-Fa-f]{2}([0-9A-Fa-f]{2}\s?){0,4}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
