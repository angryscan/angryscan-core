package info.downdetector.bigdatascanner.common.functions

interface IHyperPattern {
    val hyperPatterns: List<String>
    val options: Set<ExpressionOption>
    fun check(value: String): Boolean
}