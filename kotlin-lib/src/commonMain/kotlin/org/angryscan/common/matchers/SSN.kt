package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object SSN : IHyperMatcher, IKotlinMatcher {
    override val name = "SSN"
    override val javaPatterns = listOf(
        """\b[0-9]{3}[ -]?[0-9]{2}[ -]?[0-9]{4}\b(?![-0-9])"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\r\n\(\)\[\]\"'.,;:!?\-])[0-9]{3}[ -]?[0-9]{2}[ -]?[0-9]{4}(?:[\s\r\n\(\)\[\]\"'.,;:!?]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE
    )

    override fun check(value: String): Boolean {
        val ssn = value.replace(Regex("[^0-9]"), "").trim()
        
        if (ssn.length != 9)
            return false
            
        val area = ssn.substring(0, 3).toInt()
        val group = ssn.substring(3, 5).toInt()
        val serial = ssn.substring(5, 9).toInt()

        if (area == 0 || area == 666 || area >= 900)
            return false
        if (group == 0)
            return false
        if (serial == 0)
            return false
            
        return true
    }

    override fun toString() = name
}

