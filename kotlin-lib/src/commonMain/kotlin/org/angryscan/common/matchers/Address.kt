package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian addresses.
 * Matches addresses containing city/district/region abbreviations (г., р-н, обл.) or street abbreviations (ул., гор.)
 * followed by address text and house number (д., дом).
 */
@Serializable
object Address : IHyperMatcher, IKotlinMatcher {
    override val name = "Address"
    override val javaPatterns = listOf(
        """(?:(г\.|р-н|обл\.)|(?<!г\.|р-н|обл\.)(?<!г\.[а-яё ,.-]{0,50}, )(ул\.|гор\.))[а-яё ,.-]{4,54}(д\.|дом)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:(г\.|р-н|обл\.)|(?:^|[^\w])(ул\.|гор\.))[а-яёА-ЯЁ ,.\-]{4,54}(д\.|дом)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )


    override fun check(value: String): Boolean = true

    override fun toString() = name
}
