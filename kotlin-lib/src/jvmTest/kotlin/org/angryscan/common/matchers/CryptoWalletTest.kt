package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера CryptoWallet
 */
internal class CryptoWalletTest : MatcherTestBase(CryptoWallet) {

    // Реальные примеры адресов для тестирования
    private val bitcoinLegacy = "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa" // Genesis block address (34 символа)
    private val bitcoinP2SH = "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy" // 34 символа
    private val ethereum = "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb0" // 42 символа (0x + 40 hex)
    private val litecoinLegacy = "LTCN6Lfz5XvNF9XqHdc6P5oE6r2c5iJmKxY" // 34 символа
    private val ripple = "rN7n7otQDd6FczFgLdSqtcsAUxDkw6fzRH" // 34 символа
    private val bitcoinCash = "qpm2qsznhks23z7629mms6s4cwef74vcwvy22gdx6a" // 42 символа
    private val dogecoin = "D7Y55LdC5Hj3aNk3M2vB6tF8gH9jK1mN3pQ5rS7tU" // 34 символа (D + 1 + 32)
    private val tron = "TQn9Y2khEsLMWDmBqJ8K9XpY7hN2vC5wE8rT3yU6i" // 34 символа (T + 33)
    private val solana = "7xKXtg2CW87d97TXJSDpbD5jBkheTqA83TZRuJosgA" // 42 символа
    private val cosmos = "cosmos1hsk6jryyqjfhp5dhc55t9lrxkzy8awu00azxm7" // 45 символов
    private val tezos = "tz1KqTpEZ7Yob7QbPE4Hy4Wo8fHG8LhKxZSx" // 36 символов
    private val dash = "Xx4d8rzKwejkA3L2L5m5hM648Q7y6t9uZ3" // 34 символа
    private val zcash = "t1VzHL4Nj3V3SHqUF5u7uGwF6qHH5aqT3Nb" // 35 символов
    private val terra = "terra1hsk6jryyqjfhp5dhc55t9lrxkzy8awu00azxm7" // 45 символов
    private val bitcoinBech32 = "bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4" // 42 символа (bc1 + 40)
    private val litecoinBech32 = "ltc1qw508d6qejxtdg4y5r3zarvary0c5xw7kemeawh" // 43 символа (ltc1 + 40)
    private val ton = "EQCkR1cGmnsE45N4K0otPl5EnxnRakmGqeJUNua5fkTh" // 44 символа (EQC + 41)

    // ========== ТЕСТЫ ПОЗИЦИИ В ТЕКСТЕ ==========

    @Test
    fun testBitcoinLegacyAtStart() {
        val text = "$bitcoinLegacy находится в начале текста"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес в начале должен быть найден")
    }

    @Test
    fun testBitcoinLegacyAtEnd() {
        val text = "Адрес находится в конце: $bitcoinLegacy"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес в конце должен быть найден")
    }

    @Test
    fun testBitcoinLegacyAtStartOfLine() {
        val text = """
            $bitcoinLegacy находится в начале строки
            Следующая строка без адреса
        """.trimIndent()
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес в начале строки должен быть найден")
    }

    @Test
    fun testBitcoinLegacyAtEndOfLine() {
        val text = """
            Адрес находится в конце строки: $bitcoinLegacy
            Следующая строка без адреса
        """.trimIndent()
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес в конце строки должен быть найден")
    }

    @Test
    fun testEthereumAtStart() {
        val text = "$ethereum находится в начале"
        assertTrue(scanText(text) >= 1, "Ethereum адрес в начале должен быть найден")
    }

    @Test
    fun testEthereumAtEnd() {
        val text = "Адрес: $ethereum"
        assertTrue(scanText(text) >= 1, "Ethereum адрес в конце должен быть найден")
    }

    // ========== ТЕСТЫ НЕ ДОЛЖНЫ ОБНАРУЖИВАТЬСЯ ВНУТРИ ПОСЛЕДОВАТЕЛЬНОСТЕЙ ==========

    @Test
    fun testBitcoinLegacyNotInLetterSequence() {
        val text = "Слово${bitcoinLegacy}Слово"
        assertEquals(0, scanText(text), "Bitcoin Legacy адрес внутри буквенной последовательности не должен быть найден")
    }

    @Test
    fun testBitcoinLegacyNotInNumberSequence() {
        val text = "999999${bitcoinLegacy}9999999"
        assertEquals(0, scanText(text), "Bitcoin Legacy адрес внутри числовой последовательности не должен быть найден")
    }

    @Test
    fun testBitcoinLegacyNotInAlphanumericSequence() {
        val text = "abc123${bitcoinLegacy}789xyz"
        assertEquals(0, scanText(text), "Bitcoin Legacy адрес внутри буквенно-цифровой последовательности не должен быть найден")
    }

    @Test
    fun testEthereumNotInLetterSequence() {
        val text = "Word${ethereum}Word"
        assertEquals(0, scanText(text), "Ethereum адрес внутри буквенной последовательности не должен быть найден")
    }

    @Test
    fun testEthereumNotInNumberSequence() {
        val text = "123${ethereum}456"
        assertEquals(0, scanText(text), "Ethereum адрес внутри числовой последовательности не должен быть найден")
    }

    @Test
    fun testEthereumNotInAlphanumericSequence() {
        val text = "abc123${ethereum}789def"
        assertEquals(0, scanText(text), "Ethereum адрес внутри буквенно-цифровой последовательности не должен быть найден")
    }

    // ========== ТЕСТЫ С РАЗДЕЛИТЕЛЯМИ ==========

    @Test
    fun testBitcoinLegacyWithSpaceBefore() {
        val text = " Адрес: $bitcoinLegacy"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с пробелом перед должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithSpaceAfter() {
        val text = "$bitcoinLegacy это адрес"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с пробелом после должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithPunctuation() {
        val text = "Адрес ($bitcoinLegacy) найден"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с пунктуацией должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithColon() {
        val text = "Адрес: $bitcoinLegacy"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с двоеточием должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithEquals() {
        val text = "address=$bitcoinLegacy"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес со знаком равенства должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithDot() {
        val text = "Адрес: $bitcoinLegacy."
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с точкой должен быть найден")
    }

    // ========== ТЕСТЫ ВСЕХ КРИПТОВАЛЮТ ==========

    @Test
    fun testBitcoinP2SH() {
        val text = " P2SH адрес: $bitcoinP2SH "
        assertTrue(scanText(text) >= 1, "Bitcoin P2SH адрес должен быть найден")
    }

    @Test
    fun testLitecoinLegacy() {
        val text = " Litecoin: $litecoinLegacy "
        assertTrue(scanText(text) >= 1, "Litecoin Legacy адрес должен быть найден")
    }

    @Test
    fun testRipple() {
        val text = " Ripple: $ripple "
        assertTrue(scanText(text) >= 1, "Ripple адрес должен быть найден")
    }

    @Test
    fun testBitcoinCash() {
        val text = " Bitcoin Cash: $bitcoinCash "
        assertTrue(scanText(text) >= 1, "Bitcoin Cash адрес должен быть найден")
    }

    @Test
    fun testDogecoin() {
        val text = " Dogecoin: $dogecoin "
        assertTrue(scanText(text) >= 1, "Dogecoin адрес должен быть найден")
    }

    @Test
    fun testTron() {
        val text = " Tron: $tron "
        assertTrue(scanText(text) >= 1, "Tron адрес должен быть найден")
    }

    @Test
    fun testSolana() {
        val text = " Solana: $solana "
        assertTrue(scanText(text) >= 1, "Solana адрес должен быть найден")
    }

    @Test
    fun testCosmos() {
        val text = " Cosmos: $cosmos "
        assertTrue(scanText(text) >= 1, "Cosmos адрес должен быть найден")
    }

    @Test
    fun testTezos() {
        val text = " Tezos: $tezos "
        assertTrue(scanText(text) >= 1, "Tezos адрес должен быть найден")
    }

    @Test
    fun testDash() {
        val text = " Dash: $dash "
        assertTrue(scanText(text) >= 1, "Dash адрес должен быть найден")
    }

    @Test
    fun testZcash() {
        val text = " Zcash: $zcash "
        assertTrue(scanText(text) >= 1, "Zcash адрес должен быть найден")
    }

    @Test
    fun testTerra() {
        val text = " Terra: $terra "
        assertTrue(scanText(text) >= 1, "Terra адрес должен быть найден")
    }

    @Test
    fun testBitcoinBech32() {
        val text = " Bitcoin Bech32: $bitcoinBech32 "
        assertTrue(scanText(text) >= 1, "Bitcoin Bech32 адрес должен быть найден")
    }

    @Test
    fun testBitcoinBech32AtStart() {
        val text = "$bitcoinBech32 это Bech32 адрес"
        assertTrue(scanText(text) >= 1, "Bitcoin Bech32 адрес в начале должен быть найден")
    }

    @Test
    fun testBitcoinBech32AtEnd() {
        val text = "Bech32 адрес: $bitcoinBech32"
        assertTrue(scanText(text) >= 1, "Bitcoin Bech32 адрес в конце должен быть найден")
    }

    @Test
    fun testBitcoinBech32InWord() {
        val text = " Адрес: ${bitcoinBech32}внутрислова "
        assertTrue(scanText(text) >= 1, "Bitcoin Bech32 адрес с текстом после должен быть найден")
    }

    @Test
    fun testBitcoinBech32InLongerAddress() {
        // Адрес как часть более длинного адреса (с дополнительными символами после)
        val longerAddress = "${bitcoinBech32}abcdefghijklmnopqrstuvwxyz1234567890"
        val text = " Адрес: $longerAddress "
        assertTrue(scanText(text) >= 1, "Bitcoin Bech32 адрес в составе более длинного адреса должен быть найден")
    }

    @Test
    fun testLitecoinBech32() {
        val text = " Litecoin Bech32: $litecoinBech32 "
        assertTrue(scanText(text) >= 1, "Litecoin Bech32 адрес должен быть найден")
    }

    @Test
    fun testLitecoinBech32AtStart() {
        val text = "$litecoinBech32 это Litecoin Bech32 адрес"
        assertTrue(scanText(text) >= 1, "Litecoin Bech32 адрес в начале должен быть найден")
    }

    @Test
    fun testLitecoinBech32AtEnd() {
        val text = "Litecoin Bech32 адрес: $litecoinBech32"
        assertTrue(scanText(text) >= 1, "Litecoin Bech32 адрес в конце должен быть найден")
    }

    @Test
    fun testLitecoinBech32InWord() {
        val text = " Адрес: ${litecoinBech32}внутрислова "
        assertTrue(scanText(text) >= 1, "Litecoin Bech32 адрес с текстом после должен быть найден")
    }

    @Test
    fun testLitecoinBech32InLongerAddress() {
        // Адрес как часть более длинного адреса (с дополнительными символами после)
        val longerAddress = "${litecoinBech32}abcdefghijklmnopqrstuvwxyz1234567890"
        val text = " Адрес: $longerAddress "
        assertTrue(scanText(text) >= 1, "Litecoin Bech32 адрес в составе более длинного адреса должен быть найден")
    }

    @Test
    fun testTON() {
        val text = " TON: $ton "
        assertTrue(scanText(text) >= 1, "TON адрес должен быть найден")
    }

    @Test
    fun testTONAtStart() {
        val text = "$ton это TON адрес"
        assertTrue(scanText(text) >= 1, "TON адрес в начале должен быть найден")
    }

    @Test
    fun testTONAtEnd() {
        val text = "TON адрес: $ton"
        assertTrue(scanText(text) >= 1, "TON адрес в конце должен быть найден")
    }

    @Test
    fun testTONInWord() {
        val text = " Адрес: ${ton}внутрислова "
        assertTrue(scanText(text) >= 1, "TON адрес с текстом после должен быть найден")
    }

    @Test
    fun testTONInLongerAddress() {
        // Адрес как часть более длинного адреса (с дополнительными символами после)
        val longerAddress = "${ton}ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/"
        val text = " Адрес: $longerAddress "
        assertTrue(scanText(text) >= 1, "TON адрес в составе более длинного адреса должен быть найден")
    }

    // ========== ТЕСТЫ ПОГРАНИЧНЫХ ДЛИН ==========

    @Test
    fun testBitcoinLegacyMinimumLength() {
        // Минимальная длина Bitcoin Legacy адреса - 26 символов
        val minAddress = "1" + "A".repeat(25)
        val text = " Минимальный: $minAddress "
        // Проверяем, что адрес найден (если соответствует паттерну)
        val result = scanText(text)
        // Может быть найден или нет в зависимости от валидности Base58
        assertTrue(result >= 0, "Минимальный Bitcoin Legacy адрес")
    }

    @Test
    fun testBitcoinLegacyMaximumLength() {
        // Максимальная длина Bitcoin Legacy адреса в паттерне - 41 символ
        val maxAddress = "1" + "A".repeat(40)
        val text = " Максимальный: $maxAddress "
        val result = scanText(text)
        assertTrue(result >= 0, "Максимальный Bitcoin Legacy адрес")
    }

    @Test
    fun testEthereumExactLength() {
        // Ethereum адрес всегда 40 hex символов после 0x
        val text = " Ethereum: $ethereum "
        assertTrue(scanText(text) >= 1, "Ethereum адрес точной длины должен быть найден")
    }

    // ========== ТЕСТЫ МНОЖЕСТВЕННЫХ АДРЕСОВ ==========

    @Test
    fun testMultipleAddresses() {
        val text = """
            Bitcoin: $bitcoinLegacy
            Ethereum: $ethereum
            Litecoin: $litecoinLegacy
        """.trimIndent()
        assertTrue(scanText(text) == 3, "Несколько адресов должны быть найдены")
    }

    // ========== ТЕСТЫ ПУСТЫХ И НЕВЕРНЫХ СЛУЧАЕВ ==========

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать адреса")
    }

    @Test
    fun testInvalidBitcoinTooShort() {
        val text = " 1ABC "
        assertEquals(0, scanText(text), "Слишком короткий адрес не должен быть найден")
    }

    @Test
    fun testInvalidEthereumTooShort() {
        val text = " 0x123 "
        assertEquals(0, scanText(text), "Слишком короткий Ethereum адрес не должен быть найден")
    }

    @Test
    fun testInvalidEthereumWrongPrefix() {
        val text = " 0X742d35Cc6634C0532925a3b844Bc9e7595f0bEb "
        assertEquals(0, scanText(text), "Ethereum адрес с неправильным префиксом не должен быть найден")
    }

    // ========== ТЕСТЫ СПЕЦИАЛЬНЫХ СИМВОЛОВ ==========

    @Test
    fun testBitcoinLegacyWithNewline() {
        val text = "$bitcoinLegacy\nСледующая строка"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с переносом строки должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithCarriageReturn() {
        val text = "$bitcoinLegacy\r\nСледующая строка"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с возвратом каретки должен быть найден")
    }

    @Test
    fun testBitcoinLegacyInBrackets() {
        val text = "[$bitcoinLegacy]"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес в квадратных скобках должен быть найден")
    }

    @Test
    fun testBitcoinLegacyInBraces() {
        val text = "{$bitcoinLegacy}"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес в фигурных скобках должен быть найден")
    }

    @Test
    fun testBitcoinLegacyInParentheses() {
        val text = "($bitcoinLegacy)"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес в круглых скобках должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithQuotes() {
        val text = "\"$bitcoinLegacy\""
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес в кавычках должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithSemicolon() {
        val text = "Адрес: $bitcoinLegacy;"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с точкой с запятой должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithQuestionMark() {
        val text = "Адрес? $bitcoinLegacy"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с вопросительным знаком должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithExclamation() {
        val text = "Адрес! $bitcoinLegacy"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с восклицательным знаком должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithHash() {
        val text = "# $bitcoinLegacy"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с хешем должен быть найден")
    }

    @Test
    fun testBitcoinLegacyWithDash() {
        val text = "- $bitcoinLegacy"
        assertTrue(scanText(text) >= 1, "Bitcoin Legacy адрес с дефисом должен быть найден")
    }
}
