package info.downdetector.bigdatascanner.common.functions

interface IHyperPattern {
    val javaPattern: String
    val hyperPatterns: List<String>
    val options: Set<ExpressionOption>
}