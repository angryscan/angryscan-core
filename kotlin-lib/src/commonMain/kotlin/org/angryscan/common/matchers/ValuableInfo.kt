package org.angryscan.common.matchers

import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

object ValuableInfo : IHyperMatcher, IKotlinMatcher {
    override val name = "Valuable Info"
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


