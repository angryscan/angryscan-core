package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.MatchWithContext
import info.downdetector.bigdatascanner.common.extensions.customRegexDetector
import kotlin.text.iterator

object OMS: IHyperPattern {
    const val JAVA_PATTERN = """(?<=\D|^)(?<=(омс|полис|страховка|страхование))(\s)[0-9]{4}[ \t-]*?[0-9]{4}[ \t-]*?[0-9]{4}[ \t-]*?[0-9]{4}(?=\D|$)"""

    fun find(text: String, withContext: Boolean): Sequence<MatchWithContext> {

        return customRegexDetector(
            text,
JAVA_PATTERN
            .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
        withContext
        ).filter {
            check(it.value)
        }
    }

    override val hyperPatterns = listOf(
        """(?:^|\D)(омс|полис|страховка|страхование)\s[0-9]{4}[ \t-]*[0-9]{4}[ \t-]*[0-9]{4}[ \t-]*[0-9]{4}(?:\D|$)"""
    )
    override val options = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS
    )

    override fun check(value: String): Boolean {
        val oms = value.replace("""\D""".toRegex(), "")
        val key = oms.last().digitToInt()
        val odd = mutableListOf<Char>()  // nechet
        val even = mutableListOf<Char>() // chet
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

}


