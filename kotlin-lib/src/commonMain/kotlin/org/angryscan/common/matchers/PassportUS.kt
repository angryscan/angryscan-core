package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object PassportUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Passport US"
    override val javaPatterns = listOf(
        """(?i)(?<![\p{L}\d])(passport|pass(?:\.|\s*(?:no|number))?)\b[\s:=#"'()\[\]\{\}\-]*([A-Z][0-9]{8})(?!\d)""",
        """(?<![\p{L}\d])([A-Z][0-9]{8})(?!\d)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?i)(?:^|[^a-zA-Z0-9])(passport|pass(?:\.|\s*(?:no|number))?)\b[\s:=#"'\(\)\[\]\{\}\-]*([A-Z][0-9]{8})(?:[^0-9]|$)""",
        """(?i)(?:^|[^a-zA-Z0-9])([A-Z][0-9]{8})(?:[^0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val cleaned = value.replace(Regex("[^A-Z0-9]"), "").uppercase()

        if (cleaned.length != 9) return false

        val isLetterPlusDigits = cleaned[0].isLetter() && cleaned.substring(1).all { it.isDigit() }

        if (!isLetterPlusDigits) return false

        if (cleaned.all { it == cleaned[0] }) return false
        if (cleaned == "A00000000" || cleaned == "C00000000" || cleaned == "A99999999" || cleaned == "C99999999") return false

        val letter = cleaned[0]
        if (letter !in setOf('A', 'C')) return false
        if (letter == 'I' || letter == 'O') return false

        return true
    }

    override fun toString() = name
}