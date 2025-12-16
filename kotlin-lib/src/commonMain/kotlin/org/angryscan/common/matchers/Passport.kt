package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian passport numbers.
 * Matches passport numbers in format: XX XX XXXXXX (2 digits, space, 2 digits, space, 6 digits)
 * May be preceded by keyword "паспорт" or "серия" (series).
 * Format: series (4 digits) and number (6 digits).
 */
@Serializable
object Passport : IHyperMatcher, IKotlinMatcher {
    override val name = "Passport"
    override val javaPatterns = listOf(
        """(?<![\p{L}\d])(паспорт[ \t-]?([а-яА-Я]*[ \t-]){0,2}[0-9]{2}[ \t-]?[0-9]{2}[ \t-]?[0-9]{6})(?!\d)""",
        """(?<![\p{L}\d])[сc]ерия[ \t-]?[0-9]{2}[ \t-]?[0-9]{2}[ \t,]*(номер)?[ \t-]?[0-9]{6}(?!\d)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^а-яА-Яa-zA-Z0-9])(паспорт[ \t-]?([а-яА-Я]*[ \t-]){0,2}[0-9]{2}[ \t-]?[0-9]{2}[ \t-]?[0-9]{6})(?:[^0-9]|$)""",
        """(?:^|[^а-яА-Яa-zA-Z0-9])[cс]ерия[ \t-]?[0-9]{2}[ \t-]?[0-9]{2}[ \t,]*(номер)?[ \t-]?[0-9]{6}(?:[^0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
