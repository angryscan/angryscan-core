package org.angryscan.common.matchers

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen

@OptIn(ExperimentalForeignApi::class)
actual fun readResource(path: String): String? {
    return try {
        val file = fopen(path, "r") ?: return null
        val buffer = StringBuilder()
        val lineBuffer = ByteArray(1024)
        
        while (fgets(lineBuffer.refTo(0), lineBuffer.size, file) != null) {
            buffer.append(lineBuffer.toKString())
        }
        
        fclose(file)
        buffer.toString()
    } catch (_: Exception) {
        null
    }
}

