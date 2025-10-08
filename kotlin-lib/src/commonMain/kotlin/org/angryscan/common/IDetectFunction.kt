package org.angryscan.common

import org.angryscan.common.extensions.Match

@Deprecated("Use KotlinEngine instead")
interface IDetectFunction {
    val name: String
    val writeName: String

    fun scan(text: String): List<Match>
}