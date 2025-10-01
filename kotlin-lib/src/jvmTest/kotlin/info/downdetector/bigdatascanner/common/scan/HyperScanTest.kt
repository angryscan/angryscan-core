package info.downdetector.bigdatascanner.common.scan

import info.downdetector.bigdatascanner.common.functions.AccountNumber
import info.downdetector.bigdatascanner.common.functions.Address
import info.downdetector.bigdatascanner.common.functions.CVV
import info.downdetector.bigdatascanner.common.functions.CarNumber
import info.downdetector.bigdatascanner.common.functions.CardNumber
import info.downdetector.bigdatascanner.common.functions.Email
import info.downdetector.bigdatascanner.common.functions.FullName
import info.downdetector.bigdatascanner.common.functions.INN
import info.downdetector.bigdatascanner.common.functions.IP
import info.downdetector.bigdatascanner.common.functions.IPv6
import info.downdetector.bigdatascanner.common.functions.Login
import info.downdetector.bigdatascanner.common.functions.OMS
import info.downdetector.bigdatascanner.common.functions.Passport
import info.downdetector.bigdatascanner.common.functions.Password
import info.downdetector.bigdatascanner.common.functions.Phone
import info.downdetector.bigdatascanner.common.functions.SNILS
import info.downdetector.bigdatascanner.common.functions.ValuableInfo
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
                INN,
                Address,
                ValuableInfo,
                Login,
                Password,
                CVV,
                FullName,
                IP,
                IPv6
            )
        )
        val res = hyperScan.scan(file.readText())
        //Check Email
        assertContains(res.map { it.matcher }, Email)
        assertEquals(2,res.count { it.matcher == Email })
        //Check CardNumber
        assertContains(res.map { it.matcher }, CardNumber)
        assertEquals(1,res.count { it.matcher == CardNumber })
        //Check Phone
        assertContains(res.map { it.matcher }, Phone)
        assertEquals(2,res.count { it.matcher == Phone })
        //Check AccountNumber
        assertContains(res.map { it.matcher }, AccountNumber)
        assertEquals(1,res.count { it.matcher == AccountNumber })
        //Check CarNumber
        assertContains(res.map { it.matcher }, CarNumber)
        assertEquals(2,res.count { it.matcher == CarNumber })
        //Check SNILS
        assertContains(res.map { it.matcher }, SNILS)
        assertEquals(1, res.count { it.matcher == SNILS })
        //Check Passport
        assertContains(res.map { it.matcher }, Passport)
        assertEquals(2,res.count { it.matcher == Passport })
        //Check OMS
        assertContains(res.map { it.matcher }, OMS)
        assertEquals(1,res.count { it.matcher == OMS })
        //Check INN
        assertContains(res.map { it.matcher }, INN)
        assertEquals(1, res.count { it.matcher == INN })
        //Check Address
        assertContains(res.map { it.matcher }, Address)
        assertEquals(1, res.count { it.matcher == Address })
        //Check ValuableInfo
        assertContains(res.map { it.matcher }, ValuableInfo)
        assertEquals(2, res.count { it.matcher == ValuableInfo })
        //Check Login
        assertContains(res.map { it.matcher }, Login)
        assertEquals(1, res.count { it.matcher == Login })
        //Check Password
        assertContains(res.map { it.matcher }, Password)
        assertEquals(1, res.count { it.matcher == Password })
        //Check CVV
        assertContains(res.map { it.matcher }, CVV)
        assertEquals(1, res.count { it.matcher == CVV })
        //Check FullName
        assertContains(res.map { it.matcher }, FullName)
        assertEquals(2, res.count { it.matcher == FullName })
        //Check IP
        assertContains(res.map { it.matcher }, IP)
        assertEquals(1, res.count { it.matcher == IP })
        //Check IPv6
        assertContains(res.map { it.matcher }, IPv6)
        assertEquals(1, res.count { it.matcher == IPv6 })
    }

}