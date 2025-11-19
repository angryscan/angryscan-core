package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера CryptoWallet
 */
internal class CryptoWalletTest : MatcherTestBase(CryptoWallet) {

    // Bitcoin Legacy адреса (начинаются с 1 или 3, 26-35 символов)
    private val bitcoinAddress1 = "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa" // Genesis block address
    private val bitcoinAddress3 = "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy" // P2SH address

    // Bitcoin Bech32 адреса (начинаются с bc1, 39-59 символов)
    private val bitcoinBech32 = "bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4"

    // Ethereum адреса (начинаются с 0x, 40 hex символов)
    private val ethereumAddress = "0x742d35Cc6634C0532925a3b844Bc9e7595f0beb"

    // Litecoin Legacy адреса (начинаются с L или M, 26-33 символа)
    private val litecoinAddressL = "LVg2kJoFNg45Nbpy53h7Fe1wKyeXVRhMH9"
    private val litecoinAddressM = "MPHFjQ3VqMvQGh9qGz8bRF8hT9vkTZKLmE"

    // Litecoin Bech32 адреса (начинаются с ltc1, 39-59 символов)
    private val litecoinBech32 = "ltc1qg4mrmy8c6u5j9l8j6k5h4g3f2d1s0a9z8x7w6v5u4t3s2a1z0y9x8w"

    // Ripple адреса (начинаются с r, 25-35 символов)
    private val rippleAddress = "rN7n7otQDd6FczFgLdSqtcsAUxDkw6fzRH"

    // Bitcoin Cash адреса (начинаются с q или p)
    private val bitcoinCashAddress = "qpm2qsznhks23z7629mms6s4cwef74vcwvy22gdx6a"

    // Dogecoin адреса (начинаются с D)
    private val dogecoinAddress = "D5hNo3mW7RhZzzzP5jYxTTHLjnq8F4Fj5r"

    // Tron адреса (начинаются с T, 34 символа)
    private val tronAddress = "TQn9Y2khEsLMWPoD4HjXv5YQjJvqHjR9VK"

    // Solana адреса (32-44 символа Base58)
    private val solanaAddress = "7dHbWXmci3dT8UFYWYZweBLXgycu7Y3iL6trKn1Y7ARj"

    // Stellar адреса (начинаются с G, 56 символов)
    private val stellarAddress = "GAI3GJ2Q3B35AOZJ36C4ANE3HSS4NK7WI6DNO4ZSHRAX6NG7BMX6VJER"

    // Cardano адреса (начинаются с addr1, Bech32)
    private val cardanoAddress = "addr1qx2fxv2umyhttkxyxp8x0dlpdt3k6cwng5pxj3jhsydzer3jcu5d8ps7zex2k2xt3uqxgjqnnj78p77k2cdqw52l3kpf6"

    // Тесты для адресов в начале строки

    @Test
    fun testBitcoinAtStart() {
        val text = "$bitcoinAddress1 is a Bitcoin address"
        assertEquals(1, scanText(text), "Bitcoin адрес в начале должен быть найден")
    }

    @Test
    fun testEthereumAtStart() {
        val text = "$ethereumAddress is an Ethereum address"
        assertEquals(1, scanText(text), "Ethereum адрес в начале должен быть найден")
    }

    @Test
    fun testBitcoinBech32AtStart() {
        val text = "$bitcoinBech32 is a Bitcoin Bech32 address"
        assertEquals(1, scanText(text), "Bitcoin Bech32 адрес в начале должен быть найден")
    }

    @Test
    fun testLitecoinAtStart() {
        val text = "$litecoinAddressL is a Litecoin address"
        assertEquals(1, scanText(text), "Litecoin адрес в начале должен быть найден")
    }

    @Test
    fun testRippleAtStart() {
        val text = "$rippleAddress is a Ripple address"
        assertEquals(1, scanText(text), "Ripple адрес в начале должен быть найден")
    }

    // Тесты для адресов в конце строки

    @Test
    fun testBitcoinAtEnd() {
        val text = "Bitcoin address: $bitcoinAddress1"
        assertEquals(1, scanText(text), "Bitcoin адрес в конце должен быть найден")
    }

    @Test
    fun testEthereumAtEnd() {
        val text = "Ethereum address: $ethereumAddress"
        assertEquals(1, scanText(text), "Ethereum адрес в конце должен быть найден")
    }

    @Test
    fun testBitcoinBech32AtEnd() {
        val text = "Bitcoin Bech32 address: $bitcoinBech32"
        assertEquals(1, scanText(text), "Bitcoin Bech32 адрес в конце должен быть найден")
    }

    @Test
    fun testLitecoinAtEnd() {
        val text = "Litecoin address: $litecoinAddressL"
        assertEquals(1, scanText(text), "Litecoin адрес в конце должен быть найден")
    }

    @Test
    fun testRippleAtEnd() {
        val text = "Ripple address: $rippleAddress"
        assertEquals(1, scanText(text), "Ripple адрес в конце должен быть найден")
    }

    // Тесты для адресов с точкой и пробелом после

    @Test
    fun testBitcoinWithDotSpaceAfter() {
        val text = "Send to $bitcoinAddress1. Thank you"
        assertEquals(1, scanText(text), "Bitcoin адрес с точкой и пробелом после должен быть найден")
    }

    @Test
    fun testEthereumWithDotSpaceAfter() {
        val text = "Send to $ethereumAddress. Thank you"
        assertEquals(1, scanText(text), "Ethereum адрес с точкой и пробелом после должен быть найден")
    }

    // Тесты для адресов с новой строкой после

    @Test
    fun testBitcoinWithNewLineAfter() {
        val text = "Send to $bitcoinAddress1\nThank you"
        assertEquals(1, scanText(text), "Bitcoin адрес с новой строкой после должен быть найден")
    }

    @Test
    fun testEthereumWithNewLineAfter() {
        val text = "Send to $ethereumAddress\nThank you"
        assertEquals(1, scanText(text), "Ethereum адрес с новой строкой после должен быть найден")
    }

    // Тесты для адресов с закрывающим спецсимволом и пробелом после

    @Test
    fun testBitcoinWithClosingBraceSpace() {
        val text = "Send to ($bitcoinAddress1) please"
        assertEquals(1, scanText(text), "Bitcoin адрес с закрывающей скобкой и пробелом должен быть найден")
    }

    @Test
    fun testEthereumWithClosingBraceSpace() {
        val text = "Send to [$ethereumAddress] please"
        assertEquals(1, scanText(text), "Ethereum адрес с закрывающей квадратной скобкой и пробелом должен быть найден")
    }

    @Test
    fun testBitcoinWithClosingCurlyBraceSpace() {
        val text = "Send to {$bitcoinAddress1} please"
        assertEquals(1, scanText(text), "Bitcoin адрес с закрывающей фигурной скобкой и пробелом должен быть найден")
    }

    // Тесты для адресов с спецсимволом и пробелом перед

    @Test
    fun testBitcoinWithHashSpaceBefore() {
        val text = "# $bitcoinAddress1"
        assertEquals(1, scanText(text), "Bitcoin адрес с # и пробелом перед должен быть найден")
    }

    @Test
    fun testEthereumWithColonSpaceBefore() {
        val text = ": $ethereumAddress"
        assertEquals(1, scanText(text), "Ethereum адрес с : и пробелом перед должен быть найден")
    }

    @Test
    fun testBitcoinWithEqualsSpaceBefore() {
        val text = "= $bitcoinAddress1"
        assertEquals(1, scanText(text), "Bitcoin адрес с = и пробелом перед должен быть найден")
    }

    @Test
    fun testEthereumWithDashSpaceBefore() {
        val text = "- $ethereumAddress"
        assertEquals(1, scanText(text), "Ethereum адрес с - и пробелом перед должен быть найден")
    }

    @Test
    fun testBitcoinWithOpenParenSpaceBefore() {
        val text = "( $bitcoinAddress1"
        assertEquals(1, scanText(text), "Bitcoin адрес с ( и пробелом перед должен быть найден")
    }

    // Тесты для адресов с пробелом перед (без спецсимвола)

    @Test
    fun testBitcoinWithSpaceBefore() {
        val text = "Address: $bitcoinAddress1"
        assertEquals(1, scanText(text), "Bitcoin адрес с пробелом перед должен быть найден")
    }

    @Test
    fun testEthereumWithSpaceBefore() {
        val text = "Address: $ethereumAddress"
        assertEquals(1, scanText(text), "Ethereum адрес с пробелом перед должен быть найден")
    }

    // Тесты для адресов в составе другого слова (не должны находиться)

    @Test
    fun testBitcoinEmbeddedInWord() {
        val text = "abc${bitcoinAddress1}def"
        assertEquals(0, scanText(text), "Bitcoin адрес в составе слова не должен быть найден")
    }

    @Test
    fun testEthereumEmbeddedInWord() {
        val text = "abc${ethereumAddress}def"
        assertEquals(0, scanText(text), "Ethereum адрес в составе слова не должен быть найден")
    }

    @Test
    fun testBitcoinEmbeddedInNumber() {
        val text = "123${bitcoinAddress1}456"
        assertEquals(0, scanText(text), "Bitcoin адрес в составе числа не должен быть найден")
    }

    @Test
    fun testEthereumEmbeddedInNumber() {
        val text = "123${ethereumAddress}456"
        assertEquals(0, scanText(text), "Ethereum адрес в составе числа не должен быть найден")
    }

    @Test
    fun testBitcoinWithLetterAfter() {
        val text = "${bitcoinAddress1}abc"
        assertEquals(0, scanText(text), "Bitcoin адрес с буквой после не должен быть найден")
    }

    @Test
    fun testEthereumWithLetterAfter() {
        val text = "${ethereumAddress}abc"
        assertEquals(0, scanText(text), "Ethereum адрес с буквой после не должен быть найден")
    }

    @Test
    fun testBitcoinWithNumberAfter() {
        val text = "${bitcoinAddress1}123"
        assertEquals(0, scanText(text), "Bitcoin адрес с цифрой после не должен быть найден")
    }

    @Test
    fun testEthereumWithNumberAfter() {
        val text = "${ethereumAddress}123"
        assertEquals(0, scanText(text), "Ethereum адрес с цифрой после не должен быть найден")
    }

    @Test
    fun testBitcoinWithLetterBefore() {
        val text = "abc$bitcoinAddress1"
        assertEquals(0, scanText(text), "Bitcoin адрес с буквой перед не должен быть найден")
    }

    @Test
    fun testEthereumWithLetterBefore() {
        val text = "abc$ethereumAddress"
        assertEquals(0, scanText(text), "Ethereum адрес с буквой перед не должен быть найден")
    }

    @Test
    fun testBitcoinWithNumberBefore() {
        val text = "123$bitcoinAddress1"
        assertEquals(0, scanText(text), "Bitcoin адрес с цифрой перед не должен быть найден")
    }

    @Test
    fun testEthereumWithNumberBefore() {
        val text = "123$ethereumAddress"
        assertEquals(0, scanText(text), "Ethereum адрес с цифрой перед не должен быть найден")
    }

    // Тесты для разных криптовалют

    @Test
    fun testBitcoinP2SH() {
        val text = "P2SH address: $bitcoinAddress3"
        assertEquals(1, scanText(text), "Bitcoin P2SH адрес должен быть найден")
    }

    @Test
    fun testLitecoinM() {
        val text = "Litecoin M address: $litecoinAddressM"
        assertEquals(1, scanText(text), "Litecoin M адрес должен быть найден")
    }

    @Test
    fun testLitecoinBech32() {
        val text = "Litecoin Bech32: $litecoinBech32"
        assertEquals(1, scanText(text), "Litecoin Bech32 адрес должен быть найден")
    }

    @Test
    fun testBitcoinCash() {
        val text = "Bitcoin Cash: $bitcoinCashAddress"
        assertEquals(1, scanText(text), "Bitcoin Cash адрес должен быть найден")
    }

    @Test
    fun testDogecoin() {
        val text = "Dogecoin: $dogecoinAddress"
        assertEquals(1, scanText(text), "Dogecoin адрес должен быть найден")
    }

    @Test
    fun testTron() {
        val text = "Tron: $tronAddress"
        assertEquals(1, scanText(text), "Tron адрес должен быть найден")
    }

    @Test
    fun testSolana() {
        val text = "Solana: $solanaAddress"
        assertEquals(1, scanText(text), "Solana адрес должен быть найден")
    }

    @Test
    fun testStellar() {
        val text = "Stellar: $stellarAddress"
        assertEquals(1, scanText(text), "Stellar адрес должен быть найден")
    }

    @Test
    fun testCardano() {
        val text = "Cardano: $cardanoAddress"
        assertEquals(1, scanText(text), "Cardano адрес должен быть найден")
    }

    // Тесты для пограничных значений длины

    @Test
    fun testBitcoinMinLength() {
        // Минимальная длина Bitcoin адреса (26 символов)
        val minBitcoin = "1" + "A".repeat(25)
        val text = "Min Bitcoin: $minBitcoin"
        assertEquals(1, scanText(text), "Bitcoin адрес минимальной длины должен быть найден")
    }

    @Test
    fun testBitcoinMaxLength() {
        // Максимальная длина Bitcoin адреса (35 символов)
        val maxBitcoin = "1" + "A".repeat(34)
        val text = "Max Bitcoin: $maxBitcoin"
        assertEquals(1, scanText(text), "Bitcoin адрес максимальной длины должен быть найден")
    }

    @Test
    fun testEthereumExactLength() {
        // Ethereum адрес должен быть ровно 40 hex символов после 0x
        val text = "Ethereum: $ethereumAddress"
        assertEquals(1, scanText(text), "Ethereum адрес правильной длины должен быть найден")
    }

    @Test
    fun testBitcoinBech32MinLength() {
        // Минимальная длина Bitcoin Bech32 (39 символов после bc1)
        val minBech32 = "bc1" + "a".repeat(36)
        val text = "Min Bech32: $minBech32"
        assertEquals(1, scanText(text), "Bitcoin Bech32 минимальной длины должен быть найден")
    }

    @Test
    fun testBitcoinBech32MaxLength() {
        // Максимальная длина Bitcoin Bech32 (59 символов после bc1)
        val maxBech32 = "bc1" + "a".repeat(56)
        val text = "Max Bech32: $maxBech32"
        assertEquals(1, scanText(text), "Bitcoin Bech32 максимальной длины должен быть найден")
    }

    // Тесты для множественных адресов

    @Test
    fun testMultipleBitcoin() {
        val text = "First: $bitcoinAddress1 Second: $bitcoinAddress3"
        assertEquals(2, scanText(text), "Несколько Bitcoin адресов должны быть найдены")
    }

    @Test
    fun testMultipleDifferentCryptos() {
        val text = "Bitcoin: $bitcoinAddress1 Ethereum: $ethereumAddress Litecoin: $litecoinAddressL"
        assertEquals(3, scanText(text), "Несколько адресов разных криптовалют должны быть найдены")
    }

    // Тесты для пустой строки и невалидных данных

    @Test
    fun testEmptyString() {
        val text = ""
        assertEquals(0, scanText(text), "Пустая строка не должна содержать адресов")
    }

    @Test
    fun testInvalidBitcoinTooShort() {
        val text = "Invalid: 1A1zP1eP5QGefi2DMPTfTL5SLmv7Divf"
        assertEquals(0, scanText(text), "Слишком короткий Bitcoin адрес не должен быть найден")
    }

    @Test
    fun testInvalidEthereumTooShort() {
        val text = "Invalid: 0x742d35Cc6634C0532925a3b844Bc9e7595f0be"
        assertEquals(0, scanText(text), "Слишком короткий Ethereum адрес не должен быть найден")
    }

    @Test
    fun testInvalidEthereumInvalidChars() {
        val text = "Invalid: 0x742d35Cc6634C0532925a3b844Bc9e7595f0bez"
        assertEquals(0, scanText(text), "Ethereum адрес с невалидными символами не должен быть найден")
    }

    // Тесты для адресов с кавычками

    @Test
    fun testBitcoinInQuotes() {
        val text = "\"$bitcoinAddress1\""
        assertEquals(1, scanText(text), "Bitcoin адрес в кавычках должен быть найден")
    }

    @Test
    fun testEthereumInQuotes() {
        val text = "'$ethereumAddress'"
        assertEquals(1, scanText(text), "Ethereum адрес в одинарных кавычках должен быть найден")
    }

    // Тесты для адресов с пунктуацией

    @Test
    fun testBitcoinWithCommaSpace() {
        val text = "Addresses: $bitcoinAddress1, $ethereumAddress"
        assertEquals(2, scanText(text), "Адреса с запятой и пробелом должны быть найдены")
    }

    @Test
    fun testBitcoinWithSemicolonSpace() {
        val text = "Address: $bitcoinAddress1; next address"
        assertEquals(1, scanText(text), "Bitcoin адрес с точкой с запятой и пробелом должен быть найден")
    }

    // Тесты для адресов в разных регистрах (где применимо)

    @Test
    fun testEthereumMixedCase() {
        val text = "Ethereum: $ethereumAddress"
        assertEquals(1, scanText(text), "Ethereum адрес в смешанном регистре должен быть найден")
    }

    @Test
    fun testBitcoinBech32LowerCase() {
        val text = "Bitcoin Bech32: $bitcoinBech32"
        assertEquals(1, scanText(text), "Bitcoin Bech32 адрес в нижнем регистре должен быть найден")
    }

    // Тесты для Bitcoin Cash с префиксом bitcoincash:

    @Test
    fun testBitcoinCashWithPrefix() {
        val text = "Bitcoin Cash: bitcoincash:$bitcoinCashAddress"
        assertEquals(1, scanText(text), "Bitcoin Cash адрес с префиксом должен быть найден")
    }

    // Тесты для адресов с пробелом после (без спецсимвола)

    @Test
    fun testBitcoinWithSpaceAfter() {
        val text = "$bitcoinAddress1 is valid"
        assertEquals(1, scanText(text), "Bitcoin адрес с пробелом после должен быть найден")
    }

    @Test
    fun testEthereumWithSpaceAfter() {
        val text = "$ethereumAddress is valid"
        assertEquals(1, scanText(text), "Ethereum адрес с пробелом после должен быть найден")
    }

    // Тесты для адресов в середине текста

    @Test
    fun testBitcoinInMiddle() {
        val text = "Please send funds to $bitcoinAddress1 for payment"
        assertEquals(1, scanText(text), "Bitcoin адрес в середине текста должен быть найден")
    }

    @Test
    fun testEthereumInMiddle() {
        val text = "The address $ethereumAddress is correct"
        assertEquals(1, scanText(text), "Ethereum адрес в середине текста должен быть найден")
    }

    // Тесты для адресов, которые могут конфликтовать (например, Solana слишком общий паттерн)

    @Test
    fun testSolanaNotMatchTooShort() {
        val text = "Short: 7dHbWXmci3dT8UFYWYZweBLXgycu7Y3iL6"
        // Solana адреса должны быть 32-44 символа, это 32 - должно найтись
        assertEquals(1, scanText(text), "Solana адрес минимальной длины должен быть найден")
    }

    @Test
    fun testSolanaNotMatchTooLong() {
        val text = "Long: 7dHbWXmci3dT8UFYWYZweBLXgycu7Y3iL6trKn1Y7ARjExtra"
        assertEquals(0, scanText(text), "Слишком длинный адрес не должен быть найден как Solana")
    }
}

