package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian OGRNIP (Основной государственный регистрационный номер индивидуального предпринимателя).
 * Matches 15-digit registration numbers starting with 3 or 4.
 * Validates checksum using MOD 13 algorithm.
 * May be preceded by keywords like "ОГРНИП", "номер ОГРНИП".
 */
@Serializable
object OGRNIP : IHyperMatcher, IKotlinMatcher {
    override val name = "OGRNIP"
    
    private val keywordsPattern = """
        (?:
          ОГРНИП|
          основной\s+государственный\s+регистрационный\s+номер\s+индивидуального\s+предпринимателя|
          регистрационный\s+номер\s+в\s+реестре\s+ФЛ\s+ЧП|
          регистрационный\s+номер\s+индивидуального\s+предпринимателя|
          государственный\s+регистрационный\s+номер\s+ИП|
          номер\s+ОГРНИП|
          серия\s+и\s+номер\s+ОГРНИП
        )
    """.trimIndent()
    
    private val numberPattern = """([34]\d{14})"""
    
    override val javaPatterns = listOf(
        """
        (?ix)
        (?<![\p{L}\d])
        $keywordsPattern
        \s*[:\-]?\s*
        $numberPattern
        (?![\p{L}\d])
        """.trimIndent()
    )
    
    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        val keywordsPart = if (requireKeywords) {
            """(?:ОГРНИП|основной\s+государственный\s+регистрационный\s+номер\s+индивидуального\s+предпринимателя|регистрационный\s+номер\s+в\s+реестре\s+ФЛ\s+ЧП|регистрационный\s+номер\s+индивидуального\s+предпринимателя|государственный\s+регистрационный\s+номер\s+ИП|номер\s+ОГРНИП|серия\s+и\s+номер\s+ОГРНИП)"""
        } else {
            """(?:ОГРНИП|основной\s+государственный\s+регистрационный\s+номер\s+индивидуального\s+предпринимателя|регистрационный\s+номер\s+в\s+реестре\s+ФЛ\s+ЧП|регистрационный\s+номер\s+индивидуального\s+предпринимателя|государственный\s+регистрационный\s+номер\s+ИП|номер\s+ОГРНИП|серия\s+и\s+номер\s+ОГРНИП)?"""
        }
        return listOf(
            """
            (?ix)
            (?<![\p{L}\d])
            $keywordsPart
            \s*[:\-]?\s*
            $numberPattern
            (?![\p{L}\d])
            """.trimIndent()
        )
    }
    
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[^\w])(?:ОГРНИП|основной\s+государственный\s+регистрационный\s+номер\s+индивидуального\s+предпринимателя|регистрационный\s+номер\s+в\s+реестре\s+ФЛ\s+ЧП|регистрационный\s+номер\s+индивидуального\s+предпринимателя|государственный\s+регистрационный\s+номер\s+ИП|номер\s+ОГРНИП|серия\s+и\s+номер\s+ОГРНИП)\s*[:\-]?\s*[34]\d{14}(?:[^\w]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        val keywordsPart = if (requireKeywords) {
            """(?:ОГРНИП|основной\s+государственный\s+регистрационный\s+номер\s+индивидуального\s+предпринимателя|регистрационный\s+номер\s+в\s+реестре\s+ФЛ\s+ЧП|регистрационный\s+номер\s+индивидуального\s+предпринимателя|государственный\s+регистрационный\s+номер\s+ИП|номер\s+ОГРНИП|серия\s+и\s+номер\s+ОГРНИП)"""
        } else {
            """(?:ОГРНИП|основной\s+государственный\s+регистрационный\s+номер\s+индивидуального\s+предпринимателя|регистрационный\s+номер\s+в\s+реестре\s+ФЛ\s+ЧП|регистрационный\s+номер\s+индивидуального\s+предпринимателя|государственный\s+регистрационный\s+номер\s+ИП|номер\s+ОГРНИП|серия\s+и\s+номер\s+ОГРНИП)?"""
        }
        return listOf(
            """(?:^|[^\w])$keywordsPart\s*[:\-]?\s*[34]\d{14}(?:[^\w]|$)"""
        )
    }
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val numberPattern = Regex("[34]\\d{14}")
        val match = numberPattern.find(value)
        if (match == null) return false
        
        val ogrnipClean = match.value.replace(Regex("[^\\d]"), "")
        if (ogrnipClean.length != 15) return false
        val digits = ogrnipClean.map { it.toString().toInt() }
        val bigNum = ogrnipClean.substring(0, 14).toLong()
        var check = (bigNum % 13).toInt()
        if (check >= 10) check = 0
        return check == digits[14]
    }

    override fun toString() = name
}