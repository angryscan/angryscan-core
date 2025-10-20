package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object EducationLevel : IHyperMatcher, IKotlinMatcher {
    override val name = "Education Level"
    override val javaPatterns = listOf(
        """
        (?ixu)
        (?:^|(?<=\s)|(?<=[\(\[\{«"']))
        (?:уровень\s+образования|образование)?
        \s*[:\-]?\s*
        (
          (?:
            дошкольное\s+образование|
            начальное\s+общее(?:\s+образование)?|
            основное\s+общее(?:\s+образование)?|
            среднее\s+общее(?:\s+образование)?
          )
        |
          (?:
            среднее\s+профессиональное(?:\s+образование)?|
            \bСПО\b|
            начальное\s+профессиональное(?:\s+образование)?|
            \bНПО\b|
            высшее(?:\s+образование)?|
            \bВО\b|
            бакалавриат|
            специалитет|
            магистратура|
            аспирантура|
            ординатура|
            ассистентура-стажировка|
            подготовка\s+кадров\s+высшей\s+квалификации|
            \bбакалавр(?:а|у|ом|е|ы|ов|ам|ами|ах)?\b|
            \bмагистр(?:а|у|ом|е|ы|ов|ам|ами|ах)?\b|
            \bаспирант(?:а|у|ом|е|ы|ов|ам|ами|ах)?\b|
            \bспециалист(?:а|у|ом|е|ы|ов|ам|ами|ах)?\b
          )
        |
          (?:
            дополнительное\s+профессиональное\s+образование|
            дополнительное\s+образование|
            \bДПО\b|
            профессиональная\s+переподготовка|
            повышение\s+квалификации
          )
        )
        (?:$|(?=\s)|(?=[\)\]\}»"'.,;:!?]))
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """дошкольное\s+образование""",
        """(?:начальное|основное|среднее)\s+общее""",
        """среднее\s+профессиональное""",
        """начальное\s+профессиональное""",
        """высшее(?:\s+образование)?""",
        """бакалавриат""",
        """специалитет""",
        """магистратура""",
        """аспирантура""",
        """ординатура""",
        """бакалавр(?:а|у|ом|е|ы)?""",
        """магистр(?:а|у|ом|е|ы)?""",
        """аспирант(?:а|у|ом|е|ы)?""",
        """дополнительное\s+(?:профессиональное\s+)?образование""",
        """повышение\s+квалификации"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
