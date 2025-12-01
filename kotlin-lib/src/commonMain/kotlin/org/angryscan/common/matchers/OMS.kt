package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object OMS : IHyperMatcher, IKotlinMatcher {
    override val name = "OMS"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:
          (?:омс|полис|страховка|страхование)\s*[:\-]?\s*
        )?
        ([0-9]{4}[\s\t-]*[0-9]{4}[\s\t-]*[0-9]{4}[\s\t-]*[0-9]{4})
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:^|[^\w])(?:(?:омс|полис|страховка|страхование)\s*[:\-]?\s*)?[0-9]{4}[\s\t-]*[0-9]{4}[\s\t-]*[0-9]{4}[\s\t-]*[0-9]{4}(?:[^\w]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val oms = value.replace("""\D""".toRegex(), "")
        val key = oms.last().digitToInt()
        val odd = mutableListOf<Char>()
        val even = mutableListOf<Char>()
        oms.substring(0 until oms.length - 1).reversed().forEachIndexed { index, digit ->
            // it's odd because starts with index = 0
            if (index % 2 == 0) {
                odd.add(digit)
            } else {
                even.add(digit)
            }
        }
        val right = (odd.joinToString(separator = "").toInt() * 2).toString()
        // getValue sum of all elements
        var summ = 0
        for (elem in even)
            summ += elem.digitToInt()
        for (elem in right)
            summ += elem.digitToInt()
        // nearest value more or equal sum and sum % 10 = 0 minus sum
        val checker = 10 - summ % 10
        return checker == key || (checker == 10 && key == 0)
    }

    override fun toString() = name
}
