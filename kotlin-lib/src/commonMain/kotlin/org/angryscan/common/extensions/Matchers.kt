package org.angryscan.common.extensions

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.matchers.*

object Matchers: List<IMatcher> by listOf<IMatcher>(
    AccountNumber,
    Address,
    CardNumber(),
    CarNumber,
    CVV,
    Email,
    FullName,
    INN,
    IPv4,
    IPv6,
    Login,
    OMS,
    Passport,
    Password,
    Phone,
    SNILS,
) {
    override fun contains(element: IMatcher): Boolean {
        return this.any{
            it::class == element::class
        }
    }
}