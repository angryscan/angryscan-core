package info.downdetector.bigdatascanner.common

import info.downdetector.bigdatascanner.common.extensions.MatchWithContext
import info.downdetector.bigdatascanner.common.functions.findAccountNumbers
import info.downdetector.bigdatascanner.common.functions.findAddresses
import info.downdetector.bigdatascanner.common.functions.findCVVs
import info.downdetector.bigdatascanner.common.functions.findCarNumbers
import info.downdetector.bigdatascanner.common.functions.findCardNumbers
import info.downdetector.bigdatascanner.common.functions.findEmails
import info.downdetector.bigdatascanner.common.functions.findINN
import info.downdetector.bigdatascanner.common.functions.findIPs
import info.downdetector.bigdatascanner.common.functions.findIPv6s
import info.downdetector.bigdatascanner.common.functions.findLogins
import info.downdetector.bigdatascanner.common.functions.findNames
import info.downdetector.bigdatascanner.common.functions.findOMS
import info.downdetector.bigdatascanner.common.functions.findPassports
import info.downdetector.bigdatascanner.common.functions.findPasswords
import info.downdetector.bigdatascanner.common.functions.findPhones
import info.downdetector.bigdatascanner.common.functions.findSNILS
import info.downdetector.bigdatascanner.common.functions.findValuableInfo
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
        Emails -> findEmails(text, withContext)

        Phones -> findPhones(text, withContext)

        CardNumbers -> findCardNumbers(text, withContext)

        CarNumber -> findCarNumbers(text, withContext)

        SNILS -> findSNILS(text, withContext)

        Passport -> findPassports(text, withContext)

        OMS -> findOMS(text, withContext)

        INN -> findINN(text, withContext)

        AccountNumber -> findAccountNumbers(text, withContext)

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
