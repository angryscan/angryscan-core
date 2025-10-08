package org.angryscan.common.extensions

import org.angryscan.common.engine.IMatcher

data class Match(
    val value: String,
    val before: String = "",
    val after: String = "",
    val startPosition: Long,
    val endPosition: Long,
    val matcher: IMatcher
)
