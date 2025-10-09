package org.angryscan.common.engine

interface IScanEngine {
    fun scan(text: String): List<Match>
}