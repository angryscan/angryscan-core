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
import java.util.*

@Serializable
class HyperScanEngine(@Serializable override val matchers: List<IHyperMatcher>) : IScanEngine {
    @Transient
    private val expressions =
        matchers
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
                if (!validate.isValid)
                    throw Exception("Not valid pattern: ${pair.first}")

                expr to pair.second //Результат с возможностью обратного преобразования
            }.associate { it.first to it.second }

    @Transient
    private val database = Database.compile(expressions.keys.toList())
    @Transient
    private val scanner = Scanner().also {
        it.allocScratch(database)
    }

    override fun scan(text: String): List<Match> {
        val res = scanner
            .scan(
                database,
                text
                    .replace("\u0000", "")
                    .replace('\u000B', '\n')
            )
            .filter {
                expressions[it.matchedExpression]!!.check(it.matchedString)
            }
            .toMutableList()

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
                matcher = expressions[it.matchedExpression]!!
            )
        }.distinct()
    }

    override fun close() {
        scanner.close()
        database.close()
    }
}