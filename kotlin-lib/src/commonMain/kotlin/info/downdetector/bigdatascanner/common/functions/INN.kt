package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.MatchWithContext
import info.downdetector.bigdatascanner.common.extensions.customRegexDetector

fun findINN(text:String, withContext: Boolean): Sequence<MatchWithContext> {
    // validate inn
    /**
     * Checks if the given INN is correct.
     * @param input the INN to check
     * @return true if the INN is correct, false otherwise
     */
    fun isInnValid(input: String): Boolean {
        val inn = input.replace("-", "").replace(" ", "").trim()
        // control sequences
        val firstSequence = listOf(7, 2, 4, 10, 3, 5, 9, 4, 6, 8)
        val secondSequence = listOf(3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8)
        // first control sum
        var summ1 = 0
        for (index in firstSequence.indices)
            summ1 += firstSequence[index] * inn[index].digitToInt()
        val key1 = (summ1 % 11).toString().last()
        // second control sum
        var summ2 = 0
        for (index in secondSequence.indices)
            summ2 += secondSequence[index] * inn[index].digitToInt()
        val key2 = (summ2 % 11).toString().last()
        return key1 == inn[10] && key2 == inn[11]
    }
    return customRegexDetector(
        text,
        """(?<=[-:,()=*\s]|^)[0-9]{12}(?=[-(),*\s]|$)"""
            .toRegex(setOf(RegexOption.MULTILINE)),
        withContext
    ).filter {
        isInnValid(it.value)
    }
}