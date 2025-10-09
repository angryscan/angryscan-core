package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val CADASTRAL_NUMBER_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                   
(?:кадастровый\s+номер|кадастровый\s+номер\s+объекта\s+недвижимости|КН|кадастровый\s+номер\s+земельного\s+участка|кадастровый\s+номер\s+квартиры)?
\s*[:\-]?\s*
(\d{2}\s?:\s?\d{2}\s?:\s?\d{6,7}\s?:\s?\d{1,5})
(?![\p{L}\d\p{S}\p{P}])                    
""".trimIndent()

fun findCadastralNumber(text: String, withContext: Boolean) = regexDetector(
    text,
    CADASTRAL_NUMBER_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)