package info.downdetector.bigdatascanner.common

import info.downdetector.bigdatascanner.common.extensions.MatchWithContext
import info.downdetector.bigdatascanner.common.functions.*
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable(with = DetectFunctionSerializer::class)
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

    override fun scan(text: String, withContext: Boolean): Sequence<MatchWithContext> = when (this) {
        Emails -> Email.find(text, withContext)

        Phones -> Phone.find(text, withContext)

        CardNumbers -> CardNumber.find(text, withContext)

        CarNumber -> info.downdetector.bigdatascanner.common.functions.CarNumber.find(text, withContext)

        SNILS -> info.downdetector.bigdatascanner.common.functions.SNILS.find(text, withContext)

        Passport -> findPassports(text, withContext)

        OMS -> findOMS(text, withContext)

        INN -> findINN(text, withContext)

        AccountNumber -> info.downdetector.bigdatascanner.common.functions.AccountNumber.find(text, withContext)

        Address -> findAddresses(text, withContext)

        ValuableInfo -> findValuableInfo(text, withContext)

        Login -> findLogins(text, withContext)

        Password -> findPasswords(text, withContext)

        CVV -> findCVVs(text, withContext)

        Name -> findNames(text, withContext)

        IP -> findIPs(text, withContext)

        IPv6 -> findIPv6s(text, withContext)
    }
}
