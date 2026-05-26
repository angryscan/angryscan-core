package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher

/**
 * Matcher for US Medicare Beneficiary Identifier (MBI) and general Medical Insurance IDs.
 *
 * MBI format: 11 characters (digit, then alternating letters/digits in specific positions).
 * Excludes letters S, L, O, I, B, Z.
 *
 * General Medical Insurance ID formats:
 * - Alphanumeric: 1-5 uppercase letters + 6-15 digits (e.g. ABC123456789) — detected without keyword.
 * - Numeric only: 8-15 digits — detected only with keyword context (medical insurance, member id, policy number, etc.).
 */
@Serializable
object MedicareUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Medicare US"
    override val javaPatterns = listOf(
        // MBI with keyword
        """(?i)(?:medicare|mbi|health\s*insurance|medical\s*insurance)(?:\s*[:\-#]\s*|\s+)([0-9][a-z0-9]{3}[\s\t\-]*[a-z0-9]{3}[\s\t\-]*[a-z0-9]{4})(?:[^a-zA-Z0-9]|$)""",
        // MBI without keyword
        """(?:^|[^a-zA-Z0-9])([0-9][a-z0-9]{3}[\s\t\-]*[a-z0-9]{3}[\s\t\-]*[a-z0-9]{4})(?:[^a-zA-Z0-9]|$)""",
        // Alphanumeric insurance ID: 2-3 letters + 8-9 digits, no keyword needed
        """(?i)(?:^|[^a-zA-Z0-9])([A-Z]{2,3}[0-9]{8,9})(?:[^a-zA-Z0-9]|$)""",
        // Numeric-only insurance ID: 8-15 digits, keyword required
        """(?i)(?:medical\s*insurance|health\s*insurance|member\s*id|insurance\s*(?:id|number|no\.?)|policy\s*(?:id|number|no\.?))(?:\s*[:\-#]\s*|\s+)([0-9]{8,15})(?:[^0-9]|$)""",
        // Alphanumeric insurance ID with keyword (covers "Medical insurance ID: ABC123456789")
        """(?i)(?:medicare|mbi|health\s*insurance|medical\s*insurance(?:\s+id)?|member\s*id|insurance\s*(?:id|number|no\.?)|policy\s*(?:id|number|no\.?))(?:\s*[:\-#]\s*|\s+)([A-Z]{2,3}[0-9]{8,9})(?:[^a-zA-Z0-9]|$)"""
    )

    override fun getJavaPatterns(requireKeywords: Boolean): List<String> {
        return if (requireKeywords) {
            listOf(javaPatterns[0], javaPatterns[3], javaPatterns[4])
        } else {
            javaPatterns
        }
    }

    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        // MBI with keyword
        """(?i)(?:medicare|mbi|health\s*insurance|medical\s*insurance)(?:\s*[:\-#]\s*|\s+)[0-9][a-z0-9]{3}[\s\t\-]*[a-z0-9]{3}[\s\t\-]*[a-z0-9]{4}(?:[^a-zA-Z0-9]|$)""",
        // MBI without keyword
        """(?:^|[^a-zA-Z0-9])[0-9][a-z0-9]{3}[\s\t\-]*[a-z0-9]{3}[\s\t\-]*[a-z0-9]{4}(?:[^a-zA-Z0-9]|$)""",
        // Alphanumeric insurance ID: 2-3 letters + 8-9 digits, no keyword needed
        """(?i)(?:^|[^a-zA-Z0-9])[A-Z]{2,3}[0-9]{8,9}(?:[^a-zA-Z0-9]|$)""",
        // Numeric-only insurance ID: 8-15 digits, keyword required
        """(?i)(?:medical\s*insurance|health\s*insurance|member\s*id|insurance\s*(?:id|number|no\.?)|policy\s*(?:id|number|no\.?))(?:\s*[:\-#]\s*|\s+)[0-9]{8,15}(?:[^0-9]|$)""",
        // Alphanumeric insurance ID with keyword (covers "Medical insurance ID: ABC123456789")
        """(?i)(?:medicare|mbi|health\s*insurance|medical\s*insurance(?:\s+id)?|member\s*id|insurance\s*(?:id|number|no\.?)|policy\s*(?:id|number|no\.?))(?:\s*[:\-#]\s*|\s+)[A-Z]{2,3}[0-9]{8,9}(?:[^a-zA-Z0-9]|$)"""
    )

    override fun getHyperPatterns(requireKeywords: Boolean): List<String> {
        return if (requireKeywords) {
            listOf(hyperPatterns[0], hyperPatterns[3], hyperPatterns[4])
        } else {
            hyperPatterns
        }
    }

    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    private val NON_LETTER_OR_DIGIT_REGEX = Regex("[^A-Z0-9]")
    // Anchored to end so keyword prefix (e.g. "MEDICALINSURANCEID") is ignored
    private val ALPHANUMERIC_ID_TAIL = Regex("""[A-Z]{2,3}[0-9]{8,9}$""")
    private val NUMERIC_ID_TAIL = Regex("""[0-9]{8,15}$""")
    private val MBI_EXCLUDED_LETTERS = setOf('S', 'L', 'O', 'I', 'B', 'Z')

    override fun check(value: String): Boolean {
        val cleaned = value.replace(NON_LETTER_OR_DIGIT_REGEX, "").uppercase().trim()

        if (cleaned.length == 11 && isMBI(cleaned)) return true

        val alphaMatch = ALPHANUMERIC_ID_TAIL.find(cleaned)
        if (alphaMatch != null) return isValidInsuranceDigits(alphaMatch.value.dropWhile { it.isLetter() })

        val numericMatch = NUMERIC_ID_TAIL.find(cleaned)
        if (numericMatch != null) return isValidInsuranceDigits(numericMatch.value)

        return false
    }

    private fun isMBI(mbi: String): Boolean {
        if (mbi.any { it.isLetter() && it in MBI_EXCLUDED_LETTERS }) return false
        if (!mbi[0].isDigit() || mbi[0] == '0') return false
        if (!mbi[1].isLetter()) return false
        if (!mbi[3].isDigit()) return false
        if (!mbi[4].isLetter()) return false
        if (!mbi[6].isDigit()) return false
        if (!mbi[7].isLetter()) return false
        if (!mbi[8].isLetter()) return false
        if (!mbi[9].isDigit()) return false
        if (!mbi[10].isDigit()) return false
        return mbi.count { it.isLetter() } >= 3
    }

    private fun isValidInsuranceDigits(digits: String): Boolean {
        if (digits.isEmpty() || digits.all { it == '0' }) return false
        if (digits.all { it == digits[0] }) return false
        return true
    }

    override fun toString() = name
}

