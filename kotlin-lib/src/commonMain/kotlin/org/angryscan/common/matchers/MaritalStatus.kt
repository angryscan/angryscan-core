package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val MARITAL_STATUS_REGEX_ONLY = """
(?ix)
(?:^|(?<=\s)|(?<=[\(\[\{«"']))
(?:семейное\s+положение|семейный\s+статус)?
\s*[:\-]?\s*
(
    (?:
        женат|замужем|
        состоит\s+в\s+браке|в\s+браке|
        не\s+состоит\s+в\s+браке|не\s+женат|не\s+замужем|холост(?:ой)?|одинок(?:ая)?|
        разведен(?:а|ы)?|в\s+разводе|
        вдовец|вдова|
        гражданский\s+брак|состоит\s+в\s+гражданском\s+браке
    )
)
(?:$|(?=\s)|(?=[\)\]\}»"'.,;:!?]))
""".trimIndent()

fun findMaritalStatus(text: String, withContext: Boolean) = regexDetector(
    text,
    MARITAL_STATUS_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)
