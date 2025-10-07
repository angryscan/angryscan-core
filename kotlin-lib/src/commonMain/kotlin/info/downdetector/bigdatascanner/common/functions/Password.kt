package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.engine.IHyperMatcher
import info.downdetector.bigdatascanner.common.engine.ExpressionOption
import info.downdetector.bigdatascanner.common.engine.IKotlinMatcher

object Password : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """(((password|пароль)\s((?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@$}{'?;,:=+_\-]*))\S{3,25})|((password|пароль):\s?\S{3,25}))"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(password|пароль):?\s*\S{3,25}($|\s)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.CASELESS,
        ExpressionOption.MULTILINE,
        ExpressionOption.UTF8
    )

    override fun check(value: String) = true

}

