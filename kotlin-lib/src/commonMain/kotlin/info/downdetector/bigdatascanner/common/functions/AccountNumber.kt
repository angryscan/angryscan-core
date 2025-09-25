package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object AccountNumber: IHyperPattern {
    const val JAVA_PATTERN: String = """(?<=\D|^)40[0-9]{3}(810|840|978)[0-9]{12}(?=\D|$)"""

    fun find(text:String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns: List<String> = listOf(
        """\b(40[0-9]{3}(810|840|978)[0-9]{12})\b"""
    )
    override val options = setOf(ExpressionOption.MULTILINE)

    override fun check(value: String): Boolean = true

}