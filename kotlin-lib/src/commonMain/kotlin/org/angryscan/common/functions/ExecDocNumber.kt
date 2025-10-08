package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val EXEC_DOC_NUMBER_REGEX_ONLY = """
(?ix)
(?<![\p{L}\d\p{S}\p{P}])
(?:номер\s+исполнительного\s+документа|номер\s+исполнительного\s+производства|исполнительный\s+лист)?
\s*[:\-]?\s*
(?:№\s?)?
(\d{4,5}/\d{2}/\d{5}-(?:ИП|СВ|ФС|УД|АП|СД|МС|ПД|АС|ИД))
(?![\p{L}\d\p{S}\p{P}])
""".trimIndent()

fun findExecDocNumber(text: String, withContext: Boolean) = regexDetector(
    text,
    EXEC_DOC_NUMBER_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)