package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val BIRTH_CERT_REGEX_ONLY = """
(?ix)                                        # ignore-case + free-spacing
(?<!\p{L})
(?:свидетельство\s+о\s+рождении|серия)?      # опциональный префикс
\s*[:\-]?\s*
([IVX]{1,4}\s*[-–]?\s*[А-ЯЁ]{2})             # серия: римские цифры + 2 кириллические
[\s,;:№Nn]*                                  # разделитель/метка номера
(\d{6})                                      # номер: ровно 6 цифр
(?!\p{L})
""".trimIndent()

fun findBirthCert(text: String, withContext: Boolean) = regexDetector(
    text,
    BIRTH_CERT_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)