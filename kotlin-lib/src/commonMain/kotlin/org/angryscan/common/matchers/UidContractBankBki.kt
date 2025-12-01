package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object UidContractBankBki : IHyperMatcher, IKotlinMatcher {
    override val name = "UID Contract Bank BKI"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        \{?[0-9A-Fa-f]{8}[\s\t-]*[0-9A-Fa-f]{4}[\s\t-]*4[0-9A-Fa-f]{3}[\s\t-]*[89ABab][0-9A-Fa-f]{3}[\s\t-]*[0-9A-Fa-f]{12}\}?
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^а-яА-Яa-zA-Z0-9])\{?[0-9A-Fa-f]{8}[\s\t-]*[0-9A-Fa-f]{4}[\s\t-]*4[0-9A-Fa-f]{3}[\s\t-]*[89ABab][0-9A-Fa-f]{3}[\s\t-]*[0-9A-Fa-f]{12}\}?(?:[^а-яА-Яa-zA-Z0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val cleaned = value.replace("-", "").replace(" ", "").replace("{", "").replace("}", "").uppercase()
        if (cleaned.length != 32) return false
        val chars = cleaned.toCharArray()
        if (chars[12] != '4') return false
        val nibble = chars[16]
        if (nibble !in "89AB") return false
        return true
    }

    override fun toString() = name
}
