package info.downdetector.bigdatascanner.common.extensions

import info.downdetector.bigdatascanner.common.functions.IHyperPattern

data class HyperMatch(
    val value: String,
    val startPosition: Long,
    val endPosition: Long,
    val matcher: IHyperPattern
)
