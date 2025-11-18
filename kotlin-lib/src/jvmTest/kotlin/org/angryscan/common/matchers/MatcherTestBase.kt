package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.assertEquals

abstract class MatcherTestBase(val matcher: IMatcher) {

    fun scanText(text: String): Int {

        val kotlinRes = KotlinEngine(listOf(matcher).filterIsInstance<IKotlinMatcher>()).use {
            it.scan(text)
        }
        val hyperRes = HyperScanEngine(listOf(matcher).filterIsInstance<IHyperMatcher>()).use {
            it.scan(text)
        }
        assertEquals(
            kotlinRes.count(),
            hyperRes.count(),
            "Количество совпадений для ${matcher.name} должно быть одинаковым для обоих движков. " +
                    "Kotlin: ${kotlinRes.count()}, Hyper: ${hyperRes.count()}\nText: $text"
        )

        return kotlinRes.count()
    }
}