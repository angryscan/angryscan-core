package info.downdetector.bigdatascanner.common.scan

import info.downdetector.bigdatascanner.common.functions.AccountNumber
import info.downdetector.bigdatascanner.common.functions.CarNumber
import info.downdetector.bigdatascanner.common.functions.CardNumber
import info.downdetector.bigdatascanner.common.functions.Email
import info.downdetector.bigdatascanner.common.functions.OMS
import info.downdetector.bigdatascanner.common.functions.Passport
import info.downdetector.bigdatascanner.common.functions.Phone
import info.downdetector.bigdatascanner.common.functions.SNILS
import java.io.File
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class HyperScanTest {
    @Test
    fun scan() {
        val filePath = javaClass.getResource("/files/first.csv")?.file
        assertNotNull(filePath)
        val file = File(filePath)

        assertEquals(true, file.exists())
        val hyperScan = HyperScan(
            listOf(
                Email,
                CardNumber,
                Phone,
                AccountNumber,
                CarNumber,
                SNILS,
                Passport,
                OMS,
            )
        )
        val res = hyperScan.scan(file.readText())
        //Check Email
        assertContains(res.map { it.matcher }, Email)
        assertEquals(res.count { it.matcher == Email }, 2)
        //Check CardNumber
        assertContains(res.map { it.matcher }, CardNumber)
        assertEquals(res.count { it.matcher == CardNumber }, 1)
        //Check Phone
        assertContains(res.map { it.matcher }, Phone)
        assertEquals(res.count { it.matcher == Phone }, 2)
        //Check AccountNumber
        assertContains(res.map { it.matcher }, AccountNumber)
        assertEquals(res.count { it.matcher == AccountNumber }, 1)
        //Check CarNumber
        assertContains(res.map { it.matcher }, CarNumber)
        assertEquals(res.count { it.matcher == CarNumber }, 2)
        //Check SNILS
        assertContains(res.map { it.matcher }, SNILS)
        assertEquals(res.count { it.matcher == SNILS }, 1)
        //Check Passport
        assertContains(res.map { it.matcher }, Passport)
        assertEquals(res.count { it.matcher == Passport }, 2)
        //Check OMS
        assertContains(res.map { it.matcher }, OMS)
        assertEquals(res.count { it.matcher == OMS }, 1)
    }

}