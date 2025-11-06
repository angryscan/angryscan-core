package detectfunctions

import org.angryscan.common.engine.kotlin.KotlinEngine
import org.angryscan.common.matchers.Address
import org.angryscan.common.matchers.CVV
import org.angryscan.common.matchers.CardNumber
import org.angryscan.common.matchers.Email
import org.angryscan.common.matchers.FullName
import org.angryscan.common.matchers.INN
import org.angryscan.common.matchers.IPv4
import org.angryscan.common.matchers.IPv6
import org.angryscan.common.matchers.Login
import org.angryscan.common.matchers.OMS
import org.angryscan.common.matchers.Passport
import org.angryscan.common.matchers.Password
import org.angryscan.common.matchers.Phone
import org.angryscan.common.matchers.SNILS

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
fun detectAddress(text: String): Int {
    return KotlinEngine(listOf(Address)).scan(text).count()
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
    return KotlinEngine(listOf(IPv4)).scan(text).count()
}

@Suppress("Unused")
fun detectIPv6(text: String): Int {
    return KotlinEngine(listOf(IPv6)).scan(text).count()
}
