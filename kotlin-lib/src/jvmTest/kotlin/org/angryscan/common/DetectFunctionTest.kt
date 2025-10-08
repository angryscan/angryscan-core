package org.angryscan.common

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class DetectFunctionTest {
    @Test
    fun testText() {
        val file = javaClass.getResource("/testFiles/testText.txt")?.file
        assertNotNull(file)

        for (attribute in DetectFunction.entries) {
            assertEquals(1, getCountOfAttribute(file, attribute))
        }
    }

    @Test
    fun testCardEdge() {
        val file = javaClass.getResource("/testFiles/cardNumber/edge.txt")?.file
        assertNotNull(file)
        assertEquals(5, getCountOfAttribute(file, DetectFunction.CardNumbers))
    }

    @Test
    fun testCardWithBrace() {
        val file = javaClass.getResource("/testFiles/cardNumber/braces.txt")?.file
        assertNotNull(file)
        assertEquals(3, getCountOfAttribute(file, DetectFunction.CardNumbers))
    }

    @Test
    fun testCardWithSmth() {
        val file = javaClass.getResource("/testFiles/cardNumber/smth.txt")?.file
        assertNotNull(file)
        assertEquals(8, getCountOfAttribute(file, DetectFunction.CardNumbers))
    }

    @Test
    fun testCardWithStar() {
        val file = javaClass.getResource("/testFiles/cardNumber/star.txt")?.file
        assertNotNull(file)
        assertEquals(1, getCountOfAttribute(file, DetectFunction.CardNumbers))
    }

    @Test
    fun testCardNotValid() {
        val file = javaClass.getResource("/testFiles/cardNumber/notValid.txt")?.file
        assertNotNull(file)
        assertEquals(0, getCountOfAttribute(file, DetectFunction.CardNumbers))
    }

    @Test
    fun testSnilsEdge() {
        val file = javaClass.getResource("/testFiles/snils/edge.txt")?.file
        assertNotNull(file)
        assertEquals(6, getCountOfAttribute(file, DetectFunction.SNILS))
    }

    @Test
    fun testSnilsWithBrace() {
        val file = javaClass.getResource("/testFiles/snils/braces.txt")?.file
        assertNotNull(file)
        assertEquals(5, getCountOfAttribute(file, DetectFunction.SNILS))
    }

    @Test
    fun testSnilsWithSmth() {
        val file = javaClass.getResource("/testFiles/snils/smth.txt")?.file
        assertNotNull(file)
        assertEquals(6, getCountOfAttribute(file, DetectFunction.SNILS))
    }

    @Test
    fun testSnilsWithStar() {
        val file = javaClass.getResource("/testFiles/snils/star.txt")?.file
        assertNotNull(file)
        assertEquals(1, getCountOfAttribute(file, DetectFunction.SNILS))
    }

    @Test
    fun testSnilsNotValid() {
        val file = javaClass.getResource("/testFiles/snils/notValid.txt")?.file
        assertNotNull(file)
        assertEquals(0, getCountOfAttribute(file, DetectFunction.SNILS))
    }

    @Test
    fun testInnEdge() {
        val file = javaClass.getResource("/testFiles/inns/edge.txt")?.file
        assertNotNull(file)
        assertEquals(5, getCountOfAttribute(file, DetectFunction.INN))
    }

    @Test
    fun testInnWithBrace() {
        val file = javaClass.getResource("/testFiles/inns/braces.txt")?.file
        assertNotNull(file)
        assertEquals(2, getCountOfAttribute(file, DetectFunction.INN))
    }

    @Test
    fun testInnWithSmth() {
        val file = javaClass.getResource("/testFiles/inns/smth.txt")?.file
        assertNotNull(file)
        assertEquals(7, getCountOfAttribute(file, DetectFunction.INN))
    }

    @Test
    fun testInnWithStar() {
        val file = javaClass.getResource("/testFiles/inns/star.txt")?.file
        assertNotNull(file)
        assertEquals(1, getCountOfAttribute(file, DetectFunction.INN))
    }

    @Test
    fun testInnNotValid() {
        val file = javaClass.getResource("/testFiles/inns/notValid.txt")?.file
        assertNotNull(file)
        assertEquals(0, getCountOfAttribute(file, DetectFunction.INN))
    }

    @Test
    fun testIP() {
        val file = javaClass.getResource("/testFiles/ip.txt")?.file
        assertNotNull(file)
        assertEquals(4, getCountOfAttribute(file, DetectFunction.IP))
    }

    @Test
    fun testIPFalse() {
        val file = javaClass.getResource("/testFiles/ip_false.txt")?.file
        assertNotNull(file)
        assertEquals(0, getCountOfAttribute(file, DetectFunction.IP))
    }

    @Test
    fun testIPBoundary() {
        val ip1 = "1.1.1.1"
        val ip2 = "255.255.255.255"

        val text1 = Cleaner.cleanText(ip1)
        assertEquals(1, DetectFunction.IP.scan(text1).count())

        val text2 = Cleaner.cleanText(ip2)
        assertEquals(1, DetectFunction.IP.scan(text2).count())
    }

    @Test
    fun testIPv6() {
        // в этом файле 2 несокращенных IPv6 и очень много разнообразных сокращенных
        // в данный момент распознаем только несокращенные IPv6
        val file = javaClass.getResource("/testFiles/ipv6.txt")?.file
        assertNotNull(file)
        assertEquals(2, getCountOfAttribute(file, DetectFunction.IPv6))
    }

    @Test
    fun testIPv6False() {
        val file = javaClass.getResource("/testFiles/ipv6_false.txt")?.file
        assertNotNull(file)
        assertEquals(0, getCountOfAttribute(file, DetectFunction.IPv6))
    }

    @Test
    fun testPhones() {
        val file = javaClass.getResource("/testFiles/phones.txt")?.file
        assertNotNull(file)
        assertEquals(40, getCountOfAttribute(file, DetectFunction.Phones))
    }

    @Test
    fun testPassports() {
        val file = javaClass.getResource("/testFiles/passport/passport.txt")?.file
        assertNotNull(file)
        assertEquals(1, getCountOfAttribute(file, DetectFunction.Passport))
    }

    @Test
    fun getContext() {
        val filePath = javaClass.getResource("/testFiles/first.csv")?.file
        assertNotNull(filePath)
        val file = File(filePath)
        assertTrue(file.exists())
        val text = file.readText()
        val searchRes = DetectFunction.CardNumbers.scan(text)
        assertEquals(1, searchRes.count())
        assertEquals("4276 8070 1492 7948", searchRes.first().value)
        assertEquals("83\r\nКарта ", searchRes.first().before)
        assertEquals("\r\nг. Санкт", searchRes.first().after)
    }

    @Test
    fun scan(){
        val filePath = javaClass.getResource("/testFiles/first.csv")?.file
        assertNotNull(filePath)
        val file = File(filePath)
        assertTrue(file.exists())
        val text = file.readText()

        //Check Email
        assertEquals(2, DetectFunction.Emails.scan(text).count())
        //Check CardNumber
        assertEquals(1, DetectFunction.CardNumbers.scan(text).count())
        //Check Phone
        assertEquals(2, DetectFunction.Phones.scan(text).count())
        //Check AccountNumber
        assertEquals(1, DetectFunction.AccountNumber.scan(text).count())
        //Check CarNumber
        assertEquals(2, DetectFunction.CarNumber.scan(text).count())
        //Check SNILS
        assertEquals(1, DetectFunction.SNILS.scan(text).count())
        //Check Passport
        assertEquals(2, DetectFunction.Passport.scan(text).count())
        //Check OMS
        assertEquals(1, DetectFunction.OMS.scan(text).count())
        //Check INN
        assertEquals(1, DetectFunction.INN.scan(text).count())
        //Check Address
        assertEquals(1, DetectFunction.Address.scan(text).count())
        //Check ValuableInfo
        assertEquals(2, DetectFunction.ValuableInfo.scan(text).count())
        //Check Login
        assertEquals(1, DetectFunction.Login.scan(text).count())
        //Check Password
        assertEquals(1, DetectFunction.Password.scan(text).count())
        //Check CVV
        assertEquals(1, DetectFunction.CVV.scan(text).count())
        //Check FullName
        assertEquals(3, DetectFunction.Name.scan(text).count())
        //Check IP
        assertEquals(1, DetectFunction.IP.scan(text).count())
        //Check IPv6
        assertEquals(1, DetectFunction.IPv6.scan(text).count())
    }


    private fun getCountOfAttribute(filePath: String, field: DetectFunction): Int {
        val file = File(filePath)

        assertEquals(true, file.exists())

        val text = file.readText()//Cleaner.cleanText(file.readText())
        return field.scan(text).count()
    }
}