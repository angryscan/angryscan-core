package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object CryptoSeedPhrase : IKotlinMatcher {
    override val name = "Crypto Seed Phrase"
    
    private val bip39Words: Set<String> by lazy {
        loadBIP39Words()
    }
    
    private fun loadBIP39Words(): Set<String> {
        return try {
            val content = readResource("english-bip0039.txt") ?: return emptySet()
            content.lines()
                .map { it.trim().lowercase() }
                .filter { it.isNotEmpty() }
                .toSet()
        } catch (_: Exception) {
            emptySet()
        }
    }
    
    override val javaPatterns = listOf(
        """(?:^|\s) (?:\b[a-z]{3,8}\b\s){11}\b[a-z]{3,8}\b(?=\s|$)""",
        """(?:^|\s)(?:\b[a-z]{3,8}\b\s){14}\b[a-z]{3,8}\b(?=\s|$)""",
        """(?:^|\s)(?:\b[a-z]{3,8}\b\s){17}\b[a-z]{3,8}\b(?=\s|$)""",
        """(?:^|\s)(?:\b[a-z]{3,8}\b\s){20}\b[a-z]{3,8}\b(?=\s|$)""",
        """(?:^|\s)(?:\b[a-z]{3,8}\b\s){23}\b[a-z]{3,8}\b(?=\s|$)"""
    )
    
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override fun check(value: String): Boolean {
        val words = value.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        
        if (words.isEmpty()) return false
        
        if (!words.all { word -> word.isNotEmpty() && word[0].isLowerCase() }) {
            return false
        }
        
        if (words.size !in listOf(12, 15, 18, 21, 24)) {
            return false
        }
        
        if (!words.all { word -> word.length in 3..8 && word.all { it.isLetter() } }) {
            return false
        }
        
        val wordCounts = words.groupingBy { it }.eachCount()
        val maxRepetitions = wordCounts.values.maxOrNull() ?: 0
        if (maxRepetitions > 4) {
            return false
        }
        
        val uniqueWords = words.toSet().size
        val minUniqueRatio = 0.4
        if (uniqueWords.toDouble() / words.size < minUniqueRatio) {
            return false
        }
        
        if (bip39Words.isNotEmpty()) {
            if (!words.all { word -> bip39Words.contains(word.lowercase()) }) {
                return false
            }
        }
        
        return true
    }

    override fun toString() = name
}

