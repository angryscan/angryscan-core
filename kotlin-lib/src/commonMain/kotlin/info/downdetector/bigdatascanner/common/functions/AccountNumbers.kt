package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findAccountNumbers(text:String, withContext: Boolean) = regexDetector(
    text,
    """(?<=\D|^)40[0-9]{3}(810|840|978)[0-9]{12}(?=\D|$)"""
        .toRegex(setOf(RegexOption.MULTILINE)),
    withContext
)