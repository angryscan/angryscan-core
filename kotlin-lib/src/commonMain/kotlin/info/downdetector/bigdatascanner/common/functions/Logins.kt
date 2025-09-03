package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findLogins(text: String, withContext: Boolean) = regexDetector(
    text,
    """(логин|login)(:|\s)\s?[a-z0-9_-]\S{3,25}"""
        .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
    withContext
)