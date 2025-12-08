package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object ResidencePermit : IHyperMatcher, IKotlinMatcher {
    override val name = "Residence Permit"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        (?:
          ВНЖ|
          вид\s+на\s+жительство|
          номер\s+вида\s+на\s+жительство|
          серия\s+и\s+номер\s+вида\s+на\s+жительство|
          серия\s+и\s+номер\s+ВНЖ|
          номер\s+ВНЖ|
          документ\s+вида\s+на\s+жительство
        )
        \s*[:\-]?\s*
        ((?:82|83)\s*(?:№|N)?\s*\d{7})
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^a-zA-Z0-9А-ЯЁа-яё])(?:ВНЖ|вид\s+на\s+жительство|номер\s+вида\s+на\s+жительство|серия\s+и\s+номер\s+вида\s+на\s+жительство|серия\s+и\s+номер\s+ВНЖ|номер\s+ВНЖ|документ\s+вида\s+на\s+жительство)\s*[:\-]?\s*(?:82|83)\s*(?:№|N)?\s*\d{7}(?:[^a-zA-Z0-9А-ЯЁа-яё]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Извлекаем номер ВНЖ из значения (которое может содержать ключевые слова)
        val numberPattern = Regex("""(?:82|83)\s*(?:№|N)?\s*(\d{7})""")
        val match = numberPattern.find(value) ?: return false
        val numberPart = match.groupValues[1]
        
        // Формируем полный номер: серия + номер
        val seriesMatch = Regex("""(82|83)""").find(value)
        val series = seriesMatch?.value ?: return false
        
        val cleaned = series + numberPart
        
        if (cleaned.length != 9) return false
        
        if (series != "82" && series != "83") return false
        
        val number = cleaned.substring(2)
        
        if (number == "0000000") return false
        
        if (number.all { it == number[0] }) return false
        
        if (number.all { it in "01" }) return false
        
        return true
    }

    override fun toString() = name
}