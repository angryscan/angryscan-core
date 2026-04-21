package org.angryscan.common

import org.angryscan.common.engine.hyperscan.HyperScanEngine
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.kotlin.IKotlinMatcher
import org.angryscan.common.engine.kotlin.KotlinEngine
import org.angryscan.common.extensions.Matchers
import kotlin.math.sqrt
import kotlin.system.measureNanoTime
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * Benchmark for KotlinEngine and HyperScanEngine.
 *
 * Covers:
 *  1. Scan throughput — both engines on a representative text corpus.
 *  2. Init speed      — engine creation time.
 *  3. Consistency     — match count comparison between engines.
 *
 * Results are written to build/reports/benchmark-*.md and combined by CI
 * into a single PR comment (see .github/workflows/Tests.yml).
 *
 * Run locally:
 *   ./gradlew :kotlin-lib:jvmTest --tests "*BenchmarkTest*" --info
 */
internal class BenchmarkTest {

    // ── Configuration ──────────────────────────────────────────────────────────

    private val kotlinMatchers = Matchers.filterIsInstance<IKotlinMatcher>()
    private val hyperMatchers  = Matchers.filterIsInstance<IHyperMatcher>()

    companion object {
        private const val SCAN_WARMUP  = 5
        private const val SCAN_MEASURE = 20

        private const val INIT_WARMUP  = 1
        private const val INIT_MEASURE = 5

        /** Repeat base corpus to produce ~500 KB representative of real-world PII data. */
        private const val CORPUS_REPEAT = 250

        private val reportDir get() = java.io.File(System.getProperty("user.dir"), "build/reports")
    }

    // ── Text corpus ────────────────────────────────────────────────────────────

    /**
     * Builds a corpus from testText.txt — covers the widest variety of matchers:
     * email, phone, passport, SNILS, card number, INN, OMS, address, login, password,
     * IPv4/IPv6, full name, birthday, driver license, education doc, and more.
     */
    private fun buildCorpus(): String {
        val resource = javaClass.getResource("/testFiles/testText.txt")
            ?: error("testFiles/testText.txt not found in test resources")
        val base = java.io.File(resource.file).readText()
        return buildString(base.length * CORPUS_REPEAT + CORPUS_REPEAT) {
            repeat(CORPUS_REPEAT) { append(base); append('\n') }
        }
    }

    // ── Statistics ─────────────────────────────────────────────────────────────

    private data class Stats(
        val mean: Double, val stddev: Double,
        val min: Double,  val max: Double,
        val p50: Double
    )

    private fun LongArray.stats(): Stats {
        val ms     = map { it / 1_000_000.0 }
        val mean   = ms.average()
        val stddev = sqrt(ms.map { (it - mean) * (it - mean) }.average())
        val sorted = ms.sorted()
        return Stats(
            mean   = mean,
            stddev = stddev,
            min    = sorted.first(),
            max    = sorted.last(),
            p50    = sorted[size / 2]
        )
    }

    // ── Console helpers ────────────────────────────────────────────────────────

    private fun Stats.printScan(label: String, textBytes: Int, matchesPerIter: Int) {
        val throughput = (textBytes.toDouble() / 1024 / 1024) / (mean / 1000.0)
        println(
            "  %-18s  mean=%6.1f ms  p50=%6.1f  σ=%5.1f  throughput=%5.2f MB/s  matches=%d"
                .format(label, mean, p50, stddev, throughput, matchesPerIter)
        )
    }

    private fun Stats.printInit(label: String) {
        println(
            "  %-18s  mean=%6.1f ms  p50=%6.1f  min=%6.1f  max=%6.1f  σ=%5.1f"
                .format(label, mean, p50, min, max, stddev)
        )
    }

    // ── Markdown helpers ───────────────────────────────────────────────────────

    private fun Stats.mdScanRow(label: String, textBytes: Int, matchesPerIter: Int): String {
        val tp = (textBytes.toDouble() / 1024 / 1024) / (mean / 1000.0)
        return "| `$label` | %.1f ms | %.1f | %.1f | %.2f MB/s | %d |"
            .format(mean, p50, stddev, tp, matchesPerIter)
    }

    private fun Stats.mdInitRow(label: String): String =
        "| `$label` | %.1f ms | %.1f | %.1f | %.1f | %.1f |"
            .format(mean, p50, min, max, stddev)

    private fun writeReport(fileName: String, content: String) {
        val file = java.io.File(reportDir, fileName)
        file.parentFile.mkdirs()
        file.writeText(content)
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 1. Scan throughput
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    fun benchmarkScan() {
        val corpus = buildCorpus()

        println()
        println("╔══════════════════════════════════════════════════════════════════════╗")
        println("║              SCAN THROUGHPUT BENCHMARK                              ║")
        println("╠══════════════════════════════════════════════════════════════════════╣")
        println("║  Corpus: testText.txt × $CORPUS_REPEAT  ≈ ${corpus.length / 1024} KB")
        println("║  Matchers: ${kotlinMatchers.size} (Kotlin) / ${hyperMatchers.size} (HyperScan)")
        println("║  Warmup: $SCAN_WARMUP iters   Measure: $SCAN_MEASURE iters")
        println("╚══════════════════════════════════════════════════════════════════════╝")

        val compiledDb = HyperScanEngine(hyperMatchers).use { it.saveCompiledDatabase() }

        val kotlinStats: Stats
        val hyperStats:  Stats
        val preloadStats: Stats
        var kotlinMatches  = 0
        var hyperMatches   = 0
        var preloadMatches = 0

        KotlinEngine(kotlinMatchers).use { engine ->
            print("  KotlinEngine    warming up... "); System.out.flush()
            repeat(SCAN_WARMUP) { engine.scan(corpus) }
            println("done")
            val times = LongArray(SCAN_MEASURE)
            repeat(SCAN_MEASURE) { i -> times[i] = measureNanoTime { kotlinMatches += engine.scan(corpus).size } }
            kotlinStats = times.stats()
        }

        HyperScanEngine(hyperMatchers).use { engine ->
            print("  HyperScanEngine warming up... "); System.out.flush()
            repeat(SCAN_WARMUP) { engine.scan(corpus) }
            println("done")
            val times = LongArray(SCAN_MEASURE)
            repeat(SCAN_MEASURE) { i -> times[i] = measureNanoTime { hyperMatches += engine.scan(corpus).size } }
            hyperStats = times.stats()
        }

        HyperScanEngine.fromCompiledDatabase(hyperMatchers, compiledDb).use { engine ->
            print("  Hyper(preload)  warming up... "); System.out.flush()
            repeat(SCAN_WARMUP) { engine.scan(corpus) }
            println("done")
            val times = LongArray(SCAN_MEASURE)
            repeat(SCAN_MEASURE) { i -> times[i] = measureNanoTime { preloadMatches += engine.scan(corpus).size } }
            preloadStats = times.stats()
        }

        println("  --- results ---")
        kotlinStats.printScan("KotlinEngine",    corpus.length, kotlinMatches  / SCAN_MEASURE)
        hyperStats.printScan("HyperScanEngine",  corpus.length, hyperMatches   / SCAN_MEASURE)
        preloadStats.printScan("Hyper(preload)",  corpus.length, preloadMatches / SCAN_MEASURE)
        println()

        writeReport("benchmark-scan.md", buildString {
            appendLine("### Scan Throughput")
            appendLine()
            appendLine("Corpus: `testText.txt × $CORPUS_REPEAT ≈ ${corpus.length / 1024} KB` | " +
                       "Matchers: ${kotlinMatchers.size} | " +
                       "Warmup: $SCAN_WARMUP | Iterations: $SCAN_MEASURE")
            appendLine()
            appendLine("| Engine | mean | p50 | σ | Throughput | Matches/iter |")
            appendLine("|:---|---:|---:|---:|---:|---:|")
            appendLine(kotlinStats.mdScanRow("KotlinEngine",      corpus.length, kotlinMatches  / SCAN_MEASURE))
            appendLine(hyperStats.mdScanRow("HyperScanEngine",    corpus.length, hyperMatches   / SCAN_MEASURE))
            appendLine(preloadStats.mdScanRow("HyperScan(preload)", corpus.length, preloadMatches / SCAN_MEASURE))
        })
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 2. Init speed
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    fun benchmarkInit() {
        println()
        println("╔══════════════════════════════════════════════════════════════════════╗")
        println("║              INIT SPEED BENCHMARK                                   ║")
        println("╠══════════════════════════════════════════════════════════════════════╣")
        println("║  Matchers: ${kotlinMatchers.size} (Kotlin) / ${hyperMatchers.size} (HyperScan)")
        println("║  Warmup: $INIT_WARMUP iters   Measure: $INIT_MEASURE iters")
        println("╚══════════════════════════════════════════════════════════════════════╝")

        val compiledDb = HyperScanEngine(hyperMatchers).use { it.saveCompiledDatabase() }

        val kotlinStats   = measureInit("KotlinEngine   ") { KotlinEngine(kotlinMatchers).also { it.close() } }
        val hyperStats    = measureInit("HyperScanEngine") { HyperScanEngine(hyperMatchers).also { it.close() } }
        val preloadStats  = measureInit("Hyper(preload) ") {
            HyperScanEngine.fromCompiledDatabase(hyperMatchers, compiledDb).also { it.close() }
        }

        println("  --- results ---")
        kotlinStats.printInit("KotlinEngine")
        hyperStats.printInit("HyperScanEngine")
        preloadStats.printInit("Hyper(preload)")
        val speedup = if (preloadStats.mean > 0) hyperStats.mean / preloadStats.mean else Double.NaN
        println("  Pre-compiled DB speedup: %.1fx".format(speedup))
        println()

        writeReport("benchmark-init.md", buildString {
            appendLine("### Init Speed")
            appendLine()
            appendLine("Matchers: ${kotlinMatchers.size} | Warmup: $INIT_WARMUP | Iterations: $INIT_MEASURE")
            appendLine()
            appendLine("| Engine | mean | p50 | min | max | σ |")
            appendLine("|:---|---:|---:|---:|---:|---:|")
            appendLine(kotlinStats.mdInitRow("KotlinEngine"))
            appendLine(hyperStats.mdInitRow("HyperScanEngine"))
            appendLine(preloadStats.mdInitRow("HyperScan(preload)"))
            appendLine()
            appendLine("_Pre-compiled DB speedup: **%.1fx**_".format(speedup))
        })
    }

    private fun measureInit(label: String, factory: () -> Unit): Stats {
        print("  $label warming up... "); System.out.flush()
        repeat(INIT_WARMUP) { factory() }
        println("done")
        val times = LongArray(INIT_MEASURE)
        repeat(INIT_MEASURE) { i -> times[i] = measureNanoTime { factory() } }
        return times.stats()
    }

    // ══════════════════════════════════════════════════════════════════════════
    // 3. Consistency check
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Prints match counts from both engines on the same text.
     * A small delta is expected — engines share patterns but differ in edge-case
     * matching behaviour; known differences are documented in EngineTest.
     */
    @Test
    fun verifyScanConsistency() {
        val resource = javaClass.getResource("/testFiles/testText.txt")
        assertNotNull(resource, "testText.txt not found")
        val text = java.io.File(resource.file).readText()

        val compiledDb = HyperScanEngine(hyperMatchers).use { it.saveCompiledDatabase() }

        val kotlinCount  = KotlinEngine(kotlinMatchers).use { it.scan(text).size }
        val hyperCount   = HyperScanEngine(hyperMatchers).use { it.scan(text).size }
        val preloadCount = HyperScanEngine.fromCompiledDatabase(hyperMatchers, compiledDb).use { it.scan(text).size }

        println("\n  Consistency check on testText.txt:")
        println("  KotlinEngine       → $kotlinCount matches")
        println("  HyperScanEngine    → $hyperCount matches")
        println("  Hyper(preload)     → $preloadCount matches")

        val note = if (kotlinCount == hyperCount) "equal"
                   else "delta=${hyperCount - kotlinCount} (expected — see EngineTest for known differences)"
        val preloadNote = if (hyperCount == preloadCount) "preload matches compile"
                          else "MISMATCH: preload=$preloadCount vs compile=$hyperCount"
        println("  Result: $note  |  $preloadNote")

        writeReport("benchmark-consistency.md", buildString {
            appendLine("### Match Consistency (`testText.txt`)")
            appendLine()
            appendLine("| Engine | Matches |")
            appendLine("|:---|---:|")
            appendLine("| `KotlinEngine` | $kotlinCount |")
            appendLine("| `HyperScanEngine` | $hyperCount |")
            appendLine("| `HyperScan(preload)` | $preloadCount |")
            appendLine()
            appendLine("_${note}_ | _${preloadNote}_")
        })
    }
}
