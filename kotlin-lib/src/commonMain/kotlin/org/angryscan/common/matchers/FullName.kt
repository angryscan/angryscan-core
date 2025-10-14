package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object FullName : IHyperMatcher, IKotlinMatcher {
    override val name = "Full name"
    override val javaPatterns = listOf(
        """(^|[\p{Z}\uFEFF\u00A0])((([А-ЯЁ][а-яА-ЯёЁ]+\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+[ая]\p{Z})([А-ЯЁ][а-яё]+(на|НА)))|(([А-ЯЁ][а-яА-ЯёЁ]+\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+[^ая]\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+(ич|ь|ИЧ|Ь))))($|\W|\p{Z}|[,.-;\s])"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(^|[^а-яА-Я])((([А-ЯЁ][а-яА-ЯёЁ]+\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+[ая]\p{Z})([А-ЯЁ][а-яё]+(на|НА)))|(([А-ЯЁ][а-яА-ЯёЁ]+\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+[^ая]\p{Z})([А-ЯЁ][а-яА-ЯёЁ]+(ич|ь|ИЧ|Ь))))($|[^а-яА-Я]|\p{Z}|[,.-;\s])"""
    )
    override val expressionOptions = setOf(
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

    override fun toString() = name
}

