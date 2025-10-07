package info.downdetector.bigdatascanner.common.engine

import info.downdetector.bigdatascanner.common.extensions.Match
import kotlinx.serialization.Serializable

@Serializable
class KotlinEngine(val patterns: List<IKotlinMatcher>) : IScanEngine {
    override fun scan(text: String): List<Match> {
        return patterns.flatMap { pattern ->
            regexDetector(
                text,
                pattern
            )
        }
    }

    fun regexDetector(text: String, pattern: IKotlinMatcher): List<Match> {
        return pattern
            .javaPatterns
            .flatMap { str ->
                str.toRegex(
                    pattern.regexOptions
                ).findAll(text)
                    .filter { pattern.check(it.value) }
                    .map { match ->
                        Match(
                            value = match.value,
                            before = text.substring(
                                maxOf(0, match.range.first - 10),
                                match.range.first
                            ),
                            after = "$text ".substring(
                                match.range.last + 1,
                                minOf(match.range.last + 11, text.length)
                            ),
                            startPosition = match.range.first.toLong(),
                            endPosition = match.range.last.toLong(),
                            matcher = pattern
                        )
                    }
            }

    }
}