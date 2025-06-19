package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.customRegexDetector
import kotlin.text.iterator

fun findOMS(text: String): Sequence<String> {
    // validate oms
    /**
     * Checks if the given OMS is correct.
     * @param input the OMS to check
     * @return true if the OMS is correct, false otherwise
     */
    fun isOmsValid(input: String): Boolean {
        val oms = input.replace("-", "").replace("""\s+""".toRegex(), "")
        val key = Character.getNumericValue(oms.last())
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
            summ += Character.getNumericValue(elem)
        for (elem in right)
            summ += Character.getNumericValue(elem)
        // nearest value more or equal sum and sum % 10 = 0 minus sum
        val checker = 10 - summ % 10
        return checker == key || (checker == 10 && key == 0)
    }

    return customRegexDetector(
        text,
        """(?<=\D|^)(?<=(омс|полис|страховка|страхование))(\s)[0-9]{4}[ \t-]*?[0-9]{4}[ \t-]*?[0-9]{4}[ \t-]*?[0-9]{4}(?=\D|$)"""
            .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
    ).filter {
        isOmsValid(it)
    }
}