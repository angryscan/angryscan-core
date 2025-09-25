package info.downdetector.bigdatascanner.common.scan

import info.downdetector.bigdatascanner.common.functions.Email
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
        val hyperScan = HyperScan(listOf(Email))
        val res = hyperScan.scan(file.readText())
        assertContains(res.map { it.matcher }, Email)
        assertEquals(res.count { it.matcher == Email }, 2)
    }

}