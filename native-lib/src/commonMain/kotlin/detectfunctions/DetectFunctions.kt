package detectfunctions

import info.downdetector.bigdatascanner.common.engine.KotlinEngine
import info.downdetector.bigdatascanner.common.functions.AccountNumber
import info.downdetector.bigdatascanner.common.functions.Address
import info.downdetector.bigdatascanner.common.functions.CVV
import info.downdetector.bigdatascanner.common.functions.CarNumber
import info.downdetector.bigdatascanner.common.functions.CardNumber
import info.downdetector.bigdatascanner.common.functions.Email
import info.downdetector.bigdatascanner.common.functions.FullName
import info.downdetector.bigdatascanner.common.functions.INN
import info.downdetector.bigdatascanner.common.functions.IP
import info.downdetector.bigdatascanner.common.functions.IPv6
import info.downdetector.bigdatascanner.common.functions.Login
import info.downdetector.bigdatascanner.common.functions.OMS
import info.downdetector.bigdatascanner.common.functions.Passport
import info.downdetector.bigdatascanner.common.functions.Password
import info.downdetector.bigdatascanner.common.functions.Phone
import info.downdetector.bigdatascanner.common.functions.SNILS
import info.downdetector.bigdatascanner.common.functions.ValuableInfo

@Suppress("Unused")
fun detectEmails(text: String): Int {
    return KotlinEngine(listOf(Email)).scan(text).count()
}

@Suppress("Unused")
fun detectPhones(text: String): Int {
    return KotlinEngine(listOf(Phone)).scan(text).count()
}

@Suppress("Unused")
fun detectCardNumbers(text: String): Int {
    return KotlinEngine(listOf(CardNumber)).scan(text).count()
}

@Suppress("Unused")
fun detectCarNumber(text: String): Int {
    return KotlinEngine(listOf(CarNumber)).scan(text).count()
}

@Suppress("Unused")
fun detectSINLS(text: String): Int {
    return KotlinEngine(listOf(SNILS)).scan(text).count()
}

@Suppress("Unused")
fun detectPassport(text: String): Int {
    return KotlinEngine(listOf(Passport)).scan(text).count()
}

@Suppress("Unused")
fun detectOMS(text: String): Int {
    return KotlinEngine(listOf(OMS)).scan(text).count()
}

@Suppress("Unused")
fun detectINN(text: String): Int {
    return KotlinEngine(listOf(INN)).scan(text).count()
}

@Suppress("Unused")
fun detectAccountNumber(text: String): Int {
    return KotlinEngine(listOf(AccountNumber)).scan(text).count()
}

@Suppress("Unused")
fun detectAddress(text: String): Int {
    return KotlinEngine(listOf(Address)).scan(text).count()
}

@Suppress("Unused")
fun detectValuableInfo(text: String): Int {
    return KotlinEngine(listOf(ValuableInfo)).scan(text).count()
}

@Suppress("Unused")
fun detectLogin(text: String): Int {
    return KotlinEngine(listOf(Login)).scan(text).count()
}

@Suppress("Unused")
fun detectPassword(text: String): Int {
    return KotlinEngine(listOf(Password)).scan(text).count()
}

@Suppress("Unused")
fun detectCVV(text: String): Int {
    return KotlinEngine(listOf(CVV)).scan(text).count()
}

@Suppress("Unused")
fun detectFullName(text: String): Int {
    return KotlinEngine(listOf(FullName)).scan(text).count()
}

@Suppress("Unused")
fun detectIP(text: String): Int {
    return KotlinEngine(listOf(IP)).scan(text).count()
}

@Suppress("Unused")
fun detectIPv6(text: String): Int {
    return KotlinEngine(listOf(IPv6)).scan(text).count()
}
