package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.engine.IHyperMatcher
import info.downdetector.bigdatascanner.common.engine.ExpressionOption
import info.downdetector.bigdatascanner.common.engine.IKotlinMatcher

object Address : IHyperMatcher, IKotlinMatcher {
    override val javaPatterns = listOf(
        """(г\.|р-н|обл\.|ул\.|гор\.).{4,70}(д\.|дом)"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns = listOf(
        """(г\.|р-н|обл\.)[а-я ,.-]{4,55}(д\.|дом)""",
        """(ул\.|гор\.)[а-я ,.-]{4,55}(д\.|дом)"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

}

