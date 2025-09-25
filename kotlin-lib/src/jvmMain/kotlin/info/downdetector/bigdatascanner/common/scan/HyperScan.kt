package info.downdetector.bigdatascanner.common.scan

import com.gliwka.hyperscan.wrapper.Database
import com.gliwka.hyperscan.wrapper.Expression
import com.gliwka.hyperscan.wrapper.ExpressionFlag
import com.gliwka.hyperscan.wrapper.Scanner
import info.downdetector.bigdatascanner.common.extensions.HyperMatch
import info.downdetector.bigdatascanner.common.functions.IHyperPattern
import java.util.*

class HyperScan(patterns: List<IHyperPattern>) {
    private val expressions =
        patterns
            .flatMap { ip ->
                ip.hyperPatterns.map { hp ->
                    hp to ip
                }
            }.mapIndexed { index, pair ->
                //Конвертируем набор опций
                val es = EnumSet.of(ExpressionFlag.SOM_LEFTMOST)
                pair.second.options.forEach {
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


    fun scan(text: String): List<HyperMatch> {
        val db = Database.compile(expressions.keys.toList())
        val scanner = Scanner()
        scanner.allocScratch(db)
        val res = scanner.scan(db, text)
        return res.map {
            HyperMatch(
                value = it.matchedString,
                startPosition = it.startPosition,
                endPosition = it.endPosition,
                matcher = expressions[it.matchedExpression]!!
            )
        }
    }
}