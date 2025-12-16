package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian Tax Identification Number (ИНН - Идентификационный номер налогоплательщика).
 * Matches 12-digit INN numbers for individuals.
 * Validates using checksum algorithm and filters out fake patterns (sequential, repeating, all same digits).
 */
@Serializable
object INN : IHyperMatcher, IKotlinMatcher {
    override val name = "INN"
    override val javaPatterns = listOf(
        """(?<=^|[\s.,\-:"()>])([0-9]{12}|([0-9]{2} [0-9]{2}|([0-9]{4})) ([0-9]{6} [0-9]{2}|[0-9]{8}))(?=$|[\s.,;()"<])"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:^|[\s.,\-:"()>])([0-9]{12}|([0-9]{2} [0-9]{2}|[0-9]{4}) ([0-9]{6} [0-9]{2}|[0-9]{8}))(?:$|[ \t\r\a.,;()"<])"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
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
        val inn = value.replace("[^0-9]".toRegex(), "")
        if (inn.length != 12) return false
        
        val zeroCount = inn.count { it == '0' }
        if (zeroCount > inn.length / 2) return false
        
        if (inn.all { it == inn.first() }) return false
        
        if (inn.all { it == '0' || it == '1' }) return false

        if (isSequential(inn, true) || isSequential(inn, false)) return false

        if (isRepeatingPattern(inn, 2) || isRepeatingPattern(inn, 3) || isRepeatingPattern(inn, 4) || isRepeatingPattern(inn, 6)) return false

        val chunks = listOf(inn.substring(0, 3), inn.substring(3, 6), inn.substring(6, 9), inn.substring(9, 12))
        if (chunks.any { it.all { char -> char == it[0] } }) return false

        if (inn == inn.reversed()) return false

        val digitCounts = inn.groupingBy { it }.eachCount()
        if (digitCounts.values.any { it > 8 }) return false
        
        val firstSequence = listOf(7, 2, 4, 10, 3, 5, 9, 4, 6, 8)
        val secondSequence = listOf(3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8)
        var summ1 = 0
        for (index in firstSequence.indices)
            summ1 += firstSequence[index] * inn[index].digitToInt()
        val key1 = (summ1 % 11).toString().last()
        var summ2 = 0
        for (index in secondSequence.indices)
            summ2 += secondSequence[index] * inn[index].digitToInt()
        val key2 = (summ2 % 11).toString().last()
        return key1 == inn[10] && key2 == inn[11]
    }

    override fun toString() = name
}