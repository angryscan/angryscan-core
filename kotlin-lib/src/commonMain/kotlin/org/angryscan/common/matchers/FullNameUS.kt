package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object FullNameUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Full Name US"

    private val allKeywords = listOf(
        "name", "full name", "fullname", "employee", "person",
        "patient", "client", "customer", "applicant", "candidate",
        
        "worker", "staff", "manager", "supervisor", "director",
        "doctor", "physician", "nurse", "attorney", "lawyer",
        
        "student", "teacher", "professor", "instructor", "parent",
        "father", "mother", "spouse", "husband", "wife",
        
        "guardian", "child", "contact", "vendor", "contractor",
        "partner", "agent", "investor", "borrower", "banker",
        
        "accountant", "resident", "tenant", "driver", "passenger",
        "owner", "member", "witness", "defendant", "plaintiff"
    )

    override val javaPatterns = listOf(
        """(?i)(?:${allKeywords.joinToString("|")})[\s\-:]{0,5}([A-Z][a-z]{1,15}(?:\s+[A-Z]\.?)?\s+[A-Z][a-z]{2,15})(?![A-Za-z])"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = allKeywords.chunked(10).map { group ->
        """(?i)(?:${group.joinToString("|")})[\s\-:]{0,5}([A-Z][a-z]{1,15}(?:\s+[A-Z]\.?)?\s+[A-Z][a-z]{2,15})\b"""
    }
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        val falseList = listOf(
            "New York",
            "Los Angeles",
            "San Francisco",
            "Las Vegas",
            "United States",
            "North America",
            "South America",
            "Central Park",
            "Wall Street",
            "Main Street",
            "High School",
            "Middle School",
            "Elementary School",
            "York"
        )
        return !falseList.any { value.contains(it, ignoreCase = true) }
    }

    override fun toString() = name
}
