package info.downdetector.bigdatascanner.common.extensions

import info.downdetector.bigdatascanner.common.engine.IMatcher

data class Match(
    val value: String,
    val before: String = "",
    val after: String = "",
    val startPosition: Long,
    val endPosition: Long,
    val matcher: IMatcher
)
