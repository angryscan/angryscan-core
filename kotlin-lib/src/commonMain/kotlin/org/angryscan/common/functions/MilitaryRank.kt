package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val MILITARY_RANK_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                   
(?:воинское\s+звание|звание|чин|военный\s+чин)? 
\s*[:\-]?\s*
(                                          
  рядовой|матрос|ефрейтор|старший\s+матрос|
  младший\s+сержант|старшина\s+2\s+статьи|
  сержант|старшина\s+1\s+статьи|
  старший\s+сержант|главный\s+старшина|
  старшина|главный\s+корабельный\s+старшина|
  прапорщик|мичман|старший\s+прапорщик|старший\s+мичман|
  младший\s+лейтенант|лейтенант|старший\s+лейтенант|капитан|капитан-лейтенант|
  майор|капитан\s+3\s+ранга|подполковник|капитан\s+2\s+ранга|полковник|капитан\s+1\s+ранга|
  генерал-майор|контр-адмирал|генерал-лейтенант|вице-адмирал|генерал-полковник|адмирал|
  генерал\s+армии|адмирал\s+флота|маршал\s+Российской\s+Федерации
)
(?:\s+(медицинской\s+службы|ветеринарной\s+службы|юстиции|технической\s+службы)?)?
(?![\p{L}\d\p{S}\p{P}])                    
""".trimIndent()

fun findMilitaryRank(text: String, withContext: Boolean) = regexDetector(
    text,
    MILITARY_RANK_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)