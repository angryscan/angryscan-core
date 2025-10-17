package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val GEO__REGEX_ONLY = """
(?ix)
(?:^|(?<=\s)|(?<=[\(\[\{«"']))
(?:геолокация\s+ФЛ|координаты\s+физического\s+лица|широта\s+и\s+долгота\s+ФЛ)?
\s*[:\-]?\s*
(
  (?:
    -?(?:[1-8]\d(?:\.\d{1,10})?|90(?:\.0{1,10})?|\d\.\d{1,10})
  )
  \s*,\s*
  (?:
    -?(?:180(?:\.0{1,10})?|1[0-7]\d(?:\.\d{1,10})?|[1-9]\d(?:\.\d{1,10})?|\d\.\d{1,10})
  )
)
(?:$|(?=\s)|(?=[\)\]\}»"'.,;:!?]))
""".trimIndent()

private fun validateGeo(geo: String): Boolean {
    val parts = geo.split(",").map { it.trim() }
    if (parts.size != 2) return false
    val lat = parts[0].toDoubleOrNull() ?: return false
    val lon = parts[1].toDoubleOrNull() ?: return false
    return lat in -90.0..90.0 && lon in -180.0..180.0
}

fun findGeo(text: String, withContext: Boolean) = regexDetector(
    text,
    GEO__REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
).filter { validateGeo(it.value) }
