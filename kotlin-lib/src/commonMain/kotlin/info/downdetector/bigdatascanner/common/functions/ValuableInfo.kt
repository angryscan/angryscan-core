package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

fun findValuableInfo(text: String) = regexDetector(
    text,
    """(\.|\s|^)(СЕКРЕТ|КОНФИДЕНЦИАЛЬН|КОМПЕНСАЦ|КОММЕРЧ|ТАЙНА|КЛЮЧ|ШИФР|PIN|SECRET|PRIVACY|ДЕТАЛИ ПЛАТЕЖА|НАЗНАЧЕНИЕ ПЛАТЕЖА|DETAILS OF PAYMENT|PAYMENT DETAILS|БЕЗОПАСНОСТ|ВНУТРИБАНК|ФСБ|ФЕДЕРАЛ|ФСО|РАЗВЕДК|НАЦИОНАЛЬН|ГВАРДИ|МИНИСТЕРСТВО|МВД|ОБОРОН|МЧС|ПРЕМЬЕР|VIP|МВС|МВК|СКУД|ИНКАССАЦИЯ|ГОСУДАРСТВ)[А-Яа-яA-Z-a-z]*(\.|\s|$)"""
        .toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
)