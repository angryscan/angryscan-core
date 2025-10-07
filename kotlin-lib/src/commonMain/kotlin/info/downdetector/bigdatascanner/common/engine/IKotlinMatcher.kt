package info.downdetector.bigdatascanner.common.engine

interface IKotlinMatcher: IMatcher {
    val javaPatterns: List<String>
    val regexOptions: Set<RegexOption>
}