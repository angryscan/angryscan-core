package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object CVV : IHyperPattern {
    const val JAVA_PATTERN = """(\.|\s|^)(cvc|cvv|cav|cvc2|cvv2|cav2)(:|\s|:\s)[0-9]{3}(\.|\s|$)"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns = listOf(
        """(\.|\s|^)(cvc|cvv|cav|cvc2|cvv2|cav2)(:|\s|:\s)[0-9]{3}(\.|\s|$)"""
    )
    override val options = setOf(
        ExpressionOption.CASELESS,
        ExpressionOption.MULTILINE,
    )

    override fun check(value: String) = true

}

