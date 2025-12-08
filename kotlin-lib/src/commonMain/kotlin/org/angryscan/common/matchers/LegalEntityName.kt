package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object LegalEntityName : IHyperMatcher, IKotlinMatcher {
    override val name = "Legal Entity Name"
    override val javaPatterns = listOf(
        """
        (?ix)
        (?:(?<=^)|(?<=[\s\(\[\{«"'])|(?<![\p{L}\d]))
        (?:
          наименование\s+ЮЛ|
          полное\s+наименование\s+юридического\s+лица|
          краткое\s+наименование\s+ЮЛ|
          наименование\s+организации
        )?
        \s*
        (?:
        (?:
          ООО|ПАО|ЗАО|НАО|ИП|ФГУП|ГУП|МУП|Фонд|Ассоциация|Союз|
          Общество\s+с\s+ограниченной\s+ответственностью|
          Акционерное\s+общество|
          Публичное\s+акционерное\s+общество
        )
          \s+
          ["']?[\p{L}\s]{5,200}["']?
        )
        (?![\p{L}\d])
        """.trimIndent()
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?:^|[\s\(\[\{«"'])ООО\s+[а-яА-ЯёЁ\s]{5,}""",
        """(?:^|[\s\(\[\{«"'])(?:ПАО|ЗАО|НАО)\s+[а-яА-ЯёЁ\s]{5,}""",
        """(?:^|[\s\(\[\{«"'])ИП\s+[а-яА-ЯёЁ\s]{5,}""",
        """(?:^|[\s\(\[\{«"'])(?:ФГУП|ГУП|МУП)\s+[а-яА-ЯёЁ\s]{5,}""",
        """(?:Фонд|Ассоциация|Союз)\s+[а-яА-ЯёЁ\s]{5,}""",
        """Общество\s+с\s+ограниченной\s+ответственностью\s+[а-яА-ЯёЁ\s]{5,}""",
        """(?:Акционерное|Публичное\s+акционерное)\s+общество\s+[а-яА-ЯёЁ\s]{5,}"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Удаляем кавычки и скобки из начала и конца для проверки
        var text = value.trim()
        text = text.trimStart('"', '\'', '(', '[', '{', '«')
        text = text.trimEnd('"', '\'', ')', ']', '}', '»')
        text = text.trim()
        
        if (text.length < 5) return false
        
        // Проверка на отсутствие цифр
        if (text.any { it.isDigit() }) return false
        
        // Проверка на отсутствие знаков пунктуации (кроме пробелов)
        if (text.any { !it.isLetter() && !it.isWhitespace() }) return false
        
        val legalEntityPrefixes = setOf("ооо", "пао", "зао", "нао", "ип", "фгуп", "гуп", "муп", "фонд", "ассоциация", "союз")
        val words = text.split(Regex("[\\s]+")).filter { it.isNotBlank() }
        
        if (words.isEmpty()) return false
        
        val prefixIndices = words.mapIndexedNotNull { index, word -> 
            if (legalEntityPrefixes.contains(word.lowercase())) index else null
        }
        
        if (prefixIndices.size > 1) {
            for (i in 0 until prefixIndices.size - 1) {
                if (prefixIndices[i + 1] - prefixIndices[i] <= 2) return false
            }
        }
        
        val nameWords = words.filter { it.length > 2 }
        
        var maxConsecutiveSame = 1
        var currentConsecutive = 1
        for (i in 1 until text.length) {
            val current = text[i].lowercaseChar()
            val previous = text[i - 1].lowercaseChar()
            if (current == previous && current.isLetter()) {
                currentConsecutive++
                maxConsecutiveSame = maxOf(maxConsecutiveSame, currentConsecutive)
            } else {
                currentConsecutive = 1
            }
        }
        if (maxConsecutiveSame > 4) return false
        
        val letters = text.count { it.isLetter() }
        
        if (letters == 0) return false
        
        if (text.contains("  ") || text.contains("\t\t")) return false
        
        val letterCounts = text.filter { it.isLetter() }.groupingBy { it.lowercaseChar() }.eachCount()
        val maxLetterCount = letterCounts.values.maxOrNull() ?: 0
        if (maxLetterCount > letters * 0.4) return false
        
        val vowels = setOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я', 'a', 'e', 'i', 'o', 'u', 'y')
        val consonants = setOf('б', 'в', 'г', 'д', 'ж', 'з', 'й', 'к', 'л', 'м', 'н', 'п', 'р', 'с', 'т', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z')
        
        var maxConsecutiveConsonants = 0
        var currentConsecutiveConsonants = 0
        var vowelCount = 0
        
        for (char in text.lowercase()) {
            if (char.isLetter()) {
                if (vowels.contains(char)) {
                    vowelCount++
                    currentConsecutiveConsonants = 0
                } else if (consonants.contains(char)) {
                    currentConsecutiveConsonants++
                    maxConsecutiveConsonants = maxOf(maxConsecutiveConsonants, currentConsecutiveConsonants)
                } else {
                    currentConsecutiveConsonants = 0
                }
            } else {
                currentConsecutiveConsonants = 0
            }
        }
        
        if (maxConsecutiveConsonants > 4) return false
        
        if (letters > 0 && vowelCount < letters * 0.1) return false
        
        val wordCounts = nameWords.groupingBy { it.lowercase() }.eachCount()
        if (wordCounts.values.any { it >= 2 }) return false
        
        val spaces = text.count { it.isWhitespace() }
        if (spaces > text.length * 0.4) return false
        
        return true
    }

    override fun toString() = name
}
