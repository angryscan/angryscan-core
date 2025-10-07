package info.downdetector.bigdatascanner.common.engine

import info.downdetector.bigdatascanner.common.extensions.Match

interface IScanEngine {
    fun scan(text: String): List<Match>
}