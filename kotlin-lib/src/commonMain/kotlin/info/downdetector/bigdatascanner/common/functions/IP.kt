package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object IP : IHyperPattern {
    const val JAVA_PATTERN =
        """(^|\s)((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)\.){3}(25[0-5]|(2[0-4]|1\d|[1-9]|)\d)($|\s)"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns = listOf(
        """(^|\s)((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)\.){3}(25[0-5]|(2[0-4]|1\d|[1-9]|)\d)($|\s)"""
    )
    override val options = setOf(
        ExpressionOption.MULTILINE,
    )

    override fun check(value: String) = true
}