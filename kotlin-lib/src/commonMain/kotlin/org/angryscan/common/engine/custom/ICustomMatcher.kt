package org.angryscan.common.engine.custom

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.Match

interface ICustomMatcher: IMatcher {
    fun scan(text: String): List<Match>
}