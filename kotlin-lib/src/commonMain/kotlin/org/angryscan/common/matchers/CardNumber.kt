package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.constants.CardBins
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.IMask
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
class CardNumber(val checkCardBins: Boolean = true) : IHyperMatcher, IKotlinMatcher, IMask {
    override val name = "Card number"

    override val javaPatterns = listOf(
        """(?<=^|[\s.,\-:;"()'])([0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}|[0-9]{16})(?![^\s.,;)"<])"""
    )

    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    private fun isBinValid(card: String): Boolean {
        return CardBins.cardBins.contains(card.take(4))
    }

    private fun isCardValid(card: String): Boolean {
        fun luhn(withFirstDouble: Boolean): Boolean {
            var sum = 0
            var shouldDouble = withFirstDouble
            for (i in card.lastIndex downTo 0) {
                var digit = card[i].digitToInt()
                if (shouldDouble) {
                    digit *= 2
                    if (digit > 9) digit -= 9
                }
                sum += digit
                shouldDouble = !shouldDouble
            }
            return sum % 10 == 0
        }

        return luhn(false) || luhn(true)
    }

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s.,\-:;"()'])([0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}|[0-9]{16})\b"""
    )

    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE
    )

    override fun check(value: String): Boolean {
        val cleanCard = value.replace("[^0-9]".toRegex(), "")
        return cleanCard != "0000000000000000"
                && (!checkCardBins || isBinValid(cleanCard))
                && isCardValid(cleanCard)
    }

    override fun toString() = name + if (!checkCardBins) "(w/o BINs)" else ""

    override fun equals(other: Any?): Boolean {
        return other is CardNumber && other.checkCardBins == checkCardBins
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + checkCardBins.hashCode()
        return result
    }

    override fun mask(value: String): String {
        var i = 0
        return value.map { c ->
            if (c.isDigit()) {
                i++
                if(i in 7..<13)
                    '*'
                else
                    c
            } else
                c
        }.joinToString(separator = "")
    }
}

