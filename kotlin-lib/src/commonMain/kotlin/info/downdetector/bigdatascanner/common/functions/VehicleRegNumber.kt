package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

private val VEHICLE_REG_NUMBER_REGEX_ONLY = """
(?ix)                                      
(?<![\p{L}\d\p{S}\p{P}])                   
(?:регистрационный\s+номер\s+ТС|госномер|номерной\s+знак\s+ТС|автомобильный\s+номер|регистрационный\s+знак\s+транспортного\s+средства)?
\s*[:\-]?\s*
(?:
  ([АВЕКМНОРСТУХ]\s?\d{3}\s?[АВЕКМНОРСТУХ]{2}\s?)
| (\d{4}\s?[АВЕКМНОРСТУХ]{2}\s?)                  
)
(?:01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|67|68|69|70|71|72|73|74|75|76|77|78|79|80|81|82|83|84|85|86|87|88|89|90|91|92|93|94|95|96|97|98|99|102|103|113|116|118|121|122|123|124|125|126|134|136|138|142|150|152|154|156|159|161|163|164|166|169|172|173|174|177|178|186|190|193|196|197|198|199|250|702|716|725|750|761|763|774|777|790|797|799)
(?![\p{L}\d\p{S}\p{P}])                    
""".trimIndent()

fun findVehicleRegNumber(text: String, withContext: Boolean) = regexDetector(
    text,
    VEHICLE_REG_NUMBER_REGEX_ONLY.toRegex(setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)),
    withContext
)