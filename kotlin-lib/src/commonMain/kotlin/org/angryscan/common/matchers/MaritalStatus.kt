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
        """(?:^|[\s\r\n])семейно[ея]\s+положени[ея](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])семейн[ыо][йгмя]\s+статус[аеуым]?(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])женат[аы]?(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])замужем(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])(?:состо[ияютл]+|находится|находился[ас]?)\s+в\s+(?:гражданско[мй]\s+)?брак[еу](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])в\s+брак[еу](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])не\s+(?:женат[аы]?|замужем)(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])не\s+(?:состо[ияютл]+|находится)\s+в\s+брак[еу](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])холост[ойыая]?(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])одинок[ийоая]?(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])разведен[аоы]?(?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])в\s+развод[еу](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])вдов[ецаыуой](?:[\s\r\n]|$)""",
        """(?:^|[\s\r\n])гражданско[мй]\s+брак[еуа]?(?:[\s\r\n]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
