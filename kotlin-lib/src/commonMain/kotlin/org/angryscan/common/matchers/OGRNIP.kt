package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object OGRNIP : IHyperMatcher, IKotlinMatcher {
    override val name = "OGRNIP"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:
          ОГРНИП|
          регистрационный\s+номер\s+в\s+реестре\s+ФЛ\s+ЧП|
          регистрационный\s+номер\s+индивидуального\s+предпринимателя|
          государственный\s+регистрационный\s+номер\s+ИП
        )?
        \s*[:\-]?\s*
        ([34]\d{14})
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^\w])[34]\d{14}(?:[^\w]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
