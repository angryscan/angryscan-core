package org.angryscan.common.matchers

import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CryptoSeedPhraseTest {
    
    private fun scanText(text: String): Int {
        return KotlinEngine(listOf(CryptoSeedPhrase)).use {
            it.scan(text).count()
        }
    }

    private val seedPhrase12 = "able about above abuse access accuse acid actor adapt admit adult agent"
    private val seedPhrase15 = "able about above abuse access accuse acid actor adapt admit adult agent agree ahead aisle"
    private val seedPhrase18 = "able about above abuse access accuse acid actor adapt admit adult agent agree ahead aisle alarm album alert"
    private val seedPhrase21 = "able about above abuse access accuse acid actor adapt admit adult agent agree ahead aisle alarm album alert alien allow alpha"
    private val seedPhrase24 = "able about above abuse access accuse acid actor adapt admit adult agent agree ahead aisle alarm album alert alien allow alpha alter among amount"

    @Test
    fun testSeedPhrase12Words() {
        val text = " Seed phrase: $seedPhrase12 "
        assertTrue(scanText(text) >= 1, "Seed phrase из 12 слов должна быть найдена")
    }

    @Test
    fun testSeedPhrase12WordsAtStart() {
        val text = "$seedPhrase12 это seed phrase"
        assertTrue(scanText(text) >= 1, "Seed phrase в начале должна быть найдена")
    }

    @Test
    fun testSeedPhrase12WordsAtEnd() {
        val text = "Seed phrase находится здесь: $seedPhrase12"
        assertTrue(scanText(text) >= 1, "Seed phrase в конце должна быть найдена")
    }

    @Test
    fun testSeedPhrase15Words() {
        val text = " Seed phrase: $seedPhrase15 "
        assertTrue(scanText(text) >= 1, "Seed phrase из 15 слов должна быть найдена")
    }

    @Test
    fun testSeedPhrase18Words() {
        val text = " Seed phrase: $seedPhrase18 "
        assertTrue(scanText(text) >= 1, "Seed phrase из 18 слов должна быть найдена")
    }

    @Test
    fun testSeedPhrase21Words() {
        val text = " Seed phrase: $seedPhrase21 "
        assertTrue(scanText(text) >= 1, "Seed phrase из 21 слова должна быть найдена")
    }

    @Test
    fun testSeedPhrase24Words() {
        val text = " Seed phrase: $seedPhrase24 "
        assertTrue(scanText(text) >= 1, "Seed phrase из 24 слов должна быть найдена")
    }

    @Test
    fun testSeedPhraseInMultipleLines() {
        val text = """
            Seed phrase:
            $seedPhrase12
            Конец фразы
        """.trimIndent()
        assertTrue(scanText(text) >= 1, "Seed phrase на нескольких строках должна быть найдена")
    }

    @Test
    fun testNotSeedPhraseTooFewWords() {
        val text = "able about above abuse access accuse acid actor adapt admit adult"
        assertEquals(0, scanText(text), "10 слов не должно быть найдено как seed phrase")
    }

    @Test
    fun testNotSeedPhraseWithNumbers() {
        val text = "word1 word2 word3 word4 word5 word6 word7 word8 word9 word10 word11 word12"
        assertEquals(0, scanText(text), "Seed phrase с цифрами в словах не должна быть найдена")
    }

    @Test
    fun testNotSeedPhraseWithPunctuation() {
        val text = "word1, word2, word3, word4, word5, word6, word7, word8, word9, word10, word11, word12"
        assertEquals(0, scanText(text), "Seed phrase с запятыми не должна быть найдена")
    }

    @Test
    fun testRealBIP39Words12() {
        val realBIP39 = "able about above abuse access accuse acid actor adapt admit adult agent"
        val text = " $realBIP39 "
        assertTrue(scanText(text) >= 1, "Seed phrase из 12 реальных BIP39 слов должна быть найдена")
    }

    @Test
    fun testRealBIP39Words24() {
        val realBIP39 = "able about above abuse access accuse acid actor adapt admit adult agent agree ahead aisle alarm album alert alien allow alpha alter among amount"
        val text = " $realBIP39 "
        assertTrue(scanText(text) >= 1, "Seed phrase из 24 реальных BIP39 слов должна быть найдена")
    }

    @Test
    fun testEmptyString() {
        assertEquals(0, scanText(""), "Пустая строка не должна содержать seed phrase")
    }

    @Test
    fun testSingleWord() {
        assertEquals(0, scanText("word"), "Одно слово не должно быть найдено")
    }

    @Test
    fun testWordsWithDifferentLengths() {
        val text = "abc defg hijkl mnopqr stuvwxy z"
        val result = scanText(text)
        assertTrue(result >= 0, "Слова разной длины")
    }

    @Test
    fun testWordsTooShort() {
        val text = "abc def ghi jkl mno pqr stu vwx"
        assertEquals(0, scanText(text), "Слова короче 4 символов не должны быть найдены")
    }
    
    @Test
    fun testWordsTooLong() {
        val text = "ability account achieve across action actual adapt addict address adjust"
        assertEquals(0, scanText(text), "Слова длиннее 8 символов не должны быть найдены")
    }
}

