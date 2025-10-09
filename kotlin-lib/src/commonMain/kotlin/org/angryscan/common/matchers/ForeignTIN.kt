package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val FOREIGN_TIN_REGEX = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                   
(?:[иИ]ностранный\s+[нН]алоговый\s+[иИ]дентификационный\s+[нН]омер|[fF]oreign\s+TIN|TIN\s+(US|China))?
\s*[:\-]?\s*
(?:
  (\d{3}[-\s]\d{2}[-\s]\d{4})         
| ([A-Z0-9]{18})                    
)
(?![\p{L}\d\p{S}\p{P}])                      
""".trimIndent()

fun findForeignTIN(text: String, withContext: Boolean) = regexDetector(
    text,
    FOREIGN_TIN_REGEX.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)