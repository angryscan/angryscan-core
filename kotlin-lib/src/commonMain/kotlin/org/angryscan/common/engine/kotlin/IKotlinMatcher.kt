package org.angryscan.common.engine.kotlin

import org.angryscan.common.engine.IMatcher

interface IKotlinMatcher: IMatcher {
    val javaPatterns: List<String>
    
    /**
     * Returns Java patterns with optional keyword requirement.
     * By default, returns [javaPatterns]. Can be overridden by matchers that support
     * optional keywords.
     *
     * @param requireKeywords If true, keywords are required (default). If false, keywords are optional.
     * @return List of Java patterns
     */
    fun getJavaPatterns(requireKeywords: Boolean = true): List<String> = javaPatterns
    
    val regexOptions: Set<RegexOption>
}