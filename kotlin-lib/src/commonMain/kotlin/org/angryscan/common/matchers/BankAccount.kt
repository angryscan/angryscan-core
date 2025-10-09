package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val BANK_ACCOUNT_FL_REGEX_ONLY = """
(?ix)
(?<![\p{L}\d\p{S}\p{P}])
(?:номер\s+банковского\s+счета|р/с|расчетный\s+счет\s+ФЛ|номер\s+счета\s+физлица)?
\s*[:\-]?\s*
(408\d{17})
(?![\p{L}\d\p{S}\p{P}])
""".trimIndent()

fun findBankAccount(text: String, withContext: Boolean) = regexDetector(
    text,
    BANK_ACCOUNT_FL_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)