package org.angryscan.common.matchers

import org.angryscan.common.engine.EngineTest.Companion.getCountOfAttribute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class OMSTest {
    @Test
    fun fiveTest() {
        val file = javaClass.getResource("/testFiles/5.csv")?.file
        assertNotNull(file)
        assertEquals(1, getCountOfAttribute(file, OMS))
    }
}