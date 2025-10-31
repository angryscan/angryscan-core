package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object HashData : IHyperMatcher, IKotlinMatcher {
    override val name = "Hash Data"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?:^|(?<=\s)|(?<=[\(\[\{«"']))
        \s*
        ([0-9a-fA-F]{32}|[0-9a-fA-F]{40}|[0-9a-fA-F]{64}|[0-9a-fA-F]{96}|[0-9a-fA-F]{128})
        (?:$|(?=\s)|(?=[\)\]\}»"'\.,;:!?]))
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|\s|[\(\[\{«"'])\s*(?:[\(\[\{«"'])?\s*(?:[0-9a-fA-F]{32}|[0-9a-fA-F]{40}|[0-9a-fA-F]{64}|[0-9a-fA-F]{96}|[0-9a-fA-F]{128})(?:$|[\s\)\]\}»"'\.,;:!?])"""
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

        return s.length in listOf(32, 40, 64, 96, 128) && s.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }
    }

    override fun toString() = name
}
