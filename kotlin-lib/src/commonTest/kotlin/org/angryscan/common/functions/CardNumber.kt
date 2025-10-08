package org.angryscan.common.functions

import kotlin.test.Test
import kotlin.test.assertEquals

internal class CardNumberTest {
    @Test
    fun mask() {
        assertEquals(
            "123456******4567",
            CardNumber.mask("1234567891234567")
        )
        assertEquals(
            "1234 56** **** 4567",
            CardNumber.mask("1234 5678 9123 4567")
        )
        assertEquals(
            " 1234 56** **** 4567",
            CardNumber.mask(" 1234 5678 9123 4567")
        )
        assertEquals(
            "1234 56** **** 4567 ",
            CardNumber.mask("1234 5678 9123 4567 ")
        )
        assertEquals(
            ",1234 56** **** 4567",
            CardNumber.mask(",1234 5678 9123 4567")
        )
    }
}