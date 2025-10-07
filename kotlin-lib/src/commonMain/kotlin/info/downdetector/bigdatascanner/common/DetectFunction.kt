package info.downdetector.bigdatascanner.common

import info.downdetector.bigdatascanner.common.engine.KotlinEngine
import info.downdetector.bigdatascanner.common.extensions.Match
import kotlinx.serialization.Serializable

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
        Emails -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.Email)).scan(text)

        Phones -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.Phone)).scan(text)

        CardNumbers -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.CardNumber)).scan(text)

        CarNumber -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.CarNumber)).scan(text)

        SNILS -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.SNILS)).scan(text)

        Passport -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.Passport)).scan(text)

        OMS -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.OMS)).scan(text)

        INN -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.INN)).scan(text)

        AccountNumber -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.AccountNumber)).scan(text)

        Address -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.Address)).scan(text)

        ValuableInfo -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.ValuableInfo)).scan(text)

        Login -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.Login)).scan(text)

        Password -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.Password)).scan(text)

        CVV -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.CVV)).scan(text)

        Name -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.FullName)).scan(text)

        IP -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.IP)).scan(text)

        IPv6 -> KotlinEngine(listOf(info.downdetector.bigdatascanner.common.functions.IPv6)).scan(text)
    }
}
