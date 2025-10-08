package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val BANK_ACCOUNT_LE_REGEX_ONLY = """
(?ix)
(?<![\p{L}\d\p{S}\p{P}])
(?:номер\s+банковского\s+счета\s+ЮЛ|р/с\s+организации|расчетный\s+счет\s+ЮЛ)?
\s*[:\-]?\s*
(407\d{17})
(?![\p{L}\d\p{S}\p{P}])
""".trimIndent()

fun findBankAccountLE(text: String, withContext: Boolean) = regexDetector(
    text,
    BANK_ACCOUNT_LE_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)