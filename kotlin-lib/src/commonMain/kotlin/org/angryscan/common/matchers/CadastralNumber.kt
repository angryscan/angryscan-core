package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object CadastralNumber : IHyperMatcher, IKotlinMatcher {
    override val name = "Cadastral Number"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:
          кадастровый\s+номер|
          кадастровый\s+номер\s+объекта\s+недвижимости|
          КН|
          кадастровый\s+номер\s+земельного\s+участка|
          кадастровый\s+номер\s+квартиры
        )?
        \s*[:\-]?\s*
        (\d{2}\s?:\s?\d{2}\s?:\s?\d{6,7}\s?:\s?\d{1,5})
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?<![\p{L}\d\p{S}\p{P}])(\d{2}\s?:\s?\d{2}\s?:\s?\d{6,7}\s?:\s?\d{1,5})(?![\p{L}\d\p{S}\p{P}])"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
