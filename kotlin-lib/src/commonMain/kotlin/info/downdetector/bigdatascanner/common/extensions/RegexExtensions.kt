package info.downdetector.bigdatascanner.common.extensions

fun regexDetector(text: String, regex: Regex, withContext: Boolean): Sequence<MatchWithContext> {
    return customRegexDetector(text, regex, withContext)
}

fun customRegexDetector(text: String, regex: Regex, withContext: Boolean): Sequence<MatchWithContext> {
    return if(withContext) {
        regex.findAll(text).map { match ->
            MatchWithContext(
                value = match.value,
                before = text.substring(
                    maxOf(0,match.range.start - 10),
                    match.range.start
                ),
                 after = text.substring(
                     match.range.last,
                     minOf(match.range.last + 10, text.length)
                 )
            )
        }
    } else {
        regex.findAll(text).map { MatchWithContext(value = it.value) }
    }
}