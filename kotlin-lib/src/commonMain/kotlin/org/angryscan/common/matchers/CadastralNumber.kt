package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian cadastral numbers.
 * Matches cadastral numbers in format: XX:XX:XXXXXX:XXXX or XX:XX:XXXXXXX:XXXXX
 * Used for real estate property identification in Russia.
 */
@Serializable
object CadastralNumber : IHyperMatcher, IKotlinMatcher {
    override val name = "Cadastral Number"
    override val javaPatterns = listOf(
        """(?<![\p{L}\d])\d{2}\s?:\s?\d{2}\s?:\s?\d{6}\s?:\s?\d{1,5}(?![\p{L}\d])""",
        """(?<![\p{L}\d])\d{2}\s?:\s?\d{2}\s?:\s?\d{7}\s?:\s?\d{4,5}(?![\p{L}\d])"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^a-zA-Z0-9А-ЯЁа-яё])\d{2}\s?:\s?\d{2}\s?:\s?\d{6}\s?:\s?\d{1,5}(?:[^0-9]|$)""",
        """(?:^|[^a-zA-Z0-9А-ЯЁа-яё])\d{2}\s?:\s?\d{2}\s?:\s?\d{7}\s?:\s?\d{4,5}(?:[^0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
