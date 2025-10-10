package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val FOREIGN_PASSPORT_REGEX = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                   
(?:[иИ]ностранный\s+[пП]аспорт|[fF]oreign\s+[pP]assport|[pP]assport\s+(EU|US|China|Japan|Israel|Iran|UAE|Qatar))?
\s*[:\-]?\s*
(?:
  ([A-Z]\d{8})                       
| ([EG]\d{8})                             
| ([A-Z]{2}\d{7})                         
| ([A-Z]{1,2}[-]?\d{7,8})                  
)
(?![\p{L}\d\p{S}\p{P}])                      
""".trimIndent()

fun findForeignPassports(text: String, withContext: Boolean) = regexDetector(
    text,
    FOREIGN_PASSPORT_REGEX.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)