package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val EP_CERT_NUMBER_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                   
(?:серийный\s+номер\s+сертификата\s+ЭП|номер\s+сертификата\s+электронной\s+подписи|serial\s+number\s+ЭП|номер\s+сертификата\s+ключа\s+проверки\s+ЭП|уникальный\s+номер\s+сертификата)?
\s*[:\-]?\s*
(([0-9A-Fa-f]{2}\s?){15}[0-9A-Fa-f]{2}([0-9A-Fa-f]{2}\s?){0,4})
(?![\p{L}\d\p{S}\p{P}])                    
""".trimIndent()

fun findEpCertificateNumber(text: String, withContext: Boolean) = regexDetector(
    text,
    EP_CERT_NUMBER_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)