package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val HASH_DATA_REGEX_ONLY = """
(?ix)
(?<![\p{L}\d\p{S}\p{P}])
(?:хеш|хешированные\s+данные|hash|md5|sha1|sha256|sha384|sha512|sha3)?
\s*[:\-]?\s*
([0-9a-fA-F]{32}|[0-9a-fA-F]{40}|[0-9a-fA-F]{64}|[0-9a-fA-F]{96}|[0-9a-fA-F]{128})
(?![\p{L}\d\p{S}\p{P}])
""".trimIndent()

private fun validateHash(hash: String): Boolean {
    return hash.length in listOf(32, 40, 64, 96, 128) && hash.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }
}

fun findHashData(text: String, withContext: Boolean) = regexDetector(
    text,
    HASH_DATA_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
).filter { validateHash(it.value) }