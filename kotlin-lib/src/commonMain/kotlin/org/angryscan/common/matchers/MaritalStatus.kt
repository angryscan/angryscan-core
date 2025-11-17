package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object MaritalStatus : IHyperMatcher, IKotlinMatcher {
    override val name = "Marital Status"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?:^|(?<=\s)|(?<=[\(\[\{«"']))
        (?:семейное\s+положение|семейный\s+статус)?
        \s*[:\-]?\s*
        (
          (?:
            женат|
            замужем|
            состоит\s+в\s+браке|
            в\s+браке|
            не\s+состоит\s+в\s+браке|
            не\s+женат|
            не\s+замужем|
            холост(?:ой)?|
            одинок(?:ая)?|
            разведен(?:а|ы)?|
            в\s+разводе|
            вдовец|
            вдова|
            гражданский\s+брак|
            состоит\s+в\s+гражданском\s+браке
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
        """(?:^|[\s\(\[\{«"'\-:;])не\s+состоит\s+в\s+браке(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])не\s+женат(?:а)?(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])не\s+замужем(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])состоит\s+в\s+гражданском\s+браке(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])гражданский\s+брак(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])(?:состоит|находится)\s+в\s+браке(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])в\s+развод(?:е|у)(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])женат(?:а)?(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])замужем(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])холост(?:ой|ая)?(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])одинок(?:ий|ая)?(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])разведен(?:а|ы)?(?:[\s\)\]\}»"'.,;:!?\-]|$)""",
        """(?:^|[\s\(\[\{«"'\-:;])вдов(?:ец|а)(?:[\s\)\]\}»"'.,;:!?\-]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
