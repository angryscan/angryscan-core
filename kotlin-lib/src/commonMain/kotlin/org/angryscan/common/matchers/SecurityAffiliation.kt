package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object SecurityAffiliation : IHyperMatcher, IKotlinMatcher {
    override val name = "Security Affiliation"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:сотрудник|служащий|работник|офицер|агент|принадлежность\s+к|служба\s+в|работает\s+в|член\s+)?
        \s*[:\-]?\s*
        (
          МВД|МО|СВР|ФСБ|ФСО|
          Министерство\s+внутренних\s+дел|
          Министерство\s+обороны|
          Служба\s+внешней\s+разведки|
          Федеральная\s+служба\s+безопасности|
          Федеральная\s+служба\s+охраны
        )
        (?:\s+РФ|\s+России)?
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])МВД(?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)""",
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])СВР(?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)""",
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])ФСБ(?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)""",
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])ФСО(?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)""",
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])МО(?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)""",
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])Министерств[оа]\s+внутренн[иеы][хй]\s+дел[аы]?(?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)""",
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])Министерств[оа]\s+оборон[ыаеу](?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)""",
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])Служб[аыеуой]\s+внешн[еийя][йя]\s+разведк[иеуой](?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)""",
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])Федеральн[аояуе][йяе]\s+служб[аыеуой]\s+безопасност[иеуь](?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)""",
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])Федеральн[аояуе][йяе]\s+служб[аыеуой]\s+охран[ыеуой](?:[\s\r\n\(\)\[\]\"'.,;:!?\-]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
