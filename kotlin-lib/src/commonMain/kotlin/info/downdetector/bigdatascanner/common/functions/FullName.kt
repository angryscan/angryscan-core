package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object FullName : IHyperPattern {
    const val JAVA_PATTERN =
        """(^|[\p{Z}\uFEFF\u00A0])((([А-ЯЁ][а-яА-ЯёЁ]+\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+[ая]\p{Z})([А-ЯЁ][а-яё]+(на|НА)))|(([А-ЯЁ][а-яА-ЯёЁ]+\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+[^ая]\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+(ич|ь|ИЧ|Ь))))($|\W|\p{Z}|[,.-;\s])"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.MULTILINE)),
        withContext
    ).filter { check(it.value) }

    override val hyperPatterns = listOf(
        """(^|[^а-яА-Я])((([А-ЯЁ][а-яА-ЯёЁ]+\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+[ая]\p{Z})([А-ЯЁ][а-яё]+(на|НА)))|(([А-ЯЁ][а-яА-ЯёЁ]+\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+[^ая]\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+(ич|ь|ИЧ|Ь))))($|[^а-яА-Я]|\p{Z}|[,.-;\s])"""
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

