package org.angryscan.common.engine

interface IMatcher {
    /**
     * This function checks if the given value is correct.
     * @param value the value to check
     * @return true if the value is correct, false otherwise
     */
    fun check(value: String): Boolean
}