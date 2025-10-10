package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val SECURITY_AFFILIATION_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                   
(?:сотрудник|служащий|работник|офицер|агент|принадлежность\s+к|служба\s+в|работает\s+в|член\s+)?
\s*[:\-]?\s*
(                                     
  МВД|МО|СВР|ФСБ|ФСО                 
| Министерство\s+внутренних\s+дел       
| Министерство\s+обороны
| Служба\s+внешней\s+разведки
| Федеральная\s+служба\s+безопасности
| Федеральная\s+служба\s+охраны
)
(?:\s+РФ|\s+России)?                      
(?![\p{L}\d\p{S}\p{P}])                    
""".trimIndent()

fun findSecurityAffiliation(text: String, withContext: Boolean) = regexDetector(
    text,
    SECURITY_AFFILIATION_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)