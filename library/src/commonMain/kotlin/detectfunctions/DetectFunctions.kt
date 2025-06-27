package detectfunctions

import info.downdetector.bigdatascanner.common.DetectFunction


fun detectEmails(text: String): Int {
    return DetectFunction.Emails.scan(text).count()
}

fun detectPhones(text: String): Int {
    return DetectFunction.Phones.scan(text).count()
}

fun detectCardNumbers(text: String): Int {
    return DetectFunction.CardNumbers.scan(text).count()
}

fun detectCarNumber(text: String): Int {
    return DetectFunction.CarNumber.scan(text).count()
}

fun detectSINLS(text: String): Int {
    return DetectFunction.SNILS.scan(text).count()
}

fun detectPassport(text: String): Int {
    return DetectFunction.Passport.scan(text).count()
}

fun detectOMS(text: String): Int {
    return DetectFunction.OMS.scan(text).count()
}

fun detectINN(text: String): Int {
    return DetectFunction.INN.scan(text).count()
}

fun detectAccountNumber(text: String): Int {
    return DetectFunction.AccountNumber.scan(text).count()
}

fun detectAddress(text: String): Int {
    return DetectFunction.Address.scan(text).count()
}

fun detectValuableInfo(text: String): Int {
    return DetectFunction.ValuableInfo.scan(text).count()
}

fun detectLogin(text: String): Int {
    return DetectFunction.Login.scan(text).count()
}

fun detectPassword(text: String): Int {
    return DetectFunction.Password.scan(text).count()
}

fun detectCVV(text: String): Int {
    return DetectFunction.CVV.scan(text).count()
}

fun detectName(text: String): Int {
    return DetectFunction.Name.scan(text).count()
}

fun detectIP(text: String): Int {
    return DetectFunction.IP.scan(text).count()
}

fun detectIPv6(text: String): Int {
    return DetectFunction.IPv6.scan(text).count()
}
