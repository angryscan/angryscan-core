package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val LEGAL_ENTITY_NAME_REGEX_ONLY = """
(?ix)
(?<![\p{L}\d\p{S}\p{P}])
(?:наименование\s+ЮЛ|полное\s+наименование\s+юридического\s+лица|краткое\s+наименование\s+ЮЛ|наименование\s+организации)?
\s*[:\-]?\s*
(?:
  (?:ООО|АО|ПАО|ЗАО|НАО|ИП|ФГУП|ГУП|МУП|Фонд|Ассоциация|Союз|Общество\s+с\s+ограниченной\s+ответственностью|Акционерное\s+общество|Публичное\s+акционерное\s+общество)
  \s*
  ["']?[\p{L}\d\s\-&.,()]{5,200}["']?
)
(?![\p{L}\d\p{S}\p{P}])
""".trimIndent()

fun findLegalEntityName(text: String, withContext: Boolean) = regexDetector(
    text,
    LEGAL_ENTITY_NAME_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)