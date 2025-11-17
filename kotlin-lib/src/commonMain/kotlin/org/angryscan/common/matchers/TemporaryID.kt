package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object TemporaryID : IHyperMatcher, IKotlinMatcher {
    override val name = "Temporary ID"
    override val javaPatterns = listOf(
        """(?ix)(?:^|[\s"'«»(])(?<![\p{L}\d])(?:временное\s+удостоверение\s+личности|ВУЛ)\s*[:\-]?\s*(\d{12})(?=[\)\.\s"'«»]|$)""",
        """(?ix)(?:^|[\s"'«»(])(?<![\p{L}\d])(\d{12})(?=[\)\.\s"'«»]|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n\(\)\[\]\{\}\"'«»])\d{12}(?:\.\s|[\s\r\n\.\(\)\[\]\{\}\"'«».,;:!?]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val cleaned = value.replace(Regex("[^0-9]"), "")
        
        if (cleaned.length != 12) return false
        
        if (cleaned.all { it == '0' }) return false
        
        if (cleaned.all { it == cleaned[0] }) return false
        
        if (cleaned.all { it in "01" }) return false
        
        if (cleaned == "123456789012" || cleaned == "098765432109") return false
        
        return true
    }

    override fun toString() = name
}
