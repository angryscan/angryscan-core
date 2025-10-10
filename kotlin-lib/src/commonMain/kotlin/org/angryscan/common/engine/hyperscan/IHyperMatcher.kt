package org.angryscan.common.engine.hyperscan

import org.angryscan.common.engine.ExpressionOption
import org.angryscan.common.engine.IMatcher

interface IHyperMatcher: IMatcher {
    /**
     * A list of hyper patterns that define the regular expressions to be used for matching.
     *
     * These patterns are used to detect and extract specific patterns from the input text.
     * The patterns are defined as strings and can include regular expression syntax.
     *
     * ### Example:
     * ```kotlin
     * override val hyperPatterns: List<String> = listOf(
     *     """\b[a-zA-Z0-9][a-zA-Z0-9._%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]+\.[a-zA-Z]{2,}\b"""
     * )
     * ```
     *
     * @see IMatcher for more information about pattern matching
     */
    val hyperPatterns: List<String>
    /**
     * A set of options that configure the behavior of the pattern matching.
     *
     * These options can modify how the pattern matching is performed, such as case sensitivity,
     * multiline matching, or other pattern-specific behaviors. The available options are defined
     * in the [org.angryscan.common.engine.ExpressionOption] enum.
     *
     * ### Example:
     * ```kotlin
     * override val options: Set<ExpressionOption> = setOf(
     *     ExpressionOption.CASE_INSENSITIVE,
     *     ExpressionOption.MULTILINE
     * )
     * ```
     *
     * @see org.angryscan.common.engine.ExpressionOption for the list of available options
     */
    val expressionOptions: Set<ExpressionOption>
}