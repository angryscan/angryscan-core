package org.angryscan.common.extensions

import org.angryscan.common.matchers.*

object MatchersRegister {
    val matchers = listOf(
        AccountNumber,
        Address,
        CardNumber(),
        CarNumber,
        CVV,
        Email,
        FullName,
        INN,
        IP,
        IPv6,
        Login,
        OMS,
        Passport,
        Password,
        Phone,
        SNILS,
    )
}