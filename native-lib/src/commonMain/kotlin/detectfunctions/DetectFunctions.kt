package detectfunctions

import org.angryscan.common.engine.KotlinEngine
import org.angryscan.common.functions.AccountNumber
import org.angryscan.common.functions.Address
import org.angryscan.common.functions.CVV
import org.angryscan.common.functions.CarNumber
import org.angryscan.common.functions.CardNumber
import org.angryscan.common.functions.Email
import org.angryscan.common.functions.FullName
import org.angryscan.common.functions.INN
import org.angryscan.common.functions.IP
import org.angryscan.common.functions.IPv6
import org.angryscan.common.functions.Login
import org.angryscan.common.functions.OMS
import org.angryscan.common.functions.Passport
import org.angryscan.common.functions.Password
import org.angryscan.common.functions.Phone
import org.angryscan.common.functions.SNILS
import org.angryscan.common.functions.ValuableInfo

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
    return KotlinEngine(listOf(CardNumber())).scan(text).count()
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
