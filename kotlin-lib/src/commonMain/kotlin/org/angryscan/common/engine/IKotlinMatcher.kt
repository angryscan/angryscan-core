package org.angryscan.common.engine

interface IKotlinMatcher: IMatcher {
    val javaPatterns: List<String>
    val regexOptions: Set<RegexOption>
}