package org.angryscan.common.engine

import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import org.angryscan.common.extensions.Matchers
import org.angryscan.common.matchers.*
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class EngineTest {

    @Test
    fun first() {
        fun check(scanResult: List<Match>) {
            //Check Email
            assertEquals(2, scanResult.count { it.matcher is Email })
            //Check CardNumbscanResult
            assertEquals(1, scanResult.count { it.matcher is CardNumber })
            //Check Phone
            assertEquals(2, scanResult.count { it.matcher is Phone })
            //Check AccountNscanResulter
            assertEquals(1, scanResult.count { it.matcher is AccountNumber })
            //Check CarNumbescanResult
            assertEquals(2, scanResult.count { it.matcher is CarNumber })
            //Check SNILS
            assertEquals(1, scanResult.count { it.matcher is SNILS })
            //Check Passport
            assertEquals(2, scanResult.count { it.matcher is Passport })
            //Check OMS
            assertEquals(1, scanResult.count { it.matcher is OMS })
            //Check INN
            assertEquals(1, scanResult.count { it.matcher is INN })
            //Check Address
            assertEquals(1, scanResult.count { it.matcher is Address })
            //Check Login
            assertEquals(1, scanResult.count { it.matcher is Login })
            //Check Password
            assertEquals(1, scanResult.count { it.matcher is Password })
            //Check CVV
            assertEquals(1, scanResult.count { it.matcher is CVV })
            //Check FullName
            assertEquals(3, scanResult.count { it.matcher is FullName })
            //Check IP
            assertEquals(1, scanResult.count { it.matcher is IP })
            //Check IPv6
            assertEquals(1, scanResult.count { it.matcher is IPv6 })
        }

        val filePath = javaClass.getResource("/testFiles/first.csv")?.file
        assertNotNull(filePath)
        val file = File(filePath)
        val text = file.readText()

        val secondFilePath = javaClass.getResource("/testFiles/first_2.csv")?.file
        assertNotNull(secondFilePath)
        val secondFile = File(secondFilePath)
        val secondText = secondFile.readText()


        assertEquals(true, file.exists())
        val hyperScan = HyperScanEngine(
            Matchers.filterIsInstance<IHyperMatcher>()
        )
        check(hyperScan.scan(text))
        check(hyperScan.scan(secondText))

        val kotlinScan = KotlinEngine(
            Matchers.filterIsInstance<IKotlinMatcher>()
        )
        check(kotlinScan.scan(text))
        check(kotlinScan.scan(secondText))
    }

    @Test
    fun testText() {
        val file = javaClass.getResource("/testFiles/testText.txt")?.file
        assertNotNull(file)

        for (attribute in Matchers.filterIsInstance<IKotlinMatcher>()) {
            assertEquals(1, getCountOfAttribute(file, attribute))
        }
    }

    @Test
    fun testCardEdge() {
        val file = javaClass.getResource("/testFiles/cardNumber/edge.txt")?.file
        assertNotNull(file)
        assertEquals(5, getCountOfAttribute(file, CardNumber()))
    }

    @Test
    fun testCardWithBrace() {
        val file = javaClass.getResource("/testFiles/cardNumber/braces.txt")?.file
        assertNotNull(file)
        assertEquals(3, getCountOfAttribute(file, CardNumber()))
    }

    @Test
    fun testCardWithSmth() {
        val file = javaClass.getResource("/testFiles/cardNumber/smth.txt")?.file
        assertNotNull(file)
        assertEquals(8, getCountOfAttribute(file, CardNumber()))
    }

    @Test
    fun testCardWithStar() {
        val file = javaClass.getResource("/testFiles/cardNumber/star.txt")?.file
        assertNotNull(file)
        assertEquals(1, getCountOfAttribute(file, CardNumber()))
    }

    @Test
    fun testCardNotValid() {
        val file = javaClass.getResource("/testFiles/cardNumber/notValid.txt")?.file
        assertNotNull(file)
        assertEquals(0, getCountOfAttribute(file, CardNumber()))
    }

    @Test
    fun testSnilsEdge() {
        val file = javaClass.getResource("/testFiles/snils/edge.txt")?.file
        assertNotNull(file)
        assertEquals(6, getCountOfAttribute(file, SNILS))
    }

    @Test
    fun testSnilsWithBrace() {
        val file = javaClass.getResource("/testFiles/snils/braces.txt")?.file
        assertNotNull(file)
        assertEquals(5, getCountOfAttribute(file, SNILS))
    }

    @Test
    fun testSnilsWithSmth() {
        val file = javaClass.getResource("/testFiles/snils/smth.txt")?.file
        assertNotNull(file)
        assertEquals(6, getCountOfAttribute(file, SNILS))
    }

    @Test
    fun testSnilsWithStar() {
        val file = javaClass.getResource("/testFiles/snils/star.txt")?.file
        assertNotNull(file)
        assertEquals(1, getCountOfAttribute(file, SNILS))
    }

    @Test
    fun testSnilsNotValid() {
        val file = javaClass.getResource("/testFiles/snils/notValid.txt")?.file
        assertNotNull(file)
        assertEquals(0, getCountOfAttribute(file, SNILS))
    }

    @Test
    fun testInnEdge() {
        val file = javaClass.getResource("/testFiles/inns/edge.txt")?.file
        assertNotNull(file)
        assertEquals(5, getCountOfAttribute(file, INN))
    }

    @Test
    fun testInnWithBrace() {
        val file = javaClass.getResource("/testFiles/inns/braces.txt")?.file
        assertNotNull(file)
        assertEquals(2, getCountOfAttribute(file, INN))
    }

    @Test
    fun testInnWithSmth() {
        val file = javaClass.getResource("/testFiles/inns/smth.txt")?.file
        assertNotNull(file)
        assertEquals(7, getCountOfAttribute(file, INN))
    }

    @Test
    fun testInnWithStar() {
        val file = javaClass.getResource("/testFiles/inns/star.txt")?.file
        assertNotNull(file)
        assertEquals(1, getCountOfAttribute(file, INN))
    }

    @Test
    fun testInnNotValid() {
        val file = javaClass.getResource("/testFiles/inns/notValid.txt")?.file
        assertNotNull(file)
        assertEquals(0, getCountOfAttribute(file, INN))
    }

    @Test
    fun testIP() {
        val file = javaClass.getResource("/testFiles/ip.txt")?.file
        assertNotNull(file)
        assertEquals(4, getCountOfAttribute(file, IP))
    }

    @Test
    fun testIPFalse() {
        val file = javaClass.getResource("/testFiles/ip_false.txt")?.file
        assertNotNull(file)
        assertEquals(0, getCountOfAttribute(file, IP))
    }

    @Test
    fun testIPv6() {
        // в этом файле 2 несокращенных IPv6 и очень много разнообразных сокращенных
        // в данный момент распознаем только несокращенные IPv6
        val file = javaClass.getResource("/testFiles/ipv6.txt")?.file
        assertNotNull(file)
        assertEquals(2, getCountOfAttribute(file, IPv6))
    }

    @Test
    fun testIPv6False() {
        val file = javaClass.getResource("/testFiles/ipv6_false.txt")?.file
        assertNotNull(file)
        assertEquals(0, getCountOfAttribute(file, IPv6))
    }

    @Test
    fun testPhones() {
        val file = javaClass.getResource("/testFiles/phones.txt")?.file
        assertNotNull(file)
        assertEquals(40, getCountOfAttribute(file, Phone))
    }

    @Test
    fun testPassports() {
        val file = javaClass.getResource("/testFiles/passport/passport.txt")?.file
        assertNotNull(file)
        assertEquals(1, getCountOfAttribute(file, Passport))
    }

    @Test
    fun testPhonesSmth() {
        val file = javaClass.getResource("/testFiles/phone/smth.txt")?.file
        assertNotNull(file)
        assertEquals(2, getCountOfAttribute(file, Phone))
    }

    @Test
    fun getContext() {
        fun check(searchResult: List<Match>) {
            assertEquals(1, searchResult.count())
            assertTrue(searchResult.first().value.contains("4276 8070 1492 7948"))
            assertTrue(searchResult.first().before.contains("Карта"))
            assertTrue(searchResult.first().after.contains("г. Санкт"))
        }

        val filePath = javaClass.getResource("/testFiles/first.csv")?.file
        assertNotNull(filePath)

        check(getScanResult(filePath, CardNumber() as IKotlinMatcher))
        check(getScanResult(filePath, CardNumber() as IHyperMatcher))
    }


    companion object {
        fun getCountOfAttribute(filePath: String, matcher: IMatcher): Int {
            val file = File(filePath)

            assertEquals(true, file.exists())

            val kotlinEngine = KotlinEngine(listOf(matcher).filterIsInstance<IKotlinMatcher>())
            val hyperEngine = HyperScanEngine(listOf(matcher).filterIsInstance<IHyperMatcher>())

            val text = file.readText()
            val kotlinRes = kotlinEngine.scan(text)
            val hyperRes = hyperEngine.scan(text)
            assertEquals(
                kotlinRes.count(),
                hyperRes.count(),
                "Count of attribute ${matcher.name} is not equal with different engines"
            )
            return kotlinRes.count()
        }
        private fun getScanResult(filePath: String, matcher: IHyperMatcher): List<Match> {
            val file = File(filePath)
            assertTrue(file.exists())
            val text = file.readText()
            val hyperEngine = HyperScanEngine(listOf(matcher))

            return hyperEngine.scan(text)
        }
        private fun getScanResult(filePath: String, matcher: IKotlinMatcher): List<Match> {
            val file = File(filePath)
            assertTrue(file.exists())
            val text = file.readText()
            val kotlinEngine = KotlinEngine(listOf(matcher))
            return kotlinEngine.scan(text)
        }
    }
}