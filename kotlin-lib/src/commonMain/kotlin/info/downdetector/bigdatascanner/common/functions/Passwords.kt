package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findPasswords(text:String) = regexDetector(
    text,
    """(((password|пароль)\s((?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@$}{'?;,:=+_\-]*))\S{3,25})|((password|пароль):\s?\S{3,25}))"""
        .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
)