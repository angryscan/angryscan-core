package info.downdetector.bigdatascanner.common.engine

interface IHyperMatcher: IMatcher {
    val hyperPatterns: List<String>
    /**
     * A set of options that configure the behavior of the pattern matching.
     *
     * These options can modify how the pattern matching is performed, such as case sensitivity,
     * multiline matching, or other pattern-specific behaviors. The available options are defined
     * in the [ExpressionOption] enum.
     *
     * ### Example:
     * ```kotlin
     * override val options: Set<ExpressionOption> = setOf(
     *     ExpressionOption.CASE_INSENSITIVE,
     *     ExpressionOption.MULTILINE
     * )
     * ```
     *
     * @see ExpressionOption for the list of available options
     */
    val expressionOptions: Set<ExpressionOption>
}