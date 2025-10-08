package org.angryscan.common

import org.angryscan.common.functions.AccountNumber
import org.angryscan.common.functions.Address
import org.angryscan.common.functions.CVV
import org.angryscan.common.functions.CarNumber
import org.angryscan.common.functions.CardNumber
import org.angryscan.common.functions.Email
import org.angryscan.common.functions.FullName
import org.angryscan.common.functions.INN
import org.angryscan.common.functions.IP
import org.angryscan.common.functions.IPv6
import org.angryscan.common.functions.Login
import org.angryscan.common.functions.OMS
import org.angryscan.common.functions.Passport
import org.angryscan.common.functions.Password
import org.angryscan.common.functions.Phone
import org.angryscan.common.functions.SNILS
import org.angryscan.common.functions.ValuableInfo
import org.angryscan.common.scan.HyperScanEngine
import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class LongScanTest {
    @Test
    fun longScanDF() {
        val filePath = javaClass.getResource("/testFiles/first.csv")?.file
        assertNotNull(filePath)
        val file = File(filePath)

        assertEquals(true, file.exists())

        val text = file.readText()
            .let { t ->
                (0..100).joinToString { t + "\r\n" }
            }

        println("##### Detect Function Scan #####")

        var foundCount = 0
        val repeatCount = 100
        val progressBar = ProgressBar(repeatCount, "Scanning")
        val multipleScanTime: MutableList<Long> = mutableListOf()
        for (i in 0..repeatCount) {
            multipleScanTime.add(
                measureTimeMillis {
                    print("")
                    foundCount += DetectFunction.entries.sumOf { df -> df.scan(text).count() }
                }
            )
            progressBar.update(i)
        }
        println("Scan count: $foundCount")
        println("Multiple scan time: ${multipleScanTime.sumOf { it }}ms")
        println("First scan time: ${multipleScanTime[0]}ms")
        println("Second scan time: ${multipleScanTime[1]}ms")
        println("Max scan time: ${multipleScanTime.maxOrNull()}ms")
        println("Min scan time: ${multipleScanTime.minOrNull()}ms")
        println("Average scan time: ${multipleScanTime.sumOf { it } / 100}ms\n")
        assertEquals(244824, foundCount)
    }


    @Test
    fun longScanHS() {
        val filePath = javaClass.getResource("/testFiles/first.csv")?.file
        assertNotNull(filePath)
        val file = File(filePath)

        assertEquals(true, file.exists())
        val matchers = listOf(
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
        val hyperScan = HyperScanEngine(
            matchers
        )

        val text = file.readText()
            .let { t ->
                (0..100).joinToString { t + "\r\n" }
            }

        println("##### Hyper Scan #####")
        var foundCount = 0
        val repeatCount = 100
        val progressBar = ProgressBar(repeatCount, "Scanning")
        val multipleScanTime: MutableList<Long> = mutableListOf()
        for (i in 0..repeatCount) {
            multipleScanTime.add(
                measureTimeMillis {
                    foundCount += hyperScan.scan(text).count()
                }
            )
            progressBar.update(i)
        }
        println("Scan count: $foundCount")
        println("Multiple scan time: ${multipleScanTime.sumOf { it }}ms")
        println("First scan time: ${multipleScanTime[0]}ms")
        println("Second scan time: ${multipleScanTime[1]}ms")
        println("Max scan time: ${multipleScanTime.maxOrNull()}ms")
        println("Min scan time: ${multipleScanTime.minOrNull()}ms")
        println("Average scan time: ${multipleScanTime.sumOf { it } / 100}ms\n")
        assertEquals(244824, foundCount)
    }
}