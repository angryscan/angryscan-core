package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findAddresses(text:String) = regexDetector(
    text,
    """(г\.|р-н|обл\.|ул\.|гор\.).{4,70}(д\.|дом)"""
        .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
)