package info.downdetector.bigdatascanner.common

import info.downdetector.bigdatascanner.common.extensions.MatchWithContext
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
        Emails -> info.downdetector.bigdatascanner.common.functions.Email.find(text, withContext)

        Phones -> info.downdetector.bigdatascanner.common.functions.Phone.find(text, withContext)

        CardNumbers -> info.downdetector.bigdatascanner.common.functions.CardNumber.find(text, withContext)

        CarNumber -> info.downdetector.bigdatascanner.common.functions.CarNumber.find(text, withContext)

        SNILS -> info.downdetector.bigdatascanner.common.functions.SNILS.find(text, withContext)

        Passport -> info.downdetector.bigdatascanner.common.functions.Passport.find(text, withContext)

        OMS -> info.downdetector.bigdatascanner.common.functions.OMS.find(text, withContext)

        INN -> info.downdetector.bigdatascanner.common.functions.INN.find(text, withContext)

        AccountNumber -> info.downdetector.bigdatascanner.common.functions.AccountNumber.find(text, withContext)

        Address -> info.downdetector.bigdatascanner.common.functions.Address.find(text, withContext)

        ValuableInfo -> info.downdetector.bigdatascanner.common.functions.ValuableInfo.find(text, withContext)

        Login -> info.downdetector.bigdatascanner.common.functions.Login.find(text, withContext)

        Password -> info.downdetector.bigdatascanner.common.functions.Password.find(text, withContext)

        CVV -> info.downdetector.bigdatascanner.common.functions.CVV.find(text, withContext)

        Name -> info.downdetector.bigdatascanner.common.functions.FullName.find(text, withContext)

        IP -> info.downdetector.bigdatascanner.common.functions.IP.find(text, withContext)

        IPv6 -> info.downdetector.bigdatascanner.common.functions.IPv6.find(text, withContext)
    }
}
