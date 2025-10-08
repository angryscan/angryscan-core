package org.angryscan.common.functions

import org.angryscan.common.constants.CardBins
import org.angryscan.common.engine.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.IKotlinMatcher
import org.angryscan.common.engine.IMask

class CardNumber(val checkCardBins: Boolean = true)
    : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """(?<![^\s.,\-:"()])([0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}|[0-9]{16})(?![^\s.,;)"])"""
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
        """\b([0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}|[0-9]{16})\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE
    )

    override fun check(value: String): Boolean {
        val cleanCard = value.replace(" ", "").replace("-", "").trim()
        return cleanCard != "0000000000000000"
                && (!checkCardBins || isBinValid(cleanCard))
                && isCardValid(cleanCard)
    }

    companion object: IMask {
        override fun mask(value: String): String {
            var i = 0
            return value.map { c ->
                if (c.isDigit()) {
                    i++
                    if(i > 6 && i < 13)
                        '*'
                    else
                        c
                } else
                    c
            }.joinToString(separator = "")
        }
    }

}

