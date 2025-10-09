package org.angryscan.common.scan

import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.matchers.*
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


internal class HyperScanTest {
    @Test
    fun scan() {
        val filePath = javaClass.getResource("/testFiles/first.csv")?.file
        assertNotNull(filePath)
        val file = File(filePath)

        assertEquals(true, file.exists())
        val hyperScan = HyperScanEngine(
            listOf(
                Email,
                CardNumber(),
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
        assertEquals(2, res.count { it.matcher is Email })
        //Check CardNumber
        assertEquals(1, res.count { it.matcher is CardNumber })
        //Check Phone
        assertEquals(2, res.count { it.matcher is Phone })
        //Check AccountNumber
        assertEquals(1, res.count { it.matcher is AccountNumber })
        //Check CarNumber
        assertEquals(2, res.count { it.matcher is CarNumber })
        //Check SNILS
        assertEquals(1, res.count { it.matcher is SNILS })
        //Check Passport
        assertEquals(2, res.count { it.matcher is Passport })
        //Check OMS
        assertEquals(1, res.count { it.matcher is OMS })
        //Check INN
        assertEquals(1, res.count { it.matcher is INN })
        //Check Address
        assertEquals(1, res.count { it.matcher is Address })
        //Check ValuableInfo
        assertEquals(2, res.count { it.matcher is ValuableInfo })
        //Check Login
        assertEquals(1, res.count { it.matcher is Login })
        //Check Password
        assertEquals(1, res.count { it.matcher is Password })
        //Check CVV
        assertEquals(1, res.count { it.matcher is CVV })
        //Check FullName
        assertEquals(3, res.count { it.matcher is FullName })
        //Check IP
        assertEquals(1, res.count { it.matcher is IP })
        //Check IPv6
        assertEquals(1, res.count { it.matcher is IPv6 })
    }
}