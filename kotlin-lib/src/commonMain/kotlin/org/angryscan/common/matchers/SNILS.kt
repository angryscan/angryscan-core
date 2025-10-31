package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object SNILS : IHyperMatcher, IKotlinMatcher {
    override val name = "SNILS"
    override val javaPatterns = listOf(
        """\b[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{2}\b(?![-0-9])"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{2}(?:[\s\r\n\(\)\[\]\"'.,;:!?]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE
    )

    override fun check(value: String): Boolean {
        if(value.length <= 2)
            return false
        var summ = 0
        val snils = value.replace(Regex("[^0-9 -]"), "").replace(" ", "").replace("-", "").trim()

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
