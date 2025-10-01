package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object IPv6 : IHyperPattern {
    const val JAVA_PATTERN =
        """(^|\s)(([0-9a-fA-F]{4}:){7}[0-9a-fA-F]{4})($|\s)"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns = listOf(
        """(^|\s)(([0-9a-fA-F]{4}:){7}[0-9a-fA-F]{4})($|\s)"""
    )
    override val options = setOf(
        ExpressionOption.MULTILINE,
    )

    override fun check(value: String) = true
}