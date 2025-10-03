package info.downdetector.bigdatascanner.common

import info.downdetector.bigdatascanner.common.extensions.MatchWithContext
import info.downdetector.bigdatascanner.common.functions.findAccountNumbers
import info.downdetector.bigdatascanner.common.functions.findAddresses
import info.downdetector.bigdatascanner.common.functions.findBirthCert
import info.downdetector.bigdatascanner.common.functions.findBirthday
import info.downdetector.bigdatascanner.common.functions.findCVVs
import info.downdetector.bigdatascanner.common.functions.findCarNumbers
import info.downdetector.bigdatascanner.common.functions.findCardNumbers
import info.downdetector.bigdatascanner.common.functions.findDeathDate
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
enum class DetectFunction(
    override val writeName: String
) : IDetectFunction {
    /** This class is responsible on detection and
     * extraction information from documents
     **/
    Emails("Emails"),
    Phones("Phones"),
    CardNumbers("CardNumbers"),
    CarNumber("CarNumber"),
    SNILS("SNILS"),
    Passport("Passport"),
    OMS("OMS"),
    INN("INN"),
    AccountNumber("AccountNumber"),
    Address("Address"),
    ValuableInfo("ValuableInfo"),
    Login("Login"),
    Password("Password"),
    CVV("CVV"),
    Name("Name"),
    IP("IP"),
    IPv6("IPv6"),
    Birthday("Birthday"),
    DeathDate("DeathDate"),
    BirthCert("BirthCert");

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

        Birthday -> findBirthday(text, withContext)

        DeathDate -> findDeathDate(text, withContext)

        BirthCert -> findBirthCert(text, withContext)
    }
}
