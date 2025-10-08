package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val OKPO_YL_REGEX_ONLY = """
(?iux)
(?<![\p{L}\p{N}])                    
(?:ОКПО\s+ЮЛ|код\s+ОКПО|ОКПО\s+организации)?
[\p{Z}\p{Cf}\s]*[:\p{Pd}\-–—]?\s*         
(\d{8})                                       
(?![\p{L}\p{N}])                              
""".trimIndent()


private fun validateOkpo(okpo: String): Boolean {
    val okpoClean = okpo.replace(Regex("[\\p{Z}\\s\\-–—]+"), "")
    if (okpoClean.length != 8) return false
    val digits = okpoClean.map { it.toString().toInt() }
    val weights1 = intArrayOf(1, 2, 3, 4, 5, 6, 7)
    val sum1 = weights1.indices.sumOf { weights1[it] * digits[it] }
    var check = sum1 % 11
    if (check > 9) {
        val weights2 = intArrayOf(3, 4, 5, 6, 7, 8, 9)
        val sum2 = weights2.indices.sumOf { weights2[it] * digits[it] }
        check = sum2 % 11
    }
    if (check == 10) check = 0
    return check == digits[7]
}

fun findOKPO(text: String, withContext: Boolean) = regexDetector(
    text,
    OKPO_YL_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
).filter { validateOkpo(it.value) }