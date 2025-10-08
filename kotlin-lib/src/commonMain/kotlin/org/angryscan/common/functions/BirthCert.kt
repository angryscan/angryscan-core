package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val BIRTH_CERT_REGEX_ONLY = """
(?ix)                                        
(?<![\p{L}\d\p{S}\p{P}])                     
(?:свидетельство\s+о\s+рождении|серия)?     
\s*[:\-]?\s*
([IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2})            
[\s,;:№Nn]*                           
(\d{6})                                 
(?![\p{L}\d\p{S}\p{P}])                      
""".trimIndent()

fun findBirthCert(text: String, withContext: Boolean) = regexDetector(
    text,
    BIRTH_CERT_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)