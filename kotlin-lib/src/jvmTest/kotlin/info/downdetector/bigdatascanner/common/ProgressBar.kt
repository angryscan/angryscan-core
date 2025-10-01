package info.downdetector.bigdatascanner.common

class ProgressBar(private val total: Int, private val prefix: String = "Progress") {

    fun update(current: Int) {
        val percent = (current * 100 / total)
        val progressBar = buildString {
            append("$prefix: [")
            repeat(50) { index ->
                append(if (index * 2 <= percent) "▰" else "▱")
            }
            append("] $percent%")
        }
        print("\r$progressBar")
        System.out.flush()

        if (current == total) {
            println() // Завершаем строку
        }
    }
}