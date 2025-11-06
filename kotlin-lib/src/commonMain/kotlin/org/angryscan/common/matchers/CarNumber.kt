package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object CarNumber : IHyperMatcher, IKotlinMatcher {
    override val name = "Car number"
    override val javaPatterns = listOf(
        """(гос|номер|авто|рег).{0,15}([авекмнорстухabekmhopctyx][ \t]?[0-9]{3}[ \t]?[авекмнорстухabekmhopctyx]{2}[ \t]?[0-9]{2,3})"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(гос|номер|авто|рег).{0,15}([авекмнорстухabekmhopctyx][ \t]?[0-9]{3}[ \t]?[авекмнорстухabekmhopctyx]{2}[ \t]?[0-9]{2,3})\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}

