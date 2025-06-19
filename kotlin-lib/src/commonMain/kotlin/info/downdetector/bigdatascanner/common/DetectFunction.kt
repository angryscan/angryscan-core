package info.downdetector.bigdatascanner.common

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

    override fun scan(text: String): Sequence<String> = when (this) {
        Emails -> findEmails(text)

        Phones -> findPhones(text)

        CardNumbers -> findCardNumbers(text)

        CarNumber -> findCarNumbers(text)

        SNILS -> findSNILS(text)

        Passport -> findPassports(text)

        OMS -> findOMS(text)

        INN -> findINN(text)

        AccountNumber -> findAccountNumbers(text)

        Address -> findAddresses(text)

        ValuableInfo -> findValuableInfo(text)

        Login -> findLogins(text)

        Password -> findPasswords(text)

        CVV -> findCVVs(text)

        Name -> findNames(text)

        IP -> findIPs(text)

        IPv6 -> findIPv6s(text)
    }
}
