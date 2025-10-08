package org.angryscan.common.engine

import org.angryscan.common.extensions.Match

interface IScanEngine {
    fun scan(text: String): List<Match>
}