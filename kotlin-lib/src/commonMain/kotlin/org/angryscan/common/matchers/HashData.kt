package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object HashData : IHyperMatcher, IKotlinMatcher {
    override val name = "Hash Data"
    override val javaPatterns = listOf(
        """(?ix)\A([0-9a-fA-F]{32}|[0-9a-fA-F]{40}|[0-9a-fA-F]{64}|[0-9a-fA-F]{96}|[0-9a-fA-F]{128})(?![\p{L}\d])""",
        """(?ix)(?:(?<=:\s)|(?<=[\s({\["'«».,;:]))(?<![\p{L}\d])([0-9a-fA-F]{32}|[0-9a-fA-F]{40}|[0-9a-fA-F]{64}|[0-9a-fA-F]{96}|[0-9a-fA-F]{128})(?:[\s\r\n\.\(\)\[\]\{\}\"'«».,;:!?\-]|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?i)(?:^|[\s\r\n#:=\-\(\)\[\]\{\}\"'«».,;])(?:[0-9a-fA-F]{32}|[0-9a-fA-F]{40}|[0-9a-fA-F]{64}|[0-9a-fA-F]{96}|[0-9a-fA-F]{128})(?:[\s\r\n\.\(\)\[\]\{\}\"'«».,;:!?\-]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        fun isOpenBracket(ch: Char): Boolean = ch == '(' || ch == '[' || ch == '{' || ch == '«' || ch == '"' || ch == '\''
        fun isCloseBracketOrPunct(ch: Char): Boolean =
            ch == ')' || ch == ']' || ch == '}' || ch == '»' || ch == '"' || ch == '\'' ||
            ch == '.' || ch == ',' || ch == ';' || ch == ':' || ch == '!' || ch == '?'

        var s = value.trim()
        while (s.isNotEmpty() && isOpenBracket(s.first())) {
            s = s.drop(1).trimStart()
        }
        while (s.isNotEmpty() && (isCloseBracketOrPunct(s.last()) || s.last().isWhitespace())) {
            s = s.dropLast(1)
        }

        if (s.length !in listOf(32, 40, 64, 96, 128)) return false
        if (!s.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }) return false
        
        val cleaned = s.lowercase()
        
        if (cleaned.all { it == '0' }) return false
        if (cleaned.all { it == 'f' }) return false
        if (cleaned.all { it == cleaned[0] }) return false
        
        if (cleaned.all { it.isDigit() }) return false
        
        val uniqueChars = cleaned.toSet().size
        if (uniqueChars < 4) return false
        
        val pattern1 = "0123456789abcdef".repeat(8).take(cleaned.length)
        if (cleaned == pattern1) return false
        
        val pattern2 = "fedcba9876543210".repeat(8).take(cleaned.length)
        if (cleaned == pattern2) return false
        
        return true
    }

    override fun toString() = name
}
