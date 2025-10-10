package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val TEMPORARY_ID_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                            
(?:временное\s+удостоверение\s+личности|ВУЛ)?
\s*[:\-]?\s*
(\d{12})                                  
(?![\p{L}\d\p{S}\p{P}])                      
""".trimIndent()

fun findTemporaryID(text: String, withContext: Boolean) = regexDetector(
    text,
    TEMPORARY_ID_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)