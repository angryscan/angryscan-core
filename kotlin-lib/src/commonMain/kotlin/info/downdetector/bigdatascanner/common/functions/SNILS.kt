package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findSNILS(text: String): Sequence<String> {
    /**
     * This function checks if the given SNILS is correct.
     * @param input the SNILS to check
     * @return true if the SNILS is correct, false otherwise
     */
    fun isSnilsCorrect(input: String): Boolean {
        var summ = 0
        val snils = input.replace(" ", "").replace("-", "").trim()

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

    return regexDetector(
        text,
        """(?<=[-,()=*\s]|^)[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{3}[ -]?[0-9]{2}(?=[-(),*\s]|$)"""
            .toRegex(setOf(RegexOption.MULTILINE))
    ).filter {
        isSnilsCorrect(it)
    }
}