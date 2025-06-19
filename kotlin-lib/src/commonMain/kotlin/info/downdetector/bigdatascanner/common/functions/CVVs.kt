package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findCVVs(text: String) = regexDetector(
    text,
    """(\.|\s|^)(cvc|cvv|cav|cvc2|cvv2|cav2)(:|\s|:\s)[0-9]{3}(\.|\s|$)"""
        .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
)