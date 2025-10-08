package org.angryscan.common.functions

import org.angryscan.common.engine.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.IKotlinMatcher

object CarNumber : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """(гос|номер|авто|рег).{0,15}([авекмнорстухabekmhopctyx][ \t]?[0-9]{3}[ \t]?[авекмнорстухabekmhopctyx]{2}[ \t]?[0-9]{2,3})"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(гос|номер|авто|рег).{0,15}([авекмнорстухabekmhopctyx][ \t]?[0-9]{3}[ \t]?[авекмнорстухabekmhopctyx]{2}[ \t]?[0-9]{2,3})"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS
    )

    override fun check(value: String): Boolean = true

}

