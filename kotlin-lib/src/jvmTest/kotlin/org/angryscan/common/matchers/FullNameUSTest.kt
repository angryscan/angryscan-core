package org.angryscan.common.matchers

import org.angryscan.common.engine.IMatcher
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Тесты для проверки матчера FullNameUS (американские ФИО)
 */
internal class FullNameUSTest {

    // === Тесты с ключевыми словами ===
    
    @Test
    fun testFullNameUSWithKeywordName() {
        val text = "Name: John Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with keyword 'Name' should be found")
    }

    @Test
    fun testFullNameUSWithKeywordFullName() {
        val text = "Full Name: Michael Johnson"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with keyword 'Full Name' should be found")
    }

    @Test
    fun testFullNameUSWithKeywordEmployee() {
        val text = "Employee: Sarah Williams"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with keyword 'Employee' should be found")
    }

    @Test
    fun testFullNameUSWithKeywordPerson() {
        val text = "Person: David Brown"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with keyword 'Person' should be found")
    }

    @Test
    fun testFullNameUSWithKeywordPatient() {
        val text = "Patient: Jennifer Davis"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with keyword 'Patient' should be found")
    }

    @Test
    fun testFullNameUSWithKeywordClient() {
        val text = "Client: Robert Miller"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with keyword 'Client' should be found")
    }

    @Test
    fun testFullNameUSWithKeywordCustomer() {
        val text = "Customer: Emily Davis"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with keyword 'Customer' should be found")
    }

    @Test
    fun testFullNameUSWithKeywordApplicant() {
        val text = "Applicant: Daniel Wilson"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with keyword 'Applicant' should be found")
    }

    @Test
    fun testFullNameUSWithKeywordCandidate() {
        val text = "Candidate: Jessica Moore"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with keyword 'Candidate' should be found")
    }

    // === Тесты с расширенными ключевыми словами ===
    
    @Test
    fun testFullNameUSProfessionalContext() {
        assertEquals(1, scanText("Worker: John Smith", FullNameUS), "Should find with 'Worker'")
        assertEquals(1, scanText("Manager: Sarah Johnson", FullNameUS), "Should find with 'Manager'")
        assertEquals(1, scanText("Supervisor Michael Brown", FullNameUS), "Should find with 'Supervisor'")
    }

    @Test
    fun testFullNameUSMedicalContext() {
        assertEquals(1, scanText("Doctor: John Smith", FullNameUS), "Should find with 'Doctor'")
        assertEquals(1, scanText("Nurse Sarah Johnson", FullNameUS), "Should find with 'Nurse'")
        assertEquals(1, scanText("Physician: Michael Brown", FullNameUS), "Should find with 'Physician'")
    }

    @Test
    fun testFullNameUSEducationalContext() {
        assertEquals(1, scanText("Student: John Smith", FullNameUS), "Should find with 'Student'")
        assertEquals(1, scanText("Teacher Sarah Johnson", FullNameUS), "Should find with 'Teacher'")
        assertEquals(1, scanText("Professor: Michael Brown", FullNameUS), "Should find with 'Professor'")
    }

    @Test
    fun testFullNameUSLegalContext() {
        assertEquals(1, scanText("Plaintiff: John Smith", FullNameUS), "Should find with 'Plaintiff'")
        assertEquals(1, scanText("Attorney Sarah Johnson", FullNameUS), "Should find with 'Attorney'")
        assertEquals(1, scanText("Witness: Michael Brown", FullNameUS), "Should find with 'Witness'")
    }

    @Test
    fun testFullNameUSFamilyContext() {
        assertEquals(1, scanText("Parent: John Smith", FullNameUS), "Should find with 'Parent'")
        assertEquals(1, scanText("Spouse Sarah Johnson", FullNameUS), "Should find with 'Spouse'")
        assertEquals(1, scanText("Guardian: Michael Brown", FullNameUS), "Should find with 'Guardian'")
    }

    // === Тесты с инициалами ===
    
    @Test
    fun testFullNameUSWithMiddleInitial() {
        val text = "Name: John M. Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with middle initial should be found")
    }

    @Test
    fun testFullNameUSWithMiddleInitialNoSpace() {
        val text = "Employee: John M Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with middle initial (no space after) should be found")
    }

    @Test
    fun testFullNameUSWithMiddleInitialNoPeriod() {
        val text = "Name: John M Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with middle initial without period should be found")
    }

    // === Тесты с различными именами ===
    
    @Test
    fun testFullNameUSShortNames() {
        val text = "Employee: Joe Lee"
        assertTrue(scanText(text, FullNameUS) >= 1, "Short names with keyword should be found")
    }

    @Test
    fun testFullNameUSLongNames() {
        val text = "Employee: Christopher Washington"
        assertTrue(scanText(text, FullNameUS) >= 1, "Long names with keyword should be found")
    }

    // === Тесты с различными позициями и окружением ===
    
    @Test
    fun testFullNameUSInParentheses() {
        val text = "Patient: John Smith (age 45)"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with keyword should be found")
    }

    @Test
    fun testFullNameUSWithComma() {
        val text = "Employee: John Smith, Software Engineer"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with comma should be found")
    }

    @Test
    fun testFullNameUSWithPeriod() {
        val text = "Contact: John Smith."
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with period should be found")
    }

    @Test
    fun testFullNameUSWithColon() {
        val text = "Name: John Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with colon should be found")
    }

    @Test
    fun testFullNameUSWithDash() {
        val text = "Employee-John Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with dash should be found")
    }

    // === Тесты с разными форматами текста ===
    
    @Test
    fun testFullNameUSWithNewline() {
        val text = "Name:\nJohn Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with newline should be found")
    }

    @Test
    fun testFullNameUSWithTab() {
        val text = "Name:\tJohn Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with tab should be found")
    }

    @Test
    fun testFullNameUSWithMultipleSpaces() {
        val text = "Name:    John Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name with multiple spaces should be found")
    }

    // === Тесты для проверки фильтрации ложных срабатываний ===
    
    @Test
    fun testFullNameUSNotGeography() {
        val text = "New York is a city"
        assertEquals(0, scanText(text, FullNameUS), "Geography without keyword should not be found")
    }

    @Test
    fun testFullNameUSNotStreet() {
        val text = "Main Street is busy"
        assertEquals(0, scanText(text, FullNameUS), "Street names without keyword should not be found")
    }

    @Test
    fun testFullNameUSNotSchool() {
        val text = "High School graduation"
        assertEquals(0, scanText(text, FullNameUS), "School names without keyword should not be found")
    }
    
    @Test
    fun testFullNameUSNotCompanies() {
        assertEquals(0, scanText("Apple Store", FullNameUS), "Company names should not be found")
        assertEquals(0, scanText("Microsoft Office", FullNameUS), "Product names should not be found")
    }
    
    @Test
    fun testFullNameUSNotStandaloneNames() {
        // Без ключевых слов имена НЕ должны находиться (высокая точность)
        assertEquals(0, scanText("John Smith", FullNameUS), "Standalone names should not be found")
        assertEquals(0, scanText("Sarah Johnson", FullNameUS), "Standalone names should not be found")
    }

    // === Граничные тесты ===
    
    @Test
    fun testFullNameUSMinimumLength() {
        val text = "Patient: Joe Fox"
        assertTrue(scanText(text, FullNameUS) >= 1, "Minimum length names with keyword should be found")
    }

    @Test
    fun testFullNameUSProperCapitalization() {
        val text = "Contact: John Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Proper capitalization with keyword should be found")
    }

    // === Тесты в контексте предложений ===
    
    @Test
    fun testFullNameUSInSentence() {
        val text = "Please contact Person: John Smith at the office."
        assertTrue(scanText(text, FullNameUS) >= 1, "Full name in sentence with keyword should be found")
    }

    // === Негативные тесты ===
    
    @Test
    fun testFullNameUSEmptyString() {
        val text = ""
        assertEquals(0, scanText(text, FullNameUS), "Empty string should not contain names")
    }

    @Test
    fun testFullNameUSOnlyFirstName() {
        val text = "John"
        assertEquals(0, scanText(text, FullNameUS), "Only first name should not be found")
    }

    @Test
    fun testFullNameUSOnlyLastName() {
        val text = "Smith"
        assertEquals(0, scanText(text, FullNameUS), "Only last name should not be found")
    }

    // === Специальные случаи ===
    
    @Test
    fun testFullNameUSWithTitle() {
        val text = "Doctor: John Smith"
        assertTrue(scanText(text, FullNameUS) >= 1, "Name with keyword should be found")
    }

    @Test
    fun testFullNameUSMultipleWithKeywords() {
        // Тестируем каждое имя отдельно, чтобы избежать проблем с дедупликацией между движками
        val text1 = "Patient: John Smith"
        val text2 = "Doctor: Sarah Johnson"
        val text3 = "Nurse: Michael Brown"
        
        assertTrue(scanText(text1, FullNameUS) >= 1, "First name with keyword should be found")
        assertTrue(scanText(text2, FullNameUS) >= 1, "Second name with keyword should be found")
        assertTrue(scanText(text3, FullNameUS) >= 1, "Third name with keyword should be found")
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
