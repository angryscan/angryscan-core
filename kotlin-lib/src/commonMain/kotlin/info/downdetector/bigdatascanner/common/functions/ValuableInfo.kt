package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object ValuableInfo : IHyperPattern {
    const val JAVA_PATTERN =
        """(\.|\s|^)(СЕКРЕТ|КОНФИДЕНЦИАЛЬН|КОМПЕНСАЦ|КОММЕРЧ|ТАЙНА|КЛЮЧ|ШИФР|PIN|SECRET|PRIVACY|ДЕТАЛИ ПЛАТЕЖА|НАЗНАЧЕНИЕ ПЛАТЕЖА|DETAILS OF PAYMENT|PAYMENT DETAILS|БЕЗОПАСНОСТ|ВНУТРИБАНК|ФСБ|ФЕДЕРАЛ|ФСО|РАЗВЕДК|НАЦИОНАЛЬН|ГВАРДИ|МИНИСТЕРСТВО|МВД|ОБОРОН|МЧС|ПРЕМЬЕР|VIP|МВС|МВК|СКУД|ИНКАССАЦИЯ|ГОСУДАРСТВ)[А-Яа-яA-Za-z]*(\.|\s|$)"""

    fun find(text: String, withContext: Boolean) = regexDetector(
        text,
        JAVA_PATTERN
            .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
        withContext
    )

    override val hyperPatterns = listOf(
        """(\.|\s|^)(СЕКРЕТ|КОНФИДЕНЦИАЛЬН|КОМПЕНСАЦ|КОММЕРЧ|ТАЙНА|КЛЮЧ|ШИФР)[А-Яа-яA-Za-z]*(\.|\s|$)""",
        """(\.|\s|^)(PIN|SECRET|PRIVACY|ДЕТАЛИ ПЛАТЕЖА|НАЗНАЧЕНИЕ ПЛАТЕЖА|DETAILS OF PAYMENT|PAYMENT DETAILS)[А-Яа-яA-Za-z]*(\.|\s|$)""",
        """(\.|\s|^)(БЕЗОПАСНОСТ|ВНУТРИБАНК|ФСБ|ФЕДЕРАЛ|ФСО|РАЗВЕДК|НАЦИОНАЛЬН|ГВАРДИ|МИНИСТЕРСТВО|МВД|ОБОРОН)[А-Яа-яA-Za-z]*(\.|\s|$)""",
        """(\.|\s|^)(МЧС|ПРЕМЬЕР|VIP|МВС|МВК|СКУД|ИНКАССАЦИЯ|ГОСУДАРСТВ)[А-Яа-яA-Za-z]*(\.|\s|$)"""
    )
    override val options = setOf(
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8,
        ExpressionOption.MULTILINE
    )

    override fun check(value: String) = true
}


