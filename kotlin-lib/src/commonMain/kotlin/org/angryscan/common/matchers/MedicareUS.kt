package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for US Medicare Beneficiary Identifier (MBI).
 * Matches MBI numbers in format: 1A2B3C4D5E6F7G8H9I0J1 (11 characters: digit-letter-digit-letter-letter-digit-letter-letter-letter-digit-digit)
 * May be preceded by keywords: "medicare", "mbi", "health insurance", "medical insurance".
 * Validates character positions and excludes certain letters (S, L, O, I, B, Z).
 */
@Serializable
object MedicareUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Medicare US"
    override val javaPatterns = listOf(
        """(?i)(?:medicare|mbi|health\s*insurance|medical\s*insurance)(?:\s*[:\-#]\s*|\s+)([0-9][a-z0-9]{3}[\s\t\-]*[a-z0-9]{3}[\s\t\-]*[a-z0-9]{4})(?:[^a-zA-Z0-9]|$)""",
        """(?:^|[^a-zA-Z0-9])([0-9][a-z0-9]{3}[\s\t\-]*[a-z0-9]{3}[\s\t\-]*[a-z0-9]{4})(?:[^a-zA-Z0-9]|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(?:medicare|mbi|health\s*insurance|medical\s*insurance)(?:\s*[:\-#]\s*|\s+)[0-9][a-z0-9]{3}[\s\t\-]*[a-z0-9]{3}[\s\t\-]*[a-z0-9]{4}(?:[^a-zA-Z0-9]|$)""",
        """(?:^|[^a-zA-Z0-9])[0-9][a-z0-9]{3}[\s\t\-]*[a-z0-9]{3}[\s\t\-]*[a-z0-9]{4}(?:[^a-zA-Z0-9]|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val mbi = value.replace(Regex("[^A-Z0-9]"), "").uppercase().trim()

        if (mbi.length != 11)
            return false

        val excludedLetters = setOf('S', 'L', 'O', 'I', 'B', 'Z')
        
        if (mbi.any { it.isLetter() && it in excludedLetters })
            return false

        if (!mbi[0].isDigit() || mbi[0] == '0')
            return false

        if (!mbi[1].isLetter())
            return false

        if (!mbi[3].isDigit())
            return false

        if (!mbi[4].isLetter())
            return false

        if (!mbi[6].isDigit())
            return false

        if (!mbi[7].isLetter())
            return false

        if (!mbi[8].isLetter())
            return false

        if (!mbi[9].isDigit())
            return false

        if (!mbi[10].isDigit())
            return false

        val letterCount = mbi.count { it.isLetter() }
        if (letterCount < 3)
            return false
        
        return true
    }

    override fun toString() = name
}

