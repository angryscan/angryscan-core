package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findCarNumbers(text: String) = regexDetector(
    text,
    """(гос|номер|авто|рег).{0,15}([авекмнорстух][ \t]?[0-9]{3}[ \t]?[авекмнорстух]{2}[ \t]?[0-9]{2,3})"""
        .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
)