package info.downdetector.bigdatascanner.common.extensions

fun regexDetector(text: String, regex: Regex): Sequence<String> {
    return customRegexDetector(text, regex)
}

fun customRegexDetector(text: String, regex: Regex): Sequence<String> {
    return regex.findAll(text).map { it.value }
}