package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val VIN_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                   
(?:VIN|ВИН|идентификационный\s+номер\s+транспортного\s+средства|VIN-код|ВИН-код|идентификационный\s+номер\s+ТС)?
\s*[:\-]?\s*
([A-HJ-NPR-Z0-9]{17})            
(?![\p{L}\d\p{S}\p{P}])                    
""".trimIndent()

fun findVIN(text: String, withContext: Boolean) = regexDetector(
    text,
    VIN_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)