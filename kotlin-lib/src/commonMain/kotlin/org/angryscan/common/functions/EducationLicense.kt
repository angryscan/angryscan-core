package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val EDUCATION_LICENSE_REGEX_ONLY = """
(?ix)
(?:^|(?<=\s)|(?<=[\(\[\{«"']))
(?:полный\s+номер\s+лицензии\s+обучающей\s+организации|номер\s+лицензии\s+на\s+образовательную\s+деятельность|лицензия\s+на\s+образовательную\s+деятельность|лицензия)?
\s*[:\-]?\s*
(
  [ЛL]\s*035\s*[-–—-]\s*\d{5}\s*[-–—-]\s*\d{2}\s*/\s*\d{8}
)
(?:$|(?=\s)|(?=[\)\]\}»"'.,;:!?]))
""".trimIndent()

fun findEducationLicense(text: String, withContext: Boolean) = regexDetector(
    text,
    EDUCATION_LICENSE_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)
