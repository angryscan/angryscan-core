package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian SNILS (Страховой номер индивидуального лицевого счёта).
 * Matches 11-digit insurance account numbers in format: XXX-XXX-XXX-XX
 * Validates checksum using weighted sum algorithm (positions 1-9 multiplied by weights 9-1, sum MOD 101).
 * Filters out fake patterns (sequential, repeating, all same digits, all zeros/ones).
 * May be preceded by keywords like "СНИЛС", "страховой номер", "номер СНИЛС".
 */
@Serializable
object SNILS : IHyperMatcher, IKotlinMatcher {
    override val name = "SNILS"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:
          СНИЛС|
          страховой\s+номер\s+индивидуального\s+лицевого\s+счёта|
          страховой\s+номер\s+индивидуального\s+лицевого\s+счета|
          страховой\s+номер|
          номер\s+индивидуального\s+лицевого\s+счёта|
          номер\s+индивидуального\s+лицевого\s+счета|
          номер\s+СНИЛС|
          серия\s+и\s+номер\s+СНИЛС
        )
        \s*[:\-/\s]*\s*
        (?:[^0-9]*?\([^0-9]*?|["']*|.*?)
        ([0-9]{3}[\s\-]?[0-9]{3}[\s\-]?[0-9]{3}[\s\-]?[0-9]{2}|[0-9]{11})
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^а-яА-Яa-zA-Z0-9])(?:СНИЛС|страховой\s+номер\s+индивидуального\s+лицевого\s+счёта|страховой\s+номер\s+индивидуального\s+лицевого\s+счета|страховой\s+номер|номер\s+индивидуального\s+лицевого\s+счёта|номер\s+индивидуального\s+лицевого\s+счета|номер\s+СНИЛС|серия\s+и\s+номер\s+СНИЛС)\s*[:\-/\s]*\s*[^0-9]*?([0-9]{3}[\s\-]?[0-9]{3}[\s\-]?[0-9]{3}[\s\-]?[0-9]{2}|[0-9]{11})(?:[^а-яА-Яa-zA-Z0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8,
        ExpressionOption.CASELESS
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
        if(value.length <= 2)
            return false
        var summ = 0
        // Extract only the SNILS number from the match (may include keywords)
        val numberPattern = Regex("([0-9]{3}[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}|[0-9]{11})")
        val match = numberPattern.find(value)
        if (match == null) return false
        
        val snils = match.value.replace(Regex("[^0-9 -]"), "").replace(" ", "").replace("-", "").trim()

        if (snils.length != 11)
            return false

        val zeroCount = snils.count { it == '0' }
        if (zeroCount > snils.length / 2) return false

        if (snils.all { it == snils[0] }) return false

        if (snils.all { it == '0' || it == '1' }) return false

        if (isSequential(snils, true) || isSequential(snils, false)) return false

        if (isRepeatingPattern(snils, 2) || isRepeatingPattern(snils, 3) || isRepeatingPattern(snils, 5)) return false

        val chunks = snils.chunked(3)
        if (chunks.take(3).all { it.all { char -> char == it[0] } }) return false

        if (snils == snils.reversed()) return false

        val digitCounts = snils.groupingBy { it }.eachCount()
        if (digitCounts.values.any { it > 7 }) return false

        for (index in 0 until snils.length - 2) {
            summ += snils[index].digitToInt() * (9 - index)
        }
        val controlSum = if (listOf(100, 101).contains(summ)) {
            "00"
        } else if (summ > 101) {
            (summ % 101).toString().padStart(2, '0')
        } else {
            summ.toString().padStart(2, '0')
        }
        return snils.substring(snils.length - 2 until snils.length) == controlSum
    }

    override fun toString() = name
}

