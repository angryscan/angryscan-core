package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object Passport : IHyperPattern {
    const val JAVA_PATTERN = """(паспорт[ \t-]?([а-яА-Я]*[ \t-]){0,2}[0-9]{2}[ \t]?[0-9]{2}[ \t]?[0-9]{6})"""
    const val JAVA_PATTERN_SECOND = """[сc]ерия[ \t-]?[0-9]{2}(\s|\t)?[0-9]{2}[ \t,]?(номер)?[ \t-]?[0-9]{6}?"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
        withContext
    ) + regexDetector(
        text,
        JAVA_PATTERN_SECOND
            .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns = listOf(
        """(паспорт[ \t-]?([а-яА-Я]*[ \t-]){0,2}[0-9]{2}[ \t]?[0-9]{2}[ \t]?[0-9]{6})""",
        """[cс]ерия[ \t-]?[0-9]{2}(\s|\t)?[0-9]{2}[ \t,]?(номер)?[ \t-]?[0-9]{6}"""
    )
    override val options = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

}


