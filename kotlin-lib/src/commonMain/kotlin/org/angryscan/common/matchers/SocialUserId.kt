package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object SocialUserId : IHyperMatcher, IKotlinMatcher {
    override val name = "Social User ID"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:
          ID\s+в\s+(VK|Telegram|Instagram|Одноклассниках|Facebook)|
          username\s+в\s+(VK|Telegram|Instagram|Одноклассниках|Facebook)|
          аккаунт\s+в\s+(VK|Telegram|Instagram|Одноклассниках|Facebook)|
          профиль\s+ФЛ\s+в\s+соцсетях
        )?
        \s*[:\-]?\s*
        (@[a-zA-Z0-9_]{3,32})
        (?![a-zA-Z0-9_.\-])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])@[a-zA-Z0-9_]{3,32}(?:[\s\r\n\(\)\[\]\"',;:!?]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
