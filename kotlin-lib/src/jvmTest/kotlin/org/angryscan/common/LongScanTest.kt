package org.angryscan.common

import org.angryscan.common.engine.IScanEngine
import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import org.angryscan.common.extensions.ProgressBar
import org.angryscan.common.matchers.*
import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class LongScanTest {
    fun longScan(engine: IScanEngine, filePath: String): Int {
        val file = File(filePath)

        assertEquals(true, file.exists())
        val text = file.readText()
            .let { t ->
                (0..99).joinToString { t + "\r\n" }
            }

        var foundCount = 0
        val progressBar = ProgressBar(100, "Scanning")
        val multipleScanTime: MutableList<Long> = mutableListOf()

        repeat(100) { i ->
            multipleScanTime.add(
                measureTimeMillis {
                    print("")
                    foundCount += engine.scan(text).count()
                }
            )
            progressBar.update(i + 1)
        }
        println("Scan count: $foundCount")
        println("Multiple scan time: ${multipleScanTime.sumOf { it }}ms")
        println("First scan time: ${multipleScanTime[0]}ms")
        println("Second scan time: ${multipleScanTime[1]}ms")
        println("Max scan time: ${multipleScanTime.maxOrNull()}ms")
        println("Min scan time: ${multipleScanTime.minOrNull()}ms")
        println("Average scan time: ${multipleScanTime.sumOf { it } / 100}ms\n")
        return foundCount
    }

    @Test
    fun longScanKotlin() {
        val filePath = javaClass.getResource("/testFiles/first.csv")?.file
        assertNotNull(filePath)

        println("##### Kotlin Engine #####")
        val matchers = listOf<IKotlinMatcher>(
            Email,
            CardNumber(),
            Phone,
            SNILS,
            Passport,
            OMS,
            INN,
            Address,
            Login,
            BankAccount,
            VehicleRegNumber,
            Password,
            CVV,
            FullName,
            IPv4,
            IPv6
        )
        KotlinEngine(matchers).use { kotlinEngine ->
            assertEquals(220000, longScan(kotlinEngine, filePath))
        }

    }


    @Test
    fun longScanHS() {
        val filePath = javaClass.getResource("/testFiles/first.csv")?.file
        assertNotNull(filePath)

        println("##### Hyper Scan #####")
        val matchers = listOf<IHyperMatcher>(
            Email,
            CardNumber(),
            Phone,
            SNILS,
            Passport,
            OMS,
            INN,
            Address,
            Login,
            BankAccount,
            VehicleRegNumber,
            Password,
            CVV,
            FullName,
            IPv4,
            IPv6
        )
        HyperScanEngine(matchers).use { hyperEngine ->
            assertEquals(220000, longScan(hyperEngine, filePath))
        }
    }
}