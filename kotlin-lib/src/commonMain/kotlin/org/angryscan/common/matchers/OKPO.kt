package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object OKPO : IHyperMatcher, IKotlinMatcher {
    override val name = "OKPO"
    override val javaPatterns = listOf(
        """
        (?iux)
        (?<![\p{L}\p{N}])
        (?:ОКПО\s+ЮЛ|код\s+ОКПО|ОКПО\s+организации)?
        [\p{Z}\p{Cf}\s]*[:\p{Pd}\-–—]?\s*
        (\d{8})
        (?![\p{L}\p{N}])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """\b\d{8}\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val okpoClean = value.replace(Regex("[\\p{Z}\\s\\-–—]+"), "")
        if (okpoClean.length != 8) return false
        val digits = okpoClean.map { it.toString().toInt() }
        val weights1 = intArrayOf(1, 2, 3, 4, 5, 6, 7)
        val sum1 = weights1.indices.sumOf { weights1[it] * digits[it] }
        var check = sum1 % 11
        if (check > 9) {
            val weights2 = intArrayOf(3, 4, 5, 6, 7, 8, 9)
            val sum2 = weights2.indices.sumOf { weights2[it] * digits[it] }
            check = sum2 % 11
        }
        if (check == 10) check = 0
        return check == digits[7]
    }

    override fun toString() = name
}
