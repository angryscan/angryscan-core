package org.angryscan.common.engine.kotlin

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.angryscan.common.engine.IScanEngine
import org.angryscan.common.engine.Match

@Serializable
class KotlinEngine(
    @Serializable override val matchers: List<IKotlinMatcher>,
    val requireKeywords: Boolean = true
) : IScanEngine {
    @Transient
    private val compiledPatterns: List<Pair<Regex, IKotlinMatcher>> =
        matchers.flatMap { matcher ->
            matcher.getJavaPatterns(requireKeywords).map { pattern ->
                pattern.toRegex(matcher.regexOptions) to matcher
            }
        }

    override fun scan(text: String): List<Match> {
        return compiledPatterns.flatMap { (regex, matcher) ->
            regex.findAll(text)
                .filter { matcher.check(it.value) }
                .map { match ->
                    Match(
                        value = match.value,
                        before = text.substring(
                            maxOf(0, match.range.first - 10),
                            match.range.first
                        ),
                        after = if (match.range.last + 1 < text.length)
                            text.substring(match.range.last + 1, minOf(match.range.last + 11, text.length))
                        else "",
                        startPosition = match.range.first.toLong(),
                        endPosition = match.range.last.toLong(),
                        matcher = matcher
                    )
                }
                .toList()
        }.distinct()
    }

    override fun close() {}
}