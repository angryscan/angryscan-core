package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val MILITARY_ID_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                             
(?:удостоверение\s+личности\s+военнослужащего)?
\s*[:\-]?\s*
([А-ЯA-Z]{2}\s*\d{7})                 
(?![\p{L}\d\p{S}\p{P}])                      
""".trimIndent()

fun findMilitaryID(text: String, withContext: Boolean) = regexDetector(
    text,
    MILITARY_ID_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)