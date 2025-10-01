package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.MatchWithContext
import info.downdetector.bigdatascanner.common.extensions.regexDetector

object SNILS : IHyperPattern {
    const val JAVA_PATTERN = """(?<=[-,()=*\s]|^)[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{2}(?=[-(),*\s]|$)"""

    fun find(text: String, withContext: Boolean): Sequence<MatchWithContext> {
        return regexDetector(
            text,
            JAVA_PATTERN
                .toRegex(setOf(RegexOption.MULTILINE)),
            withContext
        ).filter {
            check(it.value)
        }
    }

    override val hyperPatterns: List<String> = listOf(
        """\b[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{2}\b"""
    )
    override val options = setOf(ExpressionOption.MULTILINE)

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

