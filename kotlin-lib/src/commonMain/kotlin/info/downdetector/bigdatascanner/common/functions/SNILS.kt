package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.engine.IHyperMatcher
import info.downdetector.bigdatascanner.common.engine.ExpressionOption
import info.downdetector.bigdatascanner.common.engine.IKotlinMatcher

object SNILS : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """\b[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{2}\b"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\b[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{2}\b"""
    )
    override val expressionOptions = setOf(ExpressionOption.MULTILINE)

    override fun check(value: String): Boolean {
        var summ = 0
        val snils = value.replace(" ", "").replace("-", "").trim()

        for (index in 0 until snils.length - 2) {
            summ += snils[index].digitToInt() * (9 - index)
        }
        val controlSum = if (listOf(100, 101).contains(summ)) {
            "00"
        } else if (summ > 101) {
            (summ % 101).toString()
        } else {
            summ.toString()
        }
        return snils.substring(snils.length - 2 until snils.length) == controlSum
    }

}

