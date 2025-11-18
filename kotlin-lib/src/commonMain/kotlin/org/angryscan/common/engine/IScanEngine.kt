package org.angryscan.common.engine

interface IScanEngine: AutoCloseable {
    val matchers: List<IMatcher>
    fun scan(text: String): List<Match>

    // Deduplicate overlapping matches that start at the same position for the same matcher.
    // Prefer the longest end position to align with Kotlin regex greedy behavior.
    fun List<Match>.distinct(): List<Match> =
        this.groupBy { Pair(it.matcher, it.startPosition) }
            .map { (_, group) -> group.maxBy { it.endPosition } }
}