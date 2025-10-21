package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object IdentityDocType : IHyperMatcher, IKotlinMatcher {
    override val name = "Identity Document Type"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?:^|(?<=\s)|(?<=[\(\[\{«"']))
        (?:
          наименование\s+вида\s+документа\s+удостоверяющего\s+личность|
          вид\s+документа\s+удостоверяющего\s+личность|
          документ\s+удостоверяющий\s+личность
        )?
        \s*[:\-]?\s*
        (
          (?:паспорт\s+гражданина\s+(?:российской\s+федерации|рф|россии))|
          (?:паспорт\s+иностранного\s+гражданина)|
          (?:загран(?:ичный|паспорт))|
          (?:дипломатический\s+паспорт)|
          (?:служебный\s+паспорт)|
          (?:временное\s+удостоверение\s+личности\s+гражданина\s+(?:рф|российской\s+федерации))|
          (?:свидетельство\s+о\s+рождении)|
          (?:вид\s+на\s+жительство(?:\s+в\s+рф)?|ВНЖ)|
          (?:разрешение\s+на\s+временное\s+проживание|РВП)|
          (?:военный\s+билет)|
          (?:удостоверение\s+личности\s+(?:офицера|военнослужащего|моряка|пенсионера|личности))|
          (?:национальный\s+паспорт)
        )
        (?:$|(?=\s)|(?=[\)\]\}»"'.,;:!?]))
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n])паспорт[аеуом]?\s+гражданин[ауом]\s+(?:российско[йгемя]\s+федераци[ия]|рф|росси[ия])(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])паспорт[аеуом]?\s+иностранного\s+гражданин[ауом](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])загран(?:ичн[ыойемя][йгемя]?|паспорт[аеуом]?)(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])(?:дипломатическ[ийогемя][йгемя]?|служебн[ыойемя][йгемя]?)\s+паспорт[аеуом]?(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])временно[ея]\s+удостоверени[еяум]\s+личност[ия](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])свидетельств[оаеум]\s+о\s+рождени[ия](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])вид[аеу]?\s+на\s+жительств[оаеу](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])ВНЖ(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])разрешени[еяум]\s+на\s+временно[ея]\s+проживани[еяу](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])РВП(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])военн[ыойемя][йгемя]?\s+билет[аеуом]?(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])удостоверени[еяум]\s+личност[ия]\s+(?:офицер[ауом]|военнослужащего|моряк[ауом]|пенсионер[ауом])(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])национальн[ыойемя][йгемя]?\s+паспорт[аеуом]?(?:[\s\r\n]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
