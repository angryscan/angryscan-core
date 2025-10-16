package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val SBER_BOOK_FL_REGEX_ONLY = """
(?ix)
(?<![\p{L}\d\p{S}\p{P}])
(?:номер\s+сберкнижки|номер\s+сберегательной\s+книжки\s+ФЛ|счет\s+сберкнижки)?
\s*[:\-]?\s*
(423\d{2}(?:\.\d{3})?(?:\.\d{1})?(?:\.\d{4})?(?:\.\d{7})?)
(?![\p{L}\d\p{S}\p{P}])
""".trimIndent()

fun findSberBook(text: String, withContext: Boolean) = regexDetector(
    text,
    SBER_BOOK_FL_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)