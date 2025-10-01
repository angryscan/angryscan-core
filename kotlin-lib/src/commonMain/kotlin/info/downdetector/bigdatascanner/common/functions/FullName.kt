package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object FullName : IHyperPattern {
    const val JAVA_PATTERN =
        """(^|\s)(?!Республика|Область|Край|Город|Село|Деревня|Улица|Проспект|Марий)((([А-ЯЁ][а-яё]+\s)([А-ЯЁ][а-яё]+[ая]\s)([А-ЯЁ][а-яё]+на))|(([А-ЯЁ][а-яё]+\s)([А-ЯЁ][а-яё]+[^ая]\s)([А-ЯЁ][а-яё]+(ич|ь))))($|\W|\s)"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns = listOf(
        """(^|\s)((([А-ЯЁ][а-яё]+\s)([А-ЯЁ][а-яё]+[ая]\s)([А-ЯЁ][а-яё]+на))|(([А-ЯЁ][а-яё]+\s)([А-ЯЁ][а-яё]+[^ая]\s)([А-ЯЁ][а-яё]+(ич|ь))))($|\W|\s)"""
    )
    override val options = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val falseList = listOf(
            "Республика",
            "Область",
            "Край",
            "Город",
            "Село",
            "Деревня",
            "Улица",
            "Проспект",
            "Марий"
        )
        return !falseList.any { value.contains(it) }
    }

}

