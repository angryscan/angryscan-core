package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object LegalEntityName : IHyperMatcher, IKotlinMatcher {
    override val name = "Legal Entity Name"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d\p{S}\p{P}])
        (?:
          наименование\s+ЮЛ|
          полное\s+наименование\s+юридического\s+лица|
          краткое\s+наименование\s+ЮЛ|
          наименование\s+организации
        )?
        \s*[:\-]?\s*
        (?:
          (?:
            ООО|АО|ПАО|ЗАО|НАО|ИП|ФГУП|ГУП|МУП|Фонд|Ассоциация|Союз|
            Общество\s+с\s+ограниченной\s+ответственностью|
            Акционерное\s+общество|
            Публичное\s+акционерное\s+общество
          )
          \s*
          ["']?[\p{L}\d\s\-&.,()]{5,200}["']?
        )
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """
        (?<![\p{L}\d\p{S}\p{P}])
        (?:
          (?:ООО|АО|ПАО|ЗАО|НАО|ИП|ФГУП|ГУП|МУП|Фонд|Ассоциация|Союз|
             Общество\s+с\s+ограниченной\s+ответственностью|
             Акционерное\s+общество|
             Публичное\s+акционерное\s+общество)
          \s*
          ["']?[\p{L}\d\s\-&.,()]{5,200}["']?
        )
        (?![\p{L}\d\p{S}\p{P}])
        """.trimIndent()
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}
