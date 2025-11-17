package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object DriverLicense : IHyperMatcher, IKotlinMatcher {
    override val name = "Driver License"
    override val javaPatterns = listOf(
        """(?<!\w)(?:[^\w\s]\s|\s|\n|[\(\[\{"']|«)\s*\d{2}\s?\d{2}\s\d{6}(?:\.\s|\s|\n|[)\]}"'»]\s|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^\w])(?:[^\w\s]\s|\s|\n|[\(\[\{"']|«)\s*\d{2}\s?\d{2}\s\d{6}(?:\.\s|\s|\n|[)\]}"'»]\s|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}