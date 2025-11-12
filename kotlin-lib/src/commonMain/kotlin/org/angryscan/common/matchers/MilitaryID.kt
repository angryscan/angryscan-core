package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object MilitaryID : IHyperMatcher, IKotlinMatcher {
    override val name = "Military ID"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?:^|[\s\r\n\(\)\"\'\[\]\{\};,.:])
        (?:удостоверение\s+личности\s+военнослужащего\s*[:\-]?\s*)?
        ([А-ЯA-Z]{2}[\s№\-]*\d{7})
        \b
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n\(\)\"\'\[\]\{\};,.:\.!?])[А-ЯA-Z]{2}[\s№\-]*\d{7}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val cleaned = value.replace(Regex("[^А-Яа-я0-9]"), "").uppercase()
        
        if (cleaned.length != 9)
            return false
        
        val letters = cleaned.substring(0, 2)
        if (letters.length != 2 || !letters.all { it in 'А'..'Я' })
            return false
        
        val digits = cleaned.substring(2)
        if (digits.length != 7 || !digits.all { it.isDigit() })
            return false
        
        if (digits == "0000000")
            return false
        
        if (digits.all { it == digits[0] })
            return false
        
        return true
    }

    override fun toString() = name
}
