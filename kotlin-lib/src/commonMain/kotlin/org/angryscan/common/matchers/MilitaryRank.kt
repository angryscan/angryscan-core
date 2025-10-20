package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object MilitaryRank : IHyperMatcher, IKotlinMatcher {
    override val name = "Military Rank"
    override val javaPatterns = listOf(
        """
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
          младший\s+лейтенант|лейтенант|старший\s+лейтенант|
          капитан|капитан-лейтенант|
          майор|капитан\s+3\s+ранга|
          подполковник|капитан\s+2\s+ранга|
          полковник|капитан\s+1\s+ранга|
          генерал-майор|контр-адмирал|
          генерал-лейтенант|вице-адмирал|
          генерал-полковник|адмирал|
          генерал\s+армии|адмирал\s+флота|
          маршал\s+Российской\s+Федерации
        )
        (?:\s+(?:медицинской\s+службы|ветеринарной\s+службы|юстиции|технической\s+службы)?)?
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """рядовой|матрос|ефрейтор""",
        """(?:младший|старший)?\s*сержант""",
        """старшина(?:\s+[12]\s+статьи)?""",
        """главный\s+(?:старшина|корабельный\s+старшина)""",
        """(?:старший\s+)?(?:прапорщик|мичман)""",
        """(?:младший|старший)?\s*лейтенант""",
        """капитан(?:-лейтенант|\s+[123]\s+ранга)?""",
        """майор""",
        """подполковник""",
        """полковник""",
        """генерал(?:-(?:майор|лейтенант|полковник)|\s+армии)?""",
        """(?:контр-|вице-)?адмирал(?:\s+флота)?""",
        """маршал\s+Российской\s+Федерации"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
