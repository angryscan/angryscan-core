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
            buildList {
                var startIndex = 0
                while (startIndex < text.length) {
                    val matchResult = regex.find(text, startIndex) ?: break
                    if (matcher.check(matchResult.value)) {
                        add(Match(
                            value = matchResult.value,
                            before = text.substring(maxOf(0, matchResult.range.first - 10), matchResult.range.first),
                            after = if (matchResult.range.last + 1 < text.length)
                                text.substring(matchResult.range.last + 1, minOf(matchResult.range.last + 11, text.length))
                            else "",
                            startPosition = matchResult.range.first.toLong(),
                            endPosition = matchResult.range.last.toLong(),
                            matcher = matcher
                        ))
                        startIndex = matchResult.range.last + 1
                    } else {
                        // Advance by 1 so overlapping candidates after a failed match are not skipped
                        startIndex = matchResult.range.first + 1
                    }
                }
            }
        }.distinct()
    }

    override fun close() {}
}