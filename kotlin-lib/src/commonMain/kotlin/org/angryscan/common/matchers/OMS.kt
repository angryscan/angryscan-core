package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object OMS : IHyperMatcher, IKotlinMatcher {
    override val name = "OMS"
    override val javaPatterns = listOf(
        """(?<=\D|^)(?<=(омс|полис|страховка|страхование))(\s)[0-9]{4}[ \t-]*?[0-9]{4}[ \t-]*?[0-9]{4}[ \t-]*?[0-9]{4}(?=\D|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:^|[\s.,\-:"()>])(омс|полис|страховка|страхование)\s[0-9]{4}[ \t-]*[0-9]{4}[ \t-]*[0-9]{4}[ \t-]*[0-9]{4}(?:$|[ \t\r.,;()"<])"""
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
            if (index % 2 == 0) {
                odd.add(digit)
            } else {
                even.add(digit)
            }
        }
        val right = (odd.joinToString(separator = "").toInt() * 2).toString()
        var summ = 0
        for (elem in even)
            summ += elem.digitToInt()
        for (elem in right)
            summ += elem.digitToInt()
        val checker = 10 - summ % 10
        return checker == key || (checker == 10 && key == 0)
    }

    override fun toString() = name
}
