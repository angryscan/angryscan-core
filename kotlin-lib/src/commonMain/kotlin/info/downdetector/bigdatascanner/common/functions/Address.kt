package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object Address : IHyperPattern {
    const val JAVA_PATTERN = """(г\.|р-н|обл\.|ул\.|гор\.).{4,70}(д\.|дом)"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns = listOf(
        """(г\.|р-н|обл\.)[а-я ,.-]{4,55}(д\.|дом)""",
        """(ул\.|гор\.)[а-я ,.-]{4,55}(д\.|дом)"""
    )
    override val options = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

}

