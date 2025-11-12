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
        (?:
          серийный\s+номер\s+сертификата\s+ЭП|
          номер\s+сертификата\s+электронной\s+подписи|
          serial\s+number\s+ЭП|
          номер\s+сертификата\s+ключа\s+проверки\s+ЭП|
          уникальный\s+номер\s+сертификата
        )?
        \s*[:\-]?\s*
        \b((?:[0-9A-Fa-f]{2}\s){15}[0-9A-Fa-f]{2}(?:\s[0-9A-Fa-f]{2}){0,4}|(?:[0-9A-Fa-f]{4}\s){7}[0-9A-Fa-f]{4}(?:\s[0-9A-Fa-f]{4}){0,2})\b
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\b([0-9A-Fa-f]{2}\s){15}[0-9A-Fa-f]{2}(\s[0-9A-Fa-f]{2}){0,4}\b""",
        """\b([0-9A-Fa-f]{4}\s){7}[0-9A-Fa-f]{4}(\s[0-9A-Fa-f]{4}){0,2}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val cleaned = value.replace(Regex("[^0-9A-Fa-f]"), "")
        
        if (cleaned.length < 32 || cleaned.length > 80)
            return false
        
        if (cleaned.all { it == '0' })
            return false
        
        if (cleaned.all { it == cleaned[0] })
            return false
        
        if (cleaned.all { it.isDigit() })
            return false
        
        return true
    }

    override fun toString() = name
}