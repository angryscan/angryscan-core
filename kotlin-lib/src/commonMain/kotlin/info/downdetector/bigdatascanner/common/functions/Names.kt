package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findNames(text:String) = regexDetector(
    text,
    """(^|\s)(?!Республика|Область|Край|Город|Село|Деревня|Улица|Проспект)((([А-ЯЁ][а-яё]+\s)([А-ЯЁ][а-яё]+[ая]\s)([А-ЯЁ][а-яё]+на))|(([А-ЯЁ][а-яё]+\s)([А-ЯЁ][а-яё]+[^ая]\s)([А-ЯЁ][а-яё]+(ич|ь))))(${'$'}|\W|\s)"""
        .toRegex(setOf(RegexOption.MULTILINE))
)