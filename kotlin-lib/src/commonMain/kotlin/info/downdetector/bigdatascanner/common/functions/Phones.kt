package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findPhones(text: String, withContext: Boolean) = regexDetector(
    text,
    """(?<=[-, ()=*]|^)((\+?7)|8)[ \t\-]?\(?[489][0-9]{2}\)?[ \t\-]?[0-9]{3}[ \t\-]?[0-9]{2}[ \t\-]?[0-9]{2}(?=\W|$)"""
        .toRegex(setOf(RegexOption.MULTILINE)),
    withContext
)