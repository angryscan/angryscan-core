package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object Phone : IHyperPattern {
    const val JAVA_PATTERN =
        """(?<=[-, ()=*]|^)((\+?7)|8)[ \t\-]?\(?[489][0-9]{2}\)?[ \t\-]?[0-9]{3}[ \t\-]?[0-9]{2}[ \t\-]?[0-9]{2}(?=\W|$)"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:[-, ()=*]|^)((\+?7)|8)[ \t\-]?\(?[489][0-9]{2}\)?[ \t\-]?[0-9]{3}[ \t\-]?[0-9]{2}[ \t\-]?[0-9]{2}(?:\W|$)"""
    )
    override val options = setOf(ExpressionOption.MULTILINE)

    override fun check(value: String): Boolean = true

}

