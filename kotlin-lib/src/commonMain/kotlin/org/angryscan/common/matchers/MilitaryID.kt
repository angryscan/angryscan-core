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
        (?:
          удостоверение\s+личности\s+военнослужащего|
          номер\s+удостоверения\s+личности\s+военнослужащего|
          серия\s+и\s+номер\s+удостоверения\s+личности\s+военнослужащего|
          номер\s+УЛВ|
          УЛВ\s+№|
          военный\s+билет|
          номер\s+военного\s+билета
        )
        \s*[:\-]?\s*
        ([А-ЯA-Z]{2}[\s№\-]*\d{7})
        \b
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n\(\)\"\'\[\]\{\};,.:\.!?])(?:удостоверение\s+личности\s+военнослужащего|номер\s+удостоверения\s+личности\s+военнослужащего|серия\s+и\s+номер\s+удостоверения\s+личности\s+военнослужащего|номер\s+УЛВ|УЛВ\s+№|военный\s+билет|номер\s+военного\s+билета)\s*[:\-]?\s*[А-ЯA-Z]{2}[\s№\-]*\d{7}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    private fun isSequential(digits: String, ascending: Boolean): Boolean {
        for (i in 1 until digits.length) {
            val current = digits[i].digitToInt()
            val previous = digits[i - 1].digitToInt()
            if (ascending) {
                if (current != previous + 1) return false
            } else {
                if (current != previous - 1) return false
            }
        }
        return true
    }

    private fun isRepeatingPattern(digits: String, patternLength: Int): Boolean {
        if (digits.length % patternLength != 0) return false
        val pattern = digits.substring(0, patternLength)
        for (i in patternLength until digits.length step patternLength) {
            if (digits.substring(i, i + patternLength) != pattern) return false
        }
        return true
    }

    override fun check(value: String): Boolean {
        // Извлекаем только номер (2 буквы + 7 цифр) из совпадения
        // Совпадение может включать ключевые слова, поэтому ищем паттерн номера
        val numberPattern = Regex("[А-ЯA-Z]{2}[\\s№\\-]*\\d{7}")
        val match = numberPattern.find(value)
        if (match == null) return false
        
        val cleaned = match.value.replace(Regex("[^А-Яа-я0-9]"), "").uppercase()
        
        if (cleaned.length != 9)
            return false
        
        val letters = cleaned.substring(0, 2)
        if (letters.length != 2 || !letters.all { it in 'А'..'Я' })
            return false
        
        if (letters == "ОТ")
            return false
        
        val digits = cleaned.substring(2)
        if (digits.length != 7 || !digits.all { it.isDigit() })
            return false
        
        val zeroCount = digits.count { it == '0' }
        if (zeroCount > digits.length / 2) return false
        
        if (digits.all { it == digits[0] }) return false
        
        if (digits.all { it == '0' || it == '1' }) return false

        if (isSequential(digits, true) || isSequential(digits, false)) return false

        if (isRepeatingPattern(digits, 2) || isRepeatingPattern(digits, 3)) return false

        val chunks = listOf(digits.substring(0, 3), digits.substring(3, 7))
        if (chunks.any { it.all { char -> char == it[0] } }) return false

        if (digits == digits.reversed()) return false

        val digitCounts = digits.groupingBy { it }.eachCount()
        if (digitCounts.values.any { it > 5 }) return false
        
        return true
    }

    override fun toString() = name
}
