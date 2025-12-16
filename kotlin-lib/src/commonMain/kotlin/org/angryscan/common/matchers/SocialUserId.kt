package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for social media user IDs/usernames.
 * Matches usernames in format: @username (3-32 characters: letters, digits, underscore)
 * May be preceded by keywords like "ID в VK", "username в Telegram", "аккаунт в Instagram".
 * Filters out CSS at-rules, reserved names (admin, test, etc.), and numeric-only usernames.
 */
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
        (?![a-zA-Z0-9_\-])
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

    override fun check(value: String): Boolean {
        val usernamePattern = Regex("""@([a-zA-Z0-9_]{3,32})""")
        val match = usernamePattern.find(value) ?: return false
        val username = match.groupValues[1].lowercase()
        
        val cssAtRules = setOf(
            "media", "page", "pagemargin", "import", "charset", "namespace",
            "keyframes", "supports", "font-face", "document", "viewport",
            "counter-style", "font-feature-values", "property", "layer",
            "container", "scope", "starting-style", "scroll-timeline",
            "animation-timeline", "color-profile", "font-palette-values"
        )
        
        if (username in cssAtRules) return false
        
        if (username.all { it.isDigit() }) return false
        
        if (!username.any { it.isLetter() }) return false
        
        val reservedNames = setOf(
            "admin", "administrator", "root", "system",
            "test", "testing", "demo", "example",
            "support", "help", "info", "contact",
            "login", "logout", "register", "signup",
            "null", "undefined", "true", "false",
            "www", "mail", "email", "ftp", "http", "https"
        )
        
        if (username in reservedNames) return false
        
        return true
    }

    override fun toString() = name
}
