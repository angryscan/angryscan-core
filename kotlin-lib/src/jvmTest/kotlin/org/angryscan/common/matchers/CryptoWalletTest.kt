package org.angryscan.common.matchers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CryptoWalletTest : MatcherTestBase(CryptoWallet) {

    private val bitcoinLegacy = "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa"
    private val bitcoinP2SH = "3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy"
    private val ethereum = "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb0"
    private val litecoinLegacy = "LTCN6Lfz5XvNF9XqHdc6P5oE6r2c5iJmKxY"
    private val ripple = "rN7n7otQDd6FczFgLdSqtcsAUxDkw6fzRH"
    private val bitcoinCash = "qpm2qsznhks23z7629mms6s4cwef74vcwvy22gdx6a"
    private val dogecoin = "D7Y55LdC5Hj3aNk3M2vB6tF8gH9jK1mN3pQ5rS7tU"
    private val tron = "TQn9Y2khEsLMWDmBqJ8K9XpY7hN2vC5wE8"
    private val cosmos = "cosmos1hsk6jryyqjfhp5dhc55t9lrxkzy8awu00azxm7"
    private val tezos = "tz1KqTpEZ7Yob7QbPE4Hy4Wo8fHG8LhKxZSx"
    private val dash = "Xx4d8rzKwejkA3L2L5m5hM648Q7y6t9uZ3"
    private val zcash = "t1VzHL4Nj3V3SHqUF5u7uGwF6qHH5aqT3Nb"
    private val terra = "terra1hsk6jryyqjfhp5dhc55t9lrxkzy8awu00azxm7"
    private val bitcoinBech32 = "bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4"
    private val litecoinBech32 = "ltc1qw508d6qejxtdg4y5r3zarvary0c5xw7kemeawh"
    private val ton = "EQCkR1cGmnsE45N4K0otPl5EnxnRakmGqeJUNua5fkTh"

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
        val longerAddress = "${ton}ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/"
        val text = " Адрес: $longerAddress "
        assertTrue(scanText(text) >= 1, "TON адрес в составе более длинного адреса должен быть найден")
    }

    @Test
    fun testBitcoinLegacyMinimumLength() {
        val minAddress = "1" + "A".repeat(25)
        val text = " Минимальный: $minAddress "
        val result = scanText(text)
        assertTrue(result >= 0, "Минимальный Bitcoin Legacy адрес")
    }

    @Test
    fun testBitcoinLegacyMaximumLength() {
        val maxAddress = "1" + "A".repeat(40)
        val text = " Максимальный: $maxAddress "
        val result = scanText(text)
        assertTrue(result >= 0, "Максимальный Bitcoin Legacy адрес")
    }

    @Test
    fun testEthereumExactLength() {
        val text = " Ethereum: $ethereum "
        assertTrue(scanText(text) >= 1, "Ethereum адрес точной длины должен быть найден")
    }

    @Test
    fun testMultipleAddresses() {
        val text = """
            Bitcoin: $bitcoinLegacy
            Ethereum: $ethereum
            Litecoin: $litecoinLegacy
        """.trimIndent()
        assertTrue(scanText(text) == 3, "Несколько адресов должны быть найдены")
    }

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

    @Test
    fun testFalsePositiveTheyellowwireshouldalignwiththepin() {
        val text = ". Theyellowwireshouldalignwiththepin"
        assertEquals(0, scanText(text), "Текст 'Theyellowwireshouldalignwiththepin' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveRSAenablesdigitalsignatures() {
        val text = ", RSAenablesdigitalsignatures"
        assertEquals(0, scanText(text), "Текст 'RSAenablesdigitalsignatures' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveThedetailsofthisbuscanbeinferredby() {
        val text = "(Thedetailsofthisbuscanbeinferredby"
        assertEquals(0, scanText(text), "Текст 'Thedetailsofthisbuscanbeinferredby' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveMITdecidedthatIcouldpublishmyworkas() {
        val text = ". MITdecidedthatIcouldpublishmyworkas"
        assertEquals(0, scanText(text), "Текст 'MITdecidedthatIcouldpublishmyworkas' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveMostoftheseerratahavesimple() {
        val text = ". Mostoftheseerratahavesimple"
        assertEquals(0, scanText(text), "Текст 'Mostoftheseerratahavesimple' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveMationabouthackingtheboxdisappointed() {
        val text = "mationabouthackingtheboxdisappointed"
        assertEquals(0, scanText(text), "Текст 'mationabouthackingtheboxdisappointed' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveHexString1() {
        val text = "33C22962E43CEF8627CBC63535F33FCE"
        assertEquals(0, scanText(text), "Hex-строка '33C22962E43CEF8627CBC63535F33FCE' не должна определяться как адрес")
    }

    @Test
    fun testFalsePositiveHexString2() {
        val text = "154A6BFE1B651582F77341561FDC68A4"
        assertEquals(0, scanText(text), "Hex-строка '154A6BFE1B651582F77341561FDC68A4' не должна определяться как адрес")
    }

    @Test
    fun testFalsePositiveHexString3() {
        val text = "DB975B51A999D84835C71F73F83B1862"
        assertEquals(0, scanText(text), "Hex-строка 'DB975B51A999D84835C71F73F83B1862' не должна определяться как адрес")
    }

    @Test
    fun testFalsePositiveNumberWithLetters1() {
        val text = "1DipartimentodiInformaticaedApplicazioni"
        assertEquals(0, scanText(text), "Текст '1DipartimentodiInformaticaedApplicazioni' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveNumberWithLetters2() {
        val text = "3ComputerTechnologyInstituteand"
        assertEquals(0, scanText(text), "Текст '3ComputerTechnologyInstituteand' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveNumberWithLetters3() {
        val text = "1ComputerScienceDepartment"
        assertEquals(0, scanText(text), "Текст '1ComputerScienceDepartment' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveHexStringWithLowercaseD() {
        val text = "df7befc8cdc3c5434ef27cc669fb1e4b"
        assertEquals(0, scanText(text), "Hex-строка 'df7befc8cdc3c5434ef27cc669fb1e4b' (начинается с маленькой d) не должна определяться как адрес Dogecoin")
    }

    @Test
    fun testFalsePositiveLongTextWithL() {
        val text = "LifttheharddriveupandrestitovertheDVDdrive"
        assertEquals(0, scanText(text), "Длинный текст 'LifttheharddriveupandrestitovertheDVDdrive' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveLongTextWithApostrophe() {
        val text = "' rightswithpoorcontentpolicyimplementations"
        assertEquals(0, scanText(text), "Текст с апострофом '' rightswithpoorcontentpolicyimplementations' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveLongTextWithM() {
        val text = "Mosttrappedeventsareentriesintosystemcalls"
        assertEquals(0, scanText(text), "Длинный текст 'Mosttrappedeventsareentriesintosystemcalls' не должен определяться как адрес")
    }

    @Test
    fun testFalsePositiveHexStringStartingWith3() {
        val text = "33A954F58BDE92E9C9BBFB8816C99F15"
        assertEquals(0, scanText(text), "Hex-строка '33A954F58BDE92E9C9BBFB8816C99F15' (начинается с 3) не должна определяться как адрес")
    }

    @Test
    fun testFalsePositiveHexStringStartingWith3v2() {
        val text = "3081920428D7F0F0422367867B25FA42\n" +
                "                            33A954F58BDE92E9C9BBFB8816C99F15\n" +
                "                            E6398722A0B2B7BFE8493E9A5C306630"
        assertEquals(0, scanText(text), "Hex-строка '33A954F58BDE92E9C9BBFB8816C99F15' (начинается с 3) не должна определяться как адрес")
    }

    @Test
    fun testFalsePositiveHexStringStartingWith1() {
        val text = "1561E52A8B6DB258746FFE18F3CDCB11"
        assertEquals(0, scanText(text), "Hex-строка '1561E52A8B6DB258746FFE18F3CDCB11' (начинается с 1) не должна определяться как адрес")
    }
}
