package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian executive document numbers (исполнительные документы).
 * Matches document numbers in format: XXXX/XX/XXXXX-XX
 * Where XX is a document type code (ИП, СВ, ФС, УД, АП, СД, МС, ПД, АС, ИД).
 * May be preceded by keywords like "номер исполнительного документа", "исполнительный лист".
 */
@Serializable
object ExecDocNumber : IHyperMatcher, IKotlinMatcher {
    override val name = "Executive Document Number"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:
          номер\s+исполнительного\s+документа|
          номер\s+исполнительного\s+производства|
          исполнительный\s+лист
        )?
        \s*[:\-]?\s*
        (?:№\s?)?
        (\d{4,5}/\d{2}/\d{5}-(?:ИП|СВ|ФС|УД|АП|СД|МС|ПД|АС|ИД))
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\A\d{4,5}/\d{2}/\d{5}-(?:ИП|СВ|ФС|УД|АП|СД|МС|ПД|АС|ИД)""",
        """[^a-zA-Z0-9А-ЯЁа-яё]\d{4,5}/\d{2}/\d{5}-(?:ИП|СВ|ФС|УД|АП|СД|МС|ПД|АС|ИД)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
