package org.angryscan.common.extensions

import org.angryscan.common.matchers.CardNumber
import kotlin.test.Test
import kotlin.test.assertTrue

internal class MatchersTest {
    @Test
    fun testContains() {
        assertTrue(Matchers.contains(CardNumber()))
    }
}