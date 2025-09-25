package info.downdetector.bigdatascanner.common.functions

import info.downdetector.bigdatascanner.common.extensions.regexDetector

object Email: IHyperPattern {
    override val javaPattern: String = """(?<=[-, ()=*]|^)[a-zA-Z0-9_.+-]+@[a-z0-9-.]+?(\.[a-z]{2,})+(?=\W|$)"""
    override val hyperPatterns: List<String> = listOf(
        """\b[a-zA-Z0-9][a-zA-Z0-9._%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]+\.[a-zA-Z]{2,}\b"""
    )
    override val options: Set<ExpressionOption> = setOf(
        ExpressionOption.MULTILINE
    )
    fun findEmails(text: String, withContext: Boolean) = regexDetector(
        text,
        javaPattern.toRegex(setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)),
        withContext
    )
}
