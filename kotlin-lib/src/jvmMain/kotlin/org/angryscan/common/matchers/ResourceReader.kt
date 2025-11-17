package org.angryscan.common.matchers

actual fun readResource(path: String): String? {
    return try {
        FullNameUS::class.java.classLoader
            ?.getResourceAsStream(path)
            ?.bufferedReader()
            ?.use { it.readText() }
    } catch (_: Exception) {
        null
    }
}

