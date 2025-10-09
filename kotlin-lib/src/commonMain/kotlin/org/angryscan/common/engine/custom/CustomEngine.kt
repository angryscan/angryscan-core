package org.angryscan.common.engine.custom

import org.angryscan.common.engine.IScanEngine
import org.angryscan.common.engine.Match

@Suppress("Unused")
class CustomEngine(val customMatchers: List<ICustomMatcher>): IScanEngine {
    override fun scan(text: String): List<Match> {
        return customMatchers.flatMap { matcher ->
            matcher.scan(text)
        }
    }
}