package info.downdetector.bigdatascanner.common

import info.downdetector.bigdatascanner.common.extensions.MatchWithContext

interface IDetectFunction {
    val name: String
    val writeName: String

    fun scan(text: String, withContext: Boolean = true): Sequence<MatchWithContext>
}