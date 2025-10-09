package org.angryscan.common.matchers

import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

object Passport : IHyperMatcher, IKotlinMatcher {
    override val name = "Passport"
    override val javaPatterns = listOf(
        """(паспорт[ \t-]?([а-яА-Я]*[ \t-]){0,2}[0-9]{2}[ \t]?[0-9]{2}[ \t]?[0-9]{6})""",
        """[сc]ерия[ \t-]?[0-9]{2}(\s|\t)?[0-9]{2}[ \t,]?(номер)?[ \t-]?[0-9]{6}?"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(паспорт[ \t-]?([а-яА-Я]*[ \t-]){0,2}[0-9]{2}[ \t]?[0-9]{2}[ \t]?[0-9]{6})""",
        """[cс]ерия[ \t-]?[0-9]{2}(\s|\t)?[0-9]{2}[ \t,]?(номер)?[ \t-]?[0-9]{6}"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

}


