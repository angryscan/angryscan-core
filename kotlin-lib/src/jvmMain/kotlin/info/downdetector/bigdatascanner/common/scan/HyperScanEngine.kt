package info.downdetector.bigdatascanner.common.scan

import com.gliwka.hyperscan.wrapper.Database
import com.gliwka.hyperscan.wrapper.Expression
import com.gliwka.hyperscan.wrapper.ExpressionFlag
import com.gliwka.hyperscan.wrapper.Scanner
import info.downdetector.bigdatascanner.common.engine.IHyperMatcher
import info.downdetector.bigdatascanner.common.engine.IScanEngine
import info.downdetector.bigdatascanner.common.extensions.Match
import info.downdetector.bigdatascanner.common.extensions.toExpressionFlag
import java.util.*

class HyperScanEngine(patterns: List<IHyperMatcher>): IScanEngine, AutoCloseable {
    private val expressions =
        patterns
            .flatMap { ip ->
                ip.hyperPatterns.map { hp ->
                    hp to ip
                }
            }.mapIndexed { index, pair ->
                //Конвертируем набор опций
                val es = EnumSet.of(ExpressionFlag.SOM_LEFTMOST)
                pair.second.expressionOptions.forEach {
                    es.add(it.toExpressionFlag())
                }

                //Создаем выражения для поиска
                val expr = Expression(
                    pair.first,
                    es,
                    index
                )

                // Проверяем выражения
                val validate = expr.validate()
                if(!validate.isValid)
                    throw Exception("Not valid pattern: ${pair.first}")

                expr to pair.second //Результат с возможностью обратного преобразования
            }.associate { it.first to it.second }

    private val database = Database.compile(expressions.keys.toList())


    override fun scan(text: String): List<Match> {
        val scanner = Scanner()
        scanner.allocScratch(database)
        val res = scanner.scan(database, text).filter {
            expressions[it.matchedExpression]!!.check(it.matchedString)
        }
        scanner.close()
        return res.map {
            Match(
                value = it.matchedString,
                before = text.substring(
                    maxOf(0, it.startPosition.toInt() - 10),
                    it.startPosition.toInt()
                ),
                after = text.substring(
                    it.endPosition.toInt(),
                    minOf(text.length, it.endPosition.toInt() + 10),
                ),
                startPosition = it.startPosition,
                endPosition = it.endPosition,
                matcher = expressions[it.matchedExpression]!!
            )
        }
    }

    override fun close() {
        database.close()
    }
}