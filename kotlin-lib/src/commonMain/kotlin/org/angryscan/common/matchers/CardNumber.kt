package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.constants.CardBins
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
class CardNumber(val checkCardBins: Boolean = true) : IHyperMatcher, IKotlinMatcher {
    override val name = "Card number"
    override val javaPatterns = listOf(
        """(?<=[\s.,\-:"()])([0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}|[0-9]{16})(?![^\s.,;)"<])"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    private fun isBinValid(card: String): Boolean {
        return CardBins.cardBins.contains(card.take(4))
    }

    private fun isCardValid(card: String): Boolean {
        var controlSum = 0
        for (index in card.indices) {
            controlSum += if ((index % 2) == 0) {
                val count = 2 * card[index].digitToInt()
                if (count <= 9) count else count - 9
            } else {
                card[index].digitToInt()
            }
        }
        return controlSum % 10 == 0
    }

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s.,\-:"()])([0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}|[0-9]{16})\b"""
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
}
