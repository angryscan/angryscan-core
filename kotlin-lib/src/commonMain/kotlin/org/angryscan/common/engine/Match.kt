package org.angryscan.common.engine

data class Match(
    val value: String,
    val before: String = "",
    val after: String = "",
    val startPosition: Long,
    val endPosition: Long,
    val matcher: IMatcher
)