package info.downdetector.bigdatascanner.common.extensions

data class MatchWithContext(
    val value: String,
    val before: String = "",
    val after: String = ""
)
