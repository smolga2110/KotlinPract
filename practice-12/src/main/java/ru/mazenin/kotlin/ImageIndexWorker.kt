package ru.mazenin.kotlin

import android.content.Context
import androidx.work.*
import java.io.File
import java.security.MessageDigest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class ImageIndexWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result =
        withContext(Dispatchers.IO) {
            try {
                val folder = File(applicationContext.filesDir, "images")
                val files =
                    folder
                        .listFiles()
                        ?.filter { it.extension in listOf("png", "jpg") }
                        ?.sortedBy { it.name } ?: emptyList()
                val entries = JSONArray()
                files.forEach { file ->
                    val digest = MessageDigest.getInstance("SHA-256")
                    file.inputStream().use { input ->
                        val buffer = ByteArray(8192)
                        while (true) {
                            val n = input.read(buffer)
                            if (n < 0) break
                            digest.update(buffer, 0, n)
                        }
                    }
                    entries.put(
                        JSONObject()
                            .put("name", file.name)
                            .put("bytes", file.length())
                            .put("sha256", digest.digest().joinToString("") { "%02x".format(it) })
                    )
                }
                val report =
                    JSONObject()
                        .put("createdAt", java.time.Instant.now().toString())
                        .put("images", entries)
                File(applicationContext.filesDir, "image-index.json").writeText(report.toString(2))
                Result.success(workDataOf("count" to files.size, "file" to "image-index.json"))
            } catch (e: Exception) {
                Result.failure(workDataOf("error" to (e.message ?: "Ошибка создания индекса")))
            }
        }
}
