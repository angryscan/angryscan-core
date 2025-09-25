package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object CarNumber : IHyperPattern {
    const val JAVA_PATTERN =
        """(гос|номер|авто|рег).{0,15}([авекмнорстухabekmhopctyx][ \t]?[0-9]{3}[ \t]?[авекмнорстухabekmhopctyx]{2}[ \t]?[0-9]{2,3})"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns: List<String> = listOf(
        """(гос|номер|авто|рег).{0,15}([авекмнорстухabekmhopctyx][ \t]?[0-9]{3}[ \t]?[авекмнорстухabekmhopctyx]{2}[ \t]?[0-9]{2,3})"""
    )
    override val options = setOf(ExpressionOption.MULTILINE, ExpressionOption.CASELESS)

    override fun check(value: String): Boolean  = true

}

