package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object IPv6 : IHyperMatcher, IKotlinMatcher {
    override val name = "IPv6"
    override val javaPatterns = listOf(
        """(?<![0-9a-fA-F:])((?:[0-9a-fA-F]{4}:){7}[0-9a-fA-F]{4})(?![0-9a-fA-F:])"""
    )
    override val regexOptions = setOf(
        RegexOption.MULTILINE,
        RegexOption.IGNORE_CASE
    )

    override val hyperPatterns = listOf(
        """(^|[^0-9a-fA-F:])((?:[0-9a-fA-F]{4}:){7}[0-9a-fA-F]{4})($|[^0-9a-fA-F:])"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean {
        // Паттерн HyperScan может захватывать символ перед адресом, нужно его убрать
        // Извлекаем только IPv6 адрес (вторая группа в паттерне)
        val cleaned = value.trim().let {
            // Если строка начинается с не-шестнадцатеричного символа, убираем его
            if (it.isNotEmpty() && !it.first().toString().matches(Regex("[0-9a-fA-F:]"))) {
                it.drop(1)
            } else {
                it
            }
        }.trimEnd().let {
            // Убираем символ после адреса, если он не является частью IPv6
            if (it.isNotEmpty() && !it.last().toString().matches(Regex("[0-9a-fA-F:]"))) {
                it.dropLast(1)
            } else {
                it
            }
        }
        // Проверяем, что это валидный IPv6 адрес
        val pattern = Regex("""^((?:[0-9a-fA-F]{4}:){7}[0-9a-fA-F]{4})$""", RegexOption.IGNORE_CASE)
        return pattern.matches(cleaned)
    }

    override fun toString() = name
}
