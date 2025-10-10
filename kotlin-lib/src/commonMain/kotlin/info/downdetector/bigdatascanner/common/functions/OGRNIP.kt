package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val OGRNIP_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                   
(?:ОГРНИП|регистрационный\s+номер\s+в\s+реестре\s+ФЛ\s+ЧП|регистрационный\s+номер\s+индивидуального\s+предпринимателя|государственный\s+регистрационный\s+номер\s+ИП)?
\s*[:\-]?\s*
([34]\d{14})                  
(?![\p{L}\d\p{S}\p{P}])                    
""".trimIndent()

fun findOGRNIP(text: String, withContext: Boolean) = regexDetector(
    text,
    OGRNIP_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)