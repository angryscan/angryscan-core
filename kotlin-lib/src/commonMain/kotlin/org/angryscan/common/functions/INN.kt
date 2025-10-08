package org.angryscan.common.functions

import org.angryscan.common.engine.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.IKotlinMatcher

object INN : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """(?<![^\s.,\-:"()])([0-9]{12}|([0-9]{2} [0-9]{2}|([0-9]{4})) ([0-9]{6} [0-9]{2}|[0-9]{8}))(?![^\s.,;)"])"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(^|[\s.,\-:"(])([0-9]{12}|([0-9]{2} [0-9]{2}|([0-9]{4})) ([0-9]{6} [0-9]{2}|[0-9]{8}))([\s.,;)]|$)"""
    )
    override val expressionOptions = setOf(
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