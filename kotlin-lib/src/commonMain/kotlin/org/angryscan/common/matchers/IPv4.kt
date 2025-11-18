package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object IPv4 : IHyperMatcher, IKotlinMatcher {
    override val name = "IPv4"
    override val javaPatterns = listOf(
        """(?<=^|[\s>\"=\-\[\]{}()`',;])((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)\.){3}(25[0-5]|(2[0-4]|1\d|[1-9]|)\d)(?=[ \t\r\a,;:()\"`'<|\[\]{}\-]|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:^|[\s>"=\-[\]{}()\`',;])((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)\.){3}(25[0-5]|(2[0-4]|1\d|[1-9]|)\d)([ \t\r\a,;:()"`'<|[\]{}\-]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String) = true

    override fun toString() = name
}