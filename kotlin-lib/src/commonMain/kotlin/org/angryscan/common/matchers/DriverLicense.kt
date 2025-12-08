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
        (?:(?<=^)|(?<=[\s\(\[\{«"'])|(?<![\p{L}\d]))
        (?:
          водительское\s+удостоверение|
          номер\s+водительского\s+удостоверения|
          номер\s+удостоверения|
          номер\s+ВУ|
          ВУ\s+№|
          driver\s+license|
          license\s+number|
          driving\s+license
        )
        \s*[:\-]?\s*
        (\d{2}\s\d{2}\s\d{6})
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\(\[\{«"'])(?:водительское\s+удостоверение|номер\s+водительского\s+удостоверения|номер\s+удостоверения|номер\s+ВУ|ВУ\s+№|driver\s+license|license\s+number|driving\s+license)\s*[:\-]?\s*\d{2}\s\d{2}\s\d{6}(?:[^0-9a-zA-ZА-ЯЁа-яё]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}