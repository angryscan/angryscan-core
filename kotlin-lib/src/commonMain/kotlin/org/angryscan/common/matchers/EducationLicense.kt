package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian educational institution licenses.
 * Matches license numbers in format: Л 035-XXXXX-XX / XXXXXXXX
 * Where Л is the license prefix, followed by series and number separated by dashes and slash.
 * May be preceded by keywords like "лицензия на образовательную деятельность".
 */
@Serializable
object EducationLicense : IHyperMatcher, IKotlinMatcher {
    override val name = "Education License"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:
          полный\s+номер\s+лицензии\s+обучающей\s+организации|
          номер\s+лицензии\s+на\s+образовательную\s+деятельность|
          лицензия\s+на\s+образовательную\s+деятельность|
          лицензия
        )?
        \s*[:\-]?\s*
        (?<![\p{L}\d])
        ([ЛL]\s*035\s*[-–—-]\s*\d{5}\s*[-–—-]\s*\d{2}\s*/\s*\d{8})
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^a-zA-Z0-9А-ЯЁа-яё])[ЛL]\s*035\s*[-–—-]\s*\d{5}\s*[-–—-]\s*\d{2}\s*/\s*\d{8}(?:[^0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
