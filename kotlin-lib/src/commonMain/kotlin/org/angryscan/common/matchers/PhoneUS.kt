package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object PhoneUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Phone US"
    override val javaPatterns = listOf(
        """(?<![\p{L}\d])(?:(?:\+?1[ \t\-]?)?(?:\([2-9][0-9]{2}\)[ \t\-][2-9][0-9]{2}[ \t\-][0-9]{4}|[2-9][0-9]{2}[ \t\-][2-9][0-9]{2}[ \t\-][0-9]{4}))(?![0-9])"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^a-zA-Z0-9])(?:(?:\+?1[ \t\-]?)?(?:\([2-9][0-9]{2}\)[ \t\-][2-9][0-9]{2}[ \t\-][0-9]{4}|[2-9][0-9]{2}[ \t\-][2-9][0-9]{2}[ \t\-][0-9]{4}))(?:[^0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    private fun isPowerOfTwo(n: Int): Boolean {
        if (n <= 0) return false
        return (n and (n - 1)) == 0
    }

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

    private val knownNonUSAreaCodes = setOf(
        "495", "499", "496", "497", "498",
        "812", "813",
        "343", "351", "383", "391", "846", "831",
        "421", "423", "395", "342", "863", "861",
        "472", "473", "474", "475", "481", "482",
        "483", "484", "485", "486", "487", "491",
        "492", "493", "494", "800", "900"
    )

    override fun check(value: String): Boolean {
        var digits = value.replace(Regex("[^0-9]"), "")
        
        if (digits.length == 11 && digits.startsWith("1")) {
            digits = digits.substring(1)
        }
        
        if (digits.length != 10) return false
        
        val areaCode = digits.substring(0, 3)
        val exchange = digits.substring(3, 6)
        val number = digits.substring(6, 10)
        
        if (areaCode in knownNonUSAreaCodes) return false
        
        val blocks = listOf(areaCode, exchange, number)
        
        for (block in blocks) {
            val zeroCount = block.count { it == '0' }
            if (zeroCount > block.length / 2) return false
        }
        
        val blockValues = blocks.map { it.toIntOrNull() ?: return false }
        if (blockValues.all { isPowerOfTwo(it) }) return false
        
        if (digits.all { it == digits[0] }) return false
        
        if (isSequential(digits, true) || isSequential(digits, false)) return false
        
        if (isRepeatingPattern(digits, 2) || isRepeatingPattern(digits, 3) || isRepeatingPattern(digits, 5)) return false
        
        if (digits == digits.reversed()) return false
        
        if (blocks.all { it.all { char -> char == it[0] } }) return false
        
        if (exchange == "555" && number.toIntOrNull() in 100..199) return false
        
        if (exchange == number) return false
        
        if (number.startsWith(exchange.substring(0, 2)) || number.startsWith(exchange.substring(1, 3))) return false
        
        val pairs = digits.chunked(2)
        if (pairs.size >= 4) {
            val firstPair = pairs[0]
            val secondPair = pairs[1]
            if (pairs.count { it == firstPair } >= 2 && pairs.count { it == secondPair } >= 2) {
                if (pairs.filter { it == firstPair || it == secondPair }.size >= 4) return false
            }
        }
        
        val month = areaCode.substring(0, 2).toIntOrNull()
        val day = areaCode.substring(1, 3).toIntOrNull() ?: exchange.substring(0, 2).toIntOrNull()
        val year = (exchange.substring(1, 3) + number.substring(0, 2)).toIntOrNull()
        if (month != null && month in 1..12 && day != null && day in 1..31 && year != null && year in 1900..2100) {
            return false
        }
        
        val uniqueDigits = digits.toSet()
        if (uniqueDigits.size <= 3) {
            val minDigit = uniqueDigits.minOrNull()?.digitToInt() ?: 0
            val maxDigit = uniqueDigits.maxOrNull()?.digitToInt() ?: 9
            if (maxDigit - minDigit <= 3) return false
        }
        
        if (digits.length >= 6) {
            var alternating = true
            val first = digits[0]
            val second = digits[1]
            for (i in 2 until digits.length) {
                if (i % 2 == 0) {
                    if (digits[i] != first) {
                        alternating = false
                        break
                    }
                } else {
                    if (digits[i] != second) {
                        alternating = false
                        break
                    }
                }
            }
            if (alternating) return false
        }
        
        val exchangeReversed = exchange.reversed()
        if (number.startsWith(exchangeReversed.substring(0, 2)) || number == exchangeReversed) return false
        
        val digitCounts = digits.groupingBy { it }.eachCount()
        if (digitCounts.values.any { it > 7 }) return false
        
        return true
    }

    override fun toString() = name
}
