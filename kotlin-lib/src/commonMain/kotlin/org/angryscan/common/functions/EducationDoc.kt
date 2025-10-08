package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val EDUCATION_DOC_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                             
(?:документ\s+об\s+образовании|диплом|аттестат|приложение\s+к\s+диплому)?
\s*[:\-]?\s*
(?:
  (\d{6}\s?\d{7})                
| (\d{2}\s?[А-Я]{2}\s?\d{6,7})   
| ([IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2}\s*\d{6})
)
(?![\p{L}\d\p{S}\p{P}])                      
""".trimIndent()

fun findEducationDoc(text: String, withContext: Boolean) = regexDetector(
    text,
    EDUCATION_DOC_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)