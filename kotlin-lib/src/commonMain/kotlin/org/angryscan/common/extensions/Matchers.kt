package org.angryscan.common.extensions

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.matchers.*

object Matchers: List<IMatcher> by listOf<IMatcher>(
    Address,
    BankAccount,
    BankAccountLE,
    BirthCert,
    Birthday,
    CadastralNumber,
    CardNumber(),
    CryptoWallet,
    CVV,
    DeathDate,
    DriverLicense,
    EducationDoc,
    EducationLevel,
    EducationLicense,
    Email,
    EpCertificateNumber,
    ExecDocNumber,
    RIN,
    FullName,
    FullNameUS,
    Geo,
    HashData,
    IdentityDocType,
    INN,
    InheritanceDoc,
    IPv4,
    IPv6,
    LegalEntityId,
    LegalEntityName,
    Login,
    MaritalStatus,
    MarriageCert,
    MedicareUS,
    MilitaryID,
    MilitaryRank,
    OGRNIP,
    OKPO,
    OMS,
    OSAGOPolicy,
    Passport,
    PassportUS,
    Password,
    Phone,
    PhoneUS,
    ResidencePermit,
    SberBook,
    SecurityAffiliation,
    SNILS,
    SocialUserId,
    SSN,
    StateRegContract,
    TemporaryID,
    UidContractBankBki,
    VIN,
    VehicleRegNumber,
) {
    override fun contains(element: IMatcher): Boolean {
        return this.any{
            it::class == element::class
        }
    }
}