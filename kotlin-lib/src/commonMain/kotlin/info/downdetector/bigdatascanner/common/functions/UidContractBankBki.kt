package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val UID_CONTRACT_FL_BANK_BKI_REGEX_ONLY = """
(?ix)                       
(?<![\p{L}\d\p{S}\p{P}])                 
(?:уникальный\s+идентификатор\s+договора|УИД\s+договора|идентификатор\s+договора\s+для\s+БКИ|УИД\s+ФЛ\s+с\s+банком)?
\s*[:\-]?\s*
\{?                                       
([0-9A-Fa-f]{8}[- ]?[0-9A-Fa-f]{4}[- ]?4[0-9A-Fa-f]{3}[- ]?[89ABab][0-9A-Fa-f]{3}[- ]?[0-9A-Fa-f]{12})
\}?                                        
[-]?[0-9A-Fa-f]?                           
(?![\p{L}\d\p{S}\p{P}])                   
""".trimIndent()

private fun validateUuidV4(uuid: String): Boolean {
    val cleaned = uuid.replace("-", "").replace(" ", "").replace("{", "").replace("}", "").uppercase()
    if (cleaned.length != 32) return false
    val chars = cleaned.toCharArray()
    if (chars[12] != '4') return false
    val nibble = chars[16]
    if (nibble !in "89AB") return false
    return true
}

fun findUidContractBank(text: String, withContext: Boolean) = regexDetector(
    text,
    UID_CONTRACT_FL_BANK_BKI_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
).filter { validateUuidV4(it.value) }