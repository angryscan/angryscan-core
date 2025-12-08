package detectfunctions

import org.angryscan.common.engine.kotlin.KotlinEngine
import org.angryscan.common.matchers.*

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

@Suppress("Unused")
fun detectBankAccount(text: String): Int {
    return KotlinEngine(listOf(BankAccount)).scan(text).count()
}

@Suppress("Unused")
fun detectBankAccountLE(text: String): Int {
    return KotlinEngine(listOf(BankAccountLE)).scan(text).count()
}

@Suppress("Unused")
fun detectBirthCert(text: String): Int {
    return KotlinEngine(listOf(Certificate)).scan(text).count()
}

@Suppress("Unused")
fun detectBirthday(text: String): Int {
    return KotlinEngine(listOf(Birthday)).scan(text).count()
}

@Suppress("Unused")
fun detectCadastralNumber(text: String): Int {
    return KotlinEngine(listOf(CadastralNumber)).scan(text).count()
}

@Suppress("Unused")
fun detectDeathDate(text: String): Int {
    return KotlinEngine(listOf(DeathDate)).scan(text).count()
}

@Suppress("Unused")
fun detectDriverLicense(text: String): Int {
    return KotlinEngine(listOf(DriverLicense)).scan(text).count()
}

@Suppress("Unused")
fun detectEducationDoc(text: String): Int {
    return KotlinEngine(listOf(EducationDoc)).scan(text).count()
}

@Suppress("Unused")
fun detectEducationLevel(text: String): Int {
    return KotlinEngine(listOf(EducationLevel)).scan(text).count()
}

@Suppress("Unused")
fun detectEducationLicense(text: String): Int {
    return KotlinEngine(listOf(EducationLicense)).scan(text).count()
}

@Suppress("Unused")
fun detectExecDocNumber(text: String): Int {
    return KotlinEngine(listOf(ExecDocNumber)).scan(text).count()
}

@Suppress("Unused")
fun detectRIN(text: String): Int {
    return KotlinEngine(listOf(RIN)).scan(text).count()
}

@Suppress("Unused")
fun detectFullNameUS(text: String): Int {
    return KotlinEngine(listOf(FullNameUS)).scan(text).count()
}

@Suppress("Unused")
fun detectGeo(text: String): Int {
    return KotlinEngine(listOf(Geo)).scan(text).count()
}

@Suppress("Unused")
fun detectHashData(text: String): Int {
    return KotlinEngine(listOf(HashData)).scan(text).count()
}

@Suppress("Unused")
fun detectIdentityDocType(text: String): Int {
    return KotlinEngine(listOf(IdentityDocType)).scan(text).count()
}

@Suppress("Unused")
fun detectLegalEntityId(text: String): Int {
    return KotlinEngine(listOf(LegalEntityId)).scan(text).count()
}

@Suppress("Unused")
fun detectLegalEntityName(text: String): Int {
    return KotlinEngine(listOf(LegalEntityName)).scan(text).count()
}

@Suppress("Unused")
fun detectMaritalStatus(text: String): Int {
    return KotlinEngine(listOf(MaritalStatus)).scan(text).count()
}

@Suppress("Unused")
fun detectMarriageCert(text: String): Int {
    return KotlinEngine(listOf(Certificate)).scan(text).count()
}

@Suppress("Unused")
fun detectMedicareUS(text: String): Int {
    return KotlinEngine(listOf(MedicareUS)).scan(text).count()
}

@Suppress("Unused")
fun detectMilitaryID(text: String): Int {
    return KotlinEngine(listOf(MilitaryID)).scan(text).count()
}

@Suppress("Unused")
fun detectMilitaryRank(text: String): Int {
    return KotlinEngine(listOf(MilitaryRank)).scan(text).count()
}

@Suppress("Unused")
fun detectOGRNIP(text: String): Int {
    return KotlinEngine(listOf(OGRNIP)).scan(text).count()
}

@Suppress("Unused")
fun detectOKPO(text: String): Int {
    return KotlinEngine(listOf(OKPO)).scan(text).count()
}

@Suppress("Unused")
fun detectOSAGOPolicy(text: String): Int {
    return KotlinEngine(listOf(OSAGOPolicy)).scan(text).count()
}

@Suppress("Unused")
fun detectPassportUS(text: String): Int {
    return KotlinEngine(listOf(PassportUS)).scan(text).count()
}

@Suppress("Unused")
fun detectPhoneUS(text: String): Int {
    return KotlinEngine(listOf(PhoneUS)).scan(text).count()
}

@Suppress("Unused")
fun detectResidencePermit(text: String): Int {
    return KotlinEngine(listOf(ResidencePermit)).scan(text).count()
}

@Suppress("Unused")
fun detectSberBook(text: String): Int {
    return KotlinEngine(listOf(SberBook)).scan(text).count()
}

@Suppress("Unused")
fun detectSecurityAffiliation(text: String): Int {
    return KotlinEngine(listOf(SecurityAffiliation)).scan(text).count()
}

@Suppress("Unused")
fun detectSocialUserId(text: String): Int {
    return KotlinEngine(listOf(SocialUserId)).scan(text).count()
}

@Suppress("Unused")
fun detectSSN(text: String): Int {
    return KotlinEngine(listOf(SSN)).scan(text).count()
}

@Suppress("Unused")
fun detectStateRegContract(text: String): Int {
    return KotlinEngine(listOf(StateRegContract)).scan(text).count()
}

@Suppress("Unused")
fun detectVIN(text: String): Int {
    return KotlinEngine(listOf(VIN)).scan(text).count()
}

@Suppress("Unused")
fun detectVehicleRegNumber(text: String): Int {
    return KotlinEngine(listOf(VehicleRegNumber)).scan(text).count()
}
