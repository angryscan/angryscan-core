package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object Geo : IHyperMatcher, IKotlinMatcher {
    override val name = "Geo"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?:^|(?<=\s)|(?<=[\(\[\{«"']))
        \s*
        (
          (?:-?(?:[1-8]\d(?:\.\d{1,10})?|90(?:\.0{1,10})?|\d\.\d{1,10}))
          \s*,\s*
          (?:-?(?:180(?:\.0{1,10})?|1[0-7]\d(?:\.\d{1,10})?|[1-9]\d(?:\.\d{1,10})?|\d\.\d{1,10}))
        )
        (?:$|(?=\s)|(?=[\)\]\}»"'.,;:!?]))
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|\s|[\(\[\{«"'])\s*(?:[\(\[\{«"'])?\s*-?(?:[1-8]\d(?:\.\d{1,10})?|90(?:\.0{1,10})?|\d\.\d{1,10})\s*,\s*-?(?:180(?:\.0{1,10})?|1[0-7]\d(?:\.\d{1,10})?|[1-9]\d(?:\.\d{1,10})?|\d\.\d{1,10})(?:$|[\s\)\]\}»"'\.,;:!?])"""
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

        val labels = listOf(
            "геолокация фл",
            "координаты физического лица",
            "широта и долгота фл"
        )
        val slow = s.lowercase()
        val matchedLabel = labels.firstOrNull { slow.startsWith(it) }
        if (matchedLabel != null) {
            val idx = matchedLabel.length
            s = s.substring(idx)
            s = s.trimStart()
            if (s.isNotEmpty() && (s.first() == ':' || s.first() == '-')) {
                s = s.drop(1)
            }
            s = s.trimStart()
        }

        while (s.isNotEmpty() && isOpenBracket(s.first())) {
            s = s.drop(1).trimStart()
        }

        while (s.isNotEmpty() && (isCloseBracketOrPunct(s.last()) || s.last().isWhitespace())) {
            s = s.dropLast(1)
        }

        val parts = s.split(",").map { it.trim() }
        if (parts.size != 2) return false
        val lat = parts[0].toDoubleOrNull() ?: return false
        val lon = parts[1].toDoubleOrNull() ?: return false
        return lat in -90.0..90.0 && lon in -180.0..180.0
    }

    override fun toString() = name
}
