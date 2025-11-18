package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object IPv6 : IHyperMatcher, IKotlinMatcher {
    override val name = "IPv6"
    override val javaPatterns = listOf(
        """(?<=^|[\s>\"=\-\[\]{}()\`',;])(([0-9a-fA-F]{4}:){7}[0-9a-fA-F]{4})(?=[ \t\r\a.,;()\"`'<|()\[\]{}]|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns = listOf(
        """(?:^|[\s>"=\-[\]{}()\`',;])(([0-9a-fA-F]{4}:){7}[0-9a-fA-F]{4})([ \t\r\a.,;()"`'<|()[\]{}]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
