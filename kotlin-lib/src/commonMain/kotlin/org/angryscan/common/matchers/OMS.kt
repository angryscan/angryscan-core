package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for Russian OMS (Обязательное медицинское страхование) insurance policy numbers.
 * Matches 16-digit policy numbers in format: XXXX XXXX XXXX XXXX
 * Validates checksum using Luhn-like algorithm.
 * Filters out fake patterns (all same digits, all zeros/ones, years in chunks).
 * May be preceded by keywords like "полис ОМС", "номер полиса ОМС", "страховка".
 */
@Serializable
object OMS : IHyperMatcher, IKotlinMatcher {
    override val name = "OMS"
    
    private val keywordsPattern = """
        (?:
          полис\s+обязательного\s+медицинского\s+страхования|
          номер\s+полиса\s+ОМС|
          номер\s+полиса\s+обязательного\s+медицинского\s+страхования|
          серия\s+и\s+номер\s+полиса\s+ОМС|
          серия\s+и\s+номер\s+полиса|
          номер\s+полиса|
          полис\s+ОМС|
          ОМС\s+№|
          омс|
          страховка|
          страхование
        )
    """.trimIndent()
    
    private val numberPattern = """([0-9]{4}[\s\t-]*[0-9]{4}[\s\t-]*[0-9]{4}[\s\t-]*[0-9]{4})"""
    
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
            keywordsPattern
        } else {
            """(?:$keywordsPattern)?"""
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

    override val hyperPatterns = listOf(
        """(?:^|[^\w])(?:полис\s+обязательного\s+медицинского\s+страхования|номер\s+полиса\s+ОМС|номер\s+полиса\s+обязательного\s+медицинского\s+страхования|серия\s+и\s+номер\s+полиса\s+ОМС|серия\s+и\s+номер\s+полиса|номер\s+полиса|полис\s+ОМС|ОМС\s+№|омс|страховка|страхование)\s*[:\-]?\s*[0-9]{4}[\s\t-]*[0-9]{4}[\s\t-]*[0-9]{4}[\s\t-]*[0-9]{4}(?:[^\w]|$)"""
    )
    
    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        val keywordsPart = if (requireKeywords) {
            """(?:полис\s+обязательного\s+медицинского\s+страхования|номер\s+полиса\s+ОМС|номер\s+полиса\s+обязательного\s+медицинского\s+страхования|серия\s+и\s+номер\s+полиса\s+ОМС|серия\s+и\s+номер\s+полиса|номер\s+полиса|полис\s+ОМС|ОМС\s+№|омс|страховка|страхование)"""
        } else {
            """(?:полис\s+обязательного\s+медицинского\s+страхования|номер\s+полиса\s+ОМС|номер\s+полиса\s+обязательного\s+медицинского\s+страхования|серия\s+и\s+номер\s+полиса\s+ОМС|серия\s+и\s+номер\s+полиса|номер\s+полиса|полис\s+ОМС|ОМС\s+№|омс|страховка|страхование)?"""
        }
        return listOf(
            """(?:^|[^\w])$keywordsPart\s*[:\-]?\s*[0-9]{4}[\s\t-]*[0-9]{4}[\s\t-]*[0-9]{4}[\s\t-]*[0-9]{4}(?:[^\w]|$)"""
        )
    }
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val oms = value.replace("""\D""".toRegex(), "")
        
        if (oms.length != 16) return false
        
        val zeroCount = oms.count { it == '0' }
        if (zeroCount > oms.length / 2) return false
        
        if (oms.all { it == oms[0] }) return false
        
        val chunks = oms.chunked(4)
        val allAreYears = chunks.all { chunk ->
            val year = chunk.toIntOrNull() ?: return@all false
            year in 1900..2100
        }
        if (allAreYears) return false
        
        if (oms.all { it == '0' || it == '1' }) return false
        
        if (oms.chunked(4).any { chunk -> chunk.all { it == chunk[0] } }) return false
        
        val key = oms.last().digitToInt()
        val odd = mutableListOf<Char>()
        val even = mutableListOf<Char>()
        oms.substring(0 until oms.length - 1).reversed().forEachIndexed { index, digit ->
            if (index % 2 == 0) {
                odd.add(digit)
            } else {
                even.add(digit)
            }
        }
        val right = (odd.joinToString(separator = "").toInt() * 2).toString()
        var summ = 0
        for (elem in even)
            summ += elem.digitToInt()
        for (elem in right)
            summ += elem.digitToInt()
        val checker = 10 - summ % 10
        return checker == key || (checker == 10 && key == 0)
    }

    override fun toString() = name
}
