package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findPassports(text: String) = regexDetector(
    text,
    """([п]аспорт[ \t-]?([а-яА-Я]*[ \t-]){0,2}[0-9]{2}[ \t]?[0-9]{2}[ \t]?[0-9]{6})"""
        .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
) + regexDetector(
    text,
    """[сc]ерия[ \t-]?[0-9]{2}(\s|\t)?[0-9]{2}[ \t,]?([н]омер)?[ \t-]?[0-9]{6}?"""
        .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
)