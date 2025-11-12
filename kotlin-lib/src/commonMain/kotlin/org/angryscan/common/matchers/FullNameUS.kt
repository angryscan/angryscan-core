package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

expect fun readResource(path: String): String?

@Serializable
object FullNameUS : IHyperMatcher, IKotlinMatcher {
    override val name = "Full Name US"
    
    private val topFirstNames: Set<String> by lazy {
        loadNamesFromResource("us_first_names.txt")
    }
    
    private val topLastNames: Set<String> by lazy {
        loadNamesFromResource("us_last_names.txt")
    }
    
    private fun loadNamesFromResource(fileName: String): Set<String> {
        return try {
            val content = readResource(fileName) ?: return emptySet()
            content.lines()
                .map { it.trim().lowercase() }
                .filter { it.isNotEmpty() }
                .toSet()
        } catch (_: Exception) {
            emptySet()
        }
    }

    override val javaPatterns = listOf(
        """(?:^|[\s\r\n])([A-Z][a-z]{1,15}(?:\s+[A-Z]\.?)?\s+[A-Z][a-z]{2,15})(?=[\s\r\n.,;:!?\-]|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """[\s\r\n][A-Z][a-z]{1,15}(?:\s+[A-Z]\.?)?\s+[A-Z][a-z]{2,15}[\s\r\n.,;:!?\-]"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        if (value.any { it.isDigit() }) {
            return false
        }
        
        val words = value.trim().split(Regex("\\s+")).filter { it.isNotEmpty() && it != "." }
        if (words.size < 2) return false
        
        val cities = setOf("new", "los", "san", "las", "washington", "ocean")
        if (cities.contains(words[0].lowercase())) {
            return false
        }
        
        val timeWords = setOf(
            "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday",
            "january", "february", "march", "august", "september", "october", "november", "december"
        )
        if (words.any { timeWords.contains(it.lowercase()) }) {
            return false
        }
        
        val technicalWords = setOf(
            "server", "system", "network", "computer", "software", "hardware",
            "database", "certificate", "connection", "template", "section",
            "cloud", "fields", "sloan", "management", "review", "press",
            "pretty", "privacy", "university", "college", "institute", "brother",
            "post", "park", "times"
        )
        if (words.any { technicalWords.contains(it.lowercase()) }) {
            return false
        }
        
        if (words[0].equals(words.last(), ignoreCase = true)) {
            return false
        }
        
        for (word in words) {
            if (word.length < 1) return false
            
            if (!word[0].isUpperCase()) {
                return false
            }
            
            for (i in 1 until word.length) {
                val char = word[i]
                if (char.isLetter() && !char.isLowerCase() && char != '.') {
                    return false
                }
            }
        }
        
        if (topFirstNames.isNotEmpty() && topLastNames.isNotEmpty()) {
            val firstName = words[0].lowercase()
            val lastNameLower = words.last().lowercase()
            
            if (!topFirstNames.contains(firstName) && !topLastNames.contains(lastNameLower)) {
                return false
            }
        }
        
        return true
    }

    override fun toString() = name
}
