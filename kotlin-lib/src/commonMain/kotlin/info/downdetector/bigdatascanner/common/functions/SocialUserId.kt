package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val SOCIAL_USER_ID_REGEX_ONLY = """(?ix)
(?<![\p{L}\d\p{S}\p{P}])
(?:ID\s+в\s+(VK|Telegram|Instagram|Одноклассниках|Facebook)|username\s+в\s+(VK|Telegram|Instagram|Одноклассниках|Facebook)|аккаунт\s+в\s+(VK|Telegram|Instagram|Одноклассниках|Facebook)|профиль\s+ФЛ\s+в\s+соцсетях)?
\s*[:\-]?\s*
(@[a-zA-Z0-9_]{3,32})
(?![\p{L}\d\p{S}\p{P}])
""".trimIndent()

fun findSocialUserId(text: String, withContext: Boolean) = regexDetector(
    text,
    SOCIAL_USER_ID_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)