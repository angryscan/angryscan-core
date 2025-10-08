package org.angryscan.common.functions

import org.angryscan.common.engine.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.IKotlinMatcher

object Phone : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns= listOf(
        """(?<=[-, ()=*]|^)((\+?7)|8)[ \t\-]?\(?[489][0-9]{2}\)?[ \t\-]?[0-9]{3}[ \t\-]?[0-9]{2}[ \t\-]?[0-9]{2}(?=\W|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:[-, ()=*]|^)((\+?7)|8)[ \t\-]?\(?[489][0-9]{2}\)?[ \t\-]?[0-9]{3}[ \t\-]?[0-9]{2}[ \t\-]?[0-9]{2}(?:\W|$)"""
    )
    override val expressionOptions = setOf(ExpressionOption.MULTILINE)

    override fun check(value: String): Boolean = true

}

