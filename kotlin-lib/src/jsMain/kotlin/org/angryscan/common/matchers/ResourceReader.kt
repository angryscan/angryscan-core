package org.angryscan.common.matchers

actual fun readResource(path: String): String? {
    return try {
        val fs = js("require('fs')")
        val pathModule = js("require('path')")
        val resourcePath = pathModule.join(js("__dirname"), "../../../../resources", path)
        fs.readFileSync(resourcePath, "utf8") as? String
    } catch (_: dynamic) {
        null
    }
}

