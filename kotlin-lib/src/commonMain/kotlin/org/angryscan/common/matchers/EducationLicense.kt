package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object EducationLicense : IHyperMatcher, IKotlinMatcher {
    override val name = "Education License"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?:^|(?<=\s)|(?<=[\(\[\{«"']))
        (?:
          полный\s+номер\s+лицензии\s+обучающей\s+организации|
          номер\s+лицензии\s+на\s+образовательную\s+деятельность|
          лицензия\s+на\s+образовательную\s+деятельность|
          лицензия
        )?
        \s*[:\-]?\s*
        ([ЛL]\s*035\s*[-–—-]\s*\d{5}\s*[-–—-]\s*\d{2}\s*/\s*\d{8})
        (?:$|(?=\s)|(?=[\)\]\}»"'.,;:!?]))
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|(?<=\s)|(?<=[\(\[\{«"']))([ЛL]\s*035\s*[-–—-]\s*\d{5}\s*[-–—-]\s*\d{2}\s*/\s*\d{8})(?:$|(?=\s)|(?=[\)\]\}»"'.,;:!?]))"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
