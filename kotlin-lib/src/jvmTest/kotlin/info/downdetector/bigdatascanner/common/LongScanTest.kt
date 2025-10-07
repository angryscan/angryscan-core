package info.downdetector.bigdatascanner.common

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
import info.downdetector.bigdatascanner.common.scan.HyperScanEngine
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