package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.MatchWithContext
import info.downdetector.bigdatascanner.common.extensions.customRegexDetector

object INN : IHyperPattern {
    const val JAVA_PATTERN = """(?<![^\s.,\-:"()])([0-9]{12}|([0-9]{2} [0-9]{2}|([0-9]{4})) ([0-9]{6} [0-9]{2}|[0-9]{8}))(?![^\s.,;)"])"""

    fun find(text: String, withContext: Boolean): Sequence<MatchWithContext> {
        return customRegexDetector(
            text,
            JAVA_PATTERN
                .toRegex(setOf(RegexOption.MULTILINE)),
            withContext
        ).filter {
            check(it.value)
        }
    }

    override val hyperPatterns = listOf(
        """(^|[\s.,\-:"(])([0-9]{12}|([0-9]{2} [0-9]{2}|([0-9]{4})) ([0-9]{6} [0-9]{2}|[0-9]{8}))([\s.,;)]|$)"""
    )
    override val options = setOf(
        ExpressionOption.MULTILINE,
    )

    override fun check(value: String): Boolean {
        val inn = value.replace("-", "").replace(" ", "").trim()
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

}