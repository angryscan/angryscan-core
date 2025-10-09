package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val RESIDENCE_PERMIT_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                             
(?:вид\s+на\s+жительство|ВНЖ)?
\s*[:\-]?\s*
((?:82|83)\s*(?:№|N)?\s*\d{7})
(?![\p{L}\d\p{S}\p{P}])                      
""".trimIndent()

fun findResidencePermit(text: String, withContext: Boolean) = regexDetector(
    text,
    RESIDENCE_PERMIT_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)