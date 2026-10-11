package ru.mazenin.kotlin

import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import java.util.concurrent.*

object ImagePolicy {
    fun validate(value: String): String {
        val text = value.trim()
        val uri = runCatching { URI(text) }.getOrNull()
        require(uri != null && uri.scheme in listOf("https", "http") && !uri.host.isNullOrBlank()) {
            "Введите корректную HTTP(S) ссылку"
        }
        return text
    }

    fun fileName(bytes: ByteArray, id: Long): String {
        val png =
            bytes.size >= 8 &&
                bytes.take(8) == listOf(137, 80, 78, 71, 13, 10, 26, 10).map { it.toByte() }
        val jpeg =
            bytes.size >= 3 &&
                bytes[0] == 0xff.toByte() &&
                bytes[1] == 0xd8.toByte() &&
                bytes[2] == 0xff.toByte()
        require(png || jpeg) { "Сервер вернул не PNG/JPEG изображение" }
        return "image_$id.${if(png) "png" else "jpg"}"
    }
}

data class DownloadResult(
    val file: File,
    val bytes: Int,
    val networkThread: String,
    val diskThread: String,
)

class ImageDownloader(
    private val directory: File,
    private val fetch: (String) -> ByteArray = ::fetchImage,
) {
    private val network = Executors.newSingleThreadExecutor { r -> Thread(r, "Network") }
    private val disk = Executors.newSingleThreadExecutor { r -> Thread(r, "Disk") }

    fun download(url: String, callback: (Result<DownloadResult>) -> Unit) {
        val validated = runCatching { ImagePolicy.validate(url) }
        if (validated.isFailure) {
            callback(Result.failure(validated.exceptionOrNull()!!))
            return
        }
        // Both named executors start on the click. Disk awaits the Network result.
        val task =
            network.submit(
                Callable { Pair(fetch(validated.getOrThrow()), Thread.currentThread().name) }
            )
        disk.execute {
            callback(
                runCatching {
                    val (bytes, networkName) = task.get(35, TimeUnit.SECONDS)
                    require(directory.exists() || directory.mkdirs()) {
                        "Не удалось создать каталог"
                    }
                    val file = File(directory, ImagePolicy.fileName(bytes, System.nanoTime()))
                    file.outputStream().use { it.write(bytes) }
                    DownloadResult(file, bytes.size, networkName, Thread.currentThread().name)
                }
            )
        }
    }

    fun close() {
        network.shutdownNow()
        disk.shutdownNow()
    }
}

fun fetchImage(url: String): ByteArray {
    val connection = URI(url).toURL().openConnection() as HttpURLConnection
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    try {
        require(connection.responseCode in 200..299) { "HTTP ${connection.responseCode}" }
        return connection.inputStream.use { input ->
            val output = java.io.ByteArrayOutputStream()
            val buffer = ByteArray(8192)
            var total = 0
            while (true) {
                val n = input.read(buffer)
                if (n < 0) break
                total += n
                require(total <= 10 * 1024 * 1024) { "Изображение больше 10 МБ" }
                output.write(buffer, 0, n)
            }
            output.toByteArray()
        }
    } finally {
        connection.disconnect()
    }
}
