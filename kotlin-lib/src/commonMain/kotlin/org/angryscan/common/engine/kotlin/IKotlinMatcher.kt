package org.angryscan.common.engine.kotlin

import org.angryscan.common.engine.IMatcher

interface IKotlinMatcher: IMatcher {
    val javaPatterns: List<String>
    val regexOptions: Set<RegexOption>
}