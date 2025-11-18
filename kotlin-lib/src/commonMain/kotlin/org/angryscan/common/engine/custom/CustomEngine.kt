package org.angryscan.common.engine.custom

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.IScanEngine
import org.angryscan.common.engine.Match

@Suppress("Unused")
@Serializable
class CustomEngine(override val matchers: List<ICustomMatcher>) : IScanEngine {
    override fun scan(text: String): List<Match> {
        return matchers.flatMap { matcher ->
            matcher.scan(text)
        }
    }

    override fun close() {}

}