package info.downdetector.bigdatascanner.common

import info.downdetector.bigdatascanner.common.extensions.MatchWithContext
import info.downdetector.bigdatascanner.common.functions.findAccountNumbers
import info.downdetector.bigdatascanner.common.functions.findAddresses
import info.downdetector.bigdatascanner.common.functions.findBankAccount
import info.downdetector.bigdatascanner.common.functions.findBankAccountLE
import info.downdetector.bigdatascanner.common.functions.findBirthCert
import info.downdetector.bigdatascanner.common.functions.findBirthday
import info.downdetector.bigdatascanner.common.functions.findCVVs
import info.downdetector.bigdatascanner.common.functions.findCadastralNumber
import info.downdetector.bigdatascanner.common.functions.findCarNumbers
import info.downdetector.bigdatascanner.common.functions.findCardNumbers
import info.downdetector.bigdatascanner.common.functions.findDeathDate
import info.downdetector.bigdatascanner.common.functions.findDriverLicense
import info.downdetector.bigdatascanner.common.functions.findEducationDoc
import info.downdetector.bigdatascanner.common.functions.findEducationLevel
import info.downdetector.bigdatascanner.common.functions.findEducationLicense
import info.downdetector.bigdatascanner.common.functions.findEmails
import info.downdetector.bigdatascanner.common.functions.findEpCertificateNumber
import info.downdetector.bigdatascanner.common.functions.findExecDocNumber
import info.downdetector.bigdatascanner.common.functions.findForeignPassports
import info.downdetector.bigdatascanner.common.functions.findForeignTIN
import info.downdetector.bigdatascanner.common.functions.findGeo
import info.downdetector.bigdatascanner.common.functions.findHashData
import info.downdetector.bigdatascanner.common.functions.findINN
import info.downdetector.bigdatascanner.common.functions.findIPs
import info.downdetector.bigdatascanner.common.functions.findIPv6s
import info.downdetector.bigdatascanner.common.functions.findIdentityDocType
import info.downdetector.bigdatascanner.common.functions.findInheritanceDoc
import info.downdetector.bigdatascanner.common.functions.findLegalEntityId
import info.downdetector.bigdatascanner.common.functions.findLegalEntityName
import info.downdetector.bigdatascanner.common.functions.findLogins
import info.downdetector.bigdatascanner.common.functions.findMaritalStatus
import info.downdetector.bigdatascanner.common.functions.findMarriageCert
import info.downdetector.bigdatascanner.common.functions.findMilitaryID
import info.downdetector.bigdatascanner.common.functions.findMilitaryRank
import info.downdetector.bigdatascanner.common.functions.findNames
import info.downdetector.bigdatascanner.common.functions.findOGRNIP
import info.downdetector.bigdatascanner.common.functions.findOKPO
import info.downdetector.bigdatascanner.common.functions.findOMS
import info.downdetector.bigdatascanner.common.functions.findOSAGOPolicy
import info.downdetector.bigdatascanner.common.functions.findPassports
import info.downdetector.bigdatascanner.common.functions.findPasswords
import info.downdetector.bigdatascanner.common.functions.findPhones
import info.downdetector.bigdatascanner.common.functions.findRefugeeCert
import info.downdetector.bigdatascanner.common.functions.findResidencePermit
import info.downdetector.bigdatascanner.common.functions.findSNILS
import info.downdetector.bigdatascanner.common.functions.findSberBook
import info.downdetector.bigdatascanner.common.functions.findSecurityAffiliation
import info.downdetector.bigdatascanner.common.functions.findSocialUserId
import info.downdetector.bigdatascanner.common.functions.findStateRegContract
import info.downdetector.bigdatascanner.common.functions.findTemporaryID
import info.downdetector.bigdatascanner.common.functions.findUidContractBank
import info.downdetector.bigdatascanner.common.functions.findVIN
import info.downdetector.bigdatascanner.common.functions.findValuableInfo
import info.downdetector.bigdatascanner.common.functions.findVehicleRegNumber
import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable(with = DetectFunctionSerializer::class)
enum class DetectFunction(
    override val writeName: String,
    private val scanFunction: (String, Boolean) -> Sequence<MatchWithContext>
) : IDetectFunction {
    Emails("Emails", ::findEmails),
    Phones("Phones", ::findPhones),
    CardNumbers("CardNumbers", ::findCardNumbers),
    CarNumber("CarNumber", ::findCarNumbers),
    SNILS("SNILS", ::findSNILS),
    Passport("Passport", ::findPassports),
    OMS("OMS", ::findOMS),
    INN("INN", ::findINN),
    AccountNumber("AccountNumber", ::findAccountNumbers),
    Address("Address", ::findAddresses),
    ValuableInfo("ValuableInfo", ::findValuableInfo),
    Login("Login", ::findLogins),
    Password("Password", ::findPasswords),
    CVV("CVV", ::findCVVs),
    Name("Name", ::findNames),
    IP("IP", ::findIPs),
    IPv6("IPv6", ::findIPv6s),
    Birthday("Birthday", ::findBirthday),
    DeathDate("DeathDate", ::findDeathDate),
    BirthCert("BirthCert", ::findBirthCert),
    ForeignPassport("ForeignPassport", ::findForeignPassports),
    RefugeeCert("RefugeeCert", ::findRefugeeCert),
    ResidencePermit("ResidencePermit", ::findResidencePermit),
    TemporaryID("TemporaryID", ::findTemporaryID),
    MilitaryID("MilitaryID", ::findMilitaryID),
    DriverLicense("DriverLicense", ::findDriverLicense),
    ForeignTIN("ForeignTIN", ::findForeignTIN),
    EducationDoc("EducationDoc", ::findEducationDoc),
    MarriageCert("MarriageCert", ::findMarriageCert),
    InheritanceDoc("InheritanceDoc", ::findInheritanceDoc),
    OGRNIP("OGRNIP", ::findOGRNIP),
    OSAGOPolicy("OSAGOPolicy", ::findOSAGOPolicy),
    SecurityAffiliation("SecurityAffiliation", ::findSecurityAffiliation),
    MilitaryRank("MilitaryRank", ::findMilitaryRank),
    EpCertificateNumber("EpCertificateNumber", ::findEpCertificateNumber),
    CadastralNumber("CadastralNumber", ::findCadastralNumber),
    VIN("VIN", ::findVIN),
    VehicleRegNumber("VehicleRegNumber", ::findVehicleRegNumber),
    SocialUserId("SocialUserId", ::findSocialUserId),
    LegalEntityName("LegalEntityName", ::findLegalEntityName),
    LegalEntityId("LegalEntityId", ::findLegalEntityId),
    OKPO("OKPO", ::findOKPO),
    StateRegContract("StateRegContract", ::findStateRegContract),
    UidContractBank("UidContractBank", ::findUidContractBank),
    ExecDocNumber("ExecDocNumber", ::findExecDocNumber),
    BankAccount("BankAccount", ::findBankAccount),
    SberBook("SberBook", ::findSberBook),
    BankAccountLE("BankAccountLE", ::findBankAccountLE),
    HashData("HashData", ::findHashData),
    Geo("Geo", ::findGeo),
    EducationLicense("EducationLicense", ::findEducationLicense),
    IdentityDocType("IdentityDocType", ::findIdentityDocType),
    MaritalStatus("MaritalStatus", ::findMaritalStatus),
    EducationLevel("EducationLevel", ::findEducationLevel),;


    override fun scan(text: String, withContext: Boolean): Sequence<MatchWithContext> =
        scanFunction(text, withContext)
}
