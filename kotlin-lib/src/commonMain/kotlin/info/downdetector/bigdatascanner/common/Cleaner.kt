package info.downdetector.bigdatascanner.common

@Deprecated("Use KotlinEngine instead")
object Cleaner {
    /*This class is responsible for cleaning documents text*/
    // delete all junk from text
    private fun delGarbageCharacters(text: String): String {
        val patternTags = """<[^>]*?>""".toRegex()
        val singleSpacePattern = """${160.toChar()}""".toRegex()
        val nextLinePattern = """\n""".toRegex()
        val pattern = """([^а-яА-Яa-zA-Z@,.:0-9- ()*=+/])|(quot)""".toRegex()
        return text
            .replace(singleSpacePattern, " ")
            .replace(nextLinePattern, "  ")
            .replace(patternTags, "  ")
            .replace(pattern, "  ")
    }

    // normalize spaces
    private fun delExtraSpaces(text: String): String {
        return text.replace("""\s{3,}""".toRegex(), "  ")
    }

    // getValue cleaned text
    fun cleanText(text: String): String {
        return delExtraSpaces(delGarbageCharacters(text)).trim()
    }
}
