package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val STATE_REG_CONTRACT_FL_REGEX_ONLY = """
(?ix)
(?<!\S)
(?:номер\s+государственной\s+регистрации\s+договора|номер\s+регистрации\s+договора\s+в\s+Росреестре|госрегистрационный\s+номер\s+договора\s+с\s+ФЛ|запись\s+регистрации)?
\s*[:\-]?\s*
(?:№\s?|N\s?)?
(\d{2}\s?[-:]\s?\d{2}\s?[-:]\s?\d{2}\s?/\s?\d{3,4}\s?/\s?\d{4}\s?[-:]\s?\d{1,3})
(?! \S)
""".trimIndent()

fun findStateRegContract(text: String, withContext: Boolean) = regexDetector(
    text,
    STATE_REG_CONTRACT_FL_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)