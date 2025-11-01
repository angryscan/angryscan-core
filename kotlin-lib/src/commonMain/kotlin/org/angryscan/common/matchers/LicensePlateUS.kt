package org.angryscan.common.matchers

import kotlinx.serialization.Serializable
import org.angryscan.common.engine.hyperscan.IHyperMatcher
import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.kotlin.IKotlinMatcher

@Serializable
object LicensePlateUS : IHyperMatcher, IKotlinMatcher {
    override val name = "License Plate US"
    override val javaPatterns = listOf(
        """(?i)(plate|license|lic|tag|vehicle|car|registration|reg)[\s\-:]{0,5}([A-Z]{2,3}[\s\-]?[0-9]{3,4}|[0-9]{2,3}[\s\-]?[A-Z]{2,3}|[0-9][A-Z]{2,3}[\s\-]?[0-9]{3}|[A-Z]{3}[0-9]{3}|[A-Z]{2}[\s\-]?[0-9]{4,5})(?=\W|$)""",
        """\b([A-Z]{2,3}[\s\-]?[0-9]{3,4}|[0-9]{2,3}[\s\-]?[A-Z]{2,3}|[0-9][A-Z]{2,3}[\s\-]?[0-9]{3}|[A-Z]{3}[0-9]{3}|[A-Z]{2}[\s\-]?[0-9]{4,5})\b"""
    )
    override val regexOptions = setOf(
        RegexOption.IGNORE_CASE,
        RegexOption.MULTILINE
    )

    override val hyperPatterns: List<String> = listOf(
        """(?i)(plate|license|lic|tag|vehicle|car|registration|reg)[\s\-:]{0,5}([A-Z]{2,3}[\s\-]?[0-9]{3,4}|[0-9]{2,3}[\s\-]?[A-Z]{2,3}|[0-9][A-Z]{2,3}[\s\-]?[0-9]{3}|[A-Z]{3}[0-9]{3}|[A-Z]{2}[\s\-]?[0-9]{4,5})\b""",
        """\b([A-Z]{2,3}[\s\-]?[0-9]{3,4}|[0-9]{2,3}[\s\-]?[A-Z]{2,3}|[0-9][A-Z]{2,3}[\s\-]?[0-9]{3}|[A-Z]{3}[0-9]{3}|[A-Z]{2}[\s\-]?[0-9]{4,5})\b"""
    )
    override val expressionOptions = setOf(
        ExpressionOption.MULTILINE,
        ExpressionOption.CASELESS,
        ExpressionOption.UTF8
    )

    override fun check(value: String): Boolean = true

    override fun toString() = name
}

