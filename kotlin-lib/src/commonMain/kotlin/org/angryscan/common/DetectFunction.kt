package org.angryscan.common

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.Match
import org.angryscan.common.engine.kotlin.KotlinEngine

@Suppress("unused")
@Serializable(with = DetectFunctionSerializer::class)
@Deprecated("Use KotlinEngine with  instead")
enum class DetectFunction(override val writeName: String) : IDetectFunction {
    /** This class is responsible on detection and
     * extraction information from documents
     **/
    Emails("emails"),
    Phones("phones"),
    CardNumbers("card_numbers"),
    CarNumber("car_numbers"),
    SNILS("snilses"),
    Passport("passports"),
    OMS("omses"),
    INN("inns"),
    AccountNumber("account_number"),
    Address("address"),
    ValuableInfo("valuable_info"),
    Login("logins"),
    Password("passwords"),
    CVV("cvv"),
    Name("full_names"),
    IP("ips"),
    IPv6("ipv6s");

    override fun scan(text: String): List<Match> = when (this) {
        Emails -> KotlinEngine(listOf(org.angryscan.common.matchers.Email))
            .scan(text)

        Phones -> KotlinEngine(listOf(org.angryscan.common.matchers.Phone))
            .scan(text)

        CardNumbers -> KotlinEngine(listOf(org.angryscan.common.matchers.CardNumber()))
            .scan(text)

        CarNumber -> KotlinEngine(listOf(org.angryscan.common.matchers.CarNumber))
            .scan(text)

        SNILS -> KotlinEngine(listOf(org.angryscan.common.matchers.SNILS))
            .scan(text)

        Passport -> KotlinEngine(listOf(org.angryscan.common.matchers.Passport))
            .scan(text)

        OMS -> KotlinEngine(listOf(org.angryscan.common.matchers.OMS))
            .scan(text)

        INN -> KotlinEngine(listOf(org.angryscan.common.matchers.INN))
            .scan(text)

        AccountNumber -> KotlinEngine(listOf(org.angryscan.common.matchers.AccountNumber))
            .scan(text)

        Address -> KotlinEngine(listOf(org.angryscan.common.matchers.Address))
            .scan(text)

        ValuableInfo -> KotlinEngine(listOf(org.angryscan.common.matchers.ValuableInfo))
            .scan(text)

        Login -> KotlinEngine(listOf(org.angryscan.common.matchers.Login))
            .scan(text)

        Password -> KotlinEngine(listOf(org.angryscan.common.matchers.Password))
            .scan(text)

        CVV -> KotlinEngine(listOf(org.angryscan.common.matchers.CVV))
            .scan(text)

        Name -> KotlinEngine(listOf(org.angryscan.common.matchers.FullName))
            .scan(text)

        IP -> KotlinEngine(listOf(org.angryscan.common.matchers.IP))
            .scan(text)

        IPv6 -> KotlinEngine(listOf(org.angryscan.common.matchers.IPv6))
            .scan(text)
    }
}
