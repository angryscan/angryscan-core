package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val INHERITANCE_DOC_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                           
(?:свидетельство\s+о\s+праве\s+на\s+наследство)?
\s*[:\-]?\s*
(\d{2}\s?[А-Я]{2}\s?\d{6,7})          
(?![\p{L}\d\p{S}\p{P}])                      
""".trimIndent()

fun findInheritanceDoc(text: String, withContext: Boolean) = regexDetector(
    text,
    INHERITANCE_DOC_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)