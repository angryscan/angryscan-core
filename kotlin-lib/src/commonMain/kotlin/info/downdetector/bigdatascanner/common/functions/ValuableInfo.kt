package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.engine.IHyperMatcher
import info.downdetector.bigdatascanner.common.engine.ExpressionOption
import info.downdetector.bigdatascanner.common.engine.IKotlinMatcher

object ValuableInfo : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """(\.|\s|^)(СЕКРЕТ|КОНФИДЕНЦИАЛЬН|КОМПЕНСАЦ|КОММЕРЧ|ТАЙНА|КЛЮЧ|ШИФР|PIN|SECRET|PRIVACY|ДЕТАЛИ ПЛАТЕЖА|НАЗНАЧЕНИЕ ПЛАТЕЖА|DETAILS OF PAYMENT|PAYMENT DETAILS|БЕЗОПАСНОСТ|ВНУТРИБАНК|ФСБ|ФЕДЕРАЛ|ФСО|РАЗВЕДК|НАЦИОНАЛЬН|ГВАРДИ|МИНИСТЕРСТВО|МВД|ОБОРОН|МЧС|ПРЕМЬЕР|VIP|МВС|МВК|СКУД|ИНКАССАЦИЯ|ГОСУДАРСТВ)[А-Яа-яA-Za-z]*(\.|\s|$)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(\.|\s|^)(СЕКРЕТ|КОНФИДЕНЦИАЛЬН|КОМПЕНСАЦ|КОММЕРЧ|ТАЙНА|КЛЮЧ|ШИФР)[А-Яа-яA-Za-z]*(\.|\s|$)""",
        """(\.|\s|^)(PIN|SECRET|PRIVACY|ДЕТАЛИ ПЛАТЕЖА|НАЗНАЧЕНИЕ ПЛАТЕЖА|DETAILS OF PAYMENT|PAYMENT DETAILS)[А-Яа-яA-Za-z]*(\.|\s|$)""",
        """(\.|\s|^)(БЕЗОПАСНОСТ|ВНУТРИБАНК|ФСБ|ФЕДЕРАЛ|ФСО|РАЗВЕДК|НАЦИОНАЛЬН|ГВАРДИ|МИНИСТЕРСТВО|МВД|ОБОРОН)[А-Яа-яA-Za-z]*(\.|\s|$)""",
        """(\.|\s|^)(МЧС|ПРЕМЬЕР|VIP|МВС|МВК|СКУД|ИНКАССАЦИЯ|ГОСУДАРСТВ)[А-Яа-яA-Za-z]*(\.|\s|$)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8,
        ExpressionOption.MULTILINE
    )

    override fun check(value: String) = true
}


