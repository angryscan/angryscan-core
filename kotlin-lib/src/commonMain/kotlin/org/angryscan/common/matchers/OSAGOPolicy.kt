package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object OSAGOPolicy : IHyperMatcher, IKotlinMatcher {
    override val name = "OSAGO Policy"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:
          ОСАГО|
          полис\s+ОСАГО|
          полис\s+обязательного\s+страхования\s+автогражданской\s+ответственности|
          номер\s+полиса\s+ОСАГО|
          серия\s+и\s+номер\s+полиса\s+ОСАГО|
          страховой\s+полис\s+ОСАГО|
          страховка\s+ОСАГО
        )
        \s*[:\-]?\s*
        ([A-ZА-Я]{3}\s+№?\s*\d{10})
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^\w])(?:ОСАГО|полис\s+ОСАГО|полис\s+обязательного\s+страхования\s+автогражданской\s+ответственности|номер\s+полиса\s+ОСАГО|серия\s+и\s+номер\s+полиса\s+ОСАГО|страховой\s+полис\s+ОСАГО|страховка\s+ОСАГО)\s*[:\-]?\s*[A-ZА-Я]{3}\s+№?\s*\d{10}(?:[^\w]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Извлекаем только номер полиса ОСАГО из совпадения (может включать ключевые слова)
        val numberPattern = Regex("[A-ZА-Я]{3}\\s+№?\\s*\\d{10}")
        val match = numberPattern.find(value)
        if (match == null) return false
        
        val cleaned = match.value.replace(Regex("[^A-Za-z0-9]"), "").uppercase()
        
        if (cleaned.length != 13) return false
        
        val series = cleaned.substring(0, 3)
        val number = cleaned.substring(3)
        
        if (!series.all { it in 'A'..'Z' }) return false
        
        if (!number.all { it.isDigit() }) return false
        
        if (number.all { it == '0' }) return false
        
        if (number.all { it == number[0] }) return false
        
        return true
    }

    override fun toString() = name
}