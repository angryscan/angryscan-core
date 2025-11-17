package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals

internal class CardNumberTests {
    @Test
    fun mask() {
        val cardNumber = CardNumber()
        assertEquals(
            "123456******3456",
            cardNumber.mask("1234567890123456")
        )
        assertEquals(
            " 123456******3456",
            cardNumber.mask(" 1234567890123456")
        )
        assertEquals(
            " 123456******3456,",
            cardNumber.mask(" 1234567890123456,")
        )
        assertEquals(
            "1234 56** **** 3456",
            cardNumber.mask("1234 5678 9012 3456")
        )
    }
}