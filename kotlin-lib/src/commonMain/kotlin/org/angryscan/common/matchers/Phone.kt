package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian phone numbers.
 * Matches phone numbers in formats:
 * - +7 (XXX) XXX-XX-XX
 * - 8 (XXX) XXX-XX-XX
 * - 7 XXX XXX XX XX
 * Country code: +7 or 8, area code starts with 4, 8, or 9, followed by 10 digits total.
 */
@Serializable
object Phone : IHyperMatcher, IKotlinMatcher {
    override val name = "Phone"
    override val javaPatterns= listOf(
        """(?<=[-, ()=*>":;']|^)((\+?7)|8)[ \t\-]?\(?[489][0-9]{2}\)?[ \t\-]?[0-9]{3}[ \t\-]?[0-9]{2}[ \t\-]?[0-9]{2}(?=\W|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:[-, ()=*>":;']|^)((\+?7)|8)[ \t\-]?\(?[489][0-9]{2}\)?[ \t\-]?[0-9]{3}[ \t\-]?[0-9]{2}[ \t\-]?[0-9]{2}(?:\b)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
