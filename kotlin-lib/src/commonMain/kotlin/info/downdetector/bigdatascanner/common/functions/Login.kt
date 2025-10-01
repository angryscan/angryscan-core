package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object Login : IHyperPattern {
    const val JAVA_PATTERN = """(логин|login):?\s*[a-z0-9_-]{3,25}"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns = listOf(
        """(логин|login):?\s*([a-z0-9_-]{3,25})($|\W)"""
    )
    override val options = setOf(
        ExpressionOption.CASELESS,
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String) = true

}

