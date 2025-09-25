package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

// Строгий числовой блок (включая високосность)
private val STRICT_NUMERIC = """
(?:
  31 [.\-/] (?:0?[13578]|1[02]) [.\-/] (?:19|20)\d{2}
| (?:29|30) [.\-/] (?:0?[13-9]|1[0-2]) [.\-/] (?:19|20)\d{2}
| 29 [.\-/] 0?2 [.\-/] (?:19|20)(?:0[48]|[2468][048]|[13579][26])
| (?:0?[1-9]|1\d|2[0-8]) [.\-/] (?:0?[1-9]|1[0-2]) [.\-/] (?:19|20)\d{2}
)
""".trimIndent()

// Дата рождения: обязательно требуем "дата рождения"/"родился(лась)"
private val BIRTHDAY_REGEX = """
(?ix)
(?<!\p{L})
(?:дата\s+рожд\w+|родился(?:\s*\(лась\))?|родилась)   # ОБЯЗАТЕЛЬНЫЙ префикс
\s*[:\-]?\s*
(?:
  $STRICT_NUMERIC
|
  (?: (0?[1-9]|[12]\d|3[01]) \s+
      (?:янв(?:арь|аря)?|фев(?:раль|раля)?|мар(?:т|та)?|апр(?:ель|еля)?|
         май|мая|июн(?:ь|я)?|июл(?:ь|я)?|авг(?:уст|уста)?|
         сен(?:тябрь|тября)?|сент(?:\.|)|
         окт(?:ябрь|ября)?|ноя(?:брь|бря)?|дек(?:ябрь|бря)?)
      \s+ ((?:19|20)\d{2}|\d{2})
  )
)
\s*(?:г\.?)?
(?!\p{L})
""".trimIndent()

fun findBirthday(text:String, withContext: Boolean) = regexDetector(
    text,
    BIRTHDAY_REGEX
        .toRegex(
            setOf(
                RegexOption.MULTILINE,
                RegexOption.IGNORE_CASE
            )
        ),
    withContext
)