package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findEmails(text: String, withContext: Boolean) = regexDetector(
    text,
    """(?<=[-, ()=*]|^)[a-zA-Z0-9_.+-]+@[a-z0-9-.]+?\.[a-z]{2,}(?=\W|$)"""
        .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
    withContext
)