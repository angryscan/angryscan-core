package org.angryscan.common.engine

interface IScanEngine {
    val matchers: List<IMatcher>
    fun scan(text: String): List<Match>
}