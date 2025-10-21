package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object LegalEntityId : IHyperMatcher, IKotlinMatcher {
    override val name = "Legal Entity ID"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:идентификатор\s+ЮЛ|код\s+LEI|LEI|SWIFT-код|BIC|идентификатор\s+юридического\s+лица)?
        \s*[:\-]?\s*
        (?:
          ([A-Z0-9]{4}0{2}[A-Z0-9]{12}[0-9]{2})|
          ([A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?)
        )
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n])(?:[A-Z0-9]{4}0{2}[A-Z0-9]{12}[0-9]{2}|[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}(?:[A-Z0-9]{3})?)(?:[\s\r\n]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        if (value.any { it.isLetter() && it.isLowerCase() }) return false

        if (value.any { !it.isLetterOrDigit() && !it.isWhitespace() }) return false
        
        val cleanValue = value.trim().replace(" ", "")
        
        if (cleanValue.length == 20) {
            var remainder = 0
            for (ch in cleanValue) {
                val token = if (ch.isDigit()) ch.toString() else ((ch - 'A') + 10).toString()
                for (digitChar in token) {
                    remainder = (remainder * 10 + (digitChar - '0')) % 97
                }
            }
            if (remainder == 1) return true
        }
        
        if (cleanValue.length == 8 || cleanValue.length == 11) {
            if (!cleanValue.substring(0, 4).all { it.isLetter() }) return false
            
            val countryCodes = setOf("AD", "AE", "AF", "AG", "AI", "AL", "AM", "AO", "AQ", "AR", "AS", "AT", "AU", "AW", "AX", "AZ", "BA", "BB", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BL", "BM", "BN", "BO", "BQ", "BR", "BS", "BT", "BV", "BW", "BY", "BZ", "CA", "CC", "CD", "CF", "CG", "CH", "CI", "CK", "CL", "CM", "CN", "CO", "CR", "CU", "CV", "CW", "CX", "CY", "CZ", "DE", "DJ", "DK", "DM", "DO", "DZ", "EC", "EE", "EG", "EH", "ER", "ES", "ET", "FI", "FJ", "FK", "FM", "FO", "FR", "GA", "GB", "GD", "GE", "GF", "GG", "GH", "GI", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM", "HN", "HR", "HT", "HU", "ID", "IE", "IL", "IM", "IN", "IO", "IQ", "IR", "IS", "IT", "JE", "JM", "JO", "JP", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "ME", "MF", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ", "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW", "PY", "QA", "RE", "RO", "RS", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SR", "SS", "ST", "SV", "SX", "SY", "SZ", "TC", "TD", "TF", "TG", "TH", "TJ", "TK", "TL", "TM", "TN", "TO", "TR", "TT", "TV", "TW", "TZ", "UA", "UG", "UM", "US", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "YE", "YT", "ZA", "ZM", "ZW")
            val countryCode = cleanValue.substring(4, 6)
            if (countryCode in countryCodes) return true
        }
        
        return false
    }

    override fun toString() = name
}
