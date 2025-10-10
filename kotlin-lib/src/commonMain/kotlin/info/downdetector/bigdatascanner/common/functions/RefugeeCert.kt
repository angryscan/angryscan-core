package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val REFUGEE_CERT_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                            
(?:свидетельство\s+о\s+рассмотрении\s+ходатайства\s+о\s+признании\s+(?:лица\s+)?беженцем|свидетельство\s+беженца)?
\s*[:\-]?\s*
(\d{2}\s*(?:№|N)?\s*\d{7}) 
(?![\p{L}\d\p{S}\p{P}])                      
""".trimIndent()

fun findRefugeeCert(text: String, withContext: Boolean) = regexDetector(
    text,
    REFUGEE_CERT_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)