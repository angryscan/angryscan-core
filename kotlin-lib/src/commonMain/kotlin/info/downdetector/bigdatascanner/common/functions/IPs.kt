package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findIPs(text: String, withContext: Boolean) = regexDetector(
    text,
    """(^|\s)((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)\.){3}(25[0-5]|(2[0-4]|1\d|[1-9]|)\d)(${'$'}|\s)"""
        .toRegex(setOf(RegexOption.MULTILINE)),
    withContext
)

fun findIPv6s(text: String, withContext: Boolean) = regexDetector(
    text,
    """(^|\s)(([0-9a-fA-F]{4}:){7}[0-9a-fA-F]{4})(${'$'}|\s)""" // только адреса без сокращений
        .toRegex(setOf(RegexOption.MULTILINE)),
    withContext
)