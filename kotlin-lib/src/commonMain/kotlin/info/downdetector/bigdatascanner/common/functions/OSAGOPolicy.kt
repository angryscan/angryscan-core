package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val OSAGO_POLICY_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                   
(?:полис\s+ОСАГО|ОСАГО|номер\s+полиса\s+ОСАГО|е-ОСАГО|электронный\s+полис\s+ОСАГО|страховка\s+ОСАГО)?
\s*[:\-]?\s*
([A-Z]{3}\s?(?:№\s?)?\s?\d{10}) 
(?![\p{L}\d\p{S}\p{P}])                    
""".trimIndent()

fun findOSAGOPolicy(text: String, withContext: Boolean) = regexDetector(
    text,
    OSAGO_POLICY_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)