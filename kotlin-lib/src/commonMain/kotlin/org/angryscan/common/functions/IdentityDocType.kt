package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val IDENTITY_DOC_TYPE_REGEX_ONLY = """
(?ix)
(?:^|(?<=\s)|(?<=[\(\[\{«"']))
(?:наименование\s+вида\s+документа\s+удостоверяющего\s+личность|вид\s+документа\s+удостоверяющего\s+личность|документ\s+удостоверяющий\s+личность)?
\s*[:\-]?\s*
(
  (?:паспорт\s+гражданина\s+(?:российской\s+федерации|рф|россии))
| (?:паспорт\s+иностранного\s+гражданина)
| (?:загран(?:ичный|паспорт))
| (?:дипломатический\s+паспорт)
| (?:служебный\s+паспорт)
| (?:временное\s+удостоверение\s+личности\s+гражданина\s+(?:рф|российской\s+федерации))
| (?:свидетельство\s+о\s+рождении)
| (?:вид\s+на\s+жительство(?:\s+в\s+рф)?|ВНЖ)
| (?:разрешение\s+на\s+временное\s+проживание|РВП)
| (?:военный\s+билет)
| (?:удостоверение\s+личности\s+(?:офицера|военнослужащего|моряка|пенсионера|личности))
| (?:национальный\s+паспорт)
)
(?:$|(?=\s)|(?=[\)\]\}»"'.,;:!?]))
""".trimIndent()

fun findIdentityDocType(text: String, withContext: Boolean) = regexDetector(
    text,
    IDENTITY_DOC_TYPE_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)
