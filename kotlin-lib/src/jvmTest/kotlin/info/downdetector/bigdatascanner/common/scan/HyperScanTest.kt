package info.downdetector.bigdatascanner.common.scan

import info.downdetector.bigdatascanner.common.functions.*
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class HyperScanTest {
    @Test
    fun scan() {
        val filePath = javaClass.getResource("/testFiles/first.csv")?.file
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
        assertEquals(2, res.count { it.matcher == Email })
        //Check CardNumber
        assertEquals(1, res.count { it.matcher == CardNumber })
        //Check Phone
        assertEquals(2, res.count { it.matcher == Phone })
        //Check AccountNumber
        assertEquals(1, res.count { it.matcher == AccountNumber })
        //Check CarNumber
        assertEquals(2, res.count { it.matcher == CarNumber })
        //Check SNILS
        assertEquals(1, res.count { it.matcher == SNILS })
        //Check Passport
        assertEquals(2, res.count { it.matcher == Passport })
        //Check OMS
        assertEquals(1, res.count { it.matcher == OMS })
        //Check INN
        assertEquals(1, res.count { it.matcher == INN })
        //Check Address
        assertEquals(1, res.count { it.matcher == Address })
        //Check ValuableInfo
        assertEquals(2, res.count { it.matcher == ValuableInfo })
        //Check Login
        assertEquals(1, res.count { it.matcher == Login })
        //Check Password
        assertEquals(1, res.count { it.matcher == Password })
        //Check CVV
        assertEquals(1, res.count { it.matcher == CVV })
        //Check FullName
        assertEquals(3, res.count { it.matcher == FullName })
        //Check IP
        assertEquals(1, res.count { it.matcher == IP })
        //Check IPv6
        assertEquals(1, res.count { it.matcher == IPv6 })
    }
}