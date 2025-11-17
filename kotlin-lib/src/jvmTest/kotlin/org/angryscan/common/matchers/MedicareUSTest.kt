package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Тесты для проверки крайних позиций и пограничных значений матчера MedicareUS (Medicare Beneficiary Identifier)
 */
internal class MedicareUSTest {

    @Test
    fun testMedicareUSAtStart() {
        val text = "Medicare 1EG4-TE5-MK73 is active"
        assertEquals(1, scanText(text, MedicareUS), "Medicare number at the beginning should be found")
    }

    @Test
    fun testMedicareUSAtEnd() {
        val text = "Patient's Medicare: 1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare number at the end should be found")
    }

    @Test
    fun testMedicareUSInMiddle() {
        val text = "Patient has Medicare 1EG4-TE5-MK73 for coverage"
        assertEquals(1, scanText(text, MedicareUS), "Medicare number in the middle should be found")
    }

    @Test
    fun testMedicareUSStandalone() {
        val text = "Medicare 1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare number as standalone string should be found")
    }

    @Test
    fun testMedicareUSWithoutSpaces() {
        val text = "Medicare 1EG4TE5MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare number without spaces should be found")
    }

    @Test
    fun testMedicareUSWithSpaces() {
        val text = "Medicare 1EG4 TE5 MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare number with spaces should be found")
    }

    @Test
    fun testMedicareUSWithDashes() {
        val text = "Medicare 1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare number with dashes should be found")
    }

    @Test
    fun testMedicareUSWithTabs() {
        val text = "Medicare\t1EG4\tTE5\tMK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare number with tabs should be found")
    }

    @Test
    fun testMedicareUSPrefixMedicare() {
        val text = "Medicare 1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare with 'Medicare' prefix should be found")
    }

    @Test
    fun testMedicareUSPrefixMBI() {
        val text = "MBI 1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare with 'MBI' prefix should be found")
    }

    @Test
    fun testMedicareUSPrefixHealthInsurance() {
        val text = "Health Insurance 1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare with 'Health Insurance' prefix should be found")
    }

    @Test
    fun testMedicareUSPrefixMedicalInsurance() {
        val text = "Medical Insurance 1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare with 'Medical Insurance' prefix should be found")
    }

    @Test
    fun testMedicareUSUpperCase() {
        val text = "MEDICARE 1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare in uppercase should be found")
    }

    @Test
    fun testMedicareUSInParentheses() {
        val text = "(Medicare 1EG4-TE5-MK73)"
        assertEquals(1, scanText(text, MedicareUS), "Medicare in parentheses should be found")
    }

    @Test
    fun testMedicareUSInQuotes() {
        val text = "\"Medicare 1EG4-TE5-MK73\""
        assertEquals(1, scanText(text, MedicareUS), "Medicare in quotes should be found")
    }

    @Test
    fun testMedicareUSWithPunctuation() {
        val text = "Medicare: 1EG4-TE5-MK73."
        assertEquals(1, scanText(text, MedicareUS), "Medicare with punctuation should be found")
    }

    @Test
    fun testMedicareUSWithColon() {
        val text = "Medicare: 1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare with colon should be found")
    }

    @Test
    fun testMedicareUSWithHash() {
        val text = "Medicare #1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare with hash should be found")
    }

    @Test
    fun testMedicareUSWithDashPrefix() {
        val text = "Medicare-1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare with dash prefix should be found")
    }

    @Test
    fun testMultipleMedicareUS() {
        val text = """
            First: Medicare 1EG4-TE5-MK73
            Second: MBI 2AF5-UH6-NP84
        """.trimIndent()
        assertEquals(2, scanText(text, MedicareUS), "Multiple Medicare numbers should be found")
    }

    @Test
    fun testMedicareUSValidFormat1() {
        val text = "Medicare 1AC0-DE1-FG23"
        assertEquals(1, scanText(text, MedicareUS), "Valid Medicare format 1 should be found")
    }

    @Test
    fun testMedicareUSValidFormat2() {
        val text = "Medicare 9TY8-KM3-PQ67"
        assertEquals(1, scanText(text, MedicareUS), "Valid Medicare format 2 should be found")
    }

    @Test
    fun testMedicareUSValidFormat3() {
        val text = "Medicare 5HJ4-WX2-RV90"
        assertEquals(1, scanText(text, MedicareUS), "Valid Medicare format 3 should be found")
    }

    @Test
    fun testMedicareUSStartsWith1() {
        val text = "Medicare 1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare starting with 1 should be found")
    }

    @Test
    fun testMedicareUSStartsWith9() {
        val text = "Medicare 9EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare starting with 9 should be found")
    }

    @Test
    fun testMedicareUSInBrackets() {
        val text = "[Medicare 1EG4-TE5-MK73]"
        assertEquals(1, scanText(text, MedicareUS), "Medicare in brackets should be found")
    }

    @Test
    fun testMedicareUSMultiline() {
        val text = """Medicare:
1EG4-TE5-MK73"""
        assertEquals(1, scanText(text, MedicareUS), "Medicare on next line should be found")
    }

    @Test
    fun testMedicareUSRealWorldExample2() {
        val text = """
            Patient Information:
            Name: John Doe
            Medicare: 1EG4-TE5-MK73
            Date of Birth: 01/01/1950
        """.trimIndent()
        assertEquals(1, scanText(text, MedicareUS), "Real world example 2 should be found")
    }

    @Test
    fun testMedicareUSTooShort() {
        val text = "Medicare 1EG4-TE5"
        assertEquals(0, scanText(text, MedicareUS), "Too short Medicare number should not be found")
    }

    @Test
    fun testMedicareUSWithoutPrefix() {
        val text = "1EG4-TE5-MK73"
        assertEquals(1, scanText(text, MedicareUS), "Medicare number without prefix should be found")
    }

    @Test
    fun testMedicareUSEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, MedicareUS), "Empty string should not contain Medicare number")
    }

    private fun scanText(text: String, matcher: IMatcher): Int {
        val kotlinEngine = KotlinEngine(listOf(matcher).filterIsInstance<IKotlinMatcher>())
        val hyperEngine = HyperScanEngine(listOf(matcher).filterIsInstance<IHyperMatcher>())

        val kotlinRes = kotlinEngine.scan(text)
        val hyperRes = hyperEngine.scan(text)
        
        assertEquals(
            kotlinRes.count(),
            hyperRes.count(),
            "Number of matches for ${matcher.name} should be the same for both engines. " +
            "Kotlin: ${kotlinRes.count()}, Hyper: ${hyperRes.count()}\nText: $text"
        )
        
        return kotlinRes.count()
    }
}

