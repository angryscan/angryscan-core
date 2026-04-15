package org.angryscan.common.engine.hyperscan

import com.gliwka.hyperscan.wrapper.Database
import com.gliwka.hyperscan.wrapper.Expression
import com.gliwka.hyperscan.wrapper.ExpressionFlag
import com.gliwka.hyperscan.wrapper.Scanner
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.angryscan.common.engine.IScanEngine
import org.angryscan.common.engine.Match
import org.angryscan.common.extensions.toExpressionFlag
import java.io.*
import java.security.MessageDigest
import java.util.*

@Serializable
class HyperScanEngine(
    @Serializable override val matchers: List<IHyperMatcher>,
    private val requireKeywords: Boolean = true
) : IScanEngine {
    @Transient
    private val expressionEntries: List<Pair<Expression, IHyperMatcher>> =
        buildExpressionEntries(matchers, requireKeywords)

    @Transient
    private val matcherByExpressionId: Map<Int, IHyperMatcher> =
        expressionEntries.associate { (expr, matcher) -> expr.id to matcher }

    @Transient
    private var database: Database = preloadedDatabase.get()
        ?: Database.compile(expressionEntries.map { it.first })

    @Transient
    private var scanner: Scanner = Scanner().also {
        it.allocScratch(database)
    }

    override fun scan(text: String): List<Match> {
        val normalizedText = text
            .replace("\u0000", "")
            .replace('\u000B', '\n')

        val res = scanner
            .scan(database, normalizedText)
            .filter {
                matcherByExpressionId[it.matchedExpression.id]!!.check(it.matchedString)
            }

        return res.map {
            Match(
                value = it.matchedString,
                before = text.substring(
                    maxOf(0, it.startPosition.toInt() - 10),
                    it.startPosition.toInt()
                ),
                after = text.substring(
                    it.endPosition.toInt() + 1,
                    minOf(text.length, it.endPosition.toInt() + 11),
                ),
                startPosition = it.startPosition,
                endPosition = it.endPosition,
                matcher = matcherByExpressionId[it.matchedExpression.id]!!
            )
        }.distinct()
    }

    override fun close() {
        scanner.close()
        database.close()
    }

    /**
     * Serializes the compiled database with a compatibility manifest.
     * The returned bytes can later be passed to [fromCompiledDatabase] for fast initialization.
     */
    fun saveCompiledDatabase(): ByteArray {
        val baos = ByteArrayOutputStream()
        saveCompiledDatabase(baos)
        return baos.toByteArray()
    }

    /**
     * Serializes the compiled database to the given [output] stream.
     * The stream is NOT closed by this method.
     */
    fun saveCompiledDatabase(output: OutputStream) {
        val dos = DataOutputStream(output)
        dos.write(MAGIC)
        dos.writeInt(FORMAT_VERSION)
        val signature = computeSignature(matchers, requireKeywords)
        dos.writeInt(signature.size)
        dos.write(signature)
        database.save(dos)
        dos.flush()
    }

    /**
     * Serializes the compiled database to the given [file].
     */
    fun saveCompiledDatabase(file: File) {
        file.outputStream().buffered().use { saveCompiledDatabase(it) }
    }

    companion object {
        private val MAGIC = "HSCE".toByteArray(Charsets.US_ASCII)
        private const val FORMAT_VERSION = 1

        // ThreadLocal used by factory methods to inject a pre-loaded Database
        // into the primary constructor path, avoiding redundant compilation.
        private val preloadedDatabase = ThreadLocal<Database?>()

        /**
         * Creates an engine from a previously saved compiled database ([ByteArray]).
         *
         * @throws IllegalArgumentException if the data is corrupted, has wrong format,
         *   or is incompatible with the provided [matchers]/[requireKeywords] configuration.
         */
        fun fromCompiledDatabase(
            matchers: List<IHyperMatcher>,
            compiledDatabase: ByteArray,
            requireKeywords: Boolean = true
        ): HyperScanEngine =
            fromCompiledDatabase(matchers, ByteArrayInputStream(compiledDatabase), requireKeywords)

        /**
         * Creates an engine from a previously saved compiled database ([InputStream]).
         * The stream is NOT closed by this method.
         */
        fun fromCompiledDatabase(
            matchers: List<IHyperMatcher>,
            input: InputStream,
            requireKeywords: Boolean = true
        ): HyperScanEngine {
            val buffered = if (input.markSupported()) input else BufferedInputStream(input)
            val dis = DataInputStream(buffered)

            val magic = ByteArray(4)
            dis.readFully(magic)
            if (!magic.contentEquals(MAGIC))
                throw IllegalArgumentException(
                    "Invalid compiled database format: wrong magic bytes"
                )

            val version = dis.readInt()
            if (version != FORMAT_VERSION)
                throw IllegalArgumentException(
                    "Unsupported compiled database format version: $version"
                )

            val expectedSignature = computeSignature(matchers, requireKeywords)

            val sigLen = dis.readInt()
            if (sigLen != expectedSignature.size)
                throw IllegalArgumentException(
                    "Invalid compiled database format: unexpected signature length $sigLen (expected ${expectedSignature.size})"
                )

            val storedSignature = ByteArray(sigLen)
            dis.readFully(storedSignature)

            if (!storedSignature.contentEquals(expectedSignature))
                throw IllegalArgumentException(
                    "Compiled database is incompatible with the provided matchers/requireKeywords configuration"
                )

            val loadedDb = Database.load(dis)

            preloadedDatabase.set(loadedDb)
            try {
                return HyperScanEngine(matchers, requireKeywords)
            } catch (e: Throwable) {
                loadedDb.close()
                throw e
            } finally {
                preloadedDatabase.remove()
            }
        }

        /**
         * Creates an engine from a previously saved compiled database ([File]).
         */
        fun fromCompiledDatabase(
            matchers: List<IHyperMatcher>,
            file: File,
            requireKeywords: Boolean = true
        ): HyperScanEngine =
            file.inputStream().buffered().use {
                fromCompiledDatabase(matchers, it, requireKeywords)
            }

        internal fun buildExpressionEntries(
            matchers: List<IHyperMatcher>,
            requireKeywords: Boolean
        ): List<Pair<Expression, IHyperMatcher>> =
            matchers
                .flatMap { matcher ->
                    matcher.getHyperPatterns(requireKeywords).map { pattern ->
                        pattern to matcher
                    }
                }
                .mapIndexed { index, (pattern, matcher) ->
                    val flags = EnumSet.of(ExpressionFlag.SOM_LEFTMOST)
                    matcher.expressionOptions.forEach { flags.add(it.toExpressionFlag()) }

                    val expr = Expression(pattern, flags, index)
                    val validate = expr.validate()
                    if (!validate.isValid)
                        throw Exception("Not valid pattern: $pattern")

                    expr to matcher
                }

        internal fun computeSignature(
            matchers: List<IHyperMatcher>,
            requireKeywords: Boolean
        ): ByteArray {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(if (requireKeywords) 1.toByte() else 0.toByte())
            matchers.forEach { matcher ->
                matcher.getHyperPatterns(requireKeywords).forEach { pattern ->
                    md.update(pattern.toByteArray(Charsets.UTF_8))
                    md.update(0)
                    matcher.expressionOptions.sortedBy { it.ordinal }.forEach { opt ->
                        md.update(opt.name.toByteArray(Charsets.UTF_8))
                        md.update(1)
                    }
                    md.update(2)
                }
            }
            return md.digest()
        }
    }
}
