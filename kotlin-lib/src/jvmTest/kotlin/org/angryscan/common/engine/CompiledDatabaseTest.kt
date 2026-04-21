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
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class CompiledDatabaseTest {

    private val hyperMatchers = Matchers.filterIsInstance<IHyperMatcher>()
    private val kotlinMatchers = Matchers.filterIsInstance<IKotlinMatcher>()

    private fun loadTestText(): String {
        val resource = javaClass.getResource("/testFiles/testText.txt")
        assertNotNull(resource, "testText.txt not found")
        return File(resource.file).readText()
    }

    private fun loadFirstCsv(): String {
        val resource = javaClass.getResource("/testFiles/first.csv")
        assertNotNull(resource, "first.csv not found")
        return File(resource.file).readText()
    }

    // ── Functional parity ───────────────────────────────────────────────────

    @Test
    fun loadFromBytes_matchesColdCompileResults() {
        val text = loadTestText()

        val compiledBytes: ByteArray
        val expectedResults: List<Match>

        HyperScanEngine(hyperMatchers).use { engine ->
            expectedResults = engine.scan(text)
            compiledBytes = engine.saveCompiledDatabase()
        }

        HyperScanEngine.fromCompiledDatabase(hyperMatchers, compiledBytes).use { engine ->
            val loadedResults = engine.scan(text)
            assertEquals(expectedResults.size, loadedResults.size, "Match count differs between compiled and loaded DB")
            expectedResults.zip(loadedResults).forEachIndexed { i, (expected, loaded) ->
                assertEquals(expected.value, loaded.value, "Match value differs at index $i")
                assertEquals(expected.startPosition, loaded.startPosition, "Start position differs at index $i")
                assertEquals(expected.endPosition, loaded.endPosition, "End position differs at index $i")
                assertEquals(expected.matcher.name, loaded.matcher.name, "Matcher name differs at index $i")
            }
        }
    }

    @Test
    fun loadFromFile_matchesColdCompileResults() {
        val text = loadFirstCsv()
        val tmpFile = File.createTempFile("hyperscan-db-", ".bin")

        try {
            val expectedResults: List<Match>

            HyperScanEngine(hyperMatchers).use { engine ->
                expectedResults = engine.scan(text)
                engine.saveCompiledDatabase(tmpFile)
            }

            assertTrue(tmpFile.length() > 0, "Compiled database file should not be empty")

            HyperScanEngine.fromCompiledDatabase(hyperMatchers, tmpFile).use { engine ->
                val loadedResults = engine.scan(text)
                assertEquals(expectedResults.size, loadedResults.size, "Match count differs between compiled and loaded DB from file")
                expectedResults.zip(loadedResults).forEachIndexed { i, (expected, loaded) ->
                    assertEquals(expected.value, loaded.value, "Match value differs at index $i")
                    assertEquals(expected.startPosition, loaded.startPosition, "Start position differs at index $i")
                    assertEquals(expected.endPosition, loaded.endPosition, "End position differs at index $i")
                    assertEquals(expected.matcher.name, loaded.matcher.name, "Matcher name differs at index $i")
                }
            }
        } finally {
            tmpFile.delete()
        }
    }

    @Test
    fun loadedDb_matchesKotlinEngineByCount() {
        val text = loadTestText()

        val compiledBytes: ByteArray
        HyperScanEngine(hyperMatchers).use { engine ->
            compiledBytes = engine.saveCompiledDatabase()
        }

        val kotlinCount = KotlinEngine(kotlinMatchers).use { it.scan(text).size }
        val loadedHyperCount = HyperScanEngine.fromCompiledDatabase(hyperMatchers, compiledBytes).use {
            it.scan(text).size
        }

        assertEquals(
            kotlinCount, loadedHyperCount,
            "Loaded HyperScanEngine match count ($loadedHyperCount) should equal KotlinEngine count ($kotlinCount)"
        )
    }

    // ── Context correctness ─────────────────────────────────────────────────

    @Test
    fun loadedDb_contextIsCorrect() {
        val text = loadFirstCsv()
        val singleMatcher = listOf(CardNumber() as IHyperMatcher)

        val compiledBytes: ByteArray
        HyperScanEngine(singleMatcher).use { engine ->
            compiledBytes = engine.saveCompiledDatabase()
        }

        HyperScanEngine.fromCompiledDatabase(singleMatcher, compiledBytes).use { engine ->
            val results = engine.scan(text)
            assertEquals(1, results.size, "Expected 1 CardNumber match")
            assertTrue(results.first().value.contains("4276 8070 1492 7948"))
            assertTrue(results.first().before.contains("Карта"))
            assertTrue(results.first().after.contains("г. Санкт"))
        }
    }

    // ── Strict-fail validation ──────────────────────────────────────────────

    @Test
    fun loadFailsOnMatcherSetMismatch() {
        val fullMatchers = hyperMatchers
        val subsetMatchers = listOf(Email as IHyperMatcher, Phone as IHyperMatcher)

        val compiledBytes: ByteArray
        HyperScanEngine(fullMatchers).use { engine ->
            compiledBytes = engine.saveCompiledDatabase()
        }

        assertFailsWith<IllegalArgumentException>(
            "Should fail when loading with a different matcher set"
        ) {
            HyperScanEngine.fromCompiledDatabase(subsetMatchers, compiledBytes)
        }
    }

    @Test
    fun loadFailsOnRequireKeywordsMismatch() {
        val compiledBytes: ByteArray
        HyperScanEngine(hyperMatchers, requireKeywords = true).use { engine ->
            compiledBytes = engine.saveCompiledDatabase()
        }

        assertFailsWith<IllegalArgumentException>(
            "Should fail when requireKeywords differs"
        ) {
            HyperScanEngine.fromCompiledDatabase(hyperMatchers, compiledBytes, requireKeywords = false)
        }
    }

    // ── Corrupted data ──────────────────────────────────────────────────────

    @Test
    fun loadFailsOnCorruptedManifest() {
        assertFailsWith<IllegalArgumentException>(
            "Should fail on random bytes"
        ) {
            HyperScanEngine.fromCompiledDatabase(hyperMatchers, ByteArray(64) { 0xFF.toByte() })
        }
    }

    @Test
    fun loadFailsOnEmptyBytes() {
        assertFailsWith<Exception>(
            "Should fail on empty bytes"
        ) {
            HyperScanEngine.fromCompiledDatabase(hyperMatchers, ByteArray(0))
        }
    }

    @Test
    fun loadFailsOnTruncatedManifest() {
        val compiledBytes: ByteArray
        HyperScanEngine(hyperMatchers).use { engine ->
            compiledBytes = engine.saveCompiledDatabase()
        }

        assertFailsWith<Exception>(
            "Should fail on truncated data"
        ) {
            HyperScanEngine.fromCompiledDatabase(hyperMatchers, compiledBytes.copyOf(10))
        }
    }

    // ── Resource lifecycle ──────────────────────────────────────────────────

    @Test
    fun loadedEngine_closeDoesNotThrow() {
        val compiledBytes: ByteArray
        HyperScanEngine(hyperMatchers).use { engine ->
            compiledBytes = engine.saveCompiledDatabase()
        }

        val engine = HyperScanEngine.fromCompiledDatabase(hyperMatchers, compiledBytes)
        engine.close()
    }

    @Test
    fun saveAndLoadMultipleTimes_consistent() {
        val text = loadTestText()

        val bytes1: ByteArray
        HyperScanEngine(hyperMatchers).use { engine ->
            bytes1 = engine.saveCompiledDatabase()
        }

        val bytes2: ByteArray
        HyperScanEngine.fromCompiledDatabase(hyperMatchers, bytes1).use { engine ->
            bytes2 = engine.saveCompiledDatabase()
        }

        HyperScanEngine.fromCompiledDatabase(hyperMatchers, bytes2).use { engine ->
            val results = engine.scan(text)
            assertTrue(results.isNotEmpty(), "Should find matches after double save/load cycle")
        }
    }
}
