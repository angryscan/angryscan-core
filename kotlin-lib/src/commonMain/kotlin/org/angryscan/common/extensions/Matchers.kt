package org.angryscan.common.extensions

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.matchers.*

object Matchers: List<IMatcher> by listOf<IMatcher>(
    AccountNumber,
    Address,
    BankAccount,
    BankAccountLE,
    BirthCert,
    Birthday,
    CadastralNumber,
    CardNumber(),
    CarNumber,
    CVV,
    DeathDate,
    DriverLicense,
    EducationDoc,
    EducationLevel,
    EducationLicense,
    Email,
    EpCertificateNumber,
    ExecDocNumber,
    ForeignPassports,
    ForeignTIN,
    FullName,
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
    MilitaryID,
    MilitaryRank,
    OGRNIP,
    OKPO,
    OMS,
    OSAGOPolicy,
    Passport,
    Password,
    Phone,
    RefugeeCert,
    ResidencePermit,
    SberBook,
    SecurityAffiliation,
    SNILS,
    SocialUserId,
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