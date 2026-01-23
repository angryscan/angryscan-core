package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian vehicle registration numbers (госномер).
 * Matches license plates in formats:
 * - А123БВ 77 (1 letter, 3 digits, 2 letters, region code)
 * - 1234АБ 77 (4 digits, 2 letters, region code)
 * Validates against list of valid Russian region codes.
 * May be preceded by keywords like "госномер", "номер автомобиля", "регистрационный номер ТС".
 */
@Serializable
object VehicleRegNumber : IHyperMatcher, IKotlinMatcher {
    override val name = "Vehicle Registration Number"
    
    private val regionCodes = """01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|57|58|59|60|61|62|63|64|65|66|67|68|69|70|71|72|73|74|75|76|77|78|79|80|81|82|83|84|85|86|87|88|89|90|91|92|93|94|95|96|97|98|99|102|103|113|116|118|121|122|123|124|125|126|134|136|138|142|150|152|154|156|159|161|163|164|166|169|172|173|174|177|178|186|190|193|196|197|198|199|250|702|716|725|750|761|763|774|777|790|797|799"""
    
    private val keywordsPattern = """
        (?:
          регистрационный\s+номер\s+ТС|
          регистрационный\s+номер\s+транспортного\s+средства|
          госномер|
          гос\.\s*номер|
          государственный\s+номер|
          номерной\s+знак\s+ТС|
          номерной\s+знак\s+транспортного\s+средства|
          автомобильный\s+номер|
          номер\s+автомобиля|
          регистрационный\s+знак\s+транспортного\s+средства|
          регистрационный\s+знак\s+ТС|
          номер\s+ТС
        )
    """.trimIndent()
    
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        $keywordsPattern
        \s*[:\-]?\s*
        (?:
          ([АВБГДЕКМНОРСТУХABEKMHOPCTYX]\s?\d{3}\s?[АВБГДЕКМНОРСТУХABEKMHOPCTYX]{2}\s+)
        | (\d{4}\s?[АВБГДЕКМНОРСТУХABEKMHOPCTYX]{2}\s+)
        )
        (?:$regionCodes)
        (?![\p{L}\d])
        """.trimIndent()
    )
    
    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        val baseKeywords = """(?:регистрационный\s+номер\s+ТС|регистрационный\s+номер\s+транспортного\s+средства|госномер|гос\.\s*номер|государственный\s+номер|номерной\s+знак\s+ТС|номерной\s+знак\s+транспортного\s+средства|автомобильный\s+номер|номер\s+автомобиля|регистрационный\s+знак\s+транспортного\s+средства|регистрационный\s+знак\s+ТС|номер\s+ТС)"""
        val keywordsPart = baseKeywords + if (!requireKeywords) "?" else ""
        return listOf(
            """
            (?ix)
            (?<![\p{L}\d])
            $keywordsPart
            \s*[:\-]?\s*
            (?:
              ([АВБГДЕКМНОРСТУХABEKMHOPCTYX]\s?\d{3}\s?[АВБГДЕКМНОРСТУХABEKMHOPCTYX]{2}\s+)
            | (\d{4}\s?[АВБГДЕКМНОРСТУХABEKMHOPCTYX]{2}\s+)
            )
            (?:$regionCodes)
            (?![\p{L}\d])
            """.trimIndent()
        )
    }
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^\w])(?:регистрационный\s+номер\s+ТС|регистрационный\s+номер\s+транспортного\s+средства|госномер|гос\.\s*номер|государственный\s+номер|номерной\s+знак\s+ТС|номерной\s+знак\s+транспортного\s+средства|автомобильный\s+номер|номер\s+автомобиля|регистрационный\s+знак\s+транспортного\s+средства|регистрационный\s+знак\s+ТС|номер\s+ТС)\s*[:\-]?\s*[АВБГДЕКМНОРСТУХ]\d{3}[АВБГДЕКМНОРСТУХ]{2}\s+(?:$regionCodes)(?:[^\w]|$)""",
        """(?:^|[^\w])(?:регистрационный\s+номер\s+ТС|регистрационный\s+номер\s+транспортного\s+средства|госномер|гос\.\s*номер|государственный\s+номер|номерной\s+знак\s+ТС|номерной\s+знак\s+транспортного\s+средства|автомобильный\s+номер|номер\s+автомобиля|регистрационный\s+знак\s+транспортного\s+средства|регистрационный\s+знак\s+ТС|номер\s+ТС)\s*[:\-]?\s*[ABEKMHOPCTYX]\d{3}[ABEKMHOPCTYX]{2}\s+(?:$regionCodes)(?:[^\w]|$)""",
        """(?:^|[^\w])(?:регистрационный\s+номер\s+ТС|регистрационный\s+номер\s+транспортного\s+средства|госномер|гос\.\s*номер|государственный\s+номер|номерной\s+знак\s+ТС|номерной\s+знак\s+транспортного\s+средства|автомобильный\s+номер|номер\s+автомобиля|регистрационный\s+знак\s+транспортного\s+средства|регистрационный\s+знак\s+ТС|номер\s+ТС)\s*[:\-]?\s*\d{4}[АВБГДЕКМНОРСТУХ]{2}\s+(?:$regionCodes)(?:[^\w]|$)""",
        """(?:^|[^\w])(?:регистрационный\s+номер\s+ТС|регистрационный\s+номер\s+транспортного\s+средства|госномер|гос\.\s*номер|государственный\s+номер|номерной\s+знак\s+ТС|номерной\s+знак\s+транспортного\s+средства|автомобильный\s+номер|номер\s+автомобиля|регистрационный\s+знак\s+транспортного\s+средства|регистрационный\s+знак\s+ТС|номер\s+ТС)\s*[:\-]?\s*\d{4}[ABEKMHOPCTYX]{2}\s+(?:$regionCodes)(?:[^\w]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        val baseKeywords = """(?:регистрационный\s+номер\s+ТС|регистрационный\s+номер\s+транспортного\s+средства|госномер|гос\.\s*номер|государственный\s+номер|номерной\s+знак\s+ТС|номерной\s+знак\s+транспортного\s+средства|автомобильный\s+номер|номер\s+автомобиля|регистрационный\s+знак\s+транспортного\s+средства|регистрационный\s+знак\s+ТС|номер\s+ТС)"""
        val keywordsPart = baseKeywords + if (!requireKeywords) "?" else ""
        return listOf(
            """(?:^|[^\w])$keywordsPart\s*[:\-]?\s*[АВБГДЕКМНОРСТУХ]\d{3}[АВБГДЕКМНОРСТУХ]{2}\s+(?:$regionCodes)(?:[^\w]|$)""",
            """(?:^|[^\w])$keywordsPart\s*[:\-]?\s*[ABEKMHOPCTYX]\d{3}[ABEKMHOPCTYX]{2}\s+(?:$regionCodes)(?:[^\w]|$)""",
            """(?:^|[^\w])$keywordsPart\s*[:\-]?\s*\d{4}[АВБГДЕКМНОРСТУХ]{2}\s+(?:$regionCodes)(?:[^\w]|$)""",
            """(?:^|[^\w])$keywordsPart\s*[:\-]?\s*\d{4}[ABEKMHOPCTYX]{2}\s+(?:$regionCodes)(?:[^\w]|$)"""
        )
    }
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
