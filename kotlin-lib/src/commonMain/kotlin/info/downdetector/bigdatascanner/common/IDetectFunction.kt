package info.downdetector.bigdatascanner.common

import info.downdetector.bigdatascanner.common.extensions.Match

@Deprecated("Use KotlinEngine instead")
interface IDetectFunction {
    val name: String
    val writeName: String

    fun scan(text: String): List<Match>
}