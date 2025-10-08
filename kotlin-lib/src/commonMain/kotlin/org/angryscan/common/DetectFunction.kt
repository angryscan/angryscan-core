package org.angryscan.common

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.KotlinEngine
import org.angryscan.common.functions.Email
import org.angryscan.common.functions.Phone

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

    override fun scan(text: String): List<org.angryscan.common.extensions.Match> = when (this) {
        Emails -> KotlinEngine(listOf(Email))
            .scan(text)

        Phones -> KotlinEngine(listOf(Phone))
            .scan(text)

        CardNumbers -> KotlinEngine(listOf(org.angryscan.common.functions.CardNumber))
            .scan(text)

        CarNumber -> KotlinEngine(listOf(org.angryscan.common.functions.CarNumber))
            .scan(text)

        SNILS -> KotlinEngine(listOf(org.angryscan.common.functions.SNILS))
            .scan(text)

        Passport -> KotlinEngine(listOf(org.angryscan.common.functions.Passport))
            .scan(text)

        OMS -> KotlinEngine(listOf(org.angryscan.common.functions.OMS))
            .scan(text)

        INN -> KotlinEngine(listOf(org.angryscan.common.functions.INN))
            .scan(text)

        AccountNumber -> KotlinEngine(listOf(org.angryscan.common.functions.AccountNumber))
            .scan(text)

        Address -> KotlinEngine(listOf(org.angryscan.common.functions.Address))
            .scan(text)

        ValuableInfo -> KotlinEngine(listOf(org.angryscan.common.functions.ValuableInfo))
            .scan(text)

        Login -> KotlinEngine(listOf(org.angryscan.common.functions.Login))
            .scan(text)

        Password -> KotlinEngine(listOf(org.angryscan.common.functions.Password))
            .scan(text)

        CVV -> KotlinEngine(listOf(org.angryscan.common.functions.CVV))
            .scan(text)

        Name -> KotlinEngine(listOf(org.angryscan.common.functions.FullName))
            .scan(text)

        IP -> KotlinEngine(listOf(org.angryscan.common.functions.IP))
            .scan(text)

        IPv6 -> KotlinEngine(listOf(org.angryscan.common.functions.IPv6))
            .scan(text)
    }
}
