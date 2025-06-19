package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.constants.CardBins
import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findCardNumbers(text: String): Sequence<String> {
    fun isBinValid(card: String): Boolean {
        return CardBins.cardBins.contains(card.substring(0, 4))
    }

    fun isCardValid(card: String): Boolean {
        var controlSum = 0
        for (index in card.indices) {
            controlSum += if ((index % 2) == 0) {
                val count = 2 * Character.getNumericValue(card[index])
                if (count <= 9) count else count - 9
            } else {
                Character.getNumericValue(card[index])
            }
        }
        return controlSum % 10 == 0
    }

    val cards = regexDetector(
        text,
        """(?<=[-:,()=*\s]|^)(([0-9]{4}[ ][0-9]{4}[ ][0-9]{4}[ ][0-9]{4})|([0-9]{16}))(?=[-(),*\s]|$)"""
            .toRegex(setOf(RegexOption.MULTILINE))
    )
    return cards.map {
        it.replace(" ", "").replace("-", "").trim()
    }.filter {
        it != "0000000000000000"
                && isBinValid(it)
                && isCardValid(it)
    }
}