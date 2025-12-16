package org.angryscan.common.extensions

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.matchers.*

object Matchers: List<IMatcher> by listOf<IMatcher>(
    Address,
    BankAccount,
    BankAccountLE,
    Birthday,
    CadastralNumber,
    Certificate,
    CardNumber(),
    CryptoWallet,
    CVV,
    DeathDate,
    DriverLicense,
    EducationDoc,
    EducationLevel,
    EducationLicense,
    Email,
    ExecDocNumber,
    RIN,
    FullName,
    FullNameUS,
    Geo,
    HashData,
    IdentityDocType,
    INN,
    IPv4,
    IPv6,
    LegalEntityId,
    LegalEntityName,
    Login,
    MaritalStatus,
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
    VIN,
    VehicleRegNumber,
) {
    override fun contains(element: IMatcher): Boolean {
        return this.any{
            it::class == element::class
        }
    }
}