package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for birth dates in Russian and English text.
 * Russian: preceded by "дата рождения", "родился", "родилась" + DD.MM.YYYY or DD Month YYYY.
 * English: preceded by "date of birth", "dob", "birth date", "born" + MM/DD/YYYY or DD/MM/YYYY.
 */
@Serializable
object Birthday : IHyperMatcher, IKotlinMatcher {
    override val name = "Birthday"

    private val ruDatePattern = """(?:31[ .\-/](?:0?[13578]|1[02])[ .\-/](?:19|20)\d{2}|(?:29|30)[ .\-/](?:0?[13-9]|1[0-2])[ .\-/](?:19|20)\d{2}|29[ .\-/]0?2[ .\-/](?:2000|(?:19|20)(?:0[48]|[2468][048]|[13579][26]))|(?:0?[1-9]|1\d|2[0-8])[ .\-/](?:0?[1-9]|1[0-2])[ .\-/](?:19|20)\d{2})"""
    private val enDatePattern = """(?:0?[1-9]|1[0-2])[/\-\.](?:0?[1-9]|[12]\d|3[01])[/\-\.](?:19|20)\d{2}"""

    override val javaPatterns = listOf(
        // Russian: дата рождения + DD.MM.YYYY
        """(?<![\p{L}\d])(?:дата\s+рожд[а-яёА-ЯЁ]+|родил(?:ся(?:\(лась\))?|ась))\s*[:\-]?\s*$ruDatePattern(?![\p{L}\d])""",
        // Russian: дата рождения + DD Month YYYY
        """(?<![\p{L}\d])(?:дата\s+рожд[а-яёА-ЯЁ]+|родил(?:ся(?:\(лась\))?|ась))\s*[:\-]?\s*(?:0?[1-9]|[12]\d|3[01])\s+(?:янв(?:арь|аря)?|фев(?:раль|раля)?|мар(?:т|та)?|апр(?:ель|еля)?|май|мая|июн(?:ь|я)?|июл(?:ь|я)?|авг(?:уст|уста)?|сен(?:тябрь|тября)?|сент\.?|окт(?:ябрь|ября)?|ноя(?:брь|бря)?|дек(?:ябрь|бря)?)\s+((?:19|20)\d{2}|\d{2})(?![\p{L}\d])""",
        // English: date of birth / dob / birth date / born + MM/DD/YYYY
        """(?i)(?<![\p{L}\d])(?:date\s+of\s+birth|d\.?o\.?b\.?|birth\s*date|born(?:\s+on)?)\s*[:\-]?\s*$enDatePattern(?![\p{L}\d])"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        // Russian: дата рождения + DD.MM.YYYY
        """(?:^|[^a-zA-Z0-9А-ЯЁа-яё])(?:дата\s+рожд[а-яёА-ЯЁ]+|родил(?:ся(?:\(лась\))?|ась))\s*[:\-]?\s*$ruDatePattern(?:[^0-9]|$)""",
        // Russian: дата рождения + DD Month YYYY
        """(?:^|[^a-zA-Z0-9А-ЯЁа-яё])(?:дата\s+рожд[а-яёА-ЯЁ]+|родил(?:ся(?:\(лась\))?|ась))\s*[:\-]?\s*(?:0?[1-9]|[12]\d|3[01])\s+(?:янв(?:арь|аря)?|фев(?:раль|раля)?|мар(?:т|та)?|апр(?:ель|еля)?|май|мая|июн(?:ь|я)?|июл(?:ь|я)?|авг(?:уст|уста)?|сен(?:тябрь|тября)?|сент\.?|окт(?:ябрь|ября)?|ноя(?:брь|бря)?|дек(?:ябрь|бря)?)\s+((?:19|20)\d{2}|\d{2})(?:[^0-9]|$)""",
        // English: date of birth / dob / birth date / born + MM/DD/YYYY
        """(?i)(?:^|[^a-zA-Z0-9])(?:date\s+of\s+birth|d\.?o\.?b\.?|birth\s*date|born(?:\s+on)?)\s*[:\-]?\s*$enDatePattern(?:[^0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
