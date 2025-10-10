package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val DRIVER_LICENSE_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                             
(?:водительское\s+удостоверение|ВУ)?
\s*[:\-]?\s*
(\d{2}\s?\d{2}\s?\d{6})
(?![\p{L}\d\p{S}\p{P}])                      
""".trimIndent()

fun findDriverLicense(text: String, withContext: Boolean) = regexDetector(
    text,
    DRIVER_LICENSE_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)